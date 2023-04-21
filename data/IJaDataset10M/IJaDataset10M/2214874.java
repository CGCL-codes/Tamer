package com.pbonhomme.xf.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SQLService {

    protected static String DEFAULT_DATASOURCE = "java:comp/env/jdbc/article";

    private Log log = LogFactory.getLog(this.getClass());

    private static Map<String, DataSource> m_datasources = new HashMap<String, DataSource>();

    protected DataSource getDataSource(String name) throws SQLException {
        DataSource datasource = (DataSource) m_datasources.get(name);
        if (datasource == null) {
            Context l_context;
            DataSource l_dataSource = null;
            try {
                l_context = new InitialContext();
                l_dataSource = (DataSource) l_context.lookup(name);
            } catch (NamingException e) {
                String l_message = "Couldn't get the datasource from JNDI, check JNDI configuration";
                log.error(l_message, e);
                throw new SQLException(l_message);
            }
            if (l_dataSource == null) {
                String l_message = "No data source in the context";
                log.error(l_message);
                throw new SQLException(l_message);
            }
            m_datasources.put(name, l_dataSource);
            datasource = l_dataSource;
        }
        return datasource;
    }

    public void closeRessources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e1) {
            log.error("SQLService: ---> probl�me de fermeture de la connexion et du prepare Statement" + e1.toString());
        }
    }

    public void closeRessources(Connection conn, PreparedStatement pstmt) {
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e1) {
            log.error("SQLService: ---> probl�me de fermeture de la connexion et du prepare Statement" + e1.toString());
        }
    }

    public void closeRessources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e1) {
            log.error("SQLService: ---> probl�me de fermeture de la connexion et du prepare Statement" + e1.toString());
        }
    }
}
