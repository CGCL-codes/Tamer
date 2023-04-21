package mecca.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class Report {

    private RecordHeader header;

    private List records;

    private boolean notEnd;

    private int i;

    private int total;

    private List align;

    public Report() {
        records = new ArrayList();
        align = new ArrayList();
    }

    public int getTotal() {
        return total;
    }

    public void addRecord(Record record) {
        records.add(record);
        total++;
        notEnd = true;
    }

    public void add(Record record) {
        addRecord(record);
    }

    public Iterator getRecords() {
        return records.iterator();
    }

    public Record next() {
        Record record = null;
        if (notEnd) {
            record = (Record) records.get(i);
            i++;
            if (i >= total) {
                i = total;
                notEnd = false;
            }
        }
        return record;
    }

    public boolean hasNext() {
        return notEnd;
    }

    public void goFirstRecord() {
        i = 0;
    }

    public void setHeader(RecordHeader header) {
        for (int i = 0; i < header.getCols(); i++) {
            align.add(new Integer(Alignment.LEFT));
        }
        this.header = header;
    }

    public RecordHeader getHeader() {
        return header;
    }

    public void setAlignment(int col, int a) {
        align.set(col, new Integer(a));
    }

    public int getAlignmentAt(int i) {
        return ((Integer) align.get(i)).intValue();
    }
}
