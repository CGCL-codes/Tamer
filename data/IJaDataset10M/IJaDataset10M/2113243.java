package orcajo.azada.browser.views.table;

import orcajo.azada.core.olap.SelectionHelper;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import de.kupzog.ktable.KTableModel;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.renderers.TextCellRenderer;

/**
 * 
 * @author Felix Saz
 *
 */
public class DataRenderer extends TextCellRenderer {

    public DataRenderer(int style) {
        super(style);
    }

    private String getStringValue(Object content) {
        String sValue;
        if (content instanceof SelectionHelper) {
            sValue = ((SelectionHelper) content).getCaption();
        } else {
            sValue = (content == null) ? "" : content.toString();
        }
        return sValue;
    }

    public int getOptimalWidth(GC gc, int col, int row, Object content, boolean fixed, KTableModel model) {
        return SWTX.getCachedStringExtent(gc, getStringValue(content)).x + 8;
    }

    public void drawCell(GC gc, Rectangle rect, int col, int row, Object content, boolean focus, boolean fixed, boolean clicked, KTableModel model) {
        applyFont(gc);
        String sValue = getStringValue(content);
        rect = drawDefaultSolidCellLine(gc, rect, COLOR_BGROWFOCUS, COLOR_BGROWFOCUS);
        drawCellContent(gc, rect, sValue, null, getForeground(), getBackground());
        resetFont(gc);
    }
}
