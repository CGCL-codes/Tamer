package com.thyante.thelibrarian.components;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

public class MediaGalleryGroup implements PaintListener, MouseListener, KeyListener {

    /**
	 * The item's default width
	 */
    public static final int DEFAULT_WIDTH = 64;

    public static final int MARGIN_HORZ = 4;

    public static final int MARGIN_VERT = 4;

    public static final int MARGIN_HORZ_IMAGE = 10;

    public static final int MARGIN_VERT_IMAGE = 10;

    /**
	 * The gallery object
	 */
    protected IMediumGallery m_gallery;

    /**
	 * The group UI component
	 */
    protected Composite m_cmpGroup;

    /**
	 * List of gallery items belonging to this group
	 */
    protected List<MediaGalleryItem> m_listItems;

    /**
	 * The foreground color
	 */
    protected Color m_colForeground;

    /**
	 * The background color
	 */
    protected Color m_colBackground;

    /**
	 * The selection foreground color
	 */
    protected Color m_colSelectionForeground;

    /**
	 * The selection background color
	 */
    protected Color m_colSelectionBackground;

    /**
	 * Height of the group component in pixels
	 */
    protected int m_nGroupHeight;

    /**
	 * Number of items per row
	 */
    protected int m_nItemsPerRow;

    /**
	 * List of selection listeners
	 */
    protected List<IMediumGallerySelectionListener> m_listSelectionListeners;

    /**
	 * List of gallery listeners
	 */
    protected List<IMediumGalleryListener> m_listGalleryListeners;

    /**
	 * User-defined data
	 */
    protected Object m_objData;

    public MediaGalleryGroup(Composite cmpGallery, IMediumGallery gallery, int nStyle) {
        m_gallery = gallery;
        m_cmpGroup = new Composite(cmpGallery, nStyle | SWT.DOUBLE_BUFFERED) {

            @Override
            public Point computeSize(int nWidthHint, int nHeightHint, boolean bChanged) {
                return new Point(nWidthHint, m_nGroupHeight);
            }
        };
        m_cmpGroup.addPaintListener(this);
        m_cmpGroup.addMouseListener(this);
        m_cmpGroup.addKeyListener(this);
        m_listItems = new LinkedList<MediaGalleryItem>();
        Display display = cmpGallery.getDisplay();
        m_colForeground = display.getSystemColor(SWT.COLOR_LIST_FOREGROUND);
        m_colBackground = display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
        m_colSelectionForeground = display.getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT);
        m_colSelectionBackground = display.getSystemColor(SWT.COLOR_LIST_SELECTION);
        m_nGroupHeight = 1;
        m_nItemsPerRow = 0;
        m_listSelectionListeners = new LinkedList<IMediumGallerySelectionListener>();
        m_listGalleryListeners = new LinkedList<IMediumGalleryListener>();
        m_objData = null;
    }

    public void dispose() {
        m_cmpGroup.removePaintListener(this);
        m_cmpGroup.removeMouseListener(this);
        m_cmpGroup.removeKeyListener(this);
        for (MediaGalleryItem item : m_listItems) item.dispose();
    }

    /**
	 * Attaches application-specific data to the group.
	 * @param objData The data to attach
	 */
    public void setData(Object objData) {
        m_objData = objData;
    }

    /**
	 * Returns the application-defined data attached to the group.
	 * @return The user data
	 */
    public Object getData() {
        return m_objData;
    }

    /**
	 * Returns the underlying UI control.
	 * @return The UI component
	 */
    public Composite getControl() {
        return m_cmpGroup;
    }

    /**
	 * Adds the item <code>item</code> to the group.
	 * @param item The item to add
	 */
    public void addItem(MediaGalleryItem item) {
        m_listItems.add(item);
        m_cmpGroup.redraw();
        fireItemAdded(item);
    }

    /**
	 * Removes the item <code>item</code> from the group.
	 * @param item The item to remove
	 */
    public void removeItem(MediaGalleryItem item) {
        item.dispose();
        m_listItems.remove(item);
        m_cmpGroup.redraw();
        fireItemRemoved(item);
    }

    /**
	 * Removes all the items from the group.
	 */
    public void removeAllItems() {
        for (MediaGalleryItem item : m_listItems) item.dispose();
        m_listItems.clear();
        m_cmpGroup.redraw();
        fireItemRemoved(null);
    }

    /**
	 * Returns an <code>Iterable</code> over all the items contained in the group.
	 * @return The <code>Iterable</code>
	 */
    public Iterable<MediaGalleryItem> getItems() {
        return m_listItems;
    }

    /**
	 * Selects or de-selects all the items in the group.
	 * @param bSelect Determines whether a selection (<code>bSelect = true</code>) or a
	 * 	de-selection (<code>bSelect = false</code>) is performed
	 */
    public void selectAll(boolean bSelect) {
        for (MediaGalleryItem item : m_listItems) item.setSelected(bSelect);
        m_cmpGroup.redraw();
    }

    /**
	 * Returns the number of selected items in this group.
	 * @return The number of selected items
	 */
    public int getSelectedItemsCount() {
        int nCount = 0;
        for (MediaGalleryItem item : m_listItems) if (item.isSelected()) nCount++;
        return nCount;
    }

    /**
	 * Returns an array of all the selected items.
	 * @return Array of selected items
	 */
    public MediaGalleryItem[] getSelection() {
        MediaGalleryItem[] rgSelectedItems = new MediaGalleryItem[getSelectedItemsCount()];
        int n = 0;
        for (MediaGalleryItem item : m_listItems) if (item.isSelected()) {
            rgSelectedItems[n] = item;
            n++;
            if (n == rgSelectedItems.length) break;
        }
        return rgSelectedItems;
    }

    /**
	 * Adds the selection listener <code>listener</code> to the selection listener list.
	 * @param listener The listener to add
	 */
    public void addSelectionListener(IMediumGallerySelectionListener listener) {
        m_listSelectionListeners.add(listener);
    }

    /**
	 * Removes the selection listener <code>listener</code> from the selection listener list.
	 * @param listener The listener to remove
	 */
    public void removeSelectionListener(IMediumGallerySelectionListener listener) {
        m_listSelectionListeners.remove(listener);
    }

    /**
	 * Adds the gallery listener <code>listener</code> to the listener list.
	 * @param listener The listener to add
	 */
    public void addMediumGalleryListener(IMediumGalleryListener listener) {
        m_listGalleryListeners.add(listener);
    }

    /**
	 * Removes the gallery listener <code>listener</code> from the listener list.
	 * @param listener The listener to remove
	 */
    public void removeGalleryListener(IMediumGalleryListener listener) {
        m_listGalleryListeners.remove(listener);
    }

    protected void fireItemAdded(MediaGalleryItem item) {
        for (IMediumGalleryListener l : m_listGalleryListeners) l.onItemAdded(m_gallery, this, item);
    }

    protected void fireItemRemoved(MediaGalleryItem item) {
        for (IMediumGalleryListener l : m_listGalleryListeners) l.onItemRemoved(m_gallery, this, item);
    }

    public void paintControl(PaintEvent e) {
        if (!SWT.getPlatform().equals("gtk")) e.gc.setAdvanced(true);
        e.gc.setAntialias(SWT.ON);
        e.gc.setInterpolation(SWT.HIGH);
        int nItemWidth = 0;
        int nItemHeight = 0;
        for (MediaGalleryItem item : m_listItems) {
            if (item.getImage() == null) continue;
            Rectangle r = item.getImage().getBounds();
            nItemWidth = Math.max(r.width, nItemWidth);
            nItemHeight = Math.max(r.height, nItemHeight);
        }
        if (nItemWidth == 0) nItemWidth = DEFAULT_WIDTH; else nItemWidth += +2 * MARGIN_HORZ_IMAGE;
        int nFontHeight = e.gc.textExtent("AnyString").y;
        nItemHeight += nFontHeight + 2 * MARGIN_VERT_IMAGE;
        Rectangle rectBounds = m_cmpGroup.getBounds();
        int x = MARGIN_HORZ;
        int y = MARGIN_VERT;
        m_nItemsPerRow = 0;
        int n = 0;
        for (MediaGalleryItem item : m_listItems) {
            int nNewX = x + nItemWidth + MARGIN_HORZ;
            if (nNewX > rectBounds.width) {
                x = MARGIN_HORZ;
                nNewX = x + nItemWidth + MARGIN_HORZ;
                y += nItemHeight + MARGIN_VERT;
                if (m_nItemsPerRow == 0) m_nItemsPerRow = n;
            }
            item.setBounds(x, y, nItemWidth, nItemHeight);
            item.draw(e.gc, x, y, nItemWidth, nItemHeight, nFontHeight, m_colForeground, m_colBackground, m_colSelectionForeground, m_colSelectionBackground);
            x = nNewX;
            n++;
        }
        if (m_nItemsPerRow == 0) m_nItemsPerRow = m_listItems.size();
        int nNewHeight = y + nItemHeight + MARGIN_VERT;
        if (m_nGroupHeight != nNewHeight) {
            m_nGroupHeight = nNewHeight;
            Event event = new Event();
            event.type = SWT.Resize;
            event.widget = m_cmpGroup;
            m_cmpGroup.notifyListeners(SWT.Resize, event);
        }
    }

    public void mouseDoubleClick(MouseEvent e) {
        if (m_listSelectionListeners.size() == 0) return;
        for (MediaGalleryItem item : m_listItems) {
            if (item.getBounds().contains(e.x, e.y)) for (IMediumGallerySelectionListener l : m_listSelectionListeners) l.onItemDoubleClicked(item, e.x, e.y);
        }
    }

    public void mouseDown(MouseEvent e) {
        if (e.button != 1) return;
        boolean bSelectAdd = (e.stateMask & SWT.CONTROL) != 0;
        for (MediaGalleryItem item : m_listItems) {
            boolean bMouseInItem = item.getBounds().contains(e.x, e.y);
            boolean bSelected = item.isSelected();
            item.setSelected(bSelectAdd ? (bSelected ^ bMouseInItem) : bMouseInItem);
            if (bSelected != item.isSelected()) {
                Rectangle r = item.getBounds();
                m_cmpGroup.redraw(r.x, r.y, r.width, r.height, false);
                for (IMediumGallerySelectionListener l : m_listSelectionListeners) l.onItemSelected(item, bSelected, item.isSelected());
            }
        }
        if (!bSelectAdd) for (MediaGalleryGroup g : m_gallery.getGroups()) if (g != this) g.selectAll(false);
    }

    public void mouseUp(MouseEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        for (MediaGalleryGroup g : m_gallery.getGroups()) if (g != this && g.getSelectedItemsCount() > 0) return;
        int nSelectedIndex = -1;
        int i = 0;
        for (MediaGalleryItem item : m_listItems) {
            if (item.isSelected()) {
                if (nSelectedIndex != -1) return;
                nSelectedIndex = i;
            }
            i++;
        }
        switch(e.keyCode) {
            case SWT.ARROW_LEFT:
                if (nSelectedIndex - 1 >= 0) {
                    m_listItems.get(nSelectedIndex - 1).setSelected(true);
                    m_listItems.get(nSelectedIndex).setSelected(false);
                    m_cmpGroup.redraw();
                }
                break;
            case SWT.ARROW_RIGHT:
                if (nSelectedIndex + 1 < m_listItems.size()) {
                    m_listItems.get(nSelectedIndex + 1).setSelected(true);
                    m_listItems.get(nSelectedIndex).setSelected(false);
                    m_cmpGroup.redraw();
                }
                break;
            case SWT.ARROW_DOWN:
                if (nSelectedIndex + m_nItemsPerRow < m_listItems.size()) {
                    m_listItems.get(nSelectedIndex + m_nItemsPerRow).setSelected(true);
                    m_listItems.get(nSelectedIndex).setSelected(false);
                    m_cmpGroup.redraw();
                }
                break;
            case SWT.ARROW_UP:
                if (nSelectedIndex - m_nItemsPerRow >= 0) {
                    m_listItems.get(nSelectedIndex - m_nItemsPerRow).setSelected(true);
                    m_listItems.get(nSelectedIndex).setSelected(false);
                    m_cmpGroup.redraw();
                }
                break;
            case '\r':
            case '\n':
                if (m_listSelectionListeners.size() > 0) for (IMediumGallerySelectionListener l : m_listSelectionListeners) l.onItemDoubleClicked(m_listItems.get(nSelectedIndex), -1, -1);
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
    }
}
