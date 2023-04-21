package cn.sharezoo.dao.impl;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.jforum.util.MD5;
import org.apache.log4j.Logger;
import org.hibernate.util.GetGeneratedKeysHelper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import cn.sharezoo.dao.BanlistDao;
import cn.sharezoo.dao.PostDao;
import cn.sharezoo.dao.PrivateMessageDao;
import cn.sharezoo.dao.TopicDao;
import cn.sharezoo.dao.UserDao;
import cn.sharezoo.domain.Admin;
import cn.sharezoo.domain.Authority;
import cn.sharezoo.domain.Coach;
import cn.sharezoo.service.impl.UserServiceImpl;
import cn.sharezoo.utils.SqlLoader;

public class UserDaoImpl implements UserDao {

    static final Logger log = Logger.getLogger(UserServiceImpl.class);

    private SqlLoader sqlLoader;

    private JdbcTemplate jdbcTemplate;

    private HibernateTemplate hibernateTemplate;

    private PostDao postDao;

    private TopicDao topicDao;

    private PrivateMessageDao privateMessageDao;

    private BanlistDao banlistDao;

    public BanlistDao getBanlistDao() {
        return banlistDao;
    }

    public void setBanlistDao(BanlistDao banlistDao) {
        this.banlistDao = banlistDao;
    }

    public TopicDao getTopicDao() {
        return topicDao;
    }

    public void setTopicDao(TopicDao topicDao) {
        this.topicDao = topicDao;
    }

    public PostDao getPostDao() {
        return postDao;
    }

    public void setPostDao(PostDao postDao) {
        this.postDao = postDao;
    }

    public SqlLoader getSqlLoader() {
        return sqlLoader;
    }

    public void setSqlLoader(SqlLoader sqlLoader) {
        this.sqlLoader = sqlLoader;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public void writeUserActive(int userId) {
        getJdbcTemplate().update(getSqlLoader().getSql("UserModel.writeUserActive"), new Object[] { new Integer(userId) }, new int[] { Types.INTEGER });
    }

    public void addToGroup(int userId, int[] groupId) {
        for (int i = 0; i < groupId.length; i++) {
            getJdbcTemplate().update(getSqlLoader().getSql("UserModel.addToGroup"), new Object[] { new Integer(userId), new Integer(groupId[i]) }, new int[] { Types.INTEGER, Types.INTEGER });
        }
    }

    public int getIdByUsernameFromJforum(String username) {
        try {
            return getJdbcTemplate().queryForInt(this.getSqlLoader().getSql("UserModel.getIdByUsername"), new Object[] { username }, new int[] { Types.VARCHAR });
        } catch (IncorrectResultSizeDataAccessException e) {
            if (0 == e.getActualSize()) {
                return -1;
            } else {
                throw e;
            }
        }
    }

    public boolean isEmailExisted(String email) {
        List ids = getJdbcTemplate().queryForList(getSqlLoader().getSql("UserModel.getUserIdFromEmail"), new Object[] { email }, new int[] { Types.VARCHAR });
        if (ids.size() != 0) {
            return true;
        }
        return false;
    }

    public int addNewToJForumIfNotExisted(String username, String password, String email, int[] groupId) throws DataAccessException, NoSuchAlgorithmException {
        int userId = getIdByUsernameFromJforum(username);
        if (userId <= 0) {
            log.debug("create new user");
            getJdbcTemplate().update(this.getSqlLoader().getSql("UserModel.addNew"), new Object[] { username, MD5.crypt(password), email, new Timestamp(System.currentTimeMillis()), null }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR });
            userId = getIdByUsernameFromJforum(username);
            addToGroup(userId, groupId);
        } else {
            log.debug("user is existed, the create action will not be executed");
        }
        return userId;
    }

    public void deleteUser(String username) {
        int userId = getIdByUsernameFromJforum(username);
        deletePhisically(userId);
    }

    public void deletePhisically(int userId) {
        getPostDao().deleteByUser(userId);
        getTopicDao().deleteByUserId(userId);
        getPrivateMessageDao().deleteByUserId(userId);
        getBanlistDao().deleteByUser(userId);
        this.deleteUserFromGroup(userId);
        getJdbcTemplate().update(getSqlLoader().getSql("UserModel.deleteUserByUserId"), new Object[] { new Integer(userId) }, new int[] { Types.INTEGER });
    }

    public PrivateMessageDao getPrivateMessageDao() {
        return privateMessageDao;
    }

    public void setPrivateMessageDao(PrivateMessageDao privateMessageDao) {
        this.privateMessageDao = privateMessageDao;
    }

    public void deleteUserFromGroup(int userId) {
        getJdbcTemplate().update(getSqlLoader().getSql("UserModel.deleteUserGroupsByUserId"), new Object[] { new Integer(userId) }, new int[] { Types.INTEGER });
    }

    public void deleteUser(int userId) {
        deletePhisically(userId);
    }

    public void initUsers() {
        boolean isAdminEmpty = getHibernateTemplate().find("from cn.sharezoo.domain.User user " + "where user.authority = '" + Authority.ADMIN + "'").isEmpty();
        boolean isContentEmpty = getHibernateTemplate().find("from cn.sharezoo.domain.User user " + "where user.authority = '" + Authority.CONTENT + "'").isEmpty();
        if (isAdminEmpty) {
            log.debug("user admin is empty");
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setEnabled(true);
            admin.setAuthority(Authority.ADMIN);
            admin.setGroupId(new Integer(2));
            admin.setEmail("admin@admin.com");
            getHibernateTemplate().save(admin);
        }
        if (isContentEmpty) {
            log.debug("content admin is empty");
            Admin admin = new Admin();
            admin.setUsername("content");
            admin.setPassword("content");
            admin.setEnabled(true);
            admin.setAuthority(Authority.CONTENT);
            admin.setGroupId(new Integer(1));
            admin.setEmail("admin@admin.com");
            getHibernateTemplate().save(admin);
        }
    }

    public List findSubStudents(int groupId) {
        return getHibernateTemplate().find("from cn.sharezoo.domain.Student stu " + "where stu.groupId = " + groupId);
    }
}
