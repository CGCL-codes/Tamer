package com.kopiright.tanit.plsql;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Hashtable;
import com.kopiright.util.base.InconsistencyException;
import com.kopiright.compiler.base.PositionedError;
import com.kopiright.compiler.base.TokenReference;
import com.kopiright.compiler.base.JavaStyleComment;
import com.kopiright.kopi.comp.kjc.*;
import com.kopiright.xkopi.comp.xkjc.*;
import com.kopiright.xkopi.comp.sqlc.FromClause;
import com.kopiright.xkopi.comp.sqlc.WhereClause;
import com.kopiright.xkopi.comp.sqlc.SelectStatement;
import com.kopiright.xkopi.comp.sqlc.SelectExpression;
import com.kopiright.xkopi.comp.sqlc.SelectTableReference;
import com.kopiright.xkopi.comp.sqlc.SelectTableReference;
import com.kopiright.xkopi.comp.sqlc.TableReference;
import com.kopiright.xkopi.comp.sqlc.IntersectTableReference;
import com.kopiright.xkopi.comp.sqlc.IntersectSpec;
import com.kopiright.xkopi.comp.sqlc.UnidiffTableReference;
import com.kopiright.xkopi.comp.sqlc.UnidiffSpec;

/**
 * This class represents an expression
 */
public class PLSQLBinarySelectStatement extends PLSQLSelectStatement {

    /**
   * Constructor
   */
    public PLSQLBinarySelectStatement(TokenReference ref, String oper, PLSQLSelectStatement left, PLSQLSelectExpression right, JavaStyleComment[] comment) {
        super(ref, null, null, null, null, null, null, null, null, null, comment);
        this.oper = oper;
        this.left = left;
        this.right = right;
        right.setSubQuery(true);
    }

    public void analyse(PLSQLContext context, Environment env) throws PositionedError {
        left.analyse(context, env);
        right.analyse(context, env);
    }

    public JStatement toXKopi(PLSQLContext context, Environment env) throws PositionedError {
        throw new InconsistencyException("Not implemented");
    }

    public TableReference toTableReference(PLSQLContext context, Environment env) throws PositionedError {
        TableReference newLeft = left.toTableReference(context, env);
        TableReference newRight = new SelectTableReference(getTokenReference(), right.toXKopi(context, env));
        if (oper.equals("INTERSECT")) {
            return new IntersectTableReference(getTokenReference(), new IntersectSpec(getTokenReference(), null), newLeft, newRight);
        } else if (oper.equals("UNION")) {
            return new UnidiffTableReference(getTokenReference(), new UnidiffSpec(getTokenReference(), "UNION", null), newLeft, newRight);
        } else if (oper.equals("MINUS")) {
            return new UnidiffTableReference(getTokenReference(), new UnidiffSpec(getTokenReference(), "EXCEPT", null), newLeft, newRight);
        }
        throw new InconsistencyException("Unknown sql operator : " + oper);
    }

    /**
   * Accepts a visitor.
   *
   * @param	visitor			the visitor
   */
    public void accept(PLVisitor visitor) throws PositionedError {
    }

    public List getVariables() {
        return left.getVariables();
    }

    public List getSelectItems() {
        return left.getSelectItems();
    }

    public List getPLVariables() {
        return left.getPLVariables();
    }

    protected PLSQLSelectStatement left;

    protected PLSQLSelectExpression right;

    protected String oper;
}
