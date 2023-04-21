package org.coury.jfilehelpers.events;

/**
 * @author Robert Eccardt
 *
 * @param <T> the data record type
 */
public class AfterWriteRecordEventArgs<T> extends WriteRecordEventArgs<T> {

    private String recordLine;

    public AfterWriteRecordEventArgs(T record, int lineNumber, String recordLine) {
        super(record, lineNumber);
        this.recordLine = recordLine;
    }

    public String getRecordLine() {
        return recordLine;
    }

    public void setRecordLine(String recordLine) {
        this.recordLine = recordLine;
    }
}
