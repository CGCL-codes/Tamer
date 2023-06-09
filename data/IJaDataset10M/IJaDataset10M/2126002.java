package org.pdfbox;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import org.pdfbox.exceptions.InvalidPasswordException;
import org.pdfbox.exceptions.COSVisitorException;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdfwriter.COSWriter;
import org.pdfbox.util.Splitter;

/**
 * This is the main program that will take a pdf document and split it into
 * a number of other documents.
 *
 * @author <a href="ben@benlitchfield.com">Ben Litchfield</a>
 * @version $Revision: 1.6 $
 */
public class PDFSplit {

    private static final String PASSWORD = "-password";

    private static final String SPLIT = "-split";

    /**
     * Infamous main method.
     *
     * @param args Command line arguments, should be one and a reference to a file.
     *
     * @throws Exception If there is an error parsing the document.
     */
    public static void main(String[] args) throws Exception {
        PDFSplit split = new PDFSplit();
        split.split(args);
    }

    private void split(String[] args) throws Exception {
        String password = "";
        String split = "1";
        Splitter splitter = new Splitter();
        String pdfFile = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(PASSWORD)) {
                i++;
                if (i >= args.length) {
                    usage();
                }
                password = args[i];
            } else if (args[i].equals(SPLIT)) {
                i++;
                if (i >= args.length) {
                    usage();
                }
                split = args[i];
            } else {
                if (pdfFile == null) {
                    pdfFile = args[i];
                }
            }
        }
        if (pdfFile == null) {
            usage();
        } else {
            InputStream input = null;
            PDDocument document = null;
            List documents = null;
            try {
                input = new FileInputStream(pdfFile);
                document = parseDocument(input);
                if (document.isEncrypted()) {
                    try {
                        document.decrypt(password);
                    } catch (InvalidPasswordException e) {
                        if (args.length == 4) {
                            System.err.println("Error: The supplied password is incorrect.");
                            System.exit(2);
                        } else {
                            System.err.println("Error: The document is encrypted.");
                            usage();
                        }
                    }
                }
                splitter.setSplitAtPage(Integer.parseInt(split));
                documents = splitter.split(document);
                for (int i = 0; i < documents.size(); i++) {
                    PDDocument doc = (PDDocument) documents.get(i);
                    String fileName = pdfFile.substring(0, pdfFile.length() - 4) + "-" + i + ".pdf";
                    writeDocument(doc, fileName);
                    doc.close();
                }
            } finally {
                if (input != null) {
                    input.close();
                }
                if (document != null) {
                    document.close();
                }
                for (int i = 0; documents != null && i < documents.size(); i++) {
                    PDDocument doc = (PDDocument) documents.get(i);
                    doc.close();
                }
            }
        }
    }

    private static final void writeDocument(PDDocument doc, String fileName) throws IOException, COSVisitorException {
        FileOutputStream output = null;
        COSWriter writer = null;
        try {
            output = new FileOutputStream(fileName);
            writer = new COSWriter(output);
            writer.write(doc);
        } finally {
            if (output != null) {
                output.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * This will parse a document.
     *
     * @param input The input stream for the document.
     *
     * @return The document.
     *
     * @throws IOException If there is an error parsing the document.
     */
    private static PDDocument parseDocument(InputStream input) throws IOException {
        PDFParser parser = new PDFParser(input);
        parser.parse();
        return parser.getPDDocument();
    }

    /**
     * This will print the usage requirements and exit.
     */
    private static void usage() {
        System.err.println("Usage: java org.pdfbox.PDFSplit [OPTIONS] <PDF file>\n" + "  -password  <password>        Password to decrypt document\n" + "  -split     <integer>         split after this many pages\n" + "  <PDF file>                   The PDF document to use\n");
        System.exit(1);
    }
}
