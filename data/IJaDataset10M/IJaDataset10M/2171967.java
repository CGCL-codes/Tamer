package org.jiql.db.cmds;

import java.util.*;
import java.sql.*;
import java.io.*;
import org.jiql.util.*;
import tools.util.EZArrayList;
import org.jiql.db.*;
import org.jiql.db.objs.*;

public class VerifyTable extends DBCommand {

    public Object execute(SQLParser sqp) throws SQLException {
        jiqlTableInfo jti = sqp.getJiqlTableInfo();
        if (jti == null) {
            {
                String t = (String) sqp.getAliases().get(sqp.getTable());
                if (t != null) {
                    sqp.setTable(t);
                    jti = sqp.getJiqlTableInfo();
                }
            }
            if (jti == null) throw JGException.get("table_does_not_exists", sqp.getTable() + " Table does NOT Exists " + sqp);
        }
        TableInfo ti = sqp.getTableInfo();
        if (ti == null) ti = Gateway.get(sqp.getProperties()).readTableInfo(sqp.getTable());
        Vector v = sqp.getIncludeAllList();
        for (int ct = 0; ct < v.size(); ct++) {
            Criteria c = (Criteria) v.elementAt(ct);
            if (ti.getColumnInfo(sqp.getRealColName(c.getName())) == null && !(sqp.getAction().equals("select") && sqp.getSelectParser().isCompareValues(c))) throw jiqlException.get("unknown_column", sqp.getTable() + " Included Unknown column " + c.getName() + ":" + ti + ":" + sqp);
        }
        v = sqp.getEitherOrAllList();
        for (int ct = 0; ct < v.size(); ct++) {
            Criteria c = (Criteria) v.elementAt(ct);
            if (ti.getColumnInfo(sqp.getRealColName(c.getName())) == null) throw jiqlException.get("unknown_column", "Additional Criteria Unknown column " + c.getName());
        }
        v = sqp.getSelectList();
        for (int ct = 0; ct < v.size(); ct++) {
            String sn = v.elementAt(ct).toString();
            if (sn.indexOf("*") > -1) continue;
            if (ti.getColumnInfo(sqp.getRealColName(sn)) == null && !sqp.isCount()) throw jiqlException.get("unknown_column", sqp.getTable() + " Selected Unknown column " + sn + ":" + sqp.getRealColName(sn) + ":" + sqp.getTAlias(sqp.getTable()) + ":" + ti);
        }
        return null;
    }
}
