package org.jmlspecs.jml4.rac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.AND_AND_Expression;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.ArrayAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.ArrayInitializer;
import org.eclipse.jdt.internal.compiler.ast.ArrayQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ArrayReference;
import org.eclipse.jdt.internal.compiler.ast.ArrayTypeReference;
import org.eclipse.jdt.internal.compiler.ast.BinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.CharLiteral;
import org.eclipse.jdt.internal.compiler.ast.ClassLiteralAccess;
import org.eclipse.jdt.internal.compiler.ast.CombinedBinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.DoubleLiteral;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.ExtendedStringLiteral;
import org.eclipse.jdt.internal.compiler.ast.FalseLiteral;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.FloatLiteral;
import org.eclipse.jdt.internal.compiler.ast.InstanceOfExpression;
import org.eclipse.jdt.internal.compiler.ast.IntLiteral;
import org.eclipse.jdt.internal.compiler.ast.LongLiteral;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.OR_OR_Expression;
import org.eclipse.jdt.internal.compiler.ast.OperatorIds;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedSingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.StringLiteral;
import org.eclipse.jdt.internal.compiler.ast.SuperReference;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TrueLiteral;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.ast.UnaryExpression;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.ExtraCompilerModifiers;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.VariableBinding;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.jmlspecs.jml4.ast.JmlArrayQualifiedTypeReference;
import org.jmlspecs.jml4.ast.JmlCastExpression;
import org.jmlspecs.jml4.ast.JmlCastExpressionWithoutType;
import org.jmlspecs.jml4.ast.JmlConditionalExpression;
import org.jmlspecs.jml4.ast.JmlElemtypeExpression;
import org.jmlspecs.jml4.ast.JmlFreshExpression;
import org.jmlspecs.jml4.ast.JmlInformalExpression;
import org.jmlspecs.jml4.ast.JmlKeywordExpression;
import org.jmlspecs.jml4.ast.JmlMessageSend;
import org.jmlspecs.jml4.ast.JmlModifier;
import org.jmlspecs.jml4.ast.JmlQuantifiedExpression;
import org.jmlspecs.jml4.ast.JmlResultReference;
import org.jmlspecs.jml4.ast.JmlSetComprehension;
import org.jmlspecs.jml4.ast.JmlSubtypeExpression;
import org.jmlspecs.jml4.ast.JmlTypeExpression;
import org.jmlspecs.jml4.ast.JmlTypeofExpression;
import org.jmlspecs.jml4.ast.JmlUnaryExpression;
import org.jmlspecs.jml4.rac.RacConstants.Condition;
import org.jmlspecs.jml4.rac.quantifiedexpression.QuantifiedExpressionTranslator;

/**
 * Translates various Java and JML expressions to RAC code. The translated code is also an expression; perhaps, this
 * approach may not work for complex JML expressions such as quantifiers which are currently not supported.
 * 
 * @author Amritam Sarcar and Yoonsik Cheon
 */
@SuppressWarnings("nls")
public class ExpressionTranslator {

    private static String unsupportedExprMessage(final String jmlFragment, final String value) {
        final String msg = "RAC: JML " + jmlFragment + " is not supported yet; " + "for now, it is being translated to " + value;
        return msg;
    }

    private static void warnUnsupported(final String expr, final String value) {
        final String msg = "WARNING: " + ExpressionTranslator.unsupportedExprMessage(expr, value);
        System.err.println(msg);
    }

    /** OldExpressions in quantified expressions */
    protected List<RacOldExpression> oldExpressionsInQuantifiers;

    /** Type of which expression is being translated. */
    protected SourceTypeBinding typeBinding;

    /**
	 * Terms of which values are to be reported upon an assertion violation.
	 */
    public Map<ASTNode, VariableBinding> terms;

    /**
	 * Contains auxiliary information such as whether an inherited ghost field was referenced.
	 */
    protected RacResult racResult;

    /** To generate unique local variables in a method scope. */
    private final VariableGenerator varGenerator;

    /** To generate class name and method name for quantified expressions */
    protected String evaluatorPUse = "";

    protected String evaluatorPDef = "";

    /** To generate local variable name and binding in quantified expressions */
    protected String localVars = "";

    protected String localBinds = "";

    /** Flag to make top level translation to true **/
    private boolean isQuantifiedExpressionNonExecutable;

    public List<String> localclassTransformation;

    protected boolean isQuantifiedExpressionTranslation;

    protected JmlQuantifiedExpression quantifiedExpression;

    protected List<JmlQuantifiedExpression> nestedQuantifiers;

    /** The expression that threw nonExecutablException */
    protected Expression thrownExpression;

    protected final ProblemReporter problemReporter;

    public ExpressionTranslator(final VariableGenerator varGenerator, final boolean isOn, final ProblemReporter problemReporter) {
        this.varGenerator = varGenerator;
        this.isQuantifiedExpressionTranslation = isOn;
        this.nestedQuantifiers = new ArrayList<JmlQuantifiedExpression>();
        this.oldExpressionsInQuantifiers = new ArrayList<RacOldExpression>();
        this.terms = new HashMap<ASTNode, VariableBinding>();
        this.problemReporter = problemReporter;
    }

    public ExpressionTranslator(final VariableGenerator varGenerator, final ProblemReporter problemReporter) {
        this.varGenerator = varGenerator;
        this.isQuantifiedExpressionTranslation = false;
        this.oldExpressionsInQuantifiers = new ArrayList<RacOldExpression>();
        this.terms = new HashMap<ASTNode, VariableBinding>();
        this.nestedQuantifiers = new ArrayList<JmlQuantifiedExpression>();
        this.problemReporter = problemReporter;
    }

    /**
	 * Dispatches the given expression to an appropriate translation method based on its dynamic (runtime) type. As some
	 * of JML node types are subclasses of the corresponding Java node types, JML node types are dispatched first.
	 */
    protected String dispatch(final Expression expr) throws NonExecutableException {
        if (expr == null) {
            return RacConstants.EMPTY_STRING;
        }
        if (expr instanceof AllocationExpression) {
            return translateExpression((AllocationExpression) expr);
        } else if (expr instanceof ArrayReference) {
            return translateExpression((ArrayReference) expr);
        } else if (expr instanceof ArrayTypeReference) {
            return translateExpression((ArrayTypeReference) expr);
        } else if (expr instanceof JmlArrayQualifiedTypeReference) {
            return translateExpression((JmlArrayQualifiedTypeReference) expr);
        } else if (expr instanceof JmlCastExpression) {
            return translateExpression((JmlCastExpression) expr);
        } else if (expr instanceof JmlCastExpressionWithoutType) {
            return translateExpression((JmlCastExpressionWithoutType) expr);
        } else if (expr instanceof JmlConditionalExpression) {
            return translateExpression((JmlConditionalExpression) expr);
        } else if (expr instanceof JmlElemtypeExpression) {
            return translateExpression((JmlElemtypeExpression) expr);
        } else if (expr instanceof JmlKeywordExpression) {
            return translateExpression((JmlKeywordExpression) expr);
        } else if (expr instanceof FieldReference) {
            return translateExpression((FieldReference) expr);
        } else if (expr instanceof JmlFreshExpression) {
            return translateExpression((JmlFreshExpression) expr);
        } else if (expr instanceof JmlInformalExpression) {
            return translateExpression((JmlInformalExpression) expr);
        } else if (expr instanceof JmlMessageSend) {
            return translateExpression((JmlMessageSend) expr);
        } else if (expr instanceof ParameterizedQualifiedTypeReference) {
            return translateExpression((ParameterizedQualifiedTypeReference) expr);
        } else if (expr instanceof ParameterizedSingleTypeReference) {
            return translateExpression((ParameterizedSingleTypeReference) expr);
        } else if (expr instanceof QualifiedNameReference) {
            return translateExpression((QualifiedNameReference) expr);
        } else if (expr instanceof QualifiedTypeReference) {
            return translateExpression((QualifiedTypeReference) expr);
        } else if (expr instanceof JmlQuantifiedExpression) {
            return translateExpression((JmlQuantifiedExpression) expr);
        } else if (expr instanceof JmlResultReference) {
            return translateExpression((JmlResultReference) expr);
        } else if (expr instanceof SingleNameReference) {
            return translateExpression((SingleNameReference) expr);
        } else if (expr instanceof SingleTypeReference) {
            return translateExpression((SingleTypeReference) expr);
        } else if (expr instanceof JmlSubtypeExpression) {
            return translateExpression((JmlSubtypeExpression) expr);
        } else if (expr instanceof JmlTypeExpression) {
            return translateExpression((JmlTypeExpression) expr);
        } else if (expr instanceof JmlTypeofExpression) {
            return translateExpression((JmlTypeofExpression) expr);
        } else if (expr instanceof JmlUnaryExpression) {
            return translateExpression((JmlUnaryExpression) expr);
        } else if (expr instanceof AND_AND_Expression) {
            return this.translateExpression((AND_AND_Expression) expr);
        } else if (expr instanceof ArrayAllocationExpression) {
            return translateExpression((ArrayAllocationExpression) expr);
        } else if (expr instanceof ArrayInitializer) {
            return translateExpression((ArrayInitializer) expr);
        } else if (expr instanceof BinaryExpression) {
            return this.translateExpression((BinaryExpression) expr);
        } else if (expr instanceof CastExpression) {
            return this.translateExpression((CastExpression) expr);
        } else if (expr instanceof CharLiteral) {
            return this.translateExpression((CharLiteral) expr);
        } else if (expr instanceof ClassLiteralAccess) {
            return translateExpression((ClassLiteralAccess) expr);
        } else if (expr instanceof CombinedBinaryExpression) {
            return translateExpression((CombinedBinaryExpression) expr);
        } else if (expr instanceof DoubleLiteral) {
            return translateExpression((DoubleLiteral) expr);
        } else if (expr instanceof EqualExpression) {
            return this.translateExpression((EqualExpression) expr);
        } else if (expr instanceof ExtendedStringLiteral) {
            return this.translateExpression((ExtendedStringLiteral) expr);
        } else if (expr instanceof FalseLiteral) {
            return this.translateExpression((FalseLiteral) expr);
        } else if (expr instanceof FloatLiteral) {
            return this.translateExpression((FloatLiteral) expr);
        } else if (expr instanceof InstanceOfExpression) {
            return this.translateExpression((InstanceOfExpression) expr);
        } else if (expr instanceof IntLiteral) {
            return this.translateExpression((IntLiteral) expr);
        } else if (expr instanceof LongLiteral) {
            return this.translateExpression((LongLiteral) expr);
        } else if (expr instanceof NullLiteral) {
            return this.translateExpression((NullLiteral) expr);
        } else if (expr instanceof OR_OR_Expression) {
            return this.translateExpression((OR_OR_Expression) expr);
        } else if (expr instanceof QualifiedAllocationExpression) {
            return this.translateExpression((QualifiedAllocationExpression) expr);
        } else if (expr instanceof StringLiteral) {
            return this.translateExpression((StringLiteral) expr);
        } else if (expr instanceof SuperReference) {
            return this.translateExpression((ThisReference) expr);
        } else if (expr instanceof ThisReference) {
            return this.translateExpression((ThisReference) expr);
        } else if (expr instanceof TrueLiteral) {
            return this.translateExpression((TrueLiteral) expr);
        } else if (expr instanceof UnaryExpression) {
            return this.translateExpression((UnaryExpression) expr);
        } else {
            System.err.println("ERROR (ExpressionTranslator): no translation for " + expr.getClass().getName() + ": " + expr);
        }
        return expr.toString();
    }

    public CodeBuffer embedSpecCase(final Expression e, final SourceTypeBinding sourceTypeBinding, final RacResult result, final String var, final VariableGenerator varGen, final CompilationResult compilationResult, final Condition condition) {
        final CodeBuffer code = new CodeBuffer();
        code.append("  try {\n");
        int tab = 0;
        String statement = "    %1 = %1 && (%2);\n";
        final String transExp = translate(e, sourceTypeBinding, result);
        for (final String string : this.localclassTransformation) {
            code.append(string);
        }
        if (condition.compareTo(Condition.PRE) == 0) {
            tab = 2;
        } else if (condition.compareTo(Condition.POST) == 0) {
            tab = 3;
        } else if (condition.compareTo(Condition.XPOST) == 0) {
            tab = 4;
            statement = "        %1 = %2;\n";
        } else {
            tab = 4;
            code.append("      %1 = %2;\n", var, transExp);
            code.append("  if (!%1) {\n", var);
            code.append(RacTranslator.racErrorDefinition(tab, RacConstants.VN_ERROR_SET, RacConstants.VN_VALUE_SET, e.sourceStart(), compilationResult, getTerms().keySet()));
            code.append("      }\n");
            if (getUseClassicalValidity()) {
                code.append("    } catch (JMLNonExecutableException rac$e) {\n");
                code.append("      %1 = true;\n", RacConstants.VN_ASSERTION);
            }
            code.append("    } catch (Throwable rac$e) {\n");
            code.append("      throw new JMLEvaluationError(\n");
            if (condition.compareTo(Condition.INV) == 0) {
                code.append("        new JMLInvariantError(rac$e));\n");
            } else {
                code.append("        new JMLHistoryConstraintError(rac$e));\n");
            }
            code.append("    }\n");
            return code;
        }
        code.append(statement, var, transExp);
        if (getUseClassicalValidity()) {
            code.append("  } catch (JMLNonExecutableException %1$nonExec) {\n", varGen.freshCatchVar());
            code.append("    %1 = true;\n", var);
        }
        final String throwable_var = varGen.freshCatchVar();
        code.append("  } catch (Throwable %1$cause) {\n", throwable_var);
        code.append("  	 JMLChecker.exit();\n");
        code.append(RacTranslator.racErrorDefinition(tab, RacConstants.VN_ERROR_SET, RacConstants.VN_VALUE_SET, e.sourceStart(), compilationResult, getTerms().keySet()));
        code.append("    throw new JMLEvaluationError(new JMLEntryPreconditionError(%1$cause));\n", throwable_var);
        code.append("  }\n");
        code.append("  if (!%1) {\n", var);
        code.append(RacTranslator.racErrorDefinition(tab, RacConstants.VN_ERROR_SET, RacConstants.VN_VALUE_SET, e.sourceStart(), compilationResult, getTerms().keySet()));
        code.append("  }\n");
        return code;
    }

    /**
	 * Returns a set of terms or values that can be printed upon an assertion violation to describe the program state.
	 */
    public Map<ASTNode, VariableBinding> getTerms() {
        return this.terms;
    }

    public boolean getUseClassicalValidity() {
        return this.problemReporter.options.jmlRacUseClassicalValidity;
    }

    protected void init() {
        this.terms = new HashMap<ASTNode, VariableBinding>();
        this.localclassTransformation = new ArrayList<String>();
        if (this.oldExpressionsInQuantifiers == null) {
            this.oldExpressionsInQuantifiers = new ArrayList<RacOldExpression>();
        }
    }

    public boolean isPresent(final String temp) {
        if (this.evaluatorPUse.contains(temp)) {
            return true;
        }
        return false;
    }

    private String operatorToString(final int operator) {
        switch(operator) {
            case OperatorIds.UNSIGNED_RIGHT_SHIFT:
                return ">>>";
            case OperatorIds.TWIDDLE:
                return "~";
            case OperatorIds.AND:
                return "&";
            case OperatorIds.AND_AND:
                return "&&";
            case OperatorIds.DIVIDE:
                return "/";
            case OperatorIds.EQUAL_EQUAL:
                return "==";
            case OperatorIds.GREATER:
                return ">";
            case OperatorIds.GREATER_EQUAL:
                return ">=";
            case OperatorIds.LEFT_SHIFT:
                return "<<";
            case OperatorIds.LESS:
                return "<";
            case OperatorIds.LESS_EQUAL:
                return "<=";
            case OperatorIds.MINUS:
                return "-";
            case OperatorIds.MULTIPLY:
                return "*";
            case OperatorIds.NOT:
                return "!";
            case OperatorIds.NOT_EQUAL:
                return "!=";
            case OperatorIds.OR:
                return "|";
            case OperatorIds.OR_OR:
                return "||";
            case OperatorIds.PLUS:
                return "+";
            case OperatorIds.REMAINDER:
                return "%";
            case OperatorIds.RIGHT_SHIFT:
                return ">>";
            case OperatorIds.XOR:
                return "^";
            case OperatorIds.JML_IMPLIES:
                return "||";
            case OperatorIds.JML_REV_IMPLIES:
                return "||";
            case OperatorIds.JML_EQUIV:
                return "==";
            case OperatorIds.JML_NOT_EQUIV:
                return "!=";
        }
        return null;
    }

    public void setEvaluatorPDef(final String str) {
        if (str == null) {
            this.evaluatorPDef = "";
        } else {
            this.evaluatorPDef = str;
        }
    }

    public void setEvaluatorPUse(final String str) {
        if (str == null) {
            this.evaluatorPUse = "";
        } else {
            this.evaluatorPUse = str;
        }
    }

    public String translate(final Expression expression) {
        init();
        String translation = "true";
        try {
            translation = dispatch(expression);
        } catch (final NonExecutableException e) {
            System.err.println(RacConstants.ENTIRE_CLAUSE + e.getMessage() + RacConstants.NON_EXEC);
        }
        if (this.isQuantifiedExpressionNonExecutable) {
            return "true";
        } else if (translation.equals("")) {
            return "true";
        } else {
            return translation;
        }
    }

    public String translate(final Expression expression, final RacResult result) {
        this.racResult = result;
        return translate(expression);
    }

    /**
	 * Translates the given expression of the given type. The returned value is a string representation of the
	 * translated expression, which is also an expression.
	 */
    public String translate(final Expression expression, final SourceTypeBinding type, final RacResult result) {
        this.typeBinding = type;
        this.racResult = result;
        init();
        String translation = "true";
        try {
            translation = dispatch(expression);
        } catch (final NonExecutableException e) {
            type.scope.problemReporter().expressionIsNonExecutable(expression);
        }
        if (this.isQuantifiedExpressionNonExecutable) {
            return "true";
        } else if (translation.equals("")) {
            return "true";
        } else {
            return translation;
        }
    }

    /**
	 * Translates the given expression of the given type. The returned value is a string representation of the
	 * translated expression, which is also an expression.
	 */
    public String translate(final Expression expression, final TypeDeclaration type, final RacResult result) {
        this.typeBinding = type.binding;
        this.racResult = result;
        init();
        String translation = "true";
        try {
            translation = dispatch(expression);
        } catch (final NonExecutableException e) {
            type.scope.problemReporter().expressionIsNonExecutable(expression);
        }
        if (this.isQuantifiedExpressionNonExecutable) {
            return "true";
        } else if (translation.equals("")) {
            return "true";
        } else {
            return translation;
        }
    }

    public String translate(final JmlQuantifiedExpression quantiExp, final Expression expr, final SourceTypeBinding typeBinding1, final RacResult racResult1) {
        this.quantifiedExpression = quantiExp;
        return this.translate(expr, typeBinding1, racResult1);
    }

    private String translateArguments(final Expression[] arguments) throws NonExecutableException {
        final StringBuffer code = new StringBuffer();
        code.append("(");
        if (arguments != null) {
            boolean isFirst = true;
            for (final Expression e : arguments) {
                code.append(isFirst ? "" : ", ");
                isFirst = false;
                code.append(dispatch(e));
            }
        }
        code.append(")");
        return code.toString();
    }

    /**
	 * Translates JML allocation expression. E.g., <code>new ArrayList<Integer>(10)</code>
	 * 
	 * @throws NonExecutableException
	 */
    protected String translateExpression(final AllocationExpression expr) throws NonExecutableException {
        final CodeBuffer code = new CodeBuffer();
        code.append("new %1%2%3", dispatch(expr.type), translateTypeArguments(expr.typeArguments), translateArguments(expr.arguments));
        return code.toString();
    }

    /**
	 * Translates the logical AND operator.
	 * 
	 * @throws NonExecutableException
	 */
    protected String translateExpression(final AND_AND_Expression expression) throws NonExecutableException {
        final int operator = (expression.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT;
        final String op = operatorToString(operator);
        final String left = dispatch(expression.left);
        final String right = dispatch(expression.right);
        return "(" + left + op + right + ")";
    }

    protected String translateExpression(final ArrayAllocationExpression expr) throws NonExecutableException {
        final StringBuffer code = new StringBuffer();
        code.append("(new ");
        code.append(dispatch(expr.type));
        for (final Expression e : expr.dimensions) {
            code.append("[");
            if (e != null) {
                code.append(dispatch(e));
            }
            code.append("]");
        }
        code.append(dispatch(expr.initializer));
        code.append(")");
        return code.toString();
    }

    protected String translateExpression(final ArrayInitializer expr) throws NonExecutableException {
        final StringBuffer code = new StringBuffer();
        if (expr != null) {
            code.append(" {");
            if (expr.expressions != null) {
                for (final Expression e : expr.expressions) {
                    code.append(dispatch(e));
                    code.append(", ");
                }
            }
            code.append("} ");
        }
        return code.toString();
    }

    /** E.g., <code>X.Y[]</code>. */
    protected String translateExpression(final ArrayQualifiedTypeReference expr) {
        final CodeBuffer code = new CodeBuffer();
        code.append(RacTranslator.getFullyQualifiedName(expr.resolvedType));
        for (int i = 0; i < expr.dimensions(); i++) {
            code.append("[]");
        }
        return code.toString();
    }

    /**
	 * Translates a JML array reference expression. E.g., <code>E1[E2]</code>
	 * 
	 * @throws NonExecutableException
	 */
    protected String translateExpression(final ArrayReference expr) throws NonExecutableException {
        final StringBuffer code = new StringBuffer();
        code.append(dispatch(expr.receiver));
        code.append("[");
        code.append(expr.position);
        code.append("]");
        return code.toString();
    }

    /** E.g., <code>T[]</code>. */
    protected String translateExpression(final ArrayTypeReference expr) {
        final CodeBuffer code = new CodeBuffer();
        code.append(RacTranslator.getFullyQualifiedName(expr.resolvedType));
        return code.toString();
    }

    protected String translateExpression(final BinaryExpression expression) throws NonExecutableException {
        final int operator = (expression.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT;
        final String opr = operatorToString(operator);
        final String left = dispatch(expression.left);
        final String right = dispatch(expression.right);
        final CodeBuffer code = new CodeBuffer();
        code.append("(");
        switch(operator) {
            case OperatorIds.JML_IMPLIES:
                code.append("!%1 %2 %3", left, opr, right);
                break;
            case OperatorIds.JML_REV_IMPLIES:
                code.append("%1 %2 !%3", left, opr, right);
                break;
            default:
                code.append("%1 %2 %3", left, opr, right);
        }
        code.append(")");
        return code.toString();
    }

    /**
	 * E.g., <code>((T) x)</code>.
	 * 
	 * @throws NonExecutableException
	 */
    protected String translateExpression(final CastExpression expr) throws NonExecutableException {
        final CodeBuffer code = new CodeBuffer();
        code.append("((%1) %2)", dispatch(expr.type), dispatch(expr.expression));
        return code.toString();
    }

    protected String translateExpression(final CharLiteral expr) {
        return expr.toString();
    }

    protected String translateExpression(final ClassLiteralAccess expr) throws NonExecutableException {
        return dispatch(expr.type) + ".class";
    }

    /**
	 * ?
	 * 
	 * @throws NonExecutableException
	 */
    protected String translateExpression(final CombinedBinaryExpression expression) throws NonExecutableException {
        final int operator = (expression.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT;
        final String op = operatorToString(operator);
        final String left = dispatch(expression.left);
        final String right = dispatch(expression.right);
        return "(" + left + op + right + ")";
    }

    protected String translateExpression(final DoubleLiteral expr) {
        return expr.toString();
    }

    /**
	 * Translates equal expression. I.e., <code>==</code>.
	 * 
	 * @throws NonExecutableException
	 */
    protected String translateExpression(final EqualExpression expression) throws NonExecutableException {
        final int operator = (expression.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT;
        final String op = operatorToString(operator);
        final String left = dispatch(expression.left);
        final String right = dispatch(expression.right);
        return "(" + left + op + right + ")";
    }

    /** E.g., <code>"Hello" + "World!"</code>. */
    protected String translateExpression(final ExtendedStringLiteral expr) {
        return "\"" + new String(expr.source()) + "\"";
    }

    protected String translateExpression(final FalseLiteral expression) {
        return expression.toString();
    }

    /**
	 * Translates the given field reference expression. If it is a reference to a ghost/model field that is not defined
	 * in the class or interface under translation, it is translated into a reflective call to the corresponding
	 * ghost/model field getter method.
	 * 
	 * @throws NonExecutableException
	 * 
	 * @see #translateGhostOrModelFieldReference(FieldBinding, String)
	 */
    protected String translateExpression(final FieldReference expr) throws NonExecutableException {
        final StringBuffer code = new StringBuffer();
        final VariableBinding binding = expr.binding;
        final boolean isGhostOrModel = JmlModifier.isGhost(binding.jml.getJmlModifiers()) || JmlModifier.isModel(binding.jml.getJmlModifiers());
        if (isGhostOrModel) {
            final String receiver = dispatch(expr.receiver);
            code.append(translateGhostOrModelFieldReference(expr.binding, receiver));
            return code.toString();
        }
        if (expr.receiver != null) {
            code.append(dispatch(expr.receiver));
            code.append(".");
        }
        code.append(expr.binding.readableName());
        return code.toString();
    }

    protected String translateExpression(final FloatLiteral expression) {
        return expression.toString();
    }

    protected String translateExpression(final InstanceOfExpression expr) throws NonExecutableException {
        final StringBuffer code = new StringBuffer();
        code.append("(");
        code.append(dispatch(expr.expression));
        code.append(" instanceof ");
        code.append(dispatch(expr.type));
        code.append(")");
        return code.toString();
    }

    protected String translateExpression(final IntLiteral expr) {
        return expr.toString();
    }

    /**
	 * Translates a JML array qualified type reference. E.g., <code>X.Y[]</code>.
	 */
    protected String translateExpression(final JmlArrayQualifiedTypeReference expr) {
        return translateExpression((ArrayQualifiedTypeReference) expr);
    }

    /**
	 * Translates a JML cast expression. E.g., <code>(non_null T) E</code>
	 * 
	 * @throws NonExecutableException
	 */
    protected String translateExpression(final JmlCastExpression expr) throws NonExecutableException {
        return translateExpression((CastExpression) expr);
    }

    /**
	 * Translates a JML cast expression without type. E.g., <code>(non_null) E</code>
	 * 
	 * @throws NonExecutableException
	 */
    protected String translateExpression(final JmlCastExpressionWithoutType expr) throws NonExecutableException {
        return translateExpression((CastExpression) expr);
    }

    /**
	 * Translates JML conditional expression. E.g., <code>E1 ? E2 : E3</code>
	 * 
	 * @throws NonExecutableException
	 */
    protected String translateExpression(final JmlConditionalExpression expr) throws NonExecutableException {
        final CodeBuffer code = new CodeBuffer();
        code.append("(%1 ? %2 : %3)", dispatch(expr.condition), dispatch(expr.valueIfTrue), dispatch(expr.valueIfFalse));
        return code.toString();
    }

    /**
	 * Translates JML elemtype expression. E.g., <code>\elemtype(int[])</code>
	 * 
	 * @throws NonExecutableException
	 */
    protected String translateExpression(final JmlElemtypeExpression expr) throws NonExecutableException {
        return "(" + dispatch(expr.expression) + ").getComponentType()";
    }

    /**
	 * Translates the given JML fresh expression. E.g., <code>\fresh(x)</code> (JMLRM 11.4.9). It is translated to
	 * <code>\not_executable</code>.
	 * 
	 * @throws NonExecutableException
	 */
    protected String translateExpression(final JmlFreshExpression expr) throws NonExecutableException {
        return NonExecutableException.throwException(expr);
    }

    /**
	 * Translates the given informal description expression. E.g., <code>(* is a good example? *)</code> (JMLRM
	 * 11.4.15). It is translated to <code>\not_executable</code>.
	 * 
	 * @throws NonExecutableException
	 */
    protected String translateExpression(final JmlInformalExpression expr) throws NonExecutableException {
        return NonExecutableException.throwException(expr);
    }

    /**
	 * Translates a JmlKeywordExpression into a top level true statement by throwing NonExecutableException.
	 */
    protected String translateExpression(final JmlKeywordExpression expr) throws NonExecutableException {
        return NonExecutableException.throwException(expr);
    }

    /**
	 * Translates JML message send expression. E.g., <code>E0.m(E1, ..., En)</code>
	 * 
	 * @throws NonExecutableException
	 */
    protected String translateExpression(final JmlMessageSend expr) throws NonExecutableException {
        final StringBuffer code = new StringBuffer();
        if (expr.binding.methodDeclaration != null) {
            if ((expr.binding.methodDeclaration.isAbstract() || ((expr.binding.methodDeclaration.modifiers & ExtraCompilerModifiers.AccSemicolonBody) != 0)) && expr.binding.jml.isModel()) {
                code.append(NonExecutableException.throwException(expr));
                return code.toString();
            }
        }
        if (expr.receiver != null) {
            if (!((this instanceof ConstraintTranslator) && ((ConstraintTranslator) this).isStatic && (expr.receiver instanceof ThisReference))) {
                code.append(dispatch(expr.receiver));
                code.append(".");
            }
        }
        code.append(expr.selector);
        code.append(translateArguments(expr.arguments));
        return code.toString();
    }

    /**
	 * Translates JML quantified expression. Refer to JMLRM 11.4.24. TODO: JmlQuantifiedExpression: not implemented yet!
	 */
    protected String translateExpression(final JmlQuantifiedExpression expr) {
        return translateQuantifiedExpression(expr);
    }

    /**
	 * Translates the given JML result reference. E.g., <code>\result</code> (see JMLRM 11.4.1).
	 */
    protected String translateExpression(final JmlResultReference expr) {
        return RacConstants.VN_RESULT;
    }

    /**
	 * Translates JML set comprehension expression. Refer to JMLRM 11.5. The translation rule for this expression is
	 * defined as follows.
	 * 
	 * <pre>
	 *   [[new S { T x | E.has(x) && P}, r]] = 
	 *     T_E c = null;
	 *     [[E, c]]
	 *     r = new S();
	 *     Iterator iter = c.iterator();
	 *     for (iter.hasNext()) {
	 *         T x = iter.next();
	 *         boolean b = false;
	 *         [[P, b]]
	 *         if (b) {
	 *            r.add(x);
	 *         }
	 *     }
	 * </pre>
	 */
    protected String translateExpression(final JmlSetComprehension expr) {
        ExpressionTranslator.warnUnsupported("set comprehension", "null");
        return "null";
    }

    /**
	 * Translates the given JML subtype expression. E.g., <code>\type(Integer) <: \type(Object)</code> (JMLRM 11.6.1).
	 * 
	 * @throws NonExecutableException
	 */
    protected String translateExpression(final JmlSubtypeExpression expr) throws NonExecutableException {
        final CodeBuffer code = new CodeBuffer();
        code.append("(%1).isAssignableFrom(%2)", dispatch(expr.right), dispatch(expr.left));
        return code.toString();
    }

    /**
	 * Translates the given JML type expression. E.g., <code>\type(Integer)</code> (JMLRM 11.4.18). It is translated to
	 * a Java class object.
	 */
    protected String translateExpression(final JmlTypeExpression expr) {
        return RacTranslator.getClassType(expr.expression.resolvedType);
    }

    /**
	 * Translates the given JML typeof expression. E.g., <code>\typeof(true)</code> (JMLRM 11.4.16). It is translated to
	 * a Java class object.
	 * 
	 * @throws NonExecutableException
	 */
    protected String translateExpression(final JmlTypeofExpression expr) throws NonExecutableException {
        if (expr.expression.resolvedType.isBaseType()) {
            return RacTranslator.getClassType(expr.expression.resolvedType);
        }
        return "(" + dispatch(expr.expression) + ").getClass()";
    }

    /**
	 * Translates JML unary expressions such as \typeof and \elemtype.
	 * 
	 * @throws NonExecutableException
	 */
    protected String translateExpression(final JmlUnaryExpression expr) throws NonExecutableException {
        final int op = expr.operatorId();
        String trnsltdExpression = "true";
        switch(op) {
            case OperatorIds.JML_NONNULLELEMENTS:
                if (expr.expression.resolvedType.isArrayType() && !expr.expression.resolvedType.leafComponentType().isBaseType()) {
                    trnsltdExpression = "JMLRacUtil.nonNullElements(" + expr.expression + ")";
                }
                break;
            case OperatorIds.JML_OLD:
                trnsltdExpression = NonExecutableException.throwException(expr);
                this.typeBinding.scope.problemReporter().expressionIsNonExecutable(expr);
                break;
            default:
                final String jmlFragment = "unary expression '" + expr.operatorToString() + "'";
                final String msg = ExpressionTranslator.unsupportedExprMessage(jmlFragment, trnsltdExpression);
                this.problemReporter.jmlEsc2Warning(msg, expr.sourceStart(), expr.sourceEnd());
                ExpressionTranslator.warnUnsupported(jmlFragment, trnsltdExpression);
                break;
        }
        return trnsltdExpression;
    }

    protected String translateExpression(final LongLiteral expr) {
        return expr.toString();
    }

    protected String translateExpression(final NullLiteral expr) {
        return "null";
    }

    protected String translateExpression(final OR_OR_Expression expression) throws NonExecutableException {
        final int operator = (expression.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT;
        final String op = operatorToString(operator);
        final String left = dispatch(expression.left);
        final String right = dispatch(expression.right);
        return "(" + left + op + right + ")";
    }

    /**
	 * Translates the given JML parameterized qualified type reference. E.g.,
	 * <code>java.util.Map<String, Integer></code>.
	 */
    protected String translateExpression(final ParameterizedQualifiedTypeReference expr) {
        return RacTranslator.getFullyQualifiedName(expr.resolvedType) + translateTypeArguments(expr.typeArguments[2]);
    }

    /**
	 * Translates the given JML parameterized single type reference. E.g., <code>MySet<Integer></code>.
	 */
    protected String translateExpression(final ParameterizedSingleTypeReference expr) {
        return RacTranslator.getFullyQualifiedName(expr.resolvedType) + translateTypeArguments(expr.typeArguments);
    }

    /**
	 * Translates a Java qualified allocation expression. I.e., <code>new Object() {}</code>.
	 */
    protected String translateExpression(final QualifiedAllocationExpression expr) {
        return expr.concreteStatement().toString();
    }

    /**
	 * Translates the given JML qualified name reference expression. If a part of the reference is a ghost/model field
	 * that is not defined in the class or interface under translation, that part is translated into a reflective call
	 * to the corresponding ghost/model field getter method.
	 * 
	 * @see #translateGhostOrModelFieldReference(FieldBinding, String)
	 */
    protected String translateExpression(final QualifiedNameReference expr) {
        if (expr.binding.kind() == Binding.FIELD) {
            String code = translateFieldBinding((FieldBinding) expr.binding, null);
            if (expr.otherBindings != null) {
                for (final FieldBinding b : expr.otherBindings) {
                    code = translateFieldBinding(b, code);
                }
            }
            return code;
        }
        if (expr.binding.kind() == Binding.LOCAL) {
            String code = new String(((LocalVariableBinding) expr.binding).name);
            if (expr.otherBindings != null) {
                for (final FieldBinding b : expr.otherBindings) {
                    code = translateFieldBinding(b, code);
                }
            }
            return code;
        }
        return expr.concreteStatement().toString();
    }

    /**
	 * Translates a JML qualified type reference. E.g., <code>X.Y</code>.
	 */
    protected String translateExpression(final QualifiedTypeReference expr) {
        return RacTranslator.getFullyQualifiedName(expr.resolvedType);
    }

    /**
	 * Translates the given single name reference. If it is a reference to a ghost/model field that is not defined in
	 * the class or interface under translation, it is translated into a reflective call to the corresponding
	 * ghost/model field getter method.
	 * 
	 * <p>
	 * An example of single name reference include <code>x</code>, where <code>x</code> is a reference to a field
	 * (static or non-static), a parameter, or a local variable.
	 * </p>
	 * 
	 * @see #translateGhostOrModelFieldReference(FieldBinding, String)
	 */
    protected String translateExpression(final SingleNameReference expr) {
        final StringBuffer code = new StringBuffer();
        if (expr.binding instanceof FieldBinding) {
            final FieldBinding field = (FieldBinding) expr.binding;
            final VariableBinding binding = field;
            final boolean isGhostOrModel = JmlModifier.isGhost(binding.jml.getJmlModifiers()) || JmlModifier.isModel(binding.jml.getJmlModifiers());
            if (isGhostOrModel) {
                final String reciever = field.isStatic() ? "null" : "this";
                return translateGhostOrModelFieldReference(field, reciever);
            }
            if (Modifier.isStatic(field.modifiers)) {
                code.append(field.declaringClass.qualifiedSourceName());
            } else if (this.isQuantifiedExpressionTranslation) {
                code.append(String.valueOf(expr.actualReceiverType.debugName()) + ".this");
            } else {
                code.append("this");
            }
            code.append(".");
        }
        if (expr.binding instanceof LocalVariableBinding) {
            if (((LocalVariableBinding) expr.binding).declaringScope instanceof MethodScope) {
                final MethodScope scope = (MethodScope) ((LocalVariableBinding) expr.binding).declaringScope;
                boolean flag = false;
                for (final LocalVariableBinding local : scope.locals) {
                    if (local == expr.binding) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    final List<RacOldExpression> racOld = ((PostStateExpressionTranslator) this).specificationOldExpressions;
                    final RacOldExpression oldExpr = racOld.get(racOld.size() - 1);
                    code.append(RacTranslator.getUnboxedValue(expr.resolvedType, oldExpr.name() + ".value()"));
                    return code.toString();
                }
            }
        }
        if (expr.binding instanceof VariableBinding) {
            this.terms.put(expr, (VariableBinding) expr.binding);
        }
        code.append(expr);
        return code.toString();
    }

    /**
	 * Translates the given single type reference. E.g., <code>Object</code>.
	 */
    protected String translateExpression(final SingleTypeReference expr) {
        return RacTranslator.getFullyQualifiedName(expr);
    }

    protected String translateExpression(final StringLiteral expr) {
        return expr.toString();
    }

    protected String translateExpression(final SuperReference expr) {
        return "super";
    }

    protected String translateExpression(final ThisReference expr) {
        if (this.isQuantifiedExpressionTranslation) {
            return expr.resolvedType.debugName() + ".this";
        }
        return "this";
    }

    protected String translateExpression(final TrueLiteral expression) {
        return expression.toString();
    }

    protected String translateExpression(final UnaryExpression expression) throws NonExecutableException {
        final int operator = (expression.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT;
        final String op = operatorToString(operator);
        final String exp = dispatch(expression.expression);
        return op + "(" + exp + ")";
    }

    /**
	 * Translates the given field binding of a JML qualified name reference. If the binding is for a ghost/model field,
	 * it may be translated into a reflective call to the corresponding ghost/model field getter method.
	 * 
	 * @param prefix
	 *            Expression code denoting the prefix of the qualified name, e.g., <code>x</code> in
	 *            <code>x.length</code>. Null if there's no prefix.
	 * 
	 * @see #translateGhostOrModelFieldReference(FieldBinding, String)
	 */
    private String translateFieldBinding(final FieldBinding binding, String prefix) {
        final StringBuffer code = new StringBuffer();
        final boolean isGhostOrModel = JmlModifier.isGhost(binding.jml.getJmlModifiers()) || JmlModifier.isModel(binding.jml.getJmlModifiers());
        if (isGhostOrModel) {
            prefix = prefix == null ? "this" : prefix;
            code.append(translateGhostOrModelFieldReference(binding, prefix));
            return code.toString();
        }
        if (prefix != null) {
            code.append(prefix);
            code.append(".");
        } else if (binding.isStatic()) {
            code.append(RacTranslator.getFullyQualifiedName(binding.declaringClass));
            code.append(".");
        }
        code.append(binding.readableName());
        return code.toString();
    }

    /**
	 * Translates a reference to the given ghost/model field. If the ghost/model field is not defined in the same class
	 * or interface under translation, the reference is translated into a reflective call to the corresponding
	 * ghost/model field setter method; otherwise, it is translated to a reference to the synthesized ghost/model field.
	 * Given a ghost/model field <code>x</code> defined in class <code>T</code>, the translated code will be one of the
	 * following forms:
	 * 
	 * <pre>
	 *   ghost$x$T
	 *   rac$invoke(T, receiver, ghost$x$T, null, null)
	 *   rac$invoke(T$JmlSurrogate, receiver, ghost$x$T$JmlSurrogate, null, null)
	 *   
	 *   [[OR]]
	 *   
	 *   model$x$T
	 *   rac$invoke(T, receiver, model$x$T, null, null)
	 *   rac$invoke(T$JmlSurrogate, receiver, model$x$T$JmlSurrogate, null, null)
	 * </pre>
	 * 
	 * @param receiver
	 *            Expression code denoting the receiver of the reflective call
	 */
    private String translateGhostOrModelFieldReference(final FieldBinding binding, final String receiver) {
        final char[] className = RacTranslator.getDeclaringClassName(binding);
        final char[] fieldName = binding.readableName();
        final CodeBuffer code = new CodeBuffer();
        final boolean isGhost = JmlModifier.isGhost(binding.jml.getJmlModifiers());
        char[] type;
        if (isGhost) {
            type = "ghost".toCharArray();
        } else {
            type = "model".toCharArray();
        }
        if (binding.declaringClass.isInterface()) {
            final String targetClass = new String(className) + "$JmlSurrogate";
            if (isGhost) {
                this.racResult.isInheritedGhostFieldUsed = true;
                boolean locallyDeclared = false;
                final FieldBinding[] fields = this.typeBinding.fields();
                if (fields != null) {
                    for (final FieldBinding f : fields) {
                        if (binding == f) {
                            locallyDeclared = true;
                            break;
                        }
                    }
                }
                if (!locallyDeclared) {
                    code.append("rac$invoke(\"%1\", %2, \"%3$%4$%5\", null, null)", targetClass, receiver, type, fieldName, className);
                } else {
                    final String ghostName = RacConstants.GHOST_METHOD_PREFIX + String.valueOf(fieldName) + "$" + String.valueOf(className);
                    if (binding.isStatic()) {
                        code.append("JmlSurrogate.%1()", ghostName);
                    } else {
                        code.append("((JmlSurrogate) JMLChecker.getSurrogate(\"%1\", rac$forName(\"%1\"), this)).%2()", targetClass, ghostName);
                    }
                    return code.toString();
                }
            } else {
                this.racResult.isInheritedModelFieldUsed = true;
                if (this.typeBinding.isClass()) {
                    code.append("rac$invoke(\"%1\", %2, \"%3$%4$%1\", null, null)", className, receiver, type, fieldName);
                } else {
                    boolean locallyDeclared = false;
                    final FieldBinding[] fields = this.typeBinding.fields();
                    if (fields != null) {
                        for (final FieldBinding f : fields) {
                            if (fieldName == f.name) {
                                locallyDeclared = true;
                                break;
                            }
                        }
                    }
                    if (!locallyDeclared) {
                        code.append("rac$invoke(\"%1\", %2, \"%3$%4$%1\", null, null)", className, receiver, type, fieldName);
                    } else {
                        final String modelName = RacConstants.MODEL_METHOD_PREFIX + String.valueOf(fieldName) + "$" + String.valueOf(className);
                        code.append("((JmlSurrogate) JMLChecker.getSurrogate(\"%2\", rac$forName(\"%1\"), this)).%2()", targetClass, modelName);
                        return code.toString();
                    }
                }
            }
        } else if (binding.declaringClass.equals(this.typeBinding)) {
            code.append("%1$%2$%3()", type, fieldName, className);
            return code.toString();
        } else {
            if (isGhost) {
                this.racResult.isInheritedGhostFieldUsed = true;
            } else {
                this.racResult.isInheritedModelFieldUsed = true;
            }
            code.append("rac$invoke(\"%1\", %2, \"%3$%4$%1\", null, null)", className, receiver, type, fieldName);
        }
        return RacTranslator.getUnboxedValue(binding.type, code.toString());
    }

    protected String translateQuantifiedExpression(final JmlQuantifiedExpression self) {
        final String var = this.varGenerator.freshVar();
        final PostStateExpressionTranslator pstrans = new PostStateExpressionTranslator(this.varGenerator, true, this.problemReporter);
        pstrans.racResult = this.racResult;
        final QuantifiedExpressionTranslator trans = new QuantifiedExpressionTranslator(this.varGenerator, null, self, var, pstrans, this.racResult, this.typeBinding);
        pstrans.nestedQuantifiers.addAll(this.nestedQuantifiers);
        if (!this.isQuantifiedExpressionTranslation) {
            pstrans.nestedQuantifiers.clear();
        }
        pstrans.nestedQuantifiers.add(self);
        final String code = trans.translate();
        if (code.equals("true")) {
            this.isQuantifiedExpressionNonExecutable = true;
            return code;
        }
        this.oldExpressionsInQuantifiers.addAll(pstrans.getOldExpressions());
        this.oldExpressionsInQuantifiers.addAll(pstrans.getOldQuantifiedExpressions());
        final TypeBinding type = self.resolvedType;
        String quantifierEvaluation = type.debugName() + " " + var + " = " + ((type == TypeBinding.BOOLEAN) ? false : "0") + " ;\n" + code + "\nreturn " + var + ";";
        final String className = this.varGenerator.freshVar();
        final StringBuffer aClass = new StringBuffer();
        aClass.append(className + "{\n");
        trans.getLocalVariablesInQuantifiedExpression(this.evaluatorPUse);
        final String localVariables = trans.getLocalVariables();
        final String localBindings = trans.getLocalBindings();
        if (trans.transExp != null) {
            this.localVars = trans.transExp.localVars;
            this.localBinds = trans.transExp.localBinds;
        }
        if (!localVariables.equals("")) {
            if (this.localVars.equals("")) {
                this.localVars = localVariables;
                this.localBinds = localBindings;
            } else {
                final String[] bindings = localBindings.split(", ");
                final String[] variables = localVariables.split(", ");
                for (int i = 0; i < variables.length; i++) {
                    if (!this.localVars.contains(variables[i])) {
                        this.localVars += " " + variables[i];
                        this.localBinds += " " + bindings[i];
                    }
                }
            }
        }
        if (!this.localVars.equals("")) {
            final String[] bindings = this.localBinds.split(", ");
            for (final String binding : bindings) {
                aClass.append("final " + binding + ";\n");
            }
            aClass.append(className + "(" + this.localBinds + "){\n");
            final String[] variables = this.localVars.split(", ");
            for (final String variable : variables) {
                aClass.append("this." + variable + " = " + variable + ";\n");
            }
            aClass.append("}\n");
        }
        quantifierEvaluation = "class " + aClass.toString() + "public " + type.debugName() + " eval(" + this.evaluatorPDef + "){\n" + quantifierEvaluation + "\n}}\n" + className + " " + var + "Evaluator = new " + className + (this.localVars.equals("") ? "();" : "(" + this.localVars + ");");
        this.localclassTransformation.add(quantifierEvaluation);
        final String return_result = var + "Evaluator.eval(" + this.evaluatorPUse + ")";
        return return_result;
    }

    private String translateTypeArguments(final TypeReference[] arguments) {
        final StringBuffer code = new StringBuffer();
        if ((arguments != null) && (arguments.length > 0)) {
            code.append("<");
            boolean isFirst = true;
            for (final TypeReference e : arguments) {
                code.append(isFirst ? "" : ", ");
                isFirst = false;
                code.append(RacTranslator.getFullyQualifiedName(e.resolvedType));
            }
            code.append(">");
        }
        return code.toString();
    }
}
