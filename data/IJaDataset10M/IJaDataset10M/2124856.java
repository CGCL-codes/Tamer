package spider.crawl;

import spider.util.Hashing;
import spider.util.Helper;
import spider.util.RobotExclusion;
import spider.util.XMLParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author : Eran Chinthaka (echintha@cs.indiana.edu)
 * @Date : Feb 15, 2007
 * <p/>
 * Initial code extract from the works of Gautam Pant
 */
public class BasicCrawler {

    private String[] seeds = null;

    protected Hashtable<String, String> seedDomain = new Hashtable<String, String>();

    private long maxPages = 0;

    private int maxFrontier = 10000;

    protected History history = new History();

    private String dir = null;

    private int maxThreads = 100;

    protected Cache cache = null;

    protected Frontier front = null;

    private int topN = 1;

    private String storageFile = "default";

    ActiveThreads activeThreads = new ActiveThreads();

    protected BadExtensions bext = new BadExtensions();

    protected RobotExclusion robot = new RobotExclusion();

    protected Statistics stat = null;

    private String statFile = "statistics.txt";

    private boolean frontierAdd = true;

    private static int DEFAULT_PAGE_SCORE = 1;

    /**
     * construct the blog with the seeds
     *
     * @param seeds    - URLs that are starting points for crawl
     *                 maxPages - maximum pages to be fetched
     *                 dir - the directory to store the results in (the directory is created if it does not exist)
     * @param maxPages
     * @param dir
     */
    public BasicCrawler(String[] seeds, long maxPages, String dir) {
        this.seeds = seeds;
        this.maxPages = maxPages;
        this.dir = dir;
    }

    /**
     * create a thread of blog
     *
     * @return - blog thread
     */
    private Thread makeCrawlerThread() {
        Thread t = null;
        Runnable r = new Runnable() {

            public void run() {
                try {
                    activeThreads.add();
                    while (maxPages == -1) {
                        Vector<String> links = new Vector<String>();
                        for (int i = 0; i < topN; i++) {
                            FrontierElement frontierElement = front.getElement();
                            if (frontierElement != null) {
                                if (!history.isInHistory(frontierElement.url)) {
                                    links.add(frontierElement.url);
                                }
                            } else {
                                break;
                            }
                        }
                        if (links.size() == 0 && front.size() == 0) {
                            activeThreads.subtract();
                            while (front.size() == 0) {
                                try {
                                    Thread.sleep(100);
                                } catch (Exception e) {
                                    continue;
                                }
                                if (activeThreads.get() == 0) {
                                    return;
                                } else {
                                    continue;
                                }
                            }
                            activeThreads.add();
                        }
                        String[] urls = new String[links.size()];
                        for (int i = 0; i < links.size(); i++) {
                            urls[i] = links.get(i);
                        }
                        FetcherPool fetcherPool = new FetcherPool(cache, robot, stat);
                        System.out.println("Fetching " + urls.length + " url(s) ...");
                        fetcherPool.fetchPages(urls);
                        for (String url : urls) {
                            String fileName = Hashing.getHashValue(url);
                            File f = new File(cache.getPath(fileName) + fileName);
                            if (f.exists()) {
                                if (frontierAdd) {
                                } else {
                                    history.add(url, fileName, -1);
                                }
                            }
                        }
                    }
                    activeThreads.subtract();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t = new Thread(r);
        return t;
    }

    public synchronized void addLinksToFrontier(String[] linksToBeRetrieved) {
        Vector<FrontierElement> frontierElements = new Vector<FrontierElement>();
        for (String aLinksToBeRetrieved : linksToBeRetrieved) {
            frontierElements.add(new FrontierElement(aLinksToBeRetrieved, 1));
        }
        front.addElements(frontierElements);
    }

    public boolean startCrawl() {
        if (dir == null || seeds == null || maxPages == 0) {
            return false;
        }
        boolean exists = (new File(dir)).exists();
        if (!exists) {
            boolean success = (new File(dir)).mkdirs();
            if (!success) {
                System.err.println("Directory:" + dir + " could not be created");
                return false;
            }
        }
        cache = new Cache();
        cache.setPath(dir);
        front = new Frontier(maxFrontier, cache);
        Vector<FrontierElement> urls = new Vector<FrontierElement>();
        for (int i = 0; i < seeds.length; i++) {
            seeds[i] = Helper.getCanonical(seeds[i]);
            if (seeds[i] == null) {
                continue;
            }
            urls.add(new FrontierElement(seeds[i], 1));
            String d = Helper.getHostName(seeds[i]);
            if (d != null) {
                seedDomain.put(d, "");
            }
        }
        front.addElements(urls);
        stat = new Statistics(System.currentTimeMillis(), history, front);
        stat.setFile(statFile);
        Thread[] crawlers = new Thread[maxThreads];
        for (int i = 0; i < maxThreads; i++) {
            crawlers[i] = makeCrawlerThread();
            crawlers[i].start();
        }
        stat.start();
        for (int i = 0; i < maxThreads; i++) {
            try {
                crawlers[i].join();
            } catch (Exception e) {
                continue;
            }
        }
        for (int i = 0; i < maxThreads; i++) {
            try {
                crawlers[i] = null;
            } catch (Exception e) {
                continue;
            }
        }
        stat.setStop(true);
        stat.toFile();
        history.toFile(storageFile);
        return true;
    }

    /**
     * score and add URls to the frontier
     * more sophiticated code may extend or override the functionality
     *
     * @param p - the XML parser, id - the filename of the page being parsed
     */
    protected void addToFrontier(XMLParser p, String url) {
        if (p.startParser()) {
            String[] newLinks = p.getLinks();
            if (newLinks != null) {
                double pageScore = getPageScore(p);
                String fileName = Hashing.getHashValue(url);
                history.add(url, fileName, pageScore);
                Vector<FrontierElement> urls = new Vector<FrontierElement>();
                for (int j = 0; j < newLinks.length; j++) {
                    String server = Helper.getHostNameWithPort(newLinks[j]);
                    Vector perm = robot.get(server);
                    if (perm != null) {
                        if (RobotExclusion.isDisallowed(newLinks[j], perm)) {
                            continue;
                        }
                    }
                    if (!history.isInHistory(newLinks[j]) && !bext.hasBadExtension(newLinks[j]) && (newLinks[j] != null)) {
                        urls.add(new FrontierElement(newLinks[j], pageScore));
                    }
                }
                if (urls.size() == 0) {
                    return;
                }
                front.addElements(urls);
            }
        } else {
            stat.parseErrors(1);
        }
    }

    /**
     * score and add URls to the frontier
     * more sophiticated code may extend or override the functionality
     *
     * @param srcUrl
     */
    protected void addToFrontier(String srcUrl, String[] newLinks) {
        if (newLinks != null) {
            String fileName = Hashing.getHashValue(srcUrl);
            history.add(srcUrl, fileName, DEFAULT_PAGE_SCORE);
            Vector<FrontierElement> urls = new Vector<FrontierElement>();
            for (int j = 0; j < newLinks.length; j++) {
                String server = Helper.getHostNameWithPort(newLinks[j]);
                Vector perm = robot.get(server);
                if (perm != null) {
                    if (RobotExclusion.isDisallowed(newLinks[j], perm)) {
                        continue;
                    }
                }
                if (!history.isInHistory(newLinks[j]) && !bext.hasBadExtension(newLinks[j]) && (newLinks[j] != null)) {
                    urls.add(new FrontierElement(newLinks[j], DEFAULT_PAGE_SCORE));
                }
            }
            if (urls.size() == 0) {
                return;
            }
            front.addElements(urls);
        }
    }

    /**
     * scores a page and returns the score
     * more sophisticated code may override the functionality
     */
    protected double getPageScore(XMLParser p) {
        return -1;
    }

    /**
     * Returns the maxFrontier.
     *
     * @return long
     */
    public long getMaxFrontier() {
        return maxFrontier;
    }

    /**
     * Sets the maxFrontier - maximum size of the frontier.
     *
     * @param maxFrontier The maxFrontier to set
     */
    public void setMaxFrontier(int maxFrontier) {
        this.maxFrontier = maxFrontier;
        System.out.println("Maximum Frontier set to: " + maxFrontier);
    }

    /**
     * Returns the maxThreads.
     *
     * @return int
     */
    public int getMaxThreads() {
        return maxThreads;
    }

    /**
     * Sets the maxThreads - maximum number of threads.
     *
     * @param maxThreads The maxThreads to set
     */
    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    /**
     * Returns the maxPages.
     *
     * @return long
     */
    public long getMaxPages() {
        return maxPages;
    }

    /**
     * Returns the topN.
     *
     * @return int
     */
    public int getTopN() {
        return topN;
    }

    /**
     * Sets the topN.
     *
     * @param topN The topN to set
     */
    public void setTopN(int topN) {
        this.topN = topN;
    }

    /**
     * Returns the storageFile.
     *
     * @return String
     */
    public String getStorageFile() {
        return storageFile;
    }

    /**
     * Sets the storageFile.
     *
     * @param storageFile The storageFile to set
     */
    public void setStorageFile(String storageFile) {
        this.storageFile = storageFile;
    }

    /**
     * Sets the statFile.
     *
     * @param statFile The statFile to set
     */
    public void setStatFile(String statFile) {
        this.statFile = statFile;
    }

    /**
     * Allows to restart the blog based on the last state of the history.
     * <p/>
     * loads history and fill up the corresponding frontier
     */
    public boolean reStartCrawl() {
        if (dir == null || seeds == null || maxPages == 0) {
            return false;
        }
        System.out.println("restarting blog...");
        boolean exists = (new File(dir)).exists();
        if (!exists) {
            boolean success = (new File(dir)).mkdirs();
            if (!success) {
                System.err.println("Directory:" + dir + " could not be created");
                return false;
            }
        }
        cache = new Cache();
        cache.setPath(dir);
        front = new Frontier(maxFrontier, cache);
        stat = new Statistics(System.currentTimeMillis(), history, front);
        stat.setFile(statFile);
        System.out.println("loading history and frontier...");
        File oldHist = new File(storageFile);
        if (oldHist.exists()) {
            try {
                BufferedReader bf = new BufferedReader(new FileReader(oldHist));
                Hashtable<String, String> files = new Hashtable<String, String>();
                String line = null;
                while ((line = bf.readLine()) != null) {
                    String[] parts = line.split("\\s+");
                    history.add(parts[1], parts[2], Double.parseDouble(parts[3]));
                    files.put(parts[1], parts[2]);
                }
                for (Enumeration<String> e = files.keys(); e.hasMoreElements(); ) {
                    String url = e.nextElement();
                    String filename = files.get(url);
                    XMLParser p = new XMLParser(new File(cache.getPath(filename) + filename));
                    addToFrontier(p, url);
                }
                bf.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("No history found");
            System.exit(1);
        }
        System.out.println("Frontier:" + front.size());
        Thread[] crawlers = new Thread[maxThreads];
        for (int i = 0; i < maxThreads; i++) {
            crawlers[i] = makeCrawlerThread();
            crawlers[i].start();
        }
        stat.start();
        for (int i = 0; i < maxThreads; i++) {
            try {
                crawlers[i].join();
            } catch (Exception e) {
                continue;
            }
        }
        for (int i = 0; i < maxThreads; i++) {
            try {
                crawlers[i] = null;
            } catch (Exception e) {
                continue;
            }
        }
        stat.setStop(true);
        stat.toFile();
        history.toFile(storageFile);
        return true;
    }

    /**
     * set the frontier to allow (true) or disallow (false) addition of new URLs.
     *
     * @param b
     */
    public void setFrontierAdd(boolean b) {
        frontierAdd = b;
    }
}
