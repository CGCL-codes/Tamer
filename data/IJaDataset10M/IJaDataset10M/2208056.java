package ch.ethz.dcg.spamato.filter.domainator.search;

import org.json.JSONObject;
import ch.ethz.dcg.spamato.test.SpamatoTestCase;

/**
 * @since XXX
 */
public class GoogleDomainSearchTest extends SpamatoTestCase {

    private GoogleDomainSearch search;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.search = new GoogleDomainSearch();
    }

    @Override
    protected void tearDown() throws Exception {
        this.search = null;
        super.tearDown();
    }

    public void testAnalyzeResponseJsonValid() throws Exception {
        final String jsonData = loadStringFromResource("/json-example-valid.txt");
        JSONObject json = new JSONObject(jsonData);
        int result = this.search.analyseResponse(json);
        assertEquals(59600000, result);
    }

    public void testDoSearchSpamDomain1() {
        SearchResult res = this.search.doSearch("chuonthis.com", "junit-tests");
        assertNotNull(res);
        System.out.println("res: " + res);
    }
}
