package com.afaker.rss.feed.biz;

import com.afaker.rss.feed.ROMEFeedImpl;
import com.afaker.rss.feed.EntrySet;
import com.afaker.rss.feed.Feed;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.impl.HttpClientFeedFetcher;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.io.FeedException;
import com.afaker.rss.http.HttpConnector;
import com.afaker.rss.search.Indexer;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedOutput;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bruce
 */
public class ROMEFeedFetcher implements FeedFetcher {

    private HttpClientFeedFetcher httpFetcher = new HttpClientFeedFetcher();

    private HttpURLFeedFetcher urlFetcher = new HttpURLFeedFetcher();

    private HttpConnector connector;

    private FeedDownloader download;

    private Feed feed;

    private String url;

    public static final int UNKNOWN = 0;

    public static final int WEB_FILE = 1;

    public static final int LOCAL_FILE = 2;

    private int fileType;

    private boolean isNew;

    private long lastRefresh;

    private long refreshPeriod;

    private static final double ditherFactor = 0.1D;

    private int MAX_LIVES = 3;

    private int lives = MAX_LIVES;

    private boolean isFirstLoad = true;

    private EntrySet newComing;

    private Indexer indexer;

    /** Creates a new instance of ROMEFeedFetcher */
    public ROMEFeedFetcher(String url) throws IOException, FeedException, FetcherException, Exception {
        this.url = url;
        feed = new ROMEFeedImpl();
        feed.build(url);
    }

    public boolean download(String filePath) throws FileNotFoundException, FeedException {
        SyndFeedOutput out = new SyndFeedOutput();
        try {
            out.output((SyndFeed) feed.getFeed(), new File(filePath));
        } catch (IOException ex) {
            Logger.getLogger(ROMEFeedFetcher.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        indexer = new Indexer();
        indexer.index(filePath);
        return true;
    }

    public synchronized boolean refresh() {
        if (now() < lastRefresh + refreshPeriod + getPeriodDither()) {
            return false;
        }
        if (connector == null) {
            connector = new HttpConnector(getURIString());
        }
        isNew = connector.load();
        if (isNew) {
            lives = MAX_LIVES;
            FeedExtractor extractor = new FeedExtractor(getURIString());
            try {
                newComing = extractor.extract();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (FeedException ex) {
                ex.printStackTrace();
            } catch (FetcherException ex) {
                ex.printStackTrace();
            }
        } else {
            if (connector.isDead()) {
                lives--;
                refreshPeriod = refreshPeriod * 2;
            }
        }
        lastRefresh = now();
        System.out.println(connector.getStatus());
        System.out.println();
        return isNew;
    }

    public long now() {
        return (new Date()).getTime();
    }

    public String getURIString() {
        return url;
    }

    public boolean shouldExpire() {
        return lives < 1;
    }

    public boolean isNew() {
        return isNew;
    }

    public boolean isDead() {
        return connector.isDead();
    }

    public String getETag() {
        return connector.getETag();
    }

    public String getLastModified() {
        return connector.getLastModified();
    }

    public long getRefreshPeriod() {
        return refreshPeriod;
    }

    public void setRefreshPeriod(long refreshPeriod) {
        this.refreshPeriod = refreshPeriod;
    }

    public long getLastRefresh() {
        return lastRefresh;
    }

    public EntrySet getNewEntries() {
        return newComing;
    }

    public Feed getFeed() {
        return feed;
    }

    private long getPeriodDither() {
        return (long) (Math.random() * ditherFactor * refreshPeriod);
    }
}
