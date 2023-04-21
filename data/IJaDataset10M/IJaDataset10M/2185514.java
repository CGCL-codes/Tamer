package com.google.checkout.sdk.notifications;

import com.google.checkout.sdk.domain.OrderSummary;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Uses a JDBC-enabled database for transactionality.
 *
 * It assumes that there is a table with a column that contains the String
 * serial numbers as sent by Checkout, and whose other columns (if any) are
 * nullable; upon successful handling of a notification, this table has that
 * notification's serial number row INSERTed, null for the other columns.
 *
 * Because this loads serailNumberTableName rows for update, this will correctly
 * handle a repeated notification from Checkout, which may happen if responses
 * are not sufficiently timely.
 *
 */
public class NamedDatabaseNotificationDispatcher extends JDBCNotificationDispatcher {

    private final String serialNumberTableName;

    private final String serialNumberColumnName;

    /**
   * Creates a NotificationDispatcher that will use the databaseConnection
   * @param request The request object. It should not have been processed prior
   * to use in this method.
   * @param response The response object. It should not be processed prior or
   * post its use in this method.
   * @param databaseConnection A connection to the database. It shouldn't have
   * any active transactions on it.
   * @param serialNumberTableName The name of a table which contains
   * {@code serialNumberColumnName} as a column. Any other columns must be nullable.
   * @param serialNumberColumnName The name of the column which holds serial numbers.
   */
    protected NamedDatabaseNotificationDispatcher(HttpServletRequest request, HttpServletResponse response, Connection databaseConnection, String serialNumberTableName, String serialNumberColumnName) {
        super(request, response, databaseConnection);
        this.serialNumberTableName = serialNumberTableName;
        this.serialNumberColumnName = serialNumberColumnName;
    }

    @Override
    protected boolean hasAlreadyHandled(String serialNumber, OrderSummary orderSummary, Notification notification) throws Exception {
        PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * " + "FROM " + serialNumberTableName + " " + "WHERE " + serialNumberColumnName + "= ? " + "FOR UPDATE");
        preparedStatement.setString(1, serialNumber);
        try {
            return preparedStatement.executeQuery().first();
        } finally {
            preparedStatement.close();
        }
    }

    @Override
    protected void rememberSerialNumber(String serialNumber, OrderSummary orderSummary, Notification notification) throws Exception {
        PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO " + serialNumberTableName + " ( " + serialNumberColumnName + " ) " + "VALUES ( ? )");
        preparedStatement.setString(1, serialNumber);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
}
