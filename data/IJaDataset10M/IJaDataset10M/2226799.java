package ro.gateway.aida.obj.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import ro.gateway.aida.db.DBPersistenceManager;
import ro.gateway.aida.db.PersistenceToken;
import ro.gateway.aida.obj.Bookmark;
import ro.gateway.aida.obj.GenericActivityProperty;

/**
 * @author Mihai Postelnicu BookmarkDB
 */
public class BookmarkDB extends DBPersistenceManager {

    private BookmarkDB(PersistenceToken token) {
        super(token);
    }

    public static BookmarkDB getManager(PersistenceToken token) {
        return new BookmarkDB(token);
    }

    public void add(Bookmark b) throws SQLException {
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(INSERT_BM);
            ps.setInt(1, b.getType());
            ps.setDate(2, new Date(System.currentTimeMillis()));
            ps.setLong(3, b.getUserId());
            ps.setLong(4, b.getObjectId());
            ps.executeUpdate();
            ps.close();
        } finally {
            con.close();
        }
    }

    public void delete(long id, long user_id) throws SQLException {
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(DELETE_BM);
            ps.setLong(1, id);
            ps.setLong(2, user_id);
            ps.executeUpdate();
            ps.close();
        } finally {
            con.close();
        }
    }

    public void deleteAllRelated(long object_id) throws SQLException {
        Connection con = getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(DELETE_ALL_RELATED);
            ps.setLong(1, object_id);
            ps.executeUpdate();
            ps.close();
        } finally {
            con.close();
        }
    }

    public ArrayList getByUser(long user_id, String lang) throws SQLException {
        Connection con = getConnection();
        ArrayList ret = new ArrayList();
        try {
            PreparedStatement ps = con.prepareStatement(GET_BM_FOR_USER);
            ps.setLong(1, user_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Bookmark b = new Bookmark();
                b.setType(rs.getInt(3));
                b.setObjectId(rs.getLong(4));
                switch(b.getType()) {
                    case Bookmark.PROJECT_BM:
                        GenericActivityProperty gap = GenericActivityPropertyDB.getManager(token).getFirstForActivity("gen_titles", b.getObjectId());
                        b.setDescription(gap != null ? gap.getName(lang) : "untitled_?");
                        break;
                }
                b.setInsDate(rs.getDate(2));
                b.setId(rs.getLong(1));
                ret.add(b);
            }
            ps.close();
            return ret;
        } finally {
            con.close();
        }
    }

    public static final String GET_BM_FOR_USER = "SELECT id,insdate,booktype,object_id FROM bookmarks WHERE user_id=?" + " ORDER BY insdate";

    public static final String INSERT_BM = "INSERT INTO bookmarks (booktype,insdate,user_id,object_id)" + " VALUES (?,?,?,?)";

    public static final String DELETE_ALL_RELATED = "DELETE FROM bookmarks WHERE object_id=?";

    public static final String DELETE_BM = "DELETE FROM bookmarks WHERE id=? AND user_id=?";
}
