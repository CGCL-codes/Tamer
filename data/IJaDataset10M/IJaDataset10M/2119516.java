package net.pmonks.DAL.generator.schema;

import java.util.*;
import java.security.*;
import java.sql.*;
import org.apache.log4j.*;
import org.apache.log4j.xml.*;
import net.pmonks.DAL.generator.config.*;

public class ForeignKeyDefinition {

    /**
     * Log4J category used for logging by the class.
     */
    static Category cat = Category.getInstance(ForeignKeyDefinition.class.getName());

    protected final String DB_CONNECTION_NAME = "CONNECTION_1";

    protected boolean foreignKeyExists = false;

    protected String foreignKeyName = null;

    protected List relationshipDefinitionList = null;

    public ForeignKeyDefinition(DBConnectionManager dbConMgr, String foreignKeyName, String FKTableName, List FKColumnNames, List FKColumns, String PKTableName, List PKColumnNames, List PKColumns) throws Exception, InvalidParameterException, SQLException {
        relationshipDefinitionList = new ArrayList();
        if (FKColumnNames.size() > 0) {
            foreignKeyExists = true;
            this.foreignKeyName = foreignKeyName;
            for (int i = 0; i < FKColumnNames.size(); i++) {
                relationshipDefinitionList.add(new ForeignKeyColumnPair(dbConMgr, FKTableName, (String) FKColumnNames.get(i), (ColumnInfo) FKColumns.get(i), PKTableName, (String) PKColumnNames.get(i), (ColumnInfo) PKColumns.get(i)));
            }
        } else {
            foreignKeyExists = false;
            this.foreignKeyName = null;
        }
    }

    public boolean exists() {
        return (foreignKeyExists);
    }

    public String getForeignKeyName() {
        return (foreignKeyName);
    }

    public List getRelationshipDefinitionList() {
        return (relationshipDefinitionList);
    }
}
