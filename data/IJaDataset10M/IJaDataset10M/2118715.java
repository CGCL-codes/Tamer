package com.dongsheng.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Date;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;
import com.dongsheng.manager.ReportManager;
import junit.framework.TestCase;

public class XMLTest extends TestCase {

    public void testXml() throws IOException, JDOMException {
        XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
        Document doc;
        StringReader read = new StringReader("<report><date name=\"year\"></date></report>");
        InputSource source = new InputSource(read);
        SAXBuilder builder = new SAXBuilder(false);
        if (new File("/home/ricky/workspace/Stock/stockhistory.xml").exists()) {
            doc = builder.build(source);
        } else {
            doc = new Document();
        }
        Element report = new Element("report");
        Element stockasc = new Element("stockasc");
        stockasc.setAttribute("date", "20110812");
        report.setContent(stockasc);
        doc.setContent(report);
        FileOutputStream out = new FileOutputStream("/home/ricky/workspace/Stock/stockhistory.xml");
        output.output(doc, out);
    }

    @SuppressWarnings("unchecked")
    public void testDate() throws JDOMException, IOException {
        Document doc;
        Element report = null;
        SAXBuilder builder = new SAXBuilder(false);
        doc = builder.build("/home/ricky/workspace/Stock/stockhistory.xml");
        report = doc.getRootElement();
        List<Element> dates = report.getChildren("date");
        for (Element ele : dates) {
            if (ele.getAttributeValue("value").equals("M2007-01")) {
                List<Element> catalogs = ele.getChildren("catalog");
                for (Element e : catalogs) {
                    if (e.getAttributeValue("value").equals("all") && e.getAttributeValue("sort").equals("asc")) {
                        List<Element> stockvos = e.getChildren("stockvo");
                        for (Element stockvo : stockvos) {
                        }
                    }
                }
            }
        }
    }

    public void testFile() {
        if (!new File("/home/ricky/data/").exists()) {
            try {
                new File("/home/ricky/data/").mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void testGenCurrent() throws JDOMException, IOException {
        ReportManager reportManager = new ReportManager();
        reportManager.generateStockCurrentPriceFile();
    }

    public void testJob() {
        try {
            URL url = new URL("http://rickysql.gotoip3.com/mapp/admin/admin.do");
            url.openConnection();
            url.getContent();
            System.out.print("00");
        } catch (Exception e) {
            System.out.print("Daily data update exception:" + e);
        }
    }
}
