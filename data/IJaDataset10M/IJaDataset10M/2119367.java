package com.sshtools.sshterm.emulation;

import javax.swing.event.EventListenerList;

public class VDUBuffer {

    public static final int debug = 0;

    public static final boolean SCROLL_UP = false;

    public static final boolean SCROLL_DOWN = true;

    public static final int NORMAL = 0x00;

    public static final int BOLD = 0x01;

    public static final int UNDERLINE = 0x02;

    public static final int INVERT = 0x04;

    public static final int LOW = 0x08;

    public static final int COLOR = 0xff0;

    public static final int COLOR_FG = 0xf0;

    public static final int COLOR_BG = 0xf00;

    private EventListenerList listenerList = new EventListenerList();

    public int height;

    public int width;

    public boolean[] update;

    public char[][] charArray;

    public int[][] charAttributes;

    public int bufSize;

    public int maxBufSize;

    public int screenBase;

    public int windowBase;

    public int scrollMarker;

    private int topMargin;

    private int bottomMargin;

    protected boolean showcursor = true;

    protected int cursorX;

    protected int cursorY;

    protected VDUDisplay display;

    public VDUBuffer(int width, int height) {
        setScreenSize(width, height);
    }

    public VDUBuffer() {
        this(80, 24);
    }

    public void addTerminalListener(TerminalListener l) {
        listenerList.add(TerminalListener.class, l);
    }

    public void removeTerminalListener(TerminalListener l) {
        listenerList.remove(TerminalListener.class, l);
    }

    public void putChar(int c, int l, char ch) {
        putChar(c, l, ch, NORMAL);
    }

    public void putChar(int c, int l, char ch, int attributes) {
        c = checkBounds(c, 0, width - 1);
        l = checkBounds(l, 0, height - 1);
        charArray[screenBase + l][c] = ch;
        charAttributes[screenBase + l][c] = attributes;
        markLine(l, 1);
    }

    public char getChar(int c, int l) {
        c = checkBounds(c, 0, width - 1);
        l = checkBounds(l, 0, height - 1);
        return charArray[screenBase + l][c];
    }

    public String getLine(int l) {
        l = checkBounds(l, 0, height - 1);
        return new String(charArray[screenBase + l]);
    }

    public int getAttributes(int c, int l) {
        c = checkBounds(c, 0, width - 1);
        l = checkBounds(l, 0, height - 1);
        return charAttributes[screenBase + l][c];
    }

    public void insertChar(int c, int l, char ch, int attributes) {
        c = checkBounds(c, 0, width - 1);
        l = checkBounds(l, 0, height - 1);
        System.arraycopy(charArray[screenBase + l], c, charArray[screenBase + l], c + 1, width - c - 1);
        System.arraycopy(charAttributes[screenBase + l], c, charAttributes[screenBase + l], c + 1, width - c - 1);
        putChar(c, l, ch, attributes);
    }

    public void deleteChar(int c, int l) {
        c = checkBounds(c, 0, width - 1);
        l = checkBounds(l, 0, height - 1);
        if (c < (width - 1)) {
            System.arraycopy(charArray[screenBase + l], c + 1, charArray[screenBase + l], c, width - c - 1);
            System.arraycopy(charAttributes[screenBase + l], c + 1, charAttributes[screenBase + l], c, width - c - 1);
        }
        putChar(width - 1, l, (char) 0);
    }

    public void putString(int c, int l, String s) {
        putString(c, l, s, NORMAL);
    }

    public void putString(int c, int l, String s, int attributes) {
        for (int i = 0; (i < s.length()) && ((c + i) < width); i++) {
            putChar(c + i, l, s.charAt(i), attributes);
        }
    }

    public void insertLine(int l) {
        insertLine(l, 1, SCROLL_UP);
    }

    public void insertLine(int l, int n) {
        insertLine(l, n, SCROLL_UP);
    }

    public void insertLine(int l, boolean scrollDown) {
        insertLine(l, 1, scrollDown);
    }

    public synchronized void insertLine(int l, int n, boolean scrollDown) {
        l = checkBounds(l, 0, height - 1);
        char[][] cbuf = null;
        int[][] abuf = null;
        int offset = 0;
        int oldBase = screenBase;
        if (l > bottomMargin) {
            return;
        }
        int top = ((l < topMargin) ? 0 : ((l > bottomMargin) ? (((bottomMargin + 1) < height) ? (bottomMargin + 1) : (height - 1)) : topMargin));
        int bottom = ((l > bottomMargin) ? (height - 1) : ((l < topMargin) ? ((topMargin > 0) ? (topMargin - 1) : 0) : bottomMargin));
        if (scrollDown) {
            if (n > (bottom - top)) {
                n = (bottom - top);
            }
            cbuf = new char[bottom - l - (n - 1)][width];
            abuf = new int[bottom - l - (n - 1)][width];
            System.arraycopy(charArray, oldBase + l, cbuf, 0, bottom - l - (n - 1));
            System.arraycopy(charAttributes, oldBase + l, abuf, 0, bottom - l - (n - 1));
            System.arraycopy(cbuf, 0, charArray, oldBase + l + n, bottom - l - (n - 1));
            System.arraycopy(abuf, 0, charAttributes, oldBase + l + n, bottom - l - (n - 1));
            cbuf = charArray;
            abuf = charAttributes;
        } else {
            try {
                if (n > ((bottom - top) + 1)) {
                    n = (bottom - top) + 1;
                }
                if (bufSize < maxBufSize) {
                    if ((bufSize + n) > maxBufSize) {
                        offset = n - (maxBufSize - bufSize);
                        scrollMarker += offset;
                        bufSize = maxBufSize;
                        screenBase = maxBufSize - height - 1;
                        windowBase = screenBase;
                    } else {
                        scrollMarker += n;
                        screenBase += n;
                        windowBase += n;
                        bufSize += n;
                    }
                    cbuf = new char[bufSize][width];
                    abuf = new int[bufSize][width];
                } else {
                    offset = n;
                    cbuf = charArray;
                    abuf = charAttributes;
                }
                if (oldBase > 0) {
                    System.arraycopy(charArray, offset, cbuf, 0, oldBase - offset);
                    System.arraycopy(charAttributes, offset, abuf, 0, oldBase - offset);
                }
                if (top > 0) {
                    System.arraycopy(charArray, oldBase, cbuf, screenBase, top);
                    System.arraycopy(charAttributes, oldBase, abuf, screenBase, top);
                }
                if (oldBase > 0) {
                    System.arraycopy(charArray, oldBase + top, cbuf, oldBase - offset, n);
                    System.arraycopy(charAttributes, oldBase + top, abuf, oldBase - offset, n);
                }
                System.arraycopy(charArray, oldBase + top + n, cbuf, screenBase + top, l - top - (n - 1));
                System.arraycopy(charAttributes, oldBase + top + n, abuf, screenBase + top, l - top - (n - 1));
                if (l < (height - 1)) {
                    System.arraycopy(charArray, oldBase + l + 1, cbuf, screenBase + l + 1, (height - 1) - l);
                    System.arraycopy(charAttributes, oldBase + l + 1, abuf, screenBase + l + 1, (height - 1) - l);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("*** Error while scrolling up:");
                System.err.println("--- BEGIN STACK TRACE ---");
                e.printStackTrace();
                System.err.println("--- END STACK TRACE ---");
                System.err.println("bufSize=" + bufSize + ", maxBufSize=" + maxBufSize);
                System.err.println("top=" + top + ", bottom=" + bottom);
                System.err.println("n=" + n + ", l=" + l);
                System.err.println("screenBase=" + screenBase + ", windowBase=" + windowBase);
                System.err.println("oldBase=" + oldBase);
                System.err.println("size.width=" + width + ", size.height=" + height);
                System.err.println("abuf.length=" + abuf.length + ", cbuf.length=" + cbuf.length);
                System.err.println("*** done dumping debug information");
            }
        }
        scrollMarker -= n;
        for (int i = 0; i < n; i++) {
            cbuf[(screenBase + l) + (scrollDown ? i : (-i))] = new char[width];
            abuf[(screenBase + l) + (scrollDown ? i : (-i))] = new int[width];
        }
        charArray = cbuf;
        charAttributes = abuf;
        if (scrollDown) {
            markLine(l, bottom - l + 1);
        } else {
            markLine(top, l - top + 1);
        }
        if ((display != null) && (display.getScrollBar() != null)) {
            display.getScrollBar().setValues(windowBase, height, 0, bufSize - 1);
        }
    }

    public void deleteLine(int l) {
        l = checkBounds(l, 0, height - 1);
        int bottom = ((l > bottomMargin) ? (height - 1) : ((l < topMargin) ? topMargin : (bottomMargin + 1)));
        System.arraycopy(charArray, screenBase + l + 1, charArray, screenBase + l, bottom - l - 1);
        System.arraycopy(charAttributes, screenBase + l + 1, charAttributes, screenBase + l, bottom - l - 1);
        charArray[(screenBase + bottom) - 1] = new char[width];
        charAttributes[(screenBase + bottom) - 1] = new int[width];
        markLine(l, bottom - l);
    }

    public void deleteArea(int c, int l, int w, int h, int curAttr) {
        c = checkBounds(c, 0, width - 1);
        l = checkBounds(l, 0, height - 1);
        char[] cbuf = new char[w];
        int[] abuf = new int[w];
        for (int i = 0; i < w; i++) {
            abuf[i] = curAttr;
        }
        for (int i = 0; (i < h) && ((l + i) < height); i++) {
            System.arraycopy(cbuf, 0, charArray[screenBase + l + i], c, w);
            System.arraycopy(abuf, 0, charAttributes[screenBase + l + i], c, w);
        }
        markLine(l, h);
    }

    public void deleteArea(int c, int l, int w, int h) {
        c = checkBounds(c, 0, width - 1);
        l = checkBounds(l, 0, height - 1);
        char[] cbuf = new char[w];
        int[] abuf = new int[w];
        for (int i = 0; (i < h) && ((l + i) < height); i++) {
            System.arraycopy(cbuf, 0, charArray[screenBase + l + i], c, w);
            System.arraycopy(abuf, 0, charAttributes[screenBase + l + i], c, w);
        }
        markLine(l, h);
    }

    public void showCursor(boolean doshow) {
        if (doshow != showcursor) {
            markLine(cursorY, 1);
        }
        showcursor = doshow;
    }

    public void setCursorPosition(int c, int l) {
        cursorX = checkBounds(c, 0, width - 1);
        cursorY = checkBounds(l, 0, height - 1);
        markLine(cursorY, 1);
    }

    public int getCursorColumn() {
        return cursorX;
    }

    public int getCursorRow() {
        return cursorY;
    }

    public void setWindowBase(int line) {
        if (line > screenBase) {
            line = screenBase;
        } else if (line < 0) {
            line = 0;
        }
        windowBase = line;
        update[0] = true;
        redraw();
    }

    public int getWindowBase() {
        return windowBase;
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
        if (bottomMargin > (height - 1)) {
            bottomMargin = height - 1;
        }
    }

    public int getTopMargin() {
        return topMargin;
    }

    public void setBottomMargin(int l) {
        if (l < topMargin) {
            bottomMargin = topMargin;
            topMargin = l;
        } else {
            bottomMargin = l;
        }
        if (topMargin < 0) {
            topMargin = 0;
        }
        if (bottomMargin > (height - 1)) {
            bottomMargin = height - 1;
        }
    }

    public int getBottomMargin() {
        return bottomMargin;
    }

    public void setBufferSize(int amount) {
        if (amount < height) {
            amount = height;
        }
        if (amount < maxBufSize) {
            char[][] cbuf = new char[amount][width];
            int[][] abuf = new int[amount][width];
            int copyStart = ((bufSize - amount) < 0) ? 0 : (bufSize - amount);
            int copyCount = ((bufSize - amount) < 0) ? bufSize : amount;
            if (charArray != null) {
                System.arraycopy(charArray, copyStart, cbuf, 0, copyCount);
            }
            if (charAttributes != null) {
                System.arraycopy(charAttributes, copyStart, abuf, 0, copyCount);
            }
            charArray = cbuf;
            charAttributes = abuf;
            bufSize = copyCount;
            screenBase = bufSize - height;
            windowBase = screenBase;
        }
        maxBufSize = amount;
        update[0] = true;
        redraw();
    }

    public int getBufferSize() {
        return bufSize;
    }

    public int getMaxBufferSize() {
        return maxBufSize;
    }

    public void setScreenSize(int w, int h) {
        char[][] cbuf;
        int[][] abuf;
        int bsize = bufSize;
        int oldHeight = height;
        if ((w < 1) || (h < 1)) {
            return;
        }
        if (debug > 0) {
            System.err.println("VDU: screen size [" + w + "," + h + "]");
        }
        if (h > maxBufSize) {
            maxBufSize = h;
        }
        if (h > bufSize) {
            bufSize = h;
            screenBase = 0;
            windowBase = 0;
        }
        if ((windowBase + h) >= bufSize) {
            windowBase = bufSize - h;
        }
        if ((screenBase + h) >= bufSize) {
            screenBase = bufSize - h;
        }
        cbuf = new char[bufSize][w];
        abuf = new int[bufSize][w];
        if ((charArray != null) && (charAttributes != null)) {
            for (int i = 0; (i < bsize) && (i < bufSize); i++) {
                System.arraycopy(charArray[i], 0, cbuf[i], 0, (w < width) ? w : width);
                System.arraycopy(charAttributes[i], 0, abuf[i], 0, (w < width) ? w : width);
            }
        }
        charArray = cbuf;
        charAttributes = abuf;
        width = w;
        height = h;
        topMargin = 0;
        bottomMargin = h - 1;
        update = new boolean[h + 1];
        update[0] = true;
        Object[] l = listenerList.getListeners(TerminalListener.class);
        for (int i = l.length - 1; i >= 0; i--) {
            ((TerminalListener) l[i]).screenResized(w, h);
        }
        if ((display != null) && (display.getScrollBar() != null)) {
            display.getScrollBar().setValue(bufSize - 1);
        }
        if (height > oldHeight) {
            cursorY += (height - oldHeight);
        }
    }

    public int getRows() {
        return height;
    }

    public int getColumns() {
        return width;
    }

    public void markLine(int l, int n) {
        l = checkBounds(l, 0, height - 1);
        for (int i = 0; (i < n) && ((l + i) < height); i++) {
            update[l + i + 1] = true;
        }
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

    public void setDisplay(VDUDisplay display) {
        this.display = display;
    }

    protected void redraw() {
        if (display != null) {
            display.checkedClearSelection();
            display.redraw();
        }
    }
}
