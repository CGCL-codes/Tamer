package RssIO;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author sahaqiel
 */
public class FeedManager {

    private static FeedManager instance;

    private boolean opened = false;

    private File folderForStorage;

    private FeedObjectIfc fetchedFeed;

    private HashMap<String, ArrayList<String>> urlCache = new HashMap<String, ArrayList<String>>();

    private ArrayList<FeedObjectIfc> feedCache = new ArrayList<FeedObjectIfc>();

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public static final String FEED_ADDED_PROPERTY = "feedAddedProperty";

    public static final String FEED_FETCHED_PROPERTY = "feedFetchedProperty";

    public static final String FEED_REMOVED_PROPERTY = "feedRemovedProperty";

    private final String NOT_OPEN = "Manager has not been opened";

    private FeedManager() {
    }

    public void addFeed(String urlStr) {
        if (!opened) {
            throw new IllegalArgumentException(NOT_OPEN);
        }
        URL feedUrl = null;
        if (urlStr != null) {
            try {
                feedUrl = new URL(urlStr);
                final String KEY = feedUrl.getHost();
                if (urlCache.containsKey(KEY)) {
                    ArrayList<String> urlList = urlCache.get(KEY);
                    urlList.add(feedUrl.toExternalForm());
                    urlCache.put(KEY, urlList);
                } else {
                    ArrayList<String> urlList = new ArrayList<String>();
                    urlList.add(feedUrl.toExternalForm());
                    urlCache.put(KEY, urlList);
                }
                pcs.firePropertyChange(FEED_ADDED_PROPERTY, urlStr, "");
            } catch (IOException ex) {
                out("could not open feed at " + urlStr);
                ex.printStackTrace();
            }
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (!opened) {
            throw new IllegalArgumentException(NOT_OPEN);
        }
        if (listener != null) {
            pcs.addPropertyChangeListener(listener);
        }
    }

    public void close() {
        if (!opened) {
            throw new IllegalArgumentException(NOT_OPEN);
        }
        BufferedWriter writer = null;
        try {
            for (String host : urlCache.keySet()) {
                String path = folderForStorage + File.separator + host;
                writer = new BufferedWriter(new FileWriter(path));
                for (String urlStr : urlCache.get(host)) {
                    writer.write(urlStr);
                    writer.newLine();
                }
                writer.flush();
            }
            opened = false;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void fetchAllFeeds() {
        Thread fetchAll = new Thread() {

            @Override
            public void run() {
                privateFetchAllFeeds();
            }
        };
        fetchAll.start();
    }

    private void privateFetchAllFeeds() {
        if (!opened) {
            throw new IllegalArgumentException(NOT_OPEN);
        }
        try {
            for (String key : urlCache.keySet()) {
                for (String siteUrl : urlCache.get(key)) {
                    URL feedUrl = new URL(siteUrl);
                    FeedObjectIfc feedObj = new FeedFetcher().getFeedObject(feedUrl);
                    if (feedObj != null) {
                        feedCache.add(feedObj);
                        pcs.firePropertyChange(FEED_FETCHED_PROPERTY, feedUrl.toExternalForm(), "");
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public FeedObjectIfc fetchFeed(final String urlStr) {
        out("fetching " + urlStr);
        Thread fetch = new Thread() {

            @Override
            public void run() {
                setFetchedFeed(privateFetchFeed(urlStr));
            }
        };
        fetch.start();
        return fetchedFeed;
    }

    private void setFetchedFeed(FeedObjectIfc feedObject) {
        this.fetchedFeed = feedObject;
    }

    private FeedObjectIfc privateFetchFeed(String urlStr) {
        if (!opened) {
            throw new IllegalArgumentException(NOT_OPEN);
        }
        if (urlStr == null) {
            return null;
        }
        for (int i = 0; i < feedCache.size(); i++) {
            String curUrl = feedCache.get(i).getSiteUrl();
            if (curUrl.equals(urlStr)) {
                return feedCache.get(i);
            }
        }
        boolean foundInUrls = false;
        for (String host : urlCache.keySet()) {
            for (String url : urlCache.get(host)) {
                if (url.equals(urlStr)) {
                    foundInUrls = true;
                }
            }
        }
        try {
            URL feedUrl = new URL(urlStr);
            if (!foundInUrls) {
                ArrayList<String> sites = new ArrayList<String>();
                sites.add(feedUrl.toExternalForm());
                urlCache.put(feedUrl.getHost(), sites);
            }
            FeedObjectIfc feedObj = new FeedFetcher().getFeedObject(feedUrl);
            if (feedObj != null) {
                feedCache.add(feedObj);
                pcs.firePropertyChange(FEED_FETCHED_PROPERTY, feedUrl.toExternalForm(), "");
                return feedObj;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public FeedObjectIfc getFeed(String urlStr) {
        if (!opened) {
            throw new IllegalArgumentException(NOT_OPEN);
        }
        if (feedCache.isEmpty()) {
            return null;
        }
        out("getFeedAtUrl " + urlStr);
        for (FeedObjectIfc curFeed : feedCache) {
            if (curFeed.getSiteUrl() != null) {
                if (curFeed.getSiteUrl().equals(urlStr)) {
                    return curFeed;
                }
            }
        }
        return null;
    }

    public static FeedManager getInstance() {
        if (instance == null) {
            instance = new FeedManager();
        }
        return instance;
    }

    public void open() {
        folderForStorage = new File(System.getProperty("user.home") + File.separatorChar + ".feedReader" + File.separatorChar);
        if (!folderForStorage.exists()) {
            boolean created = folderForStorage.mkdir();
            out(created + " Created new folder at " + folderForStorage.getAbsolutePath());
        }
        File[] containedFiles = folderForStorage.listFiles();
        BufferedReader reader = null;
        try {
            if (containedFiles != null) {
                for (int i = 0; i < containedFiles.length; i++) {
                    ArrayList<String> rssUrlList = new ArrayList<String>();
                    reader = new BufferedReader(new FileReader(containedFiles[i]));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        if (line.length() > 0) {
                            rssUrlList.add(line);
                        }
                    }
                    urlCache.put(containedFiles[i].getName(), rssUrlList);
                }
            }
            opened = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public Collection<String> listStoredFeeds() {
        if (!opened) {
            throw new IllegalArgumentException(NOT_OPEN);
        }
        ArrayList<String> allUrls = new ArrayList<String>();
        for (String site : urlCache.keySet()) {
            for (String url : urlCache.get(site)) {
                allUrls.add(url);
            }
        }
        return Collections.unmodifiableList(allUrls);
    }

    public void removeFeed(String urlStr) {
        if (!opened) {
            throw new IllegalArgumentException(NOT_OPEN);
        }
        if (urlStr != null) {
            pcs.firePropertyChange(FEED_REMOVED_PROPERTY, urlStr, "");
        }
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (!opened) {
            throw new IllegalArgumentException(NOT_OPEN);
        }
        if (listener != null) {
            pcs.addPropertyChangeListener(listener);
        }
    }

    private void out(String msg) {
        System.out.println(getClass().getName() + ":" + msg);
    }
}
