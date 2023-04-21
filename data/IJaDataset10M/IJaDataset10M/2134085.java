package org.matheclipse.core.reflection.system;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import org.matheclipse.core.basic.Config;
import org.matheclipse.core.convert.ExprVariables;
import org.matheclipse.core.convert.JASConvert;
import org.matheclipse.core.eval.exception.JASConversionException;
import org.matheclipse.core.eval.exception.Validate;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.ASTRange;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.IRational;
import org.matheclipse.core.interfaces.ISignedNumber;
import edu.jas.arith.BigRational;
import edu.jas.poly.GenPolynomial;
import edu.jas.ufd.FactorAbstract;
import edu.jas.ufd.FactorFactory;
import edu.jas.ufd.SquarefreeAbstract;
import edu.jas.ufd.SquarefreeFactory;

/**
 * Evaluate the partial fraction decomposition of a univariate polynomial
 * fraction.
 * 
 * See <a href="http://en.wikipedia.org/wiki/Partial_fraction">Wikipedia -
 * Partial fraction decomposition</a>
 */
public class Apart extends AbstractFunctionEvaluator {

    public Apart() {
    }

    @Override
    public IExpr evaluate(final IAST ast) {
        Validate.checkRange(ast, 2, 3);
        IAST variableList = null;
        if (ast.size() == 3) {
            variableList = Validate.checkSymbolOrSymbolList(ast, 2);
        } else {
            ExprVariables eVar = new ExprVariables(ast.get(1));
            if (!eVar.isSize(1)) {
                return null;
            }
            variableList = eVar.getVarList();
        }
        final IExpr arg = ast.get(1);
        if (arg.isTimes() || arg.isPower()) {
            IExpr[] parts = Apart.getFractionalParts(ast.get(1));
            if (parts != null) {
                IAST plusResult = apart(parts, variableList);
                if (plusResult != null) {
                    if (plusResult.size() == 2) {
                        return plusResult.get(1);
                    }
                    return plusResult;
                }
            }
        } else {
            return ast.get(1);
        }
        return null;
    }

    /**
	 * Returns an AST with head <code>Plus</code>, which contains the partial
	 * fraction decomposition of the numerator and denominator parts.
	 * 
	 * @param parts
	 * @param variableList
	 * @return <code>null</code> if the partial fraction decomposition wasn't
	 *         constructed
	 */
    public static IAST apart(IExpr[] parts, IAST variableList) {
        try {
            IExpr exprNumerator = F.evalExpandAll(parts[0]);
            IExpr exprDenominator = F.evalExpandAll(parts[1]);
            ASTRange r = new ASTRange(variableList, 1);
            List<IExpr> varList = r.toList();
            String[] varListStr = new String[1];
            varListStr[0] = variableList.get(1).toString();
            JASConvert<BigRational> jas = new JASConvert<BigRational>(varList, BigRational.ZERO);
            GenPolynomial<BigRational> numerator = jas.expr2JAS(exprNumerator);
            GenPolynomial<BigRational> denominator = jas.expr2JAS(exprDenominator);
            FactorAbstract<BigRational> factorAbstract = FactorFactory.getImplementation(BigRational.ZERO);
            SortedMap<GenPolynomial<BigRational>, Long> sfactors = factorAbstract.baseFactors(denominator);
            List<GenPolynomial<BigRational>> D = new ArrayList<GenPolynomial<BigRational>>(sfactors.keySet());
            SquarefreeAbstract<BigRational> sqf = SquarefreeFactory.getImplementation(BigRational.ZERO);
            List<List<GenPolynomial<BigRational>>> Ai = sqf.basePartialFraction(numerator, sfactors);
            if (Ai.size() > 0) {
                IAST result = F.Plus();
                IExpr temp;
                if (!Ai.get(0).get(0).isZERO()) {
                    temp = F.eval(jas.poly2Expr(Ai.get(0).get(0), null));
                    if (temp.isAST()) {
                        ((IAST) temp).addEvalFlags(IAST.IS_DECOMPOSED_PARTIAL_FRACTION);
                    }
                    result.add(temp);
                }
                for (int i = 1; i < Ai.size(); i++) {
                    List<GenPolynomial<BigRational>> list = Ai.get(i);
                    long j = 0L;
                    for (GenPolynomial<BigRational> genPolynomial : list) {
                        if (!genPolynomial.isZERO()) {
                            temp = F.eval(F.Times(jas.poly2Expr(genPolynomial, null), F.Power(jas.poly2Expr(D.get(i - 1), null), F.integer(j * (-1L)))));
                            if (!temp.equals(F.C0)) {
                                if (temp.isAST()) {
                                    ((IAST) temp).addEvalFlags(IAST.IS_DECOMPOSED_PARTIAL_FRACTION);
                                }
                                result.add(temp);
                            }
                        }
                        j++;
                    }
                }
                return result;
            }
        } catch (JASConversionException e) {
            if (Config.DEBUG) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
	 * Split the expression into numerator and denominator parts, by separating
	 * positive and negative powers.
	 * 
	 * @param arg
	 * @return the numerator and denominator expression
	 */
    public static IExpr[] getFractionalParts(final IExpr arg) {
        IExpr[] parts = null;
        if (arg.isTimes()) {
            parts = Apart.getFractionalPartsTimes((IAST) arg, true);
        } else if (arg.isPower()) {
            IAST temp = (IAST) arg;
            if (temp.get(2).isSignedNumber()) {
                ISignedNumber sn = (ISignedNumber) temp.get(2);
                parts = new IExpr[2];
                if (sn.equals(F.CN1)) {
                    parts[0] = F.C1;
                    parts[1] = temp.get(1);
                } else if (sn.isNegative()) {
                    parts[0] = F.C1;
                    parts[1] = F.Power(temp.get(1), sn.negate());
                } else {
                    if (sn.isInteger() && temp.get(1).isAST()) {
                        IAST function = (IAST) temp.get(1);
                        IAST denomForm = Denominator.getDenominatorForm(function);
                        if (denomForm != null) {
                            parts[0] = F.C1;
                            parts[1] = F.Power(denomForm, sn);
                            return parts;
                        }
                    }
                    parts[0] = arg;
                    parts[1] = F.C1;
                }
            }
        } else {
            if (arg.isAST()) {
                IAST denomForm = Denominator.getDenominatorForm((IAST) arg);
                if (denomForm != null) {
                    parts = new IExpr[2];
                    parts[0] = F.C1;
                    parts[1] = denomForm;
                    return parts;
                }
            }
            parts = new IExpr[2];
            parts[0] = arg;
            parts[1] = F.C1;
        }
        return parts;
    }

    /**
	 * Return the numerator and denominator for the given <code>Times[...]</code>
	 * AST, by separating positive and negative powers.
	 * 
	 * @param ast
	 *          a times expression (a*b*c....)
	 * @param splitFractionalNumbers
	 *          TODO
	 * @return the numerator and denominator expression
	 */
    public static IExpr[] getFractionalPartsTimes(final IAST ast, boolean splitFractionalNumbers) {
        IExpr[] result = new IExpr[2];
        IAST numerator = F.Times();
        IAST denominator = F.Times();
        IExpr arg;
        IAST argAST;
        for (int i = 1; i < ast.size(); i++) {
            arg = ast.get(i);
            if (arg.isAST()) {
                argAST = (IAST) arg;
                if (argAST.size() == 2) {
                    IAST denomForm = Denominator.getDenominatorForm(argAST);
                    if (denomForm != null) {
                        denominator.add(denomForm);
                        continue;
                    }
                } else if (arg.isPower()) {
                    if (argAST.get(2).isSignedNumber()) {
                        ISignedNumber sn = (ISignedNumber) argAST.get(2);
                        if (sn.equals(F.CN1)) {
                            denominator.add(argAST.get(1));
                            continue;
                        }
                        if (sn.isNegative()) {
                            denominator.add(F.Power(argAST.get(1), ((ISignedNumber) argAST.get(2)).negate()));
                            continue;
                        }
                        if (sn.isInteger() && argAST.get(1).isAST()) {
                            IAST function = (IAST) argAST.get(1);
                            IAST denomForm = Denominator.getDenominatorForm(function);
                            if (denomForm != null) {
                                denominator.add(F.Power(denomForm, sn));
                                continue;
                            }
                        }
                    }
                }
            } else if (splitFractionalNumbers && arg.isRational()) {
                IInteger numer = ((IRational) arg).getNumerator();
                if (!numer.equals(F.C1)) {
                    numerator.add(numer);
                }
                IInteger denom = ((IRational) arg).getDenominator();
                if (!denom.equals(F.C1)) {
                    denominator.add(denom);
                }
                continue;
            }
            numerator.add(arg);
        }
        if (numerator.size() == 1) {
            result[0] = F.C1;
        } else if (numerator.size() == 2) {
            result[0] = numerator.get(1);
        } else {
            result[0] = numerator;
        }
        if (denominator.size() == 1) {
            result[1] = F.C1;
        } else if (denominator.size() == 2) {
            result[1] = denominator.get(1);
        } else {
            result[1] = denominator;
        }
        return result;
    }
}
