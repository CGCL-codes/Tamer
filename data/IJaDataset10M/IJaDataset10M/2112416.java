package com.itextpdf.tool.xml;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

/**
 *
 */
public class BugRunnerTest {

    public static final String RESOURCE_TEST_PATH = "./target/test-classes";

    private final List<String> list = new ArrayList<String>();

    static {
        LoggerFactory.getInstance().setLogger(new SysoLogger(3));
    }

    @Before
    public void setup() {
        list.add("3353957.html");
        list.add("ol-test.html");
        list.add("processing-instructions.html");
    }

    @Test
    public void runBugSamples() throws IOException {
        boolean success = true;
        for (String str : list) {
            try {
                System.out.println(str);
                final Document doc = new Document();
                PdfWriter writer = null;
                try {
                    writer = PdfWriter.getInstance(doc, new FileOutputStream(RESOURCE_TEST_PATH + "/bugs/" + str + ".pdf"));
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                doc.open();
                BufferedInputStream bis = new BufferedInputStream(BugRunnerTest.class.getResourceAsStream("/bugs/" + str));
                XMLWorkerHelper helper = XMLWorkerHelper.getInstance();
                helper.parseXHtml(writer, doc, new InputStreamReader(bis));
                doc.close();
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
                success = false;
            }
        }
        if (!success) {
            Assert.fail();
        }
    }
}
