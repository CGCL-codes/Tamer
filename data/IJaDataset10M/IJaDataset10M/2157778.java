package info.kmm.retriever.collector.handler;

import info.kmm.retriever.collector.handler.Document.DocumentType;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Date;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDDocumentInformation;
import org.pdfbox.util.PDFTextStripper;

/**
 * TODO Improve handler to retrieve the title
 * 
 * Extracts information from pdf files, filling and returning a
 * <code>info.iskmm.handler.entity.Document</code> object with the
 * extracted data.
 */
public class PdfContentHandler extends AbstractHandler {

    /**
     * Retrieves an <code>info.iskmm.handler.entity.Document</code> 
     * object, containing the content retrieved from a pdf.
     * 
     * @param urlConnection the url to the resource.
     * @return a <code>Document</code> object, with the content 
     *         retrieved using the url.
     */
    @Override
    public Object getContent(final URLConnection urlConnection) {
        try {
            final Document doc = new Document(urlConnection.getURL().toString(), DocumentType.pdf);
            this.fillDocumentWithData(urlConnection, doc);
            return doc;
        } catch (final Exception e) {
            super.getLogger().severe("Exception trying to read the content of the pdf " + urlConnection.getURL().toString() + "\nError message: " + e.getMessage() + "\nError cause: " + e.getCause());
            return null;
        }
    }

    private void fillDocumentWithData(final URLConnection urlConnection, final Document doc) throws IOException {
        PDDocument pdDoc = null;
        try {
            final PDFParser parser = new PDFParser(urlConnection.getInputStream());
            parser.parse();
            pdDoc = parser.getPDDocument();
            final PDDocumentInformation docInfo = pdDoc.getDocumentInformation();
            doc.setTitle(super.removeUndesiredPatternsFromText(docInfo.getTitle()));
            doc.addAuthor(super.removeUndesiredPatternsFromText(docInfo.getAuthor()), "");
            doc.setKeywords(super.removeUndesiredPatternsFromText(docInfo.getKeywords()));
            doc.setModificationDate(new Date(urlConnection.getLastModified()));
            try {
                doc.setCreationDate(docInfo.getCreationDate().getTime());
            } catch (final Exception e) {
            }
            doc.setLength(urlConnection.getContentLength());
            final PDFTextStripper stripper = new PDFTextStripper();
            stripper.setLineSeparator("\n");
            doc.setContent(super.removeUndesiredPatternsFromText(stripper.getText(pdDoc)));
        } finally {
            if (pdDoc != null) pdDoc.close();
            super.closeStreamInsideUrlConnection(urlConnection);
        }
    }
}
