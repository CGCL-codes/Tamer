package com.google.code.ptrends.services.implementations.dataservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.apache.log4j.Logger;
import com.google.code.ptrends.entities.PriceRecord;

class PriceServiceImpl extends DataServiceBaseImpl {

    private static final Logger LOG = Logger.getLogger(PriceServiceImpl.class);

    protected PriceServiceImpl(Connection connection) {
        super(connection);
    }

    public PriceRecord findLastPrice(final int itemID, final int SupplierID) throws SQLException {
        PriceRecord record = null;
        final String sqlText = "SELECT * FROM prices " + "WHERE item_id = ? and supplier_id = ? ORDER BY price_date DESC";
        PreparedStatement findItem = null;
        ResultSet resultSet = null;
        SQLException error = null;
        try {
            findItem = connection.prepareStatement(sqlText);
            findItem.setMaxRows(1);
            findItem.setInt(1, itemID);
            findItem.setInt(2, SupplierID);
            resultSet = findItem.executeQuery();
            if (resultSet.next()) {
                record = new PriceRecord();
                record.setItemID(resultSet.getInt("item_id"));
                record.setSupplierID(resultSet.getInt("supplier_id"));
                record.setPrice(resultSet.getFloat("price"));
                record.setPriceDate(resultSet.getDate("price_date"));
            }
        } catch (SQLException e) {
            LOG.error("Error finding last price", e);
            error = e;
        } finally {
            close(resultSet, findItem);
        }
        if (error != null) {
            throw error;
        }
        return record;
    }

    public PriceRecord findPrice(final int itemID, final int supplierID, final Date priceDate) throws SQLException {
        final String query = "SELECT * FROM prices " + "WHERE item_id = ? and supplier_id = ? and price_date = ?";
        PriceRecord pr = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        SQLException sqlex = null;
        try {
            ps = connection.prepareStatement(query);
            ps.setMaxRows(1);
            ps.setInt(1, itemID);
            ps.setInt(2, supplierID);
            java.sql.Date date = new java.sql.Date(priceDate.getTime());
            ps.setDate(3, date);
            rs = ps.executeQuery();
            if (rs.next()) {
                pr = new PriceRecord();
                pr.setItemID(rs.getInt("item_id"));
                pr.setPrice(rs.getFloat("price"));
                pr.setPriceDate(rs.getDate("price_date"));
                pr.setSupplierID(rs.getInt("supplier_id"));
            }
        } catch (SQLException e) {
            LOG.error("Error finding price from date", e);
            sqlex = e;
        } finally {
            close(rs, ps);
        }
        if (sqlex != null) {
            throw sqlex;
        }
        return pr;
    }

    public boolean savePrice(final PriceRecord priceRecord) throws SQLException {
        if (priceRecord == null) {
            throw new IllegalArgumentException("Illegal null-reference savePrice PriceRecord");
        }
        boolean result = false;
        final int itemID = priceRecord.getItemID();
        final int supplierID = priceRecord.getSupplierID();
        final Date utilDate = priceRecord.getPriceDate();
        final java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        final String sqlText = "INSERT INTO prices (item_id, supplier_id, price, price_date) " + "VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;
        SQLException error = null;
        try {
            preparedStatement = connection.prepareStatement(sqlText);
            preparedStatement.setInt(1, itemID);
            preparedStatement.setInt(2, supplierID);
            preparedStatement.setFloat(3, priceRecord.getPrice());
            preparedStatement.setDate(4, sqlDate);
            preparedStatement.executeUpdate();
            updateItemVersion(itemID, utilDate);
            result = true;
        } catch (SQLException e) {
            LOG.error("Problem with Price inserting", e);
            error = e;
        } finally {
            closeStatement(preparedStatement);
        }
        if (error != null) {
            throw error;
        }
        return result;
    }

    public float updatePrice(final PriceRecord priceRecord, final float newValue) throws SQLException {
        if (priceRecord == null) {
            throw new IllegalArgumentException("Illegal null-reference updatePrice PriceRecord");
        } else if (newValue < 0) {
            throw new IllegalArgumentException("Illegal null updatePrice newValue");
        }
        float result = 0;
        final int itemID = priceRecord.getItemID();
        final int supplierID = priceRecord.getSupplierID();
        final Date utilDate = priceRecord.getPriceDate();
        final java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        final String resultQuery = "SELECT * FROM prices " + "WHERE item_id = ? AND supplier_id = ? AND price_date = ?";
        final String updateQuery = "UPDATE prices SET price = ? " + "WHERE item_id = ? AND supplier_id = ? AND price_date = ?";
        PreparedStatement preparedStatement = null;
        SQLException error = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(resultQuery);
            preparedStatement.setInt(1, itemID);
            preparedStatement.setInt(2, supplierID);
            preparedStatement.setDate(3, sqlDate);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getFloat("price");
            }
        } catch (SQLException e) {
            LOG.error("Problem with Price updating: cannot find old price value", e);
            error = e;
        } finally {
            close(resultSet, preparedStatement);
        }
        if (error != null) {
            throw error;
        }
        try {
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setFloat(1, newValue);
            preparedStatement.setInt(2, itemID);
            preparedStatement.setInt(3, supplierID);
            preparedStatement.setDate(4, sqlDate);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Problem with Price updating: cannot update price value", e);
            error = e;
        } finally {
            closeStatement(preparedStatement);
        }
        if (error != null) {
            throw error;
        }
        return result;
    }
}
