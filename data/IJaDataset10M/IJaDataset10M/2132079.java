package org.xaware.salesforce.bizcomp;

import java.util.Map;
import java.util.*;

/**
 * The purpose of this class is provide a routine to extract the where columns
 * used in the query.  Nested statements are not worried about because this would
 * not be used for select statements.
 * 
 * @author tferguson
 *
 */
public class SOQLWhereParser {

    private static final String WHERE = " WHERE ";

    private static final String SELECT = "SELECT ";

    private static final String SELECT_ESCAPE = "~###S#~";

    private int currCount = 0;

    private Map<String, String> nestedSelects = new HashMap<String, String>();

    private String tableName;

    private String[] whereColumnNames = null;

    private String[] whereColumnValues = null;

    /**
     * Added because the parent class does not handle delete operations
     */
    public void parse(String sql) throws Exception {
        sql = sql.trim().replaceAll("\\n", " ").replaceAll("\\r", " ");
        nestedSelects.clear();
        currCount = 0;
        whereColumnNames = null;
        whereColumnValues = null;
        performActualParse(sql);
    }

    /**
     * This is expecting some prep work to have been done to the sql statement,
     * the first is that comma's in values have been escaped, the second is that
     * carriage returns and line feeds have been removed.  This will not work on
     * a SOQL statement that has nested selects (which should never occur on an
     * update, upsert, or delete).
     * 
     * @param sql
     */
    private void performActualParse(String sql) {
        String upperSql = sql.toUpperCase();
        int fromPos = upperSql.indexOf(" FROM ");
        int wherePos = upperSql.indexOf(WHERE);
        if (wherePos == -1) {
            tableName = sql.substring(fromPos + 6).trim();
        } else {
            tableName = sql.substring(fromPos + 6, wherePos).trim();
        }
        Vector<String> colNames = new Vector<String>();
        Vector<String> colValues = new Vector<String>();
        if (wherePos != -1) {
            String strWhere = sql.substring(wherePos + WHERE.length());
            strWhere = replaceNestedSelects(strWhere);
            String[] expressions = strWhere.split("\\b[AaNnDd]\\b|\\b[OoRr]\\b");
            for (String expression : expressions) {
                String[] phrases = expression.split("\\s[Ll][Ii][Kk][Ee]\\s|=|>|<|\\s[Ii][Nn]\\s|\\s[Ii][Nn][Cc][Ll][Uu][Dd][Ee][Ss]\\s|\\s[Ee][Xx][Cc][Ll][Uu][Dd][Ee][Ss]\\s");
                if (phrases.length == 2) {
                    String name = phrases[0];
                    if (name.endsWith("!") || name.endsWith("<") || name.endsWith(">")) {
                        name = name.substring(0, name.length() - 1);
                    } else if (name.toUpperCase().endsWith(" NOT")) {
                        name = name.substring(0, name.length() - 4);
                    }
                    name = name.replaceAll(this.tableName + ".", "");
                    colNames.add(name.trim());
                    String value = phrases[1];
                    for (String key : this.nestedSelects.keySet()) {
                        value = value.replaceAll(key, this.nestedSelects.get(key));
                    }
                    value = value.trim();
                    colValues.add(value);
                }
            }
            whereColumnNames = colNames.toArray(new String[] {});
            whereColumnValues = colValues.toArray(new String[] {});
        }
    }

    private String replaceNestedSelects(String where) {
        String upperWhere = where.toUpperCase();
        int selectPos = upperWhere.indexOf(SELECT);
        String newWhere = where.toString();
        if (selectPos != -1) {
            String nestedSelect = where.substring(selectPos);
            boolean found = false;
            int closeParen = -1;
            while (!found) {
                closeParen = nestedSelect.indexOf(")");
                if (closeParen != -1) {
                    String t = nestedSelect.substring(0, closeParen);
                    if (t.indexOf("(") == -1) {
                        found = true;
                        nestedSelect = t;
                    }
                } else {
                    found = true;
                }
            }
            newWhere = where.substring(0, selectPos) + SELECT_ESCAPE + currCount;
            if (closeParen != -1) {
                newWhere += where.substring(closeParen + selectPos);
            }
            nestedSelects.put(SELECT_ESCAPE + currCount, nestedSelect);
            currCount++;
            if (newWhere.toUpperCase().indexOf(SELECT) != -1) {
                newWhere = replaceNestedSelects(newWhere);
            }
        }
        return newWhere;
    }

    public String[] getWhereColumnNames() {
        return whereColumnNames;
    }

    public String[] getWhereColumnValues() {
        return whereColumnValues;
    }
}
