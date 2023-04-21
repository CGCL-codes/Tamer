package nutils;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class Join {

    private Hashtable<String, ArrayList<Integer>> mSecondFileRowIndicesTable;

    private String[][] mDataFile1;

    private String[][] mDataFile2;

    private int mColFile1;

    private int mColFile2;

    private String mOutFilename;

    private boolean mHasHeaderFile1;

    private boolean mHasHeaderFile2;

    private boolean mWriteFile1ContentsFirst;

    public Join(String inFilename1, String inFilename2, int colFile1, int colFile2, boolean hasHeaderFile1, boolean hasHeaderFile2, boolean writeFile1ContentsFirst, String outFilename) {
        mDataFile1 = IOUtils.readAllLinesFromFileAsMatrix(inFilename1, "\\t");
        mDataFile2 = IOUtils.readAllLinesFromFileAsMatrix(inFilename2, "\\t");
        mColFile1 = colFile1;
        mColFile2 = colFile2;
        mOutFilename = outFilename;
        mHasHeaderFile1 = hasHeaderFile1;
        mHasHeaderFile2 = hasHeaderFile2;
        mWriteFile1ContentsFirst = writeFile1ContentsFirst;
        mSecondFileRowIndicesTable = new Hashtable<String, ArrayList<Integer>>();
        registerRowsOfFile(mDataFile2, colFile2, mSecondFileRowIndicesTable);
    }

    private void registerRowsOfFile(String[][] dataFile2, int colFile2, Hashtable<String, ArrayList<Integer>> rowIndicesTable) {
        for (int i = 0; i < dataFile2.length; i++) {
            String theValue = dataFile2[i][colFile2];
            ArrayList<Integer> storedList = rowIndicesTable.get(theValue);
            if (storedList == null) {
                storedList = new ArrayList<Integer>();
                rowIndicesTable.put(theValue, storedList);
            }
            storedList.add(i);
        }
    }

    public void performJoin() {
        BufferedWriter out = IOUtils.getBufferedWriter(mOutFilename);
        int startLineIndex = writeHeaders(out);
        for (int i = startLineIndex; i < mDataFile1.length; i++) {
            String[] dataFile1Row = mDataFile1[i];
            String valueToMatch = dataFile1Row[mColFile1];
            ArrayList<Integer> dataFile2MatchingRows = mSecondFileRowIndicesTable.get(valueToMatch);
            if (dataFile2MatchingRows == null) {
            } else {
                for (int j = 0; j < dataFile2MatchingRows.size(); j++) {
                    String[] dataFile2Row = mDataFile2[dataFile2MatchingRows.get(j)];
                    writeMatchingLineToFile(out, dataFile1Row, dataFile2Row, mColFile2, mWriteFile1ContentsFirst, '\t');
                }
            }
        }
        IOUtils.closeBufferedWriter(out);
    }

    /** Writes the headers (if they exist) and returns the starting line number for file 1. */
    private int writeHeaders(BufferedWriter out) {
        if (mHasHeaderFile1 && mHasHeaderFile2) {
            writeMatchingLineToFile(out, mDataFile1[0], mDataFile2[0], mColFile2, mWriteFile1ContentsFirst, '\t');
            return 1;
        } else if (mHasHeaderFile1) {
            String[] headers = createFillerHeaders(mDataFile2[0].length - 1);
            writeMatchingLineToFile(out, mDataFile1[0], headers, -1, mWriteFile1ContentsFirst, '\t');
            return 1;
        } else if (mHasHeaderFile2) {
            String[] headers = createFillerHeaders(mDataFile1[0].length);
            headers[mColFile1] = mDataFile2[0][mColFile2];
            writeMatchingLineToFile(out, headers, mDataFile2[0], mColFile2, mWriteFile1ContentsFirst, '\t');
        }
        return 0;
    }

    private String[] createFillerHeaders(int numHeaders) {
        String[] headers = new String[numHeaders];
        for (int i = 0; i < headers.length; i++) {
            headers[i] = "NA_" + i;
        }
        return headers;
    }

    private static void writeMatchingLineToFile(BufferedWriter out, String[] data1Row, String[] data2Row, int colFile2, boolean writeFile1ContentsFirst, char separatorChar) {
        String separatorString = "" + separatorChar;
        if (writeFile1ContentsFirst) {
            IOUtils.writeToBufferedWriter(out, data1Row, separatorString, false, false);
            writeMatchingLineToFileHelper(out, data2Row, colFile2, separatorString, true);
        } else {
            writeMatchingLineToFileHelper(out, data2Row, colFile2, separatorString, false);
            IOUtils.writeToBufferedWriter(out, data1Row, separatorString, true, false);
        }
        IOUtils.writeToBufferedWriter(out, "", true);
    }

    private static void writeMatchingLineToFileHelper(BufferedWriter out, String[] data2Row, int colFile2, String columnSeparator, boolean prefixWithColumnSeparator) {
        int numColumnsPrinted = 0;
        for (int i = 0; i < data2Row.length; i++) {
            if ((colFile2 < 0) || (i != colFile2)) {
                if ((numColumnsPrinted > 0) || prefixWithColumnSeparator) {
                    IOUtils.writeToBufferedWriter(out, columnSeparator, false);
                }
                IOUtils.writeToBufferedWriter(out, data2Row[i], false);
                numColumnsPrinted++;
            }
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        if (args.length < 8) {
            System.out.println("Usage: java Join inFilename1 inFilename2 file1Col file2Col file1HasHeader file2HasHeader writeFile1ContentFirst outFilename");
        } else {
            Join joiner = new Join(args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), Boolean.parseBoolean(args[4]), Boolean.parseBoolean(args[5]), Boolean.parseBoolean(args[6]), args[7]);
            joiner.performJoin();
        }
    }
}
