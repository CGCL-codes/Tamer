package org.avaje.ebean.server.jmx;

import org.avaje.ebean.EbeanServer;
import org.avaje.ebean.control.LogControl;
import org.avaje.ebean.control.ServerControl;
import org.avaje.ebean.server.plugin.PluginProperties;

/**
 * Implementation of the LogControl.
 * <p>
 * This is accessible via {@link EbeanServer#getServerControl()} and
 * {@link ServerControl#getLogControl()} or via JMX MBean.
 * </p>
 */
public class MLogControl implements MLogControlMBean, LogControl {

    int queryByIdLevel;

    int queryManyLevel;

    int sqlQueryLevel;

    int insertLevel;

    int updateLevel;

    int deleteLevel;

    int ormUpdateLevel;

    int sqlUpdateLevel;

    int callableSqlLevel;

    boolean debugSql;

    boolean debugLazyLoad;

    /**
	 * Configure from plugin properties.
	 */
    public MLogControl(PluginProperties properties) {
        boolean sqlConsole = properties.getPropertyBoolean("log.sqltoconsole", false);
        debugSql = properties.getPropertyBoolean("debug.sql", sqlConsole);
        debugLazyLoad = properties.getPropertyBoolean("debug.lazyload", false);
        queryByIdLevel = properties.getPropertyInt("log.findid", 1);
        queryManyLevel = properties.getPropertyInt("log.findmany", 1);
        sqlQueryLevel = properties.getPropertyInt("log.findnative", 1);
        int iudLevel = properties.getPropertyInt("log.iud", 1);
        insertLevel = properties.getPropertyInt("log.insert", iudLevel);
        updateLevel = properties.getPropertyInt("log.update", iudLevel);
        deleteLevel = properties.getPropertyInt("log.delete", iudLevel);
        ormUpdateLevel = properties.getPropertyInt("log.ormupdate", iudLevel);
        sqlUpdateLevel = properties.getPropertyInt("log.updatablesql", iudLevel);
        callableSqlLevel = properties.getPropertyInt("log.callablesql", iudLevel);
    }

    public int getSqlQueryLevel() {
        return sqlQueryLevel;
    }

    public void setSqlQueryLevel(int sqlQueryLevel) {
        this.sqlQueryLevel = sqlQueryLevel;
    }

    public int getQueryByIdLevel() {
        return queryByIdLevel;
    }

    public int getQueryManyLevel() {
        return queryManyLevel;
    }

    public int getOrmUpdateLevel() {
        return ormUpdateLevel;
    }

    public void setOrmUpdateLevel(int ormUpdateLevel) {
        this.ormUpdateLevel = ormUpdateLevel;
    }

    public int getDeleteLevel() {
        return deleteLevel;
    }

    public void setDeleteLevel(int deleteLevel) {
        this.deleteLevel = deleteLevel;
    }

    public int getInsertLevel() {
        return insertLevel;
    }

    public void setInsertLevel(int insertLevel) {
        this.insertLevel = insertLevel;
    }

    public int getUpdateLevel() {
        return updateLevel;
    }

    public void setUpdateLevel(int updateLevel) {
        this.updateLevel = updateLevel;
    }

    public int getCallableSqlLevel() {
        return callableSqlLevel;
    }

    public void setCallableSqlLevel(int callableSqlLevel) {
        this.callableSqlLevel = callableSqlLevel;
    }

    public int getSqlUpdateLevel() {
        return sqlUpdateLevel;
    }

    public void setSqlUpdateLevel(int sqlUpdateLevel) {
        this.sqlUpdateLevel = sqlUpdateLevel;
    }

    public void setQueryByIdLevel(int queryByIdLevel) {
        this.queryByIdLevel = queryByIdLevel;
    }

    public void setQueryManyLevel(int queryManyLevel) {
        this.queryManyLevel = queryManyLevel;
    }

    public boolean isDebugGeneratedSql() {
        return debugSql;
    }

    public void setDebugGeneratedSql(boolean debugSql) {
        this.debugSql = debugSql;
    }

    public boolean isDebugLazyLoad() {
        return debugLazyLoad;
    }

    public void setDebugLazyLoad(boolean debugLazyLoad) {
        this.debugLazyLoad = debugLazyLoad;
    }
}
