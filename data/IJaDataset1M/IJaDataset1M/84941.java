package be.bzbit.framework.pdf.render;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFRenderer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

/**
 *
 * @author jlust
 */
public class NonScalingPdfPrintPage implements Printable {

    private final PDFFile file;

    public NonScalingPdfPrintPage(PDFFile file) {
        this.file = file;
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        int pagenum = pageIndex + 1;
        if (pagenum >= 1 && pagenum <= file.getNumPages()) {
            Graphics2D g2 = (Graphics2D) graphics;
            PDFPage page = file.getPage(pagenum);
            Rectangle imgbounds = new Rectangle(0, 0, (int) page.getWidth(), (int) page.getHeight());
            PDFRenderer pgs = new PDFRenderer(page, g2, imgbounds, null, null);
            try {
                page.waitForFinish();
                pgs.run();
            } catch (InterruptedException ie) {
            }
            return PAGE_EXISTS;
        } else {
            return NO_SUCH_PAGE;
        }
    }
}
