package org.tripcom.distribution.h2DB;

import org.tripcom.distribution.entry.Triple;
import org.tripcom.integration.entry.TripleEntry;
import java.sql.*;
import org.h2.tools.Server;
import org.tripcom.integration.entry.SpaceURI;

public class H2Client {

    private Connection connection;

    public H2Client(String url) throws SQLException, ClassNotFoundException {
        this.connection = getConnection(url);
    }

    public void readTriplesFromDB() throws SQLException {
        Statement stat = this.connection.createStatement();
        ResultSet result = stat.executeQuery("SELECT * FROM TRIPLE;");
        while (result.next()) {
            System.out.println(result.getString("ID") + " " + result.getString("subject") + " " + result.getString("predicate") + " " + result.getString("object") + " " + result.getString("space") + " ");
        }
    }

    public void readSubjectsFromDB() throws SQLException {
        Statement stat = this.connection.createStatement();
        ResultSet result = stat.executeQuery("SELECT subject FROM TRIPLE;");
        while (result.next()) {
            System.out.println(result.getString(1));
        }
    }

    public void readTriplesForSubjects(String subject) throws SQLException {
        Statement stat = this.connection.createStatement();
        ResultSet result = stat.executeQuery("SELECT * FROM TRIPLE WHERE subject ='" + subject + "' ;");
        while (result.next()) {
            System.out.println(result.getString("ID") + " " + result.getString("subject") + " " + result.getString("predicate") + " " + result.getString("object") + " " + result.getString("space") + " ");
        }
    }

    public void readTriplesForObject(String object) throws SQLException {
        Statement stat = this.connection.createStatement();
        ResultSet result = stat.executeQuery("SELECT * FROM TRIPLE WHERE object ='" + object + "' ;");
        while (result.next()) {
            System.out.println(result.getString("ID") + " " + result.getString("subject") + " " + result.getString("predicate") + " " + result.getString("object") + " " + result.getString("space") + " ");
        }
    }

    public void readTriplesForPredicate(String predicate) throws SQLException {
        Statement stat = this.connection.createStatement();
        ResultSet result = stat.executeQuery("SELECT * FROM TRIPLE WHERE predicate ='" + predicate + "' ;");
        while (result.next()) {
            System.out.println(result.getString("ID") + " " + result.getString("subject") + " " + result.getString("predicate") + " " + result.getString("object") + " " + result.getString("space") + " ");
        }
    }

    public void readTriplesForSpace(String space) throws SQLException {
        Statement stat = this.connection.createStatement();
        ResultSet result = stat.executeQuery("SELECT * FROM TRIPLE WHERE space ='" + space + "' ;");
        while (result.next()) {
            System.out.println(result.getString("ID") + " " + result.getString("subject") + " " + result.getString("predicate") + " " + result.getString("object") + " " + result.getString("space") + " ");
        }
    }

    public void readObjectsFromDB() throws SQLException {
        Statement stat = this.connection.createStatement();
        ResultSet result = stat.executeQuery("SELECT object FROM TRIPLE;");
        while (result.next()) {
            System.out.println(result.getString(1));
        }
    }

    public void readPredicatesFromDB() throws SQLException {
        Statement stat = this.connection.createStatement();
        ResultSet result = stat.executeQuery("SELECT predicate FROM TRIPLE;");
        while (result.next()) {
            System.out.println(result.getString(1));
        }
    }

    public void readSpacesFromDB() throws SQLException {
        Statement stat = this.connection.createStatement();
        ResultSet result = stat.executeQuery("SELECT space FROM TRIPLE;");
        while (result.next()) {
            System.out.println(result.getString(1));
        }
    }

    public void createTable() throws SQLException {
        Statement stat = this.connection.createStatement();
        String query = "DROP TABLE IF EXISTS TRIPLE;" + "CREATE TABLE TRIPLE(ID INT AUTO_INCREMENT PRIMARY KEY, subject VARCHAR(255),  predicate VARCHAR(255), object VARCHAR(255), space VARCHAR(255) );";
        boolean srs = stat.execute(query);
    }

    public boolean insertIntoDB(Triple triple, SpaceURI space) throws SQLException {
        Statement stat = this.connection.createStatement();
        String query = "INSERT INTO TRIPLE (ID,subject,predicate,object,space) VALUES(" + null + ",'" + triple.getSubject() + "','" + triple.getPredicate() + "','" + triple.getObject() + "','" + space.getRootSpace() + "');";
        return stat.execute(query);
    }

    public Connection getConnection(String url) throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:" + url, "sa", "");
        return conn;
    }
}
