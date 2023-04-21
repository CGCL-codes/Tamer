package org.gudy.azureus2.ui.swt.views.table.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.gudy.azureus2.core3.config.COConfigurationManager;
import org.gudy.azureus2.core3.config.ParameterListener;
import org.gudy.azureus2.core3.util.AERunnable;
import org.gudy.azureus2.ui.swt.Utils;
import org.gudy.azureus2.ui.swt.mainwindow.Colors;
import org.gudy.azureus2.ui.swt.mainwindow.HSLColor;
import org.gudy.azureus2.ui.swt.views.table.TableColumnOrTreeColumn;
import org.gudy.azureus2.ui.swt.views.table.TableItemOrTreeItem;
import org.gudy.azureus2.ui.swt.views.table.TableOrTreeSWT;
import com.aelitis.azureus.ui.swt.utils.ColorCache;

public class TableViewSWT_EraseItem implements Listener {

    public static final Color[] alternatingColors = new Color[] { null, Colors.colorAltRow };

    private final TableOrTreeSWT table;

    private TableViewSWTImpl<?> tv;

    private boolean drawExtended;

    private boolean first = true;

    private Color colorLine;

    public TableViewSWT_EraseItem(TableViewSWTImpl<?> _tv, TableOrTreeSWT table) {
        this.table = table;
        this.tv = _tv;
        COConfigurationManager.addAndFireParameterListener("Table.extendedErase", new ParameterListener() {

            public void parameterChanged(String parameterName) {
                Utils.execSWTThread(new AERunnable() {

                    public void runSupport() {
                        drawExtended = COConfigurationManager.getBooleanParameter("Table.extendedErase");
                        if (!first) {
                            Rectangle bounds = tv.getTableComposite().getBounds();
                            tv.getTableComposite().redraw(bounds.x, bounds.y, bounds.width, bounds.height, true);
                        }
                        first = false;
                    }
                });
            }
        });
        colorLine = tv.getComposite().getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
        HSLColor hslColor = new HSLColor();
        hslColor.initHSLbyRGB(colorLine.getRed(), colorLine.getGreen(), colorLine.getBlue());
        int lum = hslColor.getLuminence();
        if (lum > 127) lum -= 25; else lum += 40;
        hslColor.setLuminence(lum);
        colorLine = new Color(tv.getComposite().getDisplay(), hslColor.getRed(), hslColor.getGreen(), hslColor.getBlue());
    }

    public void handleEvent(Event event) {
        if (event.type == SWT.EraseItem) {
            eraseItem(event);
        } else if (drawExtended) {
            paint(event);
        }
    }

    private void paint(Event event) {
        int numItems = table.getItemCount();
        int rowHeight = table.getItemHeight();
        int height = rowHeight * numItems;
        int rowAreaHeight = tv.clientArea.height + tv.clientArea.y;
        int blankHeight = rowAreaHeight - height;
        if (blankHeight > 0) {
            int startY;
            if (numItems > 0) {
                TableItemOrTreeItem lastItem = table.getItem(numItems - 1);
                if (lastItem != null) {
                    Rectangle lastItemBounds;
                    if (lastItem.getExpanded()) {
                        TableItemOrTreeItem[] subItems = lastItem.getItems();
                        lastItemBounds = subItems == null || subItems.length == 0 ? lastItem.getBounds() : subItems[subItems.length - 1].getBounds();
                    } else {
                        lastItemBounds = lastItem.getBounds();
                    }
                    startY = lastItemBounds.y + lastItemBounds.height;
                } else {
                    startY = 0;
                }
            } else {
                startY = 0;
            }
            GC gc = event.gc;
            gc.setClipping((Rectangle) null);
            for (int i = 0; i < blankHeight / rowHeight; i++) {
                int curY = (startY + (i * rowHeight));
                int pos = (i + numItems) % 2;
                Color color = i <= -1 ? ColorCache.getRandomColor() : alternatingColors[pos];
                if (color == null) {
                    continue;
                }
                gc.setBackground(color);
                gc.fillRectangle(tv.clientArea.x, startY + (i * rowHeight), tv.clientArea.width, rowHeight);
            }
            if (TableViewSWTImpl.DRAW_VERTICAL_LINES) {
                TableColumnOrTreeColumn[] columns = table.getColumns();
                int pos = 0;
                gc.setForeground(Colors.black);
                gc.setAdvanced(true);
                gc.setAlpha(10);
                for (TableColumnOrTreeColumn tc : columns) {
                    gc.drawLine(pos - 1, startY, pos - 1, startY + blankHeight);
                    pos += tc.getWidth();
                }
                gc.drawLine(pos - 1, startY, pos - 1, startY + blankHeight);
            }
        }
    }

    private void eraseItem(Event event) {
        TableItemOrTreeItem item = TableOrTreeUtils.getEventItem(event.item);
        Rectangle bounds = event.getBounds();
        if ((event.detail & (SWT.HOT | SWT.SELECTED | SWT.FOCUSED)) == 0) {
            int pos;
            TableItemOrTreeItem parentItem = item.getParentItem();
            if (parentItem != null) {
                pos = parentItem.indexOf(item) + ((table.indexOf(parentItem) + 1) % 2);
            } else {
                pos = table.indexOf(item);
            }
            Color color = alternatingColors[pos % 2];
            if (color != null) {
                event.gc.setBackground(color);
                if (parentItem != null) {
                    event.gc.setAlpha(128);
                }
            }
            Rectangle drawBounds = bounds;
            if (TableViewSWTImpl.DRAW_FULL_ROW && drawExtended && event.index == table.getColumnCount() - 1) {
                tv.swt_calculateClientArea();
                drawBounds = new Rectangle(bounds.x, bounds.y, tv.clientArea.x + tv.clientArea.width - bounds.x, bounds.height);
                event.gc.setClipping(drawBounds);
            }
            if (color != null) {
                event.gc.fillRectangle(drawBounds);
                event.detail &= ~SWT.BACKGROUND;
            }
        }
        if ((event.detail & SWT.SELECTED) > 0 && !table.isFocusControl()) {
            event.gc.setBackground(Colors.blues[3]);
            event.gc.fillRectangle(bounds);
            event.detail &= ~SWT.BACKGROUND;
        }
        if (TableViewSWTImpl.DRAW_VERTICAL_LINES && drawExtended) {
            if (item != null && (bounds.width == item.getParent().getColumn(event.index).getWidth())) {
                Color fg = event.gc.getForeground();
                event.gc.setForeground(colorLine);
                event.gc.setClipping((Rectangle) null);
                event.gc.drawLine(bounds.x + bounds.width - 1, bounds.y - 1, bounds.x + bounds.width - 1, bounds.y + bounds.height);
                event.gc.setForeground(fg);
            }
        }
    }
}
