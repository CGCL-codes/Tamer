package examples;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;
import net.nothinginteresting.datamappers2.DatamappersException;
import net.nothinginteresting.datamappers2.UnitOfWork;
import net.nothinginteresting.datamappers2.query.Condition;
import net.nothinginteresting.datamappers2.query.Criteria;
import net.nothinginteresting.datamappers2.rdbms.sql.OracleRDBMS;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import example.datamappers2.test.Customer;
import example.datamappers2.test.User;

/**
 * @author Dmitriy Gorbenko
 * 
 */
public class QueryExample {

    private static final Logger logger = Logger.getLogger(QueryExample.class);

    public static void main(String[] args) throws DatamappersException {
        ExampleSchemaCreator exampleSchemaCreator = new ExampleSchemaCreator();
        exampleSchemaCreator.createExampleSchema();
        QueryExample queryExample = new QueryExample();
        queryExample.createQueries();
    }

    public void createQueries() throws DatamappersException {
        BasicConfigurator.configure();
        UnitOfWork.begin(new OracleRDBMS(createConnection()));
        Condition<User> condition = Condition.and(Criteria.equals(User.id, User.Department().id), Criteria.equals(User.login, "aaa"));
        List<Customer> customers = UnitOfWork.findAll(Customer.class, condition);
        for (Customer customer : customers) {
            logger.debug("customer = " + customer.toString());
        }
        UnitOfWork.commit();
    }

    private Connection createConnection() {
        try {
            Class<?> cls = Class.forName("oracle.jdbc.OracleDriver");
            Driver driver = (Driver) (cls.newInstance());
            DriverManager.registerDriver(driver);
            Properties connProps = new Properties();
            connProps.setProperty("user", "netcon");
            connProps.setProperty("password", "q");
            return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ten", connProps);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
        return null;
    }
}
