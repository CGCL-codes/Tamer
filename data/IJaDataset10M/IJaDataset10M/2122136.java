package syndication.rss;

import syndication.DefaultSyndicationLoader;
import syndication.SyndicationErrorObserver;

/**
 *
 * @author Omistaja
 */
public class RssFeedLoader extends DefaultSyndicationLoader {

    public RssFeedLoader() {
        super();
    }

    public Thread startLoadingAsync(String feedUrl, RssFeedHandlerObserver feedObserver, SyndicationErrorObserver errorObserver) {
        RssFeedHandler feedHandler = new RssFeedHandler(feedObserver);
        SyndicationLoadingRunnable loadingRunnable = new SyndicationLoadingRunnable(feedUrl, feedHandler, errorObserver);
        ThreadGroup tg = getThreadGroup();
        Thread t = new Thread(tg, loadingRunnable);
        t.start();
        return t;
    }

    public void startLoadingSync(String feedUrl, RssFeedHandlerObserver handlerObserver, SyndicationErrorObserver errorObserver) {
        Thread t = startLoadingAsync(feedUrl, handlerObserver, errorObserver);
        if (t.isAlive()) {
            try {
                t.join();
            } catch (InterruptedException ie) {
                System.out.println(ie.getLocalizedMessage());
                ie.printStackTrace();
            }
        }
    }
}
