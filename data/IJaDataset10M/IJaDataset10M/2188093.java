package org.archive.crawler.byexample.datastructure.invertedindex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import org.archive.crawler.byexample.constants.OutputConstants;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

/**
 * Class representing an single row in an InvertedIndex
 * Each row is a combination of IndexEntries list and total row value.
 * Total row value is a sum of all IndexEntries values listed in the row
 * 
 * @author Michael Bendersky 
 *
 */
public class IndexRow extends TupleBinding {

    private ArrayList<IndexEntry> rowList;

    private int totalRowValue;

    public IndexRow() {
        rowList = new ArrayList<IndexEntry>();
        totalRowValue = 0;
    }

    public IndexRow getRow() {
        return this;
    }

    public void addRowEntry(IndexEntry entry) {
        rowList.add(entry);
        totalRowValue += entry.getEntryValue();
    }

    public void increaseRowLatestEntryValue() {
        int index = rowList.size() - 1;
        IndexEntry currEntry = rowList.get(index);
        currEntry.increaseEntryValue();
        rowList.set(index, currEntry);
        totalRowValue++;
    }

    public String getLatestEntryId() {
        if (rowList.size() == 0) return String.valueOf(-1); else return rowList.get(rowList.size() - 1).getEntryId();
    }

    public Iterator<IndexEntry> getRowIterator() {
        return rowList.iterator();
    }

    public int getRowSize() {
        return rowList.size();
    }

    public IndexEntry getIndex(int i) {
        return rowList.get(i);
    }

    public void sortRow(Comparator<IndexEntry> comparator) {
        Collections.sort(rowList, comparator);
    }

    public void normalizeRow() {
        for (Iterator<IndexEntry> iter = getRowIterator(); iter.hasNext(); ) {
            iter.next().scaleEntryValue(totalRowValue);
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (IndexEntry ie : rowList) {
            sb.append(ie.toString()).append(",");
        }
        return sb + OutputConstants.KEY_SEPARATOR + String.valueOf(totalRowValue);
    }

    public void addEntriesFromString(String valuesString, String totalCountString) {
        String[] arrayValues;
        String[] entryValues;
        arrayValues = valuesString.split(",");
        for (int i = 0; i < arrayValues.length; i++) {
            entryValues = arrayValues[i].split(OutputConstants.ENTRY_SEPARATOR);
            this.addRowEntry(new IndexEntry(entryValues[0], Double.parseDouble(entryValues[1])));
        }
        this.totalRowValue = Integer.parseInt(totalCountString);
    }

    public void setTotalValue(int newValue) {
        totalRowValue = newValue;
    }

    public int getTotalValue() {
        return totalRowValue;
    }

    public void objectToEntry(Object object, TupleOutput dbEntry) {
        dbEntry.writeInt(rowList.size());
        for (IndexEntry ie : rowList) {
            ie.objectToEntry(ie, dbEntry);
        }
        dbEntry.writeInt(totalRowValue);
    }

    public IndexRow entryToObject(TupleInput dbEntry) {
        IndexRow indexRow = new IndexRow();
        IndexEntry ie = new IndexEntry("dummy", 0);
        int sizeToRead = dbEntry.readInt();
        for (int i = 0; i < sizeToRead; i++) indexRow.rowList.add(ie.entryToObject(dbEntry));
        indexRow.totalRowValue = dbEntry.readInt();
        return indexRow;
    }
}
