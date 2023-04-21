package ordo;

public class ConnectionThread extends Thread {

    Connection c;

    protected boolean readOne;

    public ConnectionThread(Connection c, boolean readOne) {
        this.c = c;
        this.readOne = readOne;
    }

    public void run() {
        if (readOne) c.readEvent();
        while (!c.isSecure()) {
            synchronized (c) {
                try {
                    c.wait();
                } catch (InterruptedException ie) {
                }
            }
        }
        while (c.readEvent()) {
        }
    }
}
