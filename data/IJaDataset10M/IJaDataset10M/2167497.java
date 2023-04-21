package dbgate.ermanagement.impl.dbabstractionlayer;

import dbgate.dbutility.DBConnector;

/**
 * Created by IntelliJ IDEA.
 * User: Adipa
 * Date: Sep 12, 2010
 * Time: 12:50:18 PM
 */
public class LayerFactory {

    public static IDBLayer createLayer(int dbType) {
        switch(dbType) {
            case DBConnector.DB_ACCESS:
                return new AccessDBLayer();
            case DBConnector.DB_ORACLE:
                return new OracleDBLayer();
            case DBConnector.DB_MYSQL:
                return new MySqlDBLayer();
            default:
                return new DefaultDBLayer();
        }
    }
}
