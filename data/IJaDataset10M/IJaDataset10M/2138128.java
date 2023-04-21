package net.pmonks.DAL.generator.schema;

import java.util.*;
import java.security.*;
import java.sql.*;
import javax.sql.*;
import org.apache.log4j.*;
import org.apache.log4j.xml.*;
import net.pmonks.DAL.generator.config.*;

public class ViewDefinition {

    /**
     * Log4J category used for logging by the class.
     */
    static Category cat = Category.getInstance(ViewDefinition.class.getName());

    protected final String DB_CONNECTION_NAME = "CONNECTION_1";

    protected String viewName = null;

    protected List columnList = null;

    public ViewDefinition(DBConnectionManager dbConMgr, ViewInfo view) throws Exception, SQLException, InvalidParameterException {
        if (view == null) {
            String msg = "ViewDefinition constructor was passed a null or empty parameter.";
            cat.error(msg);
            throw new InvalidParameterException(msg);
        }
        viewName = view.getViewName();
        cat.info("* Reading view '" + viewName + "'...");
        determineColumns(dbConMgr, view);
    }

    public boolean hasColumns() {
        return ((columnList != null) && (!columnList.isEmpty()));
    }

    public String getViewName() {
        return (viewName);
    }

    public List getColumnList() {
        return (columnList);
    }

    protected void determineColumns(DBConnectionManager dbConMgr, ViewInfo view) throws SQLException, Exception {
        Connection con = dbConMgr.getConnection(DB_CONNECTION_NAME);
        DatabaseMetaData dmd = con.getMetaData();
        ResultSet rs = null;
        boolean dataColumnsExist = false;
        Iterator it = null;
        try {
            rs = dmd.getColumns(null, null, viewName, null);
            Map columnMap = new HashMap();
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                dataColumnsExist = true;
                columnMap.put(columnName, view.getColumnInfo(columnName));
            }
            if (!dataColumnsExist) {
                cat.debug("  No data columns found.");
            } else {
                columnList = new ArrayList();
                it = columnMap.keySet().iterator();
                while (it.hasNext()) {
                    String columnName = (String) it.next();
                    cat.debug("  Adding column '" + columnName + "'.");
                    columnList.add(new ColumnDefinition(dbConMgr, viewName, columnName, (ColumnInfo) columnMap.get(columnName)));
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
                rs = null;
            }
        }
    }
}
