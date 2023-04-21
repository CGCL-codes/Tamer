package d2.worldmodel.foil;

import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * Set of utilities to support FOIL.
 * @author Frans Coenen, modified for D2 use by Katie Long
 */
public class AssocRuleMining {

    /** 2-D array to hold input data from data file */
    protected short[][] dataArray = null;

    /**
	 * Number of classes in input data set (input by the user).
	 */
    protected int numClasses = 0;

    /**
	 * Error flag used when checking command line arguments (default = true).
	 */
    protected boolean errorFlag = true;

    /**
	 * Input format OK flag( default = true).
	 */
    protected boolean inputFormatOkFlag = true;

    protected int numCols = 0;

    protected int numRows = 0;

    protected int numOneItemSets = 0;

    protected BufferedReader fileInput;

    protected File filePath = null;

    /**
	 * Processes command line arguments
	 */
    public AssocRuleMining(String[] args) {
    }

    /** Default constructor */
    public AssocRuleMining() {
    }

    /**
	 * Check whether given line from input file is of appropriate format (space separated integers), 
	 * if incorrectly formatted line found inputFormatOkFlag set to false.
	 * @param counter the line number in the input file.
	 * @param str the current line from the input file.
	 */
    protected void checkLine(int counter, String str) {
        for (int index = 0; index < str.length(); index++) {
            if (!Character.isDigit(str.charAt(index)) && !Character.isWhitespace(str.charAt(index))) {
                JOptionPane.showMessageDialog(null, "FILE INPUT ERROR:\n" + "charcater on line " + counter + " is not a digit or white space");
                inputFormatOkFlag = false;
                break;
            }
        }
    }

    /**
	 * Checks that data set is ordered correctly.
	 */
    protected boolean checkOrdering() {
        boolean result = true;
        for (int index = 0; index < dataArray.length; index++) {
            if (!checkLineOrdering(index + 1, dataArray[index])) result = false;
        }
        return (result);
    }

    /**
	 * Checks whether a given line in the input data is in numeric sequence.
	 * @param lineNum the line number.
	 * @param itemSet the item set represented by the line
	 * @return true if OK and false otherwise.
	 */
    private boolean checkLineOrdering(int lineNum, short[] itemSet) {
        for (int index = 0; index < itemSet.length - 1; index++) {
            if (itemSet[index] >= itemSet[index + 1]) {
                JOptionPane.showMessageDialog(null, "FILE FORMAT ERROR:\n" + "Attribute data in line " + lineNum + " not in numeric order");
                return (false);
            }
        }
        return (true);
    }

    /**
	 * Counts number of columns represented by input data.
	 */
    protected void countNumCols() {
        int maxAttribute = 0;
        for (int index = 0; index < dataArray.length; index++) {
            int lastIndex = dataArray[index].length - 1;
            if (dataArray[index][lastIndex] > maxAttribute) maxAttribute = dataArray[index][lastIndex];
        }
        numCols = maxAttribute;
        numOneItemSets = numCols;
    }

    /**
	 * Produce an item set (array of elements) from input line.
	 * @param dataLine row from the input data file
	 * @param numberOfTokens number of items in row
	 * @return 1-D array of short integers representing attributes in input row
	 */
    protected short[] binConversion(StringTokenizer dataLine, int numberOfTokens) {
        short number;
        short[] newItemSet = null;
        for (int tokenCounter = 0; tokenCounter < numberOfTokens; tokenCounter++) {
            number = new Short(dataLine.nextToken()).shortValue();
            newItemSet = realloc1(newItemSet, number);
        }
        return (newItemSet);
    }

    /** Resizes given item set so that its length is increased by one and new element inserted.
	 * @param oldItemSet the original item set
	 * @param newElement the new element/attribute to be inserted
	 * @return the combined item set
	 */
    protected short[] reallocInsert(short[] oldItemSet, short newElement) {
        if (oldItemSet == null) {
            short[] newItemSet = { newElement };
            return (newItemSet);
        }
        int oldItemSetLength = oldItemSet.length;
        short[] newItemSet = new short[oldItemSetLength + 1];
        int index1;
        for (index1 = 0; index1 < oldItemSetLength; index1++) {
            if (newElement < oldItemSet[index1]) {
                newItemSet[index1] = newElement;
                for (int index2 = index1 + 1; index2 < newItemSet.length; index2++) newItemSet[index2] = oldItemSet[index2 - 1];
                return (newItemSet);
            } else newItemSet[index1] = oldItemSet[index1];
        }
        newItemSet[newItemSet.length - 1] = newElement;
        return (newItemSet);
    }

    /**
	 * Resizes given item set so that its length is increased by one and appends new element 
	 * (identical to append method)
	 * @param oldItemSet the original item set
	 * @param newElement the new element/attribute to be appended
	 * @return the combined item set
	 */
    protected short[] realloc1(short[] oldItemSet, short newElement) {
        if (oldItemSet == null) {
            short[] newItemSet = { newElement };
            return (newItemSet);
        }
        int oldItemSetLength = oldItemSet.length;
        short[] newItemSet = new short[oldItemSetLength + 1];
        int index;
        for (index = 0; index < oldItemSetLength; index++) newItemSet[index] = oldItemSet[index];
        newItemSet[index] = newElement;
        return (newItemSet);
    }

    /**
	 * Gets the last element in the given item set, or '0' if the itemset is empty.
	 * @param itemSet the given item set.
	 * @return the last element.
	 */
    protected short getLastElement(short[] itemSet) {
        if (itemSet == null) return (0);
        return (itemSet[itemSet.length - 1]);
    }

    /** Checks whether two item sets are the same.
	 * @param itemSet1 the first item set.
	 * @param itemSet2 the second item set to be compared with first.
	 * @return true if itemSet1 is equal to itemSet2, and false otherwise.
	 */
    protected boolean isEqual(short[] itemSet1, short[] itemSet2) {
        if (itemSet2 == null) return (false);
        int length1 = itemSet1.length;
        int length2 = itemSet2.length;
        if (length1 != length2) return (false);
        for (int index = 0; index < length1; index++) {
            if (itemSet1[index] != itemSet2[index]) return (false);
        }
        return (true);
    }

    /**
	 * Checks whether one item set is subset of a second item set.
	 * @param itemSet1 the first item set.
	 * @param itemSet2 the second item set to be compared with first.
	 * @return true if itemSet1 is a subset of itemSet2, and false otherwise.
	 */
    protected boolean isSubset(short[] itemSet1, short[] itemSet2) {
        if (itemSet1 == null) return (true);
        if (itemSet2 == null) return (false);
        for (int index1 = 0; index1 < itemSet1.length; index1++) {
            if (notMemberOf(itemSet1[index1], itemSet2)) return (false);
        }
        return (true);
    }

    /**
	 * Checks whether a particular element/attribute identified by a column number is not a 
	 * member of the given item set.
	 * @param number the attribute identifier (column number).
	 * @param itemSet the given item set.
	 * @return true if first argument is not a member of itemSet, and false otherwise
	 */
    protected boolean notMemberOf(short number, short[] itemSet) {
        for (int index = 0; index < itemSet.length; index++) {
            if (number < itemSet[index]) return (true);
            if (number == itemSet[index]) return (false);
        }
        return (true);
    }

    /**
	 * Makes a copy of a given itemSet.
	 * @param itemSet the given item set.
	 * @return copy of given item set.
	 */
    protected short[] copyItemSet(short[] itemSet) {
        if (itemSet == null) return (null);
        short[] newItemSet = new short[itemSet.length];
        for (int index = 0; index < itemSet.length; index++) {
            newItemSet[index] = itemSet[index];
        }
        return (newItemSet);
    }

    /**
	 * Outputs stored input data set; initially read from input data file, but
	 * may be reordered or pruned if desired by a particular application.
	 */
    public void outputDataArray() {
        System.out.println("DATA SET\n" + "--------");
        for (int index = 0; index < dataArray.length; index++) {
            outputItemSet(dataArray[index]);
            System.out.println();
        }
    }

    /**
	 * Outputs a given item set.
	 * @param itemSet the given item set.
	 */
    protected void outputItemSet(short[] itemSet) {
        if (itemSet == null) System.out.print(" null "); else {
            int counter = 0;
            for (int index = 0; index < itemSet.length; index++) {
                if (counter == 0) {
                    counter++;
                    System.out.print(" {");
                } else System.out.print(" ");
                System.out.print(itemSet[index]);
            }
            System.out.print("} ");
        }
    }

    /**
	 * Outputs size (number of records and number of elements) of stored
	 * input data set read from input data file.
	 */
    public void outputDataArraySize() {
        int numRecords = 0;
        int numElements = 0;
        for (int index = 0; index < dataArray.length; index++) {
            if (dataArray[index] != null) {
                numRecords++;
                numElements = numElements + dataArray[index].length;
            }
        }
        System.out.println("Number of columns  = " + numCols);
        System.out.println("Number of records  = " + numRecords);
        System.out.println("Number of elements = " + numElements);
        double density = (double) numElements / (numCols * numRecords);
        System.out.println("Data set density   = " + twoDecPlaces(density) + "%");
    }

    /** Outputs difference between two given times.
	 * @param time1 the first time.
	 * @param time2 the second time.
	 * @return duration.
	 */
    public double outputDuration(double time1, double time2) {
        double duration = (time2 - time1) / 1000;
        System.out.println("Generation time = " + twoDecPlaces(duration) + " seconds (" + twoDecPlaces(duration / 60) + " mins)");
        return (duration);
    }

    /**
	 * Converts given real number to real number rounded up to two decimal places.
	 * @param number the given number.
	 * @return the number to two decimal places.
	 */
    protected double twoDecPlaces(double number) {
        int numInt = (int) ((number + 0.005) * 100.0);
        number = (numInt) / 100.0;
        return (number);
    }
}
