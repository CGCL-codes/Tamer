package com.tensegrity.palobrowser.cube.internal;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.palo.api.ext.ui.ColorDescriptor;
import org.palo.api.ext.ui.FontDescriptor;
import org.palo.api.ext.ui.Format;
import org.palo.api.ext.ui.table.TableFormat;
import com.tensegrity.palobrowser.PalobrowserPlugin;
import com.tensegrity.palobrowser.cube.PaloTable;
import com.tensegrity.palobrowser.editors.DataComposite;
import com.tensegrity.palobrowser.resource.ColorRegistry;
import com.tensegrity.palobrowser.resource.FontRegistry;
import com.tensegrity.palobrowser.table.AxisModel;

public abstract class TableHeader extends Canvas {

    protected static final int MIN_ITEM_WIDTH = 34;

    protected static final int MIN_ITEM_HEIGHT = 15;

    protected static final int RESIZE_THRESHOLD = 4;

    public static final int DEFAULT_ITEM_SIZE = 100;

    private static final String INIT_MIN_HEADER = InternalMessages.getString("TableHeader.MinHeaderStr");

    private static final String INIT_MAX_HEADER = InternalMessages.getString("TableHeader.MaxHeaderStr");

    protected Point headerMinExt;

    protected Point headerMaxExt;

    protected Point viewOrg;

    /** internal used model */
    protected HeaderItem[][] model;

    /** 
	 * indent to use for calculating item sizes. gap between item and border,
	 * or item and image
	 **/
    static final int INDENT = 3;

    protected final int DEPTH_INDENT;

    protected int scrollOffset;

    public Cursor cursor;

    public final Cursor vResizeCursor;

    public final Cursor hResizeCursor;

    private GC extGC;

    private ItemController itemController;

    protected final DataComposite pTable;

    protected final Display dpl;

    protected final int iconSize;

    private final boolean dumpStats;

    protected AbstractTableHeaderLayouter layouter;

    /**
	 * Factory method which creates either a vertical or horizontal header
	 * @param parent the <code>Composite</code> to add this header to
	 * @param direction use either <code>SWT.VERTICAL</code> or 
	 * <code>SWT.HORIZONTAL</code> to specify header layout direction
	 * @return a <code>ITableHeader</code> implementation
	 */
    public static final TableHeader createHeader(DataComposite parent, int direction) {
        if (direction == SWT.VERTICAL) return new VerticalTableHeader(parent);
        return new HorizontalTableHeader(parent);
    }

    protected TableHeader(DataComposite parent) {
        super(parent, SWT.DOUBLE_BUFFERED);
        pTable = parent;
        dpl = getDisplay();
        iconSize = getImageSize();
        DEPTH_INDENT = iconSize + INDENT;
        hResizeCursor = new Cursor(getDisplay(), SWT.CURSOR_SIZEWE);
        vResizeCursor = new Cursor(getDisplay(), SWT.CURSOR_SIZENS);
        extGC = new GC(this);
        viewOrg = new Point(0, 0);
        addListeners();
        updateInitialItemBounds();
        dumpStats = "true".equalsIgnoreCase(Platform.getDebugOption(PalobrowserPlugin.getDefault().getBundle().getSymbolicName() + "/debug/dumpTableHeaderStats"));
    }

    public final void setModel(HeaderItem[][] model) {
        this.model = model;
        itemController = new ItemController(model, this);
        initializeItems();
    }

    public final void setScrollOffset(int scrollOffset) {
        this.scrollOffset = scrollOffset;
        layoutHeader();
    }

    public final int getScrollOffset() {
        return this.scrollOffset;
    }

    public final Point getStringExtent(HeaderItem item, String str) {
        Font fnt = setFont(extGC, item);
        Point strExt = extGC.stringExtent(str);
        if (fnt != null) {
            extGC.setFont(getFont());
            fnt.dispose();
        }
        return strExt;
    }

    public final void fade(Set domainObjects, int dimension) {
        itemController.fadeIn(domainObjects, dimension);
    }

    public final void expandAll() {
        itemController.expandAll(true);
        layoutHeader();
    }

    public final void collapseAll() {
        itemController.expandAll(false);
        layoutHeader();
    }

    public final void setExpandState(Set domObjs) {
        if (model.length == 0) return;
        long t0 = 0;
        if (dumpStats) t0 = System.currentTimeMillis();
        itemController.setInitialExpandState(domObjs);
        if (dumpStats) {
            long t1 = System.currentTimeMillis();
            System.out.println("setExpandState took: " + (t1 - t0) + "ms");
        }
    }

    public final void resizeItem(HeaderItem item, int newEnd) {
        setCursor(null);
        HeaderItem leafItem = getLeafItem(item);
        leafItem.setState(HeaderItem.RESIZED);
        resizeLeafItem(leafItem, newEnd);
        layoutHeader();
        pTable.refresh();
    }

    public final void resizeDimension(int lvl, int newEnd) {
        int index = model[lvl].length;
        for (int i = 0; i < index; i++) {
            HeaderItem item = model[lvl][i];
            item.setState(HeaderItem.RESIZED);
            resizeItemEnd(item, newEnd);
        }
        pTable.refresh();
    }

    /**
	 * Forces a complete repaint of whole header
	 */
    public final void refresh() {
        refresh(false);
    }

    public final void refresh(boolean itemChanged) {
        if (itemChanged) layoutHeader();
        if (pTable.isBatchmode()) return;
        dpl.asyncExec(new Runnable() {

            public void run() {
                if (!isDisposed()) {
                    redraw();
                    update();
                }
            }
        });
    }

    /**
	 * Returns all <code>HeaderItem</code>s of the last level.
	 * 
	 * @return
	 */
    public final HeaderItem[] getItems() {
        if (model != null && model.length > 0) return model[model.length - 1];
        return new HeaderItem[0];
    }

    /**
	 * Returns the HeaderItem which is hit by the specified position
	 * @param x
	 * @param y
	 * @return the header item or null if none could be found
	 */
    public HeaderItem getItem(int x, int y) {
        return layouter.getItem(x, y);
    }

    public final HeaderItem[][] getModel() {
        return model;
    }

    public final int getItemIndent() {
        return INDENT;
    }

    public final int getDepthIndent() {
        return DEPTH_INDENT;
    }

    public final void drawVerticalLine(GC gc, int x1, int y1, int x2, int y2) {
        Color backUp = gc.getForeground();
        gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
        gc.drawLine(x1, y1, x2, y2);
        gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
        gc.drawLine(x1 - 1, y1, x2 - 1, y2);
        gc.setForeground(backUp);
    }

    public final void drawHorizontalLine(GC gc, int x1, int y1, int x2, int y2) {
        Color backUp = gc.getForeground();
        gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
        gc.drawLine(x1, y1, x2, y2);
        gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
        gc.drawLine(x1, y1 - 1, x2, y2 - 1);
        gc.setForeground(backUp);
    }

    protected final void drawString(GC gc, String s, int x, int y, int maxX, int maxH) {
        Point p = gc.stringExtent(s);
        if (p.y >= maxH) return;
        if (p.x < maxX) {
            gc.drawString(s, x, y, true);
            return;
        }
        StringBuffer sb = new StringBuffer(s);
        for (; ; ) {
            String subject = sb.toString() + "...";
            p = gc.stringExtent(subject);
            if (p.x < maxX) {
                gc.drawString(subject, x, y, true);
                break;
            }
            int l = sb.length();
            if (l <= 0) break;
            sb.delete(l - 1, l);
        }
    }

    public final void setFont(Font f) {
        super.setFont(f);
        extGC.setFont(f);
        updateInitialItemBounds();
    }

    public final Point computeSize(int wHint, int hHint, boolean changed) {
        Point size;
        if (model != null && model.length > 0) {
            size = calcSize();
            if (size.x < MIN_ITEM_WIDTH) size.x = MIN_ITEM_WIDTH;
            if (size.y < MIN_ITEM_HEIGHT) size.y = MIN_ITEM_HEIGHT;
        } else size = new Point(0, 0);
        return size;
    }

    public final void setCursor(Cursor crs) {
        cursor = crs;
        super.setCursor(crs);
    }

    /**
	 * Checks if a dimension bound was hit by specified x and y value.
	 * Returns the corresponding dimension index, or -1 if no dimension bound
	 * was hit.
	 * @param x
	 * @param y
	 * @return
	 */
    public final int hitDimensionBound(int x, int y) {
        HeaderItem item = null;
        for (int i = 0, n = model.length; i < n; i++) {
            for (int j = 0, l = model[i].length; j < l; j++) {
                item = model[i][j];
                if (!item.isVisible() || !item.isHitBy(x, y)) continue;
                if (hitExpandIcon(item, x, y)) return -1;
                HeaderItem nextItem = getFirstChildInNextDimension(item);
                if (nextItem != null && hitExpandIcon(nextItem, x, y)) return -1;
                int hitDimBound = hitDimensionBound(item, x, y);
                if (hitDimBound == 0) {
                    return i;
                } else if (hitDimBound > 0) continue; else return -1;
            }
        }
        return -1;
    }

    public final HeaderItem getDimensionItem(int x, int y) {
        HeaderItem item = null;
        for (int i = 0, n = model.length; i < n; i++) {
            for (int j = 0, l = model[i].length; j < l; j++) {
                item = model[i][j];
                if (!item.isVisible()) continue;
                HeaderItem nextItem = item;
                if (j + 1 < model[i].length) nextItem = model[i][j + 1];
                if (hitExpandIcon(item, x, y) || hitExpandIcon(nextItem, x, y)) {
                    setCursor(null);
                    return null;
                }
                if (isDimensionItem(item, x, y)) return item;
            }
        }
        setCursor(null);
        return null;
    }

    /**
	 * 
	 * @param item
	 * @param x
	 * @param y
	 * @param keyStateMask
	 * @return true if expand icon was hit...
	 */
    public final boolean toggleItem(HeaderItem item, int x, int y, int keyStateMask) {
        if (hitExpandIcon(item, x, y)) {
            if (modifiersPressed(keyStateMask)) {
                long t0 = 0;
                if (dumpStats) t0 = System.currentTimeMillis();
                boolean itemExpState = !item.hasState(HeaderItem.EXPANDED);
                List models = deepExpand(item, itemExpState, keyStateMask);
                if (dumpStats) {
                    long t1 = System.currentTimeMillis();
                    System.out.println("TableHeader#toggleItem() computing expanded items took " + (t1 - t0) + "ms");
                }
                refreshAndNotifyExpand(models, item, keyStateMask, !itemExpState);
            } else {
                pTable.setKeyStateMask(0);
                toggle(item.lvl, item.index, model);
            }
            return true;
        }
        return false;
    }

    public final boolean toggleItem(HeaderItem item, int keyStateMask) {
        if (modifiersPressed(keyStateMask)) {
            long t0 = 0;
            if (dumpStats) t0 = System.currentTimeMillis();
            boolean itemExpState = !item.hasState(HeaderItem.EXPANDED);
            List models = deepExpand(item, itemExpState, keyStateMask);
            if (dumpStats) {
                long t1 = System.currentTimeMillis();
                System.out.println("TableHeader#toggleItem() computing expanded items took " + (t1 - t0) + "ms");
            }
            refreshAndNotifyExpand(models, item, keyStateMask, !itemExpState);
        } else {
            pTable.setKeyStateMask(0);
            toggle(item.lvl, item.index, model);
        }
        return true;
    }

    public final boolean isResizeCursorSet() {
        if (cursor != null) return cursor.equals(vResizeCursor) || cursor.equals(hResizeCursor);
        return false;
    }

    public final void initItem(HeaderItem item) {
        Point iSize = calcItemSize(item);
        checkItemWidth(item, iSize.x);
        checkItemHeight(item, iSize.y);
    }

    public final void expandItemsAtLevel(int dim, int lvl) {
        Set expanded = getExpandedModels(dim);
        List models = itemController.expandToLevel(dim, lvl);
        boolean doIt = true;
        LinkedHashSet modelsSet = new LinkedHashSet(models);
        AxisModel[] aModels = (AxisModel[]) modelsSet.toArray(new AxisModel[modelsSet.size()]);
        doIt = willShowModels(aModels, true);
        itemController.updateHeaderItems(doIt);
        if (doIt) {
            showModels(aModels, true);
        }
        doCollapse(modelsSet, expanded);
        pTable.refresh();
    }

    public final void reset() {
        for (int i = 0; i < model.length; i++) {
            int maxW = 0, maxH = 0;
            for (int j = 0, n = model[i].length; j < n; j++) {
                HeaderItem item = model[i][j];
                item.w = 0;
                item.h = 0;
                initItem(item);
                if (item.w > maxW) maxW = item.w;
                if (item.h > maxH) maxH = item.h;
            }
            initDimension(i, maxW, maxH);
        }
    }

    /**
	 * Returns parent in previous dimension corresponding to the given item
	 * @param item
	 * @return the parent in previous dimension, or null
	 */
    public final HeaderItem getDimParent(HeaderItem item) {
        int prevDim = item.lvl - 1;
        if (prevDim >= 0) {
            int numerator = model[item.lvl].length;
            int offset = numerator / model[prevDim].length;
            int pIndex = item.index / offset;
            return model[prevDim][pIndex];
        }
        return null;
    }

    protected final HeaderItem getLeafItem(HeaderItem item) {
        int lvlIndex = item.lvl;
        int dominator = model[lvlIndex].length;
        HeaderItem childItem = item;
        lvlIndex++;
        if (lvlIndex < model.length) {
            int offset = model[lvlIndex].length / dominator;
            int first = item.index * offset;
            int last = first + offset - 1;
            while (last > 0 && !model[lvlIndex][last].isVisible()) last--;
            childItem = getLeafItem(model[lvlIndex][last]);
        }
        return childItem;
    }

    public void setLayouter(AbstractTableHeaderLayouter layouter) {
        this.layouter = layouter;
    }

    public AbstractTableHeaderLayouter getLayouter() {
        return this.layouter;
    }

    protected abstract void initDimension(int lvl, int maxW, int maxH);

    protected abstract void resizeLeafItem(HeaderItem item, int newEnd);

    protected abstract void resizeItemEnd(HeaderItem item, int newEnd);

    public abstract Point calcSize();

    protected abstract void drawHeaderBounds(GC gc, Rectangle pTableSize);

    public abstract void drawHeader(GC gc, Rectangle pTableSize);

    protected abstract int hitDimensionBound(HeaderItem item, int x, int y);

    protected abstract boolean isDimensionItem(HeaderItem item, int x, int y);

    protected abstract void showModels(AxisModel[] models, boolean expState);

    protected abstract boolean willShowModels(AxisModel[] models, boolean expState);

    protected abstract void checkItemHeight(HeaderItem item, int h);

    protected abstract void checkItemWidth(HeaderItem item, int w);

    public abstract void layoutHeader();

    public abstract void zoomHeader(int factor);

    public abstract boolean hitExpandIcon(HeaderItem item, int x, int y);

    public abstract Point calcItemSize(HeaderItem item);

    public abstract void alignItem(HeaderItem item);

    private final void initializeItems() {
        if (model == null) return;
        for (int i = 0; i < model.length; i++) {
            int maxW = 0, maxH = 0;
            for (int j = 0, n = model[i].length; j < n; j++) {
                HeaderItem item = model[i][j];
                if (item.w > maxW) maxW = item.w;
                if (item.h > maxH) maxH = item.h;
            }
            initDimension(i, maxW, maxH);
        }
        layoutHeader();
    }

    private final void addListeners() {
        addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent pe) {
                TableHeader.this.paintControl(pe);
            }
        });
        addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent de) {
                TableHeader.this.widgetDisposed(de);
            }
        });
    }

    private final void widgetDisposed(DisposeEvent de) {
        vResizeCursor.dispose();
        hResizeCursor.dispose();
        extGC.dispose();
    }

    private final void paintControl(PaintEvent pe) {
        if (pTable.isBatchmode()) return;
        GC gc = pe.gc;
        Rectangle cArea = getClientArea();
        gc.setBackground(pTable.getBackground());
        gc.fillRectangle(cArea);
        drawHeaderBounds(gc, cArea);
        cArea.x = 2;
        cArea.y = 2;
        cArea.width -= 4;
        cArea.height -= 4;
        gc.setClipping(cArea);
        cArea.x = 0;
        cArea.y = 0;
        drawHeader(gc, cArea);
    }

    private final int getImageSize() {
        Rectangle imgBounds = PaloTable.expImg.getBounds();
        return Math.max(imgBounds.width, imgBounds.height);
    }

    /**
	 * Returns the first child in next dimension of given item or null
	 * if no child exists.
	 */
    public final HeaderItem getFirstChildInNextDimension(HeaderItem item) {
        int dominator = model[item.lvl].length;
        int nextLvl = item.lvl + 1;
        if (nextLvl < model.length) {
            int offset = model[nextLvl].length / dominator;
            int first = item.index * offset;
            return model[nextLvl][first];
        }
        return null;
    }

    /**
	 * Returns the last child in next dimension of given item or null
	 * if no child exists.
	 */
    public final HeaderItem getLastChildInNextDimension(HeaderItem item) {
        int dominator = model[item.lvl].length;
        int nextLvl = item.lvl + 1;
        if (nextLvl < model.length) {
            int offset = model[nextLvl].length / dominator;
            int first = item.index * offset;
            return model[nextLvl][first + offset - 1];
        }
        return null;
    }

    /**
	 * <b>NOTE:</b> call ItemController#updateHeaderItems afterwards!!
	 */
    private final List deepExpand(HeaderItem item, boolean expState, int keyMask) {
        List models = null;
        pTable.setKeyStateMask(keyMask);
        if ((keyMask & SWT.CTRL) > 0) {
            if ((keyMask & SWT.SHIFT) > 0) {
                if ((keyMask & SWT.ALT) > 0) {
                    models = itemController.expandOnCTRL_SHIFT_ALT(item, expState);
                } else models = itemController.expandOnCTRL_SHIFT(item, expState);
            } else if ((keyMask & SWT.ALT) > 0) {
                models = itemController.expandOnCTRL_ALT(item, expState);
            } else models = itemController.expandOnCTRL(item, expState);
        } else if ((keyMask & SWT.SHIFT) > 0) {
            if ((keyMask & SWT.ALT) > 0) {
                models = itemController.expandOnSHIFT_ALT(item, expState);
            } else models = itemController.expandOnSHIFT(item, expState);
        } else if ((keyMask & SWT.ALT) > 0) {
            models = itemController.expandOnALT(item, expState);
        }
        return models;
    }

    private final void refreshAndNotifyExpand(List models, HeaderItem item, int keyMask, boolean expand) {
        if (models == null) return;
        long t0 = 0;
        if (dumpStats) t0 = System.currentTimeMillis();
        Set expandedBefore = getExpandedModels(item.lvl);
        LinkedHashSet modelsSet = new LinkedHashSet(models);
        AxisModel[] aModels = (AxisModel[]) modelsSet.toArray(new AxisModel[modelsSet.size()]);
        if (dumpStats) {
            long t1 = System.currentTimeMillis();
            System.out.println("TableHeader#refreshAndNotifyExpand() - creating model array tooks " + (t1 - t0) + "ms");
        }
        boolean doIt = willShowModels(aModels, !expand);
        itemController.updateHeaderItems(doIt);
        if (doIt) showModels(aModels, !expand);
        Set expandedAfter = getExpandedModels(item.lvl);
        doCollapse(expandedAfter, expandedBefore);
        pTable.refresh();
    }

    /**
	 * This toggles the expanded state of the specified item and triggers a 
	 * redraw of the complete CubeTable afterwards.
	 * @param lvlIndex
	 * @param elIndex
	 * @param hModel
	 */
    private final void toggle(int lvlIndex, int elIndex, HeaderItem[][] hModel) {
        HeaderItem item = hModel[lvlIndex][elIndex];
        AxisModel[] aModels = new AxisModel[] { item.getModel() };
        boolean expState = !item.hasState(HeaderItem.EXPANDED);
        itemController.setExpandState(item, expState);
        boolean doIt = true;
        doIt = willShowModels(aModels, item.hasState(HeaderItem.WILL_BE_EXPANDED));
        itemController.updateHeaderItems(doIt || !expState);
        if (doIt) {
            showModels(aModels, item.hasState(HeaderItem.EXPANDED));
        }
        pTable.refresh();
    }

    public final void toggle(HeaderItem item) {
        AxisModel[] aModels = new AxisModel[] { item.getModel() };
        boolean expState = !item.hasState(HeaderItem.EXPANDED);
        itemController.setExpandState(item, expState);
        boolean doIt = true;
        doIt = willShowModels(aModels, item.hasState(HeaderItem.WILL_BE_EXPANDED));
        itemController.updateHeaderItems(doIt || !expState);
        if (doIt) {
            showModels(aModels, item.hasState(HeaderItem.EXPANDED));
        }
        pTable.refresh();
    }

    private final boolean modifiersPressed(int keyStateMask) {
        return (((keyStateMask & SWT.SHIFT) > 0) || ((keyStateMask & SWT.ALT) > 0) || ((keyStateMask & SWT.CTRL) > 0));
    }

    private final void updateInitialItemBounds() {
        headerMaxExt = getStringExtent(null, INIT_MAX_HEADER);
        headerMinExt = getStringExtent(null, INIT_MIN_HEADER);
        int indent = 2 * INDENT;
        headerMaxExt.x += indent;
        headerMaxExt.y += indent;
        headerMinExt.x += indent;
        headerMinExt.y += indent;
        if (model == null) return;
        for (int dim = 0; dim < model.length; dim++) for (int index = 0; index < model[dim].length; index++) initItem(model[dim][index]);
    }

    /**
	 * Determines currently expanded items and returns the corresponding models
	 * @param dim dimension to check
	 * @return <code>Set</code> of <code>AxisModels</code> which corresponding
	 * <code>HeaderItems</code>are currently expanded
	 */
    private final Set getExpandedModels(int dim) {
        Set expanded = new HashSet();
        for (int i = 0, n = model[dim].length; i < n; i++) {
            HeaderItem item = model[dim][i];
            if (item.hasState(HeaderItem.EXPANDED)) {
                expanded.add(item.getModel());
            }
        }
        return expanded;
    }

    /**
	 * Informs <code>PaloTable</code> on collapse events
	 * @param newState containes newly expanded <code>AxisModel</code>s
	 * @param oldState containes currently expanded <code>AxisModel</code>s
	 */
    private final void doCollapse(Set newState, Set oldState) {
        if (oldState.removeAll(newState) || newState.isEmpty()) showModels((AxisModel[]) oldState.toArray(new AxisModel[oldState.size()]), false);
    }

    public Point getViewOrg() {
        return viewOrg;
    }

    public void setViewOrg(Point viewOrg) {
        this.viewOrg = viewOrg;
    }

    public Point getHeaderMaxExt() {
        return headerMaxExt;
    }

    public void setHeaderMaxExt(Point headerMaxExt) {
        this.headerMaxExt = headerMaxExt;
    }

    public Format getFormat(HeaderItem item) {
        if (item == null) return null;
        TableFormat amf = item.getModel().getFormat();
        if (amf != null) return amf.getHeaderFormat(); else return null;
    }

    public Font setFont(GC gc, HeaderItem item) {
        Format fmt = getFormat(item);
        Font fntCustom = null;
        if (fmt != null) {
            FontDescriptor fd = fmt.getFont();
            if (fd != null) {
                FontRegistry fontReg = FontRegistry.getInstance();
                fntCustom = fontReg.getFont(fd);
            }
            ColorDescriptor cd = fmt.getFontColor();
            if (fmt.getFontColor() != null) {
                ColorRegistry colReg = ColorRegistry.getInstance();
                gc.setForeground(colReg.getColor(cd));
            }
        }
        if (fntCustom == null) gc.setFont(getFont()); else gc.setFont(fntCustom);
        return fntCustom;
    }
}
