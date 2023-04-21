package org.fudaa.dodico.all;

/**
 * @version      $Revision: 1.1 $ $Date: 2006-09-19 14:45:46 $ by $Author: deniger $
 * @author       Mickael Rubens
 */
public final class MainTest {

    private MainTest() {
    }

    /**
   * @param args
   */
    public static void main(final String[] args) {
        final double d = 0.00999E-321;
        System.out.println("double: " + d);
        System.out.println("coucou");
        System.out.println(1100 / 16);
        System.out.println(1100 % 16);
        System.out.println(Double.toString(0.000005));
    }
}
