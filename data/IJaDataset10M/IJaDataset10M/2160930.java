package org.gjt.mm.mysql;

import java.util.Hashtable;
import javax.naming.*;
import javax.naming.spi.ObjectFactory;

/**
 * Factory class for MysqlDataSource objects
 */
public class MysqlDataSourceFactory implements ObjectFactory {

    /**
     * The class name for a standard Mysql DataSource.
     */
    protected final String DataSourceClassName = "org.gjt.mm.mysql.MysqlDataSource";

    public Object getObjectInstance(Object RefObj, Name Nm, Context Ctx, Hashtable Env) throws Exception {
        Reference Ref = (Reference) RefObj;
        if (Ref.getClassName().equals(DataSourceClassName)) {
            MysqlDataSource MDS = new MysqlDataSource();
            int port_no = 1306;
            port_no = Integer.parseInt((String) Ref.get("port").getContent());
            MDS.setPort(port_no);
            MDS.setUser((String) Ref.get("user").getContent());
            MDS.setPassword((String) Ref.get("password").getContent());
            MDS.setServerName((String) Ref.get("serverName").getContent());
            MDS.setDatabaseName((String) Ref.get("databaseName").getContent());
            return MDS;
        } else {
            return null;
        }
    }
}
