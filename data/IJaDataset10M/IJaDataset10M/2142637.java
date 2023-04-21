package com.showyourwork.engine.trigonometry;

import org.jscience.mathematics.number.Rational;

public class RightTriangle {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Rational radiusVector = Rational.valueOf("10/1");
        Rational abscissa = Rational.valueOf("8/1");
        Rational ordinate = Rational.valueOf("6/1");
        Rational sinA = ordinate.divide(radiusVector);
        Rational cosA = abscissa.divide(radiusVector);
        Rational tanA = ordinate.divide(abscissa);
        Rational cotA = abscissa.divide(ordinate);
        Rational secA = radiusVector.divide(abscissa);
        Rational cscA = radiusVector.divide(ordinate);
        System.out.println("sin A = " + sinA + " = " + sinA.doubleValue());
        System.out.println("cos A = " + cosA + " = " + cosA.doubleValue());
        System.out.println("tan A = " + tanA + " = " + tanA.doubleValue());
        System.out.println("cot A = " + cotA + " = " + cotA.doubleValue());
        System.out.println("sec A = " + secA + " = " + secA.doubleValue());
        System.out.println("csc A = " + cscA + " = " + cscA.doubleValue());
        Double Arad = Math.asin(sinA.doubleValue());
        Double arcsin = Math.asin(sinA.doubleValue());
        Double arccos = Math.acos(cosA.doubleValue());
        Double arctan = Math.atan(tanA.doubleValue());
        System.out.println("arcsin = " + Math.toDegrees(arcsin));
        System.out.println("arccos = " + Math.toDegrees(arccos));
        System.out.println("arctan = " + Math.toDegrees(arctan));
        System.out.println(Math.sqrt(40D));
    }
}
