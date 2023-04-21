package com.ibm.tuningfork.infra.stream;

import com.ibm.tuningfork.infra.data.Bookmark;
import com.ibm.tuningfork.infra.sharing.ISharingConvertibleCallback;
import com.ibm.tuningfork.infra.stream.core.BookmarkStream;
import com.ibm.tuningfork.infra.stream.expression.Operand;
import com.ibm.tuningfork.infra.stream.precise.EventStreamCursor;

/**
 * BookmarkStream that is a union of input BookmarkStreams
 */
public class UnionBookmarkStream extends BookmarkStream {

    protected final long startTime;

    protected final long endTime;

    protected final BookmarkStream[] inputs;

    public UnionBookmarkStream(BookmarkStream[] inputs) {
        this("Union of bookmarks", inputs, 0, Long.MAX_VALUE);
        makeInvisible();
    }

    public UnionBookmarkStream(String name, BookmarkStream[] inputs, long startTime, long endTime) {
        super(name, "Union", Operand.makeOperandList(inputs));
        this.inputs = inputs.clone();
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public void collectSpecificReconstructionArguments(ISharingConvertibleCallback cb) throws Exception {
        cb.convert(getName());
        cb.convert(inputs);
        cb.convert(startTime);
        cb.convert(endTime);
    }

    public void derivedRun() {
        EventStreamCursor[] cursors = new EventStreamCursor[inputs.length];
        Bookmark[] current = new Bookmark[inputs.length];
        boolean[] active = new boolean[inputs.length];
        int activeCount = 0;
        for (int i = 0; i < inputs.length; i++) {
            inputs[i].start();
            active[i] = true;
            activeCount++;
            cursors[i] = inputs[i].newCursor(startTime, endTime);
        }
        while (true) {
            for (int i = 0; i < inputs.length; i++) {
                if (active[i] && current[i] == null) {
                    current[i] = (Bookmark) cursors[i].getNext();
                    while (active[i] && current[i] == null) {
                        if (cursors[i].eof()) {
                            active[i] = false;
                            activeCount--;
                        } else {
                            modifyCheck();
                            cursors[i].blockForMore();
                            current[i] = (Bookmark) cursors[i].getNext();
                        }
                    }
                }
            }
            if (activeCount == 0) {
                close();
                return;
            }
            int minIndex = 0;
            long minTime = Long.MAX_VALUE;
            for (int i = 0; i < current.length; i++) {
                if (active[i]) {
                    long candTime = current[i].getTime();
                    if (candTime < minTime) {
                        minIndex = i;
                        minTime = candTime;
                    }
                }
            }
            addBookmark(current[minIndex]);
            current[minIndex] = null;
        }
    }
}
