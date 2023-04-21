package org.pdfbox.examples.pdmodel;

import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDPage;
import org.pdfbox.pdmodel.common.PDRectangle;
import org.pdfbox.pdmodel.interactive.annotation.PDAnnotationRubberStamp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an example on how to add annotations to pages of a PDF document.
 *
 * @author Paul King
 * @version $Revision: 1.1 $
 */
public class RubberStamp {

    private RubberStamp() {
    }

    /**
     * This will print the documents data.
     *
     * @param args The command line arguments.
     *
     * @throws Exception If there is an error parsing the document.
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            usage();
        } else {
            PDDocument document = null;
            try {
                document = PDDocument.load(args[0]);
                if (document.isEncrypted()) {
                    throw new IOException("Encrypted documents are not supported for this example");
                }
                List allpages = new ArrayList();
                document.getDocumentCatalog().getPages().getAllKids(allpages);
                for (int i = 0; i < allpages.size(); i++) {
                    PDPage apage = (PDPage) allpages.get(i);
                    List annotations = apage.getAnnotations();
                    PDAnnotationRubberStamp rs = new PDAnnotationRubberStamp();
                    rs.setName(PDAnnotationRubberStamp.NAME_TOP_SECRET);
                    rs.setRectangle(new PDRectangle(100, 100));
                    rs.setContents("A top secret note");
                    annotations.add(rs);
                }
                document.save(args[1]);
            } finally {
                if (document != null) {
                    document.close();
                }
            }
        }
    }

    /**
     * This will print the usage for this document.
     */
    private static void usage() {
        System.err.println("Usage: java org.pdfbox.examples.pdmodel.RubberStamp <input-pdf> <output-pdf>");
    }
}
