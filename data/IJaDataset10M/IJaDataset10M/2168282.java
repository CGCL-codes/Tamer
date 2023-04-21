package org.ambiance.yahoo.web;

import org.ambiance.yahoo.QueryYahooService;
import org.codehaus.plexus.PlexusTestCase;

public class AmbianceYahooWebTest extends PlexusTestCase {

    public void testRequest() {
        Exception e = null;
        QueryYahooService yahoo = null;
        Object result = null;
        try {
            yahoo = (QueryYahooService) lookup("org.ambiance.yahoo.AmbianceYahooService", "web");
            result = yahoo.query("intitle:index.of (mp3 OR ogg OR flac) \"mf doom\"");
        } catch (Exception e1) {
            System.out.println(e1.getMessage());
            e = e1;
        }
        assertNull(e);
        assertNotNull(yahoo);
        assertNotNull(result);
    }
}
