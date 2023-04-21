package org.pigeons.tivo;

import java.util.Enumeration;
import java.util.Vector;
import com.tivo.hme.bananas.BHighlight;
import com.tivo.hme.bananas.BHighlights;
import com.tivo.hme.bananas.BRect;
import com.tivo.hme.bananas.BScreen;
import com.tivo.hme.bananas.BView;
import com.tivo.hme.bananas.IHighlightsLayout;
import com.tivo.hme.sdk.Resource;

/**
 * Based on Adam Doppelt's BList with generic parameterization and 
 * additional set() method that does not try to update focus 
 * 
 * @author      Matthew H Damiano
 */
public abstract class MyBList<T> extends BView {

    /**
	 * The animation used to move the highlight.
	 */
    public static final String ANIM = "*100";

    /**
	 * Array of elements.
	 */
    private Vector<T> elements_;

    /**
	 * Array of row views.
	 */
    private Vector<BView> rows_;

    /**
	 * Height of each row.
	 */
    private int rowHeight_;

    /**
	 * Number of visible rows.
	 */
    private int nVisibleRows_;

    /**
	 * A set of highlights to be shared across all rows.
	 */
    private BHighlights rowHighlights_;

    /**
	 * The currently focused row.
	 */
    private int focused_;

    /**
	 * The row that is currently being drawn at the top of the list.
	 */
    private int top_;

    /**
	 * The dirty value is used to track which portion of the list is in need of
	 * updating. It's interpretation is similar to top. Refresh uses the union
	 * of the top/dirty windows to determine which rows require refreshing.
	 */
    private int dirty_;

    /**
	 * If true, animate the next refresh.
	 */
    private boolean animate_;

    /**
	 * Creates a new BList instance. To avoid drawing partial rows, the list
	 * height should be a multiple of the rowHeight.
	 *
	 * @param parent parent
	 * @param x x
	 * @param y y
	 * @param width width
	 * @param height height
	 * @param rowHeight the height of each row contained in the list.
	 */
    public MyBList(BView parent, int x, int y, int width, int height, int rowHeight) {
        this(parent, x, y, width, height, rowHeight, true);
    }

    /**
	 * Creates a new BList instance. To avoid drawing partial rows, the list
	 * height should be a multiple of the rowHeight.
	 *
	 * @param parent parent
	 * @param x x
	 * @param y y
	 * @param width width
	 * @param height height
	 * @param rowHeight the height of each row contained in the list.
	 * @param visible if true, make the view visibile
	 */
    public MyBList(BView parent, int x, int y, int width, int height, int rowHeight, boolean visible) {
        super(parent, x, y, width, height, visible);
        this.elements_ = new Vector<T>();
        this.rows_ = new Vector<BView>();
        this.rowHeight_ = rowHeight;
        this.nVisibleRows_ = height / rowHeight;
        this.rowHighlights_ = new BHighlights(new Layout());
        clear();
    }

    public BHighlights getRowHighlights() {
        return rowHighlights_;
    }

    public void setBarAndArrows(int bar_left, int bar_right, Object action_left, Object action_right) {
        getRowHighlights().setBarAndArrows(bar_left, bar_right, action_left, action_right, true);
    }

    public int getTop() {
        return top_;
    }

    public int getNVisibleRows() {
        return nVisibleRows_;
    }

    /**
	 * Create a row for the given element. For example, you could create a view
	 * with the given parent and then set its resource to be a text resource
	 * based on the element at index.
	 *
	 * @param parent use this as the parent for your new view
	 * @param index the index for the row
	 * @return the new row
	 */
    protected abstract void createRow(BView parent, int index);

    /**
	 * Get the row height.
	 * 
	 * @return row height
	 */
    public int getRowHeight() {
        return rowHeight_;
    }

    /**
	 * Get the index of the currently focused row.
	 *
	 * @return the index of the focused row
	 */
    public int getFocus() {
        return focused_;
    }

    /**
	 * Set the focus to a particular row.
	 * 
	 * @param index the row to focus
	 * @param animate If true, animate the list as we move to the new row.
	 */
    public void setFocus(int index, boolean animate) {
        int size = size();
        if (size > 0) {
            this.animate_ = animate;
            focused_ = Math.min(Math.max(index, 0), size - 1);
            getScreen().setFocus(getRow(focused_));
            this.animate_ = false;
        }
    }

    /**
	 * Set the current top row. This doesn't take effect until the next
	 * refresh().
	 */
    public void setTop(int ntop) {
        setTop(ntop, focused_);
    }

    public void setTop(int ntop, int index) {
        dirty_ = top_;
        top_ = ntop;
        focused_ = Math.min(Math.max(index, 0), size() - 1);
    }

    /**
	 * If necessary, scrolls the list to show the currently focused row and
	 * creates new row views to wrap the elements that are currently visible.
	 */
    public void refresh() {
        BScreen screen = getScreen();
        Resource anim = animate_ ? getResource(ANIM) : null;
        animate_ = false;
        int size = size();
        screen.setPainting(false);
        try {
            top_ = Math.max(Math.min(top_, size - nVisibleRows_), 0);
            int max = Math.min(top_ + nVisibleRows_, size) - 1;
            if (focused_ < top_) {
                top_ = Math.max(0, focused_);
            } else if (focused_ > max) {
                int end = Math.min(focused_ + 1, size);
                top_ = Math.max(end - nVisibleRows_, 0);
            }
            int popMin = Math.max(top_ - nVisibleRows_, 0);
            int popMax = Math.min(top_ + 2 * nVisibleRows_, size());
            int fixMin, fixMax;
            if (dirty_ < top_) {
                fixMin = Math.max(dirty_ - nVisibleRows_, 0);
                fixMax = popMax;
            } else {
                fixMin = popMin;
                fixMax = Math.min(dirty_ + 2 * nVisibleRows_, size());
            }
            dirty_ = top_;
            for (int index = fixMin; index < fixMax; ++index) {
                if (index < popMin || index >= popMax) {
                    BView v = (BView) rows_.elementAt(index);
                    if (v != null) {
                        v.remove();
                        rows_.setElementAt(null, index);
                    }
                } else {
                    getRow(index);
                }
            }
            setTranslation(0, -top_ * rowHeight_, anim);
            BHighlights h = getHighlights();
            BHighlight pageup = h.get(H_PAGEUP);
            BHighlight pagedown = h.get(H_PAGEDOWN);
            if (pageup != null && pagedown != null) {
                pageup.setVisible((top_ > 0) ? H_VIS_TRUE : H_VIS_FALSE);
                pagedown.setVisible((top_ + nVisibleRows_ < rows_.size()) ? H_VIS_TRUE : H_VIS_FALSE);
                h.refresh(anim);
            }
            if (focused_ != -1) {
                h = getRowHighlights();
                BHighlight up = h.get(H_UP);
                BHighlight down = h.get(H_DOWN);
                if (up != null && down != null) {
                    up.setVisible((focused_ > 0) ? H_VIS_FOCUS : H_VIS_FALSE);
                    down.setVisible((focused_ < size - 1) ? H_VIS_FOCUS : H_VIS_FALSE);
                }
                rowHighlights_.refresh(anim);
            } else if (isAncestorOf(screen.getFocus())) {
                if (size > 0) {
                    setFocus(0, false);
                } else {
                    screen.setFocus(screen.getFocusDefault());
                }
            }
        } finally {
            screen.setPainting(true);
        }
    }

    /**
	 * Handle key presses. The list handles KEY_UP, KEY_DOWN, KEY_CHANNELUP and
	 * KEY_CHANNELDOWN by default.
	 */
    public boolean handleKeyPress(int code, long rawcode) {
        final int pagesize = nVisibleRows_ - 1;
        int newfocus = -1;
        int newtop = top_;
        switch(code) {
            case KEY_UP:
                newfocus = focused_ - 1;
                break;
            case KEY_DOWN:
                newfocus = focused_ + 1;
                break;
            case KEY_CHANNELUP:
                newfocus = top_;
                newtop = top_ - pagesize;
                break;
            case KEY_CHANNELDOWN:
                newfocus = top_ + pagesize;
                newtop = newfocus;
                break;
            default:
                return super.handleKeyPress(code, rawcode);
        }
        if (focused_ != -1) {
            int max = size() - 1;
            newfocus = Math.max(Math.min(newfocus, max), 0);
            if (newtop == top_) {
                if (newfocus < top_) {
                    newtop = newfocus - (pagesize - 1);
                } else if (newfocus > top_ + pagesize) {
                    newtop = newfocus - 1;
                }
            }
            newtop = Math.max(Math.min(newtop, max - pagesize), 0);
            if (newfocus == focused_ && newtop == top_) {
                return false;
            }
            getBApp().playSoundForKey(code, true, true);
            if (newtop != top_) {
                animate_ = true;
                setTop(newtop, newfocus);
                refresh();
            }
            setFocus(newfocus, true);
        }
        return true;
    }

    /**
	 * Handle key repeats for some keys.
	 */
    public boolean handleKeyRepeat(int code, long rawcode) {
        switch(code) {
            case KEY_UP:
            case KEY_DOWN:
            case KEY_CHANNELUP:
            case KEY_CHANNELDOWN:
                return handleKeyPress(code, rawcode);
        }
        return super.handleKeyRepeat(code, rawcode);
    }

    /**
	 * Handle focus movement.
	 */
    public boolean handleFocus(boolean isGained, BView gained, BView lost) {
        if (isGained) {
            if (gained == this) {
                setFocus(0, false);
            } else {
                focused_ = gained.getY() / rowHeight_;
                refresh();
            }
        } else if (gained.getParent() != this) {
            refresh();
        }
        return true;
    }

    /**
	 * Returns the number of elements in the list.
	 */
    public int size() {
        return elements_.size();
    }

    /**
	 * Returns true if the list contains element o.
	 */
    public boolean contains(Object o) {
        return elements_.contains(o);
    }

    /**
	 * Add an object to the end of the list.
	 */
    public void add(T o) {
        add(size(), o);
    }

    /**
	 * Add a group of objects to the end of the list.
	 */
    public void add(T a[]) {
        add(size(), a);
    }

    /**
	 * Add a group of objects to the end of the list.
	 */
    public void add(Vector<T> v) {
        add(size(), v);
    }

    /**
	 * Find an object in the list.
	 */
    public int indexOf(Object o) {
        return elements_.indexOf(o);
    }

    /**
	 * Find an object in the list, starting at the end.
	 */
    public int lastIndexOf(Object o) {
        return elements_.lastIndexOf(o);
    }

    /**
	 * Remove an object from the list. Returns true if the object was removed.
	 */
    public boolean remove(Object o) {
        int index = elements_.indexOf(o);
        if (index == -1) {
            return false;
        }
        remove(index);
        return true;
    }

    /**
	 * Clear the list.
	 */
    public void clear() {
        for (Enumeration<BView> e = rows_.elements(); e.hasMoreElements(); ) {
            BView v = e.nextElement();
            if (v != null) {
                v.remove();
            }
        }
        elements_.clear();
        rows_.clear();
        focused_ = -1;
        refresh();
    }

    /**
	 * Get an object from the list.
	 */
    public T get(int index) {
        return elements_.get(index);
    }

    /**
	 * Get a row at the specific index, creating it if necessary.
	 */
    public BView getRow(int index) {
        BView view = (BView) rows_.get(index);
        if (view == null) {
            view = new BView(this, 0, index * rowHeight_, getWidth(), rowHeight_);
            view.setFocusable(true);
            view.setHighlights(rowHighlights_);
            createRow(view, index);
            rows_.set(index, view);
        } else {
            view.setLocation(0, index * rowHeight_);
        }
        return view;
    }

    /**
	 * Set an object in the list.
	 * 
	 * @param index which element to change
	 * @param element the new element
	 */
    public T set(int index, T element) {
        return set(index, element, true);
    }

    /**
	 * Set an object in the list.
	 * 
	 * @param index which element to change
	 * @param element the new element
	 * @param fixFocus - true if focus should be set to the element
	 */
    public T set(int index, T element, boolean fixFocus) {
        T obj = elements_.set(index, element);
        BView v = (BView) rows_.set(index, null);
        if (v != null) {
            v.remove();
        }
        if (fixFocus && (v.hasFocus())) {
            getScreen().setFocus(getRow(index));
        }
        refresh();
        return obj;
    }

    /**
	 * Add an object at a particular index.
	 *
	 * @param index where to insert the new element
	 * @param element the new element
	 */
    public void add(int index, T element) {
        elements_.add(index, element);
        rows_.add(index, null);
        touch(index, 1);
    }

    /**
	 * Add a group of objects at a particular index.
	 *
	 * @param index where to insert the new elements
	 * @param a the new elements
	 */
    public void add(int index, T a[]) {
        for (int i = a.length; i-- > 0; ) {
            elements_.add(index, a[i]);
            rows_.add(index, null);
        }
        touch(index, a.length);
    }

    /**
	 * Add a group of objects at a particular index.
	 *
	 * @param index where to insert the new elements
	 * @param v the new elements
	 */
    public void add(int index, Vector<T> v) {
        for (int i = v.size(); i-- > 0; ) {
            elements_.add(index, v.elementAt(i));
            rows_.add(index, null);
        }
        touch(index, v.size());
    }

    /**
	 * Remove an element.
	 *
	 * @param index where to remove the element
	 */
    public Object remove(int index) {
        Object o = elements_.remove(index);
        BView v = (BView) rows_.remove(index);
        if (v != null) {
            v.remove();
        }
        touch(index, -1);
        return o;
    }

    /**
	 * A helper for adding/removing elements. This will update focused, top, and
	 * dirty. It will then call refresh.
	 *
	 * @param index the index where the change occurred
	 * @param len the number of items added (positive) or removed (negative)
	 */
    void touch(int index, int len) {
        if (index <= focused_) {
            focused_ += len;
        }
        if (len > 0) {
            if (index <= top_) {
                setTop(top_ + len);
            } else {
                dirty_ += len;
            }
        } else if (index < top_) {
            setTop(top_ + len);
        }
        refresh();
    }

    class Layout implements IHighlightsLayout {

        public BScreen getScreen() {
            return MyBList.this.getScreen();
        }

        public BRect getHighlightBounds() {
            BView row = getRow();
            if (row != null) {
                return row.getHighlightBounds();
            }
            return toScreenBounds(new BRect(0, 0, getWidth(), rowHeight_));
        }

        public boolean getHighlightIsVisible(int visible) {
            BView row = getRow();
            return (row != null) ? row.getHighlightIsVisible(visible) : false;
        }

        protected BView getRow() {
            return (focused_ != -1) ? (BView) rows_.elementAt(focused_) : null;
        }
    }
}
