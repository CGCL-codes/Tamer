package org.eclipse.swt.nebula.widgets.ctabletree.ccontainer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;

/**
 * <p>
 * NOTE:  THIS WIDGET AND ITS API ARE STILL UNDER DEVELOPMENT.  THIS IS A PRE-RELEASE ALPHA 
 * VERSION.  USERS SHOULD EXPECT API CHANGES IN FUTURE VERSIONS.
 * </p>
 */
public abstract class CContainerLayout extends Layout {

    protected CContainer container;

    protected Point bodySize = new Point(0, 0);

    private Point headerSize = new Point(0, 0);

    private Point size = new Point(0, 0);

    private Point scrollSize = new Point(0, 0);

    protected int[] columnWidths;

    protected boolean suppressHBar = false;

    private int lastColWidth = 0;

    private int oldWidth = 0;

    /**
	 * @param tree
	 */
    protected CContainerLayout(CContainer container) {
        this.container = container;
    }

    /**
	 * Computes the size of the Body Composite.  In doing so, the sizes
	 * of the Items (and their cells) must also computed.
	 * @return
	 */
    protected abstract Point computeBodySize();

    private Point computeHeaderSize(Rectangle r) {
        if (container.internalTable != null) {
            int gridLine = container.getGridLineWidth();
            headerSize.x = headerSize.y = 0;
            CContainerColumn[] ca = container.getColumns();
            columnWidths = new int[ca.length];
            for (int i = 0; i < ca.length; i++) {
                columnWidths[i] = ca[i].getWidth();
                headerSize.x += (columnWidths[i] + gridLine);
            }
            headerSize.y = container.internalTable.getHeaderHeight();
        } else {
            columnWidths = new int[] { r.width };
        }
        lastColWidth = columnWidths[columnWidths.length - 1];
        return headerSize;
    }

    protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
        if (columnWidths == null || container.isOperation(CContainer.OP_NONE) || container.isOperation(CContainer.OP_COLUMN_RESIZE)) {
            computeHeaderSize(composite.getClientArea());
        }
        computeBodySize();
        size.x = (headerSize.x > 0) ? headerSize.x : bodySize.x;
        size.y = headerSize.y + bodySize.y;
        size.x += container.marginWidth + container.marginWidth;
        size.y += container.marginHeight + container.marginHeight;
        if (wHint != SWT.DEFAULT) {
            size.x = Math.min(size.x, wHint);
        }
        if (hHint != SWT.DEFAULT) {
            size.y = Math.min(size.y, hHint);
        }
        return size;
    }

    protected void layout(Composite composite, boolean flushCache) {
        container.setOperation(CContainer.OP_NONE);
        Rectangle r = composite.getClientArea();
        suppressHBar = false;
        oldWidth = r.width;
        computeSize(composite, -1, -1, flushCache);
        updateScrollSize(r);
        layoutTable(r);
        r.y += (headerSize.y);
        r.height -= headerSize.y;
        layoutBody(r);
        layoutHeader(r);
        layoutItems(r);
    }

    private void layoutBody(Rectangle r) {
        r.y += container.marginHeight;
        r.height -= (container.marginHeight);
        container.sc.setBounds(r);
        int xOffset = ((container.style & SWT.H_SCROLL) != 0) ? container.sc.getHorizontalBar().getSelection() : 0;
        int yOffset = ((container.style & SWT.V_SCROLL) != 0) ? container.sc.getVerticalBar().getSelection() : 0;
        container.body.setBounds(-xOffset, -yOffset, r.width + xOffset, r.height + yOffset);
    }

    protected void layoutHeader(Rectangle r) {
        List fillers = new ArrayList();
        int xconsumed = 0;
        CContainerColumn[] ca = container.getColumns();
        for (int i = 0; i < ca.length; i++) {
            if (ca[i].isAutoWidth()) {
                fillers.add(ca[i]);
            } else {
                xconsumed += ca[i].getWidth();
            }
        }
        if (!fillers.isEmpty()) {
            int fillerWidth0 = (r.width - xconsumed) / fillers.size();
            int fillerWidth1 = r.width - xconsumed - (fillerWidth0 * (fillers.size() - 1));
            if (fillerWidth0 == 0) fillerWidth0 = 1;
            if (fillerWidth1 == 0) fillerWidth1 = 1;
            for (Iterator i = fillers.iterator(); i.hasNext(); ) {
                CContainerColumn col = (CContainerColumn) i.next();
                if (i.hasNext()) {
                    col.setWidth(fillerWidth0);
                } else {
                    col.setWidth(fillerWidth1);
                }
            }
        }
    }

    protected abstract void layoutItems(Rectangle r);

    private void layoutTable(Rectangle r) {
        if (container.internalTable != null) {
            if (CContainer.win32 && container.sc.getHorizontalBar() != null) {
                int sel = container.sc.getHorizontalBar().getSelection();
                Rectangle hBounds = new Rectangle(r.x, r.y, r.width, r.height);
                hBounds.x = -sel;
                hBounds.width += sel;
                container.internalTable.setBounds(hBounds);
            } else {
                container.internalTable.setBounds(r);
            }
        }
    }

    protected void opLayoutAdd(Rectangle r) {
        computeSize(container, -1, -1, false);
        r.y += (headerSize.y);
        r.height -= headerSize.y;
        updateScrollSize(r);
        layoutBody(r);
        layoutItems(r);
    }

    protected void opLayoutRemove(Rectangle r) {
        opLayoutAdd(r);
    }

    protected void opLayoutCellCollapse(Rectangle r) {
        opLayoutAdd(r);
    }

    protected void opLayoutCellExpand(Rectangle r) {
        opLayoutAdd(r);
    }

    protected void opLayoutColumnResize(Rectangle r) {
        oldWidth = headerSize.x;
        computeSize(container, -1, -1, false);
        if (headerSize.x > oldWidth) {
            suppressHBar = true;
        } else {
            suppressHBar = false;
        }
        updateScrollSize(r);
        layoutTable(r);
        r.y += (headerSize.y);
        r.height -= headerSize.y;
        layoutBody(r);
        if (CContainer.gtk) layoutHeader(r);
        layoutItems(r);
    }

    protected void opLayoutCustom(int operation, Rectangle r) {
        container.setDirtyItem(container.getItem(0));
        container.layout();
    }

    protected void opLayout() {
        Rectangle r = container.getClientArea();
        switch(container.operation) {
            case CContainer.OP_ADD:
                opLayoutAdd(r);
                break;
            case CContainer.OP_REMOVE:
                opLayoutRemove(r);
                break;
            case CContainer.OP_CELL_COLLAPSE:
                opLayoutCellCollapse(r);
                break;
            case CContainer.OP_CELL_EXPAND:
                opLayoutCellExpand(r);
                break;
            case CContainer.OP_COLUMN_RESIZE:
                opLayoutColumnResize(r);
                break;
            default:
                opLayoutCustom(container.operation, r);
        }
        container.clearDirtyItem();
        container.setOperation(CContainer.OP_NONE);
        container.redraw();
    }

    private void updateScrollSize(Rectangle r) {
        int style = container.getStyle();
        if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
            scrollSize.x = size.x;
            scrollSize.y = size.y - headerSize.y;
            if (((style & SWT.V_SCROLL) != 0) && (headerSize.x > 0) && (scrollSize.y >= r.height)) {
                scrollSize.x -= container.sc.getVerticalBar().getSize().x;
            }
            if (suppressHBar) {
                scrollSize.x -= (size.x - oldWidth);
            }
            if (((style & SWT.H_SCROLL) != 0) && ((style & SWT.V_SCROLL) != 0)) {
                container.sc.setMinSize(scrollSize);
            } else if ((style & SWT.H_SCROLL) != 0) {
                container.sc.setMinWidth(scrollSize.x);
            } else if ((style & SWT.V_SCROLL) != 0) {
                container.sc.setMinHeight(scrollSize.y);
            }
            if ((style & SWT.V_SCROLL) != 0) {
                if (scrollSize.y > r.height) {
                    columnWidths[columnWidths.length - 1] = lastColWidth - (container.sc.getVerticalBar().getSize().x + 1);
                } else {
                    columnWidths[columnWidths.length - 1] = lastColWidth;
                }
            }
        }
    }
}
