package org.pockit.model;

import java.util.Vector;
import org.pockit.PockIt;
import org.pockit.communication.PostFetcher;

public class PlaceShowModel extends ThreadedModel {

    private Place place;

    private Vector posts = new Vector();

    public PlaceShowModel(PockIt parent) {
        super(parent);
    }

    public void whileRunning() {
    }

    public void refresh() {
        Thread fetchThread = new Thread() {

            public void run() {
                PostFetcher fetcher = new PostFetcher(place);
                fetcher.fetch();
                posts = fetcher.getPosts();
                informViews();
            }
        };
        fetchThread.start();
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Place getPlace() {
        return place;
    }

    public Vector getPosts() {
        return posts;
    }
}
