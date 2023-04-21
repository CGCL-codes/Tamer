package com.avdheshyadav.p4j.jdbc.dbms.connfactory;

import com.avdheshyadav.p4j.common.DbmsType;
import com.avdheshyadav.p4j.jdbc.dbms.DataSourceAttr;

/**
 * @author Avdhesh Yadav - Avdhesh.Yadav@Gmail.com    
 */
public abstract class ConnectionFactory {

    /**
	 * @param xaApiProvider String
	 * @param database String
	 * @param dsAttr DataSourceAttr
	 * 
	 * @return XaDBConnector
	 * 
	 * @throws Exception
	 */
    public abstract XaDBConnector newXADBConnector(String xaApiProvider, String database, DataSourceAttr dsAttr) throws Exception;

    /**
	 * 
	 * @param database String
	 * @param dsAttr DataSourceAttr
	 * 
	 * @return DBConnector
	 * @throws Exception
	 */
    public abstract DBConnector newNonXADBConnector(String database, DataSourceAttr dsAttr) throws Exception;

    /**
	 * 
	 * @param whichFactory String
	 * 
	 * @return ConnectionFactory ConnectionFactory
	 */
    public static ConnectionFactory getConnectionFactory(String whichFactory) {
        ConnectionFactory factory = null;
        if (whichFactory.equalsIgnoreCase(DbmsType.MYSQL)) {
            factory = DefaultConnectionFactory.getInstance();
        } else if (whichFactory.equalsIgnoreCase(DbmsType.PGSQL)) {
            factory = DefaultConnectionFactory.getInstance();
        } else if (whichFactory.equalsIgnoreCase(DbmsType.DERBY)) {
            factory = DerbyConnectionFactory.getInstance();
        } else if (whichFactory.equalsIgnoreCase(DbmsType.HSQLDB)) {
            factory = DerbyConnectionFactory.getInstance();
        } else if (whichFactory.equalsIgnoreCase(DbmsType.H2)) {
            factory = DefaultConnectionFactory.getInstance();
        }
        return factory;
    }
}
