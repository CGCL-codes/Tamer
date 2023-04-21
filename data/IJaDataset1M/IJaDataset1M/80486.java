package com.once.server.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.once.BaseException;
import com.once.server.data.model.MetaModelFactory;
import com.once.server.data.model.sql.ISQLDataModel;
import com.once.server.data.model.sql.ISQLDataRelationInfo;
import com.once.server.data.source.sql.ISQLResults;
import com.once.server.data.source.sql.InsertStatementSetTemplate;
import com.once.server.data.source.sql.SQLDataSourceDelete;
import com.once.server.data.source.sql.SQLDataSourceInsert;
import com.once.server.data.source.sql.SQLDataSourceSelect;
import com.once.server.data.source.sql.SQLDataSourceUpdate;
import com.once.server.data.util.SQLUtils;
import com.once.server.security.AccessDeniedException;

public class DataWrapper {

    public static final String QUERY_TYPE_SELECT = "SELECT";

    public static final String QUERY_TYPE_SELECT_DISTINCT = "SELECT DISTINCT";

    public static final String QUERY_TYPE_UPDATE = "UPDATE";

    public static final String QUERY_TYPE_INSERT = "INSERT";

    public static final String QUERY_TYPE_DELETE = "DELETE";

    public static final String QUERY_TYPE_DEFERRED_INSERT = "DEFERRED INSERT";

    public static final String QUERY_TYPE_FIND = "FIND";

    protected static String RESPONSE_NO_SQL_ERRORS = "(NO SQL ERRORS)";

    protected static String RESPONSE_RECORDS = "RECORDS";

    private static final String SYMBOL_NEW_LINE = "\n";

    private static final Logger m_logger = Logger.getLogger(DataWrapper.class);

    public static String executeQuery(String loginSession, String modelName, String queryType, long startRow, long stopRow, String columnsPart, String wherePart, String orderByPart, Map<String, String> columnValues, long primaryValueToFind, boolean ignoreNullPrimaries) {
        boolean usesRelation = modelName.indexOf(".") != -1;
        try {
            if (queryType.equalsIgnoreCase(QUERY_TYPE_SELECT) || queryType.equalsIgnoreCase(QUERY_TYPE_SELECT_DISTINCT)) {
                SQLDataSourceSelect src = usesRelation == true ? new SQLDataSourceSelect(MetaModelFactory.getInstance().getRelation(modelName)) : new SQLDataSourceSelect(SQLUtils.getDataModel(modelName));
                String resp = getResponse(src.execute(loginSession, columnsPart, wherePart, orderByPart, queryType.equalsIgnoreCase(QUERY_TYPE_SELECT_DISTINCT), startRow, getRowsCount(startRow, stopRow)), startRow);
                return (resp);
            } else if (queryType.equalsIgnoreCase(QUERY_TYPE_FIND)) {
                SQLDataSourceSelect src = usesRelation ? new SQLDataSourceSelect(MetaModelFactory.getInstance().getRelation(modelName)) : new SQLDataSourceSelect(SQLUtils.getDataModel(modelName));
                ISQLResults res = src.execute(loginSession, columnsPart, wherePart, orderByPart, false, -1, -1);
                res = src.execute(loginSession, columnsPart, wherePart, orderByPart, false, -1, res.getTotalRecordsCount() + 1);
                int last;
                last = MetaModelFactory.getInstance().getRelation(modelName).getModels().size() - 1;
                String columnName = usesRelation ? MetaModelFactory.getInstance().getRelation(modelName).getModels().get(last).getName() + "." + IDataConstants.FIELD_NAME_PRIMARY : SQLUtils.getDataModel(modelName).getName() + "." + IDataConstants.FIELD_NAME_PRIMARY;
                long rowNum = stopRow < 1 ? 1 : stopRow;
                while (res.next()) {
                    try {
                        if (primaryValueToFind == Long.valueOf(res.getString(columnName)).longValue()) {
                            return String.valueOf(rowNum);
                        }
                    } catch (Exception except) {
                    }
                    rowNum++;
                }
                return "-1";
            } else if (queryType.equalsIgnoreCase(QUERY_TYPE_UPDATE)) {
                SQLDataSourceUpdate src = usesRelation ? new SQLDataSourceUpdate(MetaModelFactory.getInstance().getRelation(modelName)) : new SQLDataSourceUpdate(SQLUtils.getDataModel(modelName));
                return getResponse(src.execute(loginSession, wherePart, columnValues), -1);
            } else if (queryType.equalsIgnoreCase(QUERY_TYPE_INSERT)) {
                SQLDataSourceInsert src = usesRelation ? new SQLDataSourceInsert(MetaModelFactory.getInstance().getRelation(modelName)) : new SQLDataSourceInsert(SQLUtils.getDataModel(modelName));
                return getResponse(src.execute(loginSession, columnValues, ignoreNullPrimaries), -1);
            } else if (queryType.equalsIgnoreCase(QUERY_TYPE_DELETE)) {
                SQLDataSourceDelete src = usesRelation ? new SQLDataSourceDelete(MetaModelFactory.getInstance().getRelation(modelName)) : new SQLDataSourceDelete(SQLUtils.getDataModel(modelName));
                src.execute(loginSession, wherePart, columnValues);
                return getEmptyResponse();
            } else return (getErrorResponse("Query type " + queryType + "is not supported"));
        } catch (Throwable t) {
            m_logger.error(t, t);
            return getErrorResponse(t);
        }
    }

    public static InsertStatementSetTemplate prepareStatementTemplate(String session, String model, String queryType, List<String> columnNames, boolean ignoreNullPrimaryKey) {
        SQLDataSourceInsert source;
        InsertStatementSetTemplate template;
        if (queryType.equalsIgnoreCase(QUERY_TYPE_DEFERRED_INSERT) == true) {
            try {
                source = model.indexOf(".") != -1 ? new SQLDataSourceInsert((ISQLDataRelationInfo) MetaModelFactory.getInstance().getRelation(model)) : new SQLDataSourceInsert(SQLUtils.getDataModel(model));
            } catch (DataAccessException except) {
                source = null;
            }
            if (source != null) try {
                template = source.prepareStatementTemplate(session, columnNames);
            } catch (DataAccessException except) {
                template = null;
            } catch (AccessDeniedException except) {
                template = null;
            } else template = null;
        } else {
            source = null;
            template = null;
        }
        return (template);
    }

    public static String executeQueriesFromTemplate(String session, InsertStatementSetTemplate template, String queryType, List<Map<String, String>> columnValuesList, boolean ignoreNullPrimaryKey) {
        int columnValuesListSize;
        int index;
        int primary;
        String errorMessage;
        StringBuffer output;
        List<List<Integer>> reservedPrimary;
        List<Integer> nextPrimary;
        List<ISQLDataModel> insert;
        int insertSize;
        int model;
        int tries;
        output = new StringBuffer();
        if (queryType.equalsIgnoreCase(QUERY_TYPE_DEFERRED_INSERT) == true) {
            reservedPrimary = new ArrayList<List<Integer>>();
            columnValuesListSize = columnValuesList.size();
            insert = template.getInsertableModels();
            insertSize = insert.size();
            try {
                reservedPrimary = SQLDataSourceInsert.preparePrimarySet(insert, columnValuesListSize);
            } catch (DataAccessException except) {
            }
            if (reservedPrimary.size() == insertSize) {
                tries = 0;
                for (index = 0; index < columnValuesListSize; index++) {
                    nextPrimary = new ArrayList<Integer>();
                    for (model = 0; model < insertSize; model++) nextPrimary.add(reservedPrimary.get(model).get(index));
                    try {
                        primary = template.getDataSource().runFromTemplate(session, columnValuesList.get(index), ignoreNullPrimaryKey, template, nextPrimary, true);
                        tries = 0;
                    } catch (Throwable thrown) {
                        m_logger.error(thrown, thrown);
                        if (thrown != null && (errorMessage = thrown.getMessage()) != null && errorMessage.indexOf("An I/O error") > -1) {
                            tries++;
                            if (tries < 3) index--;
                        }
                        primary = -1;
                    }
                    if (primary > -1) {
                        output.append(primary);
                        output.append("\n");
                    }
                }
            }
            if (output.length() == 0) output.append("-1\n");
        }
        template.getDataSource().closeConnection();
        return (output.toString());
    }

    protected static long getRowsCount(long startRow, long stopRow) {
        return (stopRow < 1 ? 0 : stopRow - (startRow < 1 ? 1 : startRow) + 1);
    }

    protected static String getErrorResponse(String message) {
        return "0\n" + RESPONSE_RECORDS + "\n" + "0\n" + message + "\n";
    }

    protected static String getErrorResponse(String message, BaseException ex) {
        return getErrorResponse(message + " Root cause:" + ex.getMessage() + "\n\nStack trace:" + getStackTrace(ex));
    }

    protected static String getErrorResponse(Throwable t) {
        return getErrorResponse(t.toString() + "\n\n" + t.getMessage() + "\n\nStack trace:" + getStackTrace(t) + "\n\nCause stack trace:" + getStackTrace(t.getCause()));
    }

    protected static String getEmptyResponse() {
        return getErrorResponse(RESPONSE_NO_SQL_ERRORS);
    }

    protected static String getStackTrace(Throwable e) {
        StackTraceElement[] stack;
        int index = 0;
        String output = null;
        if (e != null) {
            stack = e.getStackTrace();
            if (stack != null) {
                output = "";
                for (; index < stack.length; index++) {
                    output = output + stack[index].getFileName() + " :: " + stack[index].getClassName() + "." + stack[index].getMethodName() + "() <" + stack[index].getLineNumber() + ">\n";
                }
            }
        }
        return output;
    }

    public static String getResponse(ISQLResults res, long startRow) {
        StringBuffer result;
        int columnCount;
        int copy;
        long row;
        result = new StringBuffer();
        result.append(res.getTotalRecordsCount());
        result.append(SYMBOL_NEW_LINE);
        columnCount = res.getColumnsCount();
        for (copy = 0; copy < columnCount; copy++) {
            result.append(res.getColumnName(copy));
            result.append(SYMBOL_NEW_LINE);
        }
        result.append(RESPONSE_RECORDS);
        result.append(SYMBOL_NEW_LINE);
        result.append(res.getRecordsCount());
        result.append(SYMBOL_NEW_LINE);
        result.append(RESPONSE_NO_SQL_ERRORS);
        result.append(SYMBOL_NEW_LINE);
        row = startRow < 1 ? 1 : startRow;
        res.beforeFirst();
        while (res.next() == true) {
            result.append(row);
            result.append(SYMBOL_NEW_LINE);
            for (copy = 0; copy < columnCount; copy++) {
                result.append(res.getString(copy).replaceAll("\\n", " "));
                result.append(SYMBOL_NEW_LINE);
            }
            row++;
        }
        return (new String(result));
    }
}
