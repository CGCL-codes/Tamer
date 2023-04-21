package org.pockit.model;

import java.util.Vector;
import org.pockit.PockIt;
import org.pockit.communication.BlipFetcher;

public class BubbleShowModel extends Model {

    private Bubble bubble;

    private Vector blips = new Vector();

    public BubbleShowModel(PockIt parent) {
        super(parent);
    }

    public void refresh() {
        Thread fetchThread = new Thread() {

            public void run() {
                BlipFetcher fetcher = new BlipFetcher(bubble);
                fetcher.fetch();
                blips = fetcher.getBlips();
                informViews();
            }
        };
        fetchThread.start();
    }

    public void setBubble(Bubble bubble) {
        this.bubble = bubble;
    }

    public Bubble getBubble() {
        return bubble;
    }

    public Vector getBlips() {
        return blips;
    }
}
