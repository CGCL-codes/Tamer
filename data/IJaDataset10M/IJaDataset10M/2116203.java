package info.goldenorb.search;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LuceneIndexSearchTest {

    final String expected = "-1";

    @Test
    public void correlate() throws Exception {
        int result;
        final String directory = "/InstitutoStela/work/related_projects/isnewsletter/files/index";
        LuceneIndexSearch lis = new LuceneIndexSearch(directory);
        try {
            final String query = "\"iskmm alex\"~100 " + "\"alexandre iskmm goncalves\"~100 " + "\"alexandre iskmm leopoldo goncalves\"~100";
            final String field = "search";
            result = lis.search(query, field);
        } finally {
            if (lis != null) lis.close();
        }
        assertEquals(String.valueOf(result), expected);
    }
}
