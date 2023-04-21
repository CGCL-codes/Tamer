package org.jmlspecs.jml4.rac.quantifiedexpression;

import java.util.LinkedList;
import java.util.Map;
import org.eclipse.jdt.internal.compiler.ast.AND_AND_Expression;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.BinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.OperatorIds;
import org.eclipse.jdt.internal.compiler.lookup.BaseTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;
import org.eclipse.jdt.internal.compiler.lookup.VariableBinding;
import org.jmlspecs.jml4.ast.JmlQuantifiedExpression;
import org.jmlspecs.jml4.ast.JmlQuantifier;
import org.jmlspecs.jml4.rac.CodeBuffer;
import org.jmlspecs.jml4.rac.PostStateExpressionTranslator;
import org.jmlspecs.jml4.rac.RacResult;
import org.jmlspecs.jml4.rac.VariableGenerator;

public abstract class StaticAnalysis extends Translator {

    /**
	 * Constructs a <code>StaticAnalysis</code> object.
	 *
	 * @param varGen variable generator to be used in translation for
	 *               generating unique local variables.
	 * @param expr quantified expression to be translated.
	 * @param resultVar variable name to have the value of quantified
	 *                  expression in the translated code.
	 * @param transExp translator to use for translating subexpressions of
	 *                 the quantified expression.
	 * @param racResult data structure to store rac results
	 * @param typeBinding sourceType
	 */
    protected StaticAnalysis(VariableGenerator varGen, RacContext ctx, JmlQuantifiedExpression expr, String resultVar, PostStateExpressionTranslator transExp, RacResult racResult, SourceTypeBinding typeBinding) {
        super(varGen, ctx, expr, resultVar, transExp, racResult, typeBinding);
    }

    /**
	 * Returns an instance of <code>StaticAnalysis</code>, that
	 * translates JML quantified expressions into Java source code.
	 *
	 * @param varGen variable generator to be used in translation for
	 *               generating unique local variables.
	 * @param expr quantified expression to be translated.
	 * @param resultVar variable name to store the result value of quantified
	 *                  expression in the translated code.
	 * @param transExp translator to use for translating subexpressions.
	 * @param racResult data structure of rac results
	 * @param typeBinding sourceType 
	 */
    public static StaticAnalysis getInstance(VariableGenerator varGen, RacContext ctx, JmlQuantifiedExpression expr, String resultVar, PostStateExpressionTranslator transExp, RacResult racResult, SourceTypeBinding typeBinding) {
        TypeBinding type = expr.boundVariables[0].binding.type;
        if (type.id == TypeIds.T_int || type.id == TypeIds.T_long || type.id == TypeIds.T_byte || type.id == TypeIds.T_short || type.id == TypeIds.T_char) {
            return new IntervalBased(varGen, ctx, expr, resultVar, transExp, racResult, typeBinding);
        } else if (type.id == TypeIds.T_boolean) {
            return new EnumerationBased(varGen, ctx, expr, resultVar, transExp, racResult, typeBinding);
        } else {
            return new SetBased(varGen, ctx, expr, resultVar, transExp, racResult, typeBinding);
        }
    }

    /**
	 * Translate a JML quantified expression into Java source code 
	 * and return the result.
	 *
	 * @return translated Java source code.
	 * @throws <code>NonExecutableQuantifierException</code> if not executable.
	 */
    public String translate() throws NonExecutableQuantifierException {
        if (quantiExp.quantifier.lexeme.equals(JmlQuantifier.FORALL)) return translateForAll();
        if (quantiExp.quantifier.lexeme.equals(JmlQuantifier.EXISTS)) return translateExists();
        if (quantiExp.quantifier.lexeme.equals(JmlQuantifier.SUM)) return translateSum();
        if (quantiExp.quantifier.lexeme.equals(JmlQuantifier.PRODUCT)) return translateProduct();
        if (quantiExp.quantifier.lexeme.equals(JmlQuantifier.MIN)) return translateMin();
        if (quantiExp.quantifier.lexeme.equals(JmlQuantifier.MAX)) return translateMax();
        if (quantiExp.quantifier.lexeme.equals(JmlQuantifier.NUM_OF)) return translateNumOf();
        throw new NonExecutableQuantifierException();
    }

    /**
	 * Return Java source code for evaluating a JML quantified expression 
	 * of a univeral quantifier. 
	 * The returned code has the following general form:
	 *
	 * <pre>
	 *   [[(\forall T x1, ..., xn; P; Q), r]] =
	 *     r = true;
	 *     Collection c1 = null;
	 *     [[S, c1]]  // S is the qset of P ==> Q
	 *     Iterator iter1 = c1.iterator();
	 *     while (r &amp;&amp; iter1.hasNext()) {
	 *        T x1 = iter1.next();
	 *        ...
	 *        Collection cn = null;
	 *        [[S, cn]]
	 *        Iterator itern = cn.iterator();
	 *        while (r &amp;&amp; itern.hasNext()) {
	 *            T xn = itern.next();
	 *            [[P ==&gt; Q, r]]
	 *        }
	 *        ...
	 *     }
	 * </pre>
	 *
	 * If the type (<code>T</code>) of the quantified variables is 
	 * <code>byte</code>, <code>char</code>, <code>short</code>, 
	 * <code>int</code>, or <code>long</code>. The following rule is
	 * applied:
	 *
	 * <pre>
	 *   [[(\forall T x1, ..., xn; P; Q), r]] =
	 *     r = true;
	 *     T l1 = 0;
	 *     T u1 = 0;
	 *     [[l1 and u1 calculation from P]]
	 *     while (r &amp;&amp; l < u) {
	 *        T x1 = l1;
	 *        ...
	 *        T ln = 0;
	 *        T un = 0;
	 *        [[ln and un calculation from P]]
	 *        while (r &amp;&amp; ln < un) {
	 *            T xn = ln;
	 *            [[P ==&gt; Q, r]]
	 *            ln = ln + 1;
	 *        }
	 *        ...
	 *        l1 = l1 + 1;
	 *     }
	 * </pre>
	 *
	 * @throws <code>NonExecutableQuantifierException</code> if not executable.
	 *
	 */
    private String translateForAll() throws NonExecutableQuantifierException {
        return transForAllOrExists();
    }

    /**
	 * Return Java source code for evaluating a JML quantified expression 
	 * of an existential quantifier. 
	 * The returned code has the following general form:
	 *
	 * <pre>
	 *   [[(\exists T x1, ..., xn; P; Q), r]] =
	 *     r = false;
	 *     Collection c1 = null;
	 *     [[S, c1]]  // S is the qset of P ==> Q
	 *     Iterator iter1 = c1.iterator();
	 *     while (!r &amp;&amp; iter1.hasNext()) {
	 *        T x1 = iter1.next();
	 *        ...
	 *        Collection cn = null;
	 *        [[S, cn]]
	 *        Iterator itern = cn.iterator();
	 *        while (!r &amp;&amp; itern.hasNext()) {
	 *            T xn = itern.next();
	 *            [[P &amp;&amp; Q, r]]
	 *        }
	 *        ...
	 *     }
	 * </pre>
	 *
	 * If the type (<code>T</code>) of the quantified variables is 
	 * <code>byte</code>, <code>char</code>, <code>short</code>, 
	 * <code>int</code>, or <code>long</code>. The following rule is
	 * applied:
	 *
	 * <pre>
	 *   [[(\exists T x1, ..., xn; P; Q), r]] =
	 *     r = false;
	 *     T l1 = 0;
	 *     T u1 = 0;
	 *     [[l1 and u1 calculation from P]]
	 *     while (!r &amp;&amp; l < u) {
	 *        T x1 = l1;
	 *        ...
	 *        T ln = 0;
	 *        T un = 0;
	 *        [[ln and un calculation from P]]
	 *        while (!r &amp;&amp; ln < un) {
	 *            T xn = ln;
	 *            [[P &amp;&amp; Q, r]]
	 *            ln = ln + 1;
	 *        }
	 *        ...
	 *        l1 = l1 + 1;
	 *     }
	 * </pre>
	 *
	 * @throws <code>NonExecutableQuantifierException</code> if not executable.
	 *
	 */
    private String translateExists() throws NonExecutableQuantifierException {
        return transForAllOrExists();
    }

    /**
	 * Return Java source code for evaluating a JML quantified expression 
	 * of a generalized quantifier <code>\sum</code>.
	 * The returned code has the following general form:
	 *
	 * <pre>
	 *   [[(\sum T x1, ..., xn; P; Q), r]] =
	 *     r = 0;
	 *     Collection c1 = null;
	 *     [[S1, c1]]  // S1 is the qset of P for x1
	 *     Iterator iter1 = c1.iterator();
	 *     while (iter1.hasNext()) {
	 *        T x1 = iter1.next();
	 *        ...
	 *        Collection cn = null;
	 *        [[Sn, cn]] // Sn is the qset of P for xn
	 *        Iterator itern = cn.iterator();
	 *        while (itern.hasNext()) {
	 *            T xn = itern.next();
	 *            boolean v1 = false;
	 *            [[P, v1]]
	 *            if (v1) {
	 *               T_Q v2 = T_init;
	 *               [[Q, v2]]
	 *               resultVar = resultVar + v2;
	 *            }
	 *        }
	 *        ...
	 *     }
	 * </pre>
	 *
	 * If the type (<code>T</code>) of the quantified variables is 
	 * <code>byte</code>, <code>char</code>, <code>short</code>, 
	 * <code>int</code>, or <code>long</code>. The following rule is
	 * applied:
	 *
	 * <pre>
	 *   [[(\sum T x1, ..., xn; P; Q), r]] =
	 *     r = 0;
	 *     T l1 = 0;
	 *     T u1 = 0;
	 *     [[calculation of l1 and u1 from P]]
	 *     while (l1 < u1) {
	 *        T x1 = l1;
	 *        ...
	 *        T ln = 0;
	 *        T un = 0;
	 *        [[calculation of ln and un from P]]
	 *        while (ln < un) {
	 *            T xn = ln;
	 *            boolean v1 = false;
	 *            [[P, v1]]
	 *            if (v1) {
	 *               T_Q v2 = T_init;
	 *               [[Q, v2]]
	 *               resultVar = resultVar + v2;
	 *            }
	 *            ln = ln + 1;
	 *        }
	 *        ...
	 *        l1 = l1 + 1;
	 *     }
	 * </pre>
	 *
	 * @throws <code>NonExecutableQuantifierException</code> if not executable.
	 */
    private String translateSum() throws NonExecutableQuantifierException {
        return transSumOrProduct();
    }

    /**
	 * Return Java source code for evaluating a JML quantified expression 
	 * of a generalized quantifier <code>\product</code>.
	 * The returned code has the following general form:
	 *
	 * <pre>
	 *   [[(\product T x1, ..., xn; P; Q), r]] =
	 *     r = 1;
	 *     Collection c1 = null;
	 *     [[S1, c1]]  // S1 is the qset of P for x1
	 *     Iterator iter1 = c1.iterator();
	 *     while (iter1.hasNext()) {
	 *        T x1 = iter1.next();
	 *        ...
	 *        Collection cn = null;
	 *        [[Sn, cn]] // Sn is the qset of P for xn
	 *        Iterator itern = cn.iterator();
	 *        while (itern.hasNext()) {
	 *            T xn = itern.next();
	 *            boolean v1 = false;
	 *            [[P, v1]]
	 *            if (v1) {
	 *               T_Q v2 = T_init;
	 *               [[Q, v2]]
	 *               resultVar = resultVar * v2;
	 *            }
	 *        }
	 *        ...
	 *     }
	 * </pre>
	 *
	 * If the type (<code>T</code>) of the quantified variables is 
	 * <code>byte</code>, <code>char</code>, <code>short</code>, 
	 * <code>int</code>, or <code>long</code>. The following rule is
	 * applied:
	 *
	 * <pre>
	 *   [[(\product T x1, ..., xn; P; Q), r]] =
	 *     r = 0;
	 *     T l1 = 0;
	 *     T u1 = 0;
	 *     [[calculation of l1 and u1 from P]]
	 *     while (l1 < u1) {
	 *        T x1 = l1;
	 *        ...
	 *        T ln = 0;
	 *        T un = 0;
	 *        [[calculation of ln and un from P]]
	 *        while (ln < un) {
	 *            T xn = ln;
	 *            boolean v1 = false;
	 *            [[P, v1]]
	 *            if (v1) {
	 *               T_Q v2 = T_init;
	 *               [[Q, v2]]
	 *               resultVar = resultVar * v2;
	 *            }
	 *            ln = ln + 1;
	 *        }
	 *        ...
	 *        l1 = l1 + 1;
	 *     }
	 * </pre>
	 *
	 * @throws <code>NonExecutableQuantifierException</code> if not executable.
	 */
    private String translateProduct() throws NonExecutableQuantifierException {
        return transSumOrProduct();
    }

    /**
	 * Return Java source code for evaluating a JML quantified expression 
	 * of a generalized quantifier <code>\min</code>.
	 * The returned code has the following general form:
	 *
	 * <pre>
	 *   [[(\min T x1, ..., xn; P; Q), r]] =
	 *     r = Integer.MAX_VALUE;
	 *     Collection c1 = null;
	 *     [[S1, c1]]  // S1 is the qset of P for x1
	 *     Iterator iter1 = c1.iterator();
	 *     while (iter1.hasNext()) {
	 *        T x1 = iter1.next();
	 *        ...
	 *        Collection cn = null;
	 *        [[Sn, cn]] // Sn is the qset of P for xn
	 *        Iterator itern = cn.iterator();
	 *        while (itern.hasNext()) {
	 *            T xn = itern.next();
	 *            boolean v1 = false;
	 *            [[P, v1]]
	 *            if (v1) {
	 *               T_Q v2 = T_init;
	 *               [[Q, v2]]
	 *               resultVar = Math.min(resultVar, v2);
	 *            }
	 *        }
	 *        ...
	 *     }
	 * </pre>
	 *
	 * If the type (<code>T</code>) of the quantified variables is 
	 * <code>byte</code>, <code>char</code>, <code>short</code>, 
	 * <code>int</code>, or <code>long</code>. The following rule is
	 * applied:
	 *
	 * <pre>
	 *   [[(\max T x1, ..., xn; P; Q), r]] =
	 *     r = Integer.MAX_VALUE;
	 *     T l1 = 0;
	 *     T u1 = 0;
	 *     [[calculation of l1 and u1 from P]]
	 *     while (l1 < u1) {
	 *        T x1 = l1;
	 *        ...
	 *        T ln = 0;
	 *        T un = 0;
	 *        [[calculation of ln and un from P]]
	 *        while (ln < un) {
	 *            T xn = ln;
	 *            boolean v1 = false;
	 *            [[P, v1]]
	 *            if (v1) {
	 *               T_Q v2 = T_init;
	 *               [[Q, v2]]
	 *               resultVar = Math.min(resultVar, v2);
	 *            }
	 *            ln = ln + 1;
	 *        }
	 *        ...
	 *        l1 = l1 + 1;
	 *     }
	 * </pre>
	 *
	 * @throws <code>NonExecutableQuantifierException</code> if not executable.
	 */
    private String translateMin() throws NonExecutableQuantifierException {
        return transMinOrMax();
    }

    /**
	 * Return Java source code for evaluating a JML quantified expression 
	 * of a generalized quantifier <code>\max</code>.
	 * The returned code has the following general form:
	 *
	 * <pre>
	 *   [[(\max T x1, ..., xn; P; Q), r]] =
	 *     r = Integer.MIN_VALUE;
	 *     Collection c1 = null;
	 *     [[S1, c1]]  // S1 is the qset of P for x1
	 *     Iterator iter1 = c1.iterator();
	 *     while (iter1.hasNext()) {
	 *        T x1 = iter1.next();
	 *        ...
	 *        Collection cn = null;
	 *        [[Sn, cn]] // Sn is the qset of P for xn
	 *        Iterator itern = cn.iterator();
	 *        while (itern.hasNext()) {
	 *            T xn = itern.next();
	 *            boolean v1 = false;
	 *            [[P, v1]]
	 *            if (v1) {
	 *               T_Q v2 = T_init;
	 *               [[Q, v2]]
	 *               resultVar = Math.max(resultVar, v2);
	 *            }
	 *        }
	 *        ...
	 *     }
	 * </pre>
	 *
	 * If the type (<code>T</code>) of the quantified variables is 
	 * <code>byte</code>, <code>char</code>, <code>short</code>, 
	 * <code>int</code>, or <code>long</code>. The following rule is
	 * applied:
	 *
	 * <pre>
	 *   [[(\max T x1, ..., xn; P; Q), r]] =
	 *     r = Integer.MIN_VALUE;
	 *     T l1 = 0;
	 *     T u1 = 0;
	 *     [[calculation of l1 and u1 from P]]
	 *     while (l1 < u1) {
	 *        T x1 = l1;
	 *        ...
	 *        T ln = 0;
	 *        T un = 0;
	 *        [[calculation of ln and un from P]]
	 *        while (ln < un) {
	 *            T xn = ln;
	 *            boolean v1 = false;
	 *            [[P, v1]]
	 *            if (v1) {
	 *               T_Q v2 = T_init;
	 *               [[Q, v2]]
	 *               resultVar = Math.max(resultVar, v2);
	 *            }
	 *            ln = ln + 1;
	 *        }
	 *        ...
	 *        l1 = l1 + 1;
	 *     }
	 * </pre>
	 *
	 * @throws <code>NonExecutableQuantifierException</code> if not executable.
	 */
    private String translateMax() throws NonExecutableQuantifierException {
        return transMinOrMax();
    }

    /**
	 * Returns code for evaluating numerical quantifiers<code>\num_of</code>.
	 * The returned code has the following general form:
	 *
	 * <pre>
	 *   [[(\num_of T x1, ..., xn; P; Q), r]] =
	 *     r = 0;
	 *     Collection c1 = null;
	 *     [[S1, c1]]  // S1 is the qset of P for x1
	 *     Iterator iter1 = c1.iterator();
	 *     while (iter1.hasNext()) {
	 *        T x1 = iter1.next();
	 *        ...
	 *        Collection cn = null;
	 *        [[Sn, cn]] // Sn is the qset of P for xn
	 *        Iterator itern = cn.iterator();
	 *        while (itern.hasNext()) {
	 *            T xn = itern.next();
	 *            boolean v1 = false;
	 *            [[P, v1]]
	 *            boolean v2 = false;
	 *            [[Q, v2]]
	 *            if (v1 && v2) {
	 *               resultVar++;
	 *            }
	 *        }
	 *        ...
	 *     }
	 * </pre>
	 *
	 * If the type (<code>T</code>) of the quantified variables is 
	 * <code>byte</code>, <code>char</code>, <code>short</code>, 
	 * <code>int</code>, or <code>long</code>. The following rule is
	 * applied:
	 *
	 * <pre>
	 *   [[(\num_of T x1, ..., xn; P; Q), r]] =
	 *     r = 0;
	 *     T l1 = 0;
	 *     T u1 = 0;
	 *     [[calculation of l1 and u1 from P]]
	 *     while (l1 < u1) {
	 *        T x1 = l1;
	 *        ...
	 *        T ln = 0;
	 *        T un = 0;
	 *        [[calculation of ln and un from P]]
	 *        while (ln < un) {
	 *            T xn = ln;
	 *            boolean v1 = false;
	 *            [[P, v1]]
	 *            boolean v2 = false;
	 *            [[Q, v2]]
	 *            if (v1 && v2) {
	 *               resultVar++;
	 *            }
	 *            ln = ln + 1;
	 *        }
	 *        ...
	 *        l1 = l1 + 1;
	 *     }
	 * </pre>
	 *
	 * @throws <code>NonExecutableQuantifierException</code> if not executable.
	 */
    private String translateNumOf() throws NonExecutableQuantifierException {
        String result = null;
        final Expression pred = quantiExp.body;
        {
            String v1 = varGen.freshVar();
            String c1 = transExp.translate(quantiExp, pred, typeBinding, this.racResult);
            String v2 = varGen.freshVar();
            String c2 = transExp.translate(quantiExp, quantiExp.range, typeBinding, this.racResult);
            ;
            result = "boolean " + v1 + " = false;\n" + v1 + " = " + c1 + ";\n" + "if (" + v1 + ") {\n" + "  boolean " + v2 + " = false;\n" + v2 + " = " + c2 + ";\n" + "  if (" + v2 + ") {\n" + "    " + resultVar + "++;\n" + "  }\n" + "}";
        }
        LocalDeclaration[] varDefs = quantiExp.boundVariables;
        for (int i = varDefs.length - 1; i >= 0; i--) {
            result = generateLoop(varDefs[i], rangePredicate(), null, result);
        }
        return "{ // from num_of expression\n" + "  " + resultVar + " = 0;\n" + result + "\n" + "}";
    }

    /** Returns Java source code for evaluating the JML quantified
	 * expression, which is either a universal or existential
	 * quantifier.  Refer to the method <code>translateForAll</code>
	 * and <code>translateExists</code> for the structure of the
	 * returned code.
	 *
	 * <pre><jml>
	 * requires quantiExp.isForAll() || quantiExp.isExists();
	 * ensures \result != null;
	 * </jml></pre>
	 *
	 * @see #translateForAll()
	 * @see #translateExists()
	 */
    private String transForAllOrExists() throws NonExecutableQuantifierException {
        final boolean isForAll = quantiExp.quantifier.lexeme.equals(JmlQuantifier.FORALL);
        final String cond = (isForAll ? "" : "!") + resultVar;
        final String initVal = isForAll ? "true" : "false";
        Expression expr = quantiExp.body;
        final Expression pred = quantiExp.range;
        if (pred != null) {
            if (isForAll) expr = new BinaryExpression(pred, expr, OperatorIds.JML_IMPLIES); else expr = new AND_AND_Expression(pred, expr, OperatorIds.AND_AND);
        }
        String result = transExp.translate(quantiExp, expr, typeBinding, this.racResult);
        result = result.concat(";");
        String tcond = cond;
        char[] condition = tcond.toCharArray();
        if (condition[0] == '!') tcond = tcond.substring(1);
        result = tcond.concat(" = ").concat(result);
        Map<ASTNode, VariableBinding> localTerms = transExp.terms;
        LocalDeclaration[] varDefs = quantiExp.boundVariables;
        for (int i = varDefs.length - 1; i >= 0; i--) {
            result = generateLoop(varDefs[i], rangePredicate(), cond, result);
        }
        transExp.terms = localTerms;
        return result;
    }

    /**
	 * Returns the range predicate of the quantified expression. The
	 * result is the conjuntion of explicit and implicit range
	 * predicates. The implicit range (e.g., p in (\forall d; r; p ==>
	 * q) and (\exists d; r; p && q)) is extracted from the predicate
	 * part of the quantified expressions and conjoined to the
	 * explicit range predicate (r).
	 *
	 * <pre><jml>
	 * requires quantiExp.isForAll() || quantiExp.isExists();
	 * ensures (* \result maybe null *);
	 * </jml></pre>
	 *
	 * @see #transForAllOrExists()
	 */
    private Expression rangePredicate() {
        Expression erange = quantiExp.range;
        Expression irange = null;
        Expression expr = quantiExp.body;
        int operator = (expr.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT;
        if (quantiExp.quantifier.lexeme.equals(JmlQuantifier.FORALL) && (expr instanceof BinaryExpression) && operator == OperatorIds.JML_IMPLIES) {
            irange = ((BinaryExpression) expr).left;
        } else if ((quantiExp.quantifier.lexeme.equals(JmlQuantifier.EXISTS) || quantiExp.quantifier.lexeme.equals(JmlQuantifier.NUM_OF)) && (expr instanceof AND_AND_Expression)) {
            irange = ((AND_AND_Expression) expr).left;
        }
        if (erange == null) {
            return irange;
        }
        if (irange == null) {
            return erange;
        }
        return new AND_AND_Expression(erange, irange, OperatorIds.AND_AND);
    }

    /**
	 * Unwraps a given JML expression if it is an instance of
	 * JmlPredicate or JmlSpecExpression.
	 */
    private static Expression unwrapJmlExpression(Expression expr) {
        return null;
    }

    /** Returns Java source code for evaluating the JML quantified 
	 * expression, which is a generalized quantifier <code>\sum</code>
	 * or <code>\product</code>.
	 *
	 * <pre><jml>
	 * requires quantiExp.isSum() || quantiExp.isProduct();
	 * </jml></pre>
	 *
	 * @see #translateProduct()
	 * @see #translateSum()
	 */
    private String transSumOrProduct() throws NonExecutableQuantifierException {
        final boolean isSum = quantiExp.quantifier.lexeme.equals(JmlQuantifier.SUM);
        final String sumOpr = isSum ? " + " : " * ";
        final String initVal = isSum ? "0" : "1";
        final String initBigintVal = isSum ? "java.math.BigInteger.ZERO" : "java.math.BigInteger.ONE";
        String result = null;
        final Expression pred = quantiExp.range;
        final Expression specExpr = quantiExp.body;
        {
            String v1 = varGen.freshVar();
            String c1 = transExp.translate(quantiExp, pred, typeBinding, this.racResult);
            String v2 = varGen.freshVar();
            String c2 = transExp.translate(quantiExp, specExpr, typeBinding, this.racResult);
            result = "boolean " + v1 + " = false;\n" + v1 + " = " + c1 + ";\n" + "if (" + v1 + ") {\n" + "  " + specExpr.resolvedType.debugName() + " " + v2 + " = 0;\n" + v2 + " = " + c2 + ";\n" + "  " + resultVar + " = " + applySumOrProduct(resultVar, sumOpr, v2, specExpr.resolvedType) + ";\n" + "}";
        }
        LocalDeclaration[] varDefs = quantiExp.boundVariables;
        for (int i = varDefs.length - 1; i >= 0; i--) {
            result = generateLoop(varDefs[i], pred, null, result);
        }
        return "{ // from " + (isSum ? "sum" : "product") + " expression\n" + "  " + resultVar + " = " + initVal + ";\n" + result + "\n" + "}";
    }

    private String applySumOrProduct(String strResultVar, String strOptr, String strV2, TypeBinding vType) {
        String strRet;
        {
            if (vType.id == TypeIds.T_byte) strRet = "(byte)(" + strResultVar + strOptr + strV2 + ")"; else if (vType.id == TypeIds.T_short) strRet = "(short)(" + strResultVar + strOptr + strV2 + ")"; else if (vType.id == TypeIds.T_char) strRet = "(char)(" + strResultVar + strOptr + strV2 + ")"; else strRet = strResultVar + strOptr + strV2;
        }
        return strRet;
    }

    /** Returns Java source code for evaluating the JML quantified 
	 * expression, which is a generalized quantifier <code>\min</code>
	 * or <code>\max</code>.
	 *
	 * <pre><jml>
	 * requires quantiExp.isMin() || quantiExp.isMax();
	 * </jml></pre>
	 *
	 * @see #translateMax()
	 * @see #translateMin()
	 */
    private String transMinOrMax() throws NonExecutableQuantifierException {
        final boolean isMin = quantiExp.quantifier.lexeme.equals(JmlQuantifier.MIN);
        final String mathMeth = isMin ? "min" : "max";
        final Expression specExpr = quantiExp.body;
        String result = null;
        final Expression pred = quantiExp.range;
        {
            String v1 = varGen.freshVar();
            String c1 = transExp.translate(quantiExp, pred, typeBinding, this.racResult);
            String v2 = varGen.freshVar();
            String c2 = transExp.translate(quantiExp, specExpr, typeBinding, this.racResult);
            String neededCast = "";
            neededCast = "(" + specExpr.resolvedType.debugName() + ") ";
            result = "boolean " + v1 + " = false;\n" + v1 + " = " + c1 + ";\n" + "if (" + v1 + ") {\n" + "  " + specExpr.resolvedType.debugName() + " " + v2 + " = 0;\n" + v2 + " = " + c2 + ";\n" + "  if(bFirstCompare) {\n" + "  " + resultVar + " = " + v2 + ";\n" + "  } else {\n" + "  " + resultVar + " = " + neededCast + "java.lang.Math." + mathMeth + "(" + resultVar + ", " + v2 + ")" + ";\n" + "  }\n" + "  bFirstCompare = false;\n" + "}";
        }
        LocalDeclaration[] varDefs = quantiExp.boundVariables;
        for (int i = varDefs.length - 1; i >= 0; i--) {
            result = generateLoop(varDefs[i], pred, null, result);
        }
        return "{ // from " + (isMin ? "min" : "max") + " expression\n" + "  boolean bFirstCompare = true;\n" + result + "\n" + "}";
    }

    /**
	 * Returns a loop code that evaluates the given body with the 
	 * quantified variable bound to each possible value of the range.
	 * For example, the interval-based approach returns the following
	 * form of code:
	 *
	 * <pre>
	 *  { 
	 *    T l = 0;
	 *    T u = 0;
	 *    [[eval of l and u from predicate part]]
	 *    while (l < u) {
	 *      T x = l;
	 *      result
	 *      l = l + 1;
	 *    }
	 *  }
	 * </pre>
	 * where <code>x</code> is the quantified variable.
	 *
	 * @param body code to be executed as the loop body
	 * @exception NonExecutableQuantifierException if not executable.
	 */
    public String generateLoop(ASTNode body) throws NonExecutableQuantifierException {
        String result = body.toString();
        Expression pred = null;
        final boolean isForAll = quantiExp.quantifier.lexeme.equals(JmlQuantifier.FORALL);
        final boolean isExists = quantiExp.quantifier.lexeme.equals(JmlQuantifier.EXISTS);
        if (isForAll || isExists) {
            pred = rangePredicate();
        } else {
            pred = quantiExp.range;
        }
        LocalDeclaration[] varDefs = quantiExp.boundVariables;
        for (int i = varDefs.length - 1; i >= 0; i--) {
            result = generateLoop(varDefs[i], pred, null, result);
        }
        CodeBuffer code = new CodeBuffer();
        code.append("{ // from quantified expression\n" + "%1\n" + "}", result);
        return code.toString();
    }

    /**
	 * Returns a loop code evaluating the body of the quantified predicate.
	 *
	 * @param varDef quantified variable
	 * @param qexpr target expression for interval calculation (i.e., 
	 *              normally, the predicate part of the quantified
	 *              expression)
	 * @param cond additional condition to be conjoined (&amp;&amp;)
	 *             to the loop condition; <code>null</code> for no 
	 *             conjoinable condition.
	 * @param result code for evaluating expression part (or inner loop)
	 *
	 * @throws <code>NonExecutableQuantifierException</code> if not executable.
	 */
    protected abstract String generateLoop(LocalDeclaration varDef, Expression qexpr, String cond, String result) throws NonExecutableQuantifierException;

    /**
	 * A concrete class for translating JML quantified expressions 
	 * into Java source code. The translation is based on the 
	 * set-based static analysis of the expression's structures.
	 */
    private static class SetBased extends StaticAnalysis {

        /**
		 * Constructs a <code>SetBased</code> object.
		 *
		 * @param varGen variable generator to be used in translation for
		 *               generating unique local variables.
		 * @param expr quantified expression to be translated.
		 * @param resultVar variable name to have the value of quantified
		 *                  expression in the translated code.
		 * @param transExp translator to use for translating subexpressions of
		 *                 the quantified expression.
		 * @param racResult data structure for rac result
		 * @param typeBinding sourceType
		 */
        SetBased(VariableGenerator varGen, RacContext ctx, JmlQuantifiedExpression expr, String resultVar, PostStateExpressionTranslator transExp, RacResult racResult, SourceTypeBinding typeBinding) {
            super(varGen, ctx, expr, resultVar, transExp, racResult, typeBinding);
        }

        /**
		 * Returns Java source code for evaluating quantified expressions
		 * using the QSet-based translation.
		 * The returned code has the following general form:
		 *
		 * <pre>
		 *  java.util.Collection c = new java.util.HashSet();
		 *  [[eval of c from qexpr]]
		 *  java.util.Iterator iter = c.iterator();
		 *  while ([condr &&] c.hasNext()) {
		 *    T x = iter.next();
		 *    result
		 *  }
		 * </pre>
		 *
		 * where <code>x</code> is the quantified variable of 
		 * type <code>T</code>.
		 *
		 * @param varDef quantified variable
		 * @param qexpr target expression for interval calculation (i.e., 
		 *              normally, the predicate part of the quantified
		 *              expression)
		 * @param cond additional condition to be conjoined (&amp;&amp;)
		 *             to the loop condition; <code>null</code> for no extra
		 *             condition.
		 * @param result code for evaluating expression part (or inner loop)
		 * @throws NonExecutableQuantifierException if not executable.
		 */
        protected String generateLoop(LocalDeclaration varDef, Expression qexpr, String cond, String result) throws NonExecutableQuantifierException {
            String type = String.valueOf(varDef.type);
            String ident = String.valueOf(varDef.name);
            String cvar = varGen.freshVar();
            String ivar = varGen.freshVar();
            String nested_quantifier = null;
            if (!transExp.localclassTransformation.isEmpty()) nested_quantifier = transExp.localclassTransformation.get(0);
            QSet qset = QSet.build(qexpr, ident);
            String qcode = qset.translate(varGen, cvar, transExp);
            StringBuffer code = new StringBuffer();
            code.append("java.util.Collection " + cvar + " = new java.util.HashSet();\n");
            code.append(qcode + "\n");
            code.append("java.util.Iterator " + ivar + " = " + cvar + ".iterator();\n");
            if (!(cond == null || cond.equals(""))) {
                char[] condition = cond.toCharArray();
                if (condition[0] == '!') {
                    code.append(String.copyValueOf(condition, 1, cond.length() - 1) + " = false;\n");
                } else {
                    code.append(cond + " = true;\n");
                }
            }
            code.append("while (");
            if (!(cond == null || cond.equals(""))) code.append(cond + " && ");
            code.append(ivar + ".hasNext()) {\n");
            code.append("  " + type + " " + ident + " = (" + type + ")" + ivar + ".next();\n");
            if (nested_quantifier != null) code.append(nested_quantifier);
            code.append(result + "\n");
            code.append("}");
            return code.toString();
        }
    }

    /**
	 * A concrete class for translating JML quantified expressions 
	 * into Java source code. The translation is based on the 
	 * interval-based static analysis of the expression's structures.
	 * 
	 * <pre><jml>
	 * invariant quantiExp.quantifiedVarDecls()[0].getType().isOrdinal();
	 * </jml></pre>
	 */
    private static class IntervalBased extends StaticAnalysis {

        /**
		 * Constructs a <code>IntervalBased</code> object.
		 *
		 * @param varGen variable generator to be used in translation for
		 *               generating unique local variables.
		 * @param expr quantified expression to be translated.
		 * @param resultVar variable name to have the value of quantified
		 *                  expression in the translated code.
		 * @param transExp translator to use for translating subexpressions of
		 *                 the quantified expression.
		 * <pre><jml>
		 * requires expr.quantifiedVarDecls()[0].getType().isOrdinal();
		 * </jml></pre>
		 * @param racResult data structure to store rac results
		 * @param typeBinding 
		 */
        IntervalBased(VariableGenerator varGen, RacContext ctx, JmlQuantifiedExpression expr, String resultVar, PostStateExpressionTranslator transExp, RacResult racResult, SourceTypeBinding typeBinding) {
            super(varGen, ctx, expr, resultVar, transExp, racResult, typeBinding);
        }

        /**
		 * Returns a loop code for evaluating quantified expressions using 
		 * the interval-based analysis. The returned code has the following
		 * general form:
		 *
		 * <pre>
		 *  T l = 0;
		 *  T u = 0;
		 *  [[eval of l and u from pred]]
		 *  while ([cond &&] l < u) {
		 *    T x = l;
		 *    result
		 *    l = l + 1;
		 *  }
		 * </pre>
		 *
		 * where <code>x</code> is the quantified variable of type
		 * <code>T</code>.
		 *
		 * @param varDef quantified variable
		 * @param pred target expression for interval calculation (i.e., 
		 *             normally, the predicate part of the quantified 
		 *             expression)
		 * @param cond additional condition to be conjoined (&amp;&amp;)
		 *             to the loop condition; <code>null</code> for no extra
		 *             condition.
		 * @param result code for evaluating expression part (or inner loop)
		 * @throws NonExecutableQuantifierException if not executable.
		 *
		 * <pre><jml>
		 * also
		 *   requires varDef.getType().isOrdinal();
		 * </jml></pre>
		 */
        protected String generateLoop(LocalDeclaration varDef, Expression pred, String cond, String result) throws NonExecutableQuantifierException {
            final String type = varDef.type.toString();
            final int tid = varDef.type.resolvedType.id;
            final String ident = String.valueOf(varDef.name);
            final boolean separateLoopVar = !(tid == TypeIds.T_int || tid == TypeIds.T_long);
            QInterval interval = null;
            {
                LocalDeclaration[] vdefs = quantiExp.boundVariables;
                LinkedList xvars = new LinkedList();
                for (int i = vdefs.length - 1; i >= 0; i--) {
                    xvars.add(String.valueOf(vdefs[i].name));
                    if (ident.equals(String.valueOf(vdefs[i].name))) break;
                }
                interval = new QInterval(pred, ident, xvars, (tid == BaseTypeBinding.LONG.id ? BaseTypeBinding.LONG : BaseTypeBinding.INT), racResult, typeBinding);
            }
            String lvar = separateLoopVar ? varGen.freshVar() : ident;
            String uvar = varGen.freshVar();
            String nested_quantifier = null;
            if (!transExp.localclassTransformation.isEmpty()) nested_quantifier = transExp.localclassTransformation.get(0);
            String qcode = interval.translate(varGen, lvar, uvar, transExp);
            StringBuffer code = new StringBuffer();
            if (tid == TypeBinding.LONG.id) {
                code.append("long " + lvar + " = 0L;\n");
                code.append("long " + uvar + " = 0L;\n");
            } else {
                code.append("int " + lvar + " = 0;\n");
                code.append("int " + uvar + " = 0;\n");
            }
            code.append(qcode + "\n");
            if (!(cond == null || cond.equals(""))) {
                char[] condition = cond.toCharArray();
                if (condition[0] == '!') {
                    code.append(String.copyValueOf(condition, 1, cond.length() - 1) + " = false;\n");
                } else {
                    code.append(cond + " = true;\n");
                }
            }
            code.append("while (");
            if (!(cond == null || cond.equals(""))) code.append(cond + " && ");
            {
                code.append(lvar + " < " + uvar + ") {\n");
            }
            if (separateLoopVar) code.append("  " + type + " " + ident + " = (" + type + ") " + lvar + ";\n");
            if (nested_quantifier != null) code.append(nested_quantifier);
            code.append(result + "\n");
            {
                code.append("  " + lvar + " = " + lvar + " + 1;\n");
            }
            code.append("}");
            return code.toString();
        }
    }

    /**
	 * A concrete class for translating JML quantified expressions 
	 * into Java source code. The translation is based on an explicit
	 * enumeration of all possible values for the quantified variable.
	 * A quantified variable of <code>boolean</code> is implemented in
	 * this way.
	 *
	 * <pre><jml>
	 *  protected invariant quantiExp.quantifiedVarDecls()[0].getType() 
	 *    == JmlStdType.Boolean;
	 * </jml></pre>
	 */
    private static class EnumerationBased extends StaticAnalysis {

        /**
		 * Constructs a <code>EnumerationBased</code> object.
		 *
		 * @param varGen variable generator to be used in translation for
		 *               generating unique local variables.
		 * @param expr quantified expression to be translated.
		 * @param resultVar variable name to have the value of quantified
		 *                  expression in the translated code.
		 * @param transExp translator to use for translating subexpressions of
		 *                 the quantified expression.
		 * <pre><jml>
		 * requires expr.quantifiedVarDecls()[0].getType() == 
		 *   JmlStdType.Boolean;
		 * </jml></pre>
		 * @param racResult data structure to store rac results
		 * @param typeBinding sourceType
		 */
        EnumerationBased(VariableGenerator varGen, RacContext ctx, JmlQuantifiedExpression expr, String resultVar, PostStateExpressionTranslator transExp, RacResult racResult, SourceTypeBinding typeBinding) {
            super(varGen, ctx, expr, resultVar, transExp, racResult, typeBinding);
        }

        /**
		 * Returns Java source code for evaluating quantified expressions
		 * using the QSet-based translation.
		 * The returned code has the following general form:
		 *
		 * <pre>
		 *  boolean[] v = { false, true };
		 *  for (int i = 0; [cond &&] i < v.length; i++) {
		 *    bool x = v[i];
		 *    result
		 *  }
		 * </pre>
		 *
		 * where <code>x</code> is the quantified variable and 
		 * <code>v</code> and <code>i</code> are fresh variables.
		 *
		 * @param varDef quantified variable
		 * @param qexpr target expression for interval calculation (i.e., 
		 *              normally, the predicate part of the quantified
		 *              expression)
		 * @param cond additional condition to be conjoined (&amp;&amp;)
		 *             to the loop condition; <code>null</code> for no extra
		 *             condition.
		 * @param result code for evaluating expression part (or inner loop)
		 * @throws NonExecutableQuantifierException if not executable.
		 */
        protected String generateLoop(LocalDeclaration varDef, Expression qexpr, String cond, String result) throws NonExecutableQuantifierException {
            String ident = String.valueOf(varDef.name);
            String cvar = varGen.freshVar();
            String ivar = varGen.freshVar();
            String nested_quantifier = null;
            if (!transExp.localclassTransformation.isEmpty()) nested_quantifier = transExp.localclassTransformation.get(0);
            StringBuffer code = new StringBuffer();
            code.append("boolean[] " + cvar + " = { false, true };\n");
            code.append("for (int " + ivar + " = 0; ");
            if (!(cond == null || cond.equals(""))) code.append(cond + " && ");
            code.append(ivar + " < " + cvar + ".length; ");
            code.append(ivar + "++) {\n");
            code.append("  boolean " + ident + " = " + cvar + "[" + ivar + "];\n");
            if (nested_quantifier != null) code.append(nested_quantifier);
            code.append(result);
            code.append("\n}");
            return code.toString();
        }
    }
}
