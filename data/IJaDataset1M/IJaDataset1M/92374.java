package org.jaffa.modules.printing.services;

import java.io.File;
import java.util.Properties;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;
import org.jaffa.unittest.ContextWrapper;
import org.jaffa.unittest.UnitTestUtil;

/**
 *
 * @author  PaulE
 */
public class FormPrintEngineiTextTest extends TestCase {

    public static final String ITEXT_TEMPLATE_PDF = UnitTestUtil.getDataDirectory() + "/resources/templates/InvoiceTemplate.pdf";

    public static final String ITEXT_REFERENCE_PDF = UnitTestUtil.getDataDirectory() + "/resources/reference/itext-Invoice.pdf";

    public static final String ITEXT_OUTPUT_PDF = UnitTestUtil.getTempDirectory() + "/itext-Invoice.pdf";

    public static final long FILE_SIZE = 57485L;

    private static final Logger log = Logger.getLogger(FormPrintEngineiTextTest.class);

    /** Assembles and returns a test suite containing all known tests.
     * @return A test suite.
     */
    public static Test suite() {
        return new ContextWrapper(new TestSuite(FormPrintEngineiTextTest.class));
    }

    /** Creates new FormPrintServiceTest
     * @param name The name of the test case.
     */
    public FormPrintEngineiTextTest(String name) {
        super(name);
    }

    /** Make sure the engine is available */
    public void testCreateEngine() throws Exception {
        log.info("RUN: " + getName());
        try {
            IFormPrintEngine engine = FormPrintFactory.newInstance(FormPrintFactory.ENGINE_TYPE_ITEXT);
            assertNotNull("No Engine Instance was returned", engine);
        } catch (Exception e) {
            log.error("Test Failed - " + e.getLocalizedMessage(), e);
            throw e;
        }
    }

    /** Generate just the output file, but specify what the file name should be */
    public void testGenerateForm() throws Exception {
        log.info("RUN: " + getName());
        try {
            IFormPrintEngine engine = FormPrintFactory.newInstance(FormPrintFactory.ENGINE_TYPE_ITEXT);
            assertNotNull("No Engine Instance was returned", engine);
            String templateName = ITEXT_TEMPLATE_PDF;
            engine.setTemplateName(templateName);
            assertEquals(templateName, engine.getTemplateName());
            Properties documentProperties = new Properties();
            documentProperties.setProperty(IFormPrintEngine.DOCUMENT_PROPERTY_AUTHOR, "@author@");
            documentProperties.setProperty(IFormPrintEngine.DOCUMENT_PROPERTY_CREATOR, "@creator@");
            documentProperties.setProperty(IFormPrintEngine.DOCUMENT_PROPERTY_KEYWORDS, "@keywords@");
            documentProperties.setProperty(IFormPrintEngine.DOCUMENT_PROPERTY_SUBJECT, "@subject@");
            documentProperties.setProperty(IFormPrintEngine.DOCUMENT_PROPERTY_TITLE, "@title@");
            engine.setDocumentProperties(documentProperties);
            InvoiceDataBean dataSource = new InvoiceDataBean();
            dataSource.setOrderNo(InvoiceDataBean.VALID_ORDER_NO);
            dataSource.populate();
            engine.setDataSource(dataSource.getDocumentRoot());
            assertEquals(engine.getDataSource(), dataSource.getDocumentRoot());
            boolean canPrint = true, canCopy = true, canModify = true;
            engine.setPermissions(canPrint, canCopy, canModify);
            engine.generate();
            byte[] rawPdf = engine.getGeneratedForm();
            assertNotNull("No PDF was generated", rawPdf);
            log.debug("Generated a PDF document. Size=" + rawPdf.length);
            File tempOut = engine.writeForm();
            assertNotNull("writePdf() did not return a file", tempOut);
            assertTrue("writePdf() did not create the file", tempOut.exists());
            log.debug("Wrote File : " + tempOut.getAbsolutePath());
            File ref = new File(ITEXT_REFERENCE_PDF);
            assertTrue("Missing Reference file : " + ITEXT_REFERENCE_PDF, ref.exists());
            assertEquals("File not same size as reference file", ref.length(), tempOut.length());
            tempOut.delete();
            File out = engine.writeForm(new File(ITEXT_OUTPUT_PDF));
            assertNotNull("writePdf(File) did not return a file", out);
            assertTrue("writePdf(File) did not create the file", out.exists());
            log.debug("Wrote File : " + out.getAbsolutePath());
            assertEquals("File not same size as reference file", ref.length(), out.length());
            double similar = UnitTestUtil.arePdfsSimilar(ref, out);
            log.debug("Generated File match was " + similar);
            assertTrue("Generated File is not similar, manually review the delta", similar > 1.0);
            out.delete();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Test Failed - " + e.getLocalizedMessage(), e);
            throw e;
        }
    }
}
