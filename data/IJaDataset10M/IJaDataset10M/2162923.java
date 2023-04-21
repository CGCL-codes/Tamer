package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.HdbAttributeInsert;

import org.slf4j.Logger;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public final class HdbAttributeInsertFactory {

    private HdbAttributeInsertFactory() {
    }

    public static IHdbAttributeInsert getInstance(final int archType, final IAdtAptAttributes attr, final Logger logger) throws ArchivingException {
        final int type = DbUtils.getDbType(archType);
        switch(type) {
            case ConfigConst.HDB_MYSQL:
                return new HDBMySqlAttributeInsert(archType, attr, logger);
            case ConfigConst.HDB_ORACLE:
                return new HDBOracleAttributeInsert(archType, attr, logger);
        }
        return null;
    }
}
