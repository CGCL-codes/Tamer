package cs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * This class provides some utilities to work with a DetailAST.
 * 
 * @author Arnaud Roques
 * 
 */
final class PartialCode {

    /**
	 * Starting of the code
	 */
    private final DetailAST root;

    public PartialCode(final DetailAST root) {
        if (root == null) {
            throw new IllegalArgumentException();
        }
        this.root = root;
    }

    /**
	 * Count the number of token which has a precise type.
	 * 
	 * @param tokenType
	 * @return
	 */
    public int totalTokens(final int tokenType) {
        return totalTokens(root, tokenType);
    }

    /**
	 * Get all tokens in the code of a precise type
	 * 
	 * @param tokenType
	 * @return
	 */
    public List getAllTokens(final int tokenType) {
        List result = new ArrayList();
        recurseForToken(root, tokenType, result);
        return Collections.unmodifiableList(result);
    }

    /**
	 * Count the total number of token
	 * 
	 * @param ast
	 * @param tokenType
	 * @return
	 */
    private static int totalTokens(final DetailAST ast, final int tokenType) {
        int result = 0;
        if (ast.getType() == tokenType) {
            result++;
        }
        DetailAST child = (DetailAST) ast.getFirstChild();
        while (child != null) {
            result += totalTokens(child, tokenType);
            child = (DetailAST) child.getNextSibling();
        }
        return result;
    }

    /**
	 * Count the total number of all idents in the code.
	 */
    public int countIdents() {
        return recurseForIdent(root, (String) null, false);
    }

    /**
	 * Count the total number of a precise ident in the code.
	 * 
	 * @param id
	 * @return
	 */
    public int countIdents(final String id) {
        return recurseForIdent(root, id, false);
    }

    /**
	 * Count the total number of a local variable in the code.
	 * 
	 * @param id
	 * @return
	 */
    public int countLocalVariable(final String id) {
        return recurseForIdent(root, id, true);
    }

    /**
	 * Count recursively idents
	 * 
	 * @param ast
	 * @param id
	 *            null if we want all idents
	 * @param onlyLocalVariable
	 * @return
	 */
    private static int recurseForIdent(final DetailAST ast, final String id, final boolean onlyLocalVariable) {
        DetailAST child = (DetailAST) ast.getFirstChild();
        int result = 0;
        while (child != null) {
            if (child.getType() == TokenTypes.IDENT && (id == null || child.getText().equals(id))) {
                if (onlyLocalVariable && isLocalVariable(child) == false) {
                } else {
                    result++;
                }
            }
            result += recurseForIdent(child, id, onlyLocalVariable);
            child = (DetailAST) child.getNextSibling();
        }
        return result;
    }

    /**
	 * Count recursively idents
	 * 
	 * @param ast
	 * @param pattern
	 * @return
	 */
    private static int recurseForIdent(final DetailAST ast, final Pattern pattern) {
        DetailAST child = (DetailAST) ast.getFirstChild();
        int result = 0;
        while (child != null) {
            if (child.getType() == TokenTypes.IDENT && pattern.matcher(child.getText()).find()) {
                result++;
            }
            result += recurseForIdent(child, pattern);
            child = (DetailAST) child.getNextSibling();
        }
        return result;
    }

    /**
	 * Look recursively for tokens
	 * 
	 * @param ast
	 * @param tokenType
	 * @param all
	 */
    private static void recurseForToken(final DetailAST ast, final int tokenType, final Collection all) {
        DetailAST child = (DetailAST) ast.getFirstChild();
        while (child != null) {
            if (child.getType() == tokenType) {
                all.add(child);
            }
            recurseForToken(child, tokenType, all);
            child = (DetailAST) child.getNextSibling();
        }
    }

    /**
	 * Test if a DetailAST is a local variable
	 * 
	 * @param child
	 * @return
	 */
    private static boolean isLocalVariable(final DetailAST child) {
        final DetailAST parent = child.getParent();
        if (parent.getType() == TokenTypes.METHOD_CALL) {
            return false;
        }
        if (parent.getType() == TokenTypes.DOT) {
            DetailAST first = (DetailAST) parent.getFirstChild();
            if (first != child) {
                return false;
            }
        }
        return true;
    }

    /**
	 * Count idents that match a pattern
	 * 
	 * @param pattern
	 * @return
	 */
    public int countIdents(final Pattern pattern) {
        return recurseForIdent(root, pattern);
    }

    /**
	 * Test if the code is a assignement
	 * 
	 * @param ident
	 * @return
	 */
    public boolean isThisAnAssignement(final String ident) {
        DetailAST assign;
        if (root.getType() == TokenTypes.ASSIGN) {
            assign = root;
        } else if (root.getType() == TokenTypes.EXPR) {
            assign = (DetailAST) root.getFirstChild();
        } else {
            return false;
        }
        if (assign == null || assign.getType() != TokenTypes.ASSIGN) {
            return false;
        }
        final DetailAST left = (DetailAST) assign.getFirstChild();
        if (left == null || left.getType() != TokenTypes.IDENT || left.getText().equals(ident) == false) {
            return false;
        }
        final DetailAST right = (DetailAST) left.getNextSibling();
        if (right == null) {
            return false;
        }
        return recurseForIdent(right, ident, true) == 0;
    }

    /**
	 * Test if the first usage of a variable is an affectation.
	 * 
	 * @param ident
	 * @return
	 */
    public boolean isFirstUsageAnAffectation(String ident) {
        DetailAST child = (DetailAST) root.getFirstChild();
        while (child != null) {
            final int usage = recurseForIdent(child, ident, true);
            if (usage > 0) {
                final PartialCode partialCode = new PartialCode(child);
                boolean result = partialCode.isThisAnAssignement(ident);
                if (result) {
                    return true;
                }
                if (child.getType() == TokenTypes.LITERAL_FOR || child.getType() == TokenTypes.SLIST) {
                    return partialCode.isFirstUsageAnAffectation(ident);
                }
                return false;
            }
            child = (DetailAST) child.getNextSibling();
        }
        return false;
    }

    /**
	 * Test if the code is negative logic
	 * 
	 * @return
	 */
    public boolean isNegativeLogic() {
        if (root.getType() != TokenTypes.EXPR) {
            return false;
        }
        final DetailAST op = (DetailAST) root.getFirstChild();
        return testNegative(op);
    }

    /**
	 * Test if the code is negative logic
	 * 
	 * @return
	 */
    private static boolean testNegative(DetailAST op) {
        if (op.getType() == TokenTypes.NOT_EQUAL) {
            return true;
        }
        if (op.getType() == TokenTypes.LNOT) {
            return true;
        }
        if (op.getType() == TokenTypes.EQUAL) {
            final DetailAST child1 = (DetailAST) op.getFirstChild();
            final DetailAST child2 = op.getLastChild();
            if (child1.getType() == TokenTypes.LITERAL_FALSE || child2.getType() == TokenTypes.LITERAL_FALSE) {
                return true;
            }
        }
        if (op.getType() == TokenTypes.LAND || op.getType() == TokenTypes.LOR) {
            final DetailAST child1 = (DetailAST) op.getFirstChild();
            final DetailAST child2 = op.getLastChild();
            if (testNegative(child1) && testNegative(child2)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Test is the code always ends by a return or a throw.
	 * 
	 * @return
	 */
    public boolean isTerminator() {
        if (root.getType() != TokenTypes.SLIST) {
            return false;
        }
        DetailAST last = root.getLastChild();
        if (last.getType() != TokenTypes.RCURLY) {
            return false;
        }
        last = last.getPreviousSibling();
        if (last == null) {
            return false;
        }
        if (last.getType() == TokenTypes.LITERAL_RETURN || last.getType() == TokenTypes.LITERAL_THROW) {
            return true;
        }
        if (last.getType() == TokenTypes.LITERAL_IF) {
            final DetailAST slist1 = last.findFirstToken(TokenTypes.SLIST);
            final DetailAST else1 = last.findFirstToken(TokenTypes.LITERAL_ELSE);
            if (else1 == null) {
                return false;
            }
            final DetailAST slist2 = else1.findFirstToken(TokenTypes.SLIST);
            if (slist2 == null) {
                return false;
            }
            return new PartialCode(slist1).isTerminator() && new PartialCode(slist2).isTerminator();
        }
        return false;
    }

    /**
	 * Test if the code is a new object creation
	 */
    public boolean isNew() {
        if (root.getType() == TokenTypes.LITERAL_NEW) {
            return true;
        }
        if (root.getType() == TokenTypes.EXPR) {
            final DetailAST child = (DetailAST) root.getFirstChild();
            if (child != null && child.getType() == TokenTypes.LITERAL_NEW) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Is the code "return null;"
	 * 
	 * @return
	 */
    public boolean isReturnNull() {
        if (root.getChildCount() == 2) {
            DetailAST expr = (DetailAST) root.getFirstChild();
            if (expr.getChildCount() != 1) {
                return false;
            }
            DetailAST litteralNull = (DetailAST) expr.getFirstChild();
            if (litteralNull.getType() == TokenTypes.LITERAL_NULL) {
                return true;
            }
        }
        return false;
    }
}
