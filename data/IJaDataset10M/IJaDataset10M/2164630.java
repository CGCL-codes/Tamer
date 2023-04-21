package br.net.woodstock.rockframework.core.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class MSAccessTest {

    private static final String DB_FILE = "C:/Temp/access/BD_Demandas_HC.mdb";

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        String url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + DB_FILE + ";DriverID=22;READONLY=true}";
        Connection c = DriverManager.getConnection(url, "", "");
        return c;
    }

    public void xtest1() throws Exception {
        Connection c = this.getConnection();
        DatabaseMetaData databaseMetaData = c.getMetaData();
        ResultSet rsTables = databaseMetaData.getTables(null, null, null, null);
        if (rsTables.next()) {
            ResultSetMetaData resultSetMetaData = rsTables.getMetaData();
            int count = resultSetMetaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                System.out.printf("%-30s", resultSetMetaData.getColumnName(i));
            }
            System.out.printf("\n");
            do {
                for (int i = 1; i <= count; i++) {
                    System.out.printf("%-30s", rsTables.getString(i));
                }
                System.out.printf("\n");
            } while (rsTables.next());
        }
        c.close();
    }

    public void test2() throws Exception {
        Connection c = this.getConnection();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Tabela_GM");
        if (rs.next()) {
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int count = resultSetMetaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                System.out.printf("%-30s", resultSetMetaData.getColumnName(i));
            }
            System.out.printf("\n");
            do {
                for (int i = 1; i <= count; i++) {
                    System.out.printf("%-30s", rs.getString(i));
                }
                System.out.printf("\n");
            } while (rs.next());
        }
        System.out.println("Chamando de novo");
        stmt = c.createStatement();
        rs = stmt.executeQuery("SELECT * FROM Tabela_GM");
        if (rs.next()) {
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int count = resultSetMetaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                System.out.printf("%-30s", resultSetMetaData.getColumnName(i));
            }
            System.out.printf("\n");
            do {
                for (int i = 1; i <= count; i++) {
                    System.out.printf("%-30s", rs.getString(i));
                }
                System.out.printf("\n");
            } while (rs.next());
        }
        rs.close();
        stmt.close();
        c.close();
    }

    public static void main(final String[] args) {
        try {
            new MSAccessTest().test2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
