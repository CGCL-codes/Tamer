package webserg.pazzlers.ch5;

public class Indecisive {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        System.out.println(decision());
    }

    private static boolean decision() {
        try {
            return true;
        } finally {
            return false;
        }
    }
}
