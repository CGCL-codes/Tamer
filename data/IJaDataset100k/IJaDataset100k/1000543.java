package cs;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * This checker looks for if statement with can be simplified.
 * <br> 
 * Up to now, it only deals with
 * <code>==</code>, <code>!=</code>, <code>&&</code> and <code>||</code>
 * <p>
 * For example:
 * <pre>
 * if (a != null) {
 * 	if (b == null) {
 * 		System.err.println();
 * 	}
 * }
 * </pre>
 * Can be simplified in:
 * <pre>
 * if (a != null &amp;&amp; b == null) {
 * 	System.err.println();
 * }
 * </pre>
 * Another example:
 * <pre>
 * if (a == null) {
 * 	// some code
 * } else {
 * 	if (b == null) {
 * 		// Some other code
 * 	}
 * }
 * </pre>
 * Can be simplified in:
 * <pre>
 * if (a == null) {
 * 	// some code
 * } else if (b == null) {
 * 	// Some other code
 * }
 * </pre>
 * 
 * 
 * @author Arnaud Roques
 * 
 */
public class CollapseIfStatement extends Check {

    public final int[] getDefaultTokens() {
        return new int[] { TokenTypes.LITERAL_IF };
    }

    public final void visitToken(DetailAST ast) {
        twoLogicalIfWithAnd(ast);
        cascadedElse(ast);
    }

    private void cascadedElse(DetailAST ast) {
        final DetailAST slist = ast.findFirstToken(TokenTypes.SLIST);
        if (slist == null) {
            log(ast.getLineNo(), ast.getColumnNo(), "Use brackets with if statements.");
            return;
        }
        final DetailAST else1 = ast.findFirstToken(TokenTypes.LITERAL_ELSE);
        if (else1 == null) {
            return;
        }
        final DetailAST slist2 = else1.findFirstToken(TokenTypes.SLIST);
        if (slist2 == null) {
            if (else1.findFirstToken(TokenTypes.LITERAL_IF) != null) {
                return;
            }
            log(else1.getLineNo(), else1.getColumnNo(), "Use brackets with else statements.");
            return;
        }
        if (slist2.getChildCount() != 2) {
            return;
        }
        final DetailAST if2 = (DetailAST) slist2.getFirstChild();
        if (if2.getType() != TokenTypes.LITERAL_IF) {
            return;
        }
        log(else1.getLineNo(), else1.getColumnNo(), "Remove brackets after the else.");
    }

    private void twoLogicalIfWithAnd(DetailAST ast) {
        final DetailAST slist = ast.findFirstToken(TokenTypes.SLIST);
        if (slist == null) {
            log(ast.getLineNo(), ast.getColumnNo(), "Use brackets with if statements.");
            return;
        }
        final DetailAST else1 = ast.findFirstToken(TokenTypes.LITERAL_ELSE);
        if (else1 != null) {
            return;
        }
        if (slist.getChildCount() != 2) {
            return;
        }
        final DetailAST if2 = (DetailAST) slist.getFirstChild();
        if (if2.getType() != TokenTypes.LITERAL_IF) {
            return;
        }
        final DetailAST else2 = if2.findFirstToken(TokenTypes.LITERAL_ELSE);
        if (else2 != null) {
            return;
        }
        log(if2.getLineNo(), if2.getColumnNo(), "Merge the two if statements using logical AND.");
    }
}
