package com.google.testing.threadtester;

/**
 * Extends RunResult to add the current line reached. This is
 * used by {@link InterleavedRunner} when reporting exceptions.
 *
 * @see ObjectInstrumentationImpl#interleave
 * @author alasdair.mackintosh@gmail.com (Alasdair Mackintosh)
 */
class SteppedRunResult extends RunResult {

    private int lineNumber;

    SteppedRunResult(Throwable main, Throwable secondary, int lineNumber) {
        super(main, secondary);
        this.lineNumber = lineNumber;
    }

    /** Returns the last line number executed in the main runnable. */
    int getLineNumber() {
        return lineNumber;
    }
}
