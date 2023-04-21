package org.coury.jfilehelpers.events;

/**
 * @author Robert Eccardt
 *
 */
public class BeforeReadRecordEventArgs<T> extends ReadRecordEventArgs {

    private boolean skipThisRecord = false;

    public BeforeReadRecordEventArgs(String recordLine, int lineNumber) {
        super(recordLine, lineNumber);
    }

    public BeforeReadRecordEventArgs(String recordLine) {
        super(recordLine, -1);
    }

    public boolean getSkipThisRecord() {
        return skipThisRecord;
    }

    public void setSkipThisRecord(boolean skipThisRecord) {
        this.skipThisRecord = skipThisRecord;
    }
}
