package users.domain.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import users.domain.User;

public class UserRowMapper implements RowMapper {

    public Object mapRow(ResultSet rs, int index) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("idUser"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        return user;
    }
}
