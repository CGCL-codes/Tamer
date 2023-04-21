package Geometria;

public class Test {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Figura a = new Quadrato(3);
        System.out.println("area Quadrato: " + a.Area());
        System.out.println("perimetro Quadrato: " + a.Perimetro());
        System.out.println();
        Figura b = new Triangolo(3, 4, 4, 5, 2);
        System.out.println("area Triangolo: " + b.Area());
        System.out.println("perimetro Triangolo: " + b.Perimetro());
        System.out.println();
        Figura c = new Cerchio(3);
        System.out.println("Area Cerchio: " + c.Area());
        System.out.println("Circonferenza: " + c.Perimetro());
        System.out.println();
    }
}
