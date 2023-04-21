package net.sourceforge.mtac.kernel.builtins;

import net.sourceforge.mtac.gui.prettyprint.PrettyPrintBox;
import net.sourceforge.mtac.gui.prettyprint.SuperscriptBox;
import net.sourceforge.mtac.kernel.ComplexNumber;
import net.sourceforge.mtac.kernel.Expression;
import net.sourceforge.mtac.kernel.Function;
import net.sourceforge.mtac.kernel.FunctionExpression;
import net.sourceforge.mtac.kernel.MTACMath;
import net.sourceforge.mtac.kernel.MTACUtilities;
import net.sourceforge.mtac.kernel.Namespace;
import net.sourceforge.mtac.kernel.NumberExpression;
import net.sourceforge.mtac.kernel.exceptions.InvalidArgumentsException;
import net.sourceforge.mtac.kernel.exceptions.MTACException;

/**
 * This is the exponentiation function.
 *
 * @author Slava Pestov
 * @author Brant Gurganus
 * @version 0.1
 */
public class Pow extends Function {

    /**
     * TODO: DOCUMENT ME!
     *
     * @return TODO: DOCUMENT ME!
     */
    public String getName() {
        return "^";
    }

    /**
     * TODO: DOCUMENT ME!
     *
     * @return TODO: DOCUMENT ME!
     */
    public String getUsage() {
        return "u ^ v: raises the expression to the specified power";
    }

    /**
     * TODO: DOCUMENT ME!
     *
     * @param args TODO: DOCUMENT ME!
     *
     * @throws InvalidArgumentsException TODO: DOCUMENT ME!
     */
    public void checkArguments(Expression[] args) throws InvalidArgumentsException {
        if (args.length != 2) {
            throw new InvalidArgumentsException("^");
        }
    }

    /**
     * TODO: DOCUMENT ME!
     *
     * @param args TODO: DOCUMENT ME!
     * @param font TODO: DOCUMENT ME!
     * @param fontSize TODO: DOCUMENT ME!
     * @param precedence TODO: DOCUMENT ME!
     *
     * @return TODO: DOCUMENT ME!
     */
    public PrettyPrintBox prettyPrint(Expression[] args, String font, int fontSize, int precedence) {
        return new SuperscriptBox(args[0], args[1], font, fontSize);
    }

    /**
     * TODO: DOCUMENT ME!
     *
     * @param namespace TODO: DOCUMENT ME!
     * @param args TODO: DOCUMENT ME!
     * @param evaluate TODO: DOCUMENT ME!
     *
     * @return TODO: DOCUMENT ME!
     *
     * @throws MTACException TODO: DOCUMENT ME!
     */
    public Expression simplify(Namespace namespace, Expression[] args, boolean evaluate) throws MTACException {
        Expression base = args[0];
        Expression exponent = args[1];
        if (evaluate && base instanceof NumberExpression && exponent instanceof NumberExpression) {
            ComplexNumber baseNum = ((NumberExpression) base).getValue();
            ComplexNumber exponentNum = ((NumberExpression) exponent).getValue();
            return new NumberExpression(MTACMath.pow(baseNum, exponentNum));
        }
        if (exponent instanceof NumberExpression) {
            ComplexNumber value = ((NumberExpression) exponent).getValue();
            if (value.equals(0.0)) {
                return MTACUtilities.ONE;
            } else if (value.equals(1.0)) {
                return base;
            } else if (value.equals(-1.0)) {
                return new FunctionExpression("reciprocal", base).simplify(namespace, evaluate);
            } else if (value.equals(0.5)) {
                return new FunctionExpression("sqrt", base).simplify(namespace, evaluate);
            } else if (value.equals(-0.5)) {
                return new FunctionExpression("reciprocal", new FunctionExpression("sqrt", base)).simplify(namespace, evaluate);
            }
        }
        if (base instanceof NumberExpression) {
            ComplexNumber value = ((NumberExpression) base).getValue();
            if (value.equals(0.0)) {
                return MTACUtilities.ZERO;
            } else if (value.equals(1.0)) {
                return MTACUtilities.ONE;
            } else if (value.equals(Math.E)) {
                return new FunctionExpression("exp", exponent).simplify(namespace, evaluate);
            }
        } else if (base.equals(MTACUtilities.E)) {
            return new FunctionExpression("exp", base);
        } else if (base instanceof FunctionExpression) {
            FunctionExpression baseFunc = (FunctionExpression) base;
            String name = baseFunc.getFunctionName();
            Expression[] fargs = baseFunc.getFunctionArguments();
            if (name.equals("^")) {
                return new FunctionExpression("^", fargs[0], new FunctionExpression("*", exponent, fargs[1])).simplify(namespace, evaluate);
            } else if (name.equals("*")) {
                Expression[] newArgs = new Expression[fargs.length];
                for (int i = 0; i < fargs.length; i++) {
                    newArgs[i] = new FunctionExpression(this, fargs[i], exponent);
                }
                return new FunctionExpression("*", newArgs);
            } else if (name.equals("sqrt")) {
                return new FunctionExpression(this, fargs[0], new FunctionExpression("*", exponent, MTACUtilities.HALF)).simplify(namespace, evaluate);
            } else if (name.equals("reciprocal")) {
                return new FunctionExpression(this, fargs[0], new FunctionExpression("neg", exponent)).simplify(namespace, evaluate);
            } else if (name.equals("exp")) {
                return new FunctionExpression("exp", new FunctionExpression("*", fargs[0], exponent)).simplify(namespace, evaluate);
            }
        }
        return new FunctionExpression(this, base, exponent);
    }
}
