package org.colimas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.colimas.db.DBException;
import org.colimas.db.mapper.DbMapper;
import org.colimas.db.mapper.I_Query;
import org.colimas.db.mapper.SQLQueries;
import org.colimas.db.mapper.SQLQueriesCodes;
import org.colimas.entity.I_Entity;
import org.colimas.entity.SCMEntity;
import org.colimas.services.logs.LogWeb;

/**
 * <h3>SCMEntityMapper.java</h3>
 * 
 * <P>
 * Function:<BR />
 * Process Query of SCM table in DB
 * </P>
 * @author zhao lei
 * @version 1.4
 *
 * Modification History:
 * <PRE>
 * SEQ DATE       ORDER DEVELOPER      DESCRIPTION
 * --- ---------- ----- -------------- -----------------------------
 * 001 2005/12/04          zhao lei       INIT
 * 002 2006/01/08          zhao lei       coding
 * 003 2006/01/14          zhao lei       change the return type of createEntity
 * 004 2006/03/19          zhao lei       delete runfilepath, makefilepath and parameters
 * </PRE>
 */
public class SCMEntityMapper extends DbMapper implements SQLQueriesCodes {

    /**<p> SCMEntityMapper</p> */
    private static SCMEntityMapper mapper;

    private LogWeb log = new LogWeb(this);

    /**
     * <p>
     * get Mapper instance(using Singleton Pattern)
     * </p>
     * @return Mapper Object
     */
    public static SCMEntityMapper getInstance() {
        if (mapper == null) {
            mapper = new SCMEntityMapper();
        }
        return mapper;
    }

    private SCMEntityMapper() {
        super();
    }

    /**
     * <p>
     * Create SCMEntity
     * </p>
     * @param Result of searching
     * @return I_Entity[]
     */
    protected I_Entity[] createEntity(ResultSet rs) {
        SCMEntity entity = new SCMEntity();
        populate(entity, rs);
        I_Entity[] entities = { entity };
        return entities;
    }

    /**
     * <p>
     * set SCM Entity
     * </p>
     * @param result
     * @return SCM Entity
     */
    private void populate(SCMEntity entity, ResultSet rs) {
        try {
            entity.setHost(rs.getString(1));
            entity.setComponentSerialNo(rs.getString(2));
            entity.setScmType(rs.getString(3));
            entity.setUserid(rs.getString(4));
            entity.setPass(rs.getString(5));
            entity.setRepository(rs.getString(6));
            entity.setModule(rs.getString(7));
            entity.setConnectionType(rs.getString(8));
            entity.setPort(rs.getString(9));
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * <p>
     * run select
     * </p>
     * @param query object
     * @return get result list
     */
    public List doQuery(I_Query query) {
        log.debug("get query string");
        String sql = query.getQueryString();
        List cl = null;
        try {
            cl = getEntityList(sql);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return cl;
    }

    /**
     * <p>
     * insert SCMEntity
     * </p>
     * @param SCMEntity
     * @return inserted num
     */
    public int insert(SCMEntity entity) {
        List column = new ArrayList();
        int rc = 0;
        try {
            String sql = null;
            log.debug("begin insert");
            sql = SQLQueries.getQuery(SQL_SCM_INSERT);
            column.add(0, entity.getHost());
            column.add(1, entity.getComponentSerialNo());
            column.add(2, entity.getScmType());
            column.add(3, entity.getUserid());
            column.add(4, entity.getPass());
            column.add(5, entity.getRepository());
            column.add(6, entity.getModule());
            column.add(7, entity.getConnectionType());
            column.add(8, entity.getPort());
            rc = super.updateEntity(sql, column);
            log.debug("insert " + rc + " record(s)");
        } catch (DBException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return rc;
    }

    /**
     * <p>
     * soft delete/recovery
     * set delete=1 to delete
     * set delete=0 to recovery
     * </p>
     * @param delete0
     * @return updated num
     * 
     */
    public int update(SCMEntity entity, int delete0) {
        int rc = 0;
        List column = new ArrayList();
        try {
            String sql = null;
            log.debug("begin update");
            sql = SQLQueries.getQuery(SQL_SCM_SOFTDELETE);
            log.debug("sql:" + sql);
            column.add(0, new Integer(delete0));
            column.add(1, entity.getHost());
            column.add(2, entity.getRepository());
            column.add(3, entity.getModule());
            column.add(4, entity.getConnectionType());
            column.add(5, entity.getComponentSerialNo());
            rc = super.updateEntity(sql, column);
            log.debug("update " + rc + " record(s)");
        } catch (DBException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return rc;
    }

    /**
     * <p>
     * update SCM entity
     * </p>
     * @param SCMEntity
     * @return updated num
     * 
     */
    public int update(SCMEntity entity) {
        return update(entity, false);
    }

    /**
     * <p>update SCMEntity</p>
     * @param entity
     * @param isAutoCommit
     * @return updated num
     */
    public int update(SCMEntity entity, boolean isAutoCommit) {
        int rc = 0;
        List column = new ArrayList();
        try {
            String sql = null;
            log.debug("begin update");
            sql = SQLQueries.getQuery(SQL_SCM_UPDATE);
            log.debug("sql:" + sql);
            String key = entity.getComponentSerialNo();
            column.add(0, entity.getHost());
            column.add(1, new Integer(entity.getScmType()));
            column.add(2, entity.getUserid());
            column.add(3, entity.getPass());
            column.add(4, entity.getRepository());
            column.add(5, entity.getModule());
            column.add(6, entity.getConnectionType());
            column.add(7, entity.getPort());
            rc = super.updateEntity(sql, column, key, isAutoCommit);
            log.debug("update " + rc + " record(s)");
        } catch (DBException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return rc;
    }

    /**
     * <p>
     * delete entity
     * </p>
     * @param SCMEntity
     * @return updated num
     */
    public int delete(SCMEntity entity) {
        int rc = 0;
        List column = new ArrayList();
        try {
            String sql = null;
            log.debug("begin delete");
            sql = SQLQueries.getQuery(SQL_SCM_DELETE);
            log.debug("sql:" + sql);
            column.add(0, entity.getHost());
            column.add(1, entity.getRepository());
            column.add(2, entity.getModule());
            column.add(3, entity.getConnectionType());
            column.add(4, entity.getComponentSerialNo());
            rc = super.updateEntity(sql, column);
            log.debug("delete " + rc + " record(s)");
        } catch (DBException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return rc;
    }
}
