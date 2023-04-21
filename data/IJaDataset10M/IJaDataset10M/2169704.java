package org.mosquitos.chat;

import net.jxta.discovery.DiscoveryService;
import net.jxta.peergroup.PeerGroup;

public class AdvertisementDiscoveryService implements Runnable {

    private PeerGroup pg;

    private int type;

    private String tagName, name;

    public AdvertisementDiscoveryService(PeerGroup pg, int type) {
        this(pg, type, null, null);
    }

    public AdvertisementDiscoveryService(PeerGroup pg, int type, String tagName, String name) {
        this.pg = pg;
        this.type = type;
        this.tagName = tagName;
    }

    public void run() {
        DiscoveryService ds = pg.getDiscoveryService();
        long waittime = 1 * 2 * 1000L;
        while (true) {
            ds.addDiscoveryListener(new DiscoveryEventListenter());
            ds.getRemoteAdvertisements(null, type, tagName, name, 1);
            synchronized (this) {
                try {
                    this.wait(waittime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
