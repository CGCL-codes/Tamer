package feedReader;

import mobi.ilabs.common.MIDletContext;
import mobi.ilabs.common.Debug;
import javax.microedition.midlet.MIDlet;
import mobi.ilabs.feed.FeedList;

/**
* FeedReader sample application.
* <p>
* @author �ystein Myhre
*/
public class FeedReader extends MIDlet implements Debug.Constants {

    public FeedReader() {
        MIDletContext.setMidlet(this);
    }

    public void destroyApp(boolean unconditional) {
    }

    public void pauseApp() {
    }

    public void startApp() {
        if (DEBUG) Debug.println("FeedReader.startApp: ");
        try {
            FeedList feedList = new FeedList();
            feedList.addFeed("Dagbladet", "http://www.dagbladet.no/rss/nyheter/", null);
            feedList.addFeed("Aftenposten", "http://www.aftenposten.no/eksport/rss-1_0/", null);
            feedList.addFeed("Digi.no", "http://www.digi.no/phpf/feed/rss/digi.php", null);
            feedList.addFeed("Engadget", "http://www.engadget.com/rss.xml", null);
            FeedListScreen feedListScreen = new FeedListScreen(feedList);
            feedListScreen.show();
        } catch (Throwable e) {
            Debug.EXCEPTION("FeedReader.startApp", e);
        }
    }
}
