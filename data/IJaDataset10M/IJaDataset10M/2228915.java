package net.sf.yajac.telnet;

import java.util.ArrayList;
import java.util.List;
import net.sf.yajac.TalkerException;

/**
 *
 * @author Mox Tryer
 */
class DisplayBuffer {

    /** Scroll up when inserting a line. */
    protected static final boolean SCROLL_UP = false;

    /** Scroll down when inserting a line. */
    protected static final boolean SCROLL_DOWN = true;

    private List<DisplayBufferRow> rows = new ArrayList<DisplayBufferRow>();

    private List<RowAction> rowActions = new ArrayList<RowAction>();

    private int height;

    private int width;

    private int bufSize;

    private int maxBufSize;

    private int screenBase;

    private int topMargin;

    private int bottomMargin;

    protected boolean connected = false;

    protected List<TerminalListener> terminalListeners = new ArrayList<TerminalListener>();

    /**
     * Get amount of rows on the screen.
     */
    public int getRows() {
        return height;
    }

    /**
     * Get amount of columns on the screen.
     */
    public int getColumns() {
        return width;
    }

    private int checkBounds(int value, int lower, int upper) {
        if (value < lower) {
            return lower;
        }
        if (value > upper) {
            return upper;
        }
        return value;
    }

    private void removeRow(int i) {
        rows.remove(i);
        rowActions.add(new RowAction(RowActionType.DELETED, i));
    }

    private void addRow(int i, int w) {
        addRowImpl(i, new DisplayBufferRow(w));
    }

    private void addRow(int i, DisplayBufferRow row) {
        row.setModified(true);
        addRowImpl(i, row);
    }

    private void addRowImpl(int i, DisplayBufferRow row) {
        rows.add(i, row);
        rowActions.add(new RowAction(RowActionType.INSERTED, i));
    }

    /**
     * Delete a rectangular portion of the screen.
     * You need to call redraw() to update the screen.
     * @param c x-coordinate (column)
     * @param l y-coordinate (row)
     * @param w with of the area in characters
     * @param h height of the area in characters
     * @see #deleteChar
     * @see #deleteLine
     */
    public synchronized void deleteArea(int c, int l, int w, int h) {
        c = checkBounds(c, 0, width - 1);
        l = checkBounds(l, 0, height - 1);
        for (int i = 0; i < h && l + i < height; i++) {
            rows.get(screenBase + l + i).clear(c, w);
        }
    }

    public synchronized void deleteArea(int c, int l, int w, int h, int curAttr) {
        c = checkBounds(c, 0, width - 1);
        l = checkBounds(l, 0, height - 1);
        for (int i = 0; i < h && l + i < height; i++) {
            rows.get(screenBase + l + i).clear(c, w, curAttr);
        }
    }

    public synchronized void deleteChar(int c, int l) {
        c = checkBounds(c, 0, width - 1);
        l = checkBounds(l, 0, height - 1);
        rows.get(screenBase + l).deleteChar(c);
    }

    public synchronized void deleteLine(int l) {
        l = checkBounds(l, 0, height - 1);
        int bottom = (l > bottomMargin ? height - 1 : (l < topMargin ? topMargin : bottomMargin + 1));
        removeRow(screenBase + l);
        addRow(screenBase + bottom - 1, width);
    }

    public synchronized void insertChar(int c, int l, char ch, int attributes, InputBulk bulk) {
        c = checkBounds(c, 0, width - 1);
        l = checkBounds(l, 0, height - 1);
        rows.get(screenBase + l).insertChar(c, ch, attributes, bulk);
    }

    /**
     * Insert a blank line at a specific position.
     * The current line and all previous lines are scrolled one line up. The
     * top line is lost. You need to call redraw() to update the screen.
     * @param l the y-coordinate to insert the line
     * @see #deleteLine
     * @see #redraw
     */
    public void insertLine(int l) {
        insertLine(l, 1, SCROLL_UP);
    }

    /**
     * Insert blank lines at a specific position.
     * You need to call redraw() to update the screen
     * @param l the y-coordinate to insert the line
     * @param n amount of lines to be inserted
     * @see #deleteLine
     * @see #redraw
     */
    public void insertLine(int l, int n) {
        insertLine(l, n, SCROLL_UP);
    }

    /**
     * Insert a blank line at a specific position. Scroll text according to
     * the argument.
     * You need to call redraw() to update the screen
     * @param l the y-coordinate to insert the line
     * @param scrollDown scroll down
     * @see #deleteLine
     * @see #SCROLL_UP
     * @see #SCROLL_DOWN
     * @see #redraw
     */
    public void insertLine(int l, boolean scrollDown) {
        insertLine(l, 1, scrollDown);
    }

    public synchronized void insertLine(int l, int n, boolean scrollDown) {
        l = checkBounds(l, 0, height - 1);
        int oldBase = screenBase;
        if (l > bottomMargin) {
            return;
        }
        int top = (l < topMargin ? 0 : (l > bottomMargin ? (bottomMargin + 1 < height ? bottomMargin + 1 : height - 1) : topMargin));
        int bottom = (l > bottomMargin ? height - 1 : (l < topMargin ? (topMargin > 0 ? topMargin - 1 : 0) : bottomMargin));
        for (int i = 0; i < n; i++) {
            addRow(oldBase + l + i + (scrollDown ? 0 : 1), width);
        }
        if (scrollDown) {
            if (n > (bottom - top)) {
                n = (bottom - top);
            }
            for (int i = 0; i < n; i++) {
                removeRow(rows.size() - 1);
            }
        } else {
            try {
                if (n > (bottom - top) + 1) {
                    n = (bottom - top) + 1;
                }
                int forDelete = 0;
                if (bufSize < maxBufSize) {
                    if (bufSize + n > maxBufSize) {
                        forDelete = bufSize + n - maxBufSize;
                        bufSize = maxBufSize;
                        screenBase = maxBufSize - height - 1;
                    } else {
                        screenBase += n;
                        bufSize += n;
                    }
                } else {
                    forDelete = bufSize + n - maxBufSize;
                }
                if (top > 0) {
                    for (int i = 0; i < n; i++) {
                        DisplayBufferRow row = rows.get(oldBase + top + i);
                        removeRow(oldBase + top + i);
                        addRow(oldBase, row);
                    }
                }
                for (int i = 0; i < forDelete; i++) {
                    removeRow(0);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("*** Error while scrolling up:");
                System.err.println("--- BEGIN STACK TRACE ---");
                e.printStackTrace();
                System.err.println("--- END STACK TRACE ---");
                System.err.println("bufSize=" + bufSize + ", maxBufSize=" + maxBufSize);
                System.err.println("top=" + top + ", bottom=" + bottom);
                System.err.println("n=" + n + ", l=" + l);
                System.err.println("screenBase=" + screenBase);
                System.err.println("oldBase=" + oldBase);
                System.err.println("size.width=" + width + ", size.height=" + height);
                System.err.println("rows.size=" + rows.size());
                System.err.println("*** done dumping debug information");
            }
        }
    }

    /**
     * Put a character on the screen with normal font and outline.
     * The character previously on that position will be overwritten.
     * You need to call redraw() to update the screen.
     * @param c x-coordinate (column)
     * @param l y-coordinate (line)
     * @param ch the character to show on the screen
     * @see #insertChar
     * @see #deleteChar
     * @see #redraw
     */
    public void putChar(int c, int l, char ch, InputBulk bulk) {
        putChar(c, l, ch, TerminalCharacter.NORMAL, bulk);
    }

    public synchronized void putChar(int c, int l, char ch, int attributes, InputBulk bulk) {
        c = checkBounds(c, 0, width - 1);
        l = checkBounds(l, 0, height - 1);
        rows.get(screenBase + l).putChar(c, ch, attributes, bulk);
    }

    /**
     * Put a String at a specific position. Any characters previously on that
     * position will be overwritten. You need to call redraw() for screen update.
     * @param c x-coordinate (column)
     * @param l y-coordinate (line)
     * @param s the string to be shown on the screen
     * @see #BOLD
     * @see #UNDERLINE
     * @see #INVERT
     * @see #INVISIBLE
     * @see #NORMAL
     * @see #LOW
     * @see #putChar(int, int, char)
     * @see #putChar(int, int, char, int)
     * @see #insertLine(int)
     * @see #insertLine(int, boolean)
     * @see #insertLine(int, int)
     * @see #insertLine(int, int, boolean)
     * @see #deleteLine
     * @see #redraw
     */
    public void putString(int c, int l, String s, InputBulk bulk) {
        putString(c, l, s, TerminalCharacter.NORMAL, bulk);
    }

    /**
     * Put a String at a specific position giving all characters the same
     * attributes. Any characters previously on that position will be
     * overwritten. You need to call redraw() to update the screen.
     * @param c x-coordinate (column)
     * @param l y-coordinate (line)
     * @param s the string to be shown on the screen
     * @param attributes character attributes
     * @see #BOLD
     * @see #UNDERLINE
     * @see #INVERT
     * @see #INVISIBLE
     * @see #NORMAL
     * @see #LOW
     * @see #putChar(int, int, char)
     * @see #putChar(int, int, char, int)
     * @see #insertLine(int)
     * @see #insertLine(int, boolean)
     * @see #insertLine(int, int)
     * @see #insertLine(int, int, boolean)
     * @see #deleteLine
     * @see #redraw
     */
    public void putString(int c, int l, String s, int attributes, InputBulk bulk) {
        for (int i = 0; i < s.length() && c + i < width; i++) {
            putChar(c + i, l, s.charAt(i), attributes, bulk);
        }
    }

    public synchronized void setScreenSize(int w, int h, boolean broadcast) throws TalkerException {
        int bsize = bufSize;
        if (w < 1 || h < 1) {
            return;
        }
        if (h > maxBufSize) {
            maxBufSize = h;
        }
        if (h > bufSize) {
            bufSize = h;
            screenBase = 0;
        }
        if (screenBase + h >= bufSize) {
            screenBase = bufSize - h;
        }
        if (bufSize > rows.size()) {
            int forInsert = bufSize - rows.size();
            for (int i = 0; i < forInsert; i++) {
                addRow(0, w);
            }
        } else {
            int forDelete = rows.size() - bufSize;
            for (int i = 0; i < forDelete; i++) {
                removeRow(0);
            }
        }
        for (DisplayBufferRow row : rows) {
            row.changeWidth(w);
        }
        width = w;
        height = h;
        topMargin = 0;
        bottomMargin = h - 1;
    }

    public synchronized void setBufferSize(int amount) {
        if (amount < height) {
            amount = height;
        }
        if (amount < maxBufSize) {
            if (amount < bufSize) {
                int forDelete = bufSize - amount;
                for (int i = 0; i < forDelete; i++) {
                    removeRow(0);
                }
            } else {
                int forInsert = amount - bufSize;
                for (int i = 0; i < forInsert; i++) {
                    addRow(0, width);
                }
            }
            int copyCount = bufSize - amount < 0 ? bufSize : amount;
            bufSize = copyCount;
            screenBase = bufSize - height;
        }
        maxBufSize = amount;
        redraw();
    }

    public List<DisplayBufferRow> getTerminalRows() {
        return rows;
    }

    public int getScreenBase() {
        return screenBase;
    }

    protected int getHeight() {
        return height;
    }

    protected int getWidth() {
        return width;
    }

    protected int getBufSize() {
        return bufSize;
    }

    protected int getMaxBufSize() {
        return maxBufSize;
    }

    public void setTopMargin(int l) {
        if (l > bottomMargin) {
            topMargin = bottomMargin;
            bottomMargin = l;
        } else {
            topMargin = l;
        }
        if (topMargin < 0) {
            topMargin = 0;
        }
        if (bottomMargin > height - 1) {
            bottomMargin = height - 1;
        }
    }

    public int getTopMargin() {
        return topMargin;
    }

    public void setBottomMargin(int l) {
        if (l == 0) {
            bottomMargin = height - 1;
        } else if (l < topMargin) {
            bottomMargin = topMargin;
            topMargin = l;
        } else {
            bottomMargin = l;
        }
        if (topMargin < 0) {
            topMargin = 0;
        }
        if (bottomMargin > height - 1) {
            bottomMargin = height - 1;
        }
    }

    public int getBottomMargin() {
        return bottomMargin;
    }

    public void addTerminalListener(TerminalListener l) {
        terminalListeners.add(l);
    }

    public void removeTerminalListener(TerminalListener l) {
        terminalListeners.remove(l);
    }

    /**
     * Trigger a redraw on the display.
     */
    protected void redraw() {
        if (connected) {
            TerminalUpdate terminalUpdate = getTerminalUpdate();
            for (TerminalListener l : terminalListeners) {
                l.sendUpdate(terminalUpdate);
            }
        }
    }

    public synchronized TerminalUpdate getTerminalUpdate() {
        return getTerminalUpdate(true);
    }

    public synchronized TerminalUpdate getTerminalUpdate(boolean clean) {
        List<UpdateRow> updateRows = new ArrayList<UpdateRow>();
        int i = 0;
        for (DisplayBufferRow row : rows) {
            if (row.isModified()) {
                updateRows.add(new UpdateRow(i, row));
                if (clean) {
                    row.setModified(false);
                }
            }
            i++;
        }
        TerminalUpdate update = new TerminalUpdate(new ArrayList<RowAction>(rowActions), updateRows, screenBase);
        if (clean) {
            rowActions.clear();
        }
        return update;
    }
}
