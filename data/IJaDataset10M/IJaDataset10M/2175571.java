package job;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import core.*;
import java.io.IOException;
import org.xml.sax.SAXException;
import java.util.*;

public class MarketingCrossingAgent extends SuopfAgent {

    private long WAKEUP = 1000L * 60L;

    private ArrayList previous = new ArrayList();

    public MarketingCrossingAgent() {
        super("marketing crossing");
    }

    protected void setup() {
        if (activated) {
            String url = getParameter("url");
            String search = getParameter("search");
            String twitterUsername = getParameter("twitter.user");
            String twitterPassword = getParameter("twitter.password");
            String links = getParameter("previous");
            StringTokenizer tokens = new StringTokenizer(links, ",");
            while (tokens.hasMoreTokens()) {
                String link = tokens.nextToken();
                if (link != "") {
                    previous.add(link);
                }
            }
            addBehaviour(new MarketingCrossingChecker(url, search, twitterUsername, twitterPassword, this, WAKEUP));
        }
    }

    private void commit() {
        StringBuffer list = new StringBuffer();
        for (Iterator i = previous.iterator(); i.hasNext(); ) {
            list.append(i.next());
            list.append(",");
        }
        saveSetting("previous", list.toString());
    }

    class MarketingCrossingChecker extends TickerBehaviour {

        private String url = null;

        private String search = null;

        private String twitterUser = null;

        private String twitterPassword = null;

        public MarketingCrossingChecker(String url, String search, String twitterUsername, String twitterPassword, Agent a, long period) {
            super(a, period);
            this.url = url;
            this.search = search;
            this.twitterUser = twitterUsername;
            this.twitterPassword = twitterPassword;
        }

        protected void onTick() {
            MarketingCrossingReader reader = new MarketingCrossingReader(url);
            MatchHandler handler = new MatchHandler(twitterUser, twitterPassword);
            reader.addHandler(handler);
            try {
                reader.process(search);
                commit();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (SAXException se) {
                se.printStackTrace();
            }
        }
    }

    class MatchHandler implements ProcessPageRequest {

        private TwitterStatus status = null;

        public MatchHandler(String user, String pass) {
            status = new TwitterStatus(user, pass);
        }

        public void processPage(String title, String link) {
            if (!previous.contains(link)) {
                previous.add(link);
                StringBuffer msg = new StringBuffer();
                if ((title.length() + link.length()) < 138) {
                    msg.append(title);
                    msg.append(": ");
                }
                msg.append(link);
                status.update(msg.toString());
            }
        }
    }
}
