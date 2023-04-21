package com.daffodilwoods.daffodildb.server.datadictionarysystem;

import com.daffodilwoods.daffodildb.server.serversystem.*;
import com.daffodilwoods.daffodildb.server.sql99.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.ddl.descriptors.*;
import com.daffodilwoods.daffodildb.server.sql99.ddl.utility.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.execution.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.iterator.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.booleanvalueexpression.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.parser.*;
import com.daffodilwoods.daffodildb.utils.*;
import com.daffodilwoods.database.general.*;
import com.daffodilwoods.database.resource.*;

public class PreparedStatementGetter {

    _ServerSession serverSession;

    private _Executer indexInfoExecuter;

    private _Executer indexColumnsExecuter;

    private _Executer fullTextIndexColumnsExecuter;

    private _Executer tableDescriptorExecuterForCheckConstraints;

    private _Executer tableDescriptorExecuterForCheckConstraintsNonDef;

    private _Executer tablesForeignConstriaintExecuter;

    private _Executer primaryKeysExecuter;

    private _Executer relatedTableAndColumnExecuter;

    private _Executer columnExecuter;

    private _Executer dataTypeExecuter;

    private _Executer tableTypesAndCodesExecuter;

    private _Executer checkIsIndexTableExecuter;

    private _Executer domainConstraintsExecuter;

    private _Executer deferredCheckingExecuter;

    private _Executer defalutClauseExecuter;

    private _Executer tableDescriptorExecuterForPrimaryAndUniqueConstraint;

    private _Executer tableDescriptorExecuterForPrimaryAndUniqueConstraintNonDef;

    private _Executer tablePrivilegesForTableExecuter;

    private _Executer columnPrivilegesForColumnExecuter;

    private _Executer referencedConstraintsJoinExecuter;

    private _Executer referencedConstraintsJoinExecuterNonDef;

    private _Executer referencingConstraintsJoinExecuter;

    private _Executer referencingConstraintsJoinExecuterNonDef;

    private _Executer trigersExecuterWithEventCondition;

    private _Executer trigersExecuterWithTriggerNameCondition;

    private _Executer triggeredUpdatedExecuterForAllColumns;

    private _Executer triggerExecuterForUpdateType;

    private _Executer viewsExecuterWithNameCondition;

    private _Executer checkConstraintExecuter;

    private _Executer tableConstraintExecuter;

    private _Executer keyColumnUsageExecuter;

    private _Executer triggerUpdateColumnsExecuter;

    private _Executer checkTableUsedExecuter;

    private _Executer checkColumnUsedExecuter;

    private _Executer routinePrivilegesExecuter;

    private _Executer hasAnyTriggerOnViewExecuter;

    private _Executer isDateSpanTableExecuter;

    private _Executer viewsForTableExecuter;

    private _Executer materializedViewsFromViewsExecuter;

    private _Executer mataerailzedQueryDefinitionExecuter;

    private _Executer isValidRoleExecuter;

    private _Executer tablesTableExecuter;

    private _Executer columnsTableExecuter;

    private _Executer dataTypeDescriptorTableExecuter;

    private _Executer referentialConstraintsTableExecuter;

    private _Executer keyColumnUsageTableExecuter;

    private _Executer columnPrivilegesTableExecuter;

    private _Executer tablePrivlegesTableExecuter;

    private _Executer schemataTableExecuter;

    private _Executer domainTableExecuter;

    private _Executer domianConstrainttaTableExecuter;

    private _Executer tableConstraintExecuterForPrimaryKey;

    private _Executer RoleAuthorizationsExecterforGranteeOnly;

    private _Executer allRoleAuthorizationsExecterforGranteeOnly;

    private _Executer allRoleAuthorizationsExecuterforGranteeAndPublic;

    private _Executer fulltextindexExecuter;

    private _Executer userValidity;

    private _Executer roleValidity;

    private _Executer domainValidity;

    private _Executer columnsExecuter;

    private _Executer tableDependent_A;

    private _Executer tableDependent_B;

    private _Executer columnDependent_A;

    private _Executer columnDependent_B;

    private _Executer routineDependent_A;

    private _Executer usageDependent_A;

    private _Executer dependent_C;

    private _Executer domainCheckConstaint;

    private _Executer usageDependent_C_Column;

    private _Executer usageDependent_C_Usage;

    private _Executer primaryConstraintExecuter;

    private _Executer checkColumnConstraintExecuter;

    private _Executer checkDomainConstraintExecuter;

    private _Executer checkTableConstraintExecuter;

    private _Executer executerTriggerOnTableForUser;

    private _Executer executerAbandonedViewForColumn;

    private _Executer executerAbandonedViewForTable;

    private _Executer indexExecuter;

    private _Executer deleteConstraintExecuter;

    private _Executer referentialConstraintExecuter;

    private _Executer constraintExecuter;

    private _Executer viewTableUsageExecuter;

    private _Executer triggerTableUsageExecuter;

    private _Executer routineTableUsageExecuter;

    private _Executer tableReferencedCheckConstraintExecuter;

    private _Executer conditionIndexInfoExecuter;

    private _Executer dependentReferentialConstraintExecuter;

    private _Executer sequenceExecuter;

    private _Executer fullTextInfoExecuter;

    private _Executer fullTextColumnInfoExecuter;

    private _Executer referencedConstraintExecuter;

    private _Executer keyColumnExecuter;

    private booleanvalueexpression tablesTableCondition;

    private booleanvalueexpression columnsTableCondition;

    private booleanvalueexpression dataTypeDescriptorTableCondition;

    private booleanvalueexpression tableConstraintsTableCondition;

    private booleanvalueexpression checkConstraintsTableCondition;

    private booleanvalueexpression referentialConstraintsTableCondition;

    private booleanvalueexpression keyColumnUsageTableCondition;

    private booleanvalueexpression columnPrivilegesTableCondition;

    private booleanvalueexpression tablePrivlegesTableCondition;

    private booleanvalueexpression checkTableUsageTableCondition;

    private booleanvalueexpression checkColumnUsageTableCondition;

    private booleanvalueexpression indexInfoTableCondition;

    private booleanvalueexpression fullTextIndexInfoTableCondition;

    private _Iterator columnsIter;

    private TableDetails columnsTableDetail;

    private _Iterator columnPrivilegesIter;

    private TableDetails columnsPrivilegeTableDetail;

    private _Iterator tablePrivilegesIter;

    private TableDetails tablePrivilegeTableDetail;

    private _Iterator dataTypeIter;

    private TableDetails dataTypeTableDetail;

    public PreparedStatementGetter(_ServerSession serverSession0) {
        serverSession = serverSession0;
    }

    public _Executer getIndexInfoExecuter() throws DException {
        if (indexInfoExecuter == null) {
            String indexInfoQuery = "Select * from " + SystemTables.INDEXINFO + " where table_catalog = ? and table_schema = ? and table_name = ? ";
            indexInfoExecuter = (_Executer) serverSession.executeQueryParameterised(indexInfoQuery, 0);
        }
        return indexInfoExecuter;
    }

    public _Executer getFullTextIndexExecuter() throws DException {
        if (fulltextindexExecuter == null) {
            String indexInfoQuery = "Select * from " + SystemTables.FULLTEXTINFO + " where table_catalog = ? and table_schema = ? and table_name = ? ";
            fulltextindexExecuter = (_Executer) serverSession.executeQueryParameterised(indexInfoQuery, 0);
        }
        return fulltextindexExecuter;
    }

    public _Executer getIndexColumnsExecuter() throws DException {
        if (indexColumnsExecuter == null) {
            String indexColumnsQuery = "Select * from " + SystemTables.INDEXCOLUMNS + " where table_catalog = ? and table_schema = ? and table_name = ? and indexname = ? order by ordinal_position";
            indexColumnsExecuter = (_Executer) serverSession.executeQueryParameterised(indexColumnsQuery, 0);
        }
        return indexColumnsExecuter;
    }

    public _Executer getFullTextIndexColumnsExecuter() throws DException {
        if (fullTextIndexColumnsExecuter == null) {
            String indexColumnsQuery = "Select * from " + SystemTables.FULLTEXTCOLUMNINFO + " where table_catalog = ? and table_schema = ? and table_name = ? and indexname = ? order by ordinal_position";
            fullTextIndexColumnsExecuter = (_Executer) serverSession.executeQueryParameterised(indexColumnsQuery, 0);
        }
        return fullTextIndexColumnsExecuter;
    }

    public _Executer getTableDescriptorExecuterForCheckConstraints() throws DException {
        if (tableDescriptorExecuterForCheckConstraints == null) {
            String tableDescriptorQueryForCheckConstraints = "select * from " + SystemTables.table_constraints_TableName + " where table_catalog = ? and table_schema = ? and table_name = ? and constraint_type = '" + SqlKeywords.CHECK + "' and is_deferrable = 'YES' and initially_deferred = 'YES'";
            tableDescriptorExecuterForCheckConstraints = (_Executer) serverSession.executeQueryParameterised(tableDescriptorQueryForCheckConstraints, 0);
        }
        return tableDescriptorExecuterForCheckConstraints;
    }

    public _Executer getTableDescriptorExecuterForCheckConstraintsNonDef() throws DException {
        if (tableDescriptorExecuterForCheckConstraintsNonDef == null) {
            String tableDescriptorQueryForCheckConstraints = "select * from " + SystemTables.table_constraints_TableName + " where table_catalog = ? and table_schema = ? and table_name = ? and constraint_type = '" + SqlKeywords.CHECK + "' and (is_deferrable = 'NO' or (is_deferrable = 'YES' and initially_deferred = 'NO')) ";
            tableDescriptorExecuterForCheckConstraintsNonDef = (_Executer) serverSession.executeQueryParameterised(tableDescriptorQueryForCheckConstraints, 0);
        }
        return tableDescriptorExecuterForCheckConstraintsNonDef;
    }

    public _Executer getTablesForeignConstriaintExecuter() throws DException {
        if (tablesForeignConstriaintExecuter == null) {
            String tablesForeignConstriaintQuery = " Select " + SystemTables.key_column_usage_TableName + ".constraint_catalog, " + SystemTables.key_column_usage_TableName + ".constraint_schema , " + SystemTables.key_column_usage_TableName + ".constraint_name, " + SystemTables.key_column_usage_TableName + ".ordinal_position " + " from " + SystemTables.key_column_usage_TableName + "," + SystemTables.table_constraints_TableName + " where " + SystemTables.key_column_usage_TableName + ".table_catalog = ? and " + SystemTables.key_column_usage_TableName + ".table_schema = ? and " + SystemTables.key_column_usage_TableName + ".table_name = ? and " + SystemTables.key_column_usage_TableName + ".column_name = ?" + " and (" + SystemTables.table_constraints_TableName + ".constraint_catalog = " + SystemTables.key_column_usage_TableName + ".constraint_catalog and " + SystemTables.table_constraints_TableName + ".constraint_schema = " + SystemTables.key_column_usage_TableName + ".constraint_schema and " + SystemTables.table_constraints_TableName + ".constraint_name = " + SystemTables.key_column_usage_TableName + ".constraint_name " + " ) " + " and " + SystemTables.table_constraints_TableName + ".constraint_type = '" + SqlKeywords.FOREIGN + " " + SqlKeywords.KEY + "\'";
            tablesForeignConstriaintExecuter = (_Executer) serverSession.executeQueryParameterised(tablesForeignConstriaintQuery, 0);
        }
        return tablesForeignConstriaintExecuter;
    }

    public _Executer getPrimaryKeysExecuter() throws DException {
        if (primaryKeysExecuter == null) {
            String primaryKeysQuery = " Select " + SystemTables.key_column_usage_TableName + ".column_name" + " from " + SystemTables.key_column_usage_TableName + ", " + SystemTables.table_constraints_TableName + " where " + SystemTables.key_column_usage_TableName + ".constraint_catalog = " + SystemTables.table_constraints_TableName + ".constraint_catalog And " + SystemTables.key_column_usage_TableName + ".constraint_schema = " + SystemTables.table_constraints_TableName + ".constraint_schema And " + SystemTables.key_column_usage_TableName + ".constraint_name = " + SystemTables.table_constraints_TableName + ".constraint_name And " + SystemTables.table_constraints_TableName + ".table_catalog = ? and " + SystemTables.table_constraints_TableName + ".table_schema = ? and " + SystemTables.table_constraints_TableName + ".table_name = ? and " + SystemTables.table_constraints_TableName + ".constraint_type = '" + SqlKeywords.PRIMARY + " " + SqlKeywords.KEY + "'";
            primaryKeysExecuter = (_Executer) serverSession.executeQueryParameterised(primaryKeysQuery, 0);
        }
        return primaryKeysExecuter;
    }

    public _Executer getrelatedTableAndColumnExecuter() throws DException {
        if (relatedTableAndColumnExecuter == null) {
            String relatedTableAndColumnQuery = " Select " + SystemTables.key_column_usage_TableName + ".table_catalog," + SystemTables.key_column_usage_TableName + ".table_schema ," + SystemTables.key_column_usage_TableName + ".table_name ," + SystemTables.key_column_usage_TableName + ".column_name" + " from " + SystemTables.key_column_usage_TableName + "," + SystemTables.referential_constraints_TableName + " where " + SystemTables.key_column_usage_TableName + ".constraint_catalog = " + SystemTables.referential_constraints_TableName + ".unique_constraint_catalog and " + SystemTables.key_column_usage_TableName + ".constraint_schema = " + SystemTables.referential_constraints_TableName + ".unique_constraint_schema and " + SystemTables.key_column_usage_TableName + ".constraint_name =" + SystemTables.referential_constraints_TableName + ".unique_constraint_name and  " + SystemTables.referential_constraints_TableName + ".constraint_catalog = ? and " + SystemTables.referential_constraints_TableName + ".constraint_schema = ? and " + SystemTables.referential_constraints_TableName + ".constraint_name = ? ";
            relatedTableAndColumnExecuter = (_Executer) serverSession.executeQueryParameterised(relatedTableAndColumnQuery, 0);
        }
        return relatedTableAndColumnExecuter;
    }

    public _Executer getcolumnExecuter() throws DException {
        if (columnExecuter == null) {
            String columnQuery = "Select * from " + SystemTables.columns_TableName + " where table_catalog = ? and table_schema = ? and table_name = ? order by ordinal_position";
            columnExecuter = (_Executer) serverSession.executeQueryParameterised(columnQuery, 0);
        }
        return columnExecuter;
    }

    public _Executer getDataTypeExecuter() throws DException {
        if (dataTypeExecuter == null) {
            String dataTypeQuery = "Select * from " + SystemTables.dataTypeDescriptor_TableName + " where object_catalog = ? and object_schema = ? and object_name = ? and object_type = 'TABLE'";
            dataTypeExecuter = (_Executer) serverSession.executeQueryParameterised(dataTypeQuery, 0);
        }
        return dataTypeExecuter;
    }

    public _Executer getDataTypeDescriptorTableExecuter() throws DException {
        if (dataTypeDescriptorTableExecuter == null) {
            StringBuffer clause = new StringBuffer();
            clause.append("select * from ");
            clause.append(SystemTables.dataTypeDescriptor_TableName);
            clause.append(" where ");
            clause.append("object_catalog = ? and object_schema = ? and object_name = ? and object_type = ? and dtd_identifier = ? ");
            dataTypeDescriptorTableExecuter = (_Executer) serverSession.executeQueryParameterised(clause.toString(), 0);
        }
        return dataTypeDescriptorTableExecuter;
    }

    public _Executer getTableTypesAndCodesExecuter() throws DException {
        if (tableTypesAndCodesExecuter == null) {
            String tableTypesQuery = "select table_type,country_code,language_code from " + SystemTables.tables_TableName + " where table_catalog = ? and table_schema = ? and table_name = ?";
            tableTypesAndCodesExecuter = (_Executer) serverSession.executeQueryParameterised(tableTypesQuery, 0);
        }
        return tableTypesAndCodesExecuter;
    }

    public _Executer getCheckIsIndexTableExecuter() throws DException {
        if (checkIsIndexTableExecuter == null) {
            String checkIsIndexTableQuery = "select table_catalog,table_schema,table_name,indexname from " + SystemTables.INDEXINFO + " where indextablename = ? ";
            checkIsIndexTableExecuter = (_Executer) serverSession.executeQueryParameterised(checkIsIndexTableQuery, 0);
        }
        return checkIsIndexTableExecuter;
    }

    public _Executer getDomainConstraintsExecuter() throws DException {
        if (domainConstraintsExecuter == null) {
            String domainConstraintsQuery = "select * from " + SystemTables.domain_constraints_TableName + " where domain_catalog = ? and domain_schema = ? and domain_name = ? ";
            domainConstraintsExecuter = (_Executer) serverSession.executeQueryParameterised(domainConstraintsQuery, 0);
        }
        return domainConstraintsExecuter;
    }

    public _Executer getdeferredCheckingExecuter() throws DException {
        if (deferredCheckingExecuter == null) {
            String deferredChecking = "select * from " + SystemTables.table_constraints_TableName + " where table_catalog = ? and table_schema = ? and table_name = ? and is_deferrable ='yes'";
            deferredCheckingExecuter = (_Executer) serverSession.executeQueryParameterised(deferredChecking, 0);
        }
        return deferredCheckingExecuter;
    }

    public _Executer getDefalutClauseExecuter() throws DException {
        if (defalutClauseExecuter == null) {
            String defalutClauseQuery = "select column_default,is_autoIncrement from " + SystemTables.columns_TableName + " where table_catalog = ? and table_schema = ? and table_name = ? and column_name != '" + SystemFields.systemFields[SystemFields.rowId] + "' order by ordinal_position ";
            defalutClauseExecuter = (_Executer) serverSession.executeQueryParameterised(defalutClauseQuery, 0);
        }
        return defalutClauseExecuter;
    }

    public _Executer getTableDescriptorExecuterForPrimaryAndUniqueConstraint() throws DException {
        if (tableDescriptorExecuterForPrimaryAndUniqueConstraint == null) {
            String tableDescriptorQueryForPrimaryAndUniqueConstraint = "select * from " + SystemTables.table_constraints_TableName + " where table_catalog = ? and table_schema = ? and table_name = ? and constraint_type in ('" + SqlKeywords.PRIMARY + " " + SqlKeywords.KEY + "','" + SqlKeywords.UNIQUE + "') and is_deferrable = 'YES' and initially_deferred = 'YES'";
            tableDescriptorExecuterForPrimaryAndUniqueConstraint = (_Executer) serverSession.executeQueryParameterised(tableDescriptorQueryForPrimaryAndUniqueConstraint, 0);
        }
        return tableDescriptorExecuterForPrimaryAndUniqueConstraint;
    }

    public _Executer getTableDescriptorExecuterForPrimaryAndUniqueConstraintNonDef() throws DException {
        if (tableDescriptorExecuterForPrimaryAndUniqueConstraintNonDef == null) {
            String tableDescriptorQueryForPrimaryAndUniqueConstraint = "select * from " + SystemTables.table_constraints_TableName + " where table_catalog = ? and table_schema = ? and table_name = ? and constraint_type in ('" + SqlKeywords.PRIMARY + " " + SqlKeywords.KEY + "','" + SqlKeywords.UNIQUE + "') and (is_deferrable = 'NO' or (is_deferrable = 'YES' and initially_deferred = 'NO'))";
            tableDescriptorExecuterForPrimaryAndUniqueConstraintNonDef = (_Executer) serverSession.executeQueryParameterised(tableDescriptorQueryForPrimaryAndUniqueConstraint, 0);
        }
        return tableDescriptorExecuterForPrimaryAndUniqueConstraintNonDef;
    }

    public _Executer getTablePrivilegesForTableExecuter() throws DException {
        if (tablePrivilegesForTableExecuter == null) {
            String tablePrivilegesForTableQuery = "select privilege_type,is_grantable from " + SystemTables.table_privileges_TableName + " where table_catalog = ? and table_schema = ? and table_name = ? and grantee in ( ? )";
            tablePrivilegesForTableExecuter = (_Executer) serverSession.executeQueryParameterised(tablePrivilegesForTableQuery, 0);
        }
        return tablePrivilegesForTableExecuter;
    }

    public _Executer getColumnPrivilegesForColumnExecuter() throws DException {
        if (columnPrivilegesForColumnExecuter == null) {
            String columnPrivilegesForColumnQuery = "select privilege_type,is_grantable from " + SystemTables.column_privileges_TableName + " where table_catalog = ? and table_schema = ? and table_name = ? and column_name = ? and grantee in ( ? )";
            columnPrivilegesForColumnExecuter = (_Executer) serverSession.executeQueryParameterised(columnPrivilegesForColumnQuery, 0);
        }
        return columnPrivilegesForColumnExecuter;
    }

    public _Executer getReferencedConstraintsJoinExecuter() throws DException {
        if (referencedConstraintsJoinExecuter == null) {
            String referencedConstraintsJoinQuery = " Select * from " + SystemTables.referential_constraints_TableName + " ,  " + SystemTables.table_constraints_TableName + " as FCTable " + " ,  " + SystemTables.table_constraints_TableName + " as PCTable" + " where (" + SystemTables.referential_constraints_TableName + ".constraint_catalog = FCtable.constraint_catalog and " + SystemTables.referential_constraints_TableName + ".constraint_schema = FCTable.constraint_schema and  " + SystemTables.referential_constraints_TableName + ".constraint_name = FCTable.constraint_name and " + "FCTable.constraint_type = '" + SqlKeywords.FOREIGN + " " + SqlKeywords.KEY + "' )" + " and " + SystemTables.referential_constraints_TableName + ".unique_constraint_catalog = PCTable.constraint_catalog  and " + SystemTables.referential_constraints_TableName + ".unique_constraint_schema = PCTable.constraint_schema  and  " + SystemTables.referential_constraints_TableName + ".unique_constraint_name = " + "PCTable.constraint_name and PCTable.table_catalog = ? and PCTable.table_schema = ? and " + "PCTable.table_name = ?  and (PCTable.constraint_type = '" + SqlKeywords.PRIMARY + " " + SqlKeywords.KEY + "' or PCTable.constraint_type = '" + SqlKeywords.UNIQUE + "' )  and FCTable.is_deferrable = 'YES' and FCTable.initially_deferred = 'YES'";
            referencedConstraintsJoinExecuter = (_Executer) serverSession.executeQueryParameterised(referencedConstraintsJoinQuery, 0);
        }
        return referencedConstraintsJoinExecuter;
    }

    public _Executer getReferencedConstraintsJoinExecuterNonDef() throws DException {
        if (referencedConstraintsJoinExecuterNonDef == null) {
            String referencedConstraintsJoinQuery = " Select * from " + SystemTables.referential_constraints_TableName + " ,  " + SystemTables.table_constraints_TableName + " as FCTable " + " ,  " + SystemTables.table_constraints_TableName + " as PCTable" + " where (" + SystemTables.referential_constraints_TableName + ".constraint_catalog = FCtable.constraint_catalog and " + SystemTables.referential_constraints_TableName + ".constraint_schema = FCTable.constraint_schema and  " + SystemTables.referential_constraints_TableName + ".constraint_name = FCTable.constraint_name and " + "FCTable.constraint_type = '" + SqlKeywords.FOREIGN + " " + SqlKeywords.KEY + "' )" + " and " + SystemTables.referential_constraints_TableName + ".unique_constraint_catalog = PCTable.constraint_catalog  and " + SystemTables.referential_constraints_TableName + ".unique_constraint_schema = PCTable.constraint_schema  and  " + SystemTables.referential_constraints_TableName + ".unique_constraint_name = " + "PCTable.constraint_name and PCTable.table_catalog = ? and PCTable.table_schema = ? and " + "PCTable.table_name = ?  and (PCTable.constraint_type = '" + SqlKeywords.PRIMARY + " " + SqlKeywords.KEY + "' or PCTable.constraint_type = '" + SqlKeywords.UNIQUE + "' )  and (FCTable.is_deferrable = 'NO' or (FCTable.is_deferrable = 'YES' and FCTable.initially_deferred = 'NO')) ";
            referencedConstraintsJoinExecuterNonDef = (_Executer) serverSession.executeQueryParameterised(referencedConstraintsJoinQuery, 0);
        }
        return referencedConstraintsJoinExecuterNonDef;
    }

    public _Executer getReferencingConstraintsJoinExecuter() throws DException {
        if (referencingConstraintsJoinExecuter == null) {
            String referencingConstraintsJoinQuery = " Select * from " + SystemTables.referential_constraints_TableName + " as RC ,  " + SystemTables.table_constraints_TableName + " as TC where (" + "RC.constraint_catalog = TC.constraint_catalog and " + "RC.constraint_schema = TC.constraint_schema and " + "RC.constraint_name = TC.constraint_name) " + " and " + "TC.table_catalog = ? and TC.table_schema = ? and TC.table_name = ? and " + "TC.constraint_type = '" + SqlKeywords.FOREIGN + " " + SqlKeywords.KEY + "' and " + "TC.is_deferrable = 'YES' and TC.initially_deferred = 'YES'";
            referencingConstraintsJoinExecuter = (_Executer) serverSession.executeQueryParameterised(referencingConstraintsJoinQuery, 0);
        }
        return referencingConstraintsJoinExecuter;
    }

    public _Executer getReferencingConstraintsJoinExecuterNonDef() throws DException {
        if (referencingConstraintsJoinExecuterNonDef == null) {
            String referencingConstraintsJoinQuery = " Select * from " + SystemTables.referential_constraints_TableName + " as RC ,  " + SystemTables.table_constraints_TableName + " as TC where (" + "RC.constraint_catalog = TC.constraint_catalog and " + "RC.constraint_schema = TC.constraint_schema and " + "RC.constraint_name = TC.constraint_name ) " + " and " + "TC.table_catalog = ? and TC.table_schema = ? and TC.table_name = ? and " + "TC.constraint_type = '" + SqlKeywords.FOREIGN + " " + SqlKeywords.KEY + "' and " + "(TC.is_deferrable = 'NO' or (TC.is_deferrable = 'YES' and TC.initially_deferred = 'NO'))";
            referencingConstraintsJoinExecuterNonDef = (_Executer) serverSession.executeQueryParameterised(referencingConstraintsJoinQuery, 0);
        }
        return referencingConstraintsJoinExecuterNonDef;
    }

    public _Executer getRoleAuthorizationsExecuterforGranteeOnly() throws DException {
        if (RoleAuthorizationsExecterforGranteeOnly == null) RoleAuthorizationsExecterforGranteeOnly = (_Executer) serverSession.executeQueryParameterised(QueryMaker.getRoleAuthorizationsforGranteeOnly(), 0);
        return RoleAuthorizationsExecterforGranteeOnly;
    }

    public _Executer getTrigersExecuterWithEventCondition() throws DException {
        if (trigersExecuterWithEventCondition == null) {
            String trigersQueryWithEventCondition = "select * from " + SystemTables.triggers_TableName + " where event_object_catalog = ? and event_object_schema = ? and event_object_table = ? and event_manipulation = ? and action_orientation = ? and condition_timing = ? ";
            trigersExecuterWithEventCondition = (_Executer) serverSession.executeQueryParameterised(trigersQueryWithEventCondition, 0);
        }
        return trigersExecuterWithEventCondition;
    }

    public _Executer getTrigersExecuterWithTriggerNameCondition() throws DException {
        if (trigersExecuterWithTriggerNameCondition == null) {
            String trigersQueryWithTriggerNameCondition = "select * from " + SystemTables.triggers_TableName + " where trigger_catalog = ? and trigger_schema = ? and trigger_name = ? and event_manipulation = ? and action_orientation = ? and condition_timing = ? ";
            trigersExecuterWithTriggerNameCondition = (_Executer) serverSession.executeQueryParameterised(trigersQueryWithTriggerNameCondition, 0);
        }
        return trigersExecuterWithTriggerNameCondition;
    }

    public _Executer getTriggeredUpdatedExecuterForAllColumns() throws DException {
        if (triggeredUpdatedExecuterForAllColumns == null) {
            String triggeredUpdatedQueryForAllColumns = "select * from " + SystemTables.triggered_update_columns_TableName + " where event_object_catalog = ? and event_object_schema = ? and event_object_table = ? ";
            triggeredUpdatedExecuterForAllColumns = (_Executer) serverSession.executeQueryParameterised(triggeredUpdatedQueryForAllColumns, 0);
        }
        return triggeredUpdatedExecuterForAllColumns;
    }

    public _Executer getTriggerExecuterForUpdateType() throws DException {
        if (triggerExecuterForUpdateType == null) {
            String query2 = "select trigger_catalog,trigger_schema,trigger_name from " + SystemTables.triggered_update_columns_TableName + " where event_object_catalog = ? and event_object_schema = ? and event_object_table = ?";
            String triggerQueryForUpdateType = "select * from " + SystemTables.triggers_TableName + " where (event_object_catalog = ? and event_object_schema = ? and event_object_table = ? and event_manipulation = '" + SqlKeywords.UPDATE + "' and action_orientation = ? and condition_timing = ? ) " + " and (trigger_catalog,trigger_schema,trigger_name) NOT IN (" + query2 + ")";
            triggerExecuterForUpdateType = (_Executer) serverSession.executeQueryParameterised(triggerQueryForUpdateType, 0);
        }
        return triggerExecuterForUpdateType;
    }

    public _Executer getViewsExecuterWithNameCondition() throws DException {
        if (viewsExecuterWithNameCondition == null) {
            String viewsQueryWithNameCondition = "select view_definition, check_option, is_updatable, is_insertable_into from " + SystemTables.views_TableName + " where table_catalog = ? and table_schema = ? and table_name = ? ";
            viewsExecuterWithNameCondition = (_Executer) serverSession.executeQueryParameterised(viewsQueryWithNameCondition, 0);
        }
        return viewsExecuterWithNameCondition;
    }

    public _Executer getCheckConstraintExecuter() throws DException {
        if (checkConstraintExecuter == null) {
            String checkConstraintQuery = "select check_clause from " + SystemTables.check_constraints_TableName + " where constraint_catalog = ? and constraint_schema = ? " + " and constraint_name = ? ";
            checkConstraintExecuter = (_Executer) serverSession.executeQueryParameterised(checkConstraintQuery, 0);
        }
        return checkConstraintExecuter;
    }

    public _Executer getTableConstraintExecuter() throws DException {
        if (tableConstraintExecuter == null) {
            String tableConstraintQuery = "select * from " + SystemTables.table_constraints_TableName + " where constraint_catalog = ? and constraint_schema = ? " + " and constraint_name = ? ";
            tableConstraintExecuter = (_Executer) serverSession.executeQueryParameterised(tableConstraintQuery, 0);
        }
        return tableConstraintExecuter;
    }

    public _Executer getKeyColumnUsageExecuter() throws DException {
        if (keyColumnUsageExecuter == null) {
            String keyColumnUsageQuery = "select * from " + SystemTables.key_column_usage_TableName + " where constraint_catalog = ? and constraint_schema = ? " + " and constraint_name = ? order by ordinal_position ";
            keyColumnUsageExecuter = (_Executer) serverSession.executeQueryParameterised(keyColumnUsageQuery, 0);
        }
        return keyColumnUsageExecuter;
    }

    public _Executer getTriggerUpdateColumnsExecuter() throws DException {
        if (triggerUpdateColumnsExecuter == null) {
            String triggerUpdateColumnsQuery = "select event_object_column from " + SystemTables.triggered_update_columns_TableName + " where trigger_catalog = ? and trigger_schema = ? and trigger_name = ?";
            triggerUpdateColumnsExecuter = (_Executer) serverSession.executeQueryParameterised(triggerUpdateColumnsQuery, 0);
        }
        return triggerUpdateColumnsExecuter;
    }

    public _Executer getCheckTableUsedExecuter() throws DException {
        if (checkTableUsedExecuter == null) {
            String checkTableUsedQuery = " Select table_catalog,table_schema,table_name from " + SystemTables.check_table_usage_TableName + " where constraint_catalog = ? and constraint_schema = ? and constraint_name = ? ";
            checkTableUsedExecuter = (_Executer) serverSession.executeQueryParameterised(checkTableUsedQuery, 0);
        }
        return checkTableUsedExecuter;
    }

    public _Executer getCheckColumnUsedExecuter() throws DException {
        if (checkColumnUsedExecuter == null) {
            String checkColumnUsedQuery = " Select table_catalog,table_schema,table_name,column_name from " + SystemTables.check_column_usage_TableName + " where constraint_catalog = ? and constraint_schema = ? and constraint_name = ? ";
            checkColumnUsedExecuter = (_Executer) serverSession.executeQueryParameterised(checkColumnUsedQuery, 0);
        }
        return checkColumnUsedExecuter;
    }

    public _Executer getRoutinePrivilegesExecuter() throws DException {
        if (routinePrivilegesExecuter == null) {
            String routinePrivilegesQuery = "select is_grantable from " + SystemTables.routine_privileges_TableName + " where specific_catalog = ? " + " and specific_schema = ? " + " and specific_name = ? and privilege_type ='" + SqlKeywords.EXECUTE + "'" + " and grantee in ( ? )";
            routinePrivilegesExecuter = (_Executer) serverSession.executeQueryParameterised(routinePrivilegesQuery, 0);
        }
        return routinePrivilegesExecuter;
    }

    public _Executer getHasAnyTriggerOnViewExecuter() throws DException {
        if (hasAnyTriggerOnViewExecuter == null) {
            String hasAnyTriggerOnViewQuery = "select * from " + SystemTables.triggers_TableName + " where event_object_catalog = ? and event_object_schema = ? and event_object_table = ? ";
            hasAnyTriggerOnViewExecuter = (_Executer) serverSession.executeQueryParameterised(hasAnyTriggerOnViewQuery, 0);
        }
        return hasAnyTriggerOnViewExecuter;
    }

    public _Executer getIsDateSpanTableExecuter() throws DException {
        if (isDateSpanTableExecuter == null) {
            String dateSpanQuery = " select * from " + SystemTables.columns_TableName + " where table_catalog = ? and table_schema = ? and table_name = ? and ( column_name = '" + SqlSchemaConstants.dateSpanFrom + "' or column_name = '" + SqlSchemaConstants.dateSpanTo + "')";
            isDateSpanTableExecuter = (_Executer) serverSession.executeQueryParameterised(dateSpanQuery, 0);
        }
        return isDateSpanTableExecuter;
    }

    public _Executer getViewsForTableExecuter() throws DException {
        if (viewsForTableExecuter == null) {
            String viewsForTableQuery = "select view_catalog,view_schema,view_name from " + SystemTables.view_table_usage_TableName + " where table_catalog = ? and table_schema = ? and table_name = ?";
            viewsForTableExecuter = (_Executer) serverSession.executeQueryParameterised(viewsForTableQuery, 0);
        }
        return viewsForTableExecuter;
    }

    public _Executer getMaterializedViewsFromViewsExecuter() throws DException {
        if (materializedViewsFromViewsExecuter == null) {
            String materializedViewsFromViews = "select table_catalog,table_schema,materialized_table_name from " + SystemTables.views_TableName + " where (table_catalog,table_schema,table_name) IN (?) and materialized_table_name is not null ";
            materializedViewsFromViewsExecuter = (_Executer) serverSession.executeQueryParameterised(materializedViewsFromViews, 0);
        }
        return materializedViewsFromViewsExecuter;
    }

    public _Executer getMataerailzedQueryDefinitionExecuter() throws DException {
        if (mataerailzedQueryDefinitionExecuter == null) {
            String mataerailzedQueryDefinitionQuery = "select view_definition from " + SystemTables.views_TableName + " where table_catalog = ? and table_schema =? and materialized_table_name = ?";
            mataerailzedQueryDefinitionExecuter = (_Executer) serverSession.executeQueryParameterised(mataerailzedQueryDefinitionQuery, 0);
        }
        return mataerailzedQueryDefinitionExecuter;
    }

    public _Executer getColumnsTableExecuter() throws DException {
        if (columnsTableExecuter == null) {
            StringBuffer clause = new StringBuffer();
            clause.append(" select * from ").append(SystemTables.columns_TableName).append(" where ");
            clause.append("table_catalog = ? and table_schema = ? and table_name =? and column_name = ? ");
            columnsTableExecuter = (_Executer) serverSession.executeQueryParameterised(clause.toString(), 0);
        }
        return columnsTableExecuter;
    }

    public _Executer getTablesTableExecuter() throws DException {
        if (tablesTableExecuter == null) {
            String queryforLoadTable = "select * from " + SystemTables.tables_TableName + " where table_catalog = ? and table_schema = ? and table_name = ?";
            tablesTableExecuter = (_Executer) serverSession.executeQueryParameterised(queryforLoadTable, 0);
        }
        return tablesTableExecuter;
    }

    public _Executer getReferentialConstraintsTableExecuter() throws DException {
        if (referentialConstraintsTableExecuter == null) {
            String referentialConstraintQuery = "select * from " + SystemTables.referential_constraints_TableName + " where constraint_catalog = ? and constraint_schema = ? " + " and constraint_name = ? ";
            referentialConstraintsTableExecuter = (_Executer) serverSession.executeQueryParameterised(referentialConstraintQuery, 0);
        }
        return referentialConstraintsTableExecuter;
    }

    public _Executer getColumnPrivilegesTableExecuter() throws DException {
        if (columnPrivilegesTableExecuter == null) {
            StringBuffer clause = new StringBuffer();
            clause.append("select * from ");
            clause.append(SystemTables.column_privileges_TableName);
            clause.append(" where grantor =? and grantee = ?  and table_catalog = ? and table_schema = ? and table_name = ? ");
            clause.append(" and column_name = ? ");
            clause.append(" and privilege_type = ?");
            columnPrivilegesTableExecuter = (_Executer) serverSession.executeQueryParameterised(clause.toString(), 0);
        }
        return columnPrivilegesTableExecuter;
    }

    public _Executer getTablePrivlegesTableExecuter() throws DException {
        if (tablePrivlegesTableExecuter == null) {
            StringBuffer clause = new StringBuffer();
            clause.append(" select * from ");
            clause.append(SystemTables.table_privileges_TableName);
            clause.append(" where grantor = ? and grantee = ? ");
            clause.append(" and table_catalog = ? and table_schema = ?");
            clause.append(" and table_name = ? and privilege_type = ? ");
            tablePrivlegesTableExecuter = (_Executer) serverSession.executeQueryParameterised(clause.toString(), 0);
        }
        return tablePrivlegesTableExecuter;
    }

    public _Executer getSchemataTableExecuter() throws DException {
        if (schemataTableExecuter == null) {
            String schemaQuery = "select * from " + SystemTables.schema_TableName + " where catalog_name = ?  and schema_name = ? ";
            schemataTableExecuter = (_Executer) serverSession.executeQueryParameterised(schemaQuery, 0);
        }
        return schemataTableExecuter;
    }

    public _Executer getDomianTableExecuter() throws DException {
        if (domainTableExecuter == null) {
            StringBuffer st = new StringBuffer();
            st.append("select * from ");
            st.append(SystemTables.domains_TableName);
            st.append(" where ");
            st.append("domain_catalog = ?  and domain_schema = ? and domain_name = ? ");
            domainTableExecuter = (_Executer) serverSession.executeQueryParameterised(st.toString(), 0);
        }
        return domainTableExecuter;
    }

    public _Executer getDomainConstraintsTableExecuter() throws DException {
        if (domianConstrainttaTableExecuter == null) {
            StringBuffer clause = new StringBuffer();
            clause.append("select * from ");
            clause.append(SystemTables.domain_constraints_TableName);
            clause.append(" where ");
            clause.append("constraint_catalog = ? and constraint_schema = ? and constraint_name = ?");
            domianConstrainttaTableExecuter = (_Executer) serverSession.executeQueryParameterised(clause.toString(), 0);
        }
        return domianConstrainttaTableExecuter;
    }

    public booleanvalueexpression getTablesTableCondition() throws DException {
        if (tablesTableCondition == null) {
            String bve = "TABLE_CATALOG=? and TABLE_SCHEMA=? and TABLE_NAME=?";
            tablesTableCondition = ConditionParser.parseCondition(bve, serverSession, SystemTables.tables_TableName);
        }
        return tablesTableCondition;
    }

    public booleanvalueexpression getColumnsTableCondition() throws DException {
        if (columnsTableCondition == null) {
            String bve = "TABLE_CATALOG=? and TABLE_SCHEMA=? and TABLE_NAME=? and COLUMN_NAME=?";
            columnsTableCondition = ConditionParser.parseCondition(bve, serverSession, SystemTables.columns_TableName);
        }
        return columnsTableCondition;
    }

    public booleanvalueexpression getDataTypeDescriptorTableCondition() throws DException {
        if (dataTypeDescriptorTableCondition == null) {
            String bve = "OBJECT_CATALOG=? and OBJECT_SCHEMA=? and OBJECT_NAME=? and OBJECT_TYPE=? and DTD_IDENTIFIER=?";
            dataTypeDescriptorTableCondition = ConditionParser.parseCondition(bve, serverSession, SystemTables.dataTypeDescriptor_TableName);
        }
        return dataTypeDescriptorTableCondition;
    }

    public booleanvalueexpression getTableConstraintsTableCondition() throws DException {
        if (tableConstraintsTableCondition == null) {
            String bve = "CONSTRAINT_CATALOG=? and CONSTRAINT_SCHEMA=? and CONSTRAINT_NAME=?";
            tableConstraintsTableCondition = ConditionParser.parseCondition(bve, serverSession, SystemTables.table_constraints_TableName);
        }
        return tableConstraintsTableCondition;
    }

    public booleanvalueexpression getCheckConstraintsTableCondition() throws DException {
        if (checkConstraintsTableCondition == null) {
            String bve = "CONSTRAINT_CATALOG=? and CONSTRAINT_SCHEMA=? and CONSTRAINT_NAME=?";
            checkConstraintsTableCondition = ConditionParser.parseCondition(bve, serverSession, SystemTables.check_constraints_TableName);
        }
        return checkConstraintsTableCondition;
    }

    public booleanvalueexpression getCheckTableUsageTableCondition() throws DException {
        if (checkTableUsageTableCondition == null) {
            String bve = "CONSTRAINT_CATALOG=? and CONSTRAINT_SCHEMA=? and CONSTRAINT_NAME=?";
            checkTableUsageTableCondition = ConditionParser.parseCondition(bve, serverSession, SystemTables.check_table_usage_TableName);
        }
        return checkTableUsageTableCondition;
    }

    public booleanvalueexpression getCheckColumnUsageTableCondition() throws DException {
        if (checkColumnUsageTableCondition == null) {
            String bve = "CONSTRAINT_CATALOG=? and CONSTRAINT_SCHEMA=? and CONSTRAINT_NAME=?";
            checkColumnUsageTableCondition = ConditionParser.parseCondition(bve, serverSession, SystemTables.check_column_usage_TableName);
        }
        return checkColumnUsageTableCondition;
    }

    public booleanvalueexpression getReferentialConstraintsTableCondition() throws DException {
        if (referentialConstraintsTableCondition == null) {
            String bve = "CONSTRAINT_CATALOG=? and CONSTRAINT_SCHEMA=? and CONSTRAINT_NAME=?";
            referentialConstraintsTableCondition = ConditionParser.parseCondition(bve, serverSession, SystemTables.referential_constraints_TableName);
        }
        return referentialConstraintsTableCondition;
    }

    public booleanvalueexpression getKeyColumnUsageTableCondition() throws DException {
        if (keyColumnUsageTableCondition == null) {
            String bve = "CONSTRAINT_CATALOG=? and CONSTRAINT_SCHEMA=? and CONSTRAINT_NAME=? and COLUMN_NAME=?";
            keyColumnUsageTableCondition = ConditionParser.parseCondition(bve, serverSession, SystemTables.key_column_usage_TableName);
        }
        return keyColumnUsageTableCondition;
    }

    public booleanvalueexpression getColumnPrivilegesTableCondition() throws DException {
        if (columnPrivilegesTableCondition == null) {
            String bve = "GRANTOR=? and GRANTEE=? and TABLE_CATALOG=? and TABLE_SCHEMA=? and TABLE_NAME=? and PRIVILEGE_TYPE=? and COLUMN_NAME=?";
            columnPrivilegesTableCondition = ConditionParser.parseCondition(bve, serverSession, SystemTables.column_privileges_TableName);
        }
        return columnPrivilegesTableCondition;
    }

    public booleanvalueexpression getTablePrivlegesTableCondition() throws DException {
        if (tablePrivlegesTableCondition == null) {
            String bve = "GRANTOR=? and GRANTEE=? and TABLE_CATALOG=? and TABLE_SCHEMA=? and TABLE_NAME=? and PRIVILEGE_TYPE=?";
            tablePrivlegesTableCondition = ConditionParser.parseCondition(bve, serverSession, SystemTables.table_privileges_TableName);
        }
        return tablePrivlegesTableCondition;
    }

    public _Executer getExecuterTablePrivilegeDescriptorDependent_A() throws DException {
        return (tableDependent_A == null) ? (tableDependent_A = (_Executer) serverSession.executeQueryParameterised(QueryGetter.tablePrivilegeDescriptorDependent_A, 0)) : tableDependent_A;
    }

    public _Executer getExecuterTablePrivilegeDescriptorDependent_B() throws DException {
        return (tableDependent_B == null) ? (tableDependent_B = (_Executer) serverSession.executeQueryParameterised(QueryGetter.tablePrivilegeDescriptorDependent_B, 0)) : tableDependent_B;
    }

    public _Executer getExecuterColumnPrivilegeDescriptorDependent_A() throws DException {
        return (columnDependent_A == null) ? (columnDependent_A = (_Executer) serverSession.executeQueryParameterised(QueryGetter.columnPrivilegeDescriptorDependent_A, 0)) : columnDependent_A;
    }

    public _Executer getExecuterColumnPrivilegeDescriptorDependent_B() throws DException {
        return (columnDependent_B == null) ? (columnDependent_B = (_Executer) serverSession.executeQueryParameterised(QueryGetter.columnPrivilegeDescriptorDependent_B, 0)) : columnDependent_B;
    }

    public _Executer getExecuterRoutinePrivilegeDescriptorDependent_A() throws DException {
        return (routineDependent_A == null) ? (routineDependent_A = (_Executer) serverSession.executeQueryParameterised(QueryGetter.routinePrivilegeDescriptorDependent_A, 0)) : routineDependent_A;
    }

    public _Executer getExecuterUsagePrivilegeDescriptorDependent_A() throws DException {
        return (usageDependent_A == null) ? (usageDependent_A = (_Executer) serverSession.executeQueryParameterised(QueryGetter.usagePrivilegeDescriptorDependent_A, 0)) : usageDependent_A;
    }

    public _Executer getExecuterPrivilegeDescriptorDependents_C() throws DException {
        return (dependent_C == null) ? (dependent_C = (_Executer) serverSession.executeQueryParameterised(QueryGetter.privilegeDescriptorDependents_C, 0)) : dependent_C;
    }

    public _Executer getExecuterDomainCheckConstraints() throws DException {
        return (domainCheckConstaint == null) ? (domainCheckConstaint = (_Executer) serverSession.executeQueryParameterised(QueryGetter.domainCheckConstraints, 0)) : domainCheckConstaint;
    }

    public _Executer getExecuterUsagePrivilegeDescriptorAncestor_C_Column() throws DException {
        return (usageDependent_C_Column == null) ? (usageDependent_C_Column = (_Executer) serverSession.executeQueryParameterised(QueryGetter.usagePrivilegeDescriptorAncestor_C_Column, 0)) : usageDependent_C_Column;
    }

    public _Executer getExecuterUsagePrivilegeDescriptorAncestor_C_Usage() throws DException {
        return (usageDependent_C_Usage == null) ? (usageDependent_C_Usage = (_Executer) serverSession.executeQueryParameterised(QueryGetter.usagePrivilegeDescriptorAncestor_C_Usage, 0)) : usageDependent_C_Usage;
    }

    public _Executer getTableConstraintExecuterForPrimaryKey() throws DException {
        if (tableConstraintExecuterForPrimaryKey == null) {
            String tableConstraintQueryForPrimaryKey = "select * from " + SystemTables.table_constraints_TableName + " where table_catalog = ? and table_schema = ? " + " and table_name = ? and constraint_type = '" + SqlKeywords.PRIMARY + " " + SqlKeywords.KEY + "'";
            tableConstraintExecuterForPrimaryKey = (_Executer) serverSession.executeQueryParameterised(tableConstraintQueryForPrimaryKey, 0);
        }
        return tableConstraintExecuterForPrimaryKey;
    }

    public _Executer getAllRoleAuthorizationsExecuterforGranteeOnly() throws DException {
        if (allRoleAuthorizationsExecterforGranteeOnly == null) allRoleAuthorizationsExecterforGranteeOnly = (_Executer) serverSession.executeQueryParameterised(QueryMaker.getAllRoleAuthorizationsforGranteeOnly(), 0);
        return allRoleAuthorizationsExecterforGranteeOnly;
    }

    public _Executer getAllRoleAuthorizationsExecuterforGranteeAndPublic() throws DException {
        if (allRoleAuthorizationsExecuterforGranteeAndPublic == null) {
            allRoleAuthorizationsExecuterforGranteeAndPublic = (_Executer) serverSession.executeQueryParameterised(QueryMaker.getAllRoleAuthorizationsforGranteeAndPublic(), 0);
        }
        return allRoleAuthorizationsExecuterforGranteeAndPublic;
    }

    public _Executer getIsValidRoleExecuter() throws DException {
        if (isValidRoleExecuter == null) {
            isValidRoleExecuter = (_Executer) serverSession.executeQueryParameterised(QueryMaker.getIsValidRole(), 0);
        }
        return isValidRoleExecuter;
    }

    public _Executer getExecuterForUserValidity() throws DException {
        if (userValidity == null) {
            String userQuery = "Select * From " + SystemTables.users_TableName + " Where USER_NAME = ?  ";
            userValidity = (_Executer) serverSession.executeQueryParameterised(userQuery, 0);
        }
        return userValidity;
    }

    public _Executer getExecuterForRoleValidity() throws DException {
        if (roleValidity == null) {
            String roleQuery = "Select * From " + SystemTables.roles_TableName + " Where ROLE_NAME = ?  ";
            roleValidity = (_Executer) serverSession.executeQueryParameterised(roleQuery, 0);
        }
        return roleValidity;
    }

    public _Executer getExecuterForDomainValidity() throws DException {
        if (domainValidity == null) {
            String usage_clause = "select * from " + SystemTables.usage_privileges_TableName + " where object_catalog=? and  object_schema=? and object_name=? and object_type = ? and grantee IN (?) ";
            domainValidity = (_Executer) serverSession.executeQueryParameterised(usage_clause, 0);
        }
        return domainValidity;
    }

    public _Executer getPrimaryConstraintExecuter() throws DException {
        if (primaryConstraintExecuter == null) {
            String primaryKeyConstraint = "select * from " + SystemTables.table_constraints_TableName + " as one, " + SystemTables.key_column_usage_TableName + " as two " + " where one.constraint_catalog = two.constraint_catalog " + " and one.constraint_schema = two.constraint_schema " + " and one.constraint_name = two.constraint_name " + " and two.table_catalog = ? " + " and two.table_schema = ? " + " and two.table_name = ? " + " and two.column_name = ?" + " and one.constraint_type in ('PRIMARY KEY', 'UNIQUE')";
            primaryConstraintExecuter = (_Executer) serverSession.executeQueryParameterised(primaryKeyConstraint, 0);
        }
        return primaryConstraintExecuter;
    }

    public _Executer getCheckConstraintForDomainExecuter() throws DException {
        if (checkDomainConstraintExecuter == null) {
            String checkDomainConstraint = "select * from " + SystemTables.domain_constraints_TableName + " as one, " + SystemTables.check_column_usage_TableName + " as two " + " where one.constraint_catalog = two.constraint_catalog " + " and one.constraint_schema = two.constraint_schema " + " and one.constraint_name = two.constraint_name " + " and two.table_catalog = ? " + " and two.table_schema = ? " + " and two.table_name = ? " + " and two.column_name = ? ";
            checkDomainConstraintExecuter = (_Executer) serverSession.executeQueryParameterised(checkDomainConstraint, 0);
        }
        return checkDomainConstraintExecuter;
    }

    public _Executer getCheckColumnConstraintExecuter() throws DException {
        if (checkColumnConstraintExecuter == null) {
            String checkColumnConstraint = "select * from " + SystemTables.table_constraints_TableName + " as one, " + SystemTables.check_column_usage_TableName + " as two " + " where one.constraint_type = 'CHECK' " + " and one.constraint_catalog = two.constraint_catalog " + " and one.constraint_schema = two.constraint_schema " + " and one.constraint_name = two.constraint_name " + " and two.table_catalog = ? " + " and two.table_schema = ? " + " and two.table_name = ? " + " and two.column_name = ? ";
            checkColumnConstraintExecuter = (_Executer) serverSession.executeQueryParameterised(checkColumnConstraint, 0);
        }
        return checkColumnConstraintExecuter;
    }

    public _Executer getCheckTableConstraintExecuter() throws DException {
        if (checkTableConstraintExecuter == null) {
            String checkTableConstraint = "select * from " + SystemTables.table_constraints_TableName + " as one, " + SystemTables.check_table_usage_TableName + " as two " + " where one.constraint_type = 'CHECK' " + " and one.constraint_catalog = two.constraint_catalog " + " and one.constraint_schema = two.constraint_schema " + " and one.constraint_name = two.constraint_name " + " and two.table_catalog = ? " + " and two.table_schema = ? " + " and two.table_name = ? ";
            checkTableConstraintExecuter = (_Executer) serverSession.executeQueryParameterised(checkTableConstraint, 0);
        }
        return checkTableConstraintExecuter;
    }

    public _Executer getTriggerOnTableExecuter() throws DException {
        if (executerTriggerOnTableForUser == null) {
            String triggerOnTableForUser = "select * from " + SystemTables.triggers_TableName + " as one, " + SystemTables.schema_TableName + " as two " + " where one.EVENT_OBJECT_CATALOG = ? " + " and one.EVENT_OBJECT_SCHEMA = ? " + " and  one.EVENT_OBJECT_TABLE = ? " + " and one.TRIGGER_CATALOG = two.CATALOG_NAME " + " and one.TRIGGER_SCHEMA = two.SCHEMA_NAME " + " and two.SCHEMA_OWNER = ? ";
            executerTriggerOnTableForUser = (_Executer) serverSession.executeQueryParameterised(triggerOnTableForUser, 0);
        }
        return executerTriggerOnTableForUser;
    }

    public _Executer getAbandonedViewForColumnExecuter() throws DException {
        if (executerAbandonedViewForColumn == null) {
            String queryAbandonedViewForColumn = " select one.* from " + SystemTables.views_TableName + " as one, " + SystemTables.view_colum_usage_TableName + " as two " + " where two.TABLE_CATALOG = ? " + " and two.TABLE_SCHEMA = ? " + " and two.TABLE_NAME = ? " + " and two.COLUMN_NAME = ? " + " and one.TABLE_CATALOG = two.VIEW_CATALOG " + " and one.TABLE_SCHEMA = two.VIEW_SCHEMA " + " and one.TABLE_NAME = two.VIEW_NAME ";
            executerAbandonedViewForColumn = (_Executer) serverSession.executeQueryParameterised(queryAbandonedViewForColumn, 0);
        }
        return executerAbandonedViewForColumn;
    }

    public _Executer getAbandonedViewForTableExecuter() throws DException {
        if (executerAbandonedViewForTable == null) {
            String queryAbandonedViewForTable = " select one.* from " + SystemTables.views_TableName + " as one, " + SystemTables.view_table_usage_TableName + " as two " + " where two.TABLE_CATALOG = ? " + " and two.TABLE_SCHEMA = ? " + " and two.TABLE_NAME = ? " + " and one.TABLE_CATALOG = two.VIEW_CATALOG " + " and one.TABLE_SCHEMA = two.VIEW_SCHEMA " + " and one.TABLE_NAME = two.VIEW_NAME ";
            executerAbandonedViewForTable = (_Executer) serverSession.executeQueryParameterised(queryAbandonedViewForTable, 0);
        }
        return executerAbandonedViewForTable;
    }

    public _Executer getIndexExecuter() throws DException {
        if (indexExecuter == null) {
            String indexColumnsQuery = "select column_name, orderType from " + SystemTables.INDEXCOLUMNS + " where table_catalog = ? and table_schema = ? and table_name = ? and indexname = ?";
            indexExecuter = (_Executer) serverSession.executeQueryParameterised(indexColumnsQuery, 0);
        }
        return indexExecuter;
    }

    public _Executer getDeleteConstraintExecuter() throws DException {
        if (deleteConstraintExecuter == null) {
            String queryForConstraints = "select * from " + SqlSchemaConstants.table_constraints_TableName + " where table_catalog = ? and table_schema = ? and table_name = ?";
            deleteConstraintExecuter = (_Executer) serverSession.executeQueryParameterised(queryForConstraints, 0);
        }
        return deleteConstraintExecuter;
    }

    public _Executer getReferentialConstraintExecuter() throws DException {
        if (referentialConstraintExecuter == null) {
            String referentialQuery = " Select * from " + SystemTables.referential_constraints_TableName + " as refCons, " + SystemTables.table_constraints_TableName + " as tabCons where " + " tabCons.table_catalog = ? and tabCons.table_schema = ? and tabCons.table_name = ? and ( tabCons.constraint_type = '" + SqlKeywords.PRIMARY + " " + SqlKeywords.KEY + "' or tabCons.constraint_type = '" + SqlKeywords.UNIQUE + "') and refCons.unique_constraint_catalog = tabCons.constraint_catalog and " + " refCons.unique_constraint_schema = tabCons.constraint_schema and refCons.unique_constraint_name = tabCons.constraint_name";
            referentialConstraintExecuter = (_Executer) serverSession.executeQueryParameterised(referentialQuery, 0);
        }
        return referentialConstraintExecuter;
    }

    public _Executer getConstraintExecuter() throws DException {
        if (constraintExecuter == null) {
            String tableConstraintQuery = " Select * from " + SystemTables.table_constraints_TableName + " where " + SystemTables.table_constraints_TableName + ".constraint_catalog = ? and " + SystemTables.table_constraints_TableName + ".constraint_schema = ? and " + SystemTables.table_constraints_TableName + ".constraint_name = ?  ";
            constraintExecuter = (_Executer) serverSession.executeQueryParameterised(tableConstraintQuery, 0);
        }
        return constraintExecuter;
    }

    public _Executer getViewTableUsageExecuter() throws DException {
        if (viewTableUsageExecuter == null) {
            String queryForViewTableUsage = "select * from " + SqlSchemaConstants.view_table_usage_TableName + " where table_catalog = ? and table_schema = ? and table_name = ?";
            viewTableUsageExecuter = (_Executer) serverSession.executeQueryParameterised(queryForViewTableUsage, 0);
        }
        return viewTableUsageExecuter;
    }

    public _Executer getTriggerTableUsageExecuter() throws DException {
        if (triggerTableUsageExecuter == null) {
            String queryForTriggerTableUsage = "select * from " + SqlSchemaConstants.trigger_table_usage_TableName + " where table_catalog = ? and table_schema = ? and table_name = ?";
            triggerTableUsageExecuter = (_Executer) serverSession.executeQueryParameterised(queryForTriggerTableUsage, 0);
        }
        return triggerTableUsageExecuter;
    }

    public _Executer getRoutineTableUsageExecuter() throws DException {
        if (routineTableUsageExecuter == null) {
            String queryForRoutineTableUsage = "select * from " + SqlSchemaConstants.routine_table_usage_TableName + " where table_catalog = ? and table_schema = ? and table_name = ?";
            routineTableUsageExecuter = (_Executer) serverSession.executeQueryParameterised(queryForRoutineTableUsage, 0);
        }
        return routineTableUsageExecuter;
    }

    public _Executer getTableReferencedCheckConstraintExecuter() throws DException {
        if (tableReferencedCheckConstraintExecuter == null) {
            String checkConstraintQuery = " Select * from " + SystemTables.check_table_usage_TableName + " as one , " + SystemTables.table_constraints_TableName + " as two " + " where one.table_catalog = ? and one.table_schema = ? and one.table_name = ? " + " and Not(two.table_catalog = ? and two.table_schema = ? and two.table_name = ?) " + " and one.CONSTRAINT_CATALOG = two.CONSTRAINT_CATALOG and one.CONSTRAINT_SCHEMA = two.CONSTRAINT_SCHEMA " + " and one.CONSTRAINT_NAME = two.CONSTRAINT_NAME ";
            tableReferencedCheckConstraintExecuter = (_Executer) serverSession.executeQueryParameterised(checkConstraintQuery, 0);
        }
        return tableReferencedCheckConstraintExecuter;
    }

    public _Executer getConditionIndexInfoExecuter() throws DException {
        if (conditionIndexInfoExecuter == null) {
            String selectQuery = "select indextablename, numberOfRecords, rootNodeAddress, rootClusterSize, rootRecordNumber, fixedVariable,is_system_generated from " + SystemTables.INDEXINFO + " where table_catalog = ? and table_schema = ? and table_name = ? and indexname = ?";
            conditionIndexInfoExecuter = (_Executer) serverSession.executeQueryParameterised(selectQuery, 0);
        }
        return conditionIndexInfoExecuter;
    }

    public _Executer getDependentReferentialConstraintExecuter() throws DException {
        if (dependentReferentialConstraintExecuter == null) {
            String dependentReferentialConstraintQuery = "select * from " + SqlSchemaConstants.referential_constraints_TableName + " where unique_constraint_catalog = ? and unique_constraint_schema = ? " + " and unique_constraint_name = ? ";
            dependentReferentialConstraintExecuter = (_Executer) serverSession.executeQueryParameterised(dependentReferentialConstraintQuery, 0);
        }
        return dependentReferentialConstraintExecuter;
    }

    public _Executer getSequenceExecuter() throws DException {
        if (sequenceExecuter == null) {
            String sequenceQuery = "select * from " + SystemTables.sequence_number_TableName + " where sequence_catalog = ? and sequence_schema = ? and sequence_name = ? ";
            sequenceExecuter = (_Executer) serverSession.executeQueryParameterised(sequenceQuery, 0);
        }
        return sequenceExecuter;
    }

    public _Executer getFullTextInfoExecuter() throws DException {
        if (fullTextInfoExecuter == null) {
            String fullTextInfoQuery = "select FixedVariable,* from " + SystemTables.FULLTEXTINFO + " where table_catalog = ? and table_schema = ? and table_name = ? and indexname = ?";
            fullTextInfoExecuter = (_Executer) serverSession.executeQueryParameterised(fullTextInfoQuery, 0);
        }
        return fullTextInfoExecuter;
    }

    public _Executer getFullTextColumnInfoExecuter() throws DException {
        if (fullTextColumnInfoExecuter == null) {
            String fullTextColumnnInfoQuery = "select column_name,orderType from " + SystemTables.FULLTEXTCOLUMNINFO + " where table_catalog = ? and table_schema = ? and table_name = ? and indexname = ?";
            fullTextColumnInfoExecuter = (_Executer) serverSession.executeQueryParameterised(fullTextColumnnInfoQuery, 0);
        }
        return fullTextColumnInfoExecuter;
    }

    public _Executer getColumnsExecuter() throws DException {
        if (columnsExecuter == null) {
            String columnsQuery = "select * from " + SqlSchemaConstants.columns_TableName + " where table_catalog = ? and table_schema = ? and table_name = ?";
            columnsExecuter = (_Executer) serverSession.executeQueryParameterised(columnsQuery, 0);
        }
        return columnsExecuter;
    }

    public _Iterator getInternalIteratorForColumnDescriptor(_ServerSession currentSession, QualifiedIdentifier tableName, booleanvalueexpression condition, Object[] conditionParameters) throws DException {
        if (columnsIter == null) {
            columnsTableDetail = new TableDetails();
            columnsTableDetail.setTableName(tableName.getTableName());
            _ServerSession globalSession = currentSession.getGlobalSession();
            ConditionSingleTableExecuter conSingTE = new ConditionSingleTableExecuter(null, columnsTableDetail, globalSession, condition, null);
            columnsIter = globalSession.getInternalIterator(tableName, conSingTE);
        }
        columnsIter.setConditionVariableValue(condition.getReferences(new TableDetails[] { columnsTableDetail }), FieldUtility.getFields(conditionParameters), 1);
        return columnsIter;
    }

    public _Iterator getInternalIteratorForColumnPrivilegeDescriptor(_ServerSession currentSession, QualifiedIdentifier tableName, booleanvalueexpression condition, Object[] conditionParameters) throws DException {
        if (columnPrivilegesIter == null) {
            columnsPrivilegeTableDetail = new TableDetails();
            columnsPrivilegeTableDetail.setTableName(tableName.getTableName());
            _ServerSession globalSession = currentSession.getGlobalSession();
            ConditionSingleTableExecuter conSingTE = new ConditionSingleTableExecuter(null, columnsPrivilegeTableDetail, globalSession, condition, null);
            columnPrivilegesIter = globalSession.getInternalIterator(tableName, conSingTE);
        }
        columnPrivilegesIter.setConditionVariableValue(condition.getReferences(new TableDetails[] { columnsPrivilegeTableDetail }), FieldUtility.getFields(conditionParameters), 1);
        return columnPrivilegesIter;
    }

    public _Iterator getInternalIteratorForTablePrivilegeDescriptor(_ServerSession currentSession, QualifiedIdentifier tableName, booleanvalueexpression condition, Object[] conditionParameters) throws DException {
        if (tablePrivilegesIter == null) {
            tablePrivilegeTableDetail = new TableDetails();
            tablePrivilegeTableDetail.setTableName(tableName.getTableName());
            _ServerSession globalSession = currentSession.getGlobalSession();
            ConditionSingleTableExecuter conSingTE = new ConditionSingleTableExecuter(null, tablePrivilegeTableDetail, globalSession, condition, null);
            tablePrivilegesIter = globalSession.getInternalIterator(tableName, conSingTE);
        }
        tablePrivilegesIter.setConditionVariableValue(condition.getReferences(new TableDetails[] { tablePrivilegeTableDetail }), FieldUtility.getFields(conditionParameters), 1);
        return tablePrivilegesIter;
    }

    public _Iterator getInternalIteratorForDataTypeDescriptor(_ServerSession currentSession, QualifiedIdentifier tableName, booleanvalueexpression condition, Object[] conditionParameters) throws DException {
        if (dataTypeIter == null) {
            dataTypeTableDetail = new TableDetails();
            dataTypeTableDetail.setTableName(tableName.getTableName());
            _ServerSession globalSession = currentSession.getGlobalSession();
            ConditionSingleTableExecuter conSingTE = new ConditionSingleTableExecuter(null, dataTypeTableDetail, globalSession, condition, null);
            dataTypeIter = globalSession.getInternalIterator(tableName, conSingTE);
        }
        dataTypeIter.setConditionVariableValue(condition.getReferences(new TableDetails[] { dataTypeTableDetail }), FieldUtility.getFields(conditionParameters), 1);
        return dataTypeIter;
    }

    public booleanvalueexpression getIndexInfoTableCondition() throws DException {
        if (indexInfoTableCondition == null) {
            String bve = "TABLE_CATALOG=? and TABLE_SCHEMA=? and TABLE_NAME=? and INDEXNAME=?";
            indexInfoTableCondition = ConditionParser.parseCondition(bve, serverSession, SqlSchemaConstants.INDEXINFO);
        }
        return indexInfoTableCondition;
    }

    public booleanvalueexpression getFullTextIndexInfoTableCondition() throws DException {
        if (fullTextIndexInfoTableCondition == null) {
            String bve = "TABLE_CATALOG=? and TABLE_SCHEMA=? and TABLE_NAME=? and INDEXNAME=?";
            fullTextIndexInfoTableCondition = ConditionParser.parseCondition(bve, serverSession, SqlSchemaConstants.FULLTEXTINFO);
        }
        return fullTextIndexInfoTableCondition;
    }

    public _Executer getReferencedConstraintExecuter() throws DException {
        if (referencedConstraintExecuter == null) {
            String clause = "select * from " + SqlSchemaConstants.table_constraints_TableName + " where table_catalog = ? and table_schema = ? and table_name = ? " + " and (constraint_type = 'Primary Key' or constraint_type = 'Unique')";
            referencedConstraintExecuter = (_Executer) serverSession.executeQueryParameterised(clause, 0);
        }
        return referencedConstraintExecuter;
    }

    public _Executer getKeyColumnExecuter() throws DException {
        if (keyColumnExecuter == null) {
            String clause = "select * from " + SqlSchemaConstants.key_column_usage_TableName + " where constraint_catalog = ? and constraint_schema = ? and constraint_name = ? ";
            keyColumnExecuter = (_Executer) serverSession.executeQueryParameterised(clause, 0);
        }
        return keyColumnExecuter;
    }
}
