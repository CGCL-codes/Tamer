package com.modelmetrics.cloudconverter.engine;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Provides SQL based values for PicklistValues.
 * 
 * @author reidcarlberg
 * @since 2009-05-25
 */
public class PicklistProviderSqlImpl implements PicklistProvider {

    private Log log = LogFactory.getLog(PicklistProviderSqlImpl.class);

    private Connection connection;

    private String sql;

    public PicklistProviderSqlImpl(Connection connection, String sql) {
        this.connection = connection;
        this.sql = sql;
    }

    public List<String> getPicklistValues() throws Exception {
        log.debug("dirtconnection? " + connection.getClass().getName());
        Statement statement = connection.createStatement();
        log.info("Picklist sql: " + sql);
        ResultSet rs = statement.executeQuery(sql);
        if (rs.getMetaData().getColumnCount() != 1) {
            rs.close();
            statement.close();
            throw new RuntimeException("Column count not right on picklist; should be 1 but is " + rs.getMetaData().getColumnCount());
        }
        ArrayList<String> values = new ArrayList<String>();
        while (rs.next()) {
            log.debug("picklist value is: " + rs.getString(1));
            values.add(rs.getString(1));
        }
        rs.close();
        statement.close();
        return values;
    }
}
