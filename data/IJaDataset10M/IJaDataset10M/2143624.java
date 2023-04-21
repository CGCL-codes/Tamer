package questions.tables;

import java.awt.Color;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPTableEvent;

public class AlternateBackground implements PdfPTableEvent {

    public void tableLayout(PdfPTable table, float[][] widths, float[] heights, int headerRows, int rowStart, PdfContentByte[] canvases) {
        int rows = widths.length;
        int columns;
        Rectangle rect;
        for (int row = headerRows + 1; row < rows; row += 2) {
            columns = widths[row].length - 1;
            rect = new Rectangle(widths[row][0], heights[row], widths[row][columns], heights[row + 1]);
            rect.setBackgroundColor(Color.YELLOW);
            rect.setBorder(Rectangle.NO_BORDER);
            canvases[PdfPTable.BACKGROUNDCANVAS].rectangle(rect);
        }
    }
}
