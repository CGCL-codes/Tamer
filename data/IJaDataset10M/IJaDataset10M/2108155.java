package com.jcrawler.util;

import java.util.*;

/**
 * One of our goals in load-test-crawling a web-application is to be able to
 * figure-out which URLs are the slowest, so we can address then and see how
 * (if at all) we can make them quicker and thus improve overall application
 * performance.
 *
 * This is a class that tracks URL processing speed and provides encapsulated
 * way of tracking/managing this information.
 */
public class DashBoard {

    public static final int SIZE = 20;

    public static final Object watch = new String("w");

    public static SortedSet urlList = new TreeSet();

    /**
   * Add a new URL and its time to the dashboard. It will only get into the
   * dashboard if it is slow-enough to deserve, such an honor, of course :)
   * @param url String
   * @param time int
   */
    public static void add(String url, long time) {
        UrlRecord newSlowUrl = new UrlRecord(url, time);
        if (urlList.size() < DashBoard.SIZE) {
            urlList.add(newSlowUrl);
        } else {
            UrlRecord minElement = (UrlRecord) urlList.first();
            long minTime = minElement.processTime;
            if (time < minTime) return;
            synchronized (DashBoard.watch) {
                urlList.remove(minElement);
                urlList.add(newSlowUrl);
            }
        }
    }

    /**
   * Return the string containing the list of record URLs
   */
    public static String print() {
        String ret = "";
        synchronized (DashBoard.watch) {
            if (urlList != null) {
                Iterator it = DashBoard.urlList.iterator();
                while (it.hasNext()) {
                    UrlRecord u = (UrlRecord) it.next();
                    String url = u.url;
                    long time = u.processTime;
                    ret += (" Time: " + time + " URL: " + url + "\n");
                }
            } else {
                ret += (" List of the slowest URLs is empty ");
            }
        }
        return ret;
    }
}

class UrlRecord implements Comparable {

    public String url;

    public long processTime;

    public UrlRecord(String url, long processTime) {
        this.url = url;
        this.processTime = processTime;
    }

    public int compareTo(Object o) throws ClassCastException {
        long k = (int) this.processTime - ((UrlRecord) o).processTime;
        if (k > 0) {
            return 1;
        } else if (k < 0) {
            return -1;
        } else return 0;
    }

    public boolean equals(Object o) {
        return (processTime == ((UrlRecord) o).processTime);
    }
}
