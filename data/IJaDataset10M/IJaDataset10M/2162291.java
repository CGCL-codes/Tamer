package com.completex.objective.components.persistency.core.impl;

import com.completex.objective.components.log.Log;
import com.completex.objective.components.persistency.PersistentObject;
import com.completex.objective.components.persistency.Query;
import com.completex.objective.components.persistency.Record;
import java.util.List;

/**
 * @author Gennady Krizhevsky
 */
public class AbstractQueryBuilder {

    protected boolean bindNulls;

    private Log logger = Log.NULL_LOGGER;

    public static String whereByPersistentObject(PersistentObject persistentObject) {
        return whereByPersistentObject(persistentObject, false);
    }

    public static String whereByPersistentObject(PersistentObject persistentObject, boolean primaryKeyOnly) {
        return whereByRecordValues(persistentObject.record(), primaryKeyOnly);
    }

    static String whereByRecordValues(PersistentObject persistentObject, boolean primaryKeyOnly) {
        return whereByRecordValues(persistentObject.record(), primaryKeyOnly);
    }

    static String whereByRecordValues(Record record, boolean primaryKeyOnly) {
        return whereByRecordValues(record, primaryKeyOnly, false);
    }

    static String whereByRecordValues(Record record, boolean primaryKeyOnly, boolean addwhere) {
        StringBuffer buffer = new StringBuffer();
        if (primaryKeyOnly) {
            List keyIndeces = record.getPrimaryKey();
            whereByKeyValues(0, buffer, keyIndeces, record, addwhere);
        } else {
            for (int i = 0, index = 0; i < record.getColumnCount(); i++) {
                if (record.isFieldDirty(i)) {
                    addToWhere(record, i, index, addwhere, buffer);
                    index++;
                }
            }
        }
        return buffer.toString();
    }

    static void addToWhere(Record record, int i, int index, boolean addwhere, StringBuffer buffer) {
        String columnName = record.getColumn(i).getColumnName();
        String tableName = record.getTableName();
        whereOrAnd(index, addwhere, buffer);
        String placeHolder = record.getObject(i) != null ? ("=? ") : " IS NULL ";
        buffer.append(tableName).append(".").append(columnName).append(placeHolder);
    }

    static void whereOrAnd(int index, boolean addwhere, StringBuffer buffer) {
        if (index > 0) {
            buffer.append(Query.AND);
        } else {
            if (addwhere) {
                buffer.append(Query.WHERE);
            }
        }
    }

    static int whereByKeyValues(int index, StringBuffer buffer, List keyIndeces, Record record, boolean addwhere) {
        for (int i = 0; i < keyIndeces.size(); i++, index++) {
            int columnIndex = ((Integer) keyIndeces.get(i)).intValue();
            addToWhere(record, columnIndex, index, addwhere, buffer);
        }
        return index;
    }

    public static String singleQoutes(String value) {
        return new StringBuffer().append("'").append(value).append("'").toString();
    }

    static void and(final StringBuffer buffer) {
        if (buffer.length() > 0) {
            buffer.append(Query.AND);
        }
    }

    static void prependWhere(final StringBuffer buffer) {
        if (buffer.length() > 0) {
            buffer.insert(0, Query.WHERE);
        }
    }

    public boolean isBindNulls() {
        return bindNulls;
    }

    public void setBindNulls(boolean bindNulls) {
        this.bindNulls = bindNulls;
    }

    public Log getLogger() {
        return logger;
    }

    public void setLogger(Log logger) {
        if (logger != null) {
            this.logger = logger;
        }
    }
}
