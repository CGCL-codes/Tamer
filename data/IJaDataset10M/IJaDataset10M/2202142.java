package net.sf.bt747.j2se.system;

import java.util.concurrent.Semaphore;
import bt747.sys.interfaces.BT747Semaphore;

/**
 * @author Mario De Weerd
 */
public final class J2SESemaphore implements BT747Semaphore {

    private Semaphore available;

    public J2SESemaphore(final int value) {
        available = new Semaphore(value, true);
    }

    public void down() {
        try {
            available.acquire();
        } catch (final InterruptedException e) {
        }
    }

    public void up() {
        available.release();
    }
}
