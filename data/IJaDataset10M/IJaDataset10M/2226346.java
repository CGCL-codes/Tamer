package org.archive.crawler.admin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.event.CrawlURIDispositionListener;
import org.archive.crawler.framework.AbstractTracker;
import org.archive.crawler.framework.CrawlController;
import org.archive.crawler.framework.exceptions.FatalConfigurationException;
import org.archive.net.UURI;
import org.archive.util.ArchiveUtils;
import org.archive.util.LongWrapper;
import org.archive.util.MimetypeUtils;
import org.archive.util.PaddingStringBuffer;

/**
 * This is an implementation of the AbstractTracker. It is designed to function
 * with the WUI as well as performing various logging activity.
 * <p>
 * At the end of each snapshot a line is written to the
 * 'progress-statistics.log' file.
 * <p>
 * The header of that file is as follows:
 * <pre> [timestamp] [discovered]    [queued] [downloaded] [doc/s(avg)]  [KB/s(avg)] [dl-failures] [busy-thread] [mem-use-KB]</pre>
 * First there is a <b>timestamp</b>, accurate down to 1 second.
 * <p>
 * <b>discovered</b>, <b>queued</b>, <b>downloaded</b> and <b>dl-failures</b>
 * are (respectively) the discovered URI count, pending URI count, successfully
 * fetched count and failed fetch count from the frontier at the time of the
 * snapshot.
 * <p>
 * <b>KB/s(avg)</b> is the bandwidth usage.  We use the total bytes downloaded
 * to calculate average bandwidth usage (KB/sec). Since we also note the value
 * each time a snapshot is made we can calculate the average bandwidth usage
 * during the last snapshot period to gain a "current" rate. The first number is
 * the current and the average is in parenthesis.
 * <p>
 * <b>doc/s(avg)</b> works the same way as doc/s except it show the number of
 * documents (URIs) rather then KB downloaded.
 * <p>
 * <b>busy-threads</b> is the total number of ToeThreads that are not available
 * (and thus presumably busy processing a URI). This information is extracted
 * from the crawl controller.
 * <p>
 * Finally mem-use-KB is extracted from the run time environment
 * (<code>Runtime.getRuntime().totalMemory()</code>).
 * <p>
 * In addition to the data collected for the above logs, various other data
 * is gathered and stored by this tracker.
 * <ul>
 *   <li> Successfully downloaded documents per fetch status code
 *   <li> Successfully downloaded documents per document mime type
 *   <li> Amount of data per mime type
 *   <li> Successfully downloaded documents per host
 *   <li> Amount of data per host
 *   <li> Disposition of all seeds (this is written to 'reports.log' at end of
 *        crawl)
 *   <li> Successfully downloaded documents per host per source
 * </ul>
 *
 * @author Parker Thompson
 * @author Kristinn Sigurdsson
 *
 * @see org.archive.crawler.framework.StatisticsTracking
 * @see org.archive.crawler.framework.AbstractTracker
 */
public class StatisticsTracker extends AbstractTracker implements CrawlURIDispositionListener, Serializable {

    private static final long serialVersionUID = 8004878315916392305L;

    /**
     * Messages from the StatisticsTracker.
     */
    private static final Logger logger = Logger.getLogger(StatisticsTracker.class.getName());

    protected long lastPagesFetchedCount = 0;

    protected long lastProcessedBytesCount = 0;

    protected long discoveredUriCount = 0;

    protected long queuedUriCount = 0;

    protected long finishedUriCount = 0;

    protected long downloadedUriCount = 0;

    protected long downloadFailures = 0;

    protected long downloadDisregards = 0;

    protected double docsPerSecond = 0;

    protected double currentDocsPerSecond = 0;

    protected int currentKBPerSec = 0;

    protected long totalKBPerSec = 0;

    protected int busyThreads = 0;

    protected long totalProcessedBytes = 0;

    protected float congestionRatio = 0;

    protected long deepestUri;

    protected long averageDepth;

    /** Keep track of the file types we see (mime type -> count) */
    protected Hashtable<String, LongWrapper> mimeTypeDistribution = new Hashtable<String, LongWrapper>();

    protected Hashtable<String, LongWrapper> mimeTypeBytes = new Hashtable<String, LongWrapper>();

    /** Keep track of fetch status codes */
    protected Hashtable<String, LongWrapper> statusCodeDistribution = new Hashtable<String, LongWrapper>();

    /** Keep track of hosts. 
     * 
     * Each of these Maps are individually unsynchronized, and cannot 
     * be trivially synchronized with the Collections wrapper. Thus
     * their synchronized access is enforced by this class.
     * 
     * <p>They're transient because usually bigmaps that get reconstituted
     * on recover from checkpoint.
     */
    protected transient Map<String, LongWrapper> hostsDistribution = null;

    protected transient Map<String, LongWrapper> hostsBytes = null;

    protected transient Map<String, Long> hostsLastFinished = null;

    /** Keep track of URL counts per host per seed */
    protected transient Map<String, HashMap<String, LongWrapper>> sourceHostDistribution = null;

    /**
     * Record of seeds' latest actions.
     */
    protected transient Map<String, SeedRecord> processedSeedsRecords;

    private int seedsCrawled;

    private int seedsNotCrawled;

    private String sExitMessage = "Before crawl end";

    public StatisticsTracker(String name) {
        super(name, "A statistics tracker thats integrated into " + "the web UI and that creates the progress-statistics log.");
    }

    public void initialize(CrawlController c) throws FatalConfigurationException {
        super.initialize(c);
        try {
            this.sourceHostDistribution = c.getBigMap("sourceHostDistribution", String.class, HashMap.class);
            this.hostsDistribution = c.getBigMap("hostsDistribution", String.class, LongWrapper.class);
            this.hostsBytes = c.getBigMap("hostsBytes", String.class, LongWrapper.class);
            this.hostsLastFinished = c.getBigMap("hostsLastFinished", String.class, Long.class);
            this.processedSeedsRecords = c.getBigMap("processedSeedsRecords", String.class, SeedRecord.class);
        } catch (Exception e) {
            throw new FatalConfigurationException("Failed setup of" + " StatisticsTracker: " + e);
        }
        controller.addCrawlURIDispositionListener(this);
    }

    protected void finalCleanup() {
        super.finalCleanup();
        if (this.hostsBytes != null) {
            this.hostsBytes.clear();
            this.hostsBytes = null;
        }
        if (this.hostsDistribution != null) {
            this.hostsDistribution.clear();
            this.hostsDistribution = null;
        }
        if (this.hostsLastFinished != null) {
            this.hostsLastFinished.clear();
            this.hostsLastFinished = null;
        }
        if (this.processedSeedsRecords != null) {
            this.processedSeedsRecords.clear();
            this.processedSeedsRecords = null;
        }
        if (this.sourceHostDistribution != null) {
            this.sourceHostDistribution.clear();
            this.sourceHostDistribution = null;
        }
    }

    protected synchronized void progressStatisticsEvent(final EventObject e) {
        discoveredUriCount = discoveredUriCount();
        downloadedUriCount = successfullyFetchedCount();
        finishedUriCount = finishedUriCount();
        queuedUriCount = queuedUriCount();
        downloadFailures = failedFetchAttempts();
        downloadDisregards = disregardedFetchAttempts();
        totalProcessedBytes = totalBytesWritten();
        congestionRatio = congestionRatio();
        deepestUri = deepestUri();
        averageDepth = averageDepth();
        if (finishedUriCount() == 0) {
            docsPerSecond = 0;
            totalKBPerSec = 0;
        } else if (getCrawlerTotalElapsedTime() < 1000) {
            return;
        } else {
            docsPerSecond = (double) downloadedUriCount / (double) (getCrawlerTotalElapsedTime() / 1000);
            totalKBPerSec = (long) (((totalProcessedBytes / 1024) / ((getCrawlerTotalElapsedTime()) / 1000)) + .5);
        }
        busyThreads = activeThreadCount();
        if (shouldrun || (System.currentTimeMillis() - lastLogPointTime) >= 1000) {
            currentDocsPerSecond = 0;
            currentKBPerSec = 0;
            long currentTime = System.currentTimeMillis();
            long sampleTime = currentTime - lastLogPointTime;
            if (sampleTime >= 1000) {
                long currentPageCount = successfullyFetchedCount();
                long samplePageCount = currentPageCount - lastPagesFetchedCount;
                currentDocsPerSecond = (double) samplePageCount / (double) (sampleTime / 1000);
                lastPagesFetchedCount = currentPageCount;
                long currentProcessedBytes = totalProcessedBytes;
                long sampleProcessedBytes = currentProcessedBytes - lastProcessedBytesCount;
                currentKBPerSec = (int) (((sampleProcessedBytes / 1024) / (sampleTime / 1000)) + .5);
                lastProcessedBytesCount = currentProcessedBytes;
            }
        }
        if (this.controller != null) {
            this.controller.logProgressStatistics(getProgressStatisticsLine());
        }
        lastLogPointTime = System.currentTimeMillis();
        super.progressStatisticsEvent(e);
    }

    /**
     * Return one line of current progress-statistics
     * 
     * @param now
     * @return String of stats
     */
    public String getProgressStatisticsLine(Date now) {
        return new PaddingStringBuffer().append(ArchiveUtils.getLog14Date(now)).raAppend(32, discoveredUriCount).raAppend(44, queuedUriCount).raAppend(57, downloadedUriCount).raAppend(74, ArchiveUtils.doubleToString(currentDocsPerSecond, 2) + "(" + ArchiveUtils.doubleToString(docsPerSecond, 2) + ")").raAppend(85, currentKBPerSec + "(" + totalKBPerSec + ")").raAppend(99, downloadFailures).raAppend(113, busyThreads).raAppend(126, (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024).raAppend(140, Runtime.getRuntime().totalMemory() / 1024).raAppend(153, ArchiveUtils.doubleToString(congestionRatio, 2)).raAppend(165, deepestUri).raAppend(177, averageDepth).toString();
    }

    public Map<String, Number> getProgressStatistics() {
        Map<String, Number> stats = new HashMap<String, Number>();
        stats.put("discoveredUriCount", new Long(discoveredUriCount));
        stats.put("queuedUriCount", new Long(queuedUriCount));
        stats.put("downloadedUriCount", new Long(downloadedUriCount));
        stats.put("currentDocsPerSecond", new Double(currentDocsPerSecond));
        stats.put("docsPerSecond", new Double(docsPerSecond));
        stats.put("totalKBPerSec", new Long(totalKBPerSec));
        stats.put("totalProcessedBytes", new Long(totalProcessedBytes));
        stats.put("currentKBPerSec", new Long(currentKBPerSec));
        stats.put("downloadFailures", new Long(downloadFailures));
        stats.put("busyThreads", new Integer(busyThreads));
        stats.put("congestionRatio", new Double(congestionRatio));
        stats.put("deepestUri", new Long(deepestUri));
        stats.put("averageDepth", new Long(averageDepth));
        stats.put("totalMemory", new Long(Runtime.getRuntime().totalMemory()));
        stats.put("freeMemory", new Long(Runtime.getRuntime().freeMemory()));
        return stats;
    }

    /**
     * Return one line of current progress-statistics
     * 
     * @return String of stats
     */
    public String getProgressStatisticsLine() {
        return getProgressStatisticsLine(new Date());
    }

    public double processedDocsPerSec() {
        return docsPerSecond;
    }

    public double currentProcessedDocsPerSec() {
        return currentDocsPerSecond;
    }

    public long processedKBPerSec() {
        return totalKBPerSec;
    }

    public int currentProcessedKBPerSec() {
        return currentKBPerSec;
    }

    /** Returns a HashMap that contains information about distributions of
     *  encountered mime types.  Key/value pairs represent
     *  mime type -> count.
     * <p>
     * <b>Note:</b> All the values are wrapped with a {@link LongWrapper LongWrapper}
     * @return mimeTypeDistribution
     */
    public Hashtable<String, LongWrapper> getFileDistribution() {
        return mimeTypeDistribution;
    }

    /**
     * Increment a counter for a key in a given HashMap. Used for various
     * aggregate data.
     * 
     * As this is used to change Maps which depend on StatisticsTracker
     * for their synchronization, this method should only be invoked
     * from a a block synchronized on 'this'. 
     *
     * @param map The HashMap
     * @param key The key for the counter to be incremented, if it does not
     *               exist it will be added (set to 1).  If null it will
     *            increment the counter "unknown".
     */
    protected static void incrementMapCount(Map<String, LongWrapper> map, String key) {
        incrementMapCount(map, key, 1);
    }

    /**
     * Increment a counter for a key in a given HashMap by an arbitrary amount.
     * Used for various aggregate data. The increment amount can be negative.
     *
     * As this is used to change Maps which depend on StatisticsTracker
     * for their synchronization, this method should only be invoked
     * from a a block synchronized on 'this'. 
     *
     * @param map
     *            The HashMap
     * @param key
     *            The key for the counter to be incremented, if it does not exist
     *            it will be added (set to equal to <code>increment</code>).
     *            If null it will increment the counter "unknown".
     * @param increment
     *            The amount to increment counter related to the <code>key</code>.
     */
    protected static void incrementMapCount(Map<String, LongWrapper> map, String key, long increment) {
        if (key == null) {
            key = "unknown";
        }
        LongWrapper lw = (LongWrapper) map.get(key);
        if (lw == null) {
            map.put(key, new LongWrapper(increment));
        } else {
            lw.longValue += increment;
        }
    }

    /**
     * Sort the entries of the given HashMap in descending order by their
     * values, which must be longs wrapped with <code>LongWrapper</code>.
     * <p>
     * Elements are sorted by value from largest to smallest. Equal values are
     * sorted in an arbitrary, but consistent manner by their keys. Only items
     * with identical value and key are considered equal.
     *
     * If the passed-in map requires access to be synchronized, the caller
     * should ensure this synchronization. 
     * 
     * @param mapOfLongWrapperValues
     *            Assumes values are wrapped with LongWrapper.
     * @return a sorted set containing the same elements as the map.
     */
    public TreeMap<String, LongWrapper> getReverseSortedCopy(final Map<String, LongWrapper> mapOfLongWrapperValues) {
        TreeMap<String, LongWrapper> sortedMap = new TreeMap<String, LongWrapper>(new Comparator<String>() {

            public int compare(String e1, String e2) {
                long firstVal = mapOfLongWrapperValues.get(e1).longValue;
                long secondVal = mapOfLongWrapperValues.get(e2).longValue;
                if (firstVal < secondVal) {
                    return 1;
                }
                if (secondVal < firstVal) {
                    return -1;
                }
                return e1.compareTo(e2);
            }
        });
        try {
            sortedMap.putAll(mapOfLongWrapperValues);
        } catch (UnsupportedOperationException e) {
            Iterator<String> i = mapOfLongWrapperValues.keySet().iterator();
            for (; i.hasNext(); ) {
                String key = i.next();
                sortedMap.put(key, mapOfLongWrapperValues.get(key));
            }
        }
        return sortedMap;
    }

    /**
     * Return a HashMap representing the distribution of status codes for
     * successfully fetched curis, as represented by a hashmap where key -&gt;
     * val represents (string)code -&gt; (integer)count.
     * 
     * <b>Note: </b> All the values are wrapped with a
     * {@link LongWrapper LongWrapper}
     * 
     * @return statusCodeDistribution
     */
    public Hashtable<String, LongWrapper> getStatusCodeDistribution() {
        return statusCodeDistribution;
    }

    /**
     * Returns the time (in millisec) when a URI belonging to a given host was
     * last finished processing. 
     * 
     * @param host The host to look up time of last completed URI.
     * @return Returns the time (in millisec) when a URI belonging to a given 
     * host was last finished processing. If no URI has been completed for host
     * -1 will be returned. 
     */
    public long getHostLastFinished(String host) {
        Long l = null;
        synchronized (hostsLastFinished) {
            l = (Long) hostsLastFinished.get(host);
        }
        return (l != null) ? l.longValue() : -1;
    }

    /**
     * Returns the accumulated number of bytes downloaded from a given host.
     * @param host name of the host
     * @return the accumulated number of bytes downloaded from a given host
     */
    public long getBytesPerHost(String host) {
        synchronized (hostsBytes) {
            return ((LongWrapper) hostsBytes.get(host)).longValue;
        }
    }

    /**
     * Returns the accumulated number of bytes from files of a given file type.
     * @param filetype Filetype to check.
     * @return the accumulated number of bytes from files of a given mime type
     */
    public long getBytesPerFileType(String filetype) {
        return ((LongWrapper) mimeTypeBytes.get(filetype)).longValue;
    }

    /**
     * Get the total number of ToeThreads (sleeping and active)
     *
     * @return The total number of ToeThreads
     */
    public int threadCount() {
        return this.controller != null ? controller.getToeCount() : 0;
    }

    /**
     * @return Current thread count (or zero if can't figure it out).
     */
    public int activeThreadCount() {
        return this.controller != null ? controller.getActiveToeCount() : 0;
    }

    /**
     * This returns the number of completed URIs as a percentage of the total
     * number of URIs encountered (should be inverse to the discovery curve)
     *
     * @return The number of completed URIs as a percentage of the total
     * number of URIs encountered
     */
    public int percentOfDiscoveredUrisCompleted() {
        long completed = finishedUriCount();
        long total = discoveredUriCount();
        if (total == 0) {
            return 0;
        }
        return (int) (100 * completed / total);
    }

    /**
     * Number of <i>discovered</i> URIs.
     *
     * <p>If crawl not running (paused or stopped) this will return the value of
     * the last snapshot.
     *
     * @return A count of all uris encountered
     *
     * @see org.archive.crawler.framework.Frontier#discoveredUriCount()
     */
    public long discoveredUriCount() {
        return shouldrun && this.controller != null && this.controller.getFrontier() != null ? controller.getFrontier().discoveredUriCount() : discoveredUriCount;
    }

    /**
     * Number of URIs that have <i>finished</i> processing.
     *
     * @return Number of URIs that have finished processing
     *
     * @see org.archive.crawler.framework.Frontier#finishedUriCount()
     */
    public long finishedUriCount() {
        return shouldrun && this.controller != null && this.controller.getFrontier() != null ? controller.getFrontier().finishedUriCount() : finishedUriCount;
    }

    /**
     * Get the total number of failed fetch attempts (connection failures -> give up, etc)
     *
     * @return The total number of failed fetch attempts
     */
    public long failedFetchAttempts() {
        return shouldrun && this.controller != null && this.controller.getFrontier() != null ? controller.getFrontier().failedFetchCount() : downloadFailures;
    }

    /**
     * Get the total number of failed fetch attempts (connection failures -> give up, etc)
     *
     * @return The total number of failed fetch attempts
     */
    public long disregardedFetchAttempts() {
        return shouldrun && this.controller != null && this.controller.getFrontier() != null ? controller.getFrontier().disregardedUriCount() : downloadDisregards;
    }

    public long successfullyFetchedCount() {
        return shouldrun && this.controller != null && this.controller.getFrontier() != null ? controller.getFrontier().succeededFetchCount() : downloadedUriCount;
    }

    public long totalCount() {
        return queuedUriCount() + activeThreadCount() + successfullyFetchedCount();
    }

    /**
     * Ratio of number of threads that would theoretically allow
     * maximum crawl progress (if each was as productive as current
     * threads), to current number of threads.
     * 
     * @return float congestion ratio 
     */
    public float congestionRatio() {
        return shouldrun && this.controller != null && this.controller.getFrontier() != null ? controller.getFrontier().congestionRatio() : congestionRatio;
    }

    /**
     * Ordinal position of the 'deepest' URI eligible 
     * for crawling. Essentially, the length of the longest
     * frontier internal queue. 
     * 
     * @return long URI count to deepest URI
     */
    public long deepestUri() {
        return shouldrun && this.controller != null && this.controller.getFrontier() != null ? controller.getFrontier().deepestUri() : deepestUri;
    }

    /**
     * Average depth of the last URI in all eligible queues.
     * That is, the average length of all eligible queues.
     * 
     * @return long average depth of last URIs in queues 
     */
    public long averageDepth() {
        return shouldrun && this.controller != null && this.controller.getFrontier() != null ? controller.getFrontier().averageDepth() : averageDepth;
    }

    /**
     * Number of URIs <i>queued</i> up and waiting for processing.
     *
     * <p>If crawl not running (paused or stopped) this will return the value
     * of the last snapshot.
     *
     * @return Number of URIs queued up and waiting for processing.
     *
     * @see org.archive.crawler.framework.Frontier#queuedUriCount()
     */
    public long queuedUriCount() {
        return shouldrun && this.controller != null && this.controller.getFrontier() != null ? controller.getFrontier().queuedUriCount() : queuedUriCount;
    }

    public long totalBytesWritten() {
        return shouldrun && this.controller != null && this.controller.getFrontier() != null ? controller.getFrontier().totalBytesWritten() : totalProcessedBytes;
    }

    /**
     * If the curi is a seed, we update the processedSeeds table.
     *
     * @param curi The CrawlURI that may be a seed.
     * @param disposition The dispositino of the CrawlURI.
     */
    private void handleSeed(CrawlURI curi, String disposition) {
        if (curi.isSeed()) {
            SeedRecord sr = new SeedRecord(curi, disposition);
            processedSeedsRecords.put(sr.getUri(), sr);
        }
    }

    public void crawledURISuccessful(CrawlURI curi) {
        handleSeed(curi, SEED_DISPOSITION_SUCCESS);
        incrementMapCount(statusCodeDistribution, Integer.toString(curi.getFetchStatus()));
        String mime = MimetypeUtils.truncate(curi.getContentType());
        incrementMapCount(mimeTypeDistribution, mime);
        incrementMapCount(mimeTypeBytes, mime, curi.getContentSize());
        saveHostStats((curi.getFetchStatus() == 1) ? "dns:" : this.controller.getServerCache().getHostFor(curi).getHostName(), curi.getContentSize());
        if (curi.containsKey(CrawlURI.A_SOURCE_TAG)) {
            saveSourceStats(curi.getString(CrawlURI.A_SOURCE_TAG), this.controller.getServerCache().getHostFor(curi).getHostName());
        }
    }

    protected void saveSourceStats(String source, String hostname) {
        synchronized (sourceHostDistribution) {
            HashMap<String, LongWrapper> hostUriCount = sourceHostDistribution.get(source);
            if (hostUriCount == null) {
                hostUriCount = new HashMap<String, LongWrapper>();
            }
            incrementMapCount(hostUriCount, hostname);
            sourceHostDistribution.put(source, hostUriCount);
        }
    }

    protected void saveHostStats(String hostname, long size) {
        synchronized (hostsDistribution) {
            incrementMapCount(hostsDistribution, hostname);
        }
        synchronized (hostsBytes) {
            incrementMapCount(hostsBytes, hostname, size);
        }
        synchronized (hostsLastFinished) {
            hostsLastFinished.put(hostname, new Long(System.currentTimeMillis()));
        }
    }

    public void crawledURINeedRetry(CrawlURI curi) {
        handleSeed(curi, SEED_DISPOSITION_RETRY);
    }

    public void crawledURIDisregard(CrawlURI curi) {
        handleSeed(curi, SEED_DISPOSITION_DISREGARD);
    }

    public void crawledURIFailure(CrawlURI curi) {
        handleSeed(curi, SEED_DISPOSITION_FAILURE);
    }

    /**
     * Get a seed iterator for the job being monitored. 
     * 
     * <b>Note:</b> This iterator will iterate over a list of <i>strings</i> not
     * UURIs like the Scope seed iterator. The strings are equal to the URIs'
     * getURIString() values.
     * @return the seed iterator
     * FIXME: Consider using TransformingIterator here
     */
    public Iterator<String> getSeeds() {
        List<String> seedsCopy = new Vector<String>();
        Iterator<UURI> i = controller.getScope().seedsIterator();
        while (i.hasNext()) {
            seedsCopy.add(i.next().toString());
        }
        return seedsCopy.iterator();
    }

    public Iterator getSeedRecordsSortedByStatusCode() {
        return getSeedRecordsSortedByStatusCode(getSeeds());
    }

    protected Iterator<SeedRecord> getSeedRecordsSortedByStatusCode(Iterator<String> i) {
        TreeSet<SeedRecord> sortedSet = new TreeSet<SeedRecord>(new Comparator<SeedRecord>() {

            public int compare(SeedRecord sr1, SeedRecord sr2) {
                int code1 = sr1.getStatusCode();
                int code2 = sr2.getStatusCode();
                if (code1 == code2) {
                    return sr1.getUri().compareTo(sr2.getUri());
                }
                code1 = -code1 - Integer.MAX_VALUE;
                code2 = -code2 - Integer.MAX_VALUE;
                return new Integer(code1).compareTo(new Integer(code2));
            }
        });
        while (i.hasNext()) {
            String seed = i.next();
            SeedRecord sr = (SeedRecord) processedSeedsRecords.get(seed);
            if (sr == null) {
                sr = new SeedRecord(seed, SEED_DISPOSITION_NOT_PROCESSED);
                processedSeedsRecords.put(seed, sr);
            }
            sortedSet.add(sr);
        }
        return sortedSet.iterator();
    }

    public void crawlEnded(String message) {
        logger.info("Entered crawlEnded");
        this.sExitMessage = message;
        super.crawlEnded(message);
        logger.info("Leaving crawlEnded");
    }

    /**
     * @param writer Where to write.
     */
    protected void writeSeedsReportTo(PrintWriter writer) {
        writer.print("[code] [status] [seed] [redirect]\n");
        seedsCrawled = 0;
        seedsNotCrawled = 0;
        for (Iterator i = getSeedRecordsSortedByStatusCode(getSeeds()); i.hasNext(); ) {
            SeedRecord sr = (SeedRecord) i.next();
            writer.print(sr.getStatusCode());
            writer.print(" ");
            if ((sr.getStatusCode() > 0)) {
                seedsCrawled++;
                writer.print("CRAWLED");
            } else {
                seedsNotCrawled++;
                writer.print("NOTCRAWLED");
            }
            writer.print(" ");
            writer.print(sr.getUri());
            if (sr.getRedirectUri() != null) {
                writer.print(" ");
                writer.print(sr.getRedirectUri());
            }
            writer.print("\n");
        }
    }

    protected void writeSourceReportTo(PrintWriter writer) {
        writer.print("[source] [host] [#urls]\n");
        for (Iterator i = sourceHostDistribution.keySet().iterator(); i.hasNext(); ) {
            Object sourceKey = i.next();
            Map<String, LongWrapper> hostCounts = (Map<String, LongWrapper>) sourceHostDistribution.get(sourceKey);
            SortedMap sortedHostCounts = getReverseSortedHostCounts(hostCounts);
            for (Iterator j = sortedHostCounts.keySet().iterator(); j.hasNext(); ) {
                Object hostKey = j.next();
                LongWrapper hostCount = (LongWrapper) hostCounts.get(hostKey);
                writer.print(sourceKey.toString());
                writer.print(" ");
                writer.print(hostKey.toString());
                writer.print(" ");
                writer.print(hostCount.longValue);
                writer.print("\n");
            }
        }
    }

    /**
     * Return a copy of the hosts distribution in reverse-sorted (largest first)
     * order.
     * 
     * @return SortedMap of hosts distribution
     */
    public SortedMap getReverseSortedHostCounts(Map<String, LongWrapper> hostCounts) {
        synchronized (hostCounts) {
            return getReverseSortedCopy(hostCounts);
        }
    }

    protected void writeHostsReportTo(PrintWriter writer) {
        SortedMap hd = getReverseSortedHostsDistribution();
        writer.print("[#urls] [#bytes] [host]\n");
        for (Iterator i = hd.keySet().iterator(); i.hasNext(); ) {
            Object key = i.next();
            if (hd.get(key) != null) {
                writer.print(((LongWrapper) hd.get(key)).longValue);
            } else {
                writer.print("-");
            }
            writer.print(" ");
            writer.print(getBytesPerHost((String) key));
            writer.print(" ");
            writer.print((String) key);
            writer.print("\n");
        }
    }

    /**
     * Return a copy of the hosts distribution in reverse-sorted
     * (largest first) order. 
     * @return SortedMap of hosts distribution
     */
    public SortedMap getReverseSortedHostsDistribution() {
        synchronized (hostsDistribution) {
            return getReverseSortedCopy(hostsDistribution);
        }
    }

    protected void writeMimetypesReportTo(PrintWriter writer) {
        writer.print("[#urls] [#bytes] [mime-types]\n");
        TreeMap fd = getReverseSortedCopy(getFileDistribution());
        for (Iterator i = fd.keySet().iterator(); i.hasNext(); ) {
            Object key = i.next();
            writer.print(Long.toString(((LongWrapper) fd.get(key)).longValue));
            writer.print(" ");
            writer.print(Long.toString(getBytesPerFileType((String) key)));
            writer.print(" ");
            writer.print((String) key);
            writer.print("\n");
        }
    }

    protected void writeResponseCodeReportTo(PrintWriter writer) {
        writer.print("[rescode] [#urls]\n");
        TreeMap scd = getReverseSortedCopy(getStatusCodeDistribution());
        for (Iterator i = scd.keySet().iterator(); i.hasNext(); ) {
            Object key = i.next();
            writer.print((String) key);
            writer.print(" ");
            writer.print(Long.toString(((LongWrapper) scd.get(key)).longValue));
            writer.print("\n");
        }
    }

    protected void writeCrawlReportTo(PrintWriter writer) {
        writer.print("Crawl Name: " + controller.getOrder().getCrawlOrderName());
        writer.print("\nCrawl Status: " + sExitMessage);
        writer.print("\nDuration Time: " + ArchiveUtils.formatMillisecondsToConventional(crawlDuration()));
        writer.print("\nTotal Seeds Crawled: " + seedsCrawled);
        writer.print("\nTotal Seeds not Crawled: " + seedsNotCrawled);
        writer.print("\nTotal Hosts Crawled: " + (hostsDistribution.size() - 1));
        writer.print("\nTotal Documents Crawled: " + finishedUriCount);
        writer.print("\nProcessed docs/sec: " + ArchiveUtils.doubleToString(docsPerSecond, 2));
        writer.print("\nBandwidth in Kbytes/sec: " + totalKBPerSec);
        writer.print("\nTotal Raw Data Size in Bytes: " + totalProcessedBytes + " (" + ArchiveUtils.formatBytesForDisplay(totalProcessedBytes) + ") \n");
    }

    protected void writeProcessorsReportTo(PrintWriter writer) {
        controller.reportTo(CrawlController.PROCESSORS_REPORT, writer);
    }

    protected void writeReportFile(String reportName, String filename) {
        File f = new File(controller.getDisk().getPath(), filename);
        try {
            PrintWriter bw = new PrintWriter(new FileWriter(f));
            writeReportTo(reportName, bw);
            bw.close();
            controller.addToManifest(f.getAbsolutePath(), CrawlController.MANIFEST_REPORT_FILE, true);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to write " + f.getAbsolutePath() + " at the end of crawl.", e);
        }
        logger.info("wrote report: " + f.getAbsolutePath());
    }

    /**
     * @param writer Where to write.
     */
    protected void writeManifestReportTo(PrintWriter writer) {
        controller.reportTo(CrawlController.MANIFEST_REPORT, writer);
    }

    /**
     * @param reportName Name of report.
     * @param w Where to write.
     */
    private void writeReportTo(String reportName, PrintWriter w) {
        if ("hosts".equals(reportName)) {
            writeHostsReportTo(w);
        } else if ("mime types".equals(reportName)) {
            writeMimetypesReportTo(w);
        } else if ("response codes".equals(reportName)) {
            writeResponseCodeReportTo(w);
        } else if ("seeds".equals(reportName)) {
            writeSeedsReportTo(w);
        } else if ("crawl".equals(reportName)) {
            writeCrawlReportTo(w);
        } else if ("processors".equals(reportName)) {
            writeProcessorsReportTo(w);
        } else if ("manifest".equals(reportName)) {
            writeManifestReportTo(w);
        } else if ("frontier".equals(reportName)) {
            writeFrontierReportTo(w);
        } else if ("source".equals(reportName)) {
            writeSourceReportTo(w);
        }
    }

    /**
     * Write the Frontier's 'nonempty' report (if available)
     * @param writer to report to
     */
    protected void writeFrontierReportTo(PrintWriter writer) {
        if (controller.getFrontier().isEmpty()) {
            writer.println("frontier empty");
        } else {
            controller.getFrontier().reportTo("nonempty", writer);
        }
    }

    /**
     * Run the reports.
     */
    public void dumpReports() {
        controller.addOrderToManifest();
        writeReportFile("hosts", "hosts-report.txt");
        writeReportFile("mime types", "mimetype-report.txt");
        writeReportFile("response codes", "responsecode-report.txt");
        writeReportFile("seeds", "seeds-report.txt");
        writeReportFile("crawl", "crawl-report.txt");
        writeReportFile("processors", "processors-report.txt");
        writeReportFile("manifest", "crawl-manifest.txt");
        writeReportFile("frontier", "frontier-report.txt");
        if (!sourceHostDistribution.isEmpty()) {
            writeReportFile("source", "source-report.txt");
        }
    }

    public void crawlCheckpoint(File cpDir) throws Exception {
        logNote("CRAWL CHECKPOINTING TO " + cpDir.toString());
    }
}
