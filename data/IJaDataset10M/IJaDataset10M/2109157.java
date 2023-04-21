package br.edu.ufcg.msnlab.misc;

import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mathExpr.ExpressionConfiguration;
import mathExpr.evaluator.complexEvaluator.ComplexType;
import mfc.field.Complex;
import mfc.polynomial.ComplexPolynomial;

/**
 * 
 * @author Marcus Williams
 * @author Carla Araujo
 *
 */
public class PolynomialParser {

    public static int polynomialDegree(String poly) {
        poly = poly.replaceAll(" ", "");
        Pattern pattern = Pattern.compile("[x][^\\^]*");
        Matcher m = pattern.matcher(poly);
        int maxDegree = m.find() ? 1 : 0;
        pattern = Pattern.compile("[x]\\^[0-9]*");
        m = pattern.matcher(poly);
        while (m.find()) {
            int degree = Integer.parseInt(m.group().substring(2));
            if (degree > maxDegree) {
                maxDegree = degree;
            }
        }
        return maxDegree;
    }

    public static ComplexPolynomial parse2complexPolynomial(String expression) {
        TreeMap<Integer, Complex> exponents = new TreeMap<Integer, Complex>();
        expression = expression.replaceAll(" ", "");
        if (Character.isLetterOrDigit(expression.charAt(0))) {
            expression = "+" + expression;
        }
        String coef = "";
        int exp = 0;
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(expression);
        int previousIndex = 0;
        int currentIndex = 0;
        char symbol = ' ';
        boolean loop = true;
        while (loop) {
            loop = matcher.find();
            if (loop) {
                currentIndex = matcher.start();
            } else {
                currentIndex = expression.length();
            }
            switch(symbol) {
                case '^':
                    exp = Integer.parseInt(expression.substring(previousIndex + 1, currentIndex));
                    break;
                case 'x':
                    exp = 1;
                    break;
                case ' ':
                    break;
                default:
                    if (!coef.equals("")) {
                        Complex c;
                        if (!exponents.containsKey(exp)) {
                            c = new Complex(0);
                        } else {
                            c = exponents.get(exp);
                        }
                        ExpressionConfiguration conf = new ExpressionConfiguration(ComplexType.TYPE);
                        conf.setExpression(c.toString() + coef);
                        Complex res = (Complex) conf.evaluateExpression();
                        exponents.put(exp, res);
                    }
                    coef = expression.substring(previousIndex, currentIndex);
                    if (coef.equals("+") || coef.equals("-")) {
                        coef += "1";
                    }
                    exp = 0;
                    break;
            }
            if (loop) {
                symbol = matcher.group().charAt(0);
                previousIndex = currentIndex;
            }
        }
        Complex c;
        if (!exponents.containsKey(exp)) {
            c = new Complex(0);
        } else {
            c = exponents.get(exp);
        }
        ExpressionConfiguration conf = new ExpressionConfiguration(ComplexType.TYPE);
        conf.setExpression(c.toString() + coef);
        Complex res = (Complex) conf.evaluateExpression();
        exponents.put(exp, res);
        int lastExponent = exponents.lastKey();
        Complex[] complexArray = new Complex[lastExponent + 1];
        for (int index = 0; index < complexArray.length; index++) {
            complexArray[index] = exponents.containsKey(index) ? exponents.get(index) : new Complex(0);
        }
        return new ComplexPolynomial(complexArray);
    }
}
