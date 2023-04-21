package net.sourceforge.customercare.client.server.helpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.sourceforge.customercare.client.server.exceptions.CustomerCareException;

/**
 * calculates a new id
 */
public class Calculator {

    public static Integer getNext(Connection cnn, String table, String col) throws CustomerCareException {
        Integer id = 1;
        try {
            Statement statement = cnn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT MAX(" + col + ") FROM " + table + ";");
            if (rs.next()) {
                id = rs.getInt(1);
                id++;
            }
        } catch (SQLException sqlEx) {
        }
        return id;
    }
}
