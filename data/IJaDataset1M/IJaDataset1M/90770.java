package edu.ucsd.ncmir.spl.utilities;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

/**
 *
 * @author spl
 */
public class Print {

    public static void print(String header, String[] text, Font font) throws PrinterException {
        PageFormat page_format = new PageFormat();
        PrinterJob printer_job = PrinterJob.getPrinterJob();
        Printer printer = new Printer(header, text, font);
        printer_job.setPrintable(printer, page_format);
        if (printer_job.printDialog()) printer_job.print();
    }

    private static class Printer implements Printable {

        private String _header;

        private String[] _text;

        private Font _font;

        Printer(String header, String[] text, Font font) {
            this._header = header;
            this._text = text;
            this._font = font;
        }

        public int print(Graphics graphics, PageFormat page_format, int page_index) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.translate(page_format.getImageableX(), page_format.getImageableY());
            graphics2D.setFont(this._font);
            FontMetrics font_metrics = graphics2D.getFontMetrics(this._font);
            int height = font_metrics.getAscent();
            int page_height = graphics2D.getClipBounds().height;
            int header_count;
            String[] headers = null;
            if (this._header != null) {
                headers = this._header.split("\n");
                header_count = headers.length;
            } else header_count = 0;
            int line = ((page_height / height) - header_count) * page_index;
            int status;
            if (line < _text.length) {
                int position = 0;
                for (int i = 0; i < header_count; i++) graphics2D.drawString(headers[i], 0, position += height);
                while ((line < _text.length) && ((position += height) < page_height)) graphics2D.drawString(_text[line++], 0, position);
                status = Printable.PAGE_EXISTS;
            } else status = Printable.NO_SUCH_PAGE;
            return status;
        }
    }
}
