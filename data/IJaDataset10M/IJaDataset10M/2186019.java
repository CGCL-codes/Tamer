package part3.chapter10;

import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfShading;
import com.itextpdf.text.pdf.PdfShadingPattern;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.ShadingColor;

public class ShadingPatternColor extends DeviceColor {

    /** The resulting PDF file. */
    public static final String RESULT = "results/part3/chapter10/shading_pattern.pdf";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        PdfContentByte canvas = writer.getDirectContent();
        PdfShading axial = PdfShading.simpleAxial(writer, 36, 716, 396, 788, BaseColor.ORANGE, BaseColor.BLUE);
        canvas.paintShading(axial);
        document.newPage();
        PdfShading radial = PdfShading.simpleRadial(writer, 200, 700, 50, 300, 700, 100, new BaseColor(0xFF, 0xF7, 0x94), new BaseColor(0xF7, 0x8A, 0x6B), false, false);
        canvas.paintShading(radial);
        PdfShadingPattern shading = new PdfShadingPattern(axial);
        colorRectangle(canvas, new ShadingColor(shading), 150, 420, 126, 126);
        canvas.setShadingFill(shading);
        canvas.rectangle(300, 420, 126, 126);
        canvas.fillStroke();
        document.close();
    }

    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new ShadingPatternColor().createPdf(RESULT);
    }
}
