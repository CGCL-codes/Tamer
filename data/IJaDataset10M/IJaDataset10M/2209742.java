package jsesh.graphicExport.pdfExport;

import java.awt.Font;
import java.awt.Graphics2D;
import java.io.OutputStream;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * utility class representing a pdf document.
 * 
 * @author rosmord
 * 
 */
public class PDFDocumentWriterAux {

    private PDFExportPreferences pdfExportPreferences;

    private Document document;

    private PdfWriter pdfWriter;

    private Font font;

    public PDFDocumentWriterAux(PDFExportPreferences prefs, OutputStream out, float docWidth, float docHeight, String comment) {
        this.pdfExportPreferences = prefs;
        document = new Document();
        DefaultFontMapper fontMapper = new DefaultFontMapper();
        fontMapper.putName("MDCTranslitLC", new DefaultFontMapper.BaseFontParameters("/jseshResources/fonts/MDCTranslitLC.ttf"));
        document.setPageSize(new Rectangle(docWidth, docHeight));
        try {
            pdfWriter = PdfWriter.getInstance(document, out);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        setHeader(comment);
    }

    private void setHeader(String comment) {
        document.addTitle(pdfExportPreferences.getTitle());
        document.addAuthor(pdfExportPreferences.getAuthor());
        document.addKeywords(pdfExportPreferences.getKeywords());
        document.addCreator("JSesh");
        document.addSubject(comment);
    }

    public PdfWriter getPdfWriter() {
        return pdfWriter;
    }

    public Document getDocument() {
        return document;
    }

    public void open() {
        document.open();
    }

    public void close() {
        document.close();
    }

    public Graphics2D createGraphics(float width, float height) {
        return pdfWriter.getDirectContent().createGraphicsShapes(width, height);
    }

    public Font getFont() {
        return font;
    }
}
