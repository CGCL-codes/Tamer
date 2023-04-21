package fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement.DbMode;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;

/**
 * @author AYADI
 * 
 */
public class DbModeFactory {

    /**
	 * 
	 */
    public DbModeFactory() {
    }

    public static IDbMode getInstance(int type) {
        switch(DbUtils.getDbType(type)) {
            case ConfigConst.BD_MYSQL:
                return new MySqlMode(type);
            case ConfigConst.BD_ORACLE:
                return new OracleMode(type);
            default:
                return null;
        }
    }
}
