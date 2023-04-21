package com.daffodilwoods.daffodildb.server.sql99.ddl.descriptors;

import java.util.*;
import com.daffodilwoods.daffodildb.server.datadictionarysystem.*;
import com.daffodilwoods.database.general.*;
import com.daffodilwoods.database.resource.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author harvinder bhutani
 * @version 1.0
 */
public class SystemConstraintsFactory {

    private SystemConstraintsFactory() {
    }

    public static void getReferencingConstraints(TreeMap constraints, String tableName, _DataDictionary dataDictionary) throws DException {
        Object[] columnsAndIndexes = getForeignKeyInformation(tableName);
        if (columnsAndIndexes != null) {
            for (int i = 0; i < columnsAndIndexes.length; i++) {
                Object[] columnsAndIndex = (Object[]) columnsAndIndexes[i];
                ReferentialConstraintDescriptor constraint = new ReferentialConstraintDescriptor();
                TableConstraintDescriptor tableConstraintDescriptor = new TableConstraintDescriptor();
                TableConstraintDescriptor referencedTableConstraintDescriptor = new TableConstraintDescriptor();
                constraint.constraint_catalog = SystemTables.systemCatalog;
                constraint.constraint_schema = SystemTables.systemSchema;
                constraint.constraint_name = columnsAndIndex[0].toString();
                constraint.unique_constraint_catalog = SystemTables.systemCatalog;
                constraint.unique_constraint_schema = SystemTables.systemSchema;
                constraint.unique_constraint_name = columnsAndIndex[3].toString();
                constraint.match_option = columnsAndIndex[4].toString();
                constraint.update_rule = SqlSchemaConstants.NoAction;
                constraint.delete_rule = SqlSchemaConstants.NoAction;
                referencedTableConstraintDescriptor.constraint_catalog = SystemTables.systemCatalog;
                referencedTableConstraintDescriptor.constraint_schema = SystemTables.systemSchema;
                referencedTableConstraintDescriptor.constraint_name = columnsAndIndex[3].toString();
                tableConstraintDescriptor.constraint_catalog = SystemTables.systemCatalog;
                tableConstraintDescriptor.constraint_schema = SystemTables.systemSchema;
                tableConstraintDescriptor.constraint_name = columnsAndIndex[0].toString();
                tableConstraintDescriptor.constraint_type = "Foreign Key";
                tableConstraintDescriptor.table_catalog = SystemTables.systemCatalog;
                tableConstraintDescriptor.table_schema = SystemTables.systemSchema;
                tableConstraintDescriptor.table_name = tableName;
                tableConstraintDescriptor.is_deferrable = SqlSchemaConstants.NO;
                tableConstraintDescriptor.initially_deferred = SqlSchemaConstants.NO;
                ArrayList arr = getKeyColumnDescriptors(columnsAndIndex, tableConstraintDescriptor);
                tableConstraintDescriptor.constraintColumnDescriptors = arr;
                tableConstraintDescriptor.intializeColumnIndexes(dataDictionary.getColumnCharacteristics(new QualifiedIdentifier(SystemTables.systemCatalog, SystemTables.systemSchema, tableName), true));
                Object[] referencedColumnsAndIndexes = getPrimaryKeyInformation(columnsAndIndex[5].toString());
                if (referencedColumnsAndIndexes != null) {
                    referencedTableConstraintDescriptor.table_catalog = SystemTables.systemCatalog;
                    referencedTableConstraintDescriptor.table_schema = SystemTables.systemSchema;
                    referencedTableConstraintDescriptor.table_name = columnsAndIndex[5].toString();
                    referencedTableConstraintDescriptor.constraint_catalog = SystemTables.systemCatalog;
                    referencedTableConstraintDescriptor.constraint_schema = SystemTables.systemSchema;
                    referencedTableConstraintDescriptor.initially_deferred = SqlSchemaConstants.NO;
                    referencedTableConstraintDescriptor.is_deferrable = SqlSchemaConstants.NO;
                    referencedTableConstraintDescriptor.constraint_type = "PRIMARY KEY";
                    referencedTableConstraintDescriptor.constraint_name = referencedColumnsAndIndexes[0].toString();
                    ArrayList arr1 = getKeyColumnDescriptors(referencedColumnsAndIndexes, referencedTableConstraintDescriptor);
                    referencedTableConstraintDescriptor.constraintColumnDescriptors = arr1;
                    referencedTableConstraintDescriptor.intializeColumnIndexes(dataDictionary.getColumnCharacteristics(new QualifiedIdentifier(SystemTables.systemCatalog, SystemTables.systemSchema, columnsAndIndex[5].toString()), true));
                    referencedTableConstraintDescriptor.constraintDescriptor = constraint;
                    constraint.referencedTableConstraintDescriptor = referencedTableConstraintDescriptor;
                    constraints.put(constraint.constraint_name, constraint);
                }
                constraint.tableConstraintDescriptor = tableConstraintDescriptor;
                tableConstraintDescriptor.constraintDescriptor = constraint;
                constraints.put(constraint.constraint_name, constraint);
            }
        }
    }

    private static Object[] getForeignKeyInformation(String tableName) throws DException {
        if (tableName.equalsIgnoreCase("ROLE_AUTHORIZATION_DESCRIPTORS")) {
            return new Object[] { new Object[] { "ROLE_AUTHORIZATION_DESCRIPTORS_FOREIGN_KEY_ROLES", new String[] { "ROLE_NAME" }, new int[] { 1 }, "ROLES_PRIMARY_KEY", "SIMPLE", "ROLES" } };
        } else if (tableName.equalsIgnoreCase("SCHEMATA")) {
            return new Object[] { new Object[] { "SCHEMATA_FOREIGN_KEY", new String[] { "SCHEMA_OWNER" }, new int[] { 1 }, "USERS_PRIMARY_KEY", "SIMPLE", "USERS" } };
        } else if (tableName.equalsIgnoreCase("CHARACTER_SETS")) {
            return new Object[] { new Object[] { "CHARACTER_SETS_FOREIGN_KEY_SCHEMATA", new String[] { "CHARACTER_SET_CATALOG", "CHARACTER_SET_SCHEMA" }, new int[] { 1, 2 }, "SCHEMATA_PRIMARY_KEY", "SIMPLE", "SCHEMATA" } };
        } else if (tableName.equalsIgnoreCase("COLLATIONS")) {
            return new Object[] { new Object[] { "COLLATIONS_PAD_FOREIGN_KEY_SCHEMATA", new String[] { "COLLATION_CATALOG", "COLLATION_SCHEMA" }, new int[] { 1, 2 }, "SCHEMATA_PRIMARY_KEY", "SIMPLE", "SCHEMATA" } };
        } else if (tableName.equalsIgnoreCase("DOMAINS")) {
            return new Object[] { new Object[] { "DOMAINS_FOREIGN_KEY_SCHEMATA", new String[] { "DOMAIN_CATALOG", "DOMAIN_SCHEMA" }, new int[] { 1, 2 }, "SCHEMATA_PRIMARY_KEY", "SIMPLE", "SCHEMATA" } };
        } else if (tableName.equalsIgnoreCase("TABLES")) {
            return new Object[] { new Object[] { "TABLES_FOREIGN_KEY_SCHEMATA", new String[] { "TABLE_CATALOG", "TABLE_SCHEMA" }, new int[] { 1, 2 }, "SCHEMATA_PRIMARY_KEY", "SIMPLE", "SCHEMATA" } };
        } else if (tableName.equalsIgnoreCase("COLUMNS")) {
            return new Object[] { new Object[] { "COLUMNS_FOREIGN_KEY_TABLES", new String[] { "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME" }, new int[] { 1, 2, 3 }, "TABLES_PRIMARY_KEY", "SIMPLE", "TABLES" } };
        } else if (tableName.equalsIgnoreCase("DATA_TYPE_DESCRIPTOR")) {
            return new Object[] { new Object[] { "DATA_TYPE_DESCRIPTOR_FOREIGN_KEY_SCHEMATA", new String[] { "USER_DEFINED_TYPE_CATALOG", "USER_DEFINED_TYPE_SCHEMA" }, new int[] { 1, 2 }, "SCHEMATA_PRIMARY_KEY", "SIMPLE", "SCHEMATA" } };
        } else if (tableName.equalsIgnoreCase("KEY_COLUMN_USAGE")) {
            return new Object[] { new Object[] { "KEY_COLUMN_USAGE_FOREIGN_KEY_COLUMNS", new String[] { "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME", "COLUMN_NAME" }, new int[] { 1, 2, 3, 4 }, "COLUMNS_PRIMARY_KEY", "SIMPLE", "COLUMNS" } };
        } else if (tableName.equalsIgnoreCase("CHECK_COLUMN_USAGE")) {
            return new Object[] { new Object[] { "CHECK_COLUMN_USAGE_FOREIGN_KEY_CHECK_CONSTRAINTS", new String[] { "CONSTRAINT_CATALOG", "CONSTRAINT_SCHEMA", "CONSTRAINT_NAME" }, new int[] { 1, 2, 3 }, "CHECK_CONSTRAINTS_PRIMARY_KEY", "SIMPLE", "CHECK_CONSTRAINTS" } };
        } else if (tableName.equalsIgnoreCase("CHECK_TABLE_USAGE")) {
            return new Object[] { new Object[] { "CHECK_TABLE_USAGE_FOREIGN_KEY_CHECK_CONSTRAINTS", new String[] { "CONSTRAINT_CATALOG", "CONSTRAINT_SCHEMA", "CONSTRAINT_NAME" }, new int[] { 1, 2, 3 }, "CHECK_CONSTRAINTS_PRIMARY_KEY", "SIMPLE", "CHECK_CONSTRAINTS" } };
        } else if (tableName.equalsIgnoreCase("TRIGGERS")) {
            return new Object[] { new Object[] { "TRIGGERS_FOREIGN_KEY_SCHEMATA", new String[] { "TRIGGER_CATALOG", "TRIGGER_SCHEMA" }, new int[] { 1, 2 }, "SCHEMATA_PRIMARY_KEY", "SIMPLE", "SCHEMATA" } };
        } else if (tableName.equalsIgnoreCase("TRIGGER_TABLE_USAGE")) {
            return new Object[] { new Object[] { "TRIGGER_TABLE_USAGE_FOREIGN_KEY_TRIGGERS", new String[] { "TRIGGER_CATALOG", "TRIGGER_SCHEMA", "TRIGGER_NAME" }, new int[] { 1, 2, 3 }, "TRIGGERS_PRIMARY_KEY", "SIMPLE", "TRIGGERS" } };
        } else if (tableName.equalsIgnoreCase("TRIGGER_COLUMN_USAGE")) {
            return new Object[] { new Object[] { "TRIGGER_COLUMN_USAGE_CHECK_REFERENCES_COLUMNS", new String[] { "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME", "COLUMN_NAME" }, new int[] { 1, 2, 3, 4 }, "COLUMNS_PRIMARY_KEY", "SIMPLE", "COLUMNS" }, new Object[] { "TRIGGER_COLUMN_USAGE_FOREIGN_KEY_TRIGGERS", new String[] { "TRIGGER_CATALOG", "TRIGGER_SCHEMA", "TRIGGER_NAME" }, new int[] { 1, 2, 3 }, "TRIGGERS_PRIMARY_KEY", "SIMPLE", "TRIGGERS" } };
        } else if (tableName.equalsIgnoreCase("TRIGGERED_UPDATE_COLUMNS")) {
            return new Object[] { new Object[] { "TRIGGERED_UPDATE_COLUMNS_FOREIGN_KEY_TRIGGERS", new String[] { "TRIGGER_CATALOG", "TRIGGER_SCHEMA", "TRIGGER_NAME" }, new int[] { 1, 2, 3 }, "TRIGGERS_PRIMARY_KEY", "SIMPLE", "TRIGGERS" }, new Object[] { "TRIGGERED_UPDATE_COLUMNS_FOREIGN_KEY_COLUMNS", new String[] { "EVENT_OBJECT_CATALOG", "EVENT_OBJECT_SCHEMA", "EVENT_OBJECT_TABLE", "EVENT_OBJECT_COLUMN" }, new int[] { 1, 2, 3, 4 }, "COLUMNS_PRIMARY_KEY", "SIMPLE", "COLUMNS" } };
        } else if (tableName.equalsIgnoreCase("VIEW_COLUMN_USAGE")) {
            return new Object[] { new Object[] { "VIEW_COLUMN_USAGE_FOREIGN_KEY_VIEWS", new String[] { "VIEW_CATALOG", "VIEW_SCHEMA", "VIEW_NAME" }, new int[] { 1, 2, 3 }, "VIEWS_PRIMARY_KEY", "SIMPLE", "VIEWS" } };
        } else if (tableName.equalsIgnoreCase("VIEW_TABLE_USAGE")) {
            return new Object[] { new Object[] { "VIEW_TABLE_USAGE_FOREIGN_KEY_VIEWS", new String[] { "VIEW_CATALOG", "VIEW_SCHEMA", "VIEW_NAME" }, new int[] { 1, 2, 3 }, "VIEWS_PRIMARY_KEY", "SIMPLE", "VIEWS" } };
        } else if (tableName.equalsIgnoreCase("DOMAIN_CONSTRAINTS")) {
            return new Object[] { new Object[] { "DOMAIN_CONSTRAINTS_FOREIGN_KEY_SCHEMATA", new String[] { "CONSTRAINT_CATALOG", "CONSTRAINT_SCHEMA" }, new int[] { 1, 2 }, "SCHEMATA_PRIMARY_KEY", "SIMPLE", "SCHEMATA" }, new Object[] { "DOMAIN_CONSTRAINTS_FOREIGN_KEY_CHECK_CONSTRAINTS", new String[] { "DOMAIN_CATALOG", "DOMAIN_SCHEMA", "CONSTRAINT_NAME" }, new int[] { 1, 2, 3 }, "CHECK_CONSTRAINTS_PRIMARY_KEY", "SIMPLE", "CHECK_CONSTRAINTS" }, new Object[] { "DOMAIN_CONSTRAINTS_FOREIGN_KEY_DOMAINS", new String[] { "DOMAIN_CATALOG", "DOMAIN_SCHEMA", "DOMAIN_NAME" }, new int[] { 1, 2, 3 }, "DOMAINS_PRIMARY_KEY", "SIMPLE", "DOMAINS" } };
        } else if (tableName.equalsIgnoreCase("COLUMN_PRIVILEGES")) {
            return new Object[] { new Object[] { "COLUMN_PRIVILEGE_FOREIGN_KEY_COLUMNS", new String[] { "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME", "COLUMN_NAME" }, new int[] { 1, 2, 3, 4 }, "COLUMNS_PRIMARY_KEY", "SIMPLE", "COLUMNS" } };
        } else if (tableName.equalsIgnoreCase("TABLE_PRIVILEGES")) {
            return new Object[] { new Object[] { "TABLE_PRIVILEGE_FOREIGN_KEY_TABLES", new String[] { "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME" }, new int[] { 1, 2, 3 }, "TABLES_PRIMARY_KEY", "SIMPLE", "TABLES" } };
        } else if (tableName.equalsIgnoreCase("ROUTINES")) {
            return new Object[] { new Object[] { "ROUTINES_FOREIGN_KEY_SCHEMATA", new String[] { "ROUTINE_CATALOG", "ROUTINE_SCHEMA" }, new int[] { 1, 2 }, "SCHEMATA_PRIMARY_KEY", "SIMPLE", "SCHEMATA" } };
        } else if (tableName.equalsIgnoreCase("PARAMETERS")) {
            return new Object[] { new Object[] { "PARAMETERS_FOREIGN_KEY_SCHEMATA", new String[] { "SPECIFIC_CATALOG", "SPECIFIC_SCHEMA" }, new int[] { 1, 2 }, "SCHEMATA_PRIMARY_KEY", "SIMPLE", "SCHEMATA" } };
        } else if (tableName.equalsIgnoreCase("ROUTINE_PRIVILEGES")) {
            return new Object[] { new Object[] { "ROUTINE_PRIVILEGES_FOREIGN_KEY_TABLES", new String[] { "SPECIFIC_CATALOG", "SPECIFIC_SCHEMA", "SPECIFIC_NAME" }, new int[] { 1, 2, 3 }, "ROUTINES_PRIMARY_KEY", "SIMPLE", "ROUTINES" } };
        } else if (tableName.equalsIgnoreCase("INDEXCOLUMNS")) {
            return new Object[] { new Object[] { "INDEXCOLUMNS_FOREIGN_KEY_COLUMNS", new String[] { "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME", "INDEXNAME" }, new int[] { 1, 2, 3, 4 }, "INDEXINFO_PRIMARY_KEY", "SIMPLE", "INDEXINFO" } };
        } else if (tableName.equalsIgnoreCase("FULLTEXTCOLUMNINFO")) {
            return new Object[] { new Object[] { "FULLTEXTINDEXCOLUMNS_FOREIGN_KEY_COLUMNS", new String[] { "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME", "INDEXNAME" }, new int[] { 1, 2, 3, 4 }, "FULLTEXTINDEX_PRIMARY_KEY", "SIMPLE", "FULLTEXTINFO" } };
        } else if (tableName.equalsIgnoreCase("User_Defined_Types")) {
            return new Object[] { new Object[] { "USER_DEFINED_TYPES_FOREIGN_KEY_SCHEMATA", new String[] { "USER_DEFINED_TYPE_CATALOG", "USER_DEFINED_TYPE_SCHEMA" }, new int[] { 1, 2 }, "SCHEMATA_PRIMARY_KEY", "SIMPLE", "SCHEMATA" }, new Object[] { "USER_DEFINED_TYPES_FOREIGN_KEY_ROUTINES", new String[] { "ORDERING_ROUTINE_CATALOG", "ORDERING_ROUTINE_SCHEMA", "ORDERING_ROUTINE_NAME" }, new int[] { 1, 2, 3 }, "ROUTINES_PRIMARY_KEY", "SIMPLE", "ROUTINES" } };
        } else if (tableName.equalsIgnoreCase("User_Defined_Type_Privileges")) {
            return new Object[] { new Object[] { "USER_DEFINED_TYPE_PRIVILEGES_FOREIGN_KEY_USER_DEFINED_TYPE", new String[] { "USER_DEFINED_TYPE_CATALOG", "USER_DEFINED_TYPE_SCHEMA", "USER_DEFINED_TYPE_NAME" }, new int[] { 1, 2, 3 }, "USER_DEFINED_TYPES_PRIMARY_KEY", "SIMPLE", "USER_DEFINED_TYPES" } };
        } else if (tableName.equalsIgnoreCase("Transforms_Table")) {
            return new Object[] { new Object[] { "TRANSFORMS_TYPES_FOREIGN_KEY", new String[] { "USER_DEFINED_TYPE_CATALOG", "USER_DEFINED_TYPE_SCHEMA", "USER_DEFINED_TYPE_NAME" }, new int[] { 1, 2, 3 }, "USER_DEFINED_TYPES_PRIMARY_KEY", "SIMPLE", "USER_DEFINED_TYPES" }, new Object[] { "TRANSFORMS_ROUTINES_FOREIGN_KEY", new String[] { "SPECIFIC_CATALOG", "SPECIFIC_SCHEMA", "SPECIFIC_NAME" }, new int[] { 1, 2, 3 }, "ROUTINES_PRIMARY_KEY", "SIMPLE", "ROUTINES" } };
        } else if (tableName.equalsIgnoreCase("Method_Specifications")) {
            return new Object[] { new Object[] { "METHOD_SPECIFICATIONS_FOREIGN_KEY_SCHEMATA", new String[] { "SPECIFIC_CATALOG", "SPECIFIC_SCHEMA" }, new int[] { 1, 2 }, "SCHEMATA_PRIMARY_KEY", "SIMPLE", "SCHEMATA" }, new Object[] { "METHOD_SPECIFICATIONS_FOREIGN_KEY_USER_DEFINED_TYPES", new String[] { "USER_DEFINED_TYPE_CATALOG", "USER_DEFINED_TYPE_SCHEMA", "USER_DEFINED_TYPE_NAME" }, new int[] { 1, 2, 3 }, "USER_DEFINED_TYPES_PRIMARY_KEY", "FULL", "USER_DEFINED_TYPES" } };
        } else if (tableName.equalsIgnoreCase("Method_Specifications_Paramters")) {
            return new Object[] { new Object[] { "METHOD_SPECIFICATION_PARAMETERS_FOREIGN_KEY", new String[] { "SPECIFIC_CATALOG", "SPECIFIC_SCHEMA", "SPECIFIC_NAME" }, new int[] { 1, 2, 3 }, "METHOD_SPECIFICATIONS_PRIMARY_KEY", "SIMPLE", "Method_Specifications" } };
        } else if (tableName.equalsIgnoreCase("ROUTINE_TABLE_USAGE")) {
            return new Object[] { new Object[] { "ROUTINE_TABLE_USAGE_FOREIGN_KEY_ROUTINES", new String[] { "SPECIFIC_CATALOG", "SPECIFIC_SCHEMA", "SPECIFIC_NAME" }, new int[] { 1, 2, 3 }, "ROUTINES_PRIMARY_KEY", "SIMPLE", "ROUTINES" } };
        } else if (tableName.equalsIgnoreCase("ROUTINE_COLUMN_USAGE")) {
            return new Object[] { new Object[] { "ROUTINE_COLUMN_USAGE_FOREIGN_KEY_ROUTINES", new String[] { "SPECIFIC_CATALOG", "SPECIFIC_SCHEMA", "SPECIFIC_NAME" }, new int[] { 1, 2, 3 }, "ROUTINES_PRIMARY_KEY", "SIMPLE", "ROUTINES" } };
        }
        return null;
    }

    public static _UniqueConstraint getPrimaryKeyConstraints(String tableName, _ColumnCharacteristics columnCharacteristics) throws DException {
        Object[] columnsAndIndexes = getPrimaryKeyInformation(tableName);
        if (columnsAndIndexes != null) {
            TableConstraintDescriptor tableConstraintDescriptor = new TableConstraintDescriptor();
            UniqueConstraintDescriptor constraint = new UniqueConstraintDescriptor();
            tableConstraintDescriptor.table_catalog = SystemTables.systemCatalog;
            tableConstraintDescriptor.table_schema = SystemTables.systemSchema;
            tableConstraintDescriptor.table_name = tableName;
            tableConstraintDescriptor.constraint_catalog = SystemTables.systemCatalog;
            tableConstraintDescriptor.constraint_schema = SystemTables.systemSchema;
            tableConstraintDescriptor.initially_deferred = SqlSchemaConstants.NO;
            tableConstraintDescriptor.is_deferrable = SqlSchemaConstants.NO;
            tableConstraintDescriptor.constraint_type = "PRIMARY KEY";
            tableConstraintDescriptor.constraint_name = columnsAndIndexes[0].toString();
            ArrayList arr = getKeyColumnDescriptors(columnsAndIndexes, tableConstraintDescriptor);
            tableConstraintDescriptor.constraintColumnDescriptors = arr;
            tableConstraintDescriptor.intializeColumnIndexes(columnCharacteristics);
            tableConstraintDescriptor.constraintDescriptor = constraint;
            constraint.tableConstraintDescriptor = tableConstraintDescriptor;
            return constraint;
        }
        return null;
    }

    public static void getUniqueKeyConstraints(String tableName, TreeMap constraints, _ColumnCharacteristics columnCharacteristics) throws DException {
        Object[] columnsAndIndexes = getUniqueKeyInformation(tableName);
        if (columnsAndIndexes != null) {
            TableConstraintDescriptor tableConstraintDescriptor = new TableConstraintDescriptor();
            UniqueConstraintDescriptor constraint = new UniqueConstraintDescriptor();
            tableConstraintDescriptor.table_catalog = SystemTables.systemCatalog;
            tableConstraintDescriptor.table_schema = SystemTables.systemSchema;
            tableConstraintDescriptor.table_name = tableName;
            tableConstraintDescriptor.constraint_catalog = SystemTables.systemCatalog;
            tableConstraintDescriptor.constraint_schema = SystemTables.systemSchema;
            tableConstraintDescriptor.initially_deferred = SqlSchemaConstants.NO;
            tableConstraintDescriptor.is_deferrable = SqlSchemaConstants.NO;
            tableConstraintDescriptor.constraint_type = "UNIQUE";
            tableConstraintDescriptor.constraint_name = columnsAndIndexes[0].toString();
            ArrayList arr = getKeyColumnDescriptors(columnsAndIndexes, tableConstraintDescriptor);
            tableConstraintDescriptor.constraintColumnDescriptors = arr;
            tableConstraintDescriptor.intializeColumnIndexes(columnCharacteristics);
            tableConstraintDescriptor.constraintDescriptor = constraint;
            constraint.tableConstraintDescriptor = tableConstraintDescriptor;
            constraints.put(tableConstraintDescriptor.constraint_name, constraint);
        }
    }

    public static void getCheckConstraints(DataDictionary dataDictionary, String tableName, TreeMap constraints) throws DException {
        Object[] constraintsAndClause = getCheckConstraintInformation(tableName);
        if (constraintsAndClause != null) {
            String[] constraintNames = (String[]) constraintsAndClause[0];
            String[] checkClause = (String[]) constraintsAndClause[1];
            int[] columnIndexes = (int[]) constraintsAndClause[2];
            for (int i = 0; i < constraintNames.length; i++) {
                TableConstraintDescriptor tableConstraintDescriptor = new TableConstraintDescriptor();
                CheckConstraintDescriptor constraint = new CheckConstraintDescriptor();
                tableConstraintDescriptor.constraint_catalog = SystemTables.systemCatalog;
                tableConstraintDescriptor.constraint_schema = SystemTables.systemSchema;
                tableConstraintDescriptor.constraint_name = constraintNames[i];
                tableConstraintDescriptor.constraint_type = "CHECK";
                tableConstraintDescriptor.table_catalog = SystemTables.systemCatalog;
                tableConstraintDescriptor.table_schema = SystemTables.systemSchema;
                tableConstraintDescriptor.table_name = tableName;
                tableConstraintDescriptor.is_deferrable = SqlSchemaConstants.NO;
                tableConstraintDescriptor.initially_deferred = SqlSchemaConstants.NO;
                constraint.constraint_catalog = tableConstraintDescriptor.constraint_catalog;
                constraint.constraint_schema = tableConstraintDescriptor.constraint_schema;
                constraint.constraint_name = tableConstraintDescriptor.constraint_name;
                constraint.tableConstraintDescriptor = tableConstraintDescriptor;
                tableConstraintDescriptor.constraintDescriptor = constraint;
                constraint.check_clause = checkClause[i];
                constraint.columnIndexes = columnIndexes;
                constraints.put(constraint.constraint_name, constraint);
            }
        }
        Object[] constraintsAndClauseForDomain = getCheckConstraintInformationForDomain(tableName);
        if (constraintsAndClauseForDomain != null) {
            for (int j = 0; j < constraintsAndClauseForDomain.length; j++) {
                Object[] constraintsAndColumnForDomain = (Object[]) constraintsAndClauseForDomain[j];
                ColumnCharacteristics columnCharacteristics = (ColumnCharacteristics) dataDictionary.getColumnCharacteristics(new QualifiedIdentifier(SystemTables.systemCatalog, SystemTables.systemSchema, tableName), true);
                int[] domainColumnIndexes = columnCharacteristics.getDomainColumnIndexes();
                if (domainColumnIndexes == null) return;
                for (int i = 0; i < domainColumnIndexes.length; i++) {
                    CheckConstraintDescriptor[] checkConstraints = getCheckConstraintForDomain(constraintsAndColumnForDomain);
                    if (checkConstraints != null) {
                        for (int k = 0; k < checkConstraints.length; k++) {
                            CheckConstraintDescriptor constraint = null;
                            constraints.put(checkConstraints[k].constraint_name, constraint = checkConstraints[k]);
                            constraint.addColumnIndex(domainColumnIndexes[i], new String[] { SystemTables.systemCatalog, SystemTables.systemSchema, tableName, columnCharacteristics.getColumnName(domainColumnIndexes[i]) });
                        }
                    }
                }
            }
        }
    }

    private static CheckConstraintDescriptor[] getCheckConstraintForDomain(Object[] constraintsAndClauseForDomain) throws DException {
        ArrayList constraintsList = new ArrayList();
        CheckConstraintDescriptor[] constraints = null;
        String[] constraintNames = (String[]) constraintsAndClauseForDomain[0];
        String[] checkClause = (String[]) constraintsAndClauseForDomain[1];
        for (int i = 0; i < constraintNames.length; i++) {
            DomainConstraintDescriptor domainConstraintDescriptor = new DomainConstraintDescriptor();
            CheckConstraintDescriptor constraint = new CheckConstraintDescriptor();
            domainConstraintDescriptor.domain_catalog = SystemTables.systemCatalog;
            domainConstraintDescriptor.domain_schema = "INFORMATION_SCHEMA";
            domainConstraintDescriptor.domain_name = "CARDINAL_NUMBER";
            domainConstraintDescriptor.is_deferrable = SqlSchemaConstants.NO;
            domainConstraintDescriptor.initially_deferred = SqlSchemaConstants.NO;
            domainConstraintDescriptor.constraint_catalog = SystemTables.systemCatalog;
            domainConstraintDescriptor.constraint_schema = "INFORMATION_SCHEMA";
            domainConstraintDescriptor.constraint_name = constraintNames[i];
            constraint.constraint_catalog = SystemTables.systemCatalog;
            constraint.constraint_schema = "INFORMATION_SCHEMA";
            constraint.constraint_name = constraintNames[i];
            constraint.domainConstraintDescriptor = domainConstraintDescriptor;
            constraint.check_clause = checkClause[i];
            constraintsList.add(constraint);
        }
        constraints = (CheckConstraintDescriptor[]) constraintsList.toArray(new CheckConstraintDescriptor[0]);
        return constraints;
    }

    private static Object[] getCheckConstraintInformationForDomain(String tableName) throws DException {
        if (tableName.equalsIgnoreCase("CHARACTER_SETS")) {
            return new Object[] { new Object[] { new String[] { "CARDINAL_NUMBER_DOMAIN_CHECK" }, new String[] { "NUMBER_OF_CHARACTERS  >=  0" } } };
        } else if (tableName.equalsIgnoreCase("COLUMNS")) {
            return new Object[] { new Object[] { new String[] { "CARDINAL_NUMBER_DOMAIN_CHECK" }, new String[] { "ORDINAL_POSITION  >=  0" } } };
        } else if (tableName.equalsIgnoreCase("DATA_TYPE_DESCRIPTOR")) {
            return new Object[] { new Object[] { new String[] { "CARDINAL_NUMBER_DOMAIN_CHECK" }, new String[] { "CHARACTER_MAXIMUM_LENGTH  >=  0" } }, new Object[] { new String[] { "CARDINAL_NUMBER_DOMAIN_CHECK" }, new String[] { "CHARACTER_OCTET_LENGTH  >=  0" } }, new Object[] { new String[] { "CARDINAL_NUMBER_DOMAIN_CHECK" }, new String[] { "NUMERIC_PRECISION  >=  0" } }, new Object[] { new String[] { "CARDINAL_NUMBER_DOMAIN_CHECK" }, new String[] { "NUMERIC_PRECISION_RADIX  >=  0" } }, new Object[] { new String[] { "CARDINAL_NUMBER_DOMAIN_CHECK" }, new String[] { "NUMERIC_SCALE  >=  0" } }, new Object[] { new String[] { "CARDINAL_NUMBER_DOMAIN_CHECK" }, new String[] { "DATETIME_PRECISION  >=  0" } }, new Object[] { new String[] { "CARDINAL_NUMBER_DOMAIN_CHECK" }, new String[] { "INTERVAL_PRECISION  >=  0" } }, new Object[] { new String[] { "CARDINAL_NUMBER_DOMAIN_CHECK" }, new String[] { "MAXIMUM_CARDINALITY  >=  0" } } };
        } else if (tableName.equalsIgnoreCase("KEY_COLUMN_USAGE")) {
            return new Object[] { new Object[] { new String[] { "CARDINAL_NUMBER_DOMAIN_CHECK" }, new String[] { "ORDINAL_POSITION  >=  0" } } };
        } else if (tableName.equalsIgnoreCase("TRIGGERS")) {
            return new Object[] { new Object[] { new String[] { "CARDINAL_NUMBER_DOMAIN_CHECK" }, new String[] { "ACTION_ORDER  >=  0" } } };
        } else if (tableName.equalsIgnoreCase("ROUTINES")) {
            return new Object[] { new Object[] { new String[] { "CARDINAL_NUMBER_DOMAIN_CHECK" }, new String[] { "MAX_DYNAMIC_RESULT_SETS  >=  0" } } };
        } else if (tableName.equalsIgnoreCase("PARAMETERS")) {
            return new Object[] { new Object[] { new String[] { "CARDINAL_NUMBER_DOMAIN_CHECK" }, new String[] { "ORDINAL_POSITION  >=  0" } } };
        } else if (tableName.equalsIgnoreCase("INDEXINFO")) {
            return new Object[] { new Object[] { new String[] { "CARDINAL_NUMBER_DOMAIN_CHECK" }, new String[] { "NumberOfRecords  >=  0" }, new Object[] { new String[] { "CARDINAL_NUMBER_DOMAIN_CHECK" }, new String[] { "RootClusterSize  >=  0" } }, new Object[] { new String[] { "CARDINAL_NUMBER_DOMAIN_CHECK" }, new String[] { "RootRecordNumber  >=  0" } } } };
        } else if (tableName.equalsIgnoreCase("INDEXCOLUMNS")) {
            return new Object[] { new Object[] { new String[] { "CARDINAL_NUMBER_DOMAIN_CHECK" }, new String[] { "ORDINAL_POSITION  >=  0" } } };
        } else if (tableName.equalsIgnoreCase("FULLTEXTCOLUMNINFO")) {
            return new Object[] { new Object[] { new String[] { "CARDINAL_NUMBER_DOMAIN_CHECK" }, new String[] { "ORDINAL_POSITION  >=  0" } } };
        } else if (tableName.equalsIgnoreCase("Method_Specifications_Paramters")) {
            return new Object[] { new Object[] { new String[] { "CARDINAL_NUMBER_DOMAIN_CHECK" }, new String[] { "ORDINAL_POSITION  >=  0" } } };
        }
        return null;
    }

    private static Object[] getCheckConstraintInformation(String tableName) throws DException {
        if (tableName.equalsIgnoreCase("USERS")) {
            return new Object[] { new String[] { "USERS_CHECK" }, new String[] { "USER_NAME NOT IN (SELECT ROLE_NAME FROM system.Definition_Schema.Roles)" }, new int[] { 5 } };
        } else if (tableName.equalsIgnoreCase("ROLES")) {
            return new Object[] { new String[] { "ROLES_CHECK" }, new String[] { "ROLE_NAME NOT IN(SELECT USER_NAME FROM system.Definition_Schema.Users)" }, new int[] { 0 } };
        } else if (tableName.equalsIgnoreCase("ROLE_AUTHORIZATION_DESCRIPTORS")) {
            return new Object[] { new String[] { "ROLE_AUTHORIZATION_DESCRIPTORS_GRANTEE_CHECK", "ROLE_AUTHORIZATION_DESCRIPTORS_GRANTOR_CHECK", "ROLE_AUTHORIZATION_DESCRIPTORS_IS_GRANTABLE_CHECK" }, new String[] { "GRANTEE IN (SELECT ROLE_NAME  FROM system.Definition_Schema.Roles) OR GRANTEE IN (SELECT USER_NAME FROM system.Definition_Schema.Users) ", "GRANTOR IN (SELECT ROLE_NAME FROM system.Definition_Schema.Roles) OR GRANTOR IN ('_SYSTEM') OR GRANTOR IN (SELECT USER_NAME FROM system.Definition_Schema.Users)", "IS_GRANTABLE IN ('YES', 'NO')" }, new int[] { 1, 2, 3 } };
        } else if (tableName.equalsIgnoreCase("SCHEMATA")) {
            return new Object[] { new String[] { "SCHEMA_OWNER_NOT_NULL", "DEFAULT_CHARACTER_SET_CATALOG_NOT_NULL", "DEFAULT_CHARACTER_SET_SCHEMA_NOT_NULL", "DEFAULT_CHARACTER_SET_NAME_NOT_NULL" }, new String[] { "\"SCHEMA_OWNER\" is not null", "\"DEFAULT_CHARACTER_SET_CATALOG\" is not null", "\"DEFAULT_CHARACTER_SET_SCHEMA\" is not null", "\"DEFAULT_CHARACTER_SET_NAME\" is not null" }, new int[] { 2, 3, 4, 5 } };
        } else if (tableName.equalsIgnoreCase("CHARACTER_SETS")) {
            return new Object[] { new String[] { "CHARACTER_SETS_DEFAULT_COLLATE_CATALOG_NOT_NULL", "CHARACTER_SETS_DEFAULT_COLLATE_SCHEMA_NOT_NULL", "CHARACTER_SETS_DEFAULT_COLLATE_NAME_NOT_NULL", "CHARACTER_SETS_CHECK_REFERENCES_COLLATIONS" }, new String[] { "\"DEFAULT_COLLATE_CATALOG\" is not null", "\"DEFAULT_COLLATE_SCHEMA\" is not null", "\"DEFAULT_COLLATE_NAME\" is not null", "DEFAULT_COLLATE_CATALOG  NOT IN   (    SELECT          CATALOG_NAME     FROM   system .  Definition_Schema .  Schemata       )  OR        (        DEFAULT_COLLATE_CATALOG ,      DEFAULT_COLLATE_SCHEMA ,      DEFAULT_COLLATE_NAME   )  IN   (    SELECT          COLLATION_CATALOG  ,      COLLATION_SCHEMA  ,      COLLATION_NAME     FROM   system .  Definition_Schema .  Collations )" }, new int[] { 5, 6, 7 } };
        } else if (tableName.equalsIgnoreCase("COLLATIONS")) {
            return new Object[] { new String[] { "COLLATIONS_CHARACTER_SET_CATALOG_NOT_NULL", "COLLATIONS_CHARACTER_SET_SCHEMA_NOT_NULL", "COLLATIONS_CHARACTER_SET_NAME_NOT_NULL", "COLLATIONS_PAD_ATTRIBUTE_CHECK", "COLLATIONS_CHECK_REFERENCES_CHARACTER_SETS" }, new String[] { "\"CHARACTER_SET_CATALOG\" is not null", "\"CHARACTER_SET_SCHEMA\" is not null", "\"CHARACTER_SET_NAME\" is not null", "PAD_ATTRIBUTE   IN  (   'NO PAD', 'PAD SPACE' )", "CHARACTER_SET_CATALOG  NOT IN   (    SELECT          CATALOG_NAME     FROM   system .  Definition_Schema .  Schemata       )  OR        (        CHARACTER_SET_CATALOG ,      CHARACTER_SET_SCHEMA ,      CHARACTER_SET_NAME   )  IN   (    SELECT          CHARACTER_SET_CATALOG  ,      CHARACTER_SET_SCHEMA  ,      CHARACTER_SET_NAME     FROM   system .  Definition_Schema .  Character_Sets       )" }, new int[] { 3, 4, 5, 6 } };
        } else if (tableName.equalsIgnoreCase("DOMAINS")) {
            return new Object[] { new String[] { "DOMAIN_CHECK_DATA_TYPE" }, new String[] { "DOMAIN_CATALOG  NOT IN   (SELECT          CATALOG_NAME     FROM   system .  Definition_Schema .  Schemata       )  OR        (        DOMAIN_CATALOG ,      DOMAIN_SCHEMA ,      DOMAIN_NAME ,  'DOMAIN',      DTD_IDENTIFIER   )  IN   (    SELECT          OBJECT_CATALOG  ,      OBJECT_SCHEMA  ,      OBJECT_NAME, OBJECT_TYPE, DTD_IDENTIFIER FROM system.Definition_Schema.Data_Type_Descriptor)" }, new int[] { 0 } };
        } else if (tableName.equalsIgnoreCase("COLUMNS")) {
            return new Object[] { new String[] { "COLUMNS_ORDINAL_POSITION_NOT_NULL", "COLUMNS_ORDINAL_POSITION_GREATER_THAN_ZERO_CHECK", "COLUMNS_IS_NULLABLE_NOT_NULL", "COLUMNS_IS_NULLABLE_CHECK", "COLUMNS_IS_SELF_REFERENCING_NOT_NULL", "COLUMNS_IS_SELF_REFERENCING_CHECK", "COLUMNS_IS_AUTOINCREMENT_NOT_NULL", "COLUMNS_IS_AUTOINCREMENT_CHECK", "COLUMNS_CHECK_REFERENCES_DOMAIN" }, new String[] { "\"ORDINAL_POSITION\" is not null", "ORDINAL_POSITION   >  0", "\"IS_NULLABLE\" is not null", "IS_NULLABLE   IN  (   'YES', 'NO' )", "\"IS_SELF_REFERENCING\" is not null", "IS_SELF_REFERENCING   IN  (   'YES', 'NO' )", "\"IS_AUTOINCREMENT\" is not null", "IS_AUTOINCREMENT   IN  (   'YES', 'NO' )", "DOMAIN_CATALOG  NOT IN (SELECT CATALOG_NAME FROM system.Definition_Schema.Schemata) OR (DOMAIN_CATALOG , DOMAIN_SCHEMA , DOMAIN_NAME) IN (SELECT DOMAIN_CATALOG, DOMAIN_SCHEMA, DOMAIN_NAME FROM system.Definition_Schema.Domains)" }, new int[] { 4, 0, 2, 10, 11, 12 } };
        } else if (tableName.equalsIgnoreCase("DATA_TYPE_DESCRIPTOR")) {
            return new Object[] { new String[] { "DATA_TYPE_DESCRIPTOR_OBJECT_DATA_TYPE_NOT_NULL", "DATA_TYPE_DESCRIPTOR_CHECK_OBJECT_TYPE", "DATA_TYPE_DESCRIPTOR_DATA_TYPE_CHECK_COMBINATIONS", "DATA_TYPE_DESCRIPTOR_CHECK_REFERENCES_UDT", "DATA_TYPE_DESCRIPTOR_CHECK_REFERENCES_COLLATION" }, new String[] { "\"DATA_TYPE\" is not null", "OBJECT_TYPE   IN  (   'TABLE', 'DOMAIN', 'USER-DEFINED TYPE', 'ROUTINE' )", "(                DATA_TYPE   IN  (   'CHAR', 'VARCHAR', 'CHARACTER', 'CHAR VARYING', 'CHARACTER VARYING', 'CHARACTER LARGE OBJECT', 'CHAR LARGE OBJECT', 'CLOB', 'LONG VARCHAR', 'TABLEKEY' )  AND        (        CHARACTER_MAXIMUM_LENGTH ,      CHARACTER_OCTET_LENGTH   ) IS NOT NULL  AND        (        NUMERIC_PRECISION ,      NUMERIC_PRECISION_RADIX ,      NUMERIC_SCALE ,      DATETIME_PRECISION ,      USER_DEFINED_TYPE_CATALOG ,      USER_DEFINED_TYPE_SCHEMA ,      USER_DEFINED_TYPE_NAME   ) IS  NULL  AND        (        INTERVAL_TYPE ,      INTERVAL_PRECISION   ) IS  NULL  AND        (        SCOPE_CATALOG ,      SCOPE_SCHEMA ,      SCOPE_NAME   ) IS  NULL  AND           MAXIMUM_CARDINALITY  IS  NULL  )  OR     (                 DATA_TYPE   IN  (   'BIT', 'BIT VARYING', 'BINARY', 'VARBINARY' )  AND   (        CHARACTER_MAXIMUM_LENGTH ,      CHARACTER_OCTET_LENGTH   ) IS NOT NULL  AND        (        COLLATION_CATALOG ,      COLLATION_SCHEMA ,      COLLATION_NAME   ) IS  NULL  AND        (        NUMERIC_PRECISION ,      NUMERIC_PRECISION_RADIX ,      NUMERIC_SCALE ,      DATETIME_PRECISION ,      USER_DEFINED_TYPE_CATALOG ,      USER_DEFINED_TYPE_SCHEMA ,      USER_DEFINED_TYPE_NAME   ) IS  NULL  AND        (        INTERVAL_TYPE ,      INTERVAL_PRECISION   ) IS  NULL  AND        (        SCOPE_CATALOG ,      SCOPE_SCHEMA ,      SCOPE_NAME   ) IS  NULL  AND           MAXIMUM_CARDINALITY  IS  NULL  )  OR     (                DATA_TYPE   IN  (   'BINARY LARGE OBJECT', 'BLOB', 'LONG VARBINARY' )  AND        (        CHARACTER_MAXIMUM_LENGTH ,      CHARACTER_OCTET_LENGTH   ) IS NOT NULL  AND        (        COLLATION_CATALOG ,      COLLATION_SCHEMA ,      COLLATION_NAME ,      NUMERIC_PRECISION ,      NUMERIC_PRECISION_RADIX ,               NUMERIC_SCALE ,      DATETIME_PRECISION ,      USER_DEFINED_TYPE_CATALOG ,      USER_DEFINED_TYPE_SCHEMA ,      USER_DEFINED_TYPE_NAME   ) IS  NULL  AND        (        INTERVAL_TYPE ,      INTERVAL_PRECISION   ) IS  NULL  AND        (        SCOPE_CATALOG ,      SCOPE_SCHEMA ,      SCOPE_NAME   ) IS  NULL  AND           MAXIMUM_CARDINALITY  IS  NULL  )  OR     (                   DATA_TYPE   IN  (   'INTEGER', 'SMALLINT', 'BYTE', 'INT', 'TINYINT', 'LONG', 'BIGINT' )  AND        (        CHARACTER_MAXIMUM_LENGTH ,      CHARACTER_OCTET_LENGTH ,      COLLATION_CATALOG ,      COLLATION_SCHEMA ,      COLLATION_NAME   ) IS  NULL  AND           NUMERIC_PRECISION_RADIX   IN  (   2, 10 )  AND           NUMERIC_PRECISION  IS NOT NULL  AND           NUMERIC_SCALE   =  0  AND           DATETIME_PRECISION  IS  NULL  AND        (        INTERVAL_TYPE ,      INTERVAL_PRECISION   ) IS  NULL  AND        (        SCOPE_CATALOG ,      SCOPE_SCHEMA ,      SCOPE_NAME   ) IS  NULL  AND               MAXIMUM_CARDINALITY  IS  NULL  )  OR     (                  DATA_TYPE   IN  (   'NUMERIC', 'DECIMAL', 'DEC' )  AND        (        CHARACTER_MAXIMUM_LENGTH ,      CHARACTER_OCTET_LENGTH ,      COLLATION_CATALOG ,      COLLATION_SCHEMA ,      COLLATION_NAME   ) IS  NULL  AND           NUMERIC_PRECISION_RADIX   =  10  AND        (        NUMERIC_PRECISION ,      NUMERIC_SCALE   ) IS NOT NULL  AND           DATETIME_PRECISION  IS  NULL  AND        (        INTERVAL_TYPE ,      INTERVAL_PRECISION   ) IS  NULL  AND        (        SCOPE_CATALOG ,      SCOPE_SCHEMA ,      SCOPE_NAME   ) IS  NULL  AND           MAXIMUM_CARDINALITY  IS  NULL  )  OR     (                    DATA_TYPE   IN  (   'REAL', 'DOUBLE PRECISION', 'FLOAT' )  AND        (        CHARACTER_MAXIMUM_LENGTH ,      CHARACTER_OCTET_LENGTH ,      COLLATION_CATALOG ,      COLLATION_SCHEMA ,      COLLATION_NAME   ) IS  NULL  AND           NUMERIC_PRECISION  IS NOT NULL  AND           NUMERIC_PRECISION_RADIX   =  2  AND                NUMERIC_SCALE  IS  NULL  AND           DATETIME_PRECISION  IS  NULL  AND        (        USER_DEFINED_TYPE_CATALOG ,      USER_DEFINED_TYPE_SCHEMA ,      USER_DEFINED_TYPE_NAME   ) IS  NULL  AND        (        INTERVAL_TYPE ,      INTERVAL_PRECISION   ) IS  NULL  AND        (        SCOPE_CATALOG ,      SCOPE_SCHEMA ,      SCOPE_NAME   ) IS  NULL  AND           MAXIMUM_CARDINALITY  IS  NULL  )  OR     (                   DATA_TYPE   IN  (   'DATE', 'TIME', 'TIMESTAMP', 'TIME WITH TIME ZONE', 'TIMESTAMP WITH TIME ZONE' )  AND        (        CHARACTER_MAXIMUM_LENGTH ,      CHARACTER_OCTET_LENGTH ,      COLLATION_CATALOG ,      COLLATION_SCHEMA ,      COLLATION_NAME   ) IS  NULL  AND        (        NUMERIC_PRECISION ,      NUMERIC_PRECISION_RADIX   ) IS  NULL  AND           NUMERIC_SCALE  IS  NULL  AND           DATETIME_PRECISION  IS NOT NULL  AND        (        USER_DEFINED_TYPE_CATALOG ,      USER_DEFINED_TYPE_SCHEMA ,      USER_DEFINED_TYPE_NAME   ) IS  NULL  AND        (         INTERVAL_TYPE ,      INTERVAL_PRECISION   ) IS  NULL  AND        (        SCOPE_CATALOG ,      SCOPE_SCHEMA ,      SCOPE_NAME   ) IS  NULL  AND           MAXIMUM_CARDINALITY  IS  NULL  )  OR     (                    DATA_TYPE   =  'INTERVAL'  AND        (        CHARACTER_MAXIMUM_LENGTH ,      CHARACTER_OCTET_LENGTH ,      COLLATION_CATALOG ,      COLLATION_SCHEMA ,      COLLATION_NAME   ) IS  NULL  AND        (        NUMERIC_PRECISION ,      NUMERIC_PRECISION_RADIX   ) IS NOT NULL  AND           NUMERIC_SCALE  IS  NULL  AND           DATETIME_PRECISION  IS NOT NULL  AND        (        USER_DEFINED_TYPE_CATALOG ,      USER_DEFINED_TYPE_SCHEMA ,      USER_DEFINED_TYPE_NAME   ) IS  NULL  AND           INTERVAL_TYPE   IN  (   'YEAR', 'MONTH', 'DAY', 'HOUR', 'MINUTE', 'SECOND', 'YEAR TO MONTH', 'DAY TO HOUR', 'DAY TO MINUTE', 'DAY TO SECOND', 'HOUR TO MINUTE', 'HOUR TO SECOND', 'MINUTE TO SECOND' )  AND           INTERVAL_PRECISION  IS NOT NULL  AND        (        SCOPE_CATALOG ,      SCOPE_SCHEMA ,              SCOPE_NAME   ) IS  NULL  AND           MAXIMUM_CARDINALITY  IS  NULL  )  OR     (                   DATA_TYPE   =  'BOOLEAN'  AND        (        CHARACTER_MAXIMUM_LENGTH ,      CHARACTER_OCTET_LENGTH ,      COLLATION_CATALOG ,      COLLATION_SCHEMA ,      COLLATION_NAME   ) IS  NULL  AND        (        NUMERIC_PRECISION ,      NUMERIC_PRECISION_RADIX   ) IS  NULL  AND           NUMERIC_SCALE  IS  NULL  AND           DATETIME_PRECISION  IS  NULL  AND        (        INTERVAL_TYPE ,      INTERVAL_PRECISION   ) IS  NULL  AND        (        USER_DEFINED_TYPE_CATALOG ,      USER_DEFINED_TYPE_SCHEMA ,      USER_DEFINED_TYPE_NAME   ) IS  NULL  AND        (        SCOPE_CATALOG ,      SCOPE_SCHEMA ,      SCOPE_NAME   ) IS  NULL  AND           MAXIMUM_CARDINALITY  IS  NULL  )  OR     (              DATA_TYPE   =  'USER-DEFINED'  AND        (        NUMERIC_PRECISION ,      NUMERIC_PRECISION_RADIX ,      NUMERIC_SCALE ,      DATETIME_PRECISION ,      CHARACTER_OCTET_LENGTH ,                   CHARACTER_MAXIMUM_LENGTH ,      INTERVAL_TYPE ,      INTERVAL_PRECISION ,      SCOPE_CATALOG ,      SCOPE_SCHEMA ,      SCOPE_NAME   ) IS  NULL  AND        (        USER_DEFINED_TYPE_CATALOG ,      USER_DEFINED_TYPE_SCHEMA ,      USER_DEFINED_TYPE_NAME   ) IS NOT NULL  AND           MAXIMUM_CARDINALITY  IS  NULL  )  OR     (              DATA_TYPE   =  'REF'  AND        (        NUMERIC_PRECISION ,      NUMERIC_PRECISION_RADIX ,      NUMERIC_SCALE ,      DATETIME_PRECISION ,      CHARACTER_OCTET_LENGTH ,      CHARACTER_MAXIMUM_LENGTH ,      INTERVAL_TYPE ,      INTERVAL_PRECISION   ) IS  NULL  AND        (        USER_DEFINED_TYPE_CATALOG ,      USER_DEFINED_TYPE_SCHEMA ,      USER_DEFINED_TYPE_NAME   ) IS NOT NULL  AND           MAXIMUM_CARDINALITY  IS  NULL  )  OR     (               DATA_TYPE   =  'ARRAY'  AND        (        NUMERIC_PRECISION ,      NUMERIC_PRECISION_RADIX ,      NUMERIC_SCALE ,      DATETIME_PRECISION ,      CHARACTER_OCTET_LENGTH ,      CHARACTER_MAXIMUM_LENGTH ,               INTERVAL_TYPE ,      INTERVAL_PRECISION   ) IS  NULL  AND        (        USER_DEFINED_TYPE_CATALOG ,                 USER_DEFINED_TYPE_SCHEMA ,      USER_DEFINED_TYPE_NAME   ) IS  NULL  AND        (        SCOPE_CATALOG ,      SCOPE_SCHEMA ,      SCOPE_NAME   ) IS  NULL  AND           MAXIMUM_CARDINALITY  IS NOT NULL  )  OR     (               DATA_TYPE   =  'ROW'  AND        (        NUMERIC_PRECISION ,      NUMERIC_PRECISION_RADIX ,      NUMERIC_SCALE ,      DATETIME_PRECISION ,      CHARACTER_OCTET_LENGTH ,      CHARACTER_MAXIMUM_LENGTH ,      INTERVAL_TYPE ,      INTERVAL_PRECISION   ) IS  NULL  AND        (        USER_DEFINED_TYPE_CATALOG ,      USER_DEFINED_TYPE_SCHEMA ,      USER_DEFINED_TYPE_NAME   ) IS  NULL  AND        (        SCOPE_CATALOG ,      SCOPE_SCHEMA ,      SCOPE_NAME   ) IS  NULL  AND           MAXIMUM_CARDINALITY  IS  NULL  )  OR     (           DATA_TYPE  NOT IN  (   'CHAR', 'CHARACTER', 'VARCHAR', 'CHARACTER VARYING', 'CHARACTER LARGE OBJECT', 'CHAR LARGE OBJECT', 'CLOB', 'LONG VARCHAR', 'BINARY LARGE OBJECT', 'BLOB', 'LONG VARBINARY', 'BIT', 'BIT VARYING',              'BINARY', 'VARBINARY', 'INT', 'INTEGER', 'SMALLINT', 'TINYINT', 'BYTE', 'LONG', 'BIGINT', 'NUMERIC', 'DECIMAL', 'DEC', 'REAL', 'DOUBLE PRECISION', 'FLOAT', 'DATE', 'TIME', 'TIMESTAMP', 'INTERVAL', 'BOOLEAN', 'USER-DEFINED', 'REF', 'ARRAY', 'ROW' )  )", "USER_DEFINED_TYPE_CATALOG   <> ANY   (    SELECT          CATALOG_NAME     FROM   system .  Definition_Schema .  Schemata       )  OR        (        USER_DEFINED_TYPE_CATALOG ,      USER_DEFINED_TYPE_SCHEMA ,      USER_DEFINED_TYPE_NAME   )  IN   (    SELECT          USER_DEFINED_TYPE_CATALOG  ,      USER_DEFINED_TYPE_SCHEMA  ,      USER_DEFINED_TYPE_NAME     FROM   system .  Definition_Schema .  User_Defined_Types       )", "COLLATION_CATALOG   <> ANY   (    SELECT          CATALOG_NAME     FROM   system .  Definition_Schema .  Schemata       )  OR        (        COLLATION_CATALOG ,      COLLATION_SCHEMA ,      COLLATION_NAME   )  IN   (    SELECT          COLLATION_CATALOG  ,      COLLATION_SCHEMA  ,      COLLATION_NAME     FROM   system .  Definition_Schema .  Collations       )" }, new int[] { 5, 3, 6, 7, 11, 12, 13, 14, 17, 18, 19, 15, 16, 20, 21, 22, 23, 8, 9, 10 } };
        } else if (tableName.equalsIgnoreCase("TABLE_CONSTRAINTS")) {
            return new Object[] { new String[] { "CONSTRAINT_TYPE_NOT_NULL", "CONSTRAINT_TYPE_CHECK", "TABLE_CONSTRAINTS_TABLE_CATALOG_NOT_NULL", "TABLE_CONSTRAINTS_TABLE_SCHEMA_NOT_NULL", "TABLE_CONSTRAINTS_TABLE_NAME_NOT_NULL", "TABLE_CONSTRAINTS_IS_DEFERRABLE_NOT_NULL", "TABLE_CONSTRAINTS_INITIALLY_DEFERRED_NOT_NULL", "TABLE_CONSTRAINTS_DEFERRED_CHECK", "TABLE_CONSTRAINTS_CHECK_VIEWS" }, new String[] { "\"CONSTRAINT_TYPE\"is not null", "CONSTRAINT_TYPE IN('UNIQUE','PRIMARY KEY','FOREIGN KEY','CHECK' )", "\"TABLE_CATALOG\"is not null", "\"TABLE_SCHEMA\"is not null", "\"TABLE_NAME\"is not null", "\"IS_DEFERRABLE\"is not null", "\"INITIALLY_DEFERRED\"is not null", "(IS_DEFERRABLE, INITIALLY_DEFERRED) IN( ('NO','NO' ), ('YES','NO' ), ('YES','YES' ))", "TABLE_CATALOG NOT IN(SELECT CATALOG_NAME FROM system.Definition_Schema.Schemata) OR( (TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME) IN(SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME FROM system.Definition_Schema.Tables WHERE TABLE_TYPE <> 'VIEW' ))" }, new int[] { 3, 4, 5, 6, 7, 8 } };
        } else if (tableName.equalsIgnoreCase("KEY_COLUMN_USAGE")) {
            return new Object[] { new String[] { "KEY_COLUMN_TABLE_CATALOG_NOT_NULL", "KEY_COLUMN_TABLE_SCHEMA_NOT_NULL", "KEY_COLUMN_TABLE_NAME_NOT_NULL", "KEY_COLUMN_ORDINAL_POSITION_NOT_NULL", "KEY_COLUMN_USAGE_ORDINAL_POSITION_GREATER_THAN_ZERO_CHECK", "KEY_COLUMN_USAGE_ORDINAL_POSITION_CONTIGUOUS_CHECK", "KEY_COLUMN_CONSTRAINT_TYPE_CHECK" }, new String[] { "\"TABLE_CATALOG\"is not null", "\"TABLE_SCHEMA\"is not null", "\"TABLE_NAME\"is not null", "\"ORDINAL_POSITION\"is not null", "ORDINAL_POSITION > 0", "0 = ALL(SELECT MAX(ORDINAL_POSITION) - COUNT( * ) FROM system.Definition_Schema.Key_Column_Usage GROUP BY CONSTRAINT_CATALOG, CONSTRAINT_SCHEMA, CONSTRAINT_NAME)", "(CONSTRAINT_CATALOG, CONSTRAINT_SCHEMA, CONSTRAINT_NAME) IN(SELECT CONSTRAINT_CATALOG, CONSTRAINT_SCHEMA, CONSTRAINT_NAME FROM system.Definition_Schema.Table_Constraints WHERE CONSTRAINT_TYPE IN('UNIQUE','PRIMARY KEY','FOREIGN KEY' ))" }, new int[] { 3, 4, 5, 7, 0, 1, 2 } };
        } else if (tableName.equalsIgnoreCase("REFERENTIAL_CONSTRAINTS")) {
            return new Object[] { new String[] { "UNIQUE_CONSTRAINT_CATALOG_NOT_NULL", "UNIQUE_CONSTRAINT_SCHEMA_NOT_NULL", "UNIQUE_CONSTRAINT_NAME_NOT_NULL", "REFERENTIAL_MATCH_OPTION_NOT_NULL", "REFERENTIAL_MATCH_OPTION_CHECK", "REFERENTIAL_UPDATE_RULE_NOT_NULL", "REFERENTIAL_UPDATE_RULE_CHECK", "REFERENTIAL_DELETE_RULE_NOT_NULL", "REFERENTIAL_DELETE_RULE_CHECK", "REFERENTIAL_CONSTRAINTS_CONSTRAINT_TYPE_CHECK", "UNIQUE_CONSTRAINT_CHECK_REFERENCES_UNIQUE_CONSTRAINT" }, new String[] { "\"UNIQUE_CONSTRAINT_CATALOG\"is not null", "\"UNIQUE_CONSTRAINT_SCHEMA\"is not null", "\"UNIQUE_CONSTRAINT_NAME\"is not null", "\"MATCH_OPTION\"is not null", "MATCH_OPTION IN('SIMPLE','PARTIAL','FULL' )", "\"UPDATE_RULE\"is not null", "UPDATE_RULE IN('CASCADE','SET NULL','SET DEFAULT','RESTRICT','NO ACTION' )", "\"DELETE_RULE\"is not null", "DELETE_RULE IN('CASCADE','SET NULL','SET DEFAULT','RESTRICT','NO ACTION' )", "(CONSTRAINT_CATALOG, CONSTRAINT_SCHEMA, CONSTRAINT_NAME) IN(SELECT CONSTRAINT_CATALOG, CONSTRAINT_SCHEMA, CONSTRAINT_NAME FROM system.Definition_Schema.Table_Constraints WHERE CONSTRAINT_TYPE = 'FOREIGN KEY' )", "UNIQUE_CONSTRAINT_CATALOG NOT IN(SELECT CATALOG_NAME FROM system.Definition_Schema.Schemata) OR( (UNIQUE_CONSTRAINT_CATALOG, UNIQUE_CONSTRAINT_SCHEMA, UNIQUE_CONSTRAINT_NAME) IN(SELECT CONSTRAINT_CATALOG, CONSTRAINT_SCHEMA, CONSTRAINT_NAME FROM system.Definition_Schema.Table_Constraints WHERE CONSTRAINT_TYPE IN('UNIQUE','PRIMARY KEY' )))" }, new int[] { 3, 4, 5, 6, 7, 8, 0, 1, 2 } };
        } else if (tableName.equalsIgnoreCase("CHECK_COLUMN_USAGE")) {
            return new Object[] { new String[] { "CHECK_COLUMN_USAGE_CHECK_REFERENCES_COLUMNS" }, new String[] { "TABLE_CATALOG NOT IN(SELECT CATALOG_NAME FROM system.Definition_Schema.Schemata) OR(TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME) IN(SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME FROM system.Definition_Schema.Columns)" }, new int[] { 3, 4, 5, 6 } };
        } else if (tableName.equalsIgnoreCase("CHECK_TABLE_USAGE")) {
            return new Object[] { new String[] { "CHECK_TABLE_USAGE_CHECK_REFERENCES_TABLES" }, new String[] { "TABLE_CATALOG NOT IN(SELECT CATALOG_NAME FROM system.Definition_Schema.Schemata) OR(TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME) IN(SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME FROM system.Definition_Schema.Tables)" }, new int[] { 3, 4, 5 } };
        } else if (tableName.equalsIgnoreCase("TRIGGERS")) {
            return new Object[] { new String[] { "TRIGGERS_EVENT_MANIPULATION_CHECK", "TRIGGERS_EVENT_OBJECT_CATALOG_NOT_NULL", "TRIGGERS_EVENT_OBJECT_SCHEMA_NOT_NULL", "TRIGGERS_EVENT_OBJECT_TABLE_NOT_NULL", "TRIGGERS_ACTION_ORDER_NOT_NULL", "TRIGGERS_ACTION_STATEMENT_NOT_NULL", "TRIGGERS_ACTION_ORIENTATION_CHECK", "TRIGGERS_CONDITION_TIMING_CHECK", "TRIGGERS_REFERENCES_TABLES" }, new String[] { "EVENT_MANIPULATION IN('INSERT','DELETE','UPDATE' )", "\"EVENT_OBJECT_CATALOG\"is not null", "\"EVENT_OBJECT_SCHEMA\"is not null", "\"EVENT_OBJECT_TABLE\"is not null", "\"ACTION_ORDER\"is not null", "\"ACTION_STATEMENT\"is not null", "ACTION_ORIENTATION IN('ROW','STATEMENT' )", "CONDITION_TIMING IN('BEFORE','AFTER' )", "EVENT_OBJECT_CATALOG <> ANY(SELECT CATALOG_NAME FROM system.Definition_Schema.Schemata) OR(EVENT_OBJECT_CATALOG, EVENT_OBJECT_SCHEMA, EVENT_OBJECT_TABLE) IN(SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME FROM system.Definition_Schema.Tables)" }, new int[] { 3, 4, 5, 6, 7, 9, 10, 11 } };
        } else if (tableName.equalsIgnoreCase("TRIGGER_TABLE_USAGE")) {
            return new Object[] { new String[] { "TRIGGER_TABLE_USAGE_CHECK_REFERENCES_TABLES" }, new String[] { "TABLE_CATALOG NOT IN(SELECT CATALOG_NAME FROM system.Definition_Schema.Schemata) OR(TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME) IN(SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME FROM system.Definition_Schema.Tables)" }, new int[] { 3, 4, 5 } };
        } else if (tableName.equalsIgnoreCase("TRIGGER_COLUMN_USAGE")) {
            return new Object[] { new String[] { "TRIGGER_COLUMN_USAGE_EVENT_NOT_UPDATE_CHECK" }, new String[] { "(TRIGGER_CATALOG, TRIGGER_SCHEMA, TRIGGER_NAME) IN(SELECT TRIGGER_CATALOG, TRIGGER_SCHEMA, TRIGGER_NAME FROM system.Definition_Schema.Triggers WHERE EVENT_MANIPULATION <> 'UPDATE' )" }, new int[] { 0, 1, 2 } };
        } else if (tableName.equalsIgnoreCase("TRIGGERED_UPDATE_COLUMNS")) {
            return new Object[] { new String[] { "EVENT_OBJECT_CATALOG_NOT_NULL", "EVENT_OBJECT_SCHEMA_NOT_NULL", "EVENT_OBJECT_TABLE_NOT_NULL", "TRIGGERED_UPDATE_COLUMNS_EVENT_MANIPULATION_CHECK" }, new String[] { "\"EVENT_OBJECT_CATALOG\"is not null", "\"EVENT_OBJECT_SCHEMA\"is not null", "\"EVENT_OBJECT_TABLE\"is not null", "(TRIGGER_CATALOG, TRIGGER_SCHEMA, TRIGGER_NAME) IN(SELECT TRIGGER_CATALOG, TRIGGER_SCHEMA, TRIGGER_NAME FROM system.Definition_Schema.Triggers WHERE EVENT_MANIPULATION = 'UPDATE' )" }, new int[] { 3, 4, 5, 0, 1, 2 } };
        } else if (tableName.equalsIgnoreCase("VIEWS")) {
            return new Object[] { new String[] { "CHECK_OPTION_NOT_NULL", "CHECK_OPTION_CHECK", "IS_UPDATABLE_NOT_NULL", "IS_UPDATABLE_CHECK", "IS_INSERTABLE_INTO_NOT_NULL", "IS_INSERTABLE_INTO_CHECK", "VIEWS_IN_TABLES_CHECK", "VIEWS_IS_UPDATABLE_CHECK_OPTION_CHECK" }, new String[] { "\"CHECK_OPTION\"is not null", "CHECK_OPTION IN('CASCADED','LOCAL','NONE' )", "\"IS_UPDATABLE\"is not null", "IS_UPDATABLE IN('YES','NO' )", "\"IS_INSERTABLE_INTO\"is not null", "IS_INSERTABLE_INTO IN('YES','NO' )", "(TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME) IN(SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME FROM system.Definition_Schema.Tables WHERE TABLE_TYPE = 'VIEW' )", "(IS_UPDATABLE, CHECK_OPTION) NOT IN( ('NO','CASCADED' ), ('NO','LOCAL' ))" }, new int[] { 4, 5, 6, 0, 1, 2 } };
        } else if (tableName.equalsIgnoreCase("VIEW_COLUMN_USAGE")) {
            return new Object[] { new String[] { "VIEW_COLUMN_USAGE_CHECK_REFERENCES_COLUMNS" }, new String[] { "TABLE_CATALOG NOT IN(SELECT CATALOG_NAME FROM system.Definition_Schema.Schemata) OR(TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME) IN(SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME FROM system.Definition_Schema.Columns)" }, new int[] { 3, 4, 5, 6 } };
        } else if (tableName.equalsIgnoreCase("VIEW_TABLE_USAGE")) {
            return new Object[] { new String[] { "VIEW_TABLE_USAGE_CHECK_REFERENCES_TABLES" }, new String[] { "TABLE_CATALOG NOT IN(SELECT CATALOG_NAME FROM system.Definition_Schema.Schemata) OR(TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME) IN(SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME FROM system.Definition_Schema.Tables)" }, new int[] { 3, 4, 5 } };
        } else if (tableName.equalsIgnoreCase("DOMAIN_CONSTRAINTS")) {
            return new Object[] { new String[] { "DOMAIN_CATALOG_NOT_NULL", "DOMAIN_SCHEMA_NOT_NULL", "DOMAIN_NAME_NOT_NULL", "DOMAIN_CONSTRAINTS_DEFERRABLE_NOT_NULL", "DOMAIN_CONSTRAINTS_INITIALLY_DEFERRED_NOT_NULL", "DOMAIN_CONSTRAINTS_CHECK_DEFERRABLE" }, new String[] { "\"DOMAIN_CATALOG\"is not null", "\"DOMAIN_SCHEMA\"is not null", "\"DOMAIN_NAME\"is not null", "\"IS_DEFERRABLE\"is not null", "\"INITIALLY_DEFERRED\"is not null", "(IS_DEFERRABLE, INITIALLY_DEFERRED) IN( ('NO','NO' ), ('YES','NO' ), ('YES','YES' ))" }, new int[] { 3, 4, 5, 6, 7 } };
        } else if (tableName.equalsIgnoreCase("COLUMN_PRIVILEGES")) {
            return new Object[] { new String[] { "COLUMN_PRIVILEGE_TYPE_CHECK", "COLUMN_PRIVILEGE_IS_GRANTABLE_NOT_NULL", "COLUMN_PRIVILEGE_IS_GRANTABALE_CHECK", "COLUMN_PRIVILEGE_GRANTOR_CHECK", "COLUMN_PRIVILEGE_GRANTEE_CHECK" }, new String[] { "PRIVILEGE_TYPE IN('SELECT','INSERT','UPDATE','REFERENCES' )", "\"IS_GRANTABLE\"is not null", "IS_GRANTABLE IN('YES','NO' )", "GRANTOR IN(SELECT ROLE_NAME FROM system.Definition_Schema.Roles) OR GRANTOR IN('_SYSTEM' ) OR GRANTOR IN(SELECT USER_NAME FROM system.Definition_Schema.Users)", "GRANTEE IN(SELECT ROLE_NAME FROM system.Definition_Schema.Roles) OR GRANTEE IN(SELECT USER_NAME FROM system.Definition_Schema.Users) OR GRANTEE IN('PUBLIC' )" }, new int[] { 6, 7, 0, 1 } };
        } else if (tableName.equalsIgnoreCase("TABLE_PRIVILEGES")) {
            return new Object[] { new String[] { "TABLE_PRIVILEGE_TYPE_CHECK", "TABLE_PRIVILEGE_GRANTABLE_NOT_NULL", "TABLE_PRIVILEGE_GRANTABLE_CHECK", "TABLE_PRIVILEGE_WITH_HIERARCHY_NOT_NULL", "TABLE_PRIVILEGE_WITH_HIERARCHY_CHECK", "TABLE_PRIVILEGE_GRANTOR_CHECK", "TABLE_PRIVILEGE_GRANTEE_CHECK" }, new String[] { "PRIVILEGE_TYPE IN('SELECT','INSERT','DELETE','UPDATE','TRIGGER','REFERENCES' )", "\"IS_GRANTABLE\"is not null", "IS_GRANTABLE IN('YES','NO' )", "\"WITH_HIERARCHY\"is not null", "WITH_HIERARCHY IN('YES','NO' )", "GRANTOR IN(SELECT ROLE_NAME FROM system.Definition_Schema.Roles) OR GRANTOR IN('_SYSTEM' ) OR GRANTOR IN(SELECT USER_NAME FROM system.Definition_Schema.Users)", "GRANTEE IN(SELECT ROLE_NAME FROM system.Definition_Schema.Roles) OR GRANTEE IN(SELECT USER_NAME FROM system.Definition_Schema.Users) OR GRANTEE IN('PUBLIC' )" }, new int[] { 5, 6, 7, 0, 1 } };
        } else if (tableName.equalsIgnoreCase("USAGE_PRIVILEGES")) {
            return new Object[] { new String[] { "USAGE_PRIVILEGES_OBJECT_TYPE_CHECK", "USAGE_PRIVILEGES_IS_GRANTABLE_NOT_NULL", "USAGE_PRIVILEGES_IS_GRANTABLE_CHECK", "USAGE_PRIVILEGE_GRANTOR_CHECK", "USAGE_PRIVILEGE_GRANTEE_CHECK" }, new String[] { "OBJECT_TYPE IN('DOMAIN','CHARACTER SET','COLLATION','TRANSLATION' )", "\"IS_GRANTABLE\"is not null", "IS_GRANTABLE IN('YES','NO' )", "GRANTOR IN(SELECT ROLE_NAME FROM system.Definition_Schema.Roles) OR GRANTOR IN('_SYSTEM' ) OR GRANTOR IN(SELECT USER_NAME FROM system.Definition_Schema.Users)", "GRANTEE IN(SELECT ROLE_NAME FROM system.Definition_Schema.Roles) OR GRANTEE IN(SELECT USER_NAME FROM system.Definition_Schema.Users)" }, new int[] { 5, 6, 0, 1 } };
        } else if (tableName.equalsIgnoreCase("ROUTINES")) {
            return new Object[] { new String[] { "ROUTINE_TYPE_NOT_NULL", "ROUTINE_TYPE_CHECK", "ROUTINE_BODY_NOT_NULL", "ROUTINE_BODY_CHECK", "EXTERNAL_LANGUAGE_CHECK", "PARAMETER_STYLE_CHECK", "IS_DETERMINISTIC_CHECK", "ROUTINES_SQL_DATA_ACCESS_NOT_NULL", "ROUTINES_SQL_DATA_ACCESS_CHECK", "ROUTINES_IS_USER_DEFINED_CAST_NOT_NULL", "ROUTINES_IS_USER_DEFINED_CAST_CHECK", "ROUTINES_IS_IMPLICITLY_INVOCABLE_CHECK", "ROUTINES_SECURITY_TYPE_CHECK", "ROUTINES_AS_LOCATOR_CHECK", "ROUTINES_IS_NULL_CALL_CHECK", "ROUTINES_COMBINATIONS", "ROUTINES_SAME_SCHEMA", "ROUTINES_CHECK_RESULT_TYPE" }, new String[] { "\"ROUTINE_TYPE\"is not null", "ROUTINE_TYPE IN('PROCEDURE','FUNCTION','INSTANCE METHOD','STATIC METHOD' )", "\"ROUTINE_BODY\"is not null", "ROUTINE_BODY IN('SQL','EXTERNAL' )", "EXTERNAL_LANGUAGE IN('JAVA', 'ADA', 'C', 'COBOL','FORTRAN','MUMPS','PASCAL','PLI' ) OR EXTERNAL_LANGUAGE IS NULL", "PARAMETER_STYLE IN('SQL','GENERAL' ) OR PARAMETER_STYLE IS NULL", "IS_DETERMINISTIC IN('YES','NO' )", "\"SQL_DATA_ACCESS\"is not null", "SQL_DATA_ACCESS IN('NONE','CONTAINS','READS','MODIFIES' )", "\"IS_USER_DEFINED_CAST\"is not null", "IS_USER_DEFINED_CAST IN('YES','NO' )", "IS_IMPLICITLY_INVOCABLE IN('YES','NO' ) OR IS_IMPLICITLY_INVOCABLE IS NULL", "SECURITY_TYPE IN('DEFINER','INVOKER','IMPLEMENTATION DEFINED' )", "AS_LOCATOR IN('YES','NO' )", "IS_NULL_CALL IN('YES','NO' ) OR IS_NULL_CALL IS NULL", "(ROUTINE_BODY = 'SQL' AND(EXTERNAL_NAME, EXTERNAL_LANGUAGE, PARAMETER_STYLE) IS NULL) OR(ROUTINE_BODY = 'EXTERNAL' AND(EXTERNAL_NAME, EXTERNAL_LANGUAGE, PARAMETER_STYLE) IS NOT NULL)", "(SPECIFIC_CATALOG, SPECIFIC_SCHEMA) = (ROUTINE_CATALOG, ROUTINE_SCHEMA) OR(SPECIFIC_CATALOG, SPECIFIC_SCHEMA) = (MODULE_CATALOG, MODULE_SCHEMA) OR(SPECIFIC_CATALOG, SPECIFIC_SCHEMA) = (USER_DEFINED_TYPE_CATALOG, USER_DEFINED_TYPE_SCHEMA)", "(ROUTINE_TYPE = 'PROCEDURE' AND DTD_IDENTIFIER IS NULL) OR(ROUTINE_TYPE <> 'PROCEDURE' AND(SPECIFIC_CATALOG, SPECIFIC_SCHEMA, SPECIFIC_NAME, 'ROUTINE',DTD_IDENTIFIER) IN(SELECT OBJECT_CATALOG, OBJECT_SCHEMA, OBJECT_NAME, OBJECT_TYPE, DTD_IDENTIFIER FROM system.Definition_Schema.Data_Type_Descriptor))" }, new int[] { 12, 14, 17, 18, 19, 20, 25, 26, 27, 31, 21, 16, 0, 1, 3, 4, 6, 7, 9, 10, 13, 2 } };
        } else if (tableName.equalsIgnoreCase("PARAMETERS")) {
            return new Object[] { new String[] { "PARAMETERS_POSITION_NOT_NULL", "PARAMETERS_ORDINAL_POSITION_GREATER_THAN_ZERO_CHECK", "PARAMETERS_ORDINAL_POSITION_CONTIGUOUS_CHECK", "PARAMETER_MODE_NOT_NULL", "PARAMETER_MODE_CHECK", "PARAMETERS_IS_RESULT_CHECK", "PARAMETERS_AS_LOCATOR_CHECK", "PARAMETERS_CHECK_DATA_TYPE" }, new String[] { "\"ORDINAL_POSITION\"is not null", "ORDINAL_POSITION > 0", "0 = ALL(SELECT MAX(ORDINAL_POSITION) - COUNT( * ) FROM system.Definition_Schema.Parameters GROUP BY SPECIFIC_CATALOG, SPECIFIC_SCHEMA, SPECIFIC_NAME)", "\"PARAMETER_MODE\"is not null", "PARAMETER_MODE IN('IN','OUT','INOUT' )", "IS_RESULT IN('YES','NO' )", "AS_LOCATOR IN('YES','NO' )", "(SPECIFIC_CATALOG, SPECIFIC_SCHEMA, SPECIFIC_NAME, 'ROUTINE',DTD_IDENTIFIER) IN(SELECT OBJECT_CATALOG, OBJECT_SCHEMA, OBJECT_NAME, OBJECT_TYPE, DTD_IDENTIFIER FROM system.Definition_Schema.Data_Type_Descriptor)" }, new int[] { 3, 0, 1, 2, 5, 6, 7, 4 } };
        } else if (tableName.equalsIgnoreCase("ROUTINE_PRIVILEGES")) {
            return new Object[] { new String[] { "ROUTINE_PRIVILEGES_TYPE_CHECK", "ROUTINE_PRIVILEGES_GRANTABLE_NOT_NULL", "ROUTINE_PRIVILEGES_GRANTABLE_CHECK", "ROUTINE_PRIVILEGE_GRANTOR_CHECK", "ROUTINE_PRIVILEGE_GRANTEE_CHECK" }, new String[] { "PRIVILEGE_TYPE IN('EXECUTE' )", "\"IS_GRANTABLE\"is not null", "IS_GRANTABLE IN('YES','NO' )", "GRANTOR IN(SELECT ROLE_NAME FROM system.Definition_Schema.Roles) OR GRANTOR IN('_SYSTEM' ) OR GRANTOR IN(SELECT USER_NAME FROM system.Definition_Schema.Users)", "GRANTEE IN(SELECT ROLE_NAME FROM system.Definition_Schema.Roles) OR GRANTEE IN(SELECT USER_NAME FROM system.Definition_Schema.Users)" }, new int[] { 5, 6, 0, 1 } };
        } else if (tableName.equalsIgnoreCase("INDEXCOLUMNS")) {
            return new Object[] { new String[] { "INDEX_COLUMNS_ORDINAL_POSITION_NOT_NULL" }, new String[] { "\"ORDINAL_POSITION\"is not null" }, new int[] { 6 } };
        } else if (tableName.equalsIgnoreCase("FULLTEXTCOLUMNINFO")) {
            return new Object[] { new String[] { "FULLTEXTINDEX_COLUMNS_ORDINAL_POSITION_NOT_NULL" }, new String[] { "\"ORDINAL_POSITION\"is not null" }, new int[] { 6 } };
        } else if (tableName.equalsIgnoreCase("User_Defined_Types")) {
            return new Object[] { new String[] { "USER_DEFINED_TYPES_USER_DEFINED_TYPE_CATEGORY_NOT_NULL", "USER_DEFINED_TYPES_USER_DEFINED_TYPE_CATEGORY_CHECK", "USER_DEFINED_TYPES_IS_INSTANTIABLE_NOT_NULL", "USER_DEFINED_TYPES_IS_INSTANTIABLE_CHECK", "USER_DEFINED_TYPES_IS_FINAL_NOT_NULL", "USER_DEFINED_TYPES_IS_FINAL_CHECK", "USER_DEFINED_TYPES_ORDERING_FORM_NOT_NULL", "USER_DEFINED_TYPES_ORDERING_FORM_CHECK", "USER_DEFINED_TYPES_ORDERING_CATEGORY_CHECK", "USER_DEFINED_TYPES_REFERENCE_TYPE_CHECK", "USER_DEFINED_TYPES_CHECK_SOURCE_TYPE", "USER_DEFINED_TYPES_CHECK_USER_GENERATED_REFERENCE_TYPE" }, new String[] { "\"USER_DEFINED_TYPE_CATEGORY\"is not null", "USER_DEFINED_TYPE_CATEGORY IN('STRUCTURED','DISTINCT' )", "\"IS_INSTANTIABLE\"is not null", "IS_INSTANTIABLE IN('YES','NO' )", "\"IS_FINAL\"is not null", "IS_FINAL IN('YES','NO' )", "\"ORDERING_FORM\"is not null", "ORDERING_FORM IN('NONE','FULL','EQUALS' )", "ORDERING_CATEGORY IN('RELATIVE','MAP','STATE' )", "REFERENCE_TYPE IN('SYSTEM GENERATED','USER GENERATED','DERIVED' )", "(USER_DEFINED_TYPE_CATEGORY = 'STRUCTURED' AND SOURCE_DTD_IDENTIFIER IS NULL) OR(USER_DEFINED_TYPE_CATEGORY = 'DISTINCT' AND(USER_DEFINED_TYPE_CATALOG, USER_DEFINED_TYPE_SCHEMA, USER_DEFINED_TYPE_NAME, SOURCE_DTD_IDENTIFIER) IS NOT NULL AND(USER_DEFINED_TYPE_CATALOG, USER_DEFINED_TYPE_SCHEMA, USER_DEFINED_TYPE_NAME, 'USER - DEFINED TYPE',SOURCE_DTD_IDENTIFIER) IN(SELECT OBJECT_CATALOG, OBJECT_SCHEMA, OBJECT_NAME, OBJECT_TYPE, DTD_IDENTIFIER FROM system.Definition_Schema.Data_Type_Descriptor WHERE DATA_TYPE NOT IN('ROW','ARRAY','REFERENCE','USER - DEFINED' )))", "(REFERENCE_TYPE <> 'USER GENERATED' AND(USER_DEFINED_TYPE_CATALOG, USER_DEFINED_TYPE_SCHEMA, USER_DEFINED_TYPE_NAME, 'USER - DEFINED TYPE',REF_DTD_IDENTIFIER) NOT IN(SELECT OBJECT_CATALOG, OBJECT_SCHEMA, OBJECT_NAME, OBJECT_TYPE, DTD_IDENTIFIER FROM system.Definition_Schema.Data_Type_Descriptor)) OR(REFERENCE_TYPE = 'USER GENERATED' AND(USER_DEFINED_TYPE_CATALOG, USER_DEFINED_TYPE_SCHEMA, USER_DEFINED_TYPE_NAME, 'USER - DEFINED TYPE',REF_DTD_IDENTIFIER) IN(SELECT OBJECT_CATALOG, OBJECT_SCHEMA, OBJECT_NAME, OBJECT_TYPE, DTD_IDENTIFIER FROM system.Definition_Schema.Data_Type_Descriptor WHERE DATA_TYPE IN('CHARACTER','INTEGER' )))" }, new int[] { 3, 5, 6, 7, 8, 12, 4, 0, 1, 2, 13 } };
        } else if (tableName.equalsIgnoreCase("User_Defined_Type_Privileges")) {
            return new Object[] { new String[] { "PRIVILEGE_TYPE_CHECK", "USER_DEFINED_TYPE_PRIVILEGES_IS_GRANTABLE_NOT_NULL", "USER_DEFINED_TYPE_PRIVILEGES_IS_GRANTABLE_CHECK", "USER_DEFINED_TYPE_PRIVILEGES_GRANTOR_CHECK", "USER_DEFINED_TYPE_PRIVILEGES_GRANTEE_CHECK" }, new String[] { "PRIVILEGE_TYPE = 'TYPE USAGE'", "\"IS_GRANTABLE\"is not null", "IS_GRANTABLE IN('YES','NO' )", "GRANTOR IN(SELECT ROLE_NAME FROM system.Definition_Schema.Roles) OR GRANTOR IN('_SYSTEM' ) OR GRANTOR IN(SELECT USER_NAME FROM system.Definition_Schema.Users)", "GRANTEE IN(SELECT ROLE_NAME FROM system.Definition_Schema.Roles) OR GRANTEE IN(SELECT USER_NAME FROM system.Definition_Schema.Users)" }, new int[] { 5, 6, 0, 1 } };
        } else if (tableName.equalsIgnoreCase("Transforms_Table")) {
            return new Object[] { new String[] { "TRANSFORM_TYPE_NOT_NULL", "TRANSFORM_TYPE_CHECK" }, new String[] { "\"TRANSFORM_TYPE\"is not null", "TRANSFORM_TYPE IN('TO SQL','FROM SQL' )" }, new int[] { 7 } };
        } else if (tableName.equalsIgnoreCase("Method_Specifications")) {
            return new Object[] { new String[] { "METHOD_SPECIFICATION_IS_STATIC_CHECK", "METHOD_SPECIFICATION_IS_OVERRIDING_CHECK", "METHOD_SPECIFICATIONS_LANGUAGE_CHECK", "METHOD_SPECIFICATIONS_PARAMETER_STYLE_CHECK", "METHOD_SPECIFICATIONS_IS_DETERMINISTIC_CHECK", "METHOD_SPECIFICATIONS_SQL_DATA_ACCESS_CHECK", "METHOD_SPECIFICATIONS_IS_NULL_CALL_CHECK", "METHOD_SPECIFICATIONS_AS_LOCATOR_CHECK", "METHOD_SPECIFICATIONS_CHECK_DATA_TYPE", "METHOD_SPECIFICATIONS_COMBINATIONS", "METHOD_SPECIFICATIONS_SAME_SCHEMA" }, new String[] { "IS_STATIC IN('YES','NO' )", "IS_OVERRIDING IN('YES','NO' )", "METHOD_LANGUAGE IN('SQL','ADA','C', 'COBOL','FORTRAN','MUMPS','PASCAL','PLI' )", "PARAMETER_STYLE IN('SQL','GENERAL' ) OR PARAMETER_STYLE IS NULL", "IS_DETERMINISTIC IN('YES','NO' )", "SQL_DATA_ACCESS IN('NONE','CONTAINS','READS','MODIFIES' )", "IS_NULL_CALL IN('YES','NO' )", "AS_LOCATOR IN('YES','NO' )", "(USER_DEFINED_TYPE_CATALOG, USER_DEFINED_TYPE_SCHEMA, USER_DEFINED_TYPE_NAME, 'USER - DEFINED TYPE',DTD_IDENTIFIER) IN(SELECT OBJECT_CATALOG, OBJECT_SCHEMA, OBJECT_NAME, OBJECT_TYPE, DTD_IDENTIFIER FROM system.Definition_Schema.Data_Type_Descriptor)", "( (METHOD_LANGUAGE = 'SQL' AND IS_DETERMINISTIC IS NULL) OR(METHOD_LANGUAGE <> 'SQL' AND IS_DETERMINISTIC IS NOT NULL))", "(SPECIFIC_CATALOG, SPECIFIC_SCHEMA) = (USER_DEFINED_TYPE_CATALOG, USER_DEFINED_TYPE_SCHEMA)" }, new int[] { 7, 8, 9, 10, 12, 13, 14, 18, 3, 4, 5, 11, 0, 1 } };
        } else if (tableName.equalsIgnoreCase("Method_Specifications_Paramters")) {
            return new Object[] { new String[] { "METHOD_SPECIFICATION_PARAMETER_POSITION_NOT_NULL", "METHOD_SPECIFICATION_PARAMETERS_ORDINAL_POSITION_GREATER_THAN_ZERO_CHECK", "METHOD_SPECIFICATION_PARAMETERS_ORDINAL_POSITION_CONTIGUOUS_CHECK", "METHOD_SPECIFICATION_PARAMETER_MODE_CHECK", "METHOD_SPECIFICATION_PARAMETER_IS_RESULT_CHECK", "METHOD_SPECIFICATION_PARAMETER_AS_LOCATOR_CHECK", "METHOD_SPECIFICATION_PARAMETERS_CHECK_DATA_TYPE" }, new String[] { "\"ORDINAL_POSITION\"is not null", "ORDINAL_POSITION > 0", "0 = ALL(SELECT MAX(ORDINAL_POSITION) - COUNT( * ) FROM system.Definition_Schema.Method_Specifications_Paramters GROUP BY SPECIFIC_CATALOG, SPECIFIC_SCHEMA, SPECIFIC_NAME)", "PARAMETER_MODE IN('IN' )", "IS_RESULT IN('YES','NO' )", "AS_LOCATOR IN('YES','NO' )", "(SPECIFIC_CATALOG, SPECIFIC_SCHEMA, SPECIFIC_NAME, 'USER - DEFINED TYPE',DTD_IDENTIFIER) IN(SELECT OBJECT_CATALOG, OBJECT_SCHEMA, OBJECT_NAME, OBJECT_TYPE, DTD_IDENTIFIER FROM system.Definition_Schema.Data_Type_Descriptor)" }, new int[] { 7, 0, 1, 2, 9, 10, 11, 8 } };
        } else if (tableName.equalsIgnoreCase("ROUTINE_TABLE_USAGE")) {
            return new Object[] { new String[] { "ROUTINE_TABLE_USAGE_CHECK_REFERENCES_TABLES" }, new String[] { "(TABLE_CATALOG <> ANY ( SELECT CATALOG_NAME FROM system.Definition_Schema.SCHEMATA ) OR ( TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME ) IN ( SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME FROM system.Definition_Schema.TABLES))" }, new int[] { 3, 4, 5 } };
        } else if (tableName.equalsIgnoreCase("ROUTINE_COLUMN_USAGE")) {
            return new Object[] { new String[] { "ROUTINE_COLUMN_USAGE_CHECK_REFERENCES_COLUMNS" }, new String[] { "( TABLE_CATALOG <> ANY ( SELECT CATALOG_NAME FROM system.Definition_Schema.SCHEMATA ) OR ( TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME ) IN ( SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME FROM system.Definition_Schema.COLUMNS ) )" }, new int[] { 3, 4, 5, 6 } };
        }
        return null;
    }

    private static Object[] getUniqueKeyInformation(String tableName) throws DException {
        if (tableName.equalsIgnoreCase("COLUMNS")) {
            return new Object[] { "COLUMNS_UNIQUE", new String[] { "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME", "ORDINAL_POSITION" }, new int[] { 1, 2, 3, 4 } };
        } else if (tableName.equalsIgnoreCase("KEY_COLUMN_USAGE")) {
            return new Object[] { "KEY_COLUMN_USAGE_UNIQUE", new String[] { "CONSTRAINT_CATALOG", "CONSTRAINT_SCHEMA", "CONSTRAINT_NAME", "ORDINAL_POSITION" }, new int[] { 1, 2, 3, 4 } };
        }
        return null;
    }

    private static Object[] getPrimaryKeyInformation(String tableName) throws DException {
        if (tableName.equalsIgnoreCase("USERS")) {
            return new Object[] { "USERS_PRIMARY_KEY", new String[] { "USER_NAME" }, new int[] { 1 } };
        } else if (tableName.equalsIgnoreCase("ROLES")) {
            return new Object[] { "ROLES_PRIMARY_KEY", new String[] { "ROLE_NAME" }, new int[] { 1 } };
        } else if (tableName.equalsIgnoreCase("ROLE_AUTHORIZATION_DESCRIPTORS")) {
            return new Object[] { "ROLE_AUTHORIZATION_DESCRIPTORS_PRIMARY_KEY", new String[] { "ROLE_NAME", "GRANTEE" }, new int[] { 1, 2 } };
        } else if (tableName.equalsIgnoreCase("SCHEMATA")) {
            return new Object[] { "SCHEMATA_PRIMARY_KEY", new String[] { "CATALOG_NAME", "SCHEMA_NAME" }, new int[] { 1, 2 } };
        } else if (tableName.equalsIgnoreCase("CHARACTER_SETS")) {
            return new Object[] { "CHARACTER_SETS_PRIMARY_KEY", new String[] { "CHARACTER_SET_CATALOG", "CHARACTER_SET_SCHEMA", "CHARACTER_SET_NAME" }, new int[] { 1, 2, 3 } };
        } else if (tableName.equalsIgnoreCase("COLLATIONS")) {
            return new Object[] { "COLLATIONS_PAD_PRIMARY_KEY", new String[] { "COLLATION_CATALOG", "COLLATION_SCHEMA", "COLLATION_NAME" }, new int[] { 1, 2, 3 } };
        } else if (tableName.equalsIgnoreCase("DOMAINS")) {
            return new Object[] { "DOMAINS_PRIMARY_KEY", new String[] { "DOMAIN_CATALOG", "DOMAIN_SCHEMA", "DOMAIN_NAME" }, new int[] { 1, 2, 3 } };
        } else if (tableName.equalsIgnoreCase("TABLES")) {
            return new Object[] { "TABLES_PRIMARY_KEY", new String[] { "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME" }, new int[] { 1, 2, 3 } };
        } else if (tableName.equalsIgnoreCase("COLUMNS")) {
            return new Object[] { "COLUMNS_PRIMARY_KEY", new String[] { "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME", "COLUMN_NAME" }, new int[] { 1, 2, 3, 4 } };
        } else if (tableName.equalsIgnoreCase("DATA_TYPE_DESCRIPTOR")) {
            return new Object[] { "DATA_TYPE_DESCRIPTOR_PRIMARY_KEY", new String[] { "OBJECT_CATALOG", "OBJECT_SCHEMA", "OBJECT_NAME", "OBJECT_TYPE", "DTD_IDENTIFIER" }, new int[] { 1, 2, 3, 4, 5 } };
        } else if (tableName.equalsIgnoreCase("TABLE_CONSTRAINTS")) {
            return new Object[] { "TABLE_CONSTRAINTS_PRIMARY_KEY", new String[] { "CONSTRAINT_CATALOG", "CONSTRAINT_SCHEMA", "CONSTRAINT_NAME" }, new int[] { 1, 2, 3 } };
        } else if (tableName.equalsIgnoreCase("KEY_COLUMN_USAGE")) {
            return new Object[] { "KEY_COLUMN_USAGE_PRIMARY_KEY", new String[] { "CONSTRAINT_CATALOG", "CONSTRAINT_SCHEMA", "CONSTRAINT_NAME", "COLUMN_NAME" }, new int[] { 1, 2, 3, 4 } };
        } else if (tableName.equalsIgnoreCase("REFERENTIAL_CONSTRAINTS")) {
            return new Object[] { "REFERENTIAL_CONSTRAINTS_PRIMARY_KEY", new String[] { "CONSTRAINT_CATALOG", "CONSTRAINT_SCHEMA", "CONSTRAINT_NAME" }, new int[] { 1, 2, 3 } };
        } else if (tableName.equalsIgnoreCase("CHECK_CONSTRAINTS")) {
            return new Object[] { "CHECK_CONSTRAINTS_PRIMARY_KEY", new String[] { "CONSTRAINT_CATALOG", "CONSTRAINT_SCHEMA", "CONSTRAINT_NAME" }, new int[] { 1, 2, 3 } };
        } else if (tableName.equalsIgnoreCase("CHECK_COLUMN_USAGE")) {
            return new Object[] { "CHECK_COLUMN_USAGE_PRIMARY_KEY", new String[] { "CONSTRAINT_CATALOG", "CONSTRAINT_SCHEMA", "CONSTRAINT_NAME", "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME", "COLUMN_NAME" }, new int[] { 1, 2, 3, 4, 5, 6, 7 } };
        } else if (tableName.equalsIgnoreCase("CHECK_TABLE_USAGE")) {
            return new Object[] { "CHECK_TABLE_USAGE_PRIMARY_KEY", new String[] { "CONSTRAINT_CATALOG", "CONSTRAINT_SCHEMA", "CONSTRAINT_NAME", "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME" }, new int[] { 1, 2, 3, 4, 5, 6 } };
        } else if (tableName.equalsIgnoreCase("TRIGGERS")) {
            return new Object[] { "TRIGGERS_PRIMARY_KEY", new String[] { "TRIGGER_CATALOG", "TRIGGER_SCHEMA", "TRIGGER_NAME" }, new int[] { 1, 2, 3 } };
        } else if (tableName.equalsIgnoreCase("TRIGGER_TABLE_USAGE")) {
            return new Object[] { "TRIGGER_TABLE_USAGE_PRIMARY_KEY", new String[] { "TRIGGER_CATALOG", "TRIGGER_SCHEMA", "TRIGGER_NAME", "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME" }, new int[] { 1, 2, 3, 4, 5, 6 } };
        } else if (tableName.equalsIgnoreCase("TRIGGER_COLUMN_USAGE")) {
            return new Object[] { "TRIGGER_COLUMN_USAGE_PRIMARY_KEY", new String[] { "TRIGGER_CATALOG", "TRIGGER_SCHEMA", "TRIGGER_NAME", "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME", "COLUMN_NAME" }, new int[] { 1, 2, 3, 4, 5, 6, 7 } };
        } else if (tableName.equalsIgnoreCase("TRIGGERED_UPDATE_COLUMNS")) {
            return new Object[] { "TRIGGERED_UPDATE_COLUMNS_PRIMARY_KEY", new String[] { "TRIGGER_CATALOG", "TRIGGER_SCHEMA", "TRIGGER_NAME", "EVENT_OBJECT_COLUMN" }, new int[] { 1, 2, 3, 4 } };
        } else if (tableName.equalsIgnoreCase("VIEWS")) {
            return new Object[] { "VIEWS_PRIMARY_KEY", new String[] { "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME" }, new int[] { 1, 2, 3 } };
        } else if (tableName.equalsIgnoreCase("VIEW_COLUMN_USAGE")) {
            return new Object[] { "VIEW_COLUMN_USAGE_PRIMARY_KEY", new String[] { "VIEW_CATALOG", "VIEW_SCHEMA", "VIEW_NAME", "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME", "COLUMN_NAME" }, new int[] { 1, 2, 3, 4, 5, 6, 7 } };
        } else if (tableName.equalsIgnoreCase("VIEW_TABLE_USAGE")) {
            return new Object[] { "VIEW_TABLE_USAGE_PRIMARY_KEY", new String[] { "VIEW_CATALOG", "VIEW_SCHEMA", "VIEW_NAME", "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME" }, new int[] { 1, 2, 3, 4, 5, 6 } };
        } else if (tableName.equalsIgnoreCase("DOMAIN_CONSTRAINTS")) {
            return new Object[] { "DOMAIN_CONSTRAINTS_PRIMARY_KEY", new String[] { "CONSTRAINT_CATALOG", "CONSTRAINT_SCHEMA", "CONSTRAINT_NAME" }, new int[] { 1, 2, 3 } };
        } else if (tableName.equalsIgnoreCase("COLUMN_PRIVILEGES")) {
            return new Object[] { "COLUMN_PRIVILEGE_PRIMARY_KEY", new String[] { "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME", "PRIVILEGE_TYPE", "COLUMN_NAME", "GRANTOR", "GRANTEE" }, new int[] { 1, 2, 3, 4, 5, 6, 7 } };
        } else if (tableName.equalsIgnoreCase("TABLE_PRIVILEGES")) {
            return new Object[] { "TABLE_PRIVILEGE_PRIMARY_KEY", new String[] { "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME", "PRIVILEGE_TYPE", "GRANTOR", "GRANTEE" }, new int[] { 1, 2, 3, 4, 5, 6 } };
        } else if (tableName.equalsIgnoreCase("USAGE_PRIVILEGES")) {
            return new Object[] { "USAGE_PRIVILEGES_PRIMARY_KEY", new String[] { "OBJECT_CATALOG", "OBJECT_SCHEMA", "OBJECT_NAME", "OBJECT_TYPE", "GRANTOR", "GRANTEE" }, new int[] { 1, 2, 3, 4, 5, 6 } };
        } else if (tableName.equalsIgnoreCase("ROUTINES")) {
            return new Object[] { "ROUTINES_PRIMARY_KEY", new String[] { "SPECIFIC_CATALOG", "SPECIFIC_SCHEMA", "SPECIFIC_NAME" }, new int[] { 1, 2, 3 } };
        } else if (tableName.equalsIgnoreCase("PARAMETERS")) {
            return new Object[] { "PARAMETERS_PRIMARY_KEY", new String[] { "SPECIFIC_CATALOG", "SPECIFIC_SCHEMA", "SPECIFIC_NAME", "ORDINAL_POSITION" }, new int[] { 1, 2, 3, 4 } };
        } else if (tableName.equalsIgnoreCase("ROUTINE_PRIVILEGES")) {
            return new Object[] { "ROUTINE_PRIVILEGES_PRIMARY_KEY", new String[] { "SPECIFIC_CATALOG", "SPECIFIC_SCHEMA", "SPECIFIC_NAME", "PRIVILEGE_TYPE", "GRANTOR", "GRANTEE" }, new int[] { 1, 2, 3, 4, 5, 6 } };
        } else if (tableName.equalsIgnoreCase("INDEXINFO")) {
            return new Object[] { "INDEXINFO_PRIMARY_KEY", new String[] { "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME", "INDEXNAME" }, new int[] { 1, 2, 3, 4 } };
        } else if (tableName.equalsIgnoreCase("INDEXCOLUMNS")) {
            return new Object[] { "INDEXCOLUMNS_PRIMARY_KEY", new String[] { "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME", "INDEXNAME", "COLUMN_NAME" }, new int[] { 1, 2, 3, 4, 5 } };
        } else if (tableName.equalsIgnoreCase("FULLTEXTINFO")) {
            return new Object[] { "FULLTEXTINDEX_PRIMARY_KEY", new String[] { "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME", "INDEXNAME" }, new int[] { 1, 2, 3, 4 } };
        } else if (tableName.equalsIgnoreCase("FULLTEXTCOLUMNINFO")) {
            return new Object[] { "FULLTEXTINDEXCOLUMNS_PRIMARY_KEY", new String[] { "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME", "INDEXNAME", "COLUMN_NAME" }, new int[] { 1, 2, 3, 4, 5 } };
        } else if (tableName.equalsIgnoreCase("User_Defined_Types")) {
            return new Object[] { "USER_DEFINED_TYPES_PRIMARY_KEY", new String[] { "USER_DEFINED_TYPE_CATALOG", "USER_DEFINED_TYPE_SCHEMA", "USER_DEFINED_TYPE_NAME" }, new int[] { 1, 2, 3 } };
        } else if (tableName.equalsIgnoreCase("User_Defined_Type_Privileges")) {
            return new Object[] { "USER_DEFINED_TYPE_PRIVILEGES_PRIMARY_KEY", new String[] { "USER_DEFINED_TYPE_CATALOG", "USER_DEFINED_TYPE_SCHEMA", "USER_DEFINED_TYPE_NAME", "PRIVILEGE_TYPE", "GRANTOR", "GRANTEE" }, new int[] { 1, 2, 3, 4, 5, 6 } };
        } else if (tableName.equalsIgnoreCase("Transforms_Table")) {
            return new Object[] { "TRANSFORMS_PRIMARY_KEY", new String[] { "USER_DEFINED_TYPE_CATALOG", "USER_DEFINED_TYPE_SCHEMA", "USER_DEFINED_TYPE_NAME", "GROUP_NAME", "TRANSFORM_TYPE" }, new int[] { 1, 2, 3, 4, 5 } };
        } else if (tableName.equalsIgnoreCase("Method_Specifications")) {
            return new Object[] { "METHOD_SPECIFICATIONS_PRIMARY_KEY", new String[] { "SPECIFIC_CATALOG", "SPECIFIC_SCHEMA", "SPECIFIC_NAME" }, new int[] { 1, 2, 3 } };
        } else if (tableName.equalsIgnoreCase("Method_Specifications_Paramters")) {
            return new Object[] { "METHOD_SPECIFICATION_PARAMETERS_PRIMARY_KEY", new String[] { "SPECIFIC_CATALOG", "SPECIFIC_SCHEMA", "SPECIFIC_NAME", "METHOD_CATALOG", "METHOD_SCHEMA", "METHOD_NAME", "METHOD_SPECIFICATION_IDENTIFIER", "ORDINAL_POSITION" }, new int[] { 1, 2, 3, 4, 5, 6, 7, 8 } };
        } else if (tableName.equalsIgnoreCase("sequence_number")) {
            return new Object[] { "SEQUENCE_NUMBER_PRIMARY_KEY", new String[] { "SEQUENCE_CATALOG", "SEQUENCE_SCHEMA", "SEQUENCE_NAME" }, new int[] { 1, 2, 3 } };
        } else if (tableName.equalsIgnoreCase("ROUTINE_TABLE_USAGE")) {
            return new Object[] { "ROUTINE_TABLE_USAGE_PRIMARY_KEY", new String[] { "SPECIFIC_CATALOG", "SPECIFIC_SCHEMA", "SPECIFIC_NAME", "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME" }, new int[] { 1, 2, 3, 4, 5, 6 } };
        } else if (tableName.equalsIgnoreCase("ROUTINE_COLUMN_USAGE")) {
            return new Object[] { "ROUTINE_COLUMN_USAGE_PRIMARY_KEY", new String[] { "SPECIFIC_CATALOG", "SPECIFIC_SCHEMA", "SPECIFIC_NAME", "TABLE_CATALOG", "TABLE_SCHEMA", "TABLE_NAME", "COLUMN_NAME" }, new int[] { 1, 2, 3, 4, 5, 6, 7 } };
        }
        return null;
    }

    private static ArrayList getKeyColumnDescriptors(Object[] ColumnsAndIndexes, TableConstraintDescriptor tableConsDes) throws DException {
        String[] columnNames = (String[]) ColumnsAndIndexes[1];
        int[] oridinalPosition = (int[]) ColumnsAndIndexes[2];
        ArrayList arr = new ArrayList(5);
        for (int i = 0; i < columnNames.length; i++) {
            KeyColumnUsageDescriptor kcud = new KeyColumnUsageDescriptor();
            kcud.constraint_catalog = SystemTables.systemCatalog;
            kcud.constraint_schema = SystemTables.systemSchema;
            kcud.constraint_name = ColumnsAndIndexes[0].toString();
            kcud.table_catalog = SystemTables.systemCatalog;
            kcud.table_schema = SystemTables.systemSchema;
            kcud.table_name = tableConsDes.table_name;
            kcud.column_name = columnNames[i];
            kcud.ordinal_position = oridinalPosition[i];
            kcud.tableConstraintDescriptor = tableConsDes;
            arr.add(kcud);
        }
        return arr;
    }
}
