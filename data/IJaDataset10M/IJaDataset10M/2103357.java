package ru.jnano.math;

import ru.jnano.math.calc.interpolation.Polinom;

public class PolinomTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        System.out.println(Polinom.Line(0, 101324.72, 11000, 22690.07118, 343.97));
        System.out.println(Polinom.Line(11000, 22690.07118, 0, 101324.72, 343.97));
    }
}
