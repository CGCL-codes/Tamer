package com.avdheshyadav.p4j.jdbc.dbms;

import org.apache.log4j.Logger;
import com.avdheshyadav.p4j.common.DbmsType;
import com.avdheshyadav.p4j.jdbc.PersistenceConfig;
import com.avdheshyadav.p4j.jdbc.common.CRUD;
import com.avdheshyadav.p4j.jdbc.common.GenericCRUD;
import com.avdheshyadav.p4j.jdbc.dao.AbsPersistenceManager;
import com.avdheshyadav.p4j.jdbc.dao.DefaultPersistenceManager;
import com.avdheshyadav.p4j.jdbc.dbms.connfactory.ConnectionFactory;
import com.avdheshyadav.p4j.jdbc.dbms.connfactory.DBConnector;
import com.avdheshyadav.p4j.jdbc.dbms.metadata.IMetaDataLoader;
import com.avdheshyadav.p4j.jdbc.dbms.metadata.MetaDataLoader;

/**
 * @author Avdhesh Yadav
 */
class MySqlDbms extends Dbms {

    static Logger logger = Logger.getLogger(MySqlDbms.class.getName());

    private static Dbms _Instance;

    /**
	 * 
	 * @param props PersistenceConfig
	 * @return Dbms
	 */
    public static Dbms getInstance(PersistenceConfig config) {
        if (_Instance == null) {
            _Instance = new MySqlDbms(config);
        }
        return _Instance;
    }

    /**
	 * 
	 * @param config PersistenceConfig
	 */
    private MySqlDbms(PersistenceConfig config) {
        super(config);
        m_ConnFactory = ConnectionFactory.getConnectionFactory(DbmsType.MYSQL);
        mDsAttr = constructDataSourceAttr(DbmsType.MYSQL);
    }

    /**
	 * 
	 * @param database String
	 * 
	 * @throws Exception
	 */
    public void createDataSource(String database, boolean isTransactional) throws Exception {
        logger.info("createDataSource in mysql:" + database);
        boolean isNew = isNewDataSource(database);
        if (isNew) {
            DBConnector connector = getDBConnector(database, isTransactional);
            IMetaDataLoader loader = new MetaDataLoader(connector);
            CRUD crud = new GenericCRUD();
            AbsPersistenceManager pcManager = new DefaultPersistenceManager(loader, crud);
            m_PcManagerMap.put(database, pcManager);
        }
    }
}
