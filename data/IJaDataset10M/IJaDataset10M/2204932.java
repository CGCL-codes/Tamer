package shu.math.test;

import shu.math.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public class CubicTester {

    public static void main(String[] args) {
        double[] xn = { 380, 390, 400, 410, 420, 430, 440, 450, 460, 470, 480, 490, 500, 510, 520, 530, 540, 550, 560, 570, 580, 590, 600, 610, 620, 630, 640, 650, 660, 670, 680, 690, 700, 710, 720, 730, 740, 750, 760, 770, 780 };
        double[] yn = { 0, 0.0003, 0.001, 0.0013, 0.0011, 0.0084, 0.0305, 0.0502, 0.0708, 0.1023, 0.1544, 0.231, 0.3443, 0.4902, 0.6644, 0.8229, 0.9341, 0.9932, 1, 0.9587, 0.8739, 0.7579, 0.6233, 0.4855, 0.3577, 0.25, 0.1671, 0.1068, 0.066, 0.04, 0.0239, 0.0139, 0.0085, 0.005, 0.003, 0.0017, 0.001, 0.0006, 0.0003, 0.0002, 0.0001 };
        Interpolation interp = new Interpolation(xn, yn);
        for (int x = 380; x <= 780; x++) {
            double r = interp.interpolate(x, Interpolation.Algo.CubicPolynomial);
            System.out.println(r);
        }
    }
}
