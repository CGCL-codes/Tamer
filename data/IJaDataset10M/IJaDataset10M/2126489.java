package practica2;

public class Consumidor extends Thread {

    private static Cua buffer;

    private String name;

    private int oat;

    private boolean l;

    public Consumidor(Cua buffertw, String name, int objectes_a_treure, boolean lent_i_aleatori) {
        buffer = buffertw;
        this.name = name;
        oat = objectes_a_treure;
        l = lent_i_aleatori;
    }

    public void run() {
        for (int i = 0; i < oat; i++) {
            if (!buffer.isEmpty()) {
                System.err.println("Consumidor " + name + " ha extret l'objecte '" + buffer.get() + "'.");
            } else {
                i--;
                System.err.println("Consumidor " + name + " s'espera perqu� la cua esta buida.");
            }
            try {
                if (l) sleep(1000 + (long) (Math.random() * 1000)); else sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.err.println("Consumidor " + name + " ha acabat de recollir tots els seus elements.");
    }
}
