package com.gorillalogic.dal.common.expr;

import com.gorillalogic.dal.*;
import com.gorillalogic.help.Help;
import com.gorillalogic.dal.parser.Tokenizer;
import java.io.PrintWriter;

/**
 * <code>GCLOps</code> defines GCL string operator forms
 * and generic string based operations.
 * 
 * @author <a href="mailto:Brendan@Gosh">U-GOSH\Brendan</a>
 * @version 1.0
 */
public class GCLOps implements Help.Category {

    public static final String DEREF_OP = ".";

    public static final String ROWCOUNT_OP = "#";

    public static final String ROW_BY_KEY_OP = "@";

    public static final String ROW_BY_ROWID_OP = "@@";

    public static final String TYPE_OP = ":";

    public static final String EXTENT_OP = "!";

    public static final String ROW_DEREF_OP = "/";

    public static final String VAR_OP = "$";

    public static final String SHELL_VAR_OP = "$$";

    public static final String NEW_OP = "new";

    public static final String WITH_OP = "with";

    public static final String IN_OP = "in";

    public static final String LIKE_OP = "like";

    public static final String DESCENDING_OP = "descending";

    public static final String SECOND_ORDER_OP = "?";

    public static final String LINK_DEREF_OP = "~";

    public static final String BACK_OP = "^";

    public static final String COL_BY_COL_INDEX_OP = DEREF_OP;

    public static final String HOME_WORLD_OP = "~";

    public static final String SELF_OP = "self";

    public static final String NULL_OP = "null";

    public static final String SORT_OP = "sortBy";

    public static final String AS_OP = "as";

    public static final String LONGPATH_OP = "..";

    public static final String SCOPE_REZ_OP = "::";

    public static final String PATHJOIN_OP = "%";

    public static final String AND_OP = "and";

    public static final String OR_OP = "or";

    public static final String NOT_OP = "not";

    public static final String NE_OP = "<>";

    public static final String EQ_OP = "=";

    public static final String GT_OP = ">";

    public static final String GTE_OP = ">=";

    public static final String LT_OP = "<";

    public static final String LTE_OP = "<=";

    public static final String ADD_OP = "+";

    public static final String SUBTRACT_OP = "-";

    public static final String MULTIPLY_OP = "*";

    public static final String DIVIDE_OP = "/";

    public static final String CONS_OP = ",";

    /**
	 * Format a path with a prefix operator, according to the
	 * the formating rules described in <code>infixPath()</code>.
	 *
	 * @param op prefix operator
	 * @param sfx tail path
	 * @return path
	 */
    public static String prefixPath(String op, String sfx) {
        return prefixPath(op, sfx, sfx);
    }

    public static String prefixPath(String op, String raw, String sfx) {
        if (elideOp(DEREF_OP, op, raw)) {
            return sfx;
        }
        return op + space(op, raw) + sfx;
    }

    public static String prefixColumnPath(String op, String sfx) {
        if (sfx.length() > 0 && Character.isDigit(sfx.charAt(0))) {
            return op + sfx;
        }
        return sfx;
    }

    /**
	 * Format a path with a suffix operator, according to the
	 * the formating rules described in <code>infixPath()</code>.
	 *
	 * @param pfx head path
	 * @param op prefix operator
	 * @return path
	 */
    public static String postfixPath(String pfx, String op) {
        if (elideOp(DEREF_OP, pfx, op)) {
            return pfx;
        }
        return pfx + space(pfx, op) + op;
    }

    /**
	 * Format a infix path. The following transformations are
	 * applied:
	 *
	 *   (a) successive '.'s are elided
	 *   (b) spaces are inserted as necessary around op so that
	 *       it remains distinguishable from what precedes or
	 *       follows it
	 *
	 * @param pfx a <code>String</code> value
	 * @param op a <code>String</code> value
	 * @param sfx a <code>String</code> value
	 * @return a <code>String</code> value
	 */
    public static String infixPath(String pfx, String op, String sfx) {
        if (elideOp(DEREF_OP, op, sfx)) {
            return pfx + sfx;
        }
        if (DEREF_OP.equals(op) && pfx.endsWith(LINK_DEREF_OP)) {
            return pfx + sfx;
        }
        return pfx + space(pfx, op) + op + space(op, sfx) + sfx;
    }

    private static String space(String lead, String follow) {
        String rez = "";
        if (follow != null & follow.length() > 0) {
            if (isAlphanumChar(follow.charAt(0))) {
                if (lead != null) {
                    int leadLen = lead.length();
                    if (leadLen > 0 && isAlphanumChar(lead.charAt(leadLen - 1))) {
                        rez = " ";
                    }
                }
            }
        }
        return rez;
    }

    private static boolean isAlphanumChar(char c) {
        if (Character.isLetterOrDigit(c)) return true;
        if (c == '_') return true;
        return false;
    }

    private static boolean elideOp(String match, String has, String sfx) {
        if (match.equals(has)) {
            if (sfx.startsWith(match)) {
                return true;
            }
        }
        return false;
    }

    public static final Tokenizer tokenizer() {
        return GCLParser.tokenizer;
    }

    public String getHelpId() {
        return "Operator";
    }

    public Category getHelpCategory() {
        throw new InternalException("GCLOps.getHelpCategory");
    }

    public int numberOfHelpItems() {
        throw new InternalException("GCLOps.numberOfHelpItems");
    }

    public Help getHelpItem(int index) {
        throw new InternalException("GCLOps.getHelpItem");
    }

    public void summary(Out out) {
        final String op = out.ref("Operator", "operator");
        out.print("GCL " + op + " syntax");
    }

    public void detail(Help.Out out) {
        out.println("Key to table below:");
        out.tab("EXPR", "Any expression included in the list below");
        out.tab("CX", "Optional EXPR, but may be omitted in which case the Expr value defaults to current context");
        out.brk();
        out.brk();
        out.tab("ArithOp EXPR", "ArithOp = (" + out.ref("+") + ',' + out.ref("-") + ')');
        out.tab("EXPR ArithOp EXPR", "ArithOp = (" + out.ref("+") + "," + out.ref("-") + "," + out.ref("*") + ")");
        out.tab("EXPR CondOp EXPR", "CondOp = (" + out.ref("=") + "," + out.ref("<>") + "," + out.ref(">") + "," + out.ref(">=") + "," + out.ref("<") + "," + out.ref("<=") + "," + out.ref("like") + ")");
        out.tab(out.ref("not") + " EXPR", "Negation");
        out.tab("EXPR RelOp EXPR", "RelOp = (" + out.ref("and") + "," + out.ref("or") + ")");
        out.tab(out.ref("CX.EXPR", "."), "Path traversal");
        out.tab("CX" + out.ref("@") + "id", "Row select by key \"id\"");
        out.tab("CX" + out.ref("@@") + "rid", "Row select by integer row id (\"" + Table.RIDNAME + "\")");
        out.tab("self");
        out.tab(out.ref("."), "Current context");
        out.tab("..");
        out.tab("?");
        out.tab("~");
        out.tab(out.ref("new") + " EXPR", "New row from extent of EXPR");
        out.tab("#");
        out.tab("CX" + out.ref("#") + "EXPR", "Number of rows in EXPR");
        out.tab("CX " + out.ref("as") + " id", "Rename expr");
        out.tab("$");
        out.tab("$$");
        out.tab(out.ref("$") + "id ", "Local variable \"id\"");
        out.tab(out.ref("$$") + "id ", "Shell variable \"id\"");
        out.tab("/", "System root");
        out.tab(out.ref("/") + "Expr", "EXPR relative to the system root directory");
        out.tab("EXPR" + out.ref("/"), "Deref through EXPR (except ./ = self)");
        out.tab("EXPR1" + out.ref("/") + "EXPR2", "Deref EXPR1 (except ./ = self) then apply EXPR");
        out.tab("CX" + out.ref("!") + "[Type]", "Extent \"Type\" in CX package");
        out.tab("CX" + out.ref(":") + "id", "Cast to type (error if not all rows dowcastable)");
        out.tab("CX" + out.ref("[") + "nth]", "Select the nth (integer value) row");
        out.tab("CX" + out.ref("[") + ":id]", "Row filter by type \"id\", and cast to \"id\"");
        out.tab("CX[EXPR]", "Row filter by boolean-valued EXPR");
        out.tab("CX " + out.ref("in") + " EXPR", "Boolean is true if CX is contained in EXPR");
        out.tab("CX " + out.ref("with") + " func(EXPR)", "Row filter by EXPR = func(EXPR), e.g.\"student with max(grade)\"");
        out.tab(out.ref("if") + " EXPR then EXPR else EXPR", "Conditional, then or else selected row-by-row");
        out.tab("CX " + out.ref("sortBy") + " EXPR", "Sort CX");
        out.tab(out.ref("(EXPR1,EXPR2,...,EXPRn)", ","), "New table with n columns; a \"projection\" (Note: parens also used for grouping operators)");
        out.tab("CX." + out.ref("func", "Function") + "(EXPRs)", "Func is user-defined method or " + out.ref("standard system function", "function"));
    }
}
