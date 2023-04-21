package org.sensorweb.util;

import java.sql.*;
import java.math.BigInteger;
import org.sensorweb.core.ObjectFactory;

/**
 * @author Xingchen Chu
 * @version 0.1
 *
 * <code> IDGenerator </code>
 */
public final class IDGenerator {

    private static final ObjectFactory context = ObjectFactory.newInstance();

    private static final String GEN_ID = "select id from id_table";

    private static final String UPDATE_ID = "update id_table set id=?";

    private static Connection connection;

    public IDGenerator() {
        try {
            System.err.println("in IDGenerator");
            init();
        } catch (Exception e) {
            throw new RuntimeException("failed to init IDGenerator due to " + e.getMessage());
        }
    }

    private static int id = 1;

    public static int nextId() {
        return id++;
    }

    private void init() throws Exception {
        System.err.println("in IDGenerator init()");
        String url = context.getProperty("db.url");
        String driver = context.getProperty("db.driver");
        String username = context.getProperty("db.username");
        String password = context.getProperty("db.password");
        Class.forName(driver);
        if (connection == null) connection = DriverManager.getConnection(url, username, password);
    }

    public BigInteger gen() {
        try {
            BigInteger result = BigInteger.valueOf(id);
            PreparedStatement ps = connection.prepareStatement(GEN_ID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = BigInteger.valueOf(Long.parseLong(rs.getString(1)));
                ps = connection.prepareStatement(UPDATE_ID);
                ps.setString(1, String.valueOf(result.longValue() + 1));
                ps.execute();
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void main(String[] args) {
        IDGenerator id = new IDGenerator();
        System.out.println(id.gen());
        System.out.println(id.gen());
        System.out.println(id.gen());
        System.out.println(id.gen());
    }
}
