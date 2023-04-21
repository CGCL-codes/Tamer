package fracas.formula;

import fracas.math.*;

public class MagnetM1Formula extends Formula {

    double diverge = 4.0d;

    public MagnetM1Formula() {
        iterations = 0;
    }

    public MagnetM1Formula(int its) {
        iterations = its;
    }

    public String getParam() {
        String toReturn = "";
        toReturn += "Mandelbrot";
        return toReturn;
    }

    public int calculate(Complex cx) {
        Complex c = cx;
        Complex zn = new Complex(0, 0, 0);
        Complex zn1 = new Complex(0, 0, 0);
        int count = 1;
        while (count < iterations) {
            zn1 = (zn.pow(2)).add(c.subtract(new Complex(1, 0, 0)));
            zn1 = zn1.divide(zn.multiply(new Complex(2, 0, 0)).add(c.subtract(new Complex(2, 0, 0))));
            zn1 = zn1.pow(2);
            if (zn1.getMod() > diverge) {
                return count;
            }
            zn = zn1;
            count++;
        }
        return count;
    }
}
