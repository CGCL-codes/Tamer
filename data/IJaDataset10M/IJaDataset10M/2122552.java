package buffferc;

import buffering.*;

/**
 *
 * @author aturmeda1
 */
public class Lector<E> extends Thread {

    private ConcurrentBuffer<E> buffer;

    public Lector(ConcurrentBuffer<E> buffer) {
        this.buffer = buffer;
    }

    public void run() {
        E value;
        while (true) {
            try {
                sleep(500);
            } catch (InterruptedException ie) {
            }
            value = buffer.receive();
            System.out.println("\tEs rep " + value);
        }
    }
}
