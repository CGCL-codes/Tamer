package net.sf.crocus.csv.record;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;
import net.sf.crocus.csv.event.RecordEvent;
import net.sf.crocus.csv.event.RecordListener;
import net.sf.crocus.csv.fields.CSVField;

/**
 * 
 * <p>
 * Description: An Abstract Record Manager
 * </p>
 * 
 * 
 * @author M Shaaf
 * @email shaaf.m@gmail.com
 */
public abstract class AbstractCSVRecordManager implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1785926313163654186L;

    private boolean allowListener = true;

    private boolean eventAsync = true;

    private ArrayList<RecordListener> recordListenerList = null;

    public AbstractCSVRecordManager() {
        recordListenerList = new ArrayList<RecordListener>();
    }

    /**
	 * 
	 * Notifies Event to Listeners
	 * 
	 * @param csvRecord
	 * @param RECORD_EVENT_TYPE
	 */
    protected void sendEvent(CSVRecord csvRecord, int RECORD_EVENT_TYPE) {
        RecordEvent recordEvent = new RecordEvent(this, RECORD_EVENT_TYPE);
        sendEvent(recordEvent);
    }

    /**
	 * Notifies Event to Listeners
	 * 
	 * @param recordEvent
	 */
    protected void sendEvent(RecordEvent recordEvent) {
        for (RecordListener list : recordListenerList) {
            list.eventPerformed(recordEvent);
        }
    }

    /**
	 * 
	 * @param record CSVRecord
	 */
    public abstract void addRecord(CSVRecord record);

    /**
	 * 
	 * @return Collection
	 */
    public abstract Collection<CSVRecord> getRecordList();

    /**
	 * 
	 * @param record CSVRecord
	 */
    public abstract void containsRecord(CSVRecord record);

    /**
	 * 
	 * @param columnIndex
	 *            int
	 * @return Object[]
	 */
    private Object[] getColumn(int columnIndex) {
        return null;
    }

    ;

    public abstract CSVRecord getRow(int rowIndex);

    /**
	 * 
	 * @return Collection
	 */
    public abstract Collection<CSVRecord> getAllRecords();

    /**
	 * 
	 * @return CSVRecord
	 */
    public abstract CSVRecord getNewRecord();

    /**
	 * 
	 * @return int
	 */
    public abstract int getRowCount();

    /**
	 * 
	 * @return int
	 */
    public abstract int getColumnCount();

    /**
	 * 
	 * @param rowIndex
	 *            int
	 * @param columnIndex
	 *            int
	 * @return Object
	 */
    public abstract Object getValueAt(int rowIndex, int columnIndex);

    /**
	 * 
	 * @param rowIndex
	 *            int
	 * @param columnIndex
	 *            int
	 * @return CSVField
	 */
    public abstract CSVField getField(int rowIndex, int columnIndex);

    /**
	 * 
	 * @param rowIndex
	 *            int
	 * @param columnIndex
	 *            int
	 * @return String
	 */
    public void addRecordListener(RecordListener recordListener) {
        this.recordListenerList.add(recordListener);
    }

    protected void setAllowListener(boolean allowListener) {
        this.allowListener = allowListener;
    }

    protected boolean isAllowListener() {
        return allowListener;
    }

    protected void setEventAsync(boolean eventAsync) {
        this.eventAsync = eventAsync;
    }

    protected boolean isEventAsync() {
        return eventAsync;
    }

    public abstract Collection<CSVRecord> getRecords(Pattern pattern);

    public abstract Collection<CSVRecord> getRecordsByField(Pattern pattern);

    public abstract CSVRecord getColumnNames();

    public abstract boolean isFirstRowNames();

    public abstract void setFirstRowNames(boolean isFirstRowNames);

    public abstract String getXML(Collection<CSVRecord> recordsList);

    public abstract String getJSON(Collection<CSVRecord> recordsList);
}
