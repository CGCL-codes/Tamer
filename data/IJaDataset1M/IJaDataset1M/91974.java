package org.rg.common.datatypes.irobot;

import java.net.MalformedURLException;
import java.net.URL;
import junit.framework.TestCase;

/**
 * test the server container class.
 * @author redman
 *
 */
public class ServerContainerTest extends TestCase {

    /**
    * test robots.txt support and filtering.
    * @throws MalformedURLException 
    */
    public void testRobotsTxtParsing() throws MalformedURLException {
        ServerContainer sc = new ServerContainer("http", "localhost", 80);
        String robotsTxt = "USER-agent: *\n" + "CRAWL-DELAY: 2.5\n" + "disallow: /cgi-bin/\n" + "Disallow : /tmp/\n" + "Disallow : /junk/";
        sc.setRobotsExclusion(robotsTxt);
        assertTrue(sc.getPolitenessDelay() == (int) (2.5 * 1000.0));
        assertFalse(sc.keepURL(new URL("http://localhost:80/cgi-bin/stary.html")));
        assertTrue(sc.keepURL(new URL("http://localhost:80/CGI-bin/stary.html")));
        assertFalse(sc.keepURL(new URL("http://localhost:80/tmp/stary.html")));
        assertFalse(sc.keepURL(new URL("http://localhost:80/junk/stary.html")));
        sc = new ServerContainer("http", "localhost", 80);
        robotsTxt = "USER-agent: *\n" + "disallow: /cgi-bin/\n" + "Disallow : /tmp/\n" + "Disallow : /junk/";
        sc.setRobotsExclusion(robotsTxt);
        assertTrue(sc.getPolitenessDelay() == ServerContainer.UNDEFINED);
    }
}
