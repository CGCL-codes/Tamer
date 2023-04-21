package spider.crawl;

import spider.util.Hashing;
import spider.util.Helper;
import java.io.File;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * @author Gautam Pant
 *         <p/>
 *         The frontier (priority queue) implementation. The methods are synchronized in order to allow multi-threaded acces
 */
public class Frontier {

    private int max_size = 0;

    /**
     * the priority queue
     */
    private Vector v = new Vector();

    /**
     * fast looup hashtable of the urls in the frontier
     */
    private Hashtable h = new Hashtable();

    /**
     * List of domains that have been accessed in past N fetches*
     */
    private Hashtable hotList = new Hashtable();

    /**
     * List of domains that have been accessed in the order in which it was visited
     */
    private Vector hotListSeq = new Vector();

    /**
     * maximum size of hot list*
     */
    private int max_hotlist = 50;

    /**
     * Sleep time (ms)
     */
    private int SLEEP = 1000;

    /**
     * Cache in use
     */
    private Cache c = null;

    /**
     * blacklisted servers (could be parts of server names)
     */
    private String[] blacklist = { "enginads.com", "enginebooks.com", "quiltingideas.com", "old-engine.com", "harrymatthews.com" };

    public Frontier(int max_size, Cache c) {
        this.max_size = max_size;
        this.c = c;
    }

    /**
     * if no size is specified, then set up a flag for unlimited frontier
     */
    public Frontier(Cache c) {
        this.max_size = -1;
        this.c = c;
    }

    /**
     * Adds a set of elements to the frontier given a sorted list of urls (sorted by score)
     * The function allows optimization of code when many URLs need to be added
     * returns true unless Exception is caught - omitted for now
     *
     * @param - a sorted listed of URL (FrontierElements)
     */
    public boolean addElements(Vector fes) {
        try {
            synchronized (this) {
                boolean worst = false;
                int count = 0;
                for (int i = 0; i < fes.size(); i++) {
                    FrontierElement fe = (FrontierElement) fes.get(i);
                    if (blackListed(fe.url)) {
                        continue;
                    }
                    if (h.containsKey(fe.url)) {
                        continue;
                    }
                    if (v.size() == 0) {
                        v.add(fe);
                        h.put(fe.url, "");
                        resize();
                        continue;
                    }
                    if (fe.score <= getWorstScore() || worst) {
                        worst = true;
                        if ((max_size > 0) && (v.size() >= max_size)) {
                            break;
                        }
                        v.add(fe);
                        h.put(fe.url, "");
                        resize();
                        continue;
                    }
                    if (i == 0 && fe.score > getBestScore()) {
                        v.insertElementAt(fe, 0);
                        h.put(fe.url, "");
                        resize();
                        continue;
                    }
                    int start = 0;
                    int end = v.size();
                    int mid = start;
                    while ((end - start) > 1) {
                        mid = (end + start) / 2;
                        if (getScoreAt(mid) >= fe.score) {
                            start = mid;
                        } else {
                            end = mid;
                        }
                    }
                    v.insertElementAt(fe, end);
                    count++;
                    h.put(fe.url, "");
                    resize();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * get the top frontier element (according to priority) and delete it
     */
    public FrontierElement getElement() {
        try {
            synchronized (this) {
                FrontierElement fe = (FrontierElement) v.firstElement();
                String domain = Helper.getHostName(fe.url);
                int pos = 0;
                while (hotList.containsKey(domain)) {
                    pos++;
                    if (pos < v.size()) {
                        fe = (FrontierElement) v.get(pos);
                        domain = Helper.getHostName(fe.url);
                    } else {
                        try {
                            if (!(new File(c.getPath(String.valueOf(Hashing.getHashValue(fe.url))) + String.valueOf(Hashing.getHashValue(fe.url)))).exists() && !((String) hotList.get(domain)).equalsIgnoreCase("cache")) {
                                Thread.sleep(SLEEP);
                            } else if (!(new File(c.getPath(String.valueOf(Hashing.getHashValue(fe.url))) + String.valueOf(Hashing.getHashValue(fe.url)))).exists()) {
                                hotList.put(domain, "");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        fe = (FrontierElement) v.firstElement();
                        h.remove(fe.url);
                        v.remove(0);
                        return fe;
                    }
                }
                if (!(new File(c.getPath(String.valueOf(Hashing.getHashValue(fe.url))) + String.valueOf(Hashing.getHashValue(fe.url)))).exists()) {
                    hotList.put(domain, "");
                    hotListSeq.add(domain);
                } else {
                    hotList.put(domain, "cache");
                    hotListSeq.add(domain);
                }
                if (hotList.size() > max_hotlist) {
                    String oldDomain = (String) hotListSeq.firstElement();
                    hotList.remove(oldDomain);
                    hotListSeq.remove(0);
                }
                if ((hotList.size() != hotListSeq.size()) || hotList.size() > max_hotlist || hotListSeq.size() > max_hotlist) {
                    System.err.println("Fatal Error in maintaining Hot List");
                    System.exit(1);
                }
                h.remove(fe.url);
                v.remove(pos);
                return fe;
            }
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * get current frontier size
     */
    public int size() {
        synchronized (this) {
            return v.size();
        }
    }

    /**
     * get max size
     */
    public int getMaxSize() {
        return max_size;
    }

    /**
     * a string version of the frontier's current state
     */
    public String toString() {
        synchronized (this) {
            StringBuffer fbuffer = new StringBuffer("");
            for (Object aV : v) {
                fbuffer.append(((FrontierElement) aV).url).append(":").append(((FrontierElement) aV).score).append("  ");
            }
            return fbuffer.toString().trim();
        }
    }

    private double getWorstScore() {
        if (v.size() > 0) {
            return (((FrontierElement) v.lastElement()).score);
        } else {
            return 0;
        }
    }

    private double getScoreAt(int pos) {
        return (((FrontierElement) v.get(pos)).score);
    }

    private double getBestScore() {
        if (v.size() > 0) {
            return (((FrontierElement) v.firstElement()).score);
        } else {
            return 0;
        }
    }

    private void resize() {
        if ((max_size > 0) && (v.size() > max_size)) {
            h.remove(((FrontierElement) v.get(v.size() - 1)).url);
            v.remove(v.size() - 1);
        }
        if ((h.size() > max_size || v.size() > max_size) && max_size > 0) {
            System.err.println("Fatal Error: Frontier size exceeded");
            System.exit(1);
        }
        if (h.size() != v.size()) {
            System.err.println("Fatal Error: Unsynchronized Frontier");
            System.exit(1);
        }
    }

    /**
     * chack if the given URL comes from a blacklisted server
     */
    private boolean blackListed(String url) {
        for (int i = 0; i < blacklist.length; i++) {
            blacklist[i] = blacklist[i].toLowerCase();
            url = url.toLowerCase();
            if (url.matches(".*" + blacklist[i] + ".*")) {
                return true;
            }
        }
        return false;
    }
}
