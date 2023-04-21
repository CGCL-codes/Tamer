package net.sourceforge.jsorter.samples;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.jsorter.SortableColumnImpl;
import net.sourceforge.jsorter.Sorter;
import net.sourceforge.jsorter.SorterConstants;

/**
 * This sample shows how to sort an ArrayList using one sort order and one sort
 * column. ArrayList is used in this sample, however, other objects that
 * implement the List interface such as LinkedList or Vector could be used as
 * well.
 */
public class OneColumnListSample {

    private static List table = new ArrayList();

    /**
	 * Loads the test data.
	 */
    private static void loadData() {
        List row = null;
        row = new ArrayList();
        row.add(new Integer(3));
        row.add("4");
        row.add(new Double(4.58));
        table.add(row);
        row = new ArrayList();
        row.add(new Integer(3));
        row.add("4");
        row.add(new Double(4.57));
        table.add(row);
        row = new ArrayList();
        row.add(new Integer(3));
        row.add("9");
        row.add(new Double(2.22));
        table.add(row);
    }

    /**
	 * Run the sample.
	 */
    public void run() {
        loadData();
        System.out.println("JSorter One Column List Sample: Before Sort");
        for (int ctr = 0, size = table.size(); ctr < size; ctr++) {
            List row = (List) table.get(ctr);
            System.out.println(row.get(0) + "," + row.get(1) + "," + row.get(2));
        }
        ArrayList sortColumns = new ArrayList();
        sortColumns.add(new SortableColumnImpl(2, SorterConstants.DESCENDING_ORDER));
        Sorter sorter = new Sorter(table, sortColumns);
        sorter.sort();
        System.out.println("JSorter One Column List Sample: After Sort");
        for (int ctr = 0; ctr < table.size(); ctr++) {
            List row = (ArrayList) table.get(ctr);
            System.out.println(row.get(0) + "," + row.get(1) + "," + row.get(2));
        }
    }

    /**
	 * Used for running this sample.
	 * 
	 * @param args
	 *            Arguments for the main method at runtime. This method does not
	 *            use any of the parameters for its processing.
	 */
    public static void main(String[] args) {
        OneColumnListSample sample = new OneColumnListSample();
        sample.run();
    }
}
