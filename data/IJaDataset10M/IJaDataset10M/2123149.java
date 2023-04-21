package gatchan.phpparser.parser;

import junit.framework.TestCase;
import net.sourceforge.phpdt.internal.compiler.ast.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The AST Tester.
 *
 * @author Matthieu Casanova
 */
public class ASTTester extends TestCase {

    public void testForeachStatement() {
        ForeachStatement foreachStatement = new ForeachStatement(null, null, null, 0, 0, 0, 0, 0, 0);
        tryStatement(foreachStatement);
    }

    public void testClassAccess() {
        ClassAccess classAccess = new ClassAccess(new Variable(new StringLiteral("toto", 0, 0, 0, 0, 0, 0), 0, 0, 0, 0, 0, 0), null, 0, 0, 0, 0);
        tryStatement(classAccess);
    }

    private void tryStatement(Statement stmt) {
        List l = new ArrayList();
        stmt.toString();
        stmt.toString(1);
        stmt.getOutsideVariable(l);
        stmt.getModifiedVariable(l);
        stmt.getUsedVariable(l);
    }
}
