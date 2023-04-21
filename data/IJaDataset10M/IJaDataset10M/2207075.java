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
import vlan.webgame.manage.entity.EquipmentLog;
import vlan.webgame.manage.dao.EquipmentLogDao;

public class EquipmentLogDaoImpl extends MysqlJdbcDaoSupport implements EquipmentLogDao {

    private static final String SQL_INSERT = "INSERT INTO `equipment_log` (`pid`,`sid`,`uid`,`role_name`,`equip_id`,`equip_name`,`amount`,`remark`,`create_time`) VALUES (?,?,?,?,?,?,?,?,?)";

    private static final String SQL_FAST_INSERT_PREFIX = "INSERT INTO `equipment_log` (`pid`,`sid`,`uid`,`role_name`,`equip_id`,`equip_name`,`amount`,`remark`,`create_time`) VALUES ";

    private static final String SQL_FAST_INSERT_VALUE_ITEM = " (?,?,?,?,?,?,?,?,?) ";

    private static final String SQL_UPDATE = "UPDATE `equipment_log` SET `pid`=?,`sid`=?,`uid`=?,`role_name`=?,`equip_id`=?,`equip_name`=?,`amount`=?,`remark`=?,`create_time`=? WHERE `id`=?";

    private static final String SQL_UPDATE_PARTIAL_PREFIX = "UPDATE `equipment_log` SET ";

    private static final String SQL_WHERE_BY_KEY = " WHERE `id`=?";

    private static final String SQL_DELETE = "DELETE FROM `equipment_log` WHERE `id`=?";

    private static final String SQL_SELECT_BY_KEY = "SELECT * FROM `equipment_log` WHERE `id`=? ORDER BY `id` DESC";

    private static final String SQL_SELECT_COUNT = "SELECT count(*) FROM `equipment_log`";

    private static final String SQL_FORMAT_SELECT = "SELECT %s FROM `equipment_log` ORDER BY `id` DESC";

    private static final String SQL_FORMAT_SELECT_PREFIX = "SELECT %s FROM `equipment_log` ";

    private static final String SQL_SELECT_PREFIX = "SELECT * FROM `equipment_log` ";

    private static final String SQL_ORDER_BY_ID_DESC = " ORDER BY `id` DESC";

    private RowMapper<EquipmentLog> equipmentLogRowMapper = new RowMapper<EquipmentLog>() {

        @Override
        public EquipmentLog mapRow(ResultSet rs, int rowNum) throws SQLException {
            EquipmentLog o = new EquipmentLog();
            o.setId(rs.getInt("id"));
            o.setPid(rs.getInt("pid"));
            o.setSid(rs.getInt("sid"));
            o.setUid(rs.getInt("uid"));
            o.setRoleName(rs.getString("role_name"));
            o.setEquipId(rs.getInt("equip_id"));
            o.setEquipName(rs.getString("equip_name"));
            o.setAmount(rs.getInt("amount"));
            o.setRemark(rs.getString("remark"));
            o.setCreateTime(rs.getTimestamp("create_time"));
            return o;
        }
    };

    /**
	 * @return 插入对象id
	 */
    @Override
    public int insert(final EquipmentLog equipmentLog) {
        return getJdbcTemplate().execute(new ConnectionCallback<Integer>() {

            @Override
            public Integer doInConnection(Connection con) throws SQLException, DataAccessException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = con.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
                    ps.setObject(1, equipmentLog.getPid());
                    ps.setObject(2, equipmentLog.getSid());
                    ps.setObject(3, equipmentLog.getUid());
                    ps.setObject(4, equipmentLog.getRoleName());
                    ps.setObject(5, equipmentLog.getEquipId());
                    ps.setObject(6, equipmentLog.getEquipName());
                    ps.setObject(7, equipmentLog.getAmount());
                    ps.setObject(8, equipmentLog.getRemark());
                    ps.setObject(9, equipmentLog.getCreateTime());
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
    public int insert(final List<EquipmentLog> list) {
        return getJdbcTemplate().execute(new ConnectionCallback<Integer>() {

            @Override
            public Integer doInConnection(Connection con) throws SQLException, DataAccessException {
                PreparedStatement ps = null;
                try {
                    ps = con.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
                    for (EquipmentLog equipmentLog : list) {
                        ps.setObject(1, equipmentLog.getPid());
                        ps.setObject(2, equipmentLog.getSid());
                        ps.setObject(3, equipmentLog.getUid());
                        ps.setObject(4, equipmentLog.getRoleName());
                        ps.setObject(5, equipmentLog.getEquipId());
                        ps.setObject(6, equipmentLog.getEquipName());
                        ps.setObject(7, equipmentLog.getAmount());
                        ps.setObject(8, equipmentLog.getRemark());
                        ps.setObject(9, equipmentLog.getCreateTime());
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

    public int fastInsert(final List<EquipmentLog> list) {
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
                    for (EquipmentLog equipmentLog : list) {
                        ps.setObject(i++, equipmentLog.getPid());
                        ps.setObject(i++, equipmentLog.getSid());
                        ps.setObject(i++, equipmentLog.getUid());
                        ps.setObject(i++, equipmentLog.getRoleName());
                        ps.setObject(i++, equipmentLog.getEquipId());
                        ps.setObject(i++, equipmentLog.getEquipName());
                        ps.setObject(i++, equipmentLog.getAmount());
                        ps.setObject(i++, equipmentLog.getRemark());
                        ps.setObject(i++, equipmentLog.getCreateTime());
                    }
                    return ps.executeUpdate();
                } finally {
                    JdbcUtils.closeStatement(ps);
                }
            }
        });
    }

    @Override
    public boolean update(final EquipmentLog equipmentLog) {
        return getJdbcTemplate().execute(new ConnectionCallback<Boolean>() {

            @Override
            public Boolean doInConnection(Connection con) throws SQLException, DataAccessException {
                PreparedStatement ps = null;
                try {
                    ps = con.prepareStatement(SQL_UPDATE);
                    ps.setObject(1, equipmentLog.getPid());
                    ps.setObject(2, equipmentLog.getSid());
                    ps.setObject(3, equipmentLog.getUid());
                    ps.setObject(4, equipmentLog.getRoleName());
                    ps.setObject(5, equipmentLog.getEquipId());
                    ps.setObject(6, equipmentLog.getEquipName());
                    ps.setObject(7, equipmentLog.getAmount());
                    ps.setObject(8, equipmentLog.getRemark());
                    ps.setObject(9, equipmentLog.getCreateTime());
                    ps.setObject(10, equipmentLog.getId());
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
    public EquipmentLog get(int id) {
        return querySingle(SQL_SELECT_BY_KEY, equipmentLogRowMapper, id);
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
    public PageResult<EquipmentLog> find(int page, int pageSize) {
        return fastQueryPage(SQL_FORMAT_SELECT, equipmentLogRowMapper, page, pageSize);
    }

    @Override
    public PageResult<EquipmentLog> find(Order order, int page, int pageSize) {
        StringBuilder sb = new StringBuilder(SQL_FORMAT_SELECT_PREFIX);
        order.toSql(sb);
        return fastQueryPage(sb.toString(), equipmentLogRowMapper, page, pageSize);
    }

    public PageResult<EquipmentLog> find(QueryParams params, int page, int pageSize) {
        StringBuilder sb = new StringBuilder(SQL_FORMAT_SELECT_PREFIX);
        params.toSql(sb);
        sb.append(SQL_ORDER_BY_ID_DESC);
        return fastQueryPage(sb.toString(), equipmentLogRowMapper, page, pageSize, params.toParams());
    }

    public PageResult<EquipmentLog> find(QueryParams params, Order order, int page, int pageSize) {
        StringBuilder sb = new StringBuilder(SQL_FORMAT_SELECT_PREFIX);
        params.toSql(sb);
        order.toSql(sb);
        return fastQueryPage(sb.toString(), equipmentLogRowMapper, page, pageSize, params.toParams());
    }

    @Override
    public List<EquipmentLog> find(int max) {
        return getJdbcTemplate().query(SQL_SELECT_PREFIX + SQL_ORDER_BY_ID_DESC + " LIMIT " + max, equipmentLogRowMapper);
    }

    @Override
    public List<EquipmentLog> find(QueryParams params, int max) {
        StringBuilder sb = new StringBuilder(SQL_SELECT_PREFIX);
        params.toSql(sb);
        sb.append(SQL_ORDER_BY_ID_DESC);
        sb.append(" LIMIT ");
        sb.append(max);
        return getJdbcTemplate().query(sb.toString(), equipmentLogRowMapper, params.toParams());
    }

    @Override
    public List<EquipmentLog> find(QueryParams params, Order order, int max) {
        StringBuilder sb = new StringBuilder(SQL_SELECT_PREFIX);
        params.toSql(sb);
        order.toSql(sb);
        sb.append(" LIMIT ");
        sb.append(max);
        return getJdbcTemplate().query(sb.toString(), equipmentLogRowMapper, params.toParams());
    }
}
