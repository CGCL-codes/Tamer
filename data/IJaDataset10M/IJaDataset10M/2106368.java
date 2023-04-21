package com.gargoylesoftware.htmlunit.html.test;

import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlIsIndex;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.test.FakeWebConnection;
import com.gargoylesoftware.htmlunit.test.WebTestCase;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  Tests for HtmlIsIndex
 *
 * @version  $Revision: 26 $
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlIsIndexTest extends WebTestCase {

    /**
     *  Create an instance
     *
     * @param  name The name of the test
     */
    public HtmlIsIndexTest(final String name) {
        super(name);
    }

    public void testFormSubmission() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>" + "<form id='form1'>" + "<isindex prompt='enterSomeText'></isindex>" + "</form></body></html>";
        final WebClient client = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection(client);
        webConnection.setContent(htmlContent);
        client.setWebConnection(webConnection);
        final HtmlPage page = (HtmlPage) client.getPage(new URL("http://www.gargoylesoftware.com"), SubmitMethod.POST, Collections.EMPTY_LIST);
        final HtmlForm form = (HtmlForm) page.getHtmlElementById("form1");
        final HtmlIsIndex isInput = (HtmlIsIndex) form.getHtmlElementsByAttribute("isindex", "prompt", "enterSomeText").get(0);
        isInput.setValue("Flintstone");
        final Page secondPage = form.submit();
        final List expectedParameters = new ArrayList();
        expectedParameters.add(new KeyValuePair("enterSomeText", "Flintstone"));
        assertEquals("url", new URL("http://www.gargoylesoftware.com/"), secondPage.getWebResponse().getUrl());
        assertEquals("method", SubmitMethod.GET, webConnection.getLastMethod());
        assertEquals("parameters", expectedParameters, webConnection.getLastParameters());
    }
}
