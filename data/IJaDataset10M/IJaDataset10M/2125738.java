package net.sf.clearwork.sample.persistent.dao.jdbcimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import net.sf.clearwork.core.utils.bean.PopulateUtils;
import net.sf.clearwork.sample.domain.UserVO;
import net.sf.clearwork.sample.persistent.dao.IUserDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class JdbcUserDao extends JdbcTemplate implements IUserDao {

    public void deleteUser(String id) {
        int num = this.update("delete from c_user where id = ?", new Object[] { id });
        logger.info("Delete " + num + " records OK.");
    }

    public UserVO findUser(String id) {
        String sql = "select id, username, password, contact_id, name, birthday, sex from c_user where id = ?";
        return (UserVO) this.queryForObject(sql, new Object[] { id }, new RowMapper() {

            public Object mapRow(ResultSet resultset, int i) throws SQLException {
                UserVO vo = new UserVO();
                PopulateUtils.populate(vo, resultset);
                return vo;
            }
        });
    }

    public void insertUser(UserVO user) {
        String sql = "insert into c_user (id, username, password, contact_id, name, birthday, sex) values (?, ?, ?, ?, ?, ?, ?)";
        int num = this.update(sql, new Object[] { user.getId(), user.getUsername(), user.getPassword(), user.getContact_id(), user.getName(), user.getBirthday(), user.getSex() });
        logger.info("Insert " + num + " records OK.");
    }

    public void updateUser(UserVO user) {
        String sql = "update c_user set username = ?, password = ?, contact_id = ?, name = ?, birthday = ?, sex = ? where id = ?";
        int num = this.update(sql, new Object[] { user.getUsername(), user.getPassword(), user.getContact_id(), user.getName(), user.getBirthday(), user.getSex(), user.getId() });
        logger.info("Update " + num + " records OK.");
    }

    @SuppressWarnings("unchecked")
    public List<UserVO> queryAllUser() {
        String sql = "select id, username, password, contact_id, name, birthday, sex from c_user";
        return this.query(sql, new RowMapper() {

            public Object mapRow(ResultSet resultset, int i) throws SQLException {
                UserVO vo = new UserVO();
                PopulateUtils.populate(vo, resultset);
                return vo;
            }
        });
    }
}
