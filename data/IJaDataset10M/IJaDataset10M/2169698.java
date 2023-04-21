package net.sourceforge.gedapi.io;

import java.io.IOException;
import java.io.Writer;
import net.sourceforge.gedapi.util.GLinkPattern;

public abstract class GLinkPullHandler {

    private int insertDelta;

    private boolean startedHandlePattern;

    protected Writer outWriter;

    public GLinkPullHandler() {
        this.startedHandlePattern = false;
        this.insertDelta = 0;
    }

    public abstract void handlePattern(GLinkPattern pattern, int ch) throws IOException;

    public void handleInsertDelta(int insertDelta) {
    }

    /**
	 * The number of java.lang.Characters to move the insertion point
	 * backward (negative integer) or forward (positive integer) from
	 * the IO stream position where this GLinkPattern was found.
	 * @return returns a positive (forward) or negative (backward) integer
	 * indicating the number of java.lang.Characters to move the insertion
	 * point of the IO stream.
	 */
    public int getInsertDelta() {
        return insertDelta;
    }

    public final void setInsertDelta(int insertDelta) {
        if (startedHandlePattern) {
            throw new IllegalStateException("You cannot set the insertDelta property while handlePattern is being invoked.");
        }
        this.insertDelta = insertDelta;
        handleInsertDelta(insertDelta);
    }

    void resetHandlePattern() {
        this.startedHandlePattern = false;
    }

    void startHandlePattern() {
        this.startedHandlePattern = true;
    }

    public void setOutWriter(Writer outWriter) {
        this.outWriter = outWriter;
    }
}
