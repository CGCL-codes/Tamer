package org.dbmaintain.dbsupport.impl;

import org.dbmaintain.dbsupport.SQLHandler;
import org.dbmaintain.dbsupport.StoredIdentifierCase;
import javax.sql.DataSource;
import java.util.Set;

/**
 * @author Filip Neven
 * @author Tim Ducheyne
 */
public class Oracle10DbSupport extends OracleDbSupport {

    /**
     * Creates support for a Oracle 10 database. Normally you don't need to use this class: OracleDbSupport
     * can find out the version automatically. Use this class only if your driver is not capable to 
     * retrieve the oracle version automatically.
     * 
     * @param databaseName 
     * @param dataSource 
     * @param defaultSchemaName 
     * @param schemaNames 
     * @param sqlHandler 
     * @param customIdentifierQuoteString 
     * @param customStoredIdentifierCase 
     */
    public Oracle10DbSupport(String databaseName, DataSource dataSource, String defaultSchemaName, Set<String> schemaNames, SQLHandler sqlHandler, String customIdentifierQuoteString, StoredIdentifierCase customStoredIdentifierCase) {
        super(databaseName, "oracle10", dataSource, defaultSchemaName, schemaNames, sqlHandler, customIdentifierQuoteString, customStoredIdentifierCase);
    }

    @Override
    protected Integer getOracleMajorVersionNumber() {
        return 10;
    }
}
