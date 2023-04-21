package uk.org.brindy.jwebdoc.user;

import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import uk.org.brindy.jwebdoc.WebDocRuntimeException;
import uk.org.brindy.jwebdoc.db.IbatisClient;
import uk.org.brindy.jwebdoc.util.MD5;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author brindy
 */
final class UserDAO {

    private static final Logger LOG = Logger.getLogger(UserDAO.class);

    private UserDAO() {
    }

    static User login(User user) {
        user.setPassword(MD5.encrypt(user.getPassword()));
        LOG.debug("password=[" + user.getPassword() + "]");
        SqlMapClient smc = IbatisClient.getSqlMapClient();
        try {
            return (User) smc.queryForObject("user.login", user);
        } catch (SQLException e) {
            throw new WebDocRuntimeException(e);
        }
    }

    static List getUserList() {
        try {
            SqlMapClient smc = IbatisClient.getSqlMapClient();
            return smc.queryForList("user.list", null);
        } catch (SQLException ex) {
            throw new WebDocRuntimeException(ex);
        }
    }

    static boolean createUser(User user) {
        user.setPassword(MD5.encrypt(user.getPassword()));
        try {
            SqlMapClient smc = IbatisClient.getSqlMapClient();
            smc.insert("user.create", user);
            return true;
        } catch (SQLException ex) {
            throw new WebDocRuntimeException(ex);
        }
    }

    static boolean deleteUser(int id) {
        try {
            SqlMapClient smc = IbatisClient.getSqlMapClient();
            return smc.update("user.delete", new Integer(id)) > 0;
        } catch (SQLException ex) {
            throw new WebDocRuntimeException(ex);
        }
    }

    static User getUser(int id) {
        try {
            SqlMapClient smc = IbatisClient.getSqlMapClient();
            return (User) smc.queryForObject("user.get", new Integer(id));
        } catch (SQLException ex) {
            throw new WebDocRuntimeException(ex);
        }
    }

    static boolean updateUser(User user) {
        try {
            SqlMapClient smc = IbatisClient.getSqlMapClient();
            return smc.update("user.update", user) > 0;
        } catch (SQLException ex) {
            throw new WebDocRuntimeException(ex);
        }
    }

    static boolean updatePassword(User user) {
        user.setPassword(MD5.encrypt(user.getPassword()));
        try {
            SqlMapClient smc = IbatisClient.getSqlMapClient();
            return smc.update("user.setpassword", user) > 0;
        } catch (SQLException ex) {
            throw new WebDocRuntimeException(ex);
        }
    }
}
