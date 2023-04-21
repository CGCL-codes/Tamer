package questions.stamppages;

import java.io.FileOutputStream;
import java.io.IOException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

public class RemoveAttachmentAnnotations {

    public static final String RESOURCE = "results/questions/stamppages/with_attachments.pdf";

    public static final String RESULT = "results/questions/stamppages/without_attachments.pdf";

    public static final String TXT = "resources/questions/txt/caesar.txt";

    public static final String IMG = "resources/questions/img/1t3xt.gif";

    public static void main(String[] args) throws IOException, DocumentException {
        createPdfWithAttachments();
        PdfReader reader = new PdfReader(RESOURCE);
        PdfDictionary page;
        PdfDictionary annotation;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            page = reader.getPageN(i);
            PdfArray annots = page.getAsArray(PdfName.ANNOTS);
            if (annots != null) {
                for (int j = annots.size() - 1; j >= 0; j--) {
                    annotation = annots.getAsDict(j);
                    if (PdfName.FILEATTACHMENT.equals(annotation.get(PdfName.SUBTYPE))) {
                        annots.remove(j);
                    }
                }
            }
        }
        reader.removeUnusedObjects();
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT));
        stamper.close();
    }

    public static void createPdfWithAttachments() throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESOURCE));
        document.open();
        document.add(new Paragraph("page 1"));
        writer.addAnnotation(PdfAnnotation.createFileAttachment(writer, new Rectangle(100f, 780f, 120f, 800f), "This is some text", "some text".getBytes(), null, "some.txt"));
        writer.addAnnotation(PdfAnnotation.createText(writer, new Rectangle(100f, 750f, 120f, 770f), "Help", "This Help annotation was made with 'createText'", false, "Help"));
        document.newPage();
        document.add(new Paragraph("page 2"));
        document.newPage();
        document.add(new Paragraph("page 3"));
        PdfFileSpecification fs1 = PdfFileSpecification.fileEmbedded(writer, TXT, "caesar.txt", null);
        writer.addAnnotation(PdfAnnotation.createFileAttachment(writer, new Rectangle(100f, 780f, 120f, 800f), "Caesar", fs1));
        PdfFileSpecification fs2 = PdfFileSpecification.fileEmbedded(writer, IMG, "1t3xt.gif", null);
        writer.addAnnotation(PdfAnnotation.createFileAttachment(writer, new Rectangle(100f, 750f, 120f, 770f), "1t3xt", fs2));
        document.close();
    }
}
