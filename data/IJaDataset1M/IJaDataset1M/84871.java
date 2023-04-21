package com.kopiright.tanit.plsql;

import java.util.List;
import com.kopiright.compiler.base.PositionedError;
import com.kopiright.compiler.base.TokenReference;
import com.kopiright.compiler.base.JavaStyleComment;
import com.kopiright.kopi.comp.kjc.JStatement;
import com.kopiright.kopi.comp.kjc.JNameExpression;
import com.kopiright.kopi.comp.kjc.JExpression;
import com.kopiright.xkopi.comp.xkjc.XMethodCallExpression;
import com.kopiright.kopi.comp.kjc.JExpressionStatement;

/**
 * This class represents an expression
 */
public class PLSQLCloseStatement extends PLSQLStatement {

    /**
   * Constructor
   */
    public PLSQLCloseStatement(TokenReference ref, String cursorName, JavaStyleComment[] comment) {
        super(ref, comment);
        this.cursorName = cursorName;
    }

    public void analyse(PLSQLContext context, Environment env) throws PositionedError {
        cursor = (PLCursorDeclaration) env.getVariable(context, cursorName);
    }

    public JStatement toXKopi(PLSQLContext context, Environment env) throws PositionedError {
        JExpression expr = new JNameExpression(getTokenReference(), cursor.getKopiName(env));
        expr = new XMethodCallExpression(getTokenReference(), expr, "close", JExpression.EMPTY);
        return new JExpressionStatement(getTokenReference(), expr, getComments());
    }

    /**
   * Accepts a visitor.
   *
   * @param	visitor			the visitor
   */
    public void accept(PLVisitor visitor) throws PositionedError {
        visitor.visitSQLCloseStatement(this, cursorName);
    }

    private String cursorName;

    private PLCursorDeclaration cursor;
}
