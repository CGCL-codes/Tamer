package eu.bseboy.tvrss.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FeedList {

    private List<String> feedList = new ArrayList<String>();

    public void addFeed(String feedURL) {
        feedList.add(feedURL);
    }

    public Iterator<String> iterator() {
        return feedList.iterator();
    }
}
