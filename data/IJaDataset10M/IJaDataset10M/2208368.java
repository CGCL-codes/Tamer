package env3d;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import motej.Mote;
import motej.MoteFinder;
import motej.MoteFinderListener;

public class EnvMoteFinder implements MoteFinderListener {

    private Logger log = LoggerFactory.getLogger(EnvMoteFinder.class);

    private MoteFinder finder;

    private Object lock = new Object();

    private Mote mote;

    public void moteFound(Mote mote) {
        log.info("SimpleMoteFinder received notification of a found mote.");
        this.mote = mote;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public Mote findMote() throws Exception {
        if (finder == null) {
            finder = MoteFinder.getMoteFinder();
            finder.addMoteFinderListener(this);
        }
        finder.startDiscovery();
        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException ex) {
            log.error(ex.getMessage(), ex);
        }
        return mote;
    }
}
