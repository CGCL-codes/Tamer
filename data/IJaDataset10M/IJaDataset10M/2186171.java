package org.openmoney.db;

import java.sql.*;
import java.util.*;
import org.openmoney.util.*;

/** Represents a Acks object, as stored in the database. An Acks object consists
 * of the following fields:
 * AckDateTime - the time of the transaction, in UTC
 * SourceUserId - the user who the credits came from
 * DestUserId - the user the funds are going to
 * AckCurrencyId - the id of the currency that the transaction is performed in
 * AckAmount - the amount of the transaction
 * AckItem - the description of the transfer
 * SourceBalance - the balance of the source user *after* trade is completed
 * DestBalance - the balance of the destination user *after* trade is completed
 *
 * The Schema is derived from Richard K Moore's original CyberCredits program.
 *@author Reuben Firmin
 */
public class Acks {

    private java.util.Date _ackDateTime;

    private int _sourceUserId;

    private int _destUserId;

    private String _ackCurrencyId;

    private int _ackAmount;

    private String _ackItem;

    private int _sourceBalance;

    private int _destBalance;

    /**
     * Get all acks which involve a user in the specified currency
	 */
    public static Acks[] getAcks(int userId, String currencyId) throws SQLException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        ResultSet rs = connection.query("SELECT AckDateTime, SourceUserId, DestUserId, AckCurrencyId, " + " AckAmount, AckItem, SourceBalance, DestBalance " + "FROM Acks " + "WHERE SourceUserId=" + userId + " " + "OR DestUserId=" + userId);
        Vector acks = new Vector();
        while (rs.next()) {
            java.util.Date ackDateTime = rs.getDate("AckDateTime");
            int sourceUserId = rs.getInt("SourceUserId");
            int destUserId = rs.getInt("DestUserId");
            String ackCurrencyId = rs.getString("AckCurrencyId");
            int ackAmount = rs.getInt("AckAmount");
            String ackItem = rs.getString("AckItem");
            int sourceBalance = rs.getInt("SourceBalance");
            int destBalance = rs.getInt("DestBalance");
            Acks ack = new Acks(ackDateTime, sourceUserId, destUserId, ackCurrencyId, ackAmount, ackItem, sourceBalance, destBalance);
            acks.addElement(ack);
        }
        rs.close();
        connection.close();
        return (Acks[]) acks.toArray(new Acks[0]);
    }

    public Acks(java.util.Date ackDateTime, int sourceUserId, int destUserId, String ackCurrencyId, int ackAmount, String ackItem, int sourceBalance, int destBalance) {
        _ackDateTime = ackDateTime;
        _sourceUserId = sourceUserId;
        _destUserId = destUserId;
        _ackCurrencyId = ackCurrencyId;
        _ackAmount = ackAmount;
        _ackItem = ackItem;
        _sourceBalance = sourceBalance;
        _destBalance = destBalance;
    }

    public boolean create() throws SQLException {
        boolean valid = false;
        Connection connection = ConnectionPool.getInstance().getConnection();
        try {
            int i = connection.update("INSERT into Acks (AckDateTime, SourceUserId, DestUserId, " + "AckCurrencyId, AckAmount, AckItem, SourceBalance, " + "DestBalance) " + "VALUES(CURRENT_TIMESTAMP," + _sourceUserId + "," + _destUserId + ",\"" + _ackCurrencyId + "\"," + _ackAmount + ",\"" + _ackItem + "\"," + _sourceBalance + "," + _destBalance + ")");
            valid = (i == 1);
        } catch (SQLException e) {
            if (!SQL.isPrimaryKeyException(e)) throw e;
        } finally {
            connection.close();
        }
        return valid;
    }

    public java.util.Date getAckDateTime() {
        return _ackDateTime;
    }

    public int getSourceUserId() {
        return _sourceUserId;
    }

    public int getDestUserId() {
        return _destUserId;
    }

    public String getAckCurrencyId() {
        return _ackCurrencyId;
    }

    public int getAckAmount() {
        return _ackAmount;
    }

    public String getAckItem() {
        return _ackItem;
    }

    public int getSourceBalance() {
        return _sourceBalance;
    }

    public int getDestBalance() {
        return _destBalance;
    }
}
