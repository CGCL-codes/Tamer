package ai9hells.pgaibe.chapter2.westworld1;

/**
 *
 * @author andre.bandarra
 */
public class Main {

    /** Creates a new instance of Main */
    public Main() {
    }

    public static void main(String[] args) {
        Miner m = new Miner(EntityNames.MINER_BOB);
        for (int i = 0; i < 20; i++) {
            m.update();
            try {
                Thread.currentThread().sleep(800);
            } catch (InterruptedException ie) {
                ;
            }
        }
        System.exit(0);
    }
}
