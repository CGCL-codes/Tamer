package CytoDDN;

import java.io.*;

/**
 * <code>CSV</code> reads or writes a comma-separated values (CSV) file. 
 *
 * @version      2010.1115
 */
public class CSV {

    private double[][] data = null;

    private String[][] dataString = null;

    private int numRow = 0;

    private int numCol = 0;

    private String fileName = null;

    private boolean asString = false;

    /**
     * A constructor for the class <code>CSV</code>
     * 
     * @param fileName    the csv file name to be read or written
     */
    public CSV(String fileName) {
        this.fileName = fileName;
    }

    /**
     * A constructor for the class <code>CSV</code>
     * 
     * @param fileName    the csv file name to be read or written
     * @param asString    Whether to read csv file as string. By default,
     *                    <code>asString = false</code>, and csv file is
     *                    read as a <code>double</code> array.
     */
    public CSV(String fileName, boolean asString) {
        this.fileName = fileName;
        this.asString = asString;
    }

    /**
     * Returns the csv file data in the form of <code>double</code> array.
     * 
     * @return           a data matrix
     */
    public double[][] getData() {
        return data;
    }

    /**
     * Returns the csv file data in the form of <code>String</code> array, if
     * <code>asString=true</code>.
     * 
     * @return           a String matrix
     */
    public String[][] getDataString() {
        return dataString;
    }

    /**
     * Returns the number of rows in the csv file data matrix.
     * 
     * @return           the number of rows
     */
    public int getNumRow() {
        return numRow;
    }

    /**
     * Returns the number of columns in the csv file data matrix.
     * 
     * @return           the number of columns
     */
    public int getNumCol() {
        return numCol;
    }

    /**
     * Reads a csv file.
     */
    public void readCSV() throws IOException {
        BufferedReader inputStream = null;
        try {
            inputStream = new BufferedReader(new FileReader(fileName));
            String l;
            String[] row;
            numRow = 0;
            while ((l = inputStream.readLine()) != null) {
                numRow++;
            }
            inputStream = new BufferedReader(new FileReader(fileName));
            l = inputStream.readLine();
            numCol = l.split(",").length;
            if (asString) {
                dataString = new String[numRow][numCol];
                int i = 0;
                inputStream = new BufferedReader(new FileReader(fileName));
                while ((l = inputStream.readLine()) != null) {
                    row = l.split(",");
                    for (int j = 0; j < numCol; j++) {
                        dataString[i][j] = row[j];
                    }
                    i++;
                }
            } else {
                data = new double[numRow][numCol];
                int i = 0;
                inputStream = new BufferedReader(new FileReader(fileName));
                while ((l = inputStream.readLine()) != null) {
                    row = l.split(",");
                    for (int j = 0; j < numCol; j++) {
                        data[i][j] = Double.parseDouble(row[j]);
                    }
                    i++;
                }
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /** 
     * Writes <code>data</code> to a csv file.
     *
     * @param dataMatrix    the data matrix to be written
     */
    public void writeCSV(double[][] dataMatrix) throws IOException {
        data = dataMatrix;
        numRow = data.length;
        numCol = data[0].length;
        if (fileName != null && data != null) {
            BufferedWriter outputStream = null;
            try {
                outputStream = new BufferedWriter(new FileWriter(fileName), 1 * 1024 * 1024);
                for (int i = 0; i < numRow; i++) {
                    String entry = "" + data[i][0];
                    for (int j = 1; j < numCol - 1; j++) {
                        entry = entry + "," + data[i][j];
                    }
                    if (numCol > 1) {
                        entry = entry + "," + data[i][numCol - 1];
                    }
                    outputStream.write(entry.toCharArray());
                    outputStream.newLine();
                }
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                CSV A = new CSV(args[0]);
                A.readCSV();
                System.out.println("numRow = " + A.getNumRow());
                System.out.println("numCol = " + A.getNumCol());
                for (int i = 0; i < A.getNumRow(); i++) {
                    for (int j = 0; j < A.getNumCol(); j++) {
                        System.out.print(A.getData()[i][j] + "\t");
                    }
                    System.out.println(" ");
                }
                CSV B = new CSV("writeCsvTest.csv");
                B.writeCSV(A.getData());
            } catch (Exception e) {
                System.out.println("There is an error.");
            }
        }
    }
}
