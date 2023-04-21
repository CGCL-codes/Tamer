package itjava.presenter;

import itjava.model.LinkStore;
import itjava.model.LinkStoreScrape;
import itjava.model.ResultEntry;
import itjava.model.ResultEntryStore;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class StackOverflowScraper {

    private String _query;

    private Iterable<String> _cacheLinks;

    public StackOverflowScraper(String query) {
        _query = query;
    }

    public Iterable<String> ShowLinks() {
        return _cacheLinks;
    }

    /**
	 * Uses web search APIs to return a list of {@link ResultEntry} objects
	 * @return Search results
	 */
    public ArrayList<ResultEntry> SearchNext() {
        ArrayList<URL> currLinks = new ArrayList<URL>();
        for (String link : _cacheLinks) {
            try {
                currLinks.add(new URL(link));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return ResultEntryStore.createResultEntryList(currLinks);
    }
}
