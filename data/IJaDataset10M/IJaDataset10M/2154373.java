package nz.org.venice.parser.errcorrect;

import java.util.List;
import java.util.Vector;
import java.util.Iterator;
import nz.org.venice.util.VeniceLog;

/**
   A row within a PartitionTable. The columns make up the rule parts within 
   a derivation.

   @author Mark Hummel
 */
public class PTLine implements Cloneable {

    int numColumns;

    Vector columns;

    final boolean valid;

    VeniceLog veniceLog;

    /**
     * Construct a row of a partition table.
     * 
     * @param num The number of columns in the row
     * @param valid Validity flag. True if the RHS can derive a corrected input.
     */
    public PTLine(int num, boolean valid) {
        numColumns = num;
        columns = new Vector(numColumns);
        this.valid = valid;
        for (int i = 0; i < numColumns; i++) {
            PTEntry e = new PTEntry();
            columns.add(e);
        }
        veniceLog = VeniceLog.getInstance();
    }

    /**
     * Construct a row of a partition table.
     * 
     * @param num The number of columns in the row.
     
     */
    public PTLine(int num) {
        this(num, false);
    }

    /**
     * Construct a row of a partition table, using previously supplied columns.
     * 
     * @param columns A list of PTEntry objects.
     */
    public PTLine(Vector columns) {
        this(columns, false);
    }

    /**
     * Construct a row of a partition table, using previously supplied columns.
     * 
     * @param columns A list of PTEntry objects.
     * @param valid The validity flag. True if the row can derive a corrected input.
     */
    public PTLine(Vector columns, boolean valid) {
        this.columns = columns;
        numColumns = columns.size();
        this.valid = valid;
        veniceLog = VeniceLog.getInstance();
    }

    /**
     * Add a column into the row. The row expands if the index is greater than 
     * the current row size.
     * 
     * @param entry A PTEntry 
     * @param index the column of the row where the entry will be added.
     */
    public void put(PTEntry entry, int index) {
        int i;
        if (index >= columns.size()) {
            PTEntry e = new PTEntry();
            columns.add(entry);
            put(entry, index);
        } else {
            columns.set(index, entry);
        }
    }

    /**
     * Return an entry of the row.
     * 
     * @param index the column number of the row.
     * @return the entry at the specified column. Null if the index is out of
     * bounds.
     */
    public PTEntry get(int index) {
        if (index >= 0 || index < columns.size()) {
            return (PTEntry) columns.get(index);
        }
        return null;
    }

    /**
     * Compare this row to a given row.
     * 
     * @param line The line to compare.
     * @return 0 if the entries of the lines are the same. -1 otherwise.
     */
    public int compareTo(PTLine line) {
        int i;
        int cmp = 0;
        for (i = 0; i < numColumns && cmp == 0; i++) {
            PTEntry e1 = get(i);
            PTEntry e2 = line.get(i);
            cmp = e1.compareTo(e2);
        }
        return cmp;
    }

    /**
     * @return a string representation of the row.
     */
    public String toString() {
        String rv = "";
        int i;
        boolean someCorrectsUnknown = false;
        boolean allCorrectsKnown = false;
        int numCorrections = 0;
        for (i = 0; i < numColumns; i++) {
            PTEntry e = get(i);
            rv += e.toString() + ",";
            if (e.getCorrectionsKnown()) {
                numCorrections += e.getNumCorrections();
                allCorrectsKnown = true;
            } else {
                allCorrectsKnown = false;
                someCorrectsUnknown = true;
            }
        }
        rv += "PTLine: #cols " + numColumns + " # corrects required: " + numCorrections + " all corrects known: " + allCorrectsKnown + " some corrects unknown? " + someCorrectsUnknown + "\n";
        return rv;
    }

    /**
     * @return a new copy of this object.
     */
    public Object clone() {
        return clone(valid);
    }

    /**
     * Return a new copy of the object.
     * @param valid Validity flag for the row copy. 
     */
    public Object clone(boolean valid) {
        PTLine rv;
        Vector columns2;
        columns2 = new Vector();
        Iterator iterator = columns.iterator();
        while (iterator.hasNext()) {
            PTEntry e = (PTEntry) iterator.next();
            PTEntry e2 = (PTEntry) e.clone();
            if (valid && !e.getCorrectionsKnown()) {
                assert false;
            }
            columns2.add(e2);
        }
        rv = new PTLine(columns2, valid);
        return rv;
    }

    /**
     * @return an iterator for the columns.
     */
    public Iterator iterator() {
        return columns.iterator();
    }

    /**
     * Calculate the cumuluative number of corrections in the columns and return.
     * 
     * @return A cumulative number of corrections.
     */
    public int getNumCorrections() {
        Iterator it = columns.iterator();
        int sum = 0;
        while (it.hasNext()) {
            PTEntry entry = (PTEntry) it.next();
            if (entry.getCorrectionsKnown()) {
                sum += entry.getNumCorrections();
            }
        }
        return sum;
    }

    /**
     * Calculat the score of all the corrections required and return,
     * 
     * @return the cumulative score of all the corrections.
     */
    public int getScore() {
        int score = 0;
        Iterator iterator = columns.iterator();
        while (iterator.hasNext()) {
            PTEntry entry = (PTEntry) iterator.next();
            score += entry.getScore();
        }
        return score;
    }

    /**
     * @return The number of columns in the row.
     */
    public int getNumColumns() {
        return numColumns;
    }

    /**
     * @return true if the row is valid, false otherwise. 
     */
    public boolean getValid() {
        return valid;
    }

    /**
     * Return a human readable list of the corrections of the entries.
     * @return a string of the entries and their corrections.
     */
    public String dumpCorrections() {
        String rv = "";
        int entryNum = 0;
        Iterator iterator = columns.iterator();
        while (iterator.hasNext()) {
            PTEntry entry = (PTEntry) iterator.next();
            rv += "Entry: " + entry.getRule().toString();
            List corrections = entry.getCorrections();
            if (corrections == null) {
                continue;
            }
            Iterator correctIt = corrections.iterator();
            while (correctIt.hasNext()) {
                Correction c = (Correction) correctIt.next();
                rv += c.toString() + "\n";
            }
        }
        return rv;
    }

    public boolean checkMe() {
        Iterator it = columns.iterator();
        while (it.hasNext()) {
            PTEntry e = (PTEntry) it.next();
            if (!e.getCorrectionsKnown()) {
                if (!e.isTerminal()) {
                    System.out.println("Ok not known for non terminal");
                } else {
                    System.out.println("Huh?");
                }
                return false;
            }
        }
        return true;
    }
}
