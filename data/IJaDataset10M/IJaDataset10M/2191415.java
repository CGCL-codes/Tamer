package com.bayareasoftware.chartengine.ds.util;

import java.sql.Types;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.bayareasoftware.chartengine.model.DataSourceInfo;
import com.bayareasoftware.chartengine.model.DataType;
import com.bayareasoftware.chartengine.model.Metadata;

public final class MetadataUtil {

    /** convert from Jdbc types to CM type system */
    public static int getDataType(int jdbcType) {
        switch(jdbcType) {
            case Types.INTEGER:
            case Types.BIGINT:
                return DataType.INTEGER;
            case Types.DOUBLE:
                return DataType.DOUBLE;
            case Types.TIMESTAMP:
            case Types.DATE:
                return DataType.DATE;
            case Types.VARCHAR:
                return DataType.STRING;
            case Types.BOOLEAN:
                return DataType.BOOLEAN;
            default:
                return DataType.UNKNOWN;
        }
    }

    public static void setColumnNames(Metadata md, String[] names) {
        if (names.length > md.getColumnCount()) {
            throw new IllegalArgumentException("Invalid number of column names");
        }
        for (int i = 0; i < names.length; i++) {
            md.setColumnName(i + 1, names[i]);
        }
    }

    public static void setColumnTypes(Metadata md, int[] types) {
        if (types.length > md.getColumnCount()) {
            throw new IllegalArgumentException("Invalid number of column types");
        }
        for (int i = 0; i < types.length; i++) {
            md.setColumnType(i + 1, types[i]);
        }
    }

    public static void setDefaultColumnNames(Metadata md, String prefix) {
        int len = md.getColumnCount();
        for (int i = 1; i <= len; i++) {
            if (md.getColumnName(i) == null) {
                md.setColumnName(i, prefix + i);
            }
        }
    }

    private static final String GOOGLE_CODE_REGEX = "http://([\\w|-]*).googlecode.com(.*)";

    private static class REGrouper {

        Pattern pattern;

        int group;

        REGrouper(String regex, int group) {
            pattern = Pattern.compile(regex);
            this.group = group;
        }
    }

    private static final REGrouper[] REGEXES = { new REGrouper(GOOGLE_CODE_REGEX, 1) };

    public static String inferDataSourceName(String url) {
        for (int i = 0; i < REGEXES.length; i++) {
            Matcher m = REGEXES[i].pattern.matcher(url);
            if (m.find()) {
                String ret = m.group(REGEXES[i].group);
                ret = ret.replace('-', '_');
                return ret;
            }
        }
        return DataSourceInfo.nameFromUrl(url);
    }
}
