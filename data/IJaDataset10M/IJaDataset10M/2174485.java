package sun.net.httpserver;

import java.util.*;
import java.nio.*;
import java.net.*;
import java.io.*;
import java.security.*;
import java.nio.channels.*;

public class SelectorCache {

    static SelectorCache cache = null;

    private SelectorCache() {
        freeSelectors = new LinkedList<SelectorWrapper>();
        CacheCleaner c = AccessController.doPrivileged(new PrivilegedAction<CacheCleaner>() {

            public CacheCleaner run() {
                CacheCleaner cleaner = new CacheCleaner();
                cleaner.setDaemon(true);
                return cleaner;
            }
        });
        c.start();
    }

    /**
     * factory method for creating single instance
     */
    public static SelectorCache getSelectorCache() {
        synchronized (SelectorCache.class) {
            if (cache == null) {
                cache = new SelectorCache();
            }
        }
        return cache;
    }

    private static class SelectorWrapper {

        private Selector sel;

        private boolean deleteFlag;

        private SelectorWrapper(Selector sel) {
            this.sel = sel;
            this.deleteFlag = false;
        }

        public Selector getSelector() {
            return sel;
        }

        public boolean getDeleteFlag() {
            return deleteFlag;
        }

        public void setDeleteFlag(boolean b) {
            deleteFlag = b;
        }
    }

    LinkedList<SelectorWrapper> freeSelectors;

    synchronized Selector getSelector() throws IOException {
        SelectorWrapper wrapper = null;
        Selector selector;
        if (freeSelectors.size() > 0) {
            wrapper = freeSelectors.remove();
            selector = wrapper.getSelector();
        } else {
            selector = Selector.open();
        }
        return selector;
    }

    synchronized void freeSelector(Selector selector) {
        freeSelectors.add(new SelectorWrapper(selector));
    }

    class CacheCleaner extends Thread {

        public void run() {
            long timeout = ServerConfig.getSelCacheTimeout() * 1000;
            while (true) {
                try {
                    Thread.sleep(timeout);
                } catch (Exception e) {
                }
                synchronized (freeSelectors) {
                    ListIterator<SelectorWrapper> l = freeSelectors.listIterator();
                    while (l.hasNext()) {
                        SelectorWrapper w = l.next();
                        if (w.getDeleteFlag()) {
                            try {
                                w.getSelector().close();
                            } catch (IOException e) {
                            }
                            l.remove();
                        } else {
                            w.setDeleteFlag(true);
                        }
                    }
                }
            }
        }
    }
}
