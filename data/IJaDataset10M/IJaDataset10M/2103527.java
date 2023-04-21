package net.sf.sql2java;

import net.sf.sql2java.common.beans.DatabaseConnection;
import net.sf.sql2java.data.holders.Database;
import net.sf.sql2java.fetching.sql.SQLFetcher;

/**
 *
 * @author annba
 */
public class DatabaseReaderTester {

    /** Creates a new instance of DatabaseReaderTester 
     * @param args 
     * 
     */
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        try {
            DatabaseConnection connection = new DatabaseConnection();
            connection.setDriverClass("com.mysql.jdbc.Driver");
            connection.setUrl("jdbc:mysql://localhost:3307/demobig");
            connection.setUsername("jpetstore");
            connection.setPassword("jpetstore");
            SQLFetcher fetcher = new SQLFetcher();
            fetcher.setConnection(connection);
            ApplicationContext.getInstance().setFetcher(fetcher);
            Database database = ApplicationContext.getInstance().getFetcher().fetch();
            System.in.read();
            ApplicationContext.getInstance().getGenerator().generate(database);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        long duration = end - start;
        System.out.println("Duration = " + duration + "ms.");
    }
}
