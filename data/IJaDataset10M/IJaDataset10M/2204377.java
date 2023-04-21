package org.equanda.domain.db;

import org.equanda.util.StringUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * convert table and field names from given names to something acceptable for the specific database.
 * Default implementation : no conversion, but add proper prefixes and suffixes.
 *
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public class Convert {

    private Map<String, String> tabletoShort = new HashMap<String, String>();

    private Map<String, String> shortToTable = new HashMap<String, String>();

    private Map<String, String> indexNames = new HashMap<String, String>();

    private Map<String, Set<String>> indexNamesPerTable = new HashMap<String, Set<String>>();

    /** initialise conversion routines, may read conversion tables */
    public void init() {
    }

    /** finish off conversion routines, may be done to make conversions persistent */
    public void done() {
    }

    /**
     * convert a table name
     *
     * @param name base table name to convert
     * @return converted table name
     */
    public String convertTable(String name) {
        return name;
    }

    /**
     * convert a field name
     *
     * @param name base field name to convert
     * @return converted field name
     */
    public String convertField(String name) {
        return name;
    }

    /**
     * convert a field name
     *
     * @param name base index name to convert
     * @return converted field name
     */
    public String convertIndex(String name) {
        return name;
    }

    public String getTableName(String base) {
        return convertTable("T_" + base);
    }

    public String getMultipleTableName(String table, String field) {
        return convertTable("M_" + table + "_" + field);
    }

    public String getFieldName(String base) {
        return convertField("F_" + base);
    }

    public String getLinkFieldName(String base) {
        return convertField("F_" + base + "_UOID");
    }

    public String getIndexName(String table, String field) {
        StringBuffer name = new StringBuffer("I_");
        if (table.length() < 5) {
            name.append(table.toUpperCase());
        } else {
            name.append(getShortTableName(table));
        }
        name.append('_');
        name.append(getSimplifiedFieldName(field));
        return validateIndexNameUnique(convertIndex(name.toString()), table);
    }

    private String getShortTableName(String tableName) {
        String res = tabletoShort.get(tableName);
        if (res == null) {
            StringBuffer camel = new StringBuffer();
            if (tableName.startsWith("T_")) tableName = tableName.substring(2);
            for (int i = 0; i < tableName.length(); i++) {
                if (Character.isUpperCase(tableName.charAt(i))) {
                    camel.append(tableName.charAt(i));
                }
            }
            if (camel.length() > 1) {
                res = camel.toString();
            } else {
                if (tableName.length() <= 5) {
                    res = tableName;
                } else {
                    res = tableName.substring(0, 5);
                }
            }
            int count = 1;
            String base = res;
            while (shortToTable.containsKey(res)) {
                res = base + (count++);
            }
            tabletoShort.put(tableName, res);
            shortToTable.put(res, tableName);
        }
        return res;
    }

    private String getSimplifiedFieldName(String name) {
        return StringUtil.replace(name, "EQUANDA_", "EQ_");
    }

    private String validateIndexNameUnique(String indexName, String tableName) {
        Set<String> indexNames = indexNamesPerTable.get(tableName);
        if (null == indexNames || indexNames.contains(indexName)) return indexName;
        StringBuffer sb = new StringBuffer();
        for (int i = 1; true; i++) {
            sb.setLength(0);
            sb.append(indexName);
            sb.append('_');
            sb.append(Integer.toString(i));
            if (indexNames.contains(sb.toString())) return sb.toString();
        }
    }
}
