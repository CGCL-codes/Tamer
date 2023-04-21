package com.guanghua.brick.db;

import java.sql.Connection;
import javax.sql.DataSource;
import com.guanghua.brick.BrickConstant;
import com.guanghua.brick.util.IConfig;

/**
 * 获取数据库连接方法的HB实现
 * @author leeon
 */
public class HBSQLConnection implements ISQLConnection {

    public Connection createConnection(String cfg) throws Exception {
        IConfig config = BrickConstant.APP_DB_CONN_CONFIG;
        if (cfg != null) {
            return HBUtil.getCurrentSession(cfg).connection();
        } else if (config.getText("default-cfg") != null) {
            return HBUtil.getCurrentSession(config.getText("default-cfg")).connection();
        } else {
            return HBUtil.getCurrentSession().connection();
        }
    }

    public DataSource createDataSource(String cfg) throws Exception {
        throw new Exception("not support create datasource in url hbsql connection create impler");
    }
}
