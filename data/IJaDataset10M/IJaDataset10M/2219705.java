package enzimaweb;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WebRequestContextTest {

    private WebRequestContext context;

    @Before
    public void setUp() throws Exception {
        context = new WebRequestContext();
    }

    @Test
    public void testGetOriginalPath() {
        String path = context.getOriginalPath();
        Assert.assertEquals("Incorrect default original path.", "", path);
    }

    @Test
    public void testGetContextPath() {
        String path = context.getContextPath();
        Assert.assertEquals("Incorrect default context path.", "", path);
    }

    @Test
    public void testGetOriginalUrl() {
        String path = context.getOriginalUrl(true);
        Assert.assertEquals("Incorrect default original URL.", "", path);
        path = context.getOriginalUrl(false);
        Assert.assertEquals("Incorrect default original URL.", "", path);
    }
}
