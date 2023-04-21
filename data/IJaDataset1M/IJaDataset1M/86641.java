package org.jmlspecs.ajmlrac;

import org.jmlspecs.checker.JmlAssertOrAssumeStatement;
import org.jmlspecs.checker.JmlAssertStatement;
import org.jmlspecs.checker.JmlAssumeStatement;
import org.jmlspecs.checker.JmlGuardedStatement;
import org.jmlspecs.checker.JmlInvariantStatement;
import org.jmlspecs.checker.JmlMethodDeclaration;
import org.jmlspecs.checker.JmlNondetChoiceStatement;
import org.jmlspecs.checker.JmlNondetIfStatement;
import org.jmlspecs.checker.JmlSpecStatement;
import org.jmlspecs.checker.JmlStdType;
import org.jmlspecs.checker.JmlUnreachableStatement;
import org.multijava.mjc.CNumericType;
import org.multijava.mjc.CStdType;
import org.multijava.mjc.CType;
import org.multijava.mjc.JAssertStatement;
import org.multijava.mjc.JAssignmentExpression;
import org.multijava.mjc.JBlock;
import org.multijava.mjc.JBreakStatement;
import org.multijava.mjc.JCatchClause;
import org.multijava.mjc.JCompoundStatement;
import org.multijava.mjc.JConstructorBlock;
import org.multijava.mjc.JContinueStatement;
import org.multijava.mjc.JDoStatement;
import org.multijava.mjc.JEmptyStatement;
import org.multijava.mjc.JExpression;
import org.multijava.mjc.JExpressionListStatement;
import org.multijava.mjc.JExpressionStatement;
import org.multijava.mjc.JForStatement;
import org.multijava.mjc.JIfStatement;
import org.multijava.mjc.JLabeledStatement;
import org.multijava.mjc.JReturnStatement;
import org.multijava.mjc.JStatement;
import org.multijava.mjc.JSwitchGroup;
import org.multijava.mjc.JSwitchStatement;
import org.multijava.mjc.JSynchronizedStatement;
import org.multijava.mjc.JThrowStatement;
import org.multijava.mjc.JTryCatchStatement;
import org.multijava.mjc.JTryFinallyStatement;
import org.multijava.mjc.JTypeDeclarationStatement;
import org.multijava.mjc.JTypeDeclarationType;
import org.multijava.mjc.JVariableDeclarationStatement;
import org.multijava.mjc.JWhileStatement;

/**
 * A visitor class for translating JML specification statements in 
 * a method body into assertion check code. The translated assertion
 * check code is stored as a <code>RacNode</code> in the AST of the 
 * specification statement and is expected to be pretty-printed by
 * the class {@link RacPrettyPrinter}. Translated are such specification
 * statements as <code>assume</code>, <code>assert</code>, and
 * <code>unreachable</code> statements.
 *
 * <pre>
 *                 TransConstuctorBody  JConstructorBlock
 *                         |                   |
 *                         +                   +
 *                         v                   V
 * TransType &lt;&gt;----- TransMethodBody -----&gt; JBlock
 *                         |                   |
 *       setAssertionCode()|&lt;&lt;call&gt;&gt;           +
 *                         V                   V
 *                 JmlAssertStatement ----|&gt; JStatement
 *                         ^
 *          assertionCode()|&lt;&lt;call&gt;&gt;
 *                         |
 *                 RacPrettyPrinter
 * </pre>
 *
 * @see #translate()
 * @see #visitJmlAssumeStatement(JmlAssumeStatement)
 * @see #visitJmlAssertStatement(JmlAssertStatement)
 * @see #visitCompoundStatement(JStatement[])
 * @see #visitJmlUnreachableStatement(JmlUnreachableStatement)

 * @author Yoonsik Cheon
 * @version $Revision: 1.53 $
 */
public class TransMethodBody extends RacAbstractVisitor {

    /**
	 * Construct an object of <code>TransMethodBody</code>.
	 *
	 * @param varGen variable name generator
	 * @param mdecl method body to be translated
	 */
    public TransMethodBody(VarGenerator varGen, JmlMethodDeclaration mdecl, JTypeDeclarationType typeDecl) {
        this.varGen = varGen;
        this.methodDecl = mdecl;
        this.body = mdecl.body();
        this.typeDecl = typeDecl;
    }

    /**
	 * Performs the translation of method body and returns the resulting
	 * method body, which may be modified during the translation.
	 * For specification statements such as assume, assert, and
	 * unreachable, the translation produces their assertion check code 
	 * (of type <code>RacNode</code>) and stores them into their ASTs.
	 *
	 * <pre><jml>
	 *  ensures \result != null;
	 * </jml></pre>
	 *
	 * @see #visitJmlAssumeStatement(JmlAssumeStatement)
	 * @see #visitJmlAssertStatement(JmlAssertStatement)
	 * @see #visitCompoundStatement(JStatement[])
	 * @see #visitJmlUnreachableStatement(JmlUnreachableStatement)
	 */
    public JBlock translate() {
        body.accept(this);
        return body;
    }

    /** 
	 * Translates the given JML <code>assert</code> statement and
	 * stores the translated assertion check code into the AST node.
	 * An <code>assert_redundantly</code> statement is translated only
	 * if the command-line option <code>noredundancy</code> is turned
	 * off (which is the default). The translated assertion check code
	 * has the following form:
	 *
	 * <pre>
	 * [[assert E;]] ==
	 *   boolean v = false;
	 *   [[E, v]]
	 *   if (!v) {
	 *     throw new JMLAssertError();
	 *   }
	 * </pre>
	 *
	 * <pre><jml>
	 * also
	 *   requires self != null;
	 *   modifies self.*;
	 *   ensures isCheckable(self) ==> self.assertionCode() != null;
	 * </jml></pre>
	 *
	 * @see #visitJmlAssumeStatement(JmlAssumeStatement)
	 * @see #translate()
	 */
    public void visitJmlAssertStatement(JmlAssertStatement self) {
        if (!isCheckable(self)) return;
        RacContext ctx = RacContext.createPositive();
        RacPredicate pred = new RacPredicate(self.predicate());
        String v1 = varGen.freshVar();
        RacNode n1 = null;
        if (!Main.aRacOptions.oldSemantics()) {
            TransExpression2 transx = new TransExpression2(varGen, ctx, pred, v1, typeDecl, "JMLAssertError");
            n1 = transx.stmt(true).incrIndent();
        } else {
            TransPredicate trans = new TransPredicate(varGen, ctx, pred, v1, typeDecl);
            n1 = trans.wrappedStmt().incrIndent();
        }
        String msg = "\"ASSERT\"";
        String stmt = "";
        RacNode n2 = null;
        JExpression expr = self.throwMessage();
        if (expr != null) {
            CType type = expr.getApparentType();
            String val = defaultValue(type);
            String v2 = varGen.freshVar();
            if (!Main.aRacOptions.oldSemantics()) {
                TransExpression2 transx = new TransExpression2(varGen, ctx, expr, v2, typeDecl, "JMLAssertError");
                n2 = transx.stmt(false).incrIndent();
            } else {
                TransExpression trans2 = new TransExpression(varGen, ctx, expr, v2, typeDecl);
                n2 = trans2.wrappedStmt(val, val).incrIndent();
            }
            stmt = "  " + type + " " + v2 + " = " + val + ";\n$1\n";
            msg = "\"ASSERT with label: \" + " + v2;
        }
        JStatement result = RacParser.parseStatement("do {\n" + "  if (" + isActive() + ") {\n" + "    JMLChecker.enter();\n" + "    java.util.Set " + VN_ERROR_SET + " = new java.util.HashSet();\n" + "    boolean " + v1 + " = false;\n" + "$0\n" + stmt + "    if (!" + v1 + ") {\n" + "      JMLChecker.exit();\n" + "      throw new JMLAssertError(" + msg + ", \"" + TransType.ident() + "\", \"" + methodDecl.ident() + "\", " + VN_ERROR_SET + ");\n" + "    }\n" + "    JMLChecker.exit();\n" + "  }\n" + "}\n" + "while (false);", n1, n2);
        self.setAssertionCode(result);
    }

    /**
	 * Returns true if the given assume (or assert) statement is
	 * checkable. The statement is <em>checkable</em> if it is not a
	 * redundant specification or the command line option
	 * <code>noredundancy</code> is not turned on.
	 */
    private static boolean isCheckable(JmlAssertOrAssumeStatement clause) {
        return !(clause.isRedundantly() && Main.aRacOptions.noredundancy());
    }

    /** 
	 * Translates the JML <code>assume</code> statement and stores the
	 * translated assertion check code into the AST node.  An
	 * <code>assume_redundantly</code> statement is translated only if
	 * the command-line option <code>noredundancy</code> is turned off
	 * (which is the default). Note that the assumptions are checked
	 * only if the runtime option
	 * <code>reportAssumptionViolation</code> is set (which is the
	 * default).  The translated assertion code has the following
	 * form:
	 *
	 * <pre>
	 * [[assume E;]] == 
	 *   if (JMLChecker.reportAssumptionViolation()) {
	 *     boolean v = false;
	 *     [[E, v]]
	 *     if (!v) {
	 *       throw new JMLAssumeError();
	 *     }
	 *   }
	 * </pre>
	 *
	 * <pre><jml>
	 * also
	 *   requires self != null;
	 *   modifies self.*;
	 *   ensures isCheckable(self) ==> self.assertionCode() != null;
	 * </jml></pre>
	 *
	 * @see #visitJmlAssertStatement(JmlAssertStatement)
	 * @see #translate()
	 */
    public void visitJmlAssumeStatement(JmlAssumeStatement self) {
        if (!isCheckable(self)) return;
        RacPredicate pred = new RacPredicate(self.predicate());
        String v1 = varGen.freshVar();
        RacContext ctx = RacContext.createPositive();
        RacNode n1 = null;
        if (!Main.aRacOptions.oldSemantics()) {
            TransExpression2 transx = new TransExpression2(varGen, ctx, pred, v1, typeDecl, "JMLAssumeError");
            n1 = transx.stmt(true).incrIndent();
        } else {
            TransPredicate trans = new TransPredicate(varGen, ctx, pred, v1, typeDecl);
            n1 = trans.wrappedStmt().incrIndent();
        }
        String msg = "\"ASSUME\"";
        String stmt = "";
        RacNode n2 = null;
        JExpression expr = self.throwMessage();
        if (expr != null) {
            CType type = expr.getApparentType();
            String val = defaultValue(type);
            String v2 = varGen.freshVar();
            if (!Main.aRacOptions.oldSemantics()) {
                TransExpression2 transx = new TransExpression2(varGen, ctx, expr, v2, typeDecl, "JMLAssumeError");
                n2 = transx.stmt(false).incrIndent();
            } else {
                TransExpression trans2 = new TransExpression(varGen, ctx, expr, v2, typeDecl);
                n2 = trans2.wrappedStmt(val, val).incrIndent();
            }
            stmt = "  " + type + " " + v2 + " = " + val + ";\n$1\n";
            msg = "\"ASSUME with label: \" + " + v2;
        }
        JStatement result = RacParser.parseStatement("do {\n" + "  if (" + isActive() + " && JMLChecker.reportAssumptionViolation()) {\n" + "    JMLChecker.enter();\n" + "    java.util.Set " + VN_ERROR_SET + " = new java.util.HashSet();\n" + "    boolean " + v1 + " = false;\n" + "$0\n" + stmt + "    if (!" + v1 + ") {\n" + "      JMLChecker.exit();\n" + "      throw new JMLAssumeError(" + msg + ", \"" + TransType.ident() + "\", \"" + methodDecl.ident() + "\", " + VN_ERROR_SET + ");\n" + "    }\n" + "    JMLChecker.exit();\n" + "  }\n" + "}\n" + "while (false);", n1, n2);
        self.setAssertionCode(result);
    }

    /** Translates the given JML guarded statement. Currently not
	 * supported yet. */
    public void visitJmlGuardedStatement(JmlGuardedStatement self) {
    }

    /** Translates the given JML invariant statement. Currently not
	 * supported yet. */
    public void visitJmlInvariantStatement(JmlInvariantStatement self) {
    }

    /** Translates the given JML nondeterministic choice
	 * statement. Currently not supported yet. */
    public void visitJmlNondetChoiceStatement(JmlNondetChoiceStatement self) {
    }

    /** Translates the given JML nondeterministic if statement. 
	 * Currently not supported yet. */
    public void visitJmlNondetIfStatement(JmlNondetIfStatement self) {
    }

    /** Translates the given JML specification statement. Currently
	 * not supported yet. */
    public void visitJmlSpecStatement(JmlSpecStatement self) {
    }

    /**
	 * Returns the string representation of the assignment operator of
	 * the given assignment expression. E.g., <code>" = "</code> for
	 * simple assignment expression.
	 *
	 * <pre><jml>
	 * requires expr != null;
	 * ensures \result != null && \fresh(\result);
	 * </jml></pre>
	 */
    public static String operator(JAssignmentExpression expr) {
        switch(expr.oper()) {
            case OPE_SIMPLE:
                return " = ";
            case OPE_STAR:
                return (" *= ");
            case OPE_SLASH:
                return (" /= ");
            case OPE_PERCENT:
                return (" %= ");
            case OPE_PLUS:
                return (" += ");
            case OPE_MINUS:
                return (" -= ");
            case OPE_SL:
                return (" <<= ");
            case OPE_SR:
                return (" >>= ");
            case OPE_BSR:
                return (" >>>= ");
            case OPE_BAND:
                return (" &= ");
            case OPE_BXOR:
                return (" ^= ");
            case OPE_BOR:
                return (" |= ");
        }
        return " = ";
    }

    /** 
	 * Translates the JML <code>unreachable</code> statement.
	 * The translated assertion code has the following form:
	 *
	 * <pre>
	 * [[unreachable;]] == check(v [&& !assumed]);
	 * </pre>
	 *
	 * The optional conjunction in the check statement is generated only
	 * if the statement is in the scope of any <code>assume</code> statement.
	 * This is because the assert condition must hold only if the assume 
	 * conditions hold.
	 * The flag <code>assumed</code> is set by <code>assume</code> 
	 * statements. 
	 *
	 * @see #visitJmlAssumeStatement(JmlAssumeStatement)
	 */
    public void visitJmlUnreachableStatement(JmlUnreachableStatement self) {
        StringBuffer c = new StringBuffer();
        c.append("do {\n");
        c.append("  if (" + isActive() + ") {\n");
        c.append("    java.util.Set ");
        c.append(VN_ERROR_SET);
        c.append(" = new java.util.HashSet();\n");
        if (self.getTokenReference() != null) {
            c.append("     " + VN_ERROR_SET + ".add(\"\\t");
            c.append(escapeString(self.getTokenReference().toString()));
            c.append("\");\n");
        }
        c.append("    throw new JMLUnreachableError(");
        c.append("\"UNREACHABLE\", \"" + TransType.ident() + "\", \"" + methodDecl.ident() + "\", " + VN_ERROR_SET + ");\n");
        c.append("  }\n");
        c.append("}\n");
        c.append("while (false);");
        self.setAssertionCode(RacParser.parseStatement(c.toString()));
    }

    /** Translates the given Java assert statement. */
    public void visitAssertStatement(JAssertStatement self) {
    }

    /** Translates the given Java block statement. */
    public void visitBlockStatement(JBlock self) {
        JStatement[] body = self.body();
        visitCompoundStatement(body);
    }

    /** Translates the given Java constructor statement. */
    public void visitConstructorBlock(JConstructorBlock self) {
        JStatement[] body = self.body();
        self.explicitSuper();
        JStatement blockCall = self.blockCall();
        if (blockCall != null) {
            blockCall.accept(this);
        }
        visitCompoundStatement(body);
    }

    /** Translates the given Java break statement. */
    public void visitBreakStatement(JBreakStatement self) {
    }

    /** Translates the given Java compound statement. */
    public void visitCompoundStatement(JCompoundStatement self) {
        JStatement[] body = self.body();
        visitCompoundStatement(body);
    }

    /** Translates the given Java compound statement. */
    public void visitCompoundStatement(JStatement[] body) {
        for (int i = 0; i < body.length; i++) {
            body[i].accept(this);
        }
    }

    /** Translates the given Java continue statement. */
    public void visitContinueStatement(JContinueStatement self) {
    }

    /** Translates the given Java empty statement. */
    public void visitEmptyStatement(JEmptyStatement self) {
    }

    /** Translates the given Java expression list statement. */
    public void visitExpressionListStatement(JExpressionListStatement self) {
    }

    /** Translates the given Java expression statement. */
    public void visitExpressionStatement(JExpressionStatement self) {
    }

    /** Translates the given Java if statement. */
    public void visitIfStatement(JIfStatement self) {
        JStatement thenClause = self.thenClause();
        JStatement elseClause = self.elseClause();
        thenClause.accept(this);
        if (elseClause != null) {
            elseClause.accept(this);
        }
    }

    /** Translates the given Java labeled statement. */
    public void visitLabeledStatement(JLabeledStatement self) {
        JStatement stmt = self.stmt();
        stmt.accept(this);
    }

    /** Translates the given Java for statement. */
    public void visitForStatement(JForStatement self) {
        JStatement init = self.init();
        JStatement incr = self.incr();
        JStatement body = self.body();
        if (init != null) {
            init.accept(this);
        }
        if (incr != null) {
            incr.accept(this);
        }
        body.accept(this);
    }

    /** Translates the given Java do statement. */
    public void visitDoStatement(JDoStatement self) {
        JStatement body = self.body();
        body.accept(this);
    }

    /** Translates the given Java while statement. */
    public void visitWhileStatement(JWhileStatement self) {
        JStatement body = self.body();
        body.accept(this);
    }

    /** Translates the given Java return statement. */
    public void visitReturnStatement(JReturnStatement self) {
    }

    /** Translates the given Java switch statement. */
    public void visitSwitchStatement(JSwitchStatement self) {
        JSwitchGroup[] groups = self.groups();
        for (int i = 0; i < groups.length; i++) {
            groups[i].accept(this);
        }
    }

    /** Translates the given Java switch group statement. */
    public void visitSwitchGroup(JSwitchGroup self) {
        JStatement[] stmts = self.getStatements();
        for (int i = 0; i < stmts.length; i++) {
            stmts[i].accept(this);
        }
    }

    /** Translates the given Java synchronized statement. */
    public void visitSynchronizedStatement(JSynchronizedStatement self) {
        JBlock body = self.body();
        body.accept(this);
    }

    /** Translates the given Java throw statement. */
    public void visitThrowStatement(JThrowStatement self) {
    }

    /** Translates the given Java try/catch statement. */
    public void visitTryCatchStatement(JTryCatchStatement self) {
        JBlock tryClause = self.tryClause();
        JCatchClause[] catchClauses = self.catchClauses();
        tryClause.accept(this);
        for (int i = 0; i < catchClauses.length; i++) {
            catchClauses[i].accept(this);
        }
    }

    /** Translates the given Java try/finally statement. */
    public void visitTryFinallyStatement(JTryFinallyStatement self) {
        JBlock tryClause = self.tryClause();
        JBlock finallyClause = self.finallyClause();
        tryClause.accept(this);
        if (finallyClause != null) {
            finallyClause.accept(this);
        }
    }

    /** Translates the given Java type declaration statement. */
    public void visitTypeDeclarationStatement(JTypeDeclarationStatement self) {
    }

    /** Translates the given Java variable declaration statement. */
    public void visitVariableDeclarationStatement(JVariableDeclarationStatement self) {
    }

    /** Return the default value of the type <code>type</code>. */
    public static String defaultValue(CType type) {
        if (type instanceof CNumericType) return (type == JmlStdType.Bigint) ? "java.math.BigInteger.ZERO" : "0"; else if (type == CStdType.Boolean) return "false"; else return "null";
    }

    /**
	 * Returns a string representation of the condition that tests if
	 * the given type of assertion is active. Assertion checking is
	 * active only when the instance has completed its construction.
	 */
    protected String isActive() {
        return "JMLChecker.isActive(JMLChecker.INTRACONDITION) && " + VN_CLASS_INIT + (methodDecl.isStatic() ? "" : " && " + VN_INIT) + (methodDecl.isConstructor() ? " && " + VN_CONSTRUCTED + "()" : "");
    }

    /** variable generator to generate fresh local variables */
    protected VarGenerator varGen;

    /** type declaration to which the method belongs */
    protected JTypeDeclarationType typeDecl;

    /** method declaration whose body is being translated */
    protected JmlMethodDeclaration methodDecl;

    /** method body to be translated */
    protected JBlock body;
}
