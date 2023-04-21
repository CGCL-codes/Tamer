package org.wizard4j.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wizard4j.flowchart.Flowchart;
import org.wizard4j.WizardFactory;
import org.wizard4j.util.XmlFormatter;
import org.wizard4j.validation.WizardValidator;
import org.wizard4j.wrapper.HtmlWrapper;
import org.xml.sax.InputSource;
import junit.framework.TestCase;

public class TestHtmlWrapper extends TestCase {

    private static Logger logger = LoggerFactory.getLogger(TestHtmlWrapper.class);

    private WizardValidator wv;

    protected void setUp() {
        wv = WizardFactory.newWizardValidator();
    }

    public void testNonsenseExample() {
        logger.info("Start testNonsenseExample");
        String xmlFileName = "file:test/flowchart.nonsense.xml";
        try {
            Flowchart flowchart = wv.validateFlowchart(new InputSource(xmlFileName));
            HtmlWrapper wrapper = new HtmlWrapper(10, "", true);
            String sessionId = wrapper.createManagedSession(flowchart);
            String ticket = wrapper.first(sessionId);
            String htmlFragment = wrapper.getHtml(ticket);
            String docType = "<!DOCTYPE HTML PUBLIC  \"-//W3C//DTD HTML 4.01 //EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";
            StringBuffer buffer = new StringBuffer(1000);
            buffer.append(docType);
            buffer.append("<html><head><title></title></head><body>\n");
            buffer.append(htmlFragment);
            buffer.append("\n</body>");
            logger.info("\n {}", buffer.toString());
            String resultString = XmlFormatter.documentToString(W3CValidator.validateContentViaGet(buffer.toString()));
            logger.info("\n {}", resultString);
            int beginIndex = resultString.indexOf("<errors>") + 8;
            int endIndex = resultString.indexOf("</errors>");
            String nbrOfErrors = resultString.substring(beginIndex, endIndex);
            logger.info(nbrOfErrors);
            assertEquals("0", nbrOfErrors);
        } catch (Exception e) {
            logger.error("", e);
            fail();
        }
    }
}
