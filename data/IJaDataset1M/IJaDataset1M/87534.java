package com.google.code.ptrends.services.implementations.dataservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.google.code.ptrends.entities.ItemRecord;

/**
 * @author Daniel
 */
class FinderServiceImpl extends DataServiceBaseImpl {

    private static final Logger LOG = Logger.getLogger(FinderServiceImpl.class);

    private HashMap<String, Integer> parametersMap = null;

    private boolean refreshParametersMap = false;

    public FinderServiceImpl(Connection connection) {
        super(connection);
    }

    /**
	 * looks for item in database according passed parameters
	 * 
	 * @param model
	 *            item model
	 * @param categoryID
	 *            category identifier
	 * @return {@link com.google.code.ptrends.entities.ItemRecord} object or null if nothing found
	 * @throws SQLException
	 */
    public ItemRecord findItem(final String model, final int categoryID) throws SQLException {
        ItemRecord record = null;
        final String queryFindItem = "SELECT * FROM items " + "WHERE model = ? and category_id = ?";
        PreparedStatement findItem = null;
        ResultSet resultSet = null;
        SQLException error = null;
        try {
            findItem = connection.prepareStatement(queryFindItem);
            findItem.setString(1, model);
            findItem.setInt(2, categoryID);
            findItem.setMaxRows(1);
            resultSet = findItem.executeQuery();
            if (resultSet.next()) {
                record = new ItemRecord();
                record.setClassID(resultSet.getInt("class_id"));
                record.setItemID(resultSet.getInt("id"));
                record.setManufacturerID(resultSet.getInt("manufacturer_id"));
                record.setModel(resultSet.getString("model"));
            }
        } catch (SQLException e) {
            LOG.error("Error finding item price", e);
            error = e;
        } finally {
            close(resultSet, findItem);
        }
        if (error != null) {
            throw error;
        }
        return record;
    }

    /**
	 * looks for item in database according passed parameters
	 * @param model
	 * item model
	 * @return
	 * @return {@link com.google.code.ptrends.entities.ItemRecord} object or null if nothing found
	 * @throws SQLException
	 */
    public ItemRecord findItem(final String model) throws SQLException {
        ItemRecord ir = null;
        final String query = "select * from items where model = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        SQLException sqlex = null;
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, model);
            rs = ps.executeQuery();
            if (rs.next()) {
                ir = new ItemRecord();
                ir.setCategoryID(rs.getInt("category_id"));
                ir.setClassID(rs.getInt("class_id"));
                ir.setItemID(rs.getInt("id"));
                ir.setManufacturerID(rs.getInt("manufacturer_id"));
                ir.setModel(rs.getString("model"));
            }
        } catch (SQLException e) {
            LOG.error("Error finding item by model", e);
            sqlex = e;
        } finally {
            close(rs, ps);
        }
        if (sqlex != null) throw sqlex;
        return ir;
    }

    /**
	 * looks for class in CLASSES table
	 * 
	 * @param className
	 *            name of a class to find
	 * @return class` id in classes table or NOT_FOUND_ID
	 * @throws SQLException
	 */
    public int findClassID(String className) throws SQLException {
        String findClassQuery = "select * from classes where name = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(findClassQuery);
            ps.setString(1, className);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id"); else return NOT_FOUND_ID;
        } catch (SQLException e) {
            LOG.error("Error looking for class");
        } finally {
            close(rs, ps);
        }
        return NOT_FOUND_ID;
    }

    /**
	 * looks for manufacturer in MANUFACTURERS table
	 * 
	 * @param manufacturerName
	 *            name of manufacturer
	 * @return manufacturer id or NOT_FOUND_ID if not found
	 * @throws SQLException
	 */
    public int findManufacturerID(String manufacturerName) throws SQLException {
        final String queryExistManufacturer = "SELECT * FROM manufacturers " + "WHERE name = ?";
        PreparedStatement findItem = null;
        ResultSet resultSet = null;
        try {
            findItem = connection.prepareStatement(queryExistManufacturer);
            findItem.setString(1, manufacturerName);
            resultSet = findItem.executeQuery();
            if (resultSet.next()) return resultSet.getInt("id"); else return NOT_FOUND_ID;
        } catch (SQLException e) {
            LOG.error("Error finding item price", e);
        } finally {
            close(resultSet, findItem);
        }
        return NOT_FOUND_ID;
    }

    /**
	 * looks for supplier in SUPPLIERS table
	 * 
	 * @param supplierName
	 *            supplier`s name to find
	 * @return supplier`s id in SUPPLIERS table or NOT_FOUND_ID if not found
	 * @throws SQLException
	 */
    public int findSupplierID(String supplierName) throws SQLException {
        String query = "select * from suppliers where name = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        SQLException sqlex = null;
        int res = NOT_FOUND_ID;
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, supplierName);
            rs = ps.executeQuery();
            if (rs.next()) res = rs.getInt("id");
        } catch (SQLException e) {
            LOG.error("Error looking for supplier");
            sqlex = e;
        } finally {
            close(rs, ps);
        }
        if (sqlex != null) throw sqlex;
        return res;
    }

    /**
	 * looks for category in CATEGORIES table
	 * 
	 * @param categoryName
	 *            name of a category to look for
	 * @return id of category in CATEGORIES table of NOT_FOUND_ID if not found
	 * @throws SQLException
	 */
    public int findCategoryID(String categoryName) throws SQLException {
        String query = "select * from CATEGORIES where NAME = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        SQLException sqlex = null;
        int res = NOT_FOUND_ID;
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, categoryName);
            rs = ps.executeQuery();
            if (rs.next()) res = rs.getInt(1);
        } catch (SQLException e) {
            LOG.error("Error looking for category");
            sqlex = e;
        } finally {
            close(rs, ps);
        }
        if (sqlex != null) throw sqlex;
        return res;
    }

    /**
	 * looks for parameter in PARAMETERS table
	 * 
	 * @param parameterName
	 *            name of parameter to look for
	 * @return id of parameter in PARAMETERS table
	 * @throws SQLException
	 * @author Daniel
	 */
    public int findParameterID(String parameterName) throws SQLException {
        if (parametersMap == null || refreshParametersMap) {
            parametersMap = prepareParameters();
            refreshParametersMap = false;
        }
        Integer result = parametersMap.get(parameterName);
        if (result == null) throw new SQLException("Parameter not found"); else return result;
    }

    /**
	 * Prepares HashMap from PARAMETERS table
	 * 
	 * @throws SQLException
	 * @author Daniel
	 */
    private HashMap<String, Integer> prepareParameters() throws SQLException {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        String query = "select * from parameters";
        PreparedStatement ps = null;
        ResultSet rs = null;
        SQLException sqlex = null;
        try {
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) map.put(rs.getString("NAME"), rs.getInt("ID"));
        } catch (SQLException e) {
            LOG.error("Error preparing parameters hash map", e);
            sqlex = e;
        } finally {
            close(rs, ps);
        }
        if (sqlex != null) throw sqlex;
        return map;
    }

    /**
	 * Tells FinderServiceImpl to refresh parameters map on next query. Useful
	 * if new parameter was added using {@link SaverServiceImpl}
	 * 
	 * @param newValue
	 */
    public void setRefreshParametersMapFlag(boolean newValue) {
        this.refreshParametersMap = newValue;
    }
}
