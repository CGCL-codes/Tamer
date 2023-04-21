package com.thinkvirtual.sql.oracle;

import java.util.List;
import java.sql.*;
import com.crossdb.sql.*;

public class OracleSelectQuery extends DefaultSelectQuery implements SelectQuery {

    public void addWhereCondition(String pre, int comparison, java.util.Date pred) {
        SQLDateTimeFormat sqldf = new SQLDateTimeFormat();
        wclause.addCondition(new WhereCondition(pre, comparison, " to_date('" + sqldf.format(pred) + "','YYYY-MM-DD HH24:MI:SS') "));
    }

    public void addWhereCondition(String and_or, String pre, int comparison, java.util.Date pred) {
        SQLDateTimeFormat sqldf = new SQLDateTimeFormat();
        wclause.addCondition(and_or, new WhereCondition(pre, comparison, " to_date('" + sqldf.format(pred) + "','YYYY-MM-DD HH24:MI:SS') "));
    }

    public String toString() {
        String ret = "SELECT ";
        if (isDistinct()) {
            ret += "DISTINCT ";
        }
        int i;
        if (columns == null || columns.size() == 0) {
            ret += "* ";
        } else {
            for (i = 0; i < columns.size(); i++) {
                String column = (String) (columns.get(i));
                ret += column + ",";
            }
            if (i > 0) {
                ret = ret.substring(0, ret.length() - 1);
            }
        }
        ret += " FROM ";
        String join_conditions = "";
        if (tables == null || tables.size() == 0) {
            return null;
        } else {
            for (i = 0; i < tables.size(); i++) {
                Object tj = tables.get(i);
                if (tj instanceof String) {
                    String tablestr = (String) (tj);
                    if (i == 0) {
                        ret += tablestr;
                    } else {
                        ret += "," + tablestr;
                    }
                } else if (tj instanceof Join) {
                    Join join = (Join) (tj);
                    ret += "," + join.getTableName();
                    WhereClause jconds2 = join.getConditions();
                    List jconds = jconds2.getConditions();
                    for (int m = 0; m < jconds.size(); m++) {
                        WhereCondition c = (WhereCondition) (jconds.get(m));
                        if (m > 0 || join_conditions.length() > 0) {
                            join_conditions += " AND ";
                        }
                        if (join.getType() == Join.LEFT_OUTER_JOIN) {
                            join_conditions += c.getPre() + WhereClause.getOperatorString(c.getOperator()) + c.getPost() + " (+) ";
                        } else {
                            join_conditions += c.getPre() + WhereClause.getOperatorString(c.getOperator()) + c.getPost();
                        }
                    }
                }
            }
        }
        if (wclause.hasConditions()) {
            ret += " WHERE ";
            ret += OracleWhereFormat.format(wclause);
        } else if (join_conditions.length() > 0) {
            ret += " WHERE " + join_conditions;
        }
        if (join_conditions.length() > 0) {
            ret += " AND " + join_conditions;
        }
        if (group_by == null || group_by.size() == 0) {
        } else {
            ret += " GROUP BY ";
            for (i = 0; i < group_by.size(); i++) {
                String group = (String) (group_by.get(i));
                ret += group + ",";
            }
            if (i > 0) {
                ret = ret.substring(0, ret.length() - 1);
            }
        }
        if (order_by == null || order_by.size() == 0) {
        } else {
            ret += " ORDER BY ";
            for (i = 0; i < order_by.size(); i++) {
                String order = (String) (order_by.get(i));
                ret += order + ",";
            }
            if (i > 0) {
                ret = ret.substring(0, ret.length() - 1);
            }
        }
        return ret;
    }

    public CrossdbResultSet execute(Statement stmt) throws SQLException {
        return new OracleResultSet(stmt.executeQuery(toString()));
    }
}
