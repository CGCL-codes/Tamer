package com.extremelogic.jtextmarker;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.xml.SAXmyHandler;
import com.lowagie.text.xml.TagMap;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author Jan So
 */
public class PdfProcessor {

    /**
     * Default page size.
     */
    private Rectangle pageSize = Constants.DEFAULT_PAGE_SIZE;

    private ByteArrayOutputStream byteStream;

    private Document document;

    private TagMap tagmap;

    private String tagmapData;

    String xmlTemplate;

    private Map<String, Object> data;

    /**
     * Flag to determine if PDF was generated.
     */
    private boolean isGenerated;

    /**
     * No param instance. Used if PDF is to be created as a byte stream.
     *
     * @throws TextMarkerException
     */
    public PdfProcessor() throws TextMarkerException {
        document = new Document(pageSize, Constants.DEFAULT_LEFT_MARGIN, Constants.DEFAULT_RIGHT_MARGIN, Constants.DEFAULT_TOP_MARGIN, Constants.DEFAULT_BOTTOM_MARGIN);
        byteStream = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, byteStream);
        } catch (DocumentException e) {
            byteStream.reset();
            throw new TextMarkerException(Constants.MODULE_NAME + e.getLocalizedMessage());
        }
    }

    /**
     * @param fileOutput
     *            - PDF output. Include PDF file extension.
     * @param ftlTemplate
     *            - Template name without the extension. Don't include the path.
     * @param templateDir
     *            - Path where the template is located.
     * @param tagmap
     *            - Tag map file.
     * @param data
     *            - Data to be printed on the PDF.
     * @param ev
     *            - Additional events. For now adding of water mark.
     * @throws TextMarkerException
     */
    public PdfProcessor(String fileOutput, String ftlTemplate, String templateDir, String tagmap, Map<String, Object> data, PdfPageEventHelper ev) throws TextMarkerException {
        this.data = data;
        document = new Document(pageSize, Constants.DEFAULT_LEFT_MARGIN, Constants.DEFAULT_RIGHT_MARGIN, Constants.DEFAULT_TOP_MARGIN, Constants.DEFAULT_BOTTOM_MARGIN);
        PdfWriter writer = null;
        tagmapData = tagmap;
        try {
            xmlTemplate = createTemplate(ftlTemplate, templateDir);
            writer = PdfWriter.getInstance(document, new FileOutputStream(fileOutput));
        } catch (Exception e) {
            String msg = Constants.MODULE_NAME + e.getLocalizedMessage();
            throw new TextMarkerException(msg);
        }
        if (null != ev) {
            writer.setPageEvent(ev);
        }
    }

    /**
     * @return PDF as a byte data
     * @throws DocumentException
     */
    public byte[] getBytes() throws DocumentException {
        ByteArrayOutputStream s = byteStream;
        if (s.size() < 1) {
            throw new DocumentException(Constants.MODULE_NAME + "Invalid size. Current document size is " + s.size() + " bytes.");
        }
        return s.toByteArray();
    }

    /**
     * @param ftlTemplate
     * @return
     * @throws TextMarkerException
     */
    private String createTemplate(String ftlTemplate, String templateDir) throws TextMarkerException {
        String template = ftlTemplate;
        Configuration cfg = new Configuration();
        try {
            Writer writer = null;
            File file = new File(template + ".xml");
            writer = new BufferedWriter(new FileWriter(file));
            cfg.setDirectoryForTemplateLoading(new File(templateDir));
            Template t = cfg.getTemplate(ftlTemplate + ".ftl");
            if (null != data) {
                t.process(data, writer);
            }
            writer.flush();
        } catch (Exception e) {
            String msg = Constants.MODULE_NAME + " createTemplate(" + ftlTemplate + ") " + e.getLocalizedMessage();
            throw new TextMarkerException(msg);
        }
        return template + ".xml";
    }

    /**
     * @return true if the pdf was sucessfully generated
     */
    public boolean isGenerated() {
        return isGenerated;
    }

    public void setTagMap(TagMap tm) {
        tagmap = tm;
    }

    /**
     * @param template
     * @param tagmap
     * @throws TextMarkerException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public void generatePdf() throws TextMarkerException {
        SAXParser parser;
        try {
            if (null == tagmap && null != tagmapData) {
                this.tagmap = new TagMap(tagmapData);
            }
            parser = SAXParserFactory.newInstance().newSAXParser();
            SAXmyHandler handler = new SAXmyHandler(document, tagmap);
            parser.parse(xmlTemplate, handler);
            document.close();
            isGenerated = true;
        } catch (Exception e) {
            String msg = Constants.MODULE_NAME + e.getLocalizedMessage();
            e.printStackTrace();
            throw new TextMarkerException(msg);
        }
    }
}
