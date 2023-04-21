package es.f2020.osseo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletContext;
import es.f2020.osseo.core.OsseoFailure;
import es.f2020.osseo.core.util.DB;
import es.f2020.osseo.domain.Keyphrase;

/**
 * Database persistence class for keyphrases.
 */
public class KeyphraseDAO {

    private DB db;

    public KeyphraseDAO(ServletContext servletContext) {
        this.db = new DB(servletContext);
    }

    public Keyphrase save(Keyphrase keyphrase) {
        Connection conn = null;
        try {
            conn = db.getConnection();
            String sql = "INSERT INTO keyphrases (website_id, keyphrase) VALUES (?,?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, keyphrase.getWebsiteId());
            ps.setString(2, keyphrase.getKeyphrase());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            keyphrase.setId(id);
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            throw new OsseoFailure("SQL error: cannot create keyphrase. ", ex);
        } finally {
            db.putConnection(conn);
        }
        return keyphrase;
    }

    public boolean delete(int id) {
        boolean deletionOk = false;
        Connection conn = null;
        try {
            conn = db.getConnection();
            String sql = "DELETE FROM keyphrases WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            deletionOk = ps.executeUpdate() == 1;
            ps.close();
        } catch (SQLException sqle) {
            throw new OsseoFailure("SQL error: cannot remove keyphrase. ", sqle);
        } finally {
            db.putConnection(conn);
        }
        return deletionOk;
    }
}
