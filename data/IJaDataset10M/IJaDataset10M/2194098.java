package ces.platform.bbs.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import org.apache.log4j.Logger;
import ces.platform.bbs.BookMark;
import ces.platform.bbs.BookMarkManager;
import ces.platform.bbs.exception.BookMarkNotFoundException;
import ces.platform.bbs.exception.UnauthorizedException;

/**
 * ��ǩ������
 * @author sword
 */
public class DbBookMarkManager implements BookMarkManager {

    private DbForumFactory factory;

    private static Logger log = Logger.getLogger(DbBookMarkManager.class.getName());

    private static final String SQL_COUNT = "SELECT COUNT(*)" + " FROM " + DbForumFactory.DB_PREFIX_MODULE + "BOOKMARK" + " WHERE I_USERID = ?";

    private static final String SQL_DELETE = "DELETE FROM " + DbForumFactory.DB_PREFIX_MODULE + "BOOKMARK" + " WHERE I_USERID = ? AND I_THREADID = ?";

    /**
	 * ���췽��
	 * @param factory ��̳���
	 */
    public DbBookMarkManager(DbForumFactory factory) {
        this.factory = factory;
    }

    /**
	 * @see ces.platform.bbs.BookMarkManager#createBookMark(int, long)
	 */
    public BookMark createBookMark(int userID, long threadID) {
        return new DbBookMark(this, userID, threadID, false);
    }

    /**
	 * @see ces.platform.bbs.BookMarkManager#deleteBookMark(int, long)
	 */
    public void deleteBookMark(int userID, long threadID) throws UnauthorizedException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbForumFactory.getConnection();
            pstmt = conn.prepareStatement(SQL_DELETE);
            pstmt.setInt(1, userID);
            pstmt.setLong(2, threadID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error(e);
        } finally {
            DbForumFactory.closeDB(null, pstmt, null, conn);
        }
    }

    /**
	 * @see ces.platform.bbs.BookMarkManager#getBookMarkCount(int)
	 */
    public int getBookMarkCount(int userID) {
        int count = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DbForumFactory.getConnection();
            pstmt = conn.prepareStatement(SQL_COUNT);
            pstmt.setInt(1, userID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.error(e);
        } finally {
            DbForumFactory.closeDB(rs, pstmt, null, conn);
        }
        return count;
    }

    /**
	 * @see ces.platform.bbs.BookMarkManager#getBookMarks(int)
	 */
    public Iterator getBookMarks(int userID) {
        return new DbBookMarkIterator(userID, this);
    }

    /**
	 * @see ces.platform.bbs.BookMarkManager#getBookMarks(int, int, int)
	 */
    public Iterator getBookMarks(int userID, int pageNum, int pageCount) {
        return new DbBookMarkIterator(userID, this, pageNum, pageCount);
    }

    /**
	 * @see ces.platform.bbs.BookMarkManager#getBookMark(int, long)
	 */
    public BookMark getBookMark(int userID, long threadID) throws BookMarkNotFoundException {
        return new DbBookMark(this, userID, threadID, true);
    }

    protected DbForumFactory getFactory() {
        return factory;
    }
}
