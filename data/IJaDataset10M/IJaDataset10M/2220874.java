package net.sourceforge.ljm.dump;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

class DumperThreadHandlerFactory implements ThreadFactory {

    private static final ThreadGroup tg = new ThreadGroup("Dumper");

    private static final AtomicLong cont = new AtomicLong();

    public Thread newThread(Runnable r) {
        return new Thread(tg, r, String.format("DumperHandler-%d", cont.getAndIncrement()));
    }
}
