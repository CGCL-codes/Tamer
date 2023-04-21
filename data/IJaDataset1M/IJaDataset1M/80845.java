package org.gbif.biogarage.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.gbif.biogarage.model.User;
import org.gbif.biogarage.service.UserManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

public class UserManagerJDBC extends BaseManagerJDBC implements UserManager {

    protected UserRowMapper userRowMapper = new UserRowMapper();

    public List<User> list() {
        String sql = "Select * from user";
        List<User> results = (List<User>) getJdbcTemplate().query(sql, new RowMapperResultSetExtractor(userRowMapper, 10));
        return results;
    }

    /**
	* Utility to create an User for a row 
	*/
    protected class UserRowMapper implements RowMapper {

        /**
		* The factory
		*/
        public Object mapRow(java.sql.ResultSet rs, int rowNum) throws SQLException {
            User u = new User();
            u.setId(rs.getLong("id"));
            u.setCreated(rs.getDate("created"));
            u.setEmail(rs.getString("email"));
            u.setLastLogin(rs.getDate("last_login"));
            u.setPassword(rs.getString("password"));
            u.setRealname(rs.getString("realname"));
            return u;
        }
    }

    @Override
    public User get(String email) {
        return null;
    }

    @Override
    public User login(String email, String password) {
        return null;
    }

    @Override
    public User get(Long id) {
        return null;
    }

    @Override
    public List<User> list(int page, int pagesize) {
        return null;
    }

    @Override
    public User newInstance() {
        return null;
    }

    @Override
    public void remove(Long id) {
    }

    @Override
    public void remove(User obj) {
    }

    @Override
    public User save(User object) {
        return null;
    }

    @Override
    public List<User> search(String querystring, int page, int pagesize) {
        return null;
    }
}
