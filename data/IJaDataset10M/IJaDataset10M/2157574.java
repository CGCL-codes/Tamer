package com.ibm.tuningfork.infra.stream.precise;

import com.ibm.tuningfork.infra.Logging;
import com.ibm.tuningfork.infra.data.ITimedData;
import com.ibm.tuningfork.infra.stream.core.IStreamCursor;
import com.ibm.tuningfork.infra.stream.core.Stream;

/**
 * An implementation of IStreamCursor for SampleStreams.
 */
public abstract class PreciseStreamCursor implements IStreamCursor {

    protected final Stream stream;

    protected final PreciseStream precise;

    public final long minTime, maxTime;

    protected long curIndex;

    protected final boolean forward;

    protected static int DONE = -2;

    public PreciseStreamCursor(Stream stream, PreciseStream precise, long startTime, long endTime) {
        this.stream = stream;
        this.precise = precise;
        forward = startTime <= endTime;
        this.minTime = Math.min(startTime, endTime);
        this.maxTime = Math.max(startTime, endTime);
        reset();
    }

    public final boolean hasMore() {
        if (curIndex == DONE) {
            return false;
        }
        return forward ? (curIndex < precise.length()) : (curIndex >= 0);
    }

    public boolean eof() {
        return stream.isClosed() && !hasMore();
    }

    public void blockForMore() {
        if (hasMore()) {
            return;
        }
        stream.waitForMore();
    }

    public void reset() {
        reset(forward ? minTime : maxTime);
    }

    protected boolean isTimeAtOrBeforeTimedData(long time, ITimedData timedData) {
        return time <= timedData.getTime();
    }

    protected boolean isTimeAtOrAfterTimedData(long time, ITimedData timedData) {
        return time >= timedData.getTime();
    }

    public void reset(long targetTime) {
        curIndex = forward ? precise.findNearestIndexBefore(targetTime) : precise.findNearestIndexAfter(targetTime);
        while (true) {
            ITimedData cur = relativePeekRaw(0);
            if (cur == null) {
                break;
            }
            long time = cur.getTime();
            if (forward) {
                if (time >= maxTime) {
                    curIndex = DONE;
                    break;
                }
                if (time >= targetTime) {
                    break;
                }
            } else {
                if (time <= minTime) {
                    curIndex = DONE;
                    break;
                }
                if (time <= targetTime) {
                    break;
                }
            }
            advance();
        }
    }

    protected final void advance() {
        if (curIndex == DONE) {
            return;
        }
        if (forward) {
            curIndex++;
            if (curIndex < precise.length()) {
                ITimedData curData = precise.getData(curIndex);
                if (curData != null && curData.follows(maxTime)) {
                    curIndex = DONE;
                    return;
                }
            }
        } else {
            curIndex--;
            if (curIndex < 0) {
                curIndex = DONE;
            }
        }
    }

    protected final ITimedData relativePeekRaw(long delta) {
        if (curIndex == DONE) {
            return null;
        }
        long newIndex = curIndex + delta;
        if (newIndex >= 0 && newIndex < precise.length()) {
            return precise.getData(newIndex);
        }
        return null;
    }

    protected final ITimedData getNextRaw() {
        if (curIndex == DONE) {
            return null;
        }
        if (curIndex < precise.length()) {
            ITimedData result = precise.getData(curIndex);
            advance();
            return result;
        }
        return null;
    }

    public void moveToEnd() {
        if (forward) {
            curIndex = precise.length();
        } else {
            curIndex = -1;
        }
    }

    public String toString() {
        return "PreciseStreamCursor at " + curIndex + " length = " + precise.length();
    }

    public void show() {
        Logging.msgln(toString());
    }
}
