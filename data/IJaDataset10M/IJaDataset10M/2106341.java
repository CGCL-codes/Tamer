package net.sf.beezle.mork.scanner;

import java.io.IOException;
import java.io.Reader;

public class Pages {

    private final int pageSize;

    private Reader src;

    /** Index of the last page */
    private int lastNo;

    /** Number of read bytes on the last page */
    private int lastFilled;

    /**
     * Stores character data that has been read from the underlying Reader.
     *
     * Invariant: pages.length > 0 && i: 0..lastNo: (pages[i] != null && pages.get(i).length == PAGE_SIZE).
     * if lastNo + 1 < pages.length, pages[lastNo + 1] may be != null to re-use previously allocated bytes.
     */
    private char[][] pages;

    public Pages(int pageSize) {
        if (pageSize == 0) {
            throw new IllegalArgumentException();
        }
        this.pageSize = pageSize;
        this.pages = new char[2][];
        this.pages[0] = new char[pageSize];
    }

    public void open(Reader src) {
        this.src = src;
        this.lastFilled = 0;
        this.lastNo = 0;
    }

    public char[] get(int no) {
        return pages[no];
    }

    /** @return number of bytes filled on the specified page */
    public int getFilled(int no) {
        return no == lastNo ? lastFilled : pageSize;
    }

    /** number of pages used. */
    public int getLastNo() {
        return lastNo;
    }

    public int getSize() {
        return pageSize * lastNo + lastFilled;
    }

    /**
     * @return -1  eof   0: current page grown   1: new page
     */
    public int read(int pageNo, int pageUsed) throws IOException {
        if (pageUsed < pageSize) {
            if (pageNo != lastNo) {
                throw new IllegalStateException(pageNo + " vs " + lastNo);
            }
            return fillLast() ? 0 : -1;
        } else {
            if (pageNo == lastNo) {
                grow();
            }
            if (getFilled(pageNo + 1) == 0) {
                if (!fillLast()) {
                    return -1;
                }
            }
            return 1;
        }
    }

    /**
     * Reads bytes to fill the last page.
     * @return false for eof
     */
    private boolean fillLast() throws IOException {
        int count;
        if (lastFilled == pageSize) {
            throw new IllegalStateException();
        }
        count = src.read(pages[lastNo], lastFilled, pageSize - lastFilled);
        if (count <= 0) {
            if (count == 0) {
                throw new IllegalStateException();
            }
            return false;
        }
        lastFilled += count;
        return true;
    }

    /** Adds a page at the end */
    private void grow() {
        char[][] newPages;
        if (lastFilled != pageSize) {
            throw new IllegalStateException();
        }
        lastNo++;
        if (lastNo >= pages.length) {
            newPages = new char[lastNo * 5 / 2][];
            System.arraycopy(pages, 0, newPages, 0, lastNo);
            pages = newPages;
        }
        if (pages[lastNo] == null) {
            pages[lastNo] = new char[pageSize];
        }
        lastFilled = 0;
    }

    /** Remove count pages from the beginning */
    public void shrink(int count) {
        char[] keepAllocated;
        if (count == 0) {
            throw new IllegalArgumentException();
        }
        if (count > lastNo) {
            throw new IllegalArgumentException(count + " vs " + lastNo);
        }
        lastNo -= count;
        keepAllocated = pages[0];
        System.arraycopy(pages, count, pages, 0, lastNo + 1);
        pages[lastNo + 1] = keepAllocated;
        for (int i = lastNo + 2; i < pages.length; i++) {
            pages[i] = null;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder;
        int i, p;
        char[] pg;
        builder = new StringBuilder();
        builder.append("pages {");
        for (p = 0; p <= lastNo; p++) {
            pg = get(p);
            builder.append("\n  page " + p + ":");
            for (i = 0; i < pageSize; i++) {
                builder.append(pg[i]);
            }
            builder.append("\n    ");
            for (i = 0; i < pageSize; i++) {
                builder.append(" " + ((int) pg[i]));
            }
        }
        builder.append("\n}");
        return builder.toString();
    }
}
