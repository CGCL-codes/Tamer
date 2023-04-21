package ca.sqlpower.architect.ddl;

import java.sql.SQLException;
import ca.sqlpower.sqlobject.SQLRelationship;
import ca.sqlpower.sqlobject.SQLRelationship.UpdateDeleteRule;

public class SQLServer2000DDLGenerator extends SQLServerDDLGenerator {

    public static final String GENERATOR_VERSION = "$Revision: 2099 $";

    public SQLServer2000DDLGenerator() throws SQLException {
        super();
    }

    @Override
    public void writeHeader() {
        println("-- Created by SQLPower SQLServer 2000 DDL Generator " + GENERATOR_VERSION + " --");
    }

    @Override
    public String getName() {
        return "Microsoft SQL Server 2000";
    }

    @Override
    protected String getPlatformName() {
        return "SQL Server 2000";
    }

    @Override
    public boolean supportsDeleteAction(SQLRelationship r) {
        UpdateDeleteRule action = r.getDeleteRule();
        return (action == UpdateDeleteRule.CASCADE) || (action == UpdateDeleteRule.NO_ACTION);
    }

    @Override
    public boolean supportsUpdateAction(SQLRelationship r) {
        UpdateDeleteRule action = r.getUpdateRule();
        return (action == UpdateDeleteRule.CASCADE) || (action == UpdateDeleteRule.NO_ACTION);
    }

    @Override
    public String getStatementTerminator() {
        return ";";
    }
}
