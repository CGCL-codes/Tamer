package com.etc.controller;

import com.etc.db.oracle.Connector;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.GregorianCalendar;
import org.apache.log4j.Logger;

/**
 *
 * @author magicbank
 */
public class ProductTransactionController {

    public static Logger log4j = Logger.getLogger(ProductTransactionController.class);

    public static final int IN = 0;

    public static final int OUT = 1;

    public static void in(GregorianCalendar time, String lotno, BigDecimal qty, int activity, String code) {
        Connection conn = Connector.getConnection();
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("INSERT INTO LOG_PRODUCT (ID, TIME, LOT, QTY, AID, TYPE, CODE) VALUES (" + "'', " + "'" + time.getTimeInMillis() + "', " + "'" + lotno + "', " + "'" + qty + "', " + "'" + activity + "', " + "'" + ProductTransactionController.IN + "', " + "'" + code + "'" + ")");
            stm.close();
        } catch (SQLException ex) {
            log4j.error("SQL Exception.", ex);
        }
        Connector.close(conn);
    }

    public static void out(GregorianCalendar time, String lotno, BigDecimal qty, int activity, String code) {
        Connection conn = Connector.getConnection();
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("INSERT INTO LOG_PRODUCT (ID, TIME, LOT, QTY, AID, TYPE, CODE) VALUES (" + "'', " + "'" + time.getTimeInMillis() + "', " + "'" + lotno + "', " + "'" + qty + "', " + "'" + activity + "', " + "'" + ProductTransactionController.OUT + "', " + "'" + code + "'" + ")");
            stm.close();
        } catch (SQLException ex) {
            log4j.error("SQL Exception.", ex);
        }
        Connector.close(conn);
    }

    public static void updateReferenceCode(String newcode, String currentcode) {
        Connection conn = Connector.getConnection();
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("UPDATE LOG_PRODUCT SET " + "CODE = '" + newcode + "' " + "WHERE " + "CODE LIKE '" + currentcode + "'");
            stm.close();
        } catch (SQLException ex) {
            log4j.error("SQL Exception.", ex);
        }
        Connector.close(conn);
    }
}
