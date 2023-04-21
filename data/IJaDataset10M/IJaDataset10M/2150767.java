package test;

import com.jellymold.boss.NewsSearch;
import com.jellymold.boss.NewsSearchResult;
import junit.framework.TestCase;
import java.util.List;

public class NewsSearchTest extends TestCase {

    public NewsSearchTest() {
    }

    public void setUp() {
    }

    public void tearDown() {
    }

    public void testNextPage() {
        NewsSearch ws = new NewsSearch();
        assertEquals(500, ws.getNextPage());
        ws.search("obama");
        List<NewsSearchResult> results = ws.getResults();
        assertEquals(200, ws.getNextPage());
        List<NewsSearchResult> results2 = ws.getResults();
        assertFalse(results.equals(results2));
    }

    public void testPreviousPage() {
        NewsSearch ws = new NewsSearch();
        assertEquals(500, ws.getPreviousPage());
        ws.search("obama");
        List<NewsSearchResult> results = ws.getResults();
        assertEquals(200, ws.getNextPage());
        assertEquals(200, ws.getPreviousPage());
        List<NewsSearchResult> results2 = ws.getResults();
        assertFalse(results.equals(results2));
    }
}
