package ces.platform.bbs.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import ces.platform.bbs.ForumColor;
import ces.platform.bbs.ForumColorManager;

public class DbForumColorManager implements ForumColorManager {

    protected DbForumFactory factory = null;

    protected DbForumColorManager(DbForumFactory factory) {
        this.factory = factory;
    }

    /**
     * @see ces.platform.bbs.ForumColorManager#getForumColor(int)
     */
    public ForumColor getForumColor(int id) {
        DbCacheManager cacheManager = factory.getCacheManager();
        if (!cacheManager.isCacheEnabled()) {
            return new DbForumColor(this, id);
        }
        Integer colorIDInteger = new Integer(id);
        DbForumColor forumColor = (DbForumColor) cacheManager.get(DbCacheManager.COLOR_CACHE, colorIDInteger);
        if (forumColor == null) {
            forumColor = new DbForumColor(this, id);
            cacheManager.add(DbCacheManager.COLOR_CACHE, colorIDInteger, forumColor);
        }
        return forumColor;
    }

    /**
     * @see ces.platform.bbs.ForumColorManager#addForumColor(java.lang.String, java.lang.String)
     */
    public ForumColor addForumColor(String title, String memo) {
        return new DbForumColor(this, title, memo);
    }

    private static final String COUNT = "SELECT COUNT(*) FROM " + DbForumFactory.DB_PREFIX_MODULE + "COLOR";

    /**
     * @see ces.platform.bbs.ForumColorManager#getForumColorCount()
     */
    public int getForumColorCount() {
        int count = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DbForumFactory.getConnection();
            pstmt = conn.prepareStatement(COUNT);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            DbForumFactory.closeDB(rs, pstmt, null, conn);
        }
        return (count == 0 ? 1 : count);
    }

    /**
     * @see ces.platform.bbs.ForumColorManager#forumColors(int, int)
     */
    public Iterator forumColors(int pageNum, int pageCount) {
        return new DbForumColorIterator(this, pageNum, pageCount);
    }

    /**
     * @see ces.platform.bbs.ForumColorManager#forumColors()
     */
    public Iterator forumColors() {
        return new DbForumColorIterator(this);
    }

    private static final String DELETE = "DELETE FROM " + DbForumFactory.DB_PREFIX_MODULE + "COLOR WHERE I_COLORID=?";

    private static final String UPDATE_BOARD_COLOR = "UPDATE " + DbForumFactory.DB_PREFIX_MODULE + "BOARD SET I_COLORID=0 WHERE I_COLORID=?";

    /**
     * @see ces.platform.bbs.ForumColorManager#deleteForumColor(ces.platform.bbs.ForumColor)
     */
    public void deleteForumColor(ForumColor forumColor) {
        if (forumColor == null) return;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbForumFactory.getConnection();
            pstmt = conn.prepareStatement(UPDATE_BOARD_COLOR);
            pstmt.setInt(1, forumColor.getId());
            pstmt.executeUpdate();
            pstmt.clearParameters();
            pstmt = conn.prepareStatement(DELETE);
            pstmt.setInt(1, forumColor.getId());
            pstmt.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            DbForumFactory.closeDB(null, pstmt, null, conn);
        }
        DbCacheManager cacheManager = factory.cacheManager;
        if (cacheManager.isCacheEnabled()) {
            cacheManager.remove(DbCacheManager.COLOR_CACHE, new Integer(forumColor.getId()));
        }
    }
}
