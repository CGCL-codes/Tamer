package printer;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.print.*;

public class Example3 {

    private static final int POINTS_PER_INCH = 72;

    /**
      * Constructor: Example3 <p>
      *
      */
    public Example3() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        Book book = new Book();
        book.append(new IntroPage(), printJob.defaultPage());
        PageFormat documentPageFormat = new PageFormat();
        documentPageFormat.setOrientation(PageFormat.LANDSCAPE);
        book.append(new Document(), documentPageFormat);
        book.append(new Document(), documentPageFormat);
        printJob.setPageable(book);
        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (Exception PrintException) {
                PrintException.printStackTrace();
            }
        }
    }

    /**
      * Class: IntroPage <p>
      *
      * This class defines the painter for the cover page by implementing the
      * Printable interface. <p>
      *
      * @author Jean-Pierre Dube <jpdube@videotron.ca>
      * @version 1.0
      * @since 1.0
      * @see Printable
      */
    private class IntroPage implements Printable {

        /**
        */
        public int print(Graphics g, PageFormat pageFormat, int page) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            g2d.setPaint(Color.black);
            Rectangle2D.Double border = new Rectangle2D.Double(0, 0, pageFormat.getImageableWidth(), pageFormat.getImageableHeight());
            g2d.draw(border);
            String titleText = "Printing in Java Part 2";
            Font titleFont = new Font("helvetica", Font.BOLD, 36);
            g2d.setFont(titleFont);
            FontMetrics fontMetrics = g2d.getFontMetrics();
            double titleX = (pageFormat.getImageableWidth() / 2) - (fontMetrics.stringWidth(titleText) / 2);
            double titleY = 3 * POINTS_PER_INCH;
            g2d.drawString(titleText, (int) titleX, (int) titleY);
            return (PAGE_EXISTS);
        }
    }

    /**
     * Class: Document <p>
    *
    * This class is the painter for the document content.<p>
    *
    */
    private class Document implements Printable {

        /**
        * Method: print <p>
      */
        public int print(Graphics g, PageFormat pageFormat, int page) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            g2d.setPaint(Color.black);
            g2d.setStroke(new BasicStroke(12));
            Rectangle2D.Double border = new Rectangle2D.Double(0, 0, pageFormat.getImageableWidth(), pageFormat.getImageableHeight());
            g2d.draw(border);
            if (page == 1) {
                g2d.drawString("This the content page of page: " + page, POINTS_PER_INCH, POINTS_PER_INCH);
                return (PAGE_EXISTS);
            } else if (page == 2) {
                g2d.drawString("This the content of the second page: " + page, POINTS_PER_INCH, POINTS_PER_INCH);
                return (PAGE_EXISTS);
            }
            return (NO_SUCH_PAGE);
        }
    }

    public static void main(String argv[]) {
        new Example3();
    }
}
