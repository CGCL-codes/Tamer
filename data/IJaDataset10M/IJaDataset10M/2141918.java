package com.framedobjects.dashwell.utils;

import java.util.Vector;
import com.framedobjects.dashwell.db.meta.MetaField;

/**
 * A general utility class.
 * 
 * @author Jens Richnow
 *
 */
public class DataUtils {

    public static final int META_FIELD_UNKNOWN = 0;

    public static final int META_FIELD_CURRENCY = 1;

    public static final int META_FIELD_DATE_TIME = 2;

    public static final int META_FIELD_DECIMAL = 3;

    public static final int META_FIELD_INTEGER = 4;

    public static final int META_FIELD_STRING = 5;

    public static final String OPERATOR_EQUALS = "=";

    public static final String OPERATOR_GREATER = ">";

    public static final String OPERATOR_GREATER_THAN = ">=";

    public static final String OPERATOR_LESS = "<";

    public static final String OPERATOR_LESS_THAN = "<=";

    public static final String OPERATOR_NOT_EQUAL = "<>";

    public static final String OPERATOR_NOT_NULL = "IS NOT NULL";

    public static final String OPERATOR_IS_NULL = "IS NULL";

    public static final String OPERATOR_LIKE = "LIKE";

    public static final String OPERATOR_NOT_LIKE = "NOT LIKE";

    public static final String QUERY_VALUE_VALUE = "Value";

    public static final String QUERY_VALUE_USER_INPUT = "User Input";

    public static final String QUERY_VALUE_USER_ID = "User ID";

    public static final String QUERY_SORT_ASCENDING = "Ascending";

    public static final String QUERY_SORT_DESCENDING = "Descending";

    public static final String LANGUAGE_ENGLISH = "English";

    public static final String LANGUAGE_GERMAN = "German";

    public static synchronized Vector getLanguages() {
        Vector<String> languages = new Vector<String>();
        languages.add(LANGUAGE_ENGLISH);
        languages.add(LANGUAGE_GERMAN);
        return languages;
    }

    public static synchronized String getLocale(String language) {
        String locale = "en_UK";
        if (language != null && language.equalsIgnoreCase(LANGUAGE_GERMAN)) {
            locale = "de_GE";
        }
        return locale;
    }

    public static synchronized String getLanguage(String locale) {
        String language = LANGUAGE_ENGLISH;
        if (locale != null && locale.equalsIgnoreCase("de_GE")) {
            language = LANGUAGE_GERMAN;
        }
        return language;
    }

    public static synchronized String getMetaFieldType(String origFieldType) {
        String metaFieldType = MetaField.TYPE_UNKNOWN;
        if (origFieldType != null) {
            String checkString = origFieldType.toUpperCase();
            if (checkString.indexOf("CHAR") >= 0) {
                metaFieldType = MetaField.TYPE_STRING;
            } else if (checkString.indexOf("INT") >= 0) {
                metaFieldType = MetaField.TYPE_INTEGER;
            } else if (checkString.indexOf("TEXT") >= 0) {
                metaFieldType = MetaField.TYPE_STRING;
            } else if (checkString.indexOf("INT") >= 0) {
                metaFieldType = MetaField.TYPE_INTEGER;
            } else if (checkString.indexOf("NUMBER") >= 0) {
                metaFieldType = MetaField.TYPE_INTEGER;
            } else if (checkString.indexOf("CURRENCY") >= 0) {
                metaFieldType = MetaField.TYPE_CURRENCY;
            } else if (checkString.indexOf("INT") >= 0) {
                metaFieldType = MetaField.TYPE_INTEGER;
            } else if (checkString.indexOf("DATETIME") >= 0) {
                metaFieldType = MetaField.TYPE_DATE_TIME;
            }
        }
        return metaFieldType;
    }

    public static synchronized Vector getQueryValues() {
        Vector<String> values = new Vector<String>();
        values.add(QUERY_VALUE_VALUE);
        values.add(QUERY_VALUE_USER_INPUT);
        values.add(QUERY_VALUE_USER_ID);
        return values;
    }

    public static synchronized Vector getQuerySortOrders() {
        Vector<String> values = new Vector<String>();
        values.add(QUERY_SORT_ASCENDING);
        values.add(QUERY_SORT_DESCENDING);
        return values;
    }

    public static synchronized int getFieldTypeInt(String metaFieldType) {
        int type = META_FIELD_UNKNOWN;
        if (metaFieldType != null) {
            if (metaFieldType.equals(MetaField.TYPE_CURRENCY)) {
                type = META_FIELD_CURRENCY;
            } else if (metaFieldType.equals(MetaField.TYPE_DATE_TIME)) {
                type = META_FIELD_DATE_TIME;
            } else if (metaFieldType.equals(MetaField.TYPE_DECIMAL)) {
                type = META_FIELD_DECIMAL;
            } else if (metaFieldType.equals(MetaField.TYPE_INTEGER)) {
                type = META_FIELD_INTEGER;
            } else if (metaFieldType.equals(MetaField.TYPE_STRING)) {
                type = META_FIELD_STRING;
            }
        }
        return type;
    }

    public static synchronized int getFieldTypeIntOrig(String origFieldType) {
        return getFieldTypeInt(getMetaFieldType(origFieldType));
    }

    public static synchronized String getFieldType(int fieldTypeInt) {
        String fieldType = MetaField.TYPE_UNKNOWN;
        switch(fieldTypeInt) {
            case META_FIELD_CURRENCY:
                fieldType = MetaField.TYPE_CURRENCY;
                break;
            case META_FIELD_DATE_TIME:
                fieldType = MetaField.TYPE_DATE_TIME;
                break;
            case META_FIELD_DECIMAL:
                fieldType = MetaField.TYPE_DECIMAL;
                break;
            case META_FIELD_INTEGER:
                fieldType = MetaField.TYPE_INTEGER;
                break;
            case META_FIELD_STRING:
                fieldType = MetaField.TYPE_STRING;
                break;
        }
        return fieldType;
    }

    public static synchronized Vector getMetaFieldTypes() {
        Vector<String> types = new Vector<String>();
        types.add(MetaField.TYPE_CURRENCY);
        types.add(MetaField.TYPE_DATE_TIME);
        types.add(MetaField.TYPE_DECIMAL);
        types.add(MetaField.TYPE_INTEGER);
        types.add(MetaField.TYPE_STRING);
        types.add(MetaField.TYPE_UNKNOWN);
        return types;
    }

    public static synchronized Vector getOperators() {
        Vector<String> operators = new Vector<String>();
        operators.add(OPERATOR_EQUALS);
        operators.add(OPERATOR_GREATER);
        operators.add(OPERATOR_GREATER_THAN);
        operators.add(OPERATOR_LESS);
        operators.add(OPERATOR_LESS_THAN);
        operators.add(OPERATOR_NOT_EQUAL);
        operators.add(OPERATOR_IS_NULL);
        operators.add(OPERATOR_NOT_NULL);
        operators.add(OPERATOR_LIKE);
        operators.add(OPERATOR_NOT_LIKE);
        return operators;
    }

    public static synchronized Vector getDriverList() {
        Vector<String> drivers = new Vector<String>();
        drivers.add(Constants.DB_DRIVER_EXCEL);
        drivers.add(Constants.DB_DRIVER_JDBC_ODBC);
        drivers.add(Constants.DB_DRIVER_MSSQL);
        drivers.add(Constants.DB_DRIVER_MYSQL);
        return drivers;
    }
}
