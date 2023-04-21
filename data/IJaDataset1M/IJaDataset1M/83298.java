package synchro;

/**
 *
 * @author boomar
 */
public class Prog1 {

    public static void main(String args[]) throws InterruptedException {
        Registre R = new Registre(10);
        monThread Th1 = new monThread(R);
        monThread Th2 = new monThread(R);
        Th1.start();
        Th2.start();
        Th1.join();
        Th2.join();
        long[] T = R.get();
        System.out.println("Valeur finale du Registre ");
        for (int i = 0; i < T.length; i++) {
            System.out.println(T[i]);
        }
    }
}
