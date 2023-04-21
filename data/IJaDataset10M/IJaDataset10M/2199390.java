package org.servingMathematics.mathematics.impl.operators;

import org.servingMathematics.mathematics.impl.MathInvalidArgumentException;
import org.servingMathematics.mathematics.impl.numbers.ComplexImpl;
import org.servingMathematics.mathematics.impl.numbers.MathIntegerImpl;
import org.servingMathematics.mathematics.impl.numbers.RationalImpl;
import org.servingMathematics.mathematics.impl.numbers.RealImpl;
import org.servingMathematics.mathematics.interfaces.MathNode;
import org.servingMathematics.mathematics.interfaces.MathObject;
import org.servingMathematics.mathematics.interfaces.Substitutable;
import org.servingMathematics.mathematics.interfaces.numbers.MathInteger;
import org.servingMathematics.mathematics.interfaces.numbers.MathNumber;
import org.servingMathematics.mathematics.interfaces.numbers.Rational;
import org.servingMathematics.mathematics.interfaces.numbers.Real;
import org.servingMathematics.mathematics.interfaces.operators.Power;

public class PowerImpl implements Power, Substitutable {

    protected MathNode[] children;

    public PowerImpl() {
    }

    public PowerImpl(MathNode[] children) {
        this.setChildren(children);
    }

    private String[] variables;

    private MathObject[] variableValues;

    public void setVariables(String[] variables) {
        this.variables = variables;
    }

    public void setVariableValues(MathObject[] variableValues) {
        this.variableValues = variableValues;
    }

    public String[] getVariables() {
        return this.variables;
    }

    public MathObject[] getVariableValues() {
        return this.variableValues;
    }

    public int getType() {
        return MathNode.SUBTRACT;
    }

    public boolean isLeaf() {
        return false;
    }

    public boolean isSymbolicExpression() {
        return children[0].isSymbolicExpression() || children[1].isSymbolicExpression();
    }

    public MathObject evaluate() throws Exception {
        MathObject child0Value = this.children[0].evaluate();
        MathObject child1Value = this.children[1].evaluate();
        MathNumber mn0 = (MathNumber) child0Value;
        if ((child0Value.isMathNumber()) && (child1Value.isMathNumber())) return ((MathObject) power(((MathNumber) child0Value), ((MathNumber) child1Value)));
        throw new MathInvalidArgumentException("The power function is only defined on MathNumbers");
    }

    public String toString() {
        String child0String;
        String child1String;
        switch(children[0].getType()) {
            case MathNode.PLUS:
                child0String = "(" + children[0].toString() + ")";
                break;
            case MathNode.SUBTRACT:
                child0String = "(" + children[0].toString() + ")";
                break;
            case MathNode.TIMES:
                child0String = "(" + children[0].toString() + ")";
                break;
            case MathNode.DIVIDE:
                child0String = "(" + children[0].toString() + ")";
                break;
            case MathNode.POWER:
                child0String = "(" + children[0].toString() + ")";
                break;
            default:
                child0String = children[0].toString();
        }
        switch(children[1].getType()) {
            case MathNode.PLUS:
                child1String = "(" + children[1].toString() + ")";
                break;
            case MathNode.SUBTRACT:
                child1String = "(" + children[1].toString() + ")";
                break;
            case MathNode.TIMES:
                child1String = "(" + children[1].toString() + ")";
                break;
            case MathNode.DIVIDE:
                child1String = "(" + children[1].toString() + ")";
                break;
            case MathNode.POWER:
                child1String = "(" + children[1].toString() + ")";
                break;
            default:
                child1String = children[1].toString();
        }
        return child0String + " ^ " + child1String;
    }

    public MathNode[] getChildren() {
        return this.children;
    }

    public void setChildren(MathNode[] children) {
        this.children = children;
    }

    public static MathNumber power(MathNumber x, MathNumber y) throws Exception {
        if (x.equalsZero() && y.equalsZero()) throw new MathInvalidArgumentException("Zero to the power zero is undefined");
        if (y.isMathInteger()) {
            int intY = ((MathInteger) y).intValue();
            if (intY == 0) return MathIntegerImpl.ONE;
            if (intY > 0) return TimesImpl.times(power(x, new MathIntegerImpl(intY - 1)), x);
            return DivideImpl.divide(power(x, new MathIntegerImpl(intY + 1)), x);
        }
        if ((x.isRational()) && (y.isReal())) {
            Rational ratX = ((Rational) x).getRationalValue();
            Rational ratY = ((Rational) y).getRationalValue();
            int xNum = ratX.getNumeratorAsInt();
            int yNum = ratY.getNumeratorAsInt();
            int xDen = ratX.getDenominatorAsInt();
            int yDen = ratY.getDenominatorAsInt();
            double dblXNum = (double) xNum;
            double dblXDen = (double) xDen;
            double dblY = ((double) yNum) / ((double) yDen);
            double num = Math.pow(dblXNum, dblY);
            double den = Math.pow(dblXDen, dblY);
            if (num == Math.floor(num) && den == Math.floor(den)) return new RationalImpl((int) num, (int) den);
            return new RealImpl(num / den);
        }
        if ((x.isReal()) && (y.isReal())) {
            double dblX = ((Real) x).doubleValue();
            double dblY = ((Real) y).doubleValue();
            if (dblX >= 0.0 || dblY == Math.floor(dblY)) return new RealImpl(Math.pow(dblX, dblY));
            return power(new ComplexImpl((Real) x, MathIntegerImpl.ZERO), y);
        }
        if (y.isReal()) {
            Real absX = (Real) AbsImpl.abs(x);
            Real argX = (Real) ArgImpl.arg(x);
            Real abs = (Real) power(absX, y);
            Real arg = (Real) TimesImpl.times(argX, y);
            return ComplexImpl.toRectangularForm(abs, arg);
        }
        Real absX = (Real) AbsImpl.abs(x);
        Real argX = (Real) ArgImpl.arg(x);
        Real yRe = (Real) y.getRealPart();
        Real yIm = (Real) y.getImaginaryPart();
        MathNumber rePower = power(x, yRe);
        Real abs = (Real) ExpImpl.exp(TimesImpl.times(TimesImpl.times(argX, yIm), MathIntegerImpl.MINUS_ONE));
        Real arg = (Real) TimesImpl.times(LogImpl.log(absX), yIm);
        ComplexImpl imPower = ComplexImpl.toRectangularForm(abs, arg);
        return TimesImpl.times(rePower, imPower);
    }
}
