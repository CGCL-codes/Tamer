package cn.sharezoo.dao.impl;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import cn.sharezoo.dao.PrivateMessageDao;
import cn.sharezoo.utils.SqlLoader;

public class PrivateMessageDaoImpl implements PrivateMessageDao {

    private SqlLoader sqlLoader;

    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public SqlLoader getSqlLoader() {
        return sqlLoader;
    }

    public void setSqlLoader(SqlLoader sqlLoader) {
        this.sqlLoader = sqlLoader;
    }

    public void delete(int pmId, int userId) {
        getJdbcTemplate().update(getSqlLoader().getSql("PrivateMessageModel.delete"), new Object[] { new Integer(pmId) }, new int[] { Types.INTEGER });
        getJdbcTemplate().update(getSqlLoader().getSql("PrivateMessagesModel.deleteText"), new Object[] { new Integer(pmId) }, new int[] { Types.INTEGER });
    }

    public void deleteByUserId(int userId) {
        List pmsList = getJdbcTemplate().queryForList(getSqlLoader().getSql("PrivateMessageModel.selectPrivmsgByUserId"), new Object[] { new Integer(userId), new Integer(userId) }, new int[] { Types.INTEGER, Types.INTEGER });
        for (Iterator iter = pmsList.iterator(); iter.hasNext(); ) {
            Map map = (Map) iter.next();
            int privmsgsId = ((BigDecimal) map.get("privmsgs_id")).intValue();
            this.delete(privmsgsId, userId);
        }
    }
}
