package cn.ibm.onehao.dao.refactor;

import java.sql.ResultSet;
import java.sql.SQLException;
import cn.ibm.onehao.domain.User;

/**
 * @author onehao
 * 
 */
public class UserDaoImpl2 {

    MyDaoTemplate template = new MyDaoTemplate();

    public User findUser(String loginName, String password) {
        String sql = "select id, username, birthday, money from user where username=?";
        Object[] args = new Object[] { loginName };
        RowMapper mapper = new UserRowMapper();
        Object user = this.template.find(sql, args, mapper);
        return (User) user;
    }

    public String findUserName(int id) {
        String sql = "select id, username, birthday, money from user where username=?";
        Object[] args = new Object[] { id };
        Object username = this.template.find(sql, args, new RowMapper() {

            @Override
            public Object mapRow(ResultSet rs) throws SQLException {
                return rs.getString("username");
            }
        });
        return (String) username;
    }
}

class StringRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet rs) throws SQLException {
        return rs.getString("username");
    }
}

class UserRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setBirthday(rs.getDate("birthday"));
        user.setMoney(rs.getFloat("money"));
        return user;
    }
}
