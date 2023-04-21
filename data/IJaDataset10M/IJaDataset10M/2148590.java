package org.tolven.wiki.test;

import org.tolven.wiki.common.Logger;
import org.tolven.wiki.download.ActionQuery;
import org.tolven.wiki.load.WikiProperties;
import org.tolven.wiki.load.WikiUploadClientUsingPhp;
import junit.framework.TestCase;

/**
 * 
 * @author Anil
 *
 */
public class TestTSW extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testHttpClientLogin() {
        WikiProperties lProps = new WikiProperties();
        Logger.log("\n #########################  Start testHttpClientLogin");
        Logger.log(lProps.getProperty("wikiUrl"));
        WikiUploadClientUsingPhp wikiClient = new WikiUploadClientUsingPhp(lProps.getProperty("wikiUrl"), lProps.getProperty("wikiUser"), lProps.getProperty("wikiPwd"));
        Logger.log("End testHttpClientLogin \n");
    }

    public void testUploadUsingPhp() {
        WikiProperties lProps = new WikiProperties();
        String content = "TestTSW /n" + " ---ENDOFTITLE--- /n" + " Body TestTSW /n" + "---ENDOFBODY---  /n";
        Logger.log("\n #########################  Start testUploadUsingPhp");
        Logger.log(lProps.getProperty("wikiUrl"));
        WikiUploadClientUsingPhp wikiClient = new WikiUploadClientUsingPhp(lProps.getProperty("wikiUrl"), lProps.getProperty("wikiUser"), lProps.getProperty("wikiPwd"));
        wikiClient.upload(content);
        Logger.log("End testUploadUsingPhp \n");
    }

    public void testMediaWikiApiLogin() {
        WikiProperties lProps = new WikiProperties();
        Logger.log("\n #########################  Start testMediaWikiApiLogin");
        Logger.log(lProps.getProperty("wikiUrl"));
        Logger.log(lProps.getProperty("wikiUser"));
        Logger.log(lProps.getProperty("wikiPwd"));
        ActionQuery query1 = new ActionQuery(lProps.getProperty("wikiUrl"), lProps.getProperty("wikiUser"), lProps.getProperty("wikiPwd"));
        assertEquals(true, query1.isLoggedIn());
        Logger.log("End testMediaWikiApiLogin \n");
    }
}
