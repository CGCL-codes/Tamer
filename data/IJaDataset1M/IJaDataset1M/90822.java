package org.hip.kernel.servlet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.sql.ResultSet;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.bom.impl.DefaultQueryResult;
import org.hip.kernel.bom.impl.test.DataHouseKeeper;
import org.hip.kernel.servlet.Context;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author: Benno Luthiger
 */
public class PageableHtmlPageTest {

    private static DataHouseKeeper data;

    private static final String NL = "" + (char) 13 + (char) 10;

    private static final String EXPECTED = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n" + "<meta http-equiv=\"content-type\" content=\"text/html;charset=utf8\" />\n" + "<title>VIF</title>\n</head>\n<body bgcolor='#FFFFFF' text='#505050' onLoad=\"\">" + NL + "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><transformed><Root>";

    @BeforeClass
    public static void init() {
        data = DataHouseKeeper.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        data.deleteAllFromSimple();
        System.out.println("Deleted all entries in tblTest.");
    }

    @Test
    public void testDo() throws Exception {
        Context lContext = new TestContext();
        TestPrintWriter lWriter = new TestPrintWriter();
        TestPageableHtmlPage lPage = new TestPageableHtmlPage(lContext);
        for (int i = 1; i < 16; i++) data.createTestEntry("test " + i);
        ResultSet lResult = data.executeQuery("SELECT TESTID, SNAME, SFIRSTNAME FROM tblTest ORDER BY sName");
        QueryResult lQueryResult = new DefaultQueryResult(data.getSimpleHome(), lResult, null);
        TestXSLQueryResultView lResultView = new TestXSLQueryResultView(lContext, lQueryResult, 15);
        lPage.add(lResultView);
        lPage.renderToWriter(lWriter, "");
        String lRendering = lWriter.getStreamedText();
        assertEquals("header 1", 0, lRendering.indexOf(EXPECTED));
        assertTrue("content page 1.1", lRendering.indexOf("<Name>test 1</Name>") >= 0);
        assertTrue("content page 1.2", lRendering.indexOf("<Name>test 10</Name>") >= 0);
        assertTrue("content page 1.3", lRendering.indexOf("<Name>test 11</Name>") >= 0);
        assertTrue("content page 1.4", lRendering.indexOf("<Name>test 12</Name>") >= 0);
        assertEquals("not content page 1.1", -1, lRendering.indexOf("<Name>test 13</Name>"));
        assertEquals("not content page 1.2", -1, lRendering.indexOf("<Name>test 2</Name>"));
        lWriter.close();
        lWriter = new TestPrintWriter();
        lPage.setToLastPage();
        lPage.renderToWriter(lWriter, "");
        lRendering = lWriter.getStreamedText();
        lResultView.renderToWriter(lWriter, "");
        assertEquals("header 2", 0, lRendering.indexOf(EXPECTED));
        assertTrue("content page 2.1", lRendering.indexOf("<Name>test 7</Name>") >= 0);
        assertTrue("content page 2.2", lRendering.indexOf("<Name>test 8</Name>") >= 0);
        assertTrue("content page 2.3", lRendering.indexOf("<Name>test 9</Name>") >= 0);
        assertEquals("not content page 2.1", -1, lRendering.indexOf("<Name>test 10</Name>"));
        assertEquals("not content page 2.2", -1, lRendering.indexOf("<Name>test 6</Name>"));
        lWriter.close();
        lWriter = new TestPrintWriter();
        lPage.nextPage();
        lPage.renderToWriter(lWriter, "");
        lRendering = lWriter.getStreamedText();
        lResultView.renderToWriter(lWriter, "");
        assertEquals("header 3", 0, lRendering.indexOf(EXPECTED));
        assertTrue("content page 3.1", lRendering.indexOf("<Name>test 7</Name>") >= 0);
        assertTrue("content page 3.2", lRendering.indexOf("<Name>test 8</Name>") >= 0);
        assertTrue("content page 3.3", lRendering.indexOf("<Name>test 9</Name>") >= 0);
        assertEquals("not content page 3.1", -1, lRendering.indexOf("<Name>test 10</Name>"));
        assertEquals("not content page 3.2", -1, lRendering.indexOf("<Name>test 6</Name>"));
        lWriter.close();
        lWriter = new TestPrintWriter();
        lPage.previousPage();
        lPage.renderToWriter(lWriter, "");
        lRendering = lWriter.getStreamedText();
        lResultView.renderToWriter(lWriter, "");
        assertEquals("header 4", 0, lRendering.indexOf(EXPECTED));
        assertTrue("content page 4.1", lRendering.indexOf("<Name>test 3</Name>") >= 0);
        assertTrue("content page 4.2", lRendering.indexOf("<Name>test 4</Name>") >= 0);
        assertTrue("content page 4.3", lRendering.indexOf("<Name>test 5</Name>") >= 0);
        assertTrue("content page 4.4", lRendering.indexOf("<Name>test 6</Name>") >= 0);
        assertEquals("not content page 4.1", -1, lRendering.indexOf("<Name>test 7</Name>"));
        assertEquals("not content page 4.2", -1, lRendering.indexOf("<Name>test 2</Name>"));
        lWriter.close();
        lWriter = new TestPrintWriter();
        lPage.setToFirstPage();
        lPage.renderToWriter(lWriter, "");
        lRendering = lWriter.getStreamedText();
        lResultView.renderToWriter(lWriter, "");
        assertEquals("header 5", 0, lRendering.indexOf(EXPECTED));
        assertTrue("content page 5.1", lRendering.indexOf("<Name>test 1</Name>") >= 0);
        assertTrue("content page 5.2", lRendering.indexOf("<Name>test 10</Name>") >= 0);
        assertTrue("content page 5.3", lRendering.indexOf("<Name>test 11</Name>") >= 0);
        assertTrue("content page 5.4", lRendering.indexOf("<Name>test 12</Name>") >= 0);
        assertEquals("not content page 5.1", -1, lRendering.indexOf("<Name>test 13</Name>"));
        assertEquals("not content page 5.2", -1, lRendering.indexOf("<Name>test 2</Name>"));
        lWriter.close();
        lWriter = new TestPrintWriter();
        lPage.previousPage();
        lPage.renderToWriter(lWriter, "");
        lRendering = lWriter.getStreamedText();
        lResultView.renderToWriter(lWriter, "");
        assertEquals("header 6", 0, lRendering.indexOf(EXPECTED));
        assertTrue("content page 6.1", lRendering.indexOf("<Name>test 1</Name>") >= 0);
        assertTrue("content page 6.2", lRendering.indexOf("<Name>test 10</Name>") >= 0);
        assertTrue("content page 6.3", lRendering.indexOf("<Name>test 11</Name>") >= 0);
        assertTrue("content page 6.4", lRendering.indexOf("<Name>test 12</Name>") >= 0);
        assertEquals("not content page 6.1", -1, lRendering.indexOf("<Name>test 13</Name>"));
        assertEquals("not content page 6.2", -1, lRendering.indexOf("<Name>test 2</Name>"));
    }
}
