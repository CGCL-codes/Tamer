package algorithm.nibble;

import junit.framework.TestCase;

public class TesteNibble extends TestCase {

    public void testarMultiplication() {
        int[] number = new int[2];
        number[0] = 1;
        number[1] = 0;
        Nibble.show(number);
        int[] number2 = new int[2];
        number2[0] = 1;
        number2[1] = 0;
        Nibble.show(number2);
        int[] res = Nibble.multiply2(number, number2);
        Nibble.show(res);
        verificarValor(res, 0, 1, 0, 0);
    }

    public void testarMultiplication2() {
        int[] number = new int[2];
        number[0] = 1;
        number[1] = 0;
        Nibble.show(number);
        int[] number2 = new int[2];
        number2[0] = 1;
        number2[1] = 1;
        Nibble.show(number2);
        int[] res = Nibble.multiply2(number, number2);
        Nibble.show(res);
        verificarValor(res, 0, 1, 1, 0);
    }

    public void testarFactorial2() {
        int[] number = new int[2];
        number[0] = 1;
        number[1] = 0;
        Nibble.show(number);
        int[] number2 = new int[2];
        number2[0] = 1;
        number2[1] = 0;
        Nibble.subtract(number2);
        Nibble.show(number2);
        int[] res = null;
        while (!Nibble.isZero(number2)) {
            res = Nibble.multiply2(number, number2);
            Nibble.subtract(number2);
        }
        Nibble.show(res);
    }

    private void verificarValor(int[] res, int... a) {
        assertEquals(res.length, a.length);
        for (int i = 0; i < a.length; i++) {
            assertEquals(res[i], a[i]);
        }
    }
}
