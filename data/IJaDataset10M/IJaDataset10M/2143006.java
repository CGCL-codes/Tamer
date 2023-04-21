package be.kuleuven.cs.samgi.extractor.propertyExtractors;

import org.apache.log4j.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfName;

/**
 * Class that can deal with the properties that are maintained in an PDF file
 *
 * @author Michael Meire
 * @version 1.0 beta
 * @since Oct 12, 2005
 */
public class PDFPropertyExtractorG extends PropertyExtractorG {

    public static final String AUTHOR = "author";

    public static final String CREATIONDATE = "creationDate";

    public static final String KEYWORDS = "keywords";

    public static final String MODIFICATIONDATE = "modificationDate";

    public static final String PRODUCER = "producer";

    public static final String SUBJECT = "subject";

    public static final String TITLE = "title";

    public static final String TRAPPED = "trapped";

    public static final String PAGECOUNT = "pageCount";

    public static final String[] keys = new String[] { "author", "creationDate", "keywords", "modificationDate", "producer", "subject", "title", "trapped", "pageCount" };

    public static final java.text.SimpleDateFormat pdfDateParser = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * Extract the properties of a given PDF file
     *
     * @param lo The PDF datahandler from which we want to extract the properties
     * @return The properties of the given PDF datahandler
     */
    public Properties extractProperties(DataHandler lo) {
        setDocumentProperties(null);
        InputStream iStr = null;
        PdfReader r = null;
        try {
            iStr = lo.getInputStream();
            r = new PdfReader(iStr);
            HashMap hashInfo = r.getInfo();
            setDocumentProperties(new Properties());
            try {
                getDocumentProperties().setProperty(AUTHOR, (String) hashInfo.get(PdfName.AUTHOR.toString().substring(1)));
            } catch (Exception e) {
            }
            try {
                String creationDate = (String) hashInfo.get(PdfName.CREATIONDATE.toString().substring(1));
                creationDate = creationDate.substring(2, 15);
                getDocumentProperties().setProperty(CREATIONDATE, pdfDateParser.parse(creationDate).toString());
            } catch (Exception e) {
            }
            try {
                getDocumentProperties().setProperty(KEYWORDS, (String) hashInfo.get(PdfName.KEYWORDS.toString().substring(1)));
            } catch (Exception e) {
            }
            try {
                String modificationDate = (String) hashInfo.get(PdfName.MODDATE.toString().substring(1));
                modificationDate = modificationDate.substring(2, 15);
                getDocumentProperties().setProperty(MODIFICATIONDATE, pdfDateParser.parse(modificationDate).toString());
            } catch (Exception e) {
            }
            try {
                getDocumentProperties().setProperty(PRODUCER, (String) hashInfo.get(PdfName.PRODUCER.toString().substring(1)));
            } catch (Exception e) {
            }
            try {
                getDocumentProperties().setProperty(SUBJECT, (String) hashInfo.get(PdfName.SUBJECT.toString().substring(1)));
            } catch (Exception e) {
            }
            try {
                getDocumentProperties().setProperty(TITLE, (String) hashInfo.get(PdfName.TITLE.toString().substring(1)));
            } catch (Exception e) {
            }
            try {
                getDocumentProperties().setProperty(TRAPPED, (String) hashInfo.get(PdfName.TRAPPED.toString().substring(1)));
            } catch (Exception e) {
            }
            try {
                getDocumentProperties().setProperty(PAGECOUNT, String.valueOf(r.getNumberOfPages()));
            } catch (Exception e) {
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                r.close();
                iStr.close();
            } catch (IOException e) {
            }
        }
        return getDocumentProperties();
    }

    /**
     * Main method to run some tests on the class
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        PDFPropertyExtractorG pdfpe = new PDFPropertyExtractorG();
        pdfpe.testSomeDocuments();
    }

    /**
     * Test for getting the first line of text from some test PDF documents
     */
    private void testSomeDocuments() {
        DataHandler dh;
        String fileNames[] = new String[] { "/home/michael/doctoreren/problemDocuments/seccion1m.pdf" };
        for (int i = 0; i < fileNames.length; i++) {
            String fileName = fileNames[i];
            System.out.println("fileName = " + fileName);
            dh = new DataHandler(new javax.activation.FileDataSource(fileName));
            System.out.println("----------------------------->  extractProperties(dh) = " + extractProperties(dh) + "------------");
        }
    }

    @Override
    public Properties extractProperties(String lo) {
        try {
            return extractProperties(new DataHandler(new FileDataSource(lo)));
        } catch (Exception e) {
        }
        return null;
    }
}
