package dbPhase.testing;

import static org.junit.Assert.*;
import junit.framework.TestCase;
import org.junit.Test;
import dbPhase.hypeerweb.WebId;

public class WebIdTest extends TestCase {

    public WebIdTest(String s) {
        super(s);
    }

    @Test
    public void testWebId() {
        WebId web = new WebId(0);
        if (!(web instanceof WebId) && web.getId() == 0) fail("WebId(int) constructor failed");
    }

    @Test
    public void testSetId() {
        WebId web = new WebId(0);
        web.setId(1);
        int id = web.getId();
        assertEquals(1, id);
    }

    @Test
    public void testGetId() {
        WebId web = new WebId(2);
        int id = web.getId();
        assertEquals(2, id);
    }

    @Test
    public void testHeight() {
        WebId web = new WebId(0);
        assertEquals(0, web.height());
        web.setId(-1);
        assertEquals(-1, web.height());
        web.setId(3);
        assertEquals(2, web.height());
        web.setId(34);
        assertEquals(6, web.height());
        web.setId(8);
        assertEquals(4, web.height());
    }

    @Test
    public void testToString() {
        WebId webId = new WebId(0);
        assertEquals("0", webId.toString());
    }

    @Test
    public void testEquals() {
        WebId webId = new WebId(0);
        WebId webId0 = new WebId(0);
        WebId webId1 = new WebId(1);
        int webIdInt = 4;
        assertTrue(webId.equals(webId0));
        assertTrue(!webId.equals(webId1));
        assertTrue(!webId.equals(webIdInt));
    }
}
