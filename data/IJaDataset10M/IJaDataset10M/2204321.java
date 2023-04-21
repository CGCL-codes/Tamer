package com.gever.sysman.export.dao;

import java.util.*;
import java.sql.*;
import com.gever.config.Constants;
import com.gever.jdbc.connection.*;
import com.gever.sysman.privilege.model.Resource;

public class ExportDDLDAO {

    private static final String SELECT_ALL = "SELECT ID,NAME,DESCRIPTION,RESOURCETYPE," + "LINK,PARENT_ID,TARGET,CODE,ORDERID FROM T_RESOURCE";

    public Collection queryAll() throws Exception {
        ConnectionProvider cp = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Resource res = null;
        Collection list = new ArrayList();
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_SOURCE);
            conn = cp.getConnection();
            ps = conn.prepareStatement(SELECT_ALL);
            rs = ps.executeQuery();
            while (rs.next()) {
                StringBuffer DB2DDL = new StringBuffer("INSERT INTO T_RESOURCE(ID,NAME,DESCRIPTION,RESOURCETYPE," + "LINK,PARENT_ID,TARGET,CODE,ORDERID) VALUES(");
                DB2DDL.append(rs.getInt("ID"));
                DB2DDL.append(",'");
                DB2DDL.append(rs.getString("NAME"));
                DB2DDL.append("','");
                DB2DDL.append(rs.getString("DESCRIPTION"));
                DB2DDL.append("','");
                DB2DDL.append(rs.getString("RESOURCETYPE"));
                DB2DDL.append("','");
                DB2DDL.append(rs.getString("LINK"));
                DB2DDL.append("',");
                DB2DDL.append(rs.getLong("PARENT_ID"));
                DB2DDL.append(",'");
                DB2DDL.append(rs.getString("TARGET").charAt(0));
                DB2DDL.append("','");
                DB2DDL.append(rs.getString("CODE"));
                DB2DDL.append("','");
                DB2DDL.append(rs.getString("ORDERID"));
                DB2DDL.append("');");
                list.add(DB2DDL.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
