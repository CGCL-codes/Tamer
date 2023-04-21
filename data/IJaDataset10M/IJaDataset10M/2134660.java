package org.sss.housekeeping.store.db;

import org.sss.housekeeping.cfg.StoreConfig;
import org.sss.housekeeping.store.DatabaseStore;

/**
 * IBM DB2數據庫連接形式
 * @author Jason Hoo (latest modification by $Author: hujianxin78728 $)
 * @version $Revision: 406 $ $Date: 2009-06-10 08:09:48 -0400 (Wed, 10 Jun 2009) $
 */
public class DB2Store extends DatabaseStore {

    public DB2Store(StoreConfig storeConfig) {
        super(storeConfig);
    }

    public String getUrl() {
        return "jdbc:db2://" + super.getStoreConfig().getServerName() + ":" + super.getStoreConfig().getServerPort() + "/" + super.getStoreConfig().getPathOrAlias();
    }

    public String getDriverClassName() {
        return "com.ibm.db2.jcc.DB2Driver";
    }

    public String getExistsTableSQL(String schemaName, String tableName) {
        if (tableName == null || tableName.equals("")) {
            return "SELECT table_name FROM sysibm.tables WHERE table_type='BASE TABLE' AND table_schema='" + schemaName + "' AND table_name='" + tableName + "'";
        } else {
            return "SELECT table_name FROM sysibm.tables WHERE table_type='BASE TABLE' AND table_schema='" + schemaName + "'";
        }
    }

    public String getCopyTableSQL(String sourceTableName, String targetTableName) {
        return "CREATE TABLE " + targetTableName + " LIKE " + sourceTableName + " INCLUDING DEFAULTS";
    }

    protected String getReportDBInsertSQL() {
        return "INSERT INTO DBTRC(INR,OBJTAB,OBJINR,TRCDAT,UPDDAT,DONFLG) VALUES ( SUBSTR(DIGITS(NEXT VALUE FOR coudtr),3),?,?,?,null,?)";
    }

    protected String getReprotFileInsertSQL() {
        return "INSERT INTO FILETRC(INR,PATHNAME,FILENAME,TRCDAT,UPDDAT,DONFLG) VALUES ( SUBSTR(DIGITS(NEXT VALUE FOR couftr),3),?,?,?,null,?)";
    }

    protected String getDateDiffSQL(String dateField) {
        return "CURRENT DATE - DATE(" + dateField + ")";
    }
}
