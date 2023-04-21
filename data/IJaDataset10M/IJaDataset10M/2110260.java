package com.versant.core.jdbc.sql.exp;

import com.versant.core.jdbc.sql.SqlDriver;
import com.versant.core.jdbc.metadata.JdbcColumn;
import com.versant.core.util.CharBuf;
import com.versant.core.common.Debug;
import java.util.Map;
import com.versant.core.common.BindingSupportImpl;

/**
 * This is a join between two columns.
 */
public class JoinExp extends LeafExp {

    private JdbcColumn left;

    private SelectExp leftTable;

    private JdbcColumn right;

    private SelectExp rightTable;

    public JoinExp(JdbcColumn left, SelectExp leftTable, JdbcColumn right, SelectExp rightTable) {
        if (Debug.DEBUG) {
            if (left.table != leftTable.table) {
                throw BindingSupportImpl.getInstance().internal("The column's table is not " + "the same as the table being joined from");
            }
            if (right.table != rightTable.table) {
                throw BindingSupportImpl.getInstance().internal("The column's table is not " + "the same as the table being joined too");
            }
        }
        this.left = left;
        this.leftTable = leftTable;
        this.right = right;
        this.rightTable = rightTable;
    }

    public void setLeftTable(SelectExp leftSExp) {
        leftTable = leftSExp;
    }

    public JoinExp() {
    }

    public SqlExp createInstance() {
        return new JoinExp();
    }

    public SqlExp getClone(SqlExp clone, Map cloneMap) {
        super.getClone(clone, cloneMap);
        JoinExp cst = (JoinExp) clone;
        cst.left = left;
        if (leftTable != null) cst.leftTable = (SelectExp) createClone(leftTable, cloneMap);
        cst.right = right;
        if (rightTable != null) cst.rightTable = (SelectExp) createClone(rightTable, cloneMap);
        return clone;
    }

    public String toString() {
        return super.toString() + " " + left + " = " + right;
    }

    /**
     * Append SQL for this node to s.
     *
     * @param driver The driver being used
     * @param s Append the SQL here
     * @param leftSibling
     */
    public void appendSQLImp(SqlDriver driver, CharBuf s, SqlExp leftSibling) {
        driver.appendSqlJoin(leftTable.alias, left, rightTable.alias, right, rightTable.outer, s);
    }

    /**
     * Make us an outer join or not. This is a NOP except for JoinExp and
     * AndJoinExp.
     * @see JoinExp
     * @see AndJoinExp
     */
    public void setOuter(boolean on) {
        rightTable.outer = on;
    }

    public boolean isOuter() {
        return rightTable.outer;
    }

    /**
     * Replace any references to old with nw. This is used when redundant
     * joins are removed.
     *
     * @see com.versant.core.jdo.query.OrNode#mergeRedundantExistsSelects
     */
    public void replaceSelectExpRef(SelectExp old, SelectExp nw) {
        if (old == leftTable) leftTable = nw;
        if (old == rightTable) rightTable = nw;
    }

    public static boolean isEqual(JoinExp jExp1, JoinExp jExp2) {
        if (jExp1 == jExp2) return true;
        if (jExp1 == null) return false;
        if (jExp2 == null) return false;
        if ((jExp1.left == jExp2.left) && (jExp1.right == jExp2.right) && (jExp1.isOuter() == jExp2.isOuter())) {
            return true;
        }
        return false;
    }
}
