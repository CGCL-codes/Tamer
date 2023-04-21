package org.matheclipse.core.convert;

import java.util.HashMap;
import java.util.Map;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.reflection.system.Blank;
import org.matheclipse.core.reflection.system.Complex;
import org.matheclipse.core.reflection.system.Pattern;
import org.matheclipse.core.reflection.system.Rational;
import org.matheclipse.parser.client.ast.ASTNode;
import org.matheclipse.parser.client.ast.FloatNode;
import org.matheclipse.parser.client.ast.FractionNode;
import org.matheclipse.parser.client.ast.FunctionNode;
import org.matheclipse.parser.client.ast.IntegerNode;
import org.matheclipse.parser.client.ast.Pattern2Node;
import org.matheclipse.parser.client.ast.Pattern3Node;
import org.matheclipse.parser.client.ast.PatternNode;
import org.matheclipse.parser.client.ast.StringNode;
import org.matheclipse.parser.client.ast.SymbolNode;

/**
 * Converts a parsed <code>org.matheclipse.parser.client.ast.ASTNode</code>
 * expression into an IExpr expression
 * 
 */
public class AST2Expr {

    public static final String[] PREDEFINED_SYMBOLS = { "True", "False", "List", "Modulus", "Flat", "HoldAll", "HoldFirst", "HoldRest", "Listable", "NumericFunction", "OneIdentity", "Orderless", "Slot", "SlotSequence", "Abs", "AddTo", "And", "Apart", "Append", "AppendTo", "Apply", "ArcCos", "ArcSin", "ArcTan", "Arg", "Array", "AtomQ", "Binomial", "Blank", "Block", "Boole", "Break", "Cancel", "CartesianProduct", "Cases", "Catalan", "CatalanNumber", "Catch", "Ceiling", "CharacteristicPolynomial", "ChessboardDistance", "Chop", "Clear", "ClearAll", "Coefficient", "CoefficientList", "Complement", "Complex", "ComplexInfinity", "ComposeList", "CompoundExpression", "Condition", "Conjugate", "ConstantArray", "Continue", "ContinuedFraction", "CoprimeQ", "Cos", "Cosh", "Cot", "Count", "Cross", "Csc", "Curl", "D", "Decrement", "Default", "Definition", "Degree", "Delete", "Denominator", "Depth", "Derivative", "Det", "DiagonalMatrix", "DigitQ", "Dimensions", "Discriminant", "Distribute", "Divergence", "DivideBy", "Do", "Dot", "Drop", "E", "Eigenvalues", "Eigenvectors", "Equal", "Erf", "EuclidianDistance", "EulerGamma", "EulerPhi", "EvenQ", "Exp", "Expand", "ExpandAll", "Exponent", "ExtendedGCD", "Extract", "Factor", "Factorial", "Factorial2", "FactorInteger", "FactorSquareFree", "FactorSquareFreeList", "FactorTerms", "Fibonacci", "FindRoot", "First", "Fit", "FixedPoint", "Floor", "Fold", "FoldList", "For", "FractionalPart", "FreeQ", "FromCharacterCode", "FromContinuedFraction", "FullForm", "FullSimplify", "Function", "Gamma", "GCD", "Glaisher", "GoldenRatio", "Greater", "GreaterEqual", "GroebnerBasis", "HarmonicNumber", "Head", "HilbertMatrix", "Hold", "Horner", "I", "IdentityMatrix", "If", "Im", "Increment", "Infinity", "Inner", "IntegerPartitions", "IntegerQ", "Integrate", "Intersection", "Inverse", "InverseFunction", "JacobiMatrix", "JacobiSymbol", "JavaForm", "Join", "Khinchin", "KOrderlessPartitions", "KPartitions", "Last", "LCM", "LeafCount", "Length", "Less", "LessEqual", "LetterQ", "Level", "Limit", "LinearProgramming", "LinearSolve", "Log", "LowerCaseQ", "LUDecomposition", "ManhattanDistance", "Map", "MapAll", "MapThread", "MatchQ", "MatrixPower", "MatrixQ", "Max", "Mean", "Median", "MemberQ", "Min", "Mod", "Module", "MoebiusMu", "Most", "Multinomial", "N", "Negative", "Nest", "NestList", "NextPrime", "NIntegrate", "NonCommutativeMultiply", "NonNegative", "Norm", "Not", "NRoots", "NumberQ", "Numerator", "NumericQ", "OddQ", "Or", "Order", "OrderedQ", "Out", "Outer", "Package", "PadLeft", "PadRight", "ParametricPlot", "Part", "Partition", "Pattern", "Permutations", "Pi", "Plot", "Plot3D", "Plus", "PolynomialExtendedGCD", "PolynomialGCD", "PolynomialLCM", "PolynomialQ", "PolynomialQuotient", "PolynomialQuotientRemainder", "PolynomialRemainder", "Position", "Positive", "PossibleZeroQ", "Power", "PowerExpand", "PowerMod", "PreDecrement", "PreIncrement", "Prepend", "PrependTo", "PrimeQ", "PrimitiveRoots", "Print", "Product", "Quotient", "RandomInteger", "RandomReal", "Range", "Rational", "Rationalize", "Re", "Reap", "ReplaceAll", "ReplacePart", "ReplaceRepeated", "Rest", "Resultant", "Return", "Reverse", "Riffle", "RootIntervals", "Roots", "RotateLeft", "RotateRight", "Round", "Rule", "RuleDelayed", "SameQ", "Scan", "Sec", "Select", "Set", "SetAttributes", "SetDelayed", "Sign", "SignCmp", "Simplify", "Sin", "SingularValueDecomposition", "Sinh", "Solve", "Sort", "Sow", "Sqrt", "SquaredEuclidianDistance", "SquareFreeQ", "StirlingS2", "StringDrop", "StringJoin", "StringLength", "StringTake", "Subsets", "SubtractFrom", "Sum", "SyntaxLength", "SyntaxQ", "Table", "Take", "Tan", "Tanh", "Taylor", "Thread", "Through", "Throw", "Times", "TimesBy", "Timing", "ToCharacterCode", "Together", "ToString", "Total", "ToUnicode", "Tr", "Trace", "Transpose", "TrigExpand", "TrigReduce", "TrigToExp", "TrueQ", "Trunc", "Unequal", "Union", "UnsameQ", "UpperCaseQ", "ValueQ", "VandermondeMatrix", "Variables", "VectorQ", "While" };

    ;

    static final Map<String, String> PREDEFINED_SYMBOLS_MAP = new HashMap<String, String>();

    static {
        for (String str : PREDEFINED_SYMBOLS) {
            PREDEFINED_SYMBOLS_MAP.put(str.toLowerCase(), str);
        }
    }

    /**
	 * Typical instance of an ASTNode to IExpr converter
	 */
    public static final AST2Expr CONST = new AST2Expr();

    public static final AST2Expr CONST_LC = new AST2Expr(true);

    private boolean fLowercaseEnabled;

    /**
	 * 
	 * @param sType
	 * @param tType
	 * @deprecated
	 */
    public AST2Expr(final Class<ASTNode> sType, final Class<IExpr> tType) {
        this(false);
    }

    public AST2Expr() {
        this(false);
    }

    public AST2Expr(boolean lowercaseEnabled) {
        super();
        fLowercaseEnabled = lowercaseEnabled;
    }

    /**
	 * Converts a parsed FunctionNode expression into an IAST expression
	 */
    public IAST convert(IAST ast, FunctionNode functionNode) throws ConversionException {
        ast.set(0, convert(functionNode.get(0)));
        for (int i = 1; i < functionNode.size(); i++) {
            ast.add(convert(functionNode.get(i)));
        }
        return ast;
    }

    /**
	 * Converts a parsed ASTNode expression into an IExpr expression
	 */
    public IExpr convert(ASTNode node) throws ConversionException {
        if (node == null) {
            return null;
        }
        if (node instanceof Pattern3Node) {
            throw new UnsupportedOperationException("'___' pattern-matching expression not implemented");
        }
        if (node instanceof FunctionNode) {
            final FunctionNode functionNode = (FunctionNode) node;
            final IAST ast = F.ast(convert(functionNode.get(0)), functionNode.size(), false);
            for (int i = 1; i < functionNode.size(); i++) {
                ast.add(convert(functionNode.get(i)));
            }
            IExpr head = ast.head();
            if (ast.isASTSizeGE(F.GreaterEqual, 3)) {
                ISymbol compareHead = F.Greater;
                return rewriteLessGreaterAST(ast, compareHead);
            } else if (ast.isASTSizeGE(F.Greater, 3)) {
                ISymbol compareHead = F.GreaterEqual;
                return rewriteLessGreaterAST(ast, compareHead);
            } else if (ast.isASTSizeGE(F.LessEqual, 3)) {
                ISymbol compareHead = F.Less;
                return rewriteLessGreaterAST(ast, compareHead);
            } else if (ast.isASTSizeGE(F.Less, 3)) {
                ISymbol compareHead = F.LessEqual;
                return rewriteLessGreaterAST(ast, compareHead);
            } else if (head.equals(F.PatternHead)) {
                final IExpr expr = Pattern.CONST.evaluate(ast);
                if (expr != null) {
                    return expr;
                }
            } else if (head.equals(F.BlankHead)) {
                final IExpr expr = Blank.CONST.evaluate(ast);
                if (expr != null) {
                    return expr;
                }
            } else if (head.equals(F.ComplexHead)) {
                final IExpr expr = Complex.CONST.evaluate(ast);
                if (expr != null) {
                    return expr;
                }
            } else if (head.equals(F.RationalHead)) {
                final IExpr expr = Rational.CONST.evaluate(ast);
                if (expr != null) {
                    return expr;
                }
            }
            return ast;
        }
        if (node instanceof SymbolNode) {
            String nodeStr = node.getString();
            if (fLowercaseEnabled) {
                nodeStr = nodeStr.toLowerCase();
                String temp = PREDEFINED_SYMBOLS_MAP.get(nodeStr);
                if (temp != null) {
                    nodeStr = temp;
                }
            }
            if (nodeStr.equals("I")) {
                return F.CI;
            } else if (nodeStr.equals("Infinity")) {
                return F.CInfinity;
            }
            return F.$s(nodeStr);
        }
        if (node instanceof Pattern2Node) {
            final Pattern2Node p2n = (Pattern2Node) node;
            return F.$ps((ISymbol) convert(p2n.getSymbol()), convert(p2n.getConstraint()), p2n.isDefault());
        }
        if (node instanceof PatternNode) {
            final PatternNode pn = (PatternNode) node;
            return F.$p((ISymbol) convert(pn.getSymbol()), convert(pn.getConstraint()), pn.isDefault());
        }
        if (node instanceof IntegerNode) {
            final IntegerNode integerNode = (IntegerNode) node;
            final String iStr = integerNode.getString();
            if (iStr != null) {
                return F.integer(iStr, integerNode.getNumberFormat());
            }
            return F.integer(integerNode.getIntValue());
        }
        if (node instanceof FractionNode) {
            FractionNode fr = (FractionNode) node;
            if (fr.isSign()) {
                return F.fraction((IInteger) convert(fr.getNumerator()), (IInteger) convert(fr.getDenominator())).negate();
            }
            return F.fraction((IInteger) convert(((FractionNode) node).getNumerator()), (IInteger) convert(((FractionNode) node).getDenominator()));
        }
        if (node instanceof StringNode) {
            return F.stringx(node.getString());
        }
        if (node instanceof FloatNode) {
            return F.num(node.getString());
        }
        return F.$s(node.toString());
    }

    /**
	 * Convert less or greter relations on input. Example: convert expressions
	 * like <code>a<b<=c</code> to <code>Less[a,b]&&LessEqual[b,c]</code>.
	 * 
	 * @param ast
	 * @param compareHead
	 * @return
	 */
    private IExpr rewriteLessGreaterAST(final IAST ast, ISymbol compareHead) {
        IExpr temp;
        boolean evaled = false;
        IAST andAST = F.ast(F.And);
        for (int i = 1; i < ast.size(); i++) {
            temp = ast.get(i);
            if (temp.isASTSizeGE(compareHead, 3)) {
                IAST lt = (IAST) temp;
                andAST.add(lt);
                ast.set(i, lt.get(lt.size() - 1));
                evaled = true;
            }
        }
        if (evaled) {
            andAST.add(ast);
            return andAST;
        } else {
            return ast;
        }
    }
}
