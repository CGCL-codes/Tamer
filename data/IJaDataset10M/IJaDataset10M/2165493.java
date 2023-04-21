package com.kopiright.tanit.plsql;

import java.util.List;
import com.kopiright.compiler.base.PositionedError;
import com.kopiright.compiler.base.TokenReference;
import com.kopiright.compiler.base.JavaStyleComment;
import com.kopiright.compiler.base.CWarning;
import com.kopiright.kopi.comp.kjc.*;
import com.kopiright.xkopi.comp.xkjc.XMethodCallExpression;
import com.kopiright.xkopi.comp.xkjc.XNameExpression;
import com.kopiright.xkopi.comp.xkjc.XAssignmentExpression;

/**
 * This class represents an expression
 */
public class PLSQLSavepointStatement extends PLSQLStatement {

    /**
   * Constructor
   */
    public PLSQLSavepointStatement(TokenReference ref, String savepointName, JavaStyleComment[] jcomment) {
        super(ref, jcomment);
        this.savepointName = savepointName;
    }

    public void analyse(PLSQLContext context, Environment env) throws PositionedError {
        PLSQLContext body;
        for (body = context; body != null && !(body instanceof PLBody); body = body.getParentContext()) {
        }
        check(body != null, PlSqlMessages.SAVEPOINT_CONTEXT_ERROR);
        ((PLBody) body).addSavepointDeclaration(savepointName.toLowerCase());
    }

    public JStatement toXKopi(PLSQLContext context, Environment env) throws PositionedError {
        JExpression expr, conn, access;
        conn = new XMethodCallExpression(getTokenReference(), new JThisExpression(getTokenReference()), "getConnection", JExpression.EMPTY);
        conn = new XMethodCallExpression(getTokenReference(), conn, "getJDBCConnection", JExpression.EMPTY);
        expr = new JMethodCallExpression(getTokenReference(), conn, "setSavepoint", new JExpression[] { new JStringLiteral(getTokenReference(), savepointName) });
        access = XNameExpression.build(getTokenReference(), savepointName.toLowerCase());
        return new JExpressionStatement(getTokenReference(), new XAssignmentExpression(getTokenReference(), access, expr), getComments());
    }

    /**
   * Accepts a visitor.
   *
   * @param	visitor			the visitor
   */
    public void accept(PLVisitor visitor) throws PositionedError {
        visitor.visitSQLSavepointStatement(this, savepointName);
    }

    private String savepointName;
}
