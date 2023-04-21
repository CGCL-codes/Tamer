package com.fusteeno.gnutella.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import com.fusteeno.UserPreferences;
import com.fusteeno.gnutella.net.Message;
import com.fusteeno.gnutella.net.Servent;

public class ActiveSevent implements Runnable {

    private static ActiveSevent instance = null;

    private static final int TTLSERVENT = 60 * 1000;

    private static final int SLEEPTIME = 30 * 1000;

    private UserPreferences userPrefs = UserPreferences.getInstance();

    private HashMap<String, Item> listActiveServents = new HashMap<String, Item>(2 * userPrefs.getMaxConnections());

    private boolean alive;

    private Thread thread;

    public static ActiveSevent getInstance() {
        if (instance == null) instance = new ActiveSevent();
        return instance;
    }

    /**
	 * Adds a connected servent
	 * THREAD-SAFE
	 * @param s servent to add
	 * @
	 */
    public void add(Servent s) {
        Item i = new Item(s);
        synchronized (listActiveServents) {
            listActiveServents.put(i.getServent().getIp() + ":" + i.getServent().getPort(), i);
        }
    }

    /**
	 * Returns true if the servent is inside the list
	 * THREAD-SAFE
	 * @param s servent
	 * @return true if the list contains the servent, false otherwise
	 */
    public boolean contains(Servent s) {
        boolean inside = false;
        synchronized (listActiveServents) {
            inside = listActiveServents.containsKey(s.getIp() + ":" + s.getPort());
        }
        return inside;
    }

    /**
	 * Removes a servent 
	 * THREAD-SAFE
	 * @param s servent to remove
	 */
    public void remove(Servent s) {
        synchronized (listActiveServents) {
            listActiveServents.remove(s.getIp() + ":" + s.getPort());
        }
    }

    /**
	 * Marks a servent as active
	 * THREAD-SAFE
	 * @param s sevent to mark active
	 */
    public void hit(Servent s) {
        synchronized (listActiveServents) {
            Item i = listActiveServents.get(s.getIp() + ":" + s.getPort());
            if (i != null) i.hit(); else Debug.log("[ActiveServent] Servent non trovato (" + s.getIp() + ":" + s.getPort() + ")");
        }
    }

    /**
	 * Send a msg to all servents
	 * THREAD-SAFE
	 * @param msg msg to send
	 * @param priority priority's msg
	 * @param source servent che invia il msg (null per se stesso)
	 */
    public void sendMessage(Message msg, int priority, Servent source) {
        synchronized (listActiveServents) {
            Iterator<Item> iter = listActiveServents.values().iterator();
            while (iter.hasNext()) {
                Servent servent = iter.next().getServent();
                if (!servent.equals(source)) servent.send(msg, priority);
            }
        }
    }

    public synchronized void start() {
        if (!alive) {
            alive = true;
            thread = new Thread(this);
            thread.setName("ActiveSevent Thread");
            thread.start();
            Debug.log("[ActiveServent] ActiveServent started");
        }
    }

    public void run() {
        while (alive) {
            try {
                Thread.sleep(SLEEPTIME);
                synchronized (listActiveServents) {
                    Iterator<Item> iter = listActiveServents.values().iterator();
                    while (iter.hasNext()) {
                        Item i = iter.next();
                        if ((System.currentTimeMillis() - i.getCreationTime()) > TTLSERVENT) {
                            Debug.log("[ActiveServent] Servent " + i.getServent().getIp() + ":" + i.getServent().getPort() + " inattivo: STOP");
                            iter.remove();
                            i.getServent().stop();
                        }
                    }
                }
            } catch (InterruptedException e) {
            }
        }
    }

    /**
	 * Numbers of serverts inside the list (connecteds)
	 * THREAD-SAFE
	 * @return
	 */
    public int size() {
        synchronized (listActiveServents) {
            return listActiveServents.size();
        }
    }

    /**
	 * Returns the list of servent's addresses connecteds
	 * THREAD-SAFE
	 * @return list of addresses
	 */
    public Collection<String> getAddresses() {
        synchronized (listActiveServents) {
            return listActiveServents.keySet();
        }
    }

    public synchronized void stop() {
        if (alive) {
            alive = false;
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                Debug.log("[ActiveServent] Thread interrotto: " + e.getMessage());
            }
            synchronized (listActiveServents) {
                Iterator<Item> iter = listActiveServents.values().iterator();
                while (iter.hasNext()) {
                    Servent s = iter.next().getServent();
                    iter.remove();
                    s.stop();
                }
            }
            Debug.log("[ActiveServent] ActiveServent stopped.");
        }
    }

    private class Item {

        private long creationTime;

        private Servent servent;

        public long getCreationTime() {
            return creationTime;
        }

        public Servent getServent() {
            return servent;
        }

        public Item(Servent s) {
            creationTime = System.currentTimeMillis();
            servent = s;
        }

        public void hit() {
            creationTime = System.currentTimeMillis();
        }
    }
}
