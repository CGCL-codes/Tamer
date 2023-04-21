package com.maschinenstuermer.profiler.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HsqlApp {

    private Connection connection;

    public static void main(String[] args) throws Exception {
        HsqlApp hsqlApp = new HsqlApp();
        hsqlApp.run();
    }

    public void run() throws Exception, SQLException {
        execute("create table EMPLOYEE (id INTEGER IDENTITY, surname VARCHAR(255), forename VARCHAR(255), age INT);");
        executeUpdate("insert into EMPLOYEE (surname, forename, age) VALUES('Müller', 'Hans', 38);");
        ResultSet resultSet = executeQuery("select surname, forename, age from EMPLOYEE;");
        while (resultSet.next()) {
            String surname = resultSet.getString("surname");
            String forename = resultSet.getString("forename");
            int age = resultSet.getInt("age");
            System.out.println(surname + ", " + forename + ", age=" + age);
        }
        PreparedStatement preparedStatement = prepareStatement("select surname, forename, age from EMPLOYEE;");
        preparedStatement.execute();
    }

    public HsqlApp() throws Exception {
        Class.forName("org.hsqldb.jdbcDriver");
        connection = DriverManager.getConnection("jdbc:hsqldb:mem:testdb;shutdown=true", "sa", "");
    }

    private boolean execute(String sql) throws Exception {
        Statement statement = connection.createStatement();
        boolean result = statement.execute(sql);
        return result;
    }

    private ResultSet executeQuery(String sql) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql);
        return result;
    }

    public int executeUpdate(String sql) throws Exception {
        Statement statement = connection.createStatement();
        int result = statement.executeUpdate(sql);
        return result;
    }

    public PreparedStatement prepareStatement(String sql) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        return preparedStatement;
    }
}
