package net.sf.javadc.gui;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import net.sf.javadc.interfaces.IDownloadManager;
import net.sf.javadc.interfaces.IHub;
import net.sf.javadc.interfaces.IHubManager;
import net.sf.javadc.interfaces.ISearchRequestFactory;
import net.sf.javadc.interfaces.ISegmentManager;
import net.sf.javadc.interfaces.ISettings;
import net.sf.javadc.listeners.HubListener;
import net.sf.javadc.mockups.BaseHub;
import net.sf.javadc.mockups.BaseSettings;
import net.sf.javadc.net.DownloadManager;
import net.sf.javadc.net.SearchRequest;
import net.sf.javadc.net.SearchRequestFactory;
import net.sf.javadc.net.SearchResult;
import net.sf.javadc.net.SegmentManager;
import net.sf.javadc.net.hub.AllHubs;
import net.sf.javadc.net.hub.HubManager;

/**
 * @author Timo Westk�mper
 */
public class SearchComponentHubListenerExtTest extends TestCase {

    protected MySearchComponent searchComponent;

    protected SearchRequest searchRequest1, searchRequest2;

    protected List models, searchRequests;

    private ISearchRequestFactory factory = new SearchRequestFactory();

    private IHub hub = new BaseHub();

    protected ISettings settings = new BaseSettings(true);

    private IHubManager hubManager = new HubManager();

    private ISegmentManager segmentManager = new SegmentManager(settings);

    protected IDownloadManager downloadManager = new DownloadManager(hubManager, segmentManager);

    protected IHub allHubs = new AllHubs(hubManager);

    protected HubListener hubListener;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(SearchComponentHubListenerExtTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        searchComponent = new MySearchComponent(hub, settings, downloadManager);
        searchRequest1 = factory.createFromQuery("F?F?0?1?eeee");
        searchRequest1.setFreeSlots(true);
        hubListener = searchComponent.getHubListener();
    }

    /**
     * Constructor for SearchComponentHubListenerExtTest.
     * 
     * @param arg0
     */
    public SearchComponentHubListenerExtTest(String arg0) {
        super(arg0);
    }

    public void testSearchResultMatching1() {
        hubListener.searchResultAdded(hub, new SearchResult(hub, "timowest", "eeee.gif", settings, 1), searchRequest1);
        assertEquals(searchComponent.srs.size(), 1);
    }

    public void testSearchResultMatching2() {
        hubListener.searchResultAdded(hub, new SearchResult(hub, "timowest", "eeee.gif", settings, 0), searchRequest1);
        assertEquals(searchComponent.srs.size(), 0);
    }

    public void testSearchResultMatching3() {
        hubListener.searchResultAdded(hub, new SearchResult(hub, "timowest", "eefe.gif", settings, 1), searchRequest1);
        assertEquals(searchComponent.srs.size(), 0);
    }

    private class MySearchComponent extends SearchComponent {

        public final List srs = new ArrayList();

        public MySearchComponent(IHub _hub, ISettings _settings, IDownloadManager _downloadManager) {
            super(_hub, _settings, _downloadManager);
        }

        public void addSearch(SearchRequest searchRequest) {
            super.addSearch(searchRequest);
            if (searchRequest != null) srs.add(searchRequest);
        }
    }
}
