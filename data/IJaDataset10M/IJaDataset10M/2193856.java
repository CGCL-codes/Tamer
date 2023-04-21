package algebra;

import java.util.Arrays;
import structs.Complex;

public class Quadratic {

    public static double[] solve(double a, double b, double c) {
        double square = b * b - 4.0 * a * c;
        if (square > 0) {
            System.out.println("R1:=" + ((-b + Math.sqrt(square)) / (2.0 * a)));
            System.out.println("R2:=" + ((-b - Math.sqrt(square)) / (2.0 * a)));
            return new double[] { ((-b + Math.sqrt(square)) / 2.0 * a), ((-b - Math.sqrt(square)) / 2.0 * a) };
        } else if (square == 0) {
            System.out.println("R1:=" + ((-b + Math.sqrt(square)) / (2.0 * a)));
            return new double[] { ((-b + Math.sqrt(square)) / (2.0 * a)) };
        } else {
            Complex r1 = new Complex(-b / (2.0 * a), Math.sqrt(Math.abs(b * b - 4.0 * a * c)) / (2.0d * a));
            Complex r2 = new Complex(-b / (2.0 * a), -Math.sqrt(Math.abs(b * b - 4.0 * a * c)) / (2.0d * a));
            System.out.println("R1:=" + r1.getReal() + " + " + r1.getImaginary() + "i");
            System.out.println("R2:=" + r2.getReal() + " + " + r2.getImaginary() + "i");
            return new double[] { r1.getReal(), r1.getImaginary(), r2.getReal(), r2.getImaginary() };
        }
    }

    public static double[] solve(int a, int b, int c) {
        double square = b * b - 4.0 * a * c;
        if (square > 0) {
            System.out.println("R1:=" + ((-b + Math.sqrt(square)) / (2.0 * a)));
            System.out.println("R2:=" + ((-b - Math.sqrt(square)) / (2.0 * a)));
            return new double[] { ((-b + Math.sqrt(square)) / 2.0 * a), ((-b - Math.sqrt(square)) / 2.0 * a) };
        } else if (square == 0) {
            System.out.println("R1:=" + ((-b + Math.sqrt(square)) / (2.0 * a)));
            return new double[] { ((-b + Math.sqrt(square)) / (2.0 * a)) };
        } else {
            Complex r1 = new Complex(-b / (2.0 * a), Math.sqrt(Math.abs(b * b - 4.0 * a * c)) / (2.0d * a));
            Complex r2 = new Complex(-b / (2.0 * a), -Math.sqrt(Math.abs(b * b - 4.0 * a * c)) / (2.0d * a));
            System.out.println("R1:=" + r1.getReal() + " + " + r1.getImaginary() + "i");
            System.out.println("R2:=" + r2.getReal() + " + " + r2.getImaginary() + "i");
            return new double[] { r1.getReal(), r1.getImaginary(), r2.getReal(), r2.getImaginary() };
        }
    }
}
