package net.sf.reqbook.services.format;

import net.sf.reqbook.services.css.InvalidCSSException;
import net.sf.reqbook.services.InvalidOutputPathException;
import net.sf.reqbook.services.InvalidInputFileException;
import net.sf.reqbook.services.URIConstants;
import net.sf.reqbook.services.css.CSSProcessor;
import net.sf.reqbook.services.progress.ProgressCollector;
import net.sf.reqbook.services.progress.PhaseProgressReporter;
import net.sf.reqbook.services.parser.InputFileIOException;
import net.sf.reqbook.services.io.ExternalFilesCopier;
import net.sf.reqbook.common.InternalErrorException;
import net.sf.reqbook.TestUtil;
import org.xml.sax.InputSource;
import org.jmock.Mock;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.File;

/**
 * $Id: HTMLTransformerImplTest.java,v 1.9 2006/02/21 10:59:19 poma Exp $
 *
 * @author Pavel Sher
 */
public class HTMLTransformerImplTest extends TransformerTestCase {

    private Mock filesCopier;

    private Mock cssProcessor;

    protected void setUp() throws Exception {
        super.setUp();
        filesCopier = mock(ExternalFilesCopier.class);
        cssProcessor = mock(CSSProcessor.class);
    }

    public void testPreparePipes() throws InternalErrorException {
        expectPrepareCommonHandlers();
        expectPrepareDocbookTransformer("/reqbook/xsl/html/docbook-chunked");
        expectPrepareDocbookTransformer("/reqbook/xsl/html/docbook-single");
        stubsHandlersBinding();
        HTMLTransformerImpl tr = new HTMLTransformerImpl();
        setupTransformer(tr);
        tr.preparePipes();
    }

    public void testInvalidInputFile() throws IOException, InternalErrorException, InvalidCSSException, InvalidOutputPathException, InputFileIOException {
        String output = TestUtil.prepareTestCaseTmpDir(getName()).getAbsolutePath();
        String emptyFile = new File(TestUtil.filesDir(), "spec.xml").getAbsolutePath();
        messages.expects(once()).method("format").with(eq("inputFileDoesNotExist"), eq(new File("input.xml").getAbsolutePath()));
        messages.expects(once()).method("format").with(eq("inputFileEmpty"), eq(emptyFile));
        HTMLTransformerImpl tr = new HTMLTransformerImpl();
        setupTransformer(tr);
        try {
            tr.transform(new HTMLFormat(true), "input.xml", output);
            fail("An exception expected");
        } catch (InvalidInputFileException e) {
        }
        try {
            tr.transform(new HTMLFormat(true), emptyFile, output);
            fail("An exception expected");
        } catch (InvalidInputFileException e) {
        }
    }

    public void testTransform_ChunkedHTML_NoCustomCSS() throws InternalErrorException, IOException, InvalidCSSException, InvalidOutputPathException, InputFileIOException, InvalidInputFileException {
        expectPrepareCommonHandlers();
        expectPrepareDocbookTransformer("/reqbook/xsl/html/docbook-chunked");
        stubsHandlersBinding();
        String input = new File(TestUtil.filesDir(), "simple.xml").getAbsolutePath();
        String output = TestUtil.prepareTestCaseTmpDir(getName()).getAbsolutePath();
        String cssPath = new File("css/reqbook.css").getAbsolutePath();
        cssProcessor.expects(once()).method("copyMainCSS").with(eq(cssPath), eq(new File(output))).will(returnValue("_css/reqbook.css"));
        prepareConfigurationExpectations(input, output, output + File.separatorChar, "_css/reqbook.css");
        HTMLTransformerImpl tr = new HTMLTransformerImpl();
        setupTransformer(tr);
        tr.transform(new HTMLFormat(true), input, output);
    }

    public void testTransform_SingleHTML_NoCustomCSS() throws InternalErrorException, IOException, InvalidCSSException, InvalidOutputPathException, InputFileIOException, InvalidInputFileException {
        expectPrepareCommonHandlers();
        expectPrepareDocbookTransformer("/reqbook/xsl/html/docbook-single");
        stubsHandlersBinding();
        String input = new File(TestUtil.filesDir(), "simple.xml").getAbsolutePath();
        String output = TestUtil.prepareTestCaseTmpDir(getName()).getAbsolutePath();
        String outputPathParam = new File(output, "simple.html").getAbsolutePath();
        String cssPath = new File("css/reqbook.css").getAbsolutePath();
        cssProcessor.expects(once()).method("copyMainCSS").with(eq(cssPath), eq(new File(output))).will(returnValue("_css/reqbook.css"));
        prepareConfigurationExpectations(input, output, outputPathParam, "_css/reqbook.css");
        HTMLTransformerImpl tr = new HTMLTransformerImpl();
        setupTransformer(tr);
        tr.transform(new HTMLFormat(false), input, output);
    }

    public void testTransform_WithCustomCSS() throws InternalErrorException, IOException, InvalidCSSException, InvalidOutputPathException, InputFileIOException, InvalidInputFileException {
        expectPrepareCommonHandlers();
        expectPrepareDocbookTransformer("/reqbook/xsl/html/docbook-single");
        stubsHandlersBinding();
        String input = new File(TestUtil.filesDir(), "simple.xml").getAbsolutePath();
        String output = TestUtil.prepareTestCaseTmpDir(getName()).getAbsolutePath();
        String outputPathParam = new File(output, "simple.html").getAbsolutePath();
        String cssPath = new File(TestUtil.filesDir(), "testcss/custom.css").getAbsolutePath();
        cssProcessor.expects(once()).method("copyMainCSS").with(eq(cssPath), eq(new File(output))).will(returnValue("_css/custom.css"));
        prepareConfigurationExpectations(input, output, outputPathParam, "_css/custom.css");
        HTMLTransformerImpl tr = new HTMLTransformerImpl();
        setupTransformer(tr);
        tr.transform(new HTMLFormat(false, cssPath), input, output);
    }

    public void testTransform_ChunkedHTML_WithProgressCollector() throws InternalErrorException, IOException, InvalidCSSException, InvalidOutputPathException, InputFileIOException, InvalidInputFileException {
        Mock progressCollector = mock(ProgressCollector.class);
        Mock validationReporter = mock(PhaseProgressReporter.class);
        Mock reqbookReporter = mock(PhaseProgressReporter.class);
        Mock docbookReporter = mock(PhaseProgressReporter.class);
        progressCollector.expects(once()).method("addPhase").with(eq(4)).will(returnValue(validationReporter.proxy()));
        progressCollector.expects(once()).method("addPhase").with(eq(10)).will(returnValue(reqbookReporter.proxy()));
        progressCollector.expects(once()).method("addPhase").with(eq(86)).will(returnValue(docbookReporter.proxy()));
        File input = new File(TestUtil.filesDir(), "simple.xml");
        validationReporter.expects(once()).method("setCounterMaximum").with(eq((int) input.length()));
        reqbookTransformerHandler.expects(once()).method("setParameter").with(eq(TransformUtil.PHASE_PROGRESS_PARAM), eq(reqbookReporter.proxy()));
        docbookTransformerHandler.expects(once()).method("setParameter").with(eq(TransformUtil.PHASE_PROGRESS_PARAM), eq(docbookReporter.proxy()));
        expectPrepareCommonHandlers();
        expectPrepareDocbookTransformer("/reqbook/xsl/html/docbook-chunked");
        stubsHandlersBinding();
        String output = TestUtil.prepareTestCaseTmpDir(getName()).getAbsolutePath();
        String cssPath = new File("css/reqbook.css").getAbsolutePath();
        cssProcessor.expects(once()).method("copyMainCSS").with(eq(cssPath), eq(new File(output))).will(returnValue("_css/reqbook.css"));
        prepareConfigurationExpectations(input.getAbsolutePath(), output, output + File.separatorChar, "_css/reqbook.css");
        HTMLTransformerImpl tr = new HTMLTransformerImpl();
        setupTransformer(tr);
        tr.setProgressCollector((ProgressCollector) progressCollector.proxy());
        tr.transform(new HTMLFormat(true), input.getAbsolutePath(), output);
    }

    private void prepareConfigurationExpectations(String input, String output, String outputPathParam, String cssPathParam) {
        expectSetOutputDirParam(output);
        docbookTransformerHandler.expects(once()).method("clearParameters");
        docbookTransformerHandler.expects(once()).method("setParameter").with(eq(HTMLTransformerImpl.OUTPUT_PATH_PARAM), eq(outputPathParam));
        docbookTransformerHandler.expects(once()).method("setParameter").with(eq(HTMLTransformerImpl.CSS_FILE_PATH_PARAM), eq(cssPathParam));
        filesCopier.expects(once()).method("setOutputPath").with(eq(output));
        expectParsing(input);
    }

    protected void setupTransformer(HTMLTransformerImpl tr) {
        super.setupTransformer(tr);
        tr.setFilesCopier((ExternalFilesCopier) filesCopier.proxy());
        tr.setCssProcessor((CSSProcessor) cssProcessor.proxy());
    }

    private void expectPrepareCommonHandlers() {
        expectPrepareValidator();
        expectPrepareReqbookTransformer("/reqbook/xsl/html");
    }

    protected void expectPrepareReqbookTransformer(String systemId) {
        StreamSource reqbookStylesheet = new StreamSource();
        resolver.expects(atLeastOnce()).method("resolveSource").with(eq(systemId)).will(returnValue(reqbookStylesheet));
        reqbookTransformer.expects(atLeastOnce()).method("setStylesheet").with(same(reqbookStylesheet));
        reqbookTransformer.expects(atLeastOnce()).method("createPipeHandler").will(returnValue(reqbookTransformerHandler.proxy()));
    }

    protected void expectPrepareValidator() {
        InputSource schema = new InputSource();
        resolver.expects(atLeastOnce()).method("resolveInputSource").with(eq(URIConstants.REQBOOK_SCHEMA_URI)).will(returnValue(schema));
        rngValidator.expects(atLeastOnce()).method("setSchema").with(same(schema));
        rngValidator.expects(atLeastOnce()).method("createPipeHandler").will(returnValue(rngValidatorHandler.proxy()));
    }

    protected void stubsHandlersBinding() {
        super.stubsHandlersBinding();
        filesCopier.stubs().method("setOutputHandler");
    }

    protected void expectSetOutputDirParam(String output) {
        reqbookTransformerHandler.expects(once()).method("setParameter").with(eq(HTMLTransformerImpl.OUTPUT_DIR_PARAM), eq(output + File.separator));
    }
}
