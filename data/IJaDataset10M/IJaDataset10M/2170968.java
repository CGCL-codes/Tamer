package productorconsumidor;

/**
 *
 * @author Risvil
 */
public class Main {

    private static final int NPRODUCTORES = 5;

    private static final int NCONSUMIDORES = 5;

    static Buffer buf = new Buffer(3);

    private static Productor productores[] = new Productor[NPRODUCTORES];

    private static Consumidor consumidores[] = new Consumidor[NCONSUMIDORES];

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (int i = 0; i < NPRODUCTORES; ++i) {
            productores[i] = new Productor(buf, i);
            productores[i].start();
        }
        for (int j = 0; j < NCONSUMIDORES; ++j) {
            consumidores[j] = new Consumidor(buf, j);
            consumidores[j].start();
        }
        for (int i = 0; i < NPRODUCTORES; ++i) {
            try {
                productores[i].join();
            } catch (Exception e) {
            }
        }
        for (int j = 0; j < NCONSUMIDORES; ++j) {
            try {
                consumidores[j].join();
            } catch (Exception e) {
            }
        }
        System.out.println("Main: Imprimiendo buffer: ");
        buf.printBuffer();
        System.out.println("");
        System.out.println("Fin del hilo main");
    }
}
