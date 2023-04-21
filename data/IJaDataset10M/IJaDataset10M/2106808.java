package com.totsp.sample.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.totsp.sample.client.MyService;
import com.totsp.sample.client.exception.DataException;
import com.totsp.sample.client.model.Entry;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Service Impl class, this is the GWT SERVER side code, runs on the server, not bound by the JRE emulation
 * library or Java 1.5.  In this case getting a DataSource from Tomcat and storing stuff, in "real life" would pass
 * off such to your DAO or other and just be a "wrapper" with no logic.
 *
 * @author ccollins
 *
 */
public class MyServiceImpl extends RemoteServiceServlet implements MyService {

    public List myMethod(String s) throws DataException {
        Context initContext = null;
        Context envContext = null;
        DataSource ds = null;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        List resultList = new ArrayList();
        try {
            initContext = new InitialContext();
            envContext = (Context) initContext.lookup("java:/comp/env");
            ds = (DataSource) envContext.lookup("jdbc/DataSource");
        } catch (NamingException e) {
            e.printStackTrace();
            throw new DataException("UNABLE TO GET DATASOURCE - \n" + e.getMessage());
        }
        try {
            conn = ds.getConnection();
            st = conn.createStatement();
            try {
                st.executeUpdate("CREATE TABLE sample ( id INTEGER IDENTITY, name VARCHAR(256), time VARCHAR(256))");
            } catch (SQLException e) {
            }
            st.executeUpdate("INSERT INTO sample (name, time) VALUES ('" + s + "', '" + new Date() + "')");
            conn.commit();
            rs = st.executeQuery("SELECT * FROM sample");
            while (rs.next()) {
                String name = rs.getString("name");
                String time = rs.getString("time");
                Entry entry = new Entry();
                entry.name = name;
                entry.time = time;
                resultList.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataException("SQLException - " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }
}
