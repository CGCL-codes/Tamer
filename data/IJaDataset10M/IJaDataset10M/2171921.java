package org.jtools.rjdbc.internal.meta;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import org.jpattern.io.Input;
import org.jpattern.io.Output;
import org.jtools.io.IOUtils;
import org.jtools.sql.SQLType;

public final class DBMData {

    private static final int index_flags = 0;

    private static final int offset_flags = -1;

    public static final int allProceduresAreCallable = 1;

    public static final int allTablesAreSelectable = 2;

    public static final int autoCommitFailureClosesAllResultSets = 3;

    public static final int dataDefinitionCausesTransactionCommit = 4;

    public static final int dataDefinitionIgnoredInTransactions = 5;

    public static final int doesMaxRowSizeIncludeBlobs = 6;

    public static final int isCatalogAtStart = 7;

    public static final int isReadOnly = 8;

    public static final int locatorsUpdateCopy = 9;

    public static final int nullPlusNonNullIsNull = 10;

    public static final int nullsAreSortedAtEnd = 11;

    public static final int nullsAreSortedAtStart = 12;

    public static final int nullsAreSortedHigh = 13;

    public static final int nullsAreSortedLow = 14;

    public static final int storesLowerCaseIdentifiers = 15;

    public static final int storesLowerCaseQuotedIdentifiers = 16;

    public static final int storesMixedCaseIdentifiers = 17;

    public static final int storesMixedCaseQuotedIdentifiers = 18;

    public static final int storesUpperCaseIdentifiers = 19;

    public static final int storesUpperCaseQuotedIdentifiers = 20;

    public static final int supportsANSI92EntryLevelSQL = 21;

    public static final int supportsANSI92FullSQL = 22;

    public static final int supportsANSI92IntermediateSQL = 23;

    public static final int supportsAlterTableWithAddColumn = 24;

    public static final int supportsAlterTableWithDropColumn = 25;

    public static final int supportsBatchUpdates = 26;

    public static final int supportsCatalogsInDataManipulation = 27;

    public static final int supportsCatalogsInIndexDefinitions = 28;

    public static final int supportsCatalogsInPrivilegeDefinitions = 29;

    public static final int supportsCatalogsInProcedureCalls = 30;

    public static final int supportsCatalogsInTableDefinitions = 31;

    public static final int supportsColumnAliasing = 32;

    public static final int supportsConvert = 33;

    public static final int supportsCoreSQLGrammar = 34;

    public static final int supportsCorrelatedSubqueries = 35;

    public static final int supportsDataDefinitionAndDataManipulationTransactions = 36;

    public static final int supportsDataManipulationTransactionsOnly = 37;

    public static final int supportsDifferentTableCorrelationNames = 38;

    public static final int supportsExpressionsInOrderBy = 39;

    public static final int supportsExtendedSQLGrammar = 40;

    public static final int supportsFullOuterJoins = 41;

    public static final int supportsGetGeneratedKeys = 42;

    public static final int supportsGroupBy = 43;

    public static final int supportsGroupByBeyondSelect = 44;

    public static final int supportsGroupByUnrelated = 45;

    public static final int supportsIntegrityEnhancementFacility = 46;

    public static final int supportsLikeEscapeClause = 47;

    public static final int supportsLimitedOuterJoins = 48;

    public static final int supportsMinimumSQLGrammar = 49;

    public static final int supportsMixedCaseIdentifiers = 50;

    public static final int supportsMixedCaseQuotedIdentifiers = 51;

    public static final int supportsMultipleOpenResults = 52;

    public static final int supportsMultipleResultSets = 53;

    public static final int supportsMultipleTransactions = 54;

    public static final int supportsNamedParameters = 55;

    public static final int supportsNonNullableColumns = 56;

    public static final int supportsOpenCursorsAcrossCommit = 57;

    public static final int supportsOpenCursorsAcrossRollback = 58;

    public static final int supportsOpenStatementsAcrossCommit = 59;

    public static final int supportsOpenStatementsAcrossRollback = 60;

    public static final int supportsOrderByUnrelated = 61;

    public static final int supportsOuterJoins = 62;

    public static final int supportsPositionedDelete = 63;

    public static final int supportsPositionedUpdate = 64;

    public static final int supportsResultSetHoldability_HOLD = 65;

    public static final int supportsResultSetHoldability_CLOSE = 66;

    public static final int supportsSavepoints = 67;

    public static final int supportsSchemasInDataManipulation = 68;

    public static final int supportsSchemasInIndexDefinitions = 69;

    public static final int supportsSchemasInPrivilegeDefinitions = 70;

    public static final int supportsSchemasInProcedureCalls = 71;

    public static final int supportsSchemasInTableDefinitions = 72;

    public static final int supportsSelectForUpdate = 73;

    public static final int supportsStatementPooling = 74;

    public static final int supportsStoredFunctionsUsingCallSyntax = 75;

    public static final int supportsStoredProcedures = 76;

    public static final int supportsSubqueriesInComparisons = 77;

    public static final int supportsSubqueriesInExists = 78;

    public static final int supportsSubqueriesInIns = 79;

    public static final int supportsSubqueriesInQuantifieds = 80;

    public static final int supportsTableCorrelationNames = 81;

    public static final int supportsTransactions = 81;

    public static final int supportsUnion = 82;

    public static final int supportsUnionAll = 83;

    public static final int usesLocalFilePerTable = 84;

    public static final int usesLocalFiles = 85;

    public static final int supportsResultSetType_FORWARD_ONLY = 86;

    public static final int supportsResultSetType_SCROLL_INSENSITIVE = 87;

    public static final int supportsResultSetType_SCROLL_SENSITIVE = 88;

    public static final int supportsResultSetConcurrency_FORWARD_ONLY_READ_ONLY = 89;

    public static final int supportsResultSetConcurrency_SCROLL_INSENSITIVE_READ_ONLY = 90;

    public static final int supportsResultSetConcurrency_SCROLL_SENSITIVE_READ_ONLY = 91;

    public static final int supportsResultSetConcurrency_FORWARD_ONLY_UPDATABLE = 92;

    public static final int supportsResultSetConcurrency_SCROLL_INSENSITIVE_UPDATABLE = 93;

    public static final int supportsResultSetConcurrency_SCROLL_SENSITIVE_UPDATABLE = 94;

    public static final int supportsTransactionIsolationLevel_NONE = 95;

    public static final int supportsTransactionIsolationLevel_READ_UNCOMMITTED = 96;

    public static final int supportsTransactionIsolationLevel_READ_COMMITTED = 97;

    public static final int supportsTransactionIsolationLevel_REPEATABLE_READ = 98;

    public static final int supportsTransactionIsolationLevel_SERIALIZABLE = 99;

    private static final int flags_last = supportsTransactionIsolationLevel_SERIALIZABLE;

    private static final int num_flags = flags_last - offset_flags + 1;

    private static final int count_flags = num_flags / 32 + (num_flags % 32 == 0 ? 0 : 1);

    private static final int index_op = index_flags + count_flags;

    private static final int num_op = 1;

    private static final int count_op = 1;

    private static final int index_int = index_op + count_op;

    private static final int offset_int = 501;

    public static final int getDatabaseMajorVersion = 501;

    public static final int getDatabaseMinorVersion = 502;

    public static final int getDefaultTransactionIsolation = 503;

    public static final int getDriverMajorVersion = 504;

    public static final int getDriverMinorVersion = 505;

    public static final int getJDBCMajorVersion = 506;

    public static final int getJDBCMinorVersion = 507;

    public static final int getMaxBinaryLiteralLength = 508;

    public static final int getMaxCatalogNameLength = 509;

    public static final int getMaxCharLiteralLength = 510;

    public static final int getMaxColumnNameLength = 511;

    public static final int getMaxColumnsInGroupBy = 512;

    public static final int getMaxColumnsInIndex = 513;

    public static final int getMaxColumnsInOrderBy = 514;

    public static final int getMaxColumnsInSelect = 515;

    public static final int getMaxColumnsInTable = 516;

    public static final int getMaxConnections = 517;

    public static final int getMaxCursorNameLength = 518;

    public static final int getMaxIndexLength = 519;

    public static final int getMaxProcedureNameLength = 520;

    public static final int getMaxRowSize = 521;

    public static final int getMaxSchemaNameLength = 522;

    public static final int getMaxStatementLength = 523;

    public static final int getMaxStatements = 524;

    public static final int getMaxTableNameLength = 525;

    public static final int getMaxTablesInSelect = 526;

    public static final int getMaxUserNameLength = 527;

    public static final int getResultSetHoldability = 528;

    public static final int getSQLStateType = 529;

    private static final int num_int = getSQLStateType - offset_int + 1;

    private static final int count_int = num_int;

    private static final int index_string = 0;

    private static final int offset_string = 1001;

    public static final int getCatalogSeparator = 1001;

    public static final int getCatalogTerm = 1002;

    public static final int getDatabaseProductName = 1003;

    public static final int getDatabaseProductVersion = 1004;

    public static final int getDriverName = 1005;

    public static final int getDriverVersion = 1006;

    public static final int getExtraNameCharacters = 1007;

    public static final int getIdentifierQuoteString = 1008;

    public static final int getNumericFunctions = 1009;

    public static final int getProcedureTerm = 1010;

    public static final int getSQLKeywords = 1011;

    public static final int getSchemaTerm = 1012;

    public static final int getSearchStringEscape = 1013;

    public static final int getStringFunctions = 1014;

    public static final int getSystemFunctions = 1015;

    public static final int getTimeDateFunctions = 1016;

    public static final int getURL = 1017;

    public static final int getUserName = 1018;

    private static final int num_string = getUserName - offset_string + 1;

    private static final int count_string = num_int;

    public static final int OP_DELETE = 0x00070707;

    public static final int OP_INSERT = 0x00383838;

    public static final int OP_UPDATE = 0x07C0C0C0;

    public static final int CAP_DETECTED = 0x010000FF;

    public static final int CAP_OTHER_VISIBLE = 0x0200FF00;

    public static final int CAP_OWN_VISIBLE = 0x04FF0000;

    private static final int RS_FORWARD_ONLY = 0x00494949;

    private static final int RS_SCROLL_INSENSITIVE = 0x00929292;

    private static final int RS_SCROLL_SENSITIVE = 0x07242424;

    private final int[] intData = new int[index_int + num_int];

    private final String[] stringData = new String[num_string];

    private final byte[] convertFlags = new byte[(SQLType.values().length * (SQLType.values().length) / 8) + 1];

    private RowIdLifetime rowIdLifetime;

    public String getString(int number) {
        return stringData[number - offset_string];
    }

    private void setString(int number, String value) {
        stringData[number - offset_string] = value;
    }

    public boolean getFlag(int number) {
        int index = index_flags + (number - offset_flags) / 32;
        int mask = 0x01 << ((number - offset_flags) % 32);
        return (intData[index] & mask) == mask;
    }

    private void setFlag(int number, boolean onOff) {
        int index = index_flags + (number - offset_flags) / 32;
        int mask = 0x01 << ((number - offset_flags) % 32);
        if (onOff) intData[index] |= mask; else intData[index] &= ~mask;
    }

    private boolean getConvertFlag(int number) {
        if (number < 0) return true;
        int index = number / 8;
        int mask = 0x01 << (number % 8);
        return (convertFlags[index] & mask) == mask;
    }

    public boolean getConvertFlag(int fromType, int toType) {
        return getConvertFlag(convertFlagNumber(SQLType.valueOf(fromType), SQLType.valueOf(toType)));
    }

    private void setConvertFlag(int number, boolean onOff) {
        if (number < 0) return;
        int index = number / 8;
        int mask = 0x01 << (number % 8);
        if (onOff) convertFlags[index] |= mask; else convertFlags[index] &= ~mask;
    }

    private int convertFlagNumber(SQLType from, SQLType to) {
        return from.ordinal() * (SQLType.values().length) + to.ordinal();
    }

    public int getInt(int number) {
        return intData[index_int + number - offset_int];
    }

    private void setInt(int number, int value) {
        intData[index_int + number - offset_int] = value;
    }

    private void setInts(DatabaseMetaData md) throws SQLException {
        setInt(getDatabaseMajorVersion, md.getDatabaseMajorVersion());
        setInt(getDatabaseMinorVersion, md.getDatabaseMinorVersion());
        setInt(getDefaultTransactionIsolation, md.getDefaultTransactionIsolation());
        setInt(getDriverMajorVersion, md.getDriverMajorVersion());
        setInt(getDriverMinorVersion, md.getDriverMinorVersion());
        setInt(getJDBCMajorVersion, md.getJDBCMajorVersion());
        setInt(getJDBCMinorVersion, md.getJDBCMinorVersion());
        setInt(getMaxBinaryLiteralLength, md.getMaxBinaryLiteralLength());
        setInt(getMaxCatalogNameLength, md.getMaxCatalogNameLength());
        setInt(getMaxCharLiteralLength, md.getMaxCharLiteralLength());
        setInt(getMaxColumnNameLength, md.getMaxColumnNameLength());
        setInt(getMaxColumnsInGroupBy, md.getMaxColumnsInGroupBy());
        setInt(getMaxColumnsInIndex, md.getMaxColumnsInIndex());
        setInt(getMaxColumnsInOrderBy, md.getMaxColumnsInOrderBy());
        setInt(getMaxColumnsInSelect, md.getMaxColumnsInSelect());
        setInt(getMaxColumnsInTable, md.getMaxColumnsInTable());
        setInt(getMaxConnections, md.getMaxConnections());
        setInt(getMaxCursorNameLength, md.getMaxCursorNameLength());
        setInt(getMaxIndexLength, md.getMaxIndexLength());
        setInt(getMaxProcedureNameLength, md.getMaxProcedureNameLength());
        setInt(getMaxRowSize, md.getMaxRowSize());
        setInt(getMaxSchemaNameLength, md.getMaxSchemaNameLength());
        setInt(getMaxStatementLength, md.getMaxStatementLength());
        setInt(getMaxStatements, md.getMaxStatements());
        setInt(getMaxTableNameLength, md.getMaxTableNameLength());
        setInt(getMaxTablesInSelect, md.getMaxTablesInSelect());
        setInt(getMaxUserNameLength, md.getMaxUserNameLength());
        setInt(getResultSetHoldability, md.getResultSetHoldability());
        setInt(getSQLStateType, md.getSQLStateType());
    }

    private void setStrings(DatabaseMetaData md) throws SQLException {
        setString(getCatalogSeparator, md.getCatalogSeparator());
        setString(getCatalogTerm, md.getCatalogTerm());
        setString(getDatabaseProductName, md.getDatabaseProductName());
        setString(getDatabaseProductVersion, md.getDatabaseProductVersion());
        setString(getDriverName, md.getDriverName());
        setString(getDriverVersion, md.getDriverVersion());
        setString(getExtraNameCharacters, md.getExtraNameCharacters());
        setString(getIdentifierQuoteString, md.getIdentifierQuoteString());
        setString(getNumericFunctions, md.getNumericFunctions());
        setString(getProcedureTerm, md.getProcedureTerm());
        setString(getSQLKeywords, md.getSQLKeywords());
        setString(getSchemaTerm, md.getSchemaTerm());
        setString(getSearchStringEscape, md.getSearchStringEscape());
        setString(getStringFunctions, md.getStringFunctions());
        setString(getSystemFunctions, md.getSystemFunctions());
        setString(getTimeDateFunctions, md.getTimeDateFunctions());
        setString(getURL, md.getURL());
        setString(getUserName, md.getUserName());
    }

    private void setFlags(DatabaseMetaData md) throws SQLException {
        setFlag(allProceduresAreCallable, md.allProceduresAreCallable());
        setFlag(allTablesAreSelectable, md.allTablesAreSelectable());
        setFlag(autoCommitFailureClosesAllResultSets, md.autoCommitFailureClosesAllResultSets());
        setFlag(dataDefinitionCausesTransactionCommit, md.dataDefinitionCausesTransactionCommit());
        setFlag(dataDefinitionIgnoredInTransactions, md.dataDefinitionIgnoredInTransactions());
        setFlag(doesMaxRowSizeIncludeBlobs, md.doesMaxRowSizeIncludeBlobs());
        setFlag(isCatalogAtStart, md.isCatalogAtStart());
        setFlag(isReadOnly, md.isReadOnly());
        setFlag(locatorsUpdateCopy, md.locatorsUpdateCopy());
        setFlag(nullPlusNonNullIsNull, md.nullPlusNonNullIsNull());
        setFlag(nullsAreSortedAtEnd, md.nullsAreSortedAtEnd());
        setFlag(nullsAreSortedAtStart, md.nullsAreSortedAtStart());
        setFlag(nullsAreSortedHigh, md.nullsAreSortedHigh());
        setFlag(nullsAreSortedLow, md.nullsAreSortedLow());
        setFlag(storesLowerCaseIdentifiers, md.storesLowerCaseIdentifiers());
        setFlag(storesLowerCaseQuotedIdentifiers, md.storesLowerCaseQuotedIdentifiers());
        setFlag(storesMixedCaseIdentifiers, md.storesMixedCaseIdentifiers());
        setFlag(storesMixedCaseQuotedIdentifiers, md.storesMixedCaseQuotedIdentifiers());
        setFlag(storesUpperCaseIdentifiers, md.storesUpperCaseIdentifiers());
        setFlag(storesUpperCaseQuotedIdentifiers, md.storesUpperCaseQuotedIdentifiers());
        setFlag(supportsANSI92EntryLevelSQL, md.supportsANSI92EntryLevelSQL());
        setFlag(supportsANSI92FullSQL, md.supportsANSI92FullSQL());
        setFlag(supportsANSI92IntermediateSQL, md.supportsANSI92IntermediateSQL());
        setFlag(supportsAlterTableWithAddColumn, md.supportsAlterTableWithAddColumn());
        setFlag(supportsAlterTableWithDropColumn, md.supportsAlterTableWithDropColumn());
        setFlag(supportsBatchUpdates, md.supportsBatchUpdates());
        setFlag(supportsCatalogsInDataManipulation, md.supportsCatalogsInDataManipulation());
        setFlag(supportsCatalogsInIndexDefinitions, md.supportsCatalogsInIndexDefinitions());
        setFlag(supportsCatalogsInPrivilegeDefinitions, md.supportsCatalogsInPrivilegeDefinitions());
        setFlag(supportsCatalogsInProcedureCalls, md.supportsCatalogsInProcedureCalls());
        setFlag(supportsCatalogsInTableDefinitions, md.supportsCatalogsInTableDefinitions());
        setFlag(supportsColumnAliasing, md.supportsColumnAliasing());
        setFlag(supportsConvert, md.supportsConvert());
        setFlag(supportsCoreSQLGrammar, md.supportsCoreSQLGrammar());
        setFlag(supportsCorrelatedSubqueries, md.supportsCorrelatedSubqueries());
        setFlag(supportsDataDefinitionAndDataManipulationTransactions, md.supportsDataDefinitionAndDataManipulationTransactions());
        setFlag(supportsDataManipulationTransactionsOnly, md.supportsDataManipulationTransactionsOnly());
        setFlag(supportsDifferentTableCorrelationNames, md.supportsDifferentTableCorrelationNames());
        setFlag(supportsExpressionsInOrderBy, md.supportsExpressionsInOrderBy());
        setFlag(supportsExtendedSQLGrammar, md.supportsExtendedSQLGrammar());
        setFlag(supportsFullOuterJoins, md.supportsFullOuterJoins());
        setFlag(supportsGetGeneratedKeys, md.supportsGetGeneratedKeys());
        setFlag(supportsGroupBy, md.supportsGroupBy());
        setFlag(supportsGroupByBeyondSelect, md.supportsGroupByBeyondSelect());
        setFlag(supportsGroupByUnrelated, md.supportsGroupByUnrelated());
        setFlag(supportsIntegrityEnhancementFacility, md.supportsIntegrityEnhancementFacility());
        setFlag(supportsLikeEscapeClause, md.supportsLikeEscapeClause());
        setFlag(supportsLimitedOuterJoins, md.supportsLimitedOuterJoins());
        setFlag(supportsMinimumSQLGrammar, md.supportsMinimumSQLGrammar());
        setFlag(supportsMixedCaseIdentifiers, md.supportsMixedCaseIdentifiers());
        setFlag(supportsMixedCaseQuotedIdentifiers, md.supportsMixedCaseQuotedIdentifiers());
        setFlag(supportsMultipleOpenResults, md.supportsMultipleOpenResults());
        setFlag(supportsMultipleResultSets, md.supportsMultipleResultSets());
        setFlag(supportsMultipleTransactions, md.supportsMultipleTransactions());
        setFlag(supportsNamedParameters, md.supportsNamedParameters());
        setFlag(supportsNonNullableColumns, md.supportsNonNullableColumns());
        setFlag(supportsOpenCursorsAcrossCommit, md.supportsOpenCursorsAcrossCommit());
        setFlag(supportsOpenCursorsAcrossRollback, md.supportsOpenCursorsAcrossRollback());
        setFlag(supportsOpenStatementsAcrossCommit, md.supportsOpenStatementsAcrossCommit());
        setFlag(supportsOpenStatementsAcrossRollback, md.supportsOpenStatementsAcrossRollback());
        setFlag(supportsOrderByUnrelated, md.supportsOrderByUnrelated());
        setFlag(supportsOuterJoins, md.supportsOuterJoins());
        setFlag(supportsPositionedDelete, md.supportsPositionedDelete());
        setFlag(supportsPositionedUpdate, md.supportsPositionedUpdate());
        setFlag(supportsResultSetHoldability_HOLD, md.supportsResultSetHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT));
        setFlag(supportsResultSetHoldability_CLOSE, md.supportsResultSetHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT));
        setFlag(supportsSavepoints, md.supportsSavepoints());
        setFlag(supportsSchemasInDataManipulation, md.supportsSchemasInDataManipulation());
        setFlag(supportsSchemasInIndexDefinitions, md.supportsSchemasInIndexDefinitions());
        setFlag(supportsSchemasInPrivilegeDefinitions, md.supportsSchemasInPrivilegeDefinitions());
        setFlag(supportsSchemasInProcedureCalls, md.supportsSchemasInProcedureCalls());
        setFlag(supportsSchemasInTableDefinitions, md.supportsSchemasInTableDefinitions());
        setFlag(supportsSelectForUpdate, md.supportsSelectForUpdate());
        setFlag(supportsStatementPooling, md.supportsStatementPooling());
        setFlag(supportsStoredFunctionsUsingCallSyntax, md.supportsStoredFunctionsUsingCallSyntax());
        setFlag(supportsStoredProcedures, md.supportsStoredProcedures());
        setFlag(supportsSubqueriesInComparisons, md.supportsSubqueriesInComparisons());
        setFlag(supportsSubqueriesInExists, md.supportsSubqueriesInExists());
        setFlag(supportsSubqueriesInIns, md.supportsSubqueriesInIns());
        setFlag(supportsSubqueriesInQuantifieds, md.supportsSubqueriesInQuantifieds());
        setFlag(supportsTableCorrelationNames, md.supportsTableCorrelationNames());
        setFlag(supportsTransactions, md.supportsTransactions());
        setFlag(supportsUnion, md.supportsUnion());
        setFlag(supportsUnionAll, md.supportsUnionAll());
        setFlag(usesLocalFilePerTable, md.usesLocalFilePerTable());
        setFlag(usesLocalFiles, md.usesLocalFiles());
        setFlag(supportsResultSetConcurrency_FORWARD_ONLY_READ_ONLY, md.supportsResultSetConcurrency(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY));
        setFlag(supportsResultSetConcurrency_FORWARD_ONLY_UPDATABLE, md.supportsResultSetConcurrency(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE));
        setFlag(supportsResultSetConcurrency_SCROLL_INSENSITIVE_READ_ONLY, md.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY));
        setFlag(supportsResultSetConcurrency_SCROLL_INSENSITIVE_UPDATABLE, md.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE));
        setFlag(supportsResultSetConcurrency_SCROLL_SENSITIVE_READ_ONLY, md.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY));
        setFlag(supportsResultSetConcurrency_SCROLL_SENSITIVE_UPDATABLE, md.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE));
        setFlag(supportsResultSetType_FORWARD_ONLY, md.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY));
        setFlag(supportsResultSetType_SCROLL_INSENSITIVE, md.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE));
        setFlag(supportsResultSetType_SCROLL_SENSITIVE, md.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE));
        setFlag(supportsTransactionIsolationLevel_NONE, md.supportsTransactionIsolationLevel(Connection.TRANSACTION_NONE));
        setFlag(supportsTransactionIsolationLevel_READ_COMMITTED, md.supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_COMMITTED));
        setFlag(supportsTransactionIsolationLevel_READ_UNCOMMITTED, md.supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_UNCOMMITTED));
        setFlag(supportsTransactionIsolationLevel_REPEATABLE_READ, md.supportsTransactionIsolationLevel(Connection.TRANSACTION_REPEATABLE_READ));
        setFlag(supportsTransactionIsolationLevel_SERIALIZABLE, md.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE));
        setConvertFlags(md);
    }

    private void setConvertFlags(DatabaseMetaData md) throws SQLException {
        for (SQLType from : SQLType.values()) {
            for (SQLType to : SQLType.values()) {
                setConvertFlag(convertFlagNumber(from, to), md.supportsConvert(from.getType(), to.getType()));
            }
        }
    }

    public boolean getOp(int type, int cap, int op) throws SQLException {
        switch(type) {
            case ResultSet.TYPE_FORWARD_ONLY:
                return (intData[index_op] & cap & op & RS_FORWARD_ONLY) != 0;
            case ResultSet.TYPE_SCROLL_INSENSITIVE:
                return (intData[index_op] & cap & op & RS_SCROLL_INSENSITIVE) != 0;
            case ResultSet.TYPE_SCROLL_SENSITIVE:
                return (intData[index_op] & cap & op & RS_SCROLL_SENSITIVE) != 0;
        }
        throw new SQLException("unexpected type:" + type);
    }

    private void setOp(int type, int cap, int op, boolean onOff) {
        if (onOff) switch(type) {
            case ResultSet.TYPE_FORWARD_ONLY:
                intData[index_op] |= (cap & op & RS_FORWARD_ONLY);
                break;
            case ResultSet.TYPE_SCROLL_INSENSITIVE:
                intData[index_op] |= (cap & op & RS_SCROLL_INSENSITIVE);
                break;
            case ResultSet.TYPE_SCROLL_SENSITIVE:
                intData[index_op] |= (cap & op & RS_SCROLL_SENSITIVE);
                break;
        }
    }

    private void setOps(DatabaseMetaData md) throws SQLException {
        intData[index_op] = 0;
        int[] types = { ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.TYPE_SCROLL_SENSITIVE };
        for (int type : types) {
            setOp(type, CAP_DETECTED, OP_DELETE, md.deletesAreDetected(type));
            setOp(type, CAP_DETECTED, OP_INSERT, md.insertsAreDetected(type));
            setOp(type, CAP_DETECTED, OP_UPDATE, md.updatesAreDetected(type));
            setOp(type, CAP_OTHER_VISIBLE, OP_DELETE, md.othersDeletesAreVisible(type));
            setOp(type, CAP_OTHER_VISIBLE, OP_INSERT, md.othersInsertsAreVisible(type));
            setOp(type, CAP_OTHER_VISIBLE, OP_UPDATE, md.othersUpdatesAreVisible(type));
            setOp(type, CAP_OWN_VISIBLE, OP_DELETE, md.ownDeletesAreVisible(type));
            setOp(type, CAP_OWN_VISIBLE, OP_INSERT, md.ownInsertsAreVisible(type));
            setOp(type, CAP_OWN_VISIBLE, OP_UPDATE, md.ownUpdatesAreVisible(type));
        }
    }

    private void set(DatabaseMetaData md) throws SQLException {
        setFlags(md);
        setOps(md);
        setInts(md);
        setStrings(md);
        this.rowIdLifetime = md.getRowIdLifetime();
    }

    public void write(DatabaseMetaData md, Output output) throws IOException, SQLException {
        set(md);
        IOUtils.set(output, intData);
        IOUtils.set(output, stringData);
        output.set(convertFlags, true);
        output.set(rowIdLifetime.ordinal());
    }

    public void read(Input input) throws IOException {
        System.arraycopy(IOUtils.getIntArray(input), 0, intData, 0, intData.length);
        System.arraycopy(IOUtils.getStringArray(input), 0, stringData, 0, stringData.length);
        input.getBytes(convertFlags);
        rowIdLifetime = RowIdLifetime.values()[input.getInt()];
    }

    public RowIdLifetime getRowIdLifetime() {
        return rowIdLifetime;
    }
}
