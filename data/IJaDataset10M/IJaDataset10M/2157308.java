package org.lorecraft.routes;

import org.lorecraft.routes.RouteFactory;
import org.lorecraft.routes.RouteSet;
import junit.framework.TestCase;

public class RouteSetTest extends TestCase {

    public void testRouteSet() throws Exception {
        RouteSet routeSet = RouteFactory.get().createRouteSet().add("/", "/blog", "action:index").add("/rss.xml", "/blog", "action:feed", "feedType:rss").add("/:year[\\d{4}]/:month/:day", "/blog", "action:by_date").add("/show/:id", "/blog", "action:show").add("/:action/:id[\\d+]", "/blog");
        assertEquals("index", routeSet.map("/").getAction());
        assertEquals("show", routeSet.map("/show/1").getAction());
        assertEquals("by_date", routeSet.map("/2005").getAction());
        assertEquals("by_date", routeSet.map("/2005/10").getAction());
        assertEquals("2005", routeSet.map("/2005/10").getParams().get("year")[0]);
        assertEquals("10", routeSet.map("/2005/10").getParams().get("month")[0]);
        assertEquals("by_date", routeSet.map("/2005/10/5").getAction());
        assertEquals("entry", routeSet.map("/entry/2005").getAction());
        assertEquals("feed", routeSet.map("/rss.xml").getAction());
        assertEquals("rss", routeSet.map("/rss.xml").getParams().get("feedType")[0]);
    }

    public void testPathTranslator() {
        RouteSet routeSet = RouteFactory.get().createRouteSet().add("/", "/blog", "action:index").add("/:year[\\d{4}]/:month/:day", "/blog", "action:by_date").add("/show/:id", "/blog", "action:show").add("/feed/:feedType.xml", "/blog", "action:deliver").add("/proxy/*path", "/blog", "action:proxy").add("/:action/:id[\\d+]", "/blog");
        assertEquals("/find/100", routeSet.getPath("/blog", "find", "id:100"));
        assertEquals("/show/100", routeSet.getPath("/blog", "show", "id:100"));
        assertEquals("/2005", routeSet.getPath("/blog", "by_date", "year:2005"));
        assertEquals("/2005/12", routeSet.getPath("/blog", "by_date", "year:2005", "month:12"));
        assertEquals("/2005/12/18", routeSet.getPath("/blog", "by_date", "year:2005", "month:12", "day:18"));
        assertEquals("/feed/rss.xml", routeSet.getPath("/blog", "deliver", "feedType:rss"));
        assertEquals("/proxy/path/to/file.txt", routeSet.getPath("/blog", "proxy", "path:/path/to/file.txt"));
        assertEquals("/show/100?page=1&name=My+name+is+%3CJos%C3%A9%3E", routeSet.getPath("/blog", "show", "id:100", "page:1", "name:My name is <José>"));
        assertEquals("/", routeSet.getPath("/blog", "index"));
    }
}
