package com.frameworkset.common.poolman.sql;

import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.frameworkset.common.poolman.util.SQLManager;

/**
 * ����ȫ����ݿ����ӳص�����������Ϣ
 * @author biaoping.yin
 * created on 2005-3-30
 * version 1.0
 */
public class PrimaryKeyCacheManager {

    private static Logger log = Logger.getLogger(PrimaryKeyCacheManager.class);

    private Map primaryKeyCaches;

    private static PrimaryKeyCacheManager self;

    private PrimaryKeyCacheManager() {
        primaryKeyCaches = Collections.synchronizedMap(new HashMap());
    }

    public static PrimaryKeyCacheManager getInstance() {
        if (self == null) self = new PrimaryKeyCacheManager();
        return self;
    }

    public void addPrimaryKeyCache(PrimaryKeyCache primaryKeyCache) {
        primaryKeyCaches.put(primaryKeyCache.getDbname(), primaryKeyCache);
    }

    public boolean removePrimaryKeyCache(String dbname) {
        if (primaryKeyCaches.remove(dbname) != null) return true;
        return false;
    }

    public PrimaryKeyCache getPrimaryKeyCache(String dbname) {
        if (SQLManager.getInstance().getPool(dbname) == null) return null;
        String _dbname = SQLManager.getRealDBNameFromExternalDBNameIfExist(dbname);
        PrimaryKeyCache keyCache = (PrimaryKeyCache) primaryKeyCaches.get(_dbname);
        if (keyCache != null) return keyCache;
        return keyCache;
    }

    public PrimaryKey loaderPrimaryKey(String dbname, String tableName) {
        return loaderPrimaryKey(null, dbname, tableName);
    }

    /**
     * ����ݿ��м��ر��������Ϣ
     * @param con
     * @param dbname
     * @param tableName
     * @return
     */
    public PrimaryKey loaderPrimaryKey(Connection con, String dbname, String tableName) {
        return getPrimaryKeyCache(dbname).loaderPrimaryKey(con, tableName);
    }
}
