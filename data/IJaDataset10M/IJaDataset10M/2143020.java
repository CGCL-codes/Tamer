package no.monsen.webservice.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import no.monsen.webservice.commons.MonsenFact;
import org.apache.log4j.Logger;

/**
 * 
 * @author geir.k.wollum
 * 28.02.2008
 */
public class MonsenDAO {

    private Logger log = Logger.getLogger(MonsenDAO.class.getName());

    private static MonsenDAO instance = new MonsenDAO();

    private MonsenDAO() {
    }

    public static MonsenDAO getInstance() {
        return instance;
    }

    private Connection getConnection() throws NamingException, SQLException {
        DataSource source = null;
        Connection con = null;
        Context c = new InitialContext();
        source = (DataSource) c.lookup("jdbc/monsen");
        con = source.getConnection();
        return con;
    }

    public void addVisitor(MonsenFact request) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String clientID = request.getClientID();
        try {
            con = this.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM visits WHERE clientID = ?");
            pstmt.setString(1, clientID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                count += 1;
                pstmt = con.prepareStatement("UPDATE visits SET count = ? WHERE clientID = ?");
                pstmt.setInt(1, count);
                pstmt.setString(2, clientID);
                pstmt.executeUpdate();
            } else {
                pstmt = con.prepareStatement("INSERT INTO visits(clientID,count) VALUES(?,1)");
                pstmt.setString(1, clientID);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            log.error("SQLException: " + e.getMessage());
        } catch (NamingException ne) {
            log.error("NamingException: " + ne.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.warn("SQLException: " + e.getMessage());
            }
        }
    }

    public MonsenFact getMonsenFact() {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtCount = null;
        ResultSet rs = null;
        String fact = null;
        int id = -1;
        int count = 0;
        try {
            con = this.getConnection();
            pstmtCount = con.prepareStatement("SELECT COUNT(*) FROM facts");
            rs = pstmtCount.executeQuery();
            if (rs.next()) count = rs.getInt(1);
            int wildcard = (int) Math.floor(Math.random() * count + 1);
            pstmt = con.prepareStatement("SELECT * FROM facts");
            rs = pstmt.executeQuery();
            if (rs.next()) {
                rs.absolute(wildcard);
                fact = rs.getString("fact");
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            fact = "Monsen er utilgjengelig (" + e.getMessage() + ")";
            log.error("SQLException: " + e.getMessage());
        } catch (NamingException e) {
            fact = "Monsen er utilgjengelig (" + e.getMessage() + ")";
            log.error("NamingException: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (pstmtCount != null) {
                    pstmtCount.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.warn("SQLException: " + e.getMessage());
            }
        }
        return new MonsenFact(id, fact, null);
    }

    public String checkVersion(String clientVersion) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String thisVersion = null;
        try {
            con = this.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM currentVersion");
            if (rs.next()) {
                thisVersion = rs.getString("thisVersion");
                System.out.println(thisVersion);
            }
        } catch (SQLException e) {
            log.error("SQLException: " + e.getMessage());
        } catch (NamingException e) {
            log.error("NamingException: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.warn("SQLException: " + e.getMessage());
            }
        }
        return thisVersion;
    }
}
