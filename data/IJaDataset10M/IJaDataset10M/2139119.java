package com.kopiright.tanit.plsql;

import com.kopiright.compiler.base.PositionedError;
import com.kopiright.compiler.base.TokenReference;
import com.kopiright.kopi.comp.kjc.CType;
import com.kopiright.kopi.comp.kjc.CStdType;
import com.kopiright.kopi.comp.kjc.JUnaryMinusExpression;
import com.kopiright.kopi.comp.kjc.JExpression;
import com.kopiright.xkopi.comp.xkjc.XMethodCallExpression;
import com.kopiright.kopi.comp.kjc.JLogicalComplementExpression;
import com.kopiright.xkopi.comp.sqlc.Expression;

/**
 * Expression Percent
 */
public class PLPercentNotFoundExpression extends PLPercentExpression {

    /**
   * Construct a node in the parsing tree
   * This method is directly called by the parser
   * @param	where		the line of this node in the source code
   * @param	prefix		prefix operand
   */
    public PLPercentNotFoundExpression(TokenReference where, PLExpression prefix) {
        super(where, prefix);
    }

    public void analyse(PLSQLContext context, Environment env) throws PositionedError {
        prefix.analyse(context, env);
    }

    public CType getType() {
        return CStdType.Boolean;
    }

    public JExpression toXKopi(PLSQLContext context, Environment env) throws PositionedError {
        JExpression expr;
        expr = new XMethodCallExpression(getTokenReference(), prefix.toXKopi(context, env), "rowFound", JExpression.EMPTY);
        return new JLogicalComplementExpression(getTokenReference(), expr);
    }

    /**
   * Accepts a visitor.
   *
   * @param	visitor			the visitor
   */
    public void accept(PLVisitor visitor) throws PositionedError {
        visitor.visitPLPercentNotFoundExpression(this, prefix);
    }
}
