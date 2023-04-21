package org.jcvi.util;

import org.jcvi.common.util.Range;

public interface IndexedFileRange {

    Range getRangeFor(String id);

    boolean contains(String id);

    void put(String id, Range range);

    void remove(String id);

    boolean isClosed();

    void close();

    int size();

    CloseableIterator<String> getIds();
}
