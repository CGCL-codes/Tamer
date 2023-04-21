package org.easyrec.store.dao.web.impl;

import org.easyrec.store.dao.web.BasicDAO;
import org.easyrec.utils.spring.store.dao.DaoUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import javax.sql.DataSource;
import java.sql.Types;

/**
 * The Class is the Implementation of the Operator DAO
 * <p/>
 * <p>
 * <b>Company:&nbsp;</b> SAT, Research Studios Austria
 * </p>
 * <p/>
 * <p>
 * <b>Copyright:&nbsp;</b> (c) 2007
 * <p/>
 * <b>last modified:</b><br/> $Author: pmarschik $<br/> $Date: 2008-07-17
 * 20:00:46 +0200 (Do, 17 Jul 2008) $<br/> $Revision: 17653 $
 * </p>
 *
 * @author <AUTHOR>
 */
public class BasicDAOMysqlImpl extends JdbcDaoSupport implements BasicDAO {

    private String dbName;

    private String tableName;

    private String tableStringId;

    private String tableId;

    protected static final String DELIMITER = ":::";

    @SuppressWarnings({ "UnusedDeclaration" })
    public BasicDAOMysqlImpl() {
    }

    public BasicDAOMysqlImpl(DataSource dataSource) {
        setDataSource(dataSource);
        dbName = DaoUtils.getDatabaseURLAndUserName(dataSource);
        logger.debug(dbName);
    }

    public String getDbName() {
        return dbName.substring(dbName.indexOf("/") + 2, dbName.indexOf("(") - 1);
    }

    public String getDbUserName() {
        return dbName.substring(dbName.indexOf("(") + 1, dbName.length() - 1);
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    public String getTableName() {
        return tableName;
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    public String getTableId() {
        return tableId;
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    public String getTableStringId() {
        return tableStringId;
    }

    public void setTableStringId(String tableStringId) {
        this.tableStringId = tableStringId;
    }

    public int count() {
        StringBuilder sql = new StringBuilder().append(" SELECT Count(1) FROM ").append(tableName);
        try {
            return getJdbcTemplate().queryForInt(sql.toString(), null, null);
        } catch (Exception e) {
            logger.debug(e);
            return 0;
        }
    }

    public boolean exists(String stringId) {
        StringBuilder sql = new StringBuilder().append(" SELECT Count(1) FROM ").append(tableName).append(" WHERE ").append(tableStringId).append("  = ? ");
        Object[] args = { stringId };
        int[] argTypes = { Types.VARCHAR };
        try {
            return getJdbcTemplate().queryForInt(sql.toString(), args, argTypes) > 0;
        } catch (Exception e) {
            logger.debug(e);
            return false;
        }
    }

    public boolean exists(int id) {
        StringBuilder sql = new StringBuilder().append(" SELECT Count(1) FROM ").append(tableName).append(" WHERE ").append(tableId).append("  = ? ");
        Object[] args = { id };
        int[] argTypes = { Types.INTEGER };
        try {
            return getJdbcTemplate().queryForInt(sql.toString(), args, argTypes) > 0;
        } catch (Exception e) {
            logger.debug(e);
            return false;
        }
    }
}
