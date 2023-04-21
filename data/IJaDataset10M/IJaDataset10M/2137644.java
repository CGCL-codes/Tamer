package com.itextpdf.tool.xml.html;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import org.junit.Test;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * @author Balder
 *
 */
public class SpecialCharInPDFTest {

    public static final String RESOURCE_TEST_PATH = "./target/test-classes/";

    public static final String SNIPPETS = "/snippets/";

    private static final String TEST = "index_";

    static {
        Document.compress = false;
        LoggerFactory.getInstance().setLogger(new SysoLogger(3));
    }

    private final CssUtils utils = CssUtils.getInstance();

    @Test
    public void parseXfaOnlyXML() throws IOException {
        BufferedInputStream bis = new BufferedInputStream(SpecialCharInPDFTest.class.getResourceAsStream(SNIPPETS + TEST + "snippet.html"));
        final Document doc = new Document(PageSize.A4);
        float margin = utils.parseRelativeValue("10%", PageSize.A4.getWidth());
        doc.setMargins(margin, margin, margin, margin);
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(doc, new FileOutputStream(RESOURCE_TEST_PATH + TEST + "_charset.pdf"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        CssFilesImpl cssFiles = new CssFilesImpl();
        cssFiles.add(XMLWorkerHelper.getInstance().getDefaultCSS());
        StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
        HtmlPipelineContext hpc = new HtmlPipelineContext(null);
        hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(Tags.getHtmlTagProcessorFactory()).charSet(Charset.forName("ISO-8859-1"));
        Pipeline pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(hpc, new PdfWriterPipeline(doc, writer)));
        XMLWorker worker = new XMLWorker(pipeline, true);
        doc.open();
        XMLParser p = new XMLParser(true, worker);
        p.parse(new InputStreamReader(bis));
        doc.close();
    }
}
