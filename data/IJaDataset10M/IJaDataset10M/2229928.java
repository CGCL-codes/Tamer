package com.hs.mail.imap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.mail.Quota;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import com.hs.mail.imap.user.Alias;
import com.hs.mail.imap.user.User;

/**
 * 
 * @author Won Chul Doh
 * @since Mar 23, 2010
 *
 */
public class MySqlUserDao extends AbstractDao implements UserDao {

    public User getUser(long id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        return (User) queryForObject(sql, new Object[] { new Long(id) }, userMapper);
    }

    public long getUserID(String address) {
        String sql = "SELECT id FROM user WHERE userid = ?";
        return queryForLong(sql, new Object[] { address });
    }

    public User getUserByAddress(String address) {
        String sql = "SELECT * FROM user WHERE userid = ?";
        return (User) queryForObject(sql, new Object[] { address }, userMapper);
    }

    public int getUserCount(String domain) {
        String sql = "SELECT COUNT(*) FROM user WHERE userid LIKE ?";
        return getJdbcTemplate().queryForInt(sql, new Object[] { new StringBuilder("%@").append(escape(domain)).toString() });
    }

    @SuppressWarnings("unchecked")
    public List<User> getUserList(String domain, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM user WHERE userid LIKE ? ORDER BY userid LIMIT ?, ?";
        return getJdbcTemplate().query(sql, new Object[] { new StringBuilder("%@").append(escape(domain)).toString(), new Integer(offset), new Integer(pageSize) }, userMapper);
    }

    public long addUser(final User user) {
        final String sql = "INSERT INTO user (userid, passwd, maxmail_size, forward) VALUES(?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, user.getUserID());
                pstmt.setString(2, user.getPassword());
                pstmt.setLong(3, user.getQuota());
                pstmt.setString(4, user.getForwardTo());
                return pstmt;
            }
        }, keyHolder);
        long id = keyHolder.getKey().longValue();
        user.setID(id);
        return id;
    }

    public int updateUser(User user) {
        String sql = "UPDATE user SET userid = ?, passwd = ?, maxmail_size = ?, forward = ? WHERE id = ?";
        return getJdbcTemplate().update(sql, new Object[] { user.getUserID(), user.getPassword(), new Long(user.getQuota()), user.getForwardTo(), new Long(user.getID()) });
    }

    public int deleteUser(long id) {
        String sql = "DELETE FROM user WHERE id = ?";
        return getJdbcTemplate().update(sql, new Object[] { new Long(id) });
    }

    public Alias getAlias(long id) {
        String sql = "SELECT a.*, u.userid FROM alias a, user u WHERE a.id = ? AND a.deliver_to = u.id";
        return (Alias) queryForObject(sql, new Object[] { new Long(id) }, aliasMapper);
    }

    public int getAliasCount(String domain) {
        String sql = "SELECT COUNT(*) FROM alias a, user u WHERE a.alias LIKE ? AND a.deliver_to = u.id";
        return getJdbcTemplate().queryForInt(sql, new Object[] { new StringBuilder("%@").append(escape(domain)).toString() });
    }

    @SuppressWarnings("unchecked")
    public List<Alias> getAliasList(String domain, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        String sql = "SELECT a.*, u.userid FROM alias a, user u WHERE a.alias LIKE ? AND a.deliver_to = u.id ORDER BY a.alias LIMIT ?, ?";
        return getJdbcTemplate().query(sql, new Object[] { new StringBuilder("%@").append(escape(domain)).toString(), new Integer(offset), new Integer(pageSize) }, aliasMapper);
    }

    @SuppressWarnings("unchecked")
    public List<Alias> expandAlias(String alias) {
        String sql = "SELECT a.*, u.userid FROM alias a, user u WHERE a.alias = ? AND a.deliver_to = u.id";
        return getJdbcTemplate().query(sql, new Object[] { alias }, aliasMapper);
    }

    public long addAlias(final Alias alias) {
        final String sql = "INSERT INTO alias (alias, deliver_to) VALUES(?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, alias.getAlias());
                pstmt.setLong(2, alias.getDeliverTo());
                return pstmt;
            }
        }, keyHolder);
        long id = keyHolder.getKey().longValue();
        alias.setID(id);
        return id;
    }

    public int updateAlias(Alias alias) {
        String sql = "UPDATE alias SET alias = ?, deliver_to = ? WHERE id = ?";
        return getJdbcTemplate().update(sql, new Object[] { alias.getAlias(), new Long(alias.getDeliverTo()), new Long(alias.getID()) });
    }

    public int deleteAlias(long id) {
        String sql = "DELETE FROM alias WHERE id = ?";
        return getJdbcTemplate().update(sql, new Object[] { new Long(id) });
    }

    public long getQuotaLimit(long ownerID) {
        String sql = "SELECT maxmail_size FROM user WHERE id = ?";
        long limit = queryForLong(sql, new Object[] { new Long(ownerID) });
        return limit * 1024 * 1024;
    }

    public long getQuotaUsage(long ownerID) {
        String sql = "SELECT SUM(size) FROM mailbox b, message m, physmessage p WHERE b.ownerid=? AND b.mailboxid=m.mailboxid AND m.physmessageid=p.id";
        return queryForLong(sql, new Object[] { new Long(ownerID) });
    }

    public Quota getQuota(long ownerID, String quotaRoot) {
        Quota quota = new Quota(quotaRoot);
        quota.setResourceLimit("STORAGE", getQuotaLimit(ownerID));
        quota.resources[0].usage = getQuotaUsage(ownerID);
        return quota;
    }

    public void setQuota(long ownerID, Quota quota) {
        String sql = "UPDATE user SET maxmail_size = ? WHERE id = ?";
        for (int i = 0; i < quota.resources.length; i++) {
            if ("STORAGE".equals(quota.resources[i].name)) {
                getJdbcTemplate().update(sql, new Object[] { new Long(quota.resources[i].limit), new Long(ownerID) });
                quota.resources[i].usage = getQuotaUsage(ownerID);
                return;
            }
        }
    }

    private static RowMapper userMapper = new RowMapper() {

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setID(rs.getLong("id"));
            user.setUserID(rs.getString("userid"));
            user.setPassword(rs.getString("passwd"));
            user.setQuota(rs.getLong("maxmail_size"));
            user.setForwardTo(rs.getString("forward"));
            return user;
        }
    };

    private static RowMapper aliasMapper = new RowMapper() {

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            Alias alias = new Alias();
            alias.setID(rs.getLong("id"));
            alias.setAlias(rs.getString("alias"));
            alias.setDeliverTo(rs.getLong("deliver_to"));
            alias.setUserID(rs.getString("userid"));
            return alias;
        }
    };
}
