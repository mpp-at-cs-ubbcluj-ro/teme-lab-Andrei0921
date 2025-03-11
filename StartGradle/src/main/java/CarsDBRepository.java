import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CarsDBRepository implements CarRepository{

    private JdbcUtils dbUtils;



    private static final Logger logger= LogManager.getLogger();

    public CarsDBRepository(Properties props) {
        logger.info("Initializing CarsDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public List<Car> findByManufacturer(String manufacturerN) {
        logger.traceEntry();
        Connection conn= dbUtils.getConnection();
        List<Car>cars= new ArrayList<>();
        try(PreparedStatement preStmt=conn.prepareStatement("Select * from cars where manufacturer=?")){
            preStmt.setString(1,manufacturerN);
            try(ResultSet rs=preStmt.executeQuery()){
                while(rs.next()){
                    int id=rs.getInt("id");
                    String manufacturer=rs.getString("manufacturer");
                    String model=rs.getString("model");
                    int year=rs.getInt("year");
                    Car car=new Car(manufacturer,model,year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        }catch (SQLException e){
            logger.error(e);
            System.err.println("Error Db"+e);
        }
        return cars;


    }

    @Override
    public List<Car> findBetweenYears(int min, int max) {
        logger.traceEntry();
        Connection conn= dbUtils.getConnection();
        List<Car>cars= new ArrayList<>();
        try(PreparedStatement preStmt=conn.prepareStatement("Select * from cars where year BETWEEN ? AND ?")){
           preStmt.setInt(1, min);
           preStmt.setInt(2, max);
            try(ResultSet rs=preStmt.executeQuery()){
                while(rs.next()){
                    int id=rs.getInt("id");
                    String manufacturer=rs.getString("manufacturer");
                    String model=rs.getString("model");
                    int year=rs.getInt("year");
                    Car car=new Car(manufacturer,model,year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        }catch (SQLException e){
            logger.error(e);
            System.err.println("Error Db"+e);
        }
        return cars;


    }

    @Override
    public void add(Car elem) {
        logger.traceEntry("saving task {}",elem);
        Connection conn= dbUtils.getConnection();
        try (PreparedStatement preStmt= conn.prepareStatement("insert into cars (manufacturer, model,year)values(?,?,?)")){
            preStmt.setString(1,elem.getManufacturer());
            preStmt.setString(2, elem.getModel());
            preStmt.setInt(3,elem.getYear());
            int result= preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);

        }catch (SQLException e){
            logger.error(e);
            System.err.println("Error Db"+e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Car elem) {
        logger.traceEntry("saving task {}",elem);
        Connection conn= dbUtils.getConnection();
        try (PreparedStatement preStmt= conn.prepareStatement("update cars set manufacturer=?,model=?,year=? where id=? ")){
            preStmt.setString(1,elem.getManufacturer());
            preStmt.setString(2, elem.getModel());
            preStmt.setInt(3,elem.getYear());
            preStmt.setInt(4,integer);
            int result= preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);

        }catch (SQLException e){
            logger.error(e);
            System.err.println("Error Db"+e);
        }
        logger.traceExit();
    }

    @Override
    public Iterable<Car> findAll() {
         logger.traceEntry();
         Connection conn= dbUtils.getConnection();
         List<Car>cars= new ArrayList<>();
         try(PreparedStatement preStmt=conn.prepareStatement("Select * from cars")){
             try(ResultSet rs=preStmt.executeQuery()){
                 while(rs.next()){
                     int id=rs.getInt("id");
                     String manufacturer=rs.getString("manufacturer");
                     String model=rs.getString("model");
                     int year=rs.getInt("year");
                     Car car=new Car(manufacturer,model,year);
                     car.setId(id);
                     cars.add(car);
                 }
             }
         }catch (SQLException e){
             logger.error(e);
             System.err.println("Error Db"+e);
         }
	return cars;

    }
}
