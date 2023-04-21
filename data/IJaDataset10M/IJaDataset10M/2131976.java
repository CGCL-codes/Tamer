package org.jamwiki.servlets;

import org.jamwiki.JAMWikiUnitTest;
import org.jamwiki.model.VirtualWiki;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import static org.junit.Assert.*;

public class WikiPageInfoTest extends JAMWikiUnitTest {

    /**
	 *
	 */
    @Test
    public void testGetVirtualWiki() {
        MockHttpServletRequest mockRequest = this.getMockHttpServletRequest("/virtual/Topic");
        WikiPageInfo p = new WikiPageInfo(mockRequest);
        assertEquals(p.getVirtualWikiName(), "virtual", p.getVirtualWikiName());
        p.setVirtualWikiName("en");
        assertEquals(p.getVirtualWikiName(), "en", p.getVirtualWikiName());
        mockRequest = this.getMockHttpServletRequest("/");
        p = new WikiPageInfo(mockRequest);
        assertEquals(p.getVirtualWikiName(), VirtualWiki.defaultVirtualWiki().getName(), p.getVirtualWikiName());
    }

    /**
	 *
	 */
    private MockHttpServletRequest getMockHttpServletRequest(String url) {
        MockServletContext mockContext = new MockServletContext("context");
        return new MockHttpServletRequest(mockContext, "GET", url);
    }
}
