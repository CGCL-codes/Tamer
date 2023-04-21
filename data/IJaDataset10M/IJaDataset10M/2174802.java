package coyousoft.javaeedemo.dao.mysql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import coyousoft.easydao.core.JdbcFactory;
import coyousoft.easydao.core.JdbcOperations;
import coyousoft.easydao.sql.SqlFactory;
import coyousoft.javaeedemo.dao.UserDao;
import coyousoft.javaeedemo.entity.User;
import coyousoft.util.Pagination;

public class UserDaoImpl implements UserDao {

    private static JdbcOperations dao = JdbcFactory.get(UserDaoImpl.class);

    public int add(User user) throws SQLException {
        String sql = SqlFactory.getSql(UserDaoImpl.class, "add");
        return dao.update(sql, user.getUserName(), user.getUserPassword(), user.getUserRealName(), user.getUserEmail());
    }

    public boolean checkUserName(String userName) throws SQLException {
        String sql = SqlFactory.getSql(UserDaoImpl.class, "checkUserName");
        return dao.queryForInt(sql, userName.toUpperCase()) == 0;
    }

    public boolean checkUserEmail(String userEmail) throws SQLException {
        String sql = SqlFactory.getSql(UserDaoImpl.class, "checkUserEmail");
        return dao.queryForInt(sql, userEmail.toUpperCase()) == 0;
    }

    public Pagination<User> getUserList(Pagination<User> pagi) throws SQLException {
        List<User> userList = new ArrayList<User>();
        String sql = SqlFactory.getSql(UserDaoImpl.class, "getUserList1");
        for (Map<String, Object> row : dao.queryForList(sql, pagi.getOffset(), pagi.getPageSize())) {
            userList.add(new User().fill(row));
        }
        return pagi.fill(countUser(), userList);
    }

    private int countUser() throws SQLException {
        String sql = SqlFactory.getSql(UserDaoImpl.class, "countUser");
        return dao.queryForInt(sql);
    }

    public Pagination<User> getUserList(Pagination<User> pagi, String userName) throws SQLException {
        List<User> userList = new ArrayList<User>();
        String sql = SqlFactory.getSql(UserDaoImpl.class, "getUserList2");
        for (Map<String, Object> row : dao.queryForList(sql, "%" + userName.toUpperCase() + "%", pagi.getOffset(), pagi.getPageSize())) {
            userList.add(new User().fill(row));
        }
        return pagi.fill(countUser(userName), userList);
    }

    public int countUser(String userName) throws SQLException {
        String sql = SqlFactory.getSql(UserDaoImpl.class, "countUser1");
        return dao.queryForInt(sql, "%" + userName.toUpperCase() + "%");
    }

    public int delete(Long userId) throws SQLException {
        String sql = SqlFactory.getSql(UserDaoImpl.class, "delete");
        return dao.update(sql, userId);
    }

    public User getUser(Long userId) throws SQLException {
        User user = null;
        String sql = SqlFactory.getSql(UserDaoImpl.class, "getUser");
        for (Map<String, Object> row : dao.queryForList(sql, userId)) {
            user = new User().fill(row);
            break;
        }
        return user;
    }

    public int update(User user) throws SQLException {
        String sql = SqlFactory.getSql(UserDaoImpl.class, "update");
        return dao.update(sql, user.getUserName(), user.getUserRealName(), user.getUserEmail(), user.getUserId());
    }
}
