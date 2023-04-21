package vlan.webgame.manage.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.jmantis.core.Order;
import org.jmantis.core.PageResult;
import org.jmantis.core.QueryParams;
import org.jmantis.core.dao.MysqlJdbcDaoSupport;
import org.jmantis.core.utils.MathUtils;
import vlan.webgame.manage.entity.PetTrainLog;
import vlan.webgame.manage.dao.PetTrainLogDao;

public class PetTrainLogDaoImpl extends MysqlJdbcDaoSupport implements PetTrainLogDao {

    private static final String SQL_INSERT = "INSERT INTO `pet_train_log` (`pid`,`sid`,`uid`,`role_name`,`pet_id`,`pet_name`,`type`,`gold`,`usetime`,`time`) VALUES (?,?,?,?,?,?,?,?,?,?)";

    private static final String SQL_FAST_INSERT_PREFIX = "INSERT INTO `pet_train_log` (`pid`,`sid`,`uid`,`role_name`,`pet_id`,`pet_name`,`type`,`gold`,`usetime`,`time`) VALUES ";

    private static final String SQL_FAST_INSERT_VALUE_ITEM = " (?,?,?,?,?,?,?,?,?,?) ";

    private static final String SQL_UPDATE = "UPDATE `pet_train_log` SET `pid`=?,`sid`=?,`uid`=?,`role_name`=?,`pet_id`=?,`pet_name`=?,`type`=?,`gold`=?,`usetime`=?,`time`=? WHERE `id`=?";

    private static final String SQL_UPDATE_PARTIAL_PREFIX = "UPDATE `pet_train_log` SET ";

    private static final String SQL_WHERE_BY_KEY = " WHERE `id`=?";

    private static final String SQL_DELETE = "DELETE FROM `pet_train_log` WHERE `id`=?";

    private static final String SQL_SELECT_BY_KEY = "SELECT * FROM `pet_train_log` WHERE `id`=? ORDER BY `id` DESC";

    private static final String SQL_SELECT_COUNT = "SELECT count(*) FROM `pet_train_log`";

    private static final String SQL_FORMAT_SELECT = "SELECT %s FROM `pet_train_log` ORDER BY `id` DESC";

    private static final String SQL_FORMAT_SELECT_PREFIX = "SELECT %s FROM `pet_train_log` ";

    private static final String SQL_SELECT_PREFIX = "SELECT * FROM `pet_train_log` ";

    private static final String SQL_ORDER_BY_ID_DESC = " ORDER BY `id` DESC";

    private RowMapper<PetTrainLog> petTrainLogRowMapper = new RowMapper<PetTrainLog>() {

        @Override
        public PetTrainLog mapRow(ResultSet rs, int rowNum) throws SQLException {
            PetTrainLog o = new PetTrainLog();
            o.setId(rs.getInt("id"));
            o.setPid(rs.getInt("pid"));
            o.setSid(rs.getInt("sid"));
            o.setUid(rs.getInt("uid"));
            o.setRoleName(rs.getString("role_name"));
            o.setPetId(rs.getInt("pet_id"));
            o.setPetName(rs.getString("pet_name"));
            o.setType(rs.getInt("type"));
            o.setGold(rs.getInt("gold"));
            o.setUsetime(rs.getInt("usetime"));
            o.setTime(rs.getLong("time"));
            return o;
        }
    };

    /**
	 * @return 插入对象id
	 */
    @Override
    public int insert(final PetTrainLog petTrainLog) {
        return getJdbcTemplate().execute(new ConnectionCallback<Integer>() {

            @Override
            public Integer doInConnection(Connection con) throws SQLException, DataAccessException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = con.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
                    ps.setObject(1, petTrainLog.getPid());
                    ps.setObject(2, petTrainLog.getSid());
                    ps.setObject(3, petTrainLog.getUid());
                    ps.setObject(4, petTrainLog.getRoleName());
                    ps.setObject(5, petTrainLog.getPetId());
                    ps.setObject(6, petTrainLog.getPetName());
                    ps.setObject(7, petTrainLog.getType());
                    ps.setObject(8, petTrainLog.getGold());
                    ps.setObject(9, petTrainLog.getUsetime());
                    ps.setObject(10, petTrainLog.getTime());
                    ps.execute();
                    int key = 0;
                    rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        key = rs.getInt(1);
                    }
                    return key;
                } finally {
                    JdbcUtils.closeResultSet(rs);
                    JdbcUtils.closeStatement(ps);
                }
            }
        });
    }

    @Override
    public int insert(final List<PetTrainLog> list) {
        return getJdbcTemplate().execute(new ConnectionCallback<Integer>() {

            @Override
            public Integer doInConnection(Connection con) throws SQLException, DataAccessException {
                PreparedStatement ps = null;
                try {
                    ps = con.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
                    for (PetTrainLog petTrainLog : list) {
                        ps.setObject(1, petTrainLog.getPid());
                        ps.setObject(2, petTrainLog.getSid());
                        ps.setObject(3, petTrainLog.getUid());
                        ps.setObject(4, petTrainLog.getRoleName());
                        ps.setObject(5, petTrainLog.getPetId());
                        ps.setObject(6, petTrainLog.getPetName());
                        ps.setObject(7, petTrainLog.getType());
                        ps.setObject(8, petTrainLog.getGold());
                        ps.setObject(9, petTrainLog.getUsetime());
                        ps.setObject(10, petTrainLog.getTime());
                        ps.addBatch();
                    }
                    int[] ints = ps.executeBatch();
                    return MathUtils.sum(ints);
                } finally {
                    JdbcUtils.closeStatement(ps);
                }
            }
        });
    }

    public int fastInsert(final List<PetTrainLog> list) {
        return getJdbcTemplate().execute(new ConnectionCallback<Integer>() {

            @Override
            public Integer doInConnection(Connection con) throws SQLException, DataAccessException {
                PreparedStatement ps = null;
                try {
                    StringBuilder sb = new StringBuilder(SQL_FAST_INSERT_PREFIX);
                    for (int i = 0; i < list.size(); i++) {
                        if (i != 0) {
                            sb.append(",");
                        }
                        sb.append(SQL_FAST_INSERT_VALUE_ITEM);
                    }
                    ps = con.prepareStatement(sb.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                    int i = 1;
                    for (PetTrainLog petTrainLog : list) {
                        ps.setObject(i++, petTrainLog.getPid());
                        ps.setObject(i++, petTrainLog.getSid());
                        ps.setObject(i++, petTrainLog.getUid());
                        ps.setObject(i++, petTrainLog.getRoleName());
                        ps.setObject(i++, petTrainLog.getPetId());
                        ps.setObject(i++, petTrainLog.getPetName());
                        ps.setObject(i++, petTrainLog.getType());
                        ps.setObject(i++, petTrainLog.getGold());
                        ps.setObject(i++, petTrainLog.getUsetime());
                        ps.setObject(i++, petTrainLog.getTime());
                    }
                    return ps.executeUpdate();
                } finally {
                    JdbcUtils.closeStatement(ps);
                }
            }
        });
    }

    @Override
    public boolean update(final PetTrainLog petTrainLog) {
        return getJdbcTemplate().execute(new ConnectionCallback<Boolean>() {

            @Override
            public Boolean doInConnection(Connection con) throws SQLException, DataAccessException {
                PreparedStatement ps = null;
                try {
                    ps = con.prepareStatement(SQL_UPDATE);
                    ps.setObject(1, petTrainLog.getPid());
                    ps.setObject(2, petTrainLog.getSid());
                    ps.setObject(3, petTrainLog.getUid());
                    ps.setObject(4, petTrainLog.getRoleName());
                    ps.setObject(5, petTrainLog.getPetId());
                    ps.setObject(6, petTrainLog.getPetName());
                    ps.setObject(7, petTrainLog.getType());
                    ps.setObject(8, petTrainLog.getGold());
                    ps.setObject(9, petTrainLog.getUsetime());
                    ps.setObject(10, petTrainLog.getTime());
                    ps.setObject(11, petTrainLog.getId());
                    return ps.executeUpdate() > 0;
                } finally {
                    JdbcUtils.closeStatement(ps);
                }
            }
        });
    }

    @Override
    public boolean updatePartial(final Map<String, Object> m, final int id) {
        if (m == null || m.isEmpty()) {
            return false;
        }
        return getJdbcTemplate().execute(new ConnectionCallback<Boolean>() {

            @Override
            public Boolean doInConnection(Connection con) throws SQLException, DataAccessException {
                PreparedStatement ps = null;
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append(SQL_UPDATE_PARTIAL_PREFIX);
                    toSqlSet(sb, m);
                    sb.append(SQL_WHERE_BY_KEY);
                    ps = con.prepareStatement(sb.toString());
                    int i = 1;
                    for (Map.Entry<String, Object> e : m.entrySet()) {
                        ps.setObject(i, e.getValue());
                        i++;
                    }
                    ps.setObject(i, id);
                    return ps.executeUpdate() > 0;
                } finally {
                    JdbcUtils.closeStatement(ps);
                }
            }
        });
    }

    @Override
    public int updatePartial(final Map<String, Object> m, final QueryParams wh) {
        if (m == null || m.isEmpty() || wh == null || wh.isEmpty()) {
            return 0;
        }
        return getJdbcTemplate().execute(new ConnectionCallback<Integer>() {

            @Override
            public Integer doInConnection(Connection con) throws SQLException, DataAccessException {
                PreparedStatement ps = null;
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append(SQL_UPDATE_PARTIAL_PREFIX);
                    toSqlSet(sb, m);
                    wh.toSql(sb);
                    ps = con.prepareStatement(sb.toString());
                    int i = 1;
                    for (Map.Entry<String, Object> e : m.entrySet()) {
                        ps.setObject(i, e.getValue());
                        i++;
                    }
                    wh.setPreparedStatement(ps, i);
                    return ps.executeUpdate();
                } finally {
                    JdbcUtils.closeStatement(ps);
                }
            }
        });
    }

    @Override
    public boolean del(int id) {
        return getJdbcTemplate().update(SQL_DELETE, id) > 0;
    }

    @Override
    public PetTrainLog get(int id) {
        return querySingle(SQL_SELECT_BY_KEY, petTrainLogRowMapper, id);
    }

    @Override
    public long getCount() {
        return getJdbcTemplate().queryForLong(SQL_SELECT_COUNT);
    }

    @Override
    public long getCount(QueryParams params) {
        StringBuilder sb = new StringBuilder(SQL_SELECT_COUNT);
        params.toSql(sb);
        return getJdbcTemplate().queryForLong(sb.toString(), params.toParams());
    }

    @Override
    public PageResult<PetTrainLog> find(int page, int pageSize) {
        return fastQueryPage(SQL_FORMAT_SELECT, petTrainLogRowMapper, page, pageSize);
    }

    @Override
    public PageResult<PetTrainLog> find(Order order, int page, int pageSize) {
        StringBuilder sb = new StringBuilder(SQL_FORMAT_SELECT_PREFIX);
        order.toSql(sb);
        return fastQueryPage(sb.toString(), petTrainLogRowMapper, page, pageSize);
    }

    public PageResult<PetTrainLog> find(QueryParams params, int page, int pageSize) {
        StringBuilder sb = new StringBuilder(SQL_FORMAT_SELECT_PREFIX);
        params.toSql(sb);
        sb.append(SQL_ORDER_BY_ID_DESC);
        return fastQueryPage(sb.toString(), petTrainLogRowMapper, page, pageSize, params.toParams());
    }

    public PageResult<PetTrainLog> find(QueryParams params, Order order, int page, int pageSize) {
        StringBuilder sb = new StringBuilder(SQL_FORMAT_SELECT_PREFIX);
        params.toSql(sb);
        order.toSql(sb);
        return fastQueryPage(sb.toString(), petTrainLogRowMapper, page, pageSize, params.toParams());
    }

    @Override
    public List<PetTrainLog> find(int max) {
        return getJdbcTemplate().query(SQL_SELECT_PREFIX + SQL_ORDER_BY_ID_DESC + " LIMIT " + max, petTrainLogRowMapper);
    }

    @Override
    public List<PetTrainLog> find(QueryParams params, int max) {
        StringBuilder sb = new StringBuilder(SQL_SELECT_PREFIX);
        params.toSql(sb);
        sb.append(SQL_ORDER_BY_ID_DESC);
        sb.append(" LIMIT ");
        sb.append(max);
        return getJdbcTemplate().query(sb.toString(), petTrainLogRowMapper, params.toParams());
    }

    @Override
    public List<PetTrainLog> find(QueryParams params, Order order, int max) {
        StringBuilder sb = new StringBuilder(SQL_SELECT_PREFIX);
        params.toSql(sb);
        order.toSql(sb);
        sb.append(" LIMIT ");
        sb.append(max);
        return getJdbcTemplate().query(sb.toString(), petTrainLogRowMapper, params.toParams());
    }
}
