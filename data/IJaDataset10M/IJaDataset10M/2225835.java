package br.org.eteg.curso.javaoo.capitulo02;

public class ExemploShift {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        int a = -10;
        int b = a >>> 2;
        System.out.println("a: " + a + " = " + Integer.toBinaryString(a));
        System.out.println("b: " + b + " = " + Integer.toBinaryString(b));
    }
}
