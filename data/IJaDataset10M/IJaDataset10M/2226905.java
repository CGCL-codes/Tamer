package de.lichtflut.infra.html.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import de.lichtflut.infra.html.MTIAccessor;

/**
 * <p>
 *   This Class is providing specified information for a Set of defined targets
 * 	It could be also used as Thread
 * 	TODO: There could be a few problems with Mutual Exclusions.
 * 	Have a look at LinkedList-function-callOrder and behaviour
 * </p>
 * 
 * <p>
 * 	Created 04.06.2009
 * </p>
 *
 * @author Nils Bleisch
 */
public class MTICrawler implements Runnable {

    /** The raw data, which is collected by 
	 * all the crawler-threads is already "preprocessed" (DOM-Structure)
	 * and available/accessable via MTIAccessor-Objects.
	 * These accessors are added and stored in rawDataAccessors.
	 * rawDataAccessor can also be set by an extern function call,
	 * so a HTTP-Request isnt the only way to extracting information through
	 * X-HTML-Markup. 
	 */
    private List<MTIAccessor> rawDataAccessors;

    private final int MAX_BUFFER_CNT = 4;

    private List<CrawlersLittleHelper> helperThreads;

    private List<CrawlersLittleHelper> rdyQueue;

    /** This Map is holding the errors/exceptions,
	 * occured while crawling for specified targets
	 * One entry consists of the target-string (key) and the occurred Exception 
	 */
    private Map<String, Exception> occurredErrors = new HashMap<String, Exception>();

    private List<Object> targets = null;

    private int maxThreadCnt = 0;

    private MTICrawlerExtractionSpec extrationSpec;

    /**
	 * Constructor, initializing the Object
	 * Created: 04.06.2009
	 * @author Nils Bleisch
	 * @param  int  specifies the maximum amount of crawler-threads,
	 * a zero means unlimited
	 * Created: 04.06.2009
	 * @author Nils Bleisch
	 */
    public MTICrawler(int maxThreadCnt, MTICrawlerExtractionSpec extractionSpec) {
        this.maxThreadCnt = Math.abs(maxThreadCnt);
        this.maxThreadCnt = maxThreadCnt;
        this.rawDataAccessors = new LinkedList<MTIAccessor>();
        this.rdyQueue = new LinkedList<CrawlersLittleHelper>();
        this.helperThreads = new LinkedList<CrawlersLittleHelper>();
        this.extrationSpec = extractionSpec;
    }

    /**
	 * crawl-Method, starts the crawl-process,
	 * adding and removing new helperThreads, if possible
	 * @param  List targets, target-type: could be:
	 * io.Reader
	 * io.File
	 * net-URL
	 * Created: 04.06.2009
	 * @author Nils Bleisch
	 */
    public void crawl(List<Object> targets) {
        while (targets != null && targets.size() != 0 || helperThreads.size() != 0) {
            if (rawDataAccessors.size() == 0 && rdyQueue.size() == 0 && targets.size() != 0) {
                if (maxThreadCnt == 0 || maxThreadCnt > helperThreads.size()) {
                    CrawlersLittleHelper helper = new CrawlersLittleHelper(this, helperThreads.size());
                    helper.setTarget(targets.remove(0));
                    new Thread(helper).start();
                    helperThreads.add(helper);
                }
                continue;
            }
            while (rdyQueue.size() != 0) {
                if (rawDataAccessors.size() >= MAX_BUFFER_CNT && rdyQueue.size() >= MAX_BUFFER_CNT || targets.size() == 0) {
                    CrawlersLittleHelper helper = rdyQueue.remove(0);
                    helper.setTerminateCondition(true);
                    helperThreads.remove(helper);
                }
                if (targets.size() == 0) break;
                rdyQueue.remove(0).setTarget(targets.remove(0));
                if (rawDataAccessors.size() != 0) {
                    MTIAccessor accessor = rawDataAccessors.remove(0);
                    extrationSpec.extractSpecifiedInformation(accessor);
                }
            }
        }
        stopAndRemoveThreads();
        while (rawDataAccessors.size() != 0) {
            MTIAccessor accessor = rawDataAccessors.remove(0);
            extrationSpec.extractSpecifiedInformation(accessor);
        }
    }

    /**
	 * This Method is removing all Threads (CrawlersLittleHelper)
	 * If there is an active Thread, it would be stopped
	 * adding and removing new helperThreads, if possible
	 * Created: 09.06.2009
	 * @author Nils Bleisch
	 */
    public void stopAndRemoveThreads() {
        for (CrawlersLittleHelper helperThread : helperThreads) {
            helperThread.setTerminateCondition(true);
        }
        helperThreads.removeAll(helperThreads);
        rdyQueue.retainAll(rdyQueue);
    }

    public void run() {
        crawl(getTargets());
    }

    private List<Object> getTargets() {
        return targets;
    }

    public List<MTIAccessor> getRawDataAccessors() {
        return rawDataAccessors;
    }

    public List<CrawlersLittleHelper> getRdyQueue() {
        return rdyQueue;
    }

    public List<CrawlersLittleHelper> getHelperThreads() {
        return helperThreads;
    }

    public Map<String, Exception> getOccurredErrors() {
        return occurredErrors;
    }

    public void setTargets(final List<Object> targets) {
        this.targets = targets;
    }

    /**
	 * inner-class, used as Thread
	 * Delivers MTIAccessor-Objects
	 * to MTICrawler for specified target-URL's
	 * Created: 04.06.2009
	 * @author Nils Bleisch
	 */
    private static class CrawlersLittleHelper implements Runnable {

        private MTICrawler crawlerManager;

        private Object target;

        private boolean terminateCondition = false;

        public CrawlersLittleHelper(MTICrawler manager, int helperID) {
            this.crawlerManager = manager;
        }

        /**
		 * Run method to crawl for specified targets
		 * The result is available as an MTIAccessor-Object
		 * and is stored in rawDataAccessor-Collection of MTICrawler 
		 * Created: 03.06.2009
		 * @author Nils Bleisch
		 */
        public void run() {
            while (!terminateCondition) {
                if (target == null) continue;
                MTIAccessor accessor = null;
                try {
                    if (target instanceof Reader) accessor = new MTIAccessor(MTIAccessor.generateExtractor((Reader) target)); else if (target instanceof File) accessor = new MTIAccessor(MTIAccessor.generateExtractor((File) target)); else if (target instanceof URL) {
                        accessor = new MTIAccessor(MTIAccessor.generateExtractor(((URL) target)));
                    } else {
                        crawlerManager.getOccurredErrors().put(target.toString(), new Exception("Type of Target:" + target.getClass().getName() + " is not supported"));
                    }
                } catch (FileNotFoundException any) {
                    crawlerManager.getOccurredErrors().put(target.toString(), any);
                    target = null;
                    crawlerManager.getRdyQueue().add(this);
                    continue;
                } catch (IOException any) {
                    crawlerManager.getOccurredErrors().put(target.toString(), any);
                    target = null;
                    crawlerManager.getRdyQueue().add(this);
                    continue;
                }
                crawlerManager.getRawDataAccessors().add(accessor);
                target = null;
                crawlerManager.getRdyQueue().add(this);
            }
        }

        public void setTarget(Object target) {
            this.target = target;
        }

        public void setTerminateCondition(boolean condition) {
            terminateCondition = condition;
        }
    }
}
