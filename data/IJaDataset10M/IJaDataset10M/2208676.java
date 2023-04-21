package com.j2xtreme.xwidget.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import com.j2xtreme.xbean.MethodInvocationException;
import com.j2xtreme.xbean.ObjectReference;
import com.j2xtreme.xbean.PropertyReference;
import com.j2xtreme.xbean.SimpleObjectReference;
import com.j2xtreme.xwidget.xwt.RenderEvent;
import com.j2xtreme.xwidget.xwt.RenderListener;

/**
 * @author Rob Schoening
 *
 */
public abstract class DataSourceSQLQuery implements SQLQuery, RenderListener {

    static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(DataSourceSQLQuery.class);

    private DataSource dataSource = null;

    String sql = "";

    String dataSourceURL = null;

    Map bindVars = new HashMap();

    int myOrderByColumn = 0;

    int order = ASCENDING;

    public static int ASCENDING = 0;

    public static int DESCENDING = 1;

    public void setSortAscending(boolean b) {
        if (b) {
            order = ASCENDING;
        } else {
            order = DESCENDING;
        }
    }

    public int getSortByColumn() {
        return myOrderByColumn;
    }

    public boolean getSortAscending() {
        return order == ASCENDING;
    }

    public boolean getSortDescending() {
        return order == DESCENDING;
    }

    public void setSortDescending(boolean b) {
        if (b == true) {
            order = DESCENDING;
        } else {
            order = ASCENDING;
        }
    }

    public void setSortByColumn(int col) {
        myOrderByColumn = col;
    }

    public void setSQL(String sql) {
        this.sql = sql;
    }

    public String getSQL() {
        return this.sql;
    }

    public void setDataSourceURL(String dataSourceURL) {
        this.dataSourceURL = dataSourceURL;
        this.dataSource = null;
    }

    public void setDataSource(DataSource ds) {
        dataSource = ds;
        dataSourceURL = null;
    }

    public void setBindVariable(int i, Object obj) {
        ObjectReference ov = new SimpleObjectReference(obj);
        bindVars.put(new Integer(i), ov);
    }

    public void setBindVariable(int i, Object obj, String prop) {
        ObjectReference ref = new PropertyReference(obj, prop);
        bindVars.put(new Integer(i), ref);
    }

    public String getOrderByClause() {
        if (myOrderByColumn < 1) {
            return "";
        } else {
            String clause = " ORDER BY " + myOrderByColumn + (order == DESCENDING ? " DESC" : " ASC");
            return clause;
        }
    }

    public abstract void processResultSet(ResultSet rs) throws java.sql.SQLException;

    public void select() throws java.sql.SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            DataSource ds = getDataSource();
            con = ds.getConnection();
            String sql = getSQL() + getOrderByClause();
            log.debug("Preparing SQL: " + sql);
            ps = con.prepareStatement(sql);
            Iterator it = bindVars.entrySet().iterator();
            List tmp = new ArrayList();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Integer idx = (Integer) entry.getKey();
                ObjectReference ref = (ObjectReference) entry.getValue();
                ps.setObject(idx.intValue(), ref.get());
            }
            rs = ps.executeQuery();
            processResultSet(rs);
        } catch (Exception e) {
            throw new MethodInvocationException(e);
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
            try {
                ps.close();
            } catch (Exception e) {
            }
            try {
                con.close();
            } catch (Exception e) {
            }
        }
    }

    public void beforeRender(RenderEvent event) {
        try {
            select();
        } catch (SQLException e) {
        }
    }

    public DataSource getDataSource() throws java.sql.SQLException {
        if (dataSource != null) {
            return dataSource;
        } else if (dataSourceURL != null) {
            dataSource = DataSourceResolver.resolve(dataSourceURL);
            return dataSource;
        }
        throw new java.sql.SQLException("dataSource not configured");
    }
}
