package si.mk.k3;

import junit.framework.TestCase;
import org.cheffo.jeplite.ParseException;
import org.nfunk.jep.JEP;

/**
 * This class was written only to measure performance of expression parsers.
 * First I was using JEP from singluarsystems, but it was VERY slow.
 * The same expressions , which JEPLite evaluates in 0.13 s, JEP tool almost
 * 11 seconds!
 */
public class ParserPerformanceTest extends TestCase {

    private static final int NUM_VARS = 3;

    private static final int NUM_EXPRESSIONS = 6;

    private static final int CYCLES = 100;

    private String expressions[];

    public void testJEP() {
        System.out.println("Testin JEP...");
        JEP jep = new JEP();
        jep.addStandardFunctions();
        jep.addStandardConstants();
        jep.setAllowAssignment(true);
        expressions = new String[NUM_EXPRESSIONS];
        expressions[0] = "1";
        expressions[1] = "1 + 1";
        expressions[2] = "x1";
        expressions[3] = "x1 * 2";
        expressions[4] = "x1 * 2 + 3 * x3 + x2 / x1";
        expressions[5] = "sin(x1)";
        for (int i = 0; i < NUM_VARS; i++) {
            jep.addVariable("x" + (i + 1), i + 3);
        }
        long t[] = new long[NUM_EXPRESSIONS];
        long t0 = 0;
        for (int j = 0; j < NUM_EXPRESSIONS; j++) {
            t0 = System.nanoTime();
            for (int i = 0; i < CYCLES; i++) {
                jep.parseExpression(expressions[j]);
                double x = jep.getValue();
                if (i == 10) {
                    System.out.println(x);
                }
            }
            t[j] = System.nanoTime() - t0;
        }
        double sum = 0;
        for (int j = 0; j < NUM_EXPRESSIONS; j++) {
            System.out.println("time = " + t[j] / 1e6 + "    exp = " + expressions[j]);
            sum += t[j];
        }
        System.out.println("Sum = " + sum / 1e9);
    }

    public void testJEPLite() {
        System.out.println("Testin JEPLite ...");
        org.cheffo.jeplite.JEP jep = new org.cheffo.jeplite.JEP();
        jep.addStandardFunctions();
        jep.addStandardConstants();
        expressions = new String[NUM_EXPRESSIONS];
        expressions[0] = "1";
        expressions[1] = "1 + 1";
        expressions[2] = "x1";
        expressions[3] = "x1 * 2";
        expressions[4] = "x1 * 2 + 3 * x3 + x2 / x1";
        expressions[5] = "sin(x1)";
        for (int i = 0; i < NUM_VARS; i++) {
            jep.addVariable("x.a" + (i + 1), i + 3);
        }
        long t[] = new long[NUM_EXPRESSIONS];
        long t0 = 0;
        try {
            for (int j = 0; j < NUM_EXPRESSIONS; j++) {
                t0 = System.nanoTime();
                for (int i = 0; i < CYCLES; i++) {
                    jep.parseExpression(expressions[j]);
                    double x = jep.getValue();
                    if (i == 10) {
                        System.out.println(x);
                    }
                }
                t[j] = System.nanoTime() - t0;
            }
        } catch (ParseException ex) {
            System.out.println(ex);
        }
        double sum = 0;
        for (int j = 0; j < NUM_EXPRESSIONS; j++) {
            System.out.println("time = " + t[j] / 1e6 + "    exp = " + expressions[j]);
            sum += t[j];
        }
        System.out.println("Sum = " + sum / 1e9);
    }

    public void testOlikurtParser() {
    }
}
