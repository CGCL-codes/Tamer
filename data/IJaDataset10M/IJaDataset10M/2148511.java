package net.sourceforge.retriever.frontier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class FrontierURLTest {

    @Test
    public void testCreateFrontierURL() {
        final FrontierUrl frontierUrl = new FrontierUrl();
        frontierUrl.setUrl("http://www.stela.org.br/");
        frontierUrl.setIp("0.0.0.1");
        assertEquals("http://www.stela.org.br/", frontierUrl.getUrl());
        assertEquals("0.0.0.1", frontierUrl.getIp());
    }

    @Test
    public void testEqualObjects() {
        final FrontierUrl frontierUrl1 = new FrontierUrl();
        frontierUrl1.setUrl("http://www.stela.org.br/");
        frontierUrl1.setIp("0.0.0.1");
        final FrontierUrl frontierUrl2 = new FrontierUrl();
        frontierUrl2.setUrl("http://www.stela.org.br/");
        frontierUrl2.setIp("0.0.2.1");
        assertEquals(frontierUrl1, frontierUrl2);
    }

    @Test
    public void testDifferentObjects() {
        final FrontierUrl frontierUrl1 = new FrontierUrl();
        frontierUrl1.setUrl("http://www.stela.org.br/localizacao/");
        frontierUrl1.setIp("0.0.0.1");
        final FrontierUrl frontierUrl2 = new FrontierUrl();
        frontierUrl2.setUrl("http://www.stela.org.br/");
        frontierUrl2.setIp("0.0.0.1");
        assertNotSame(frontierUrl1, frontierUrl2);
    }

    @Test
    public void testAdditionalInfo() {
        final FrontierUrl frontierURL = new FrontierUrl();
        final Map<String, Object> additionalInfo = new HashMap<String, Object>();
        additionalInfo.put("key", "parent info");
        frontierURL.setAdditionalInfo(additionalInfo);
        assertEquals("parent info", frontierURL.getAdditionalInfo().get("key"));
    }

    @Test
    public void testGetParent() {
        final FrontierUrl parent = new FrontierUrl();
        parent.setUrl("http://www.google.com/");
        parent.setIp("10.0.0.1");
        final FrontierUrl frontierURL = new FrontierUrl();
        frontierURL.setParent(parent);
        assertEquals("http://www.google.com/", frontierURL.getParent().getUrl());
        assertEquals("10.0.0.1", frontierURL.getParent().getIp());
    }

    @Test
    public void testSetSleepAfterFetch() {
        final FrontierUrl frontierURL = new FrontierUrl();
        assertEquals(0l, frontierURL.getTimeToSleepInMillis());
        frontierURL.setTimeToSleepInMillis(1000);
        assertEquals(1000l, frontierURL.getTimeToSleepInMillis());
    }

    @Test
    public void testGetExceptionFetching() {
        final FrontierUrl url = new FrontierUrl();
        url.setExceptionFetching(new Throwable("Exception message."));
        assertEquals("Exception message.", url.getExceptionFetching().getMessage());
    }
}
