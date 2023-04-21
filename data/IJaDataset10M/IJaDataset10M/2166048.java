package apps;

public class Prueba {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        double R = 6.371e+6;
        for (double l = 1000; l < 1000000; l *= 10) {
            double h = Math.sqrt(l * l + R * R) - R;
            System.out.println("" + l + " ---> " + h);
        }
    }
}
