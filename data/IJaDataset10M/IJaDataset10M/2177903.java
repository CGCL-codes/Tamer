package org.tigr.microarray.mev.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JOptionPane;
import org.tigr.util.StringSplitter;

public class GenepixFileParser extends Thread {

    private final int FIRST_NUM = 1;

    private final String BLOCK = "\"Block\"";

    private final String BLOCK_N = "Block";

    private final String COLUMN = "\"Column\"";

    private final String COLUMN_N = "Column";

    private final String F_PIX = "\"F Pixels\"";

    private final String F_PIX_N = "F Pixels";

    private final String ID = "\"ID\"";

    private final String ID_N = "ID";

    private final String NAME = "\"Name\"";

    private final String NAME_N = "Name";

    private final String RW = "\"Row\"";

    private final String ROW_N = "Row";

    private final String WAVE_KEY_1 = "ImageName";

    private final String WAVE_KEY_2 = "Wavelengths";

    private final String FLAGS = "\"Flags\"";

    private final String FLAGS_N = "Flags";

    private boolean ready;

    private boolean hasError;

    private File gprFile;

    private int taskEnd, counter;

    private int fPixAddr;

    private String errorMsg;

    private String chA, chB, chAn, chBn;

    private String chAs, chAsn, chBs, chBsn;

    private String bgChA, bgChB, bgChAn, bgChBn;

    private Vector tavFile;

    public GenepixFileParser() {
        this(new String(""));
    }

    /****************************************************************************
   * <b>Constructor: </b>
   * <p><b>Parameters: </b>
   * <br> genepixFileName --- the name of GenePix file to be loaded..
   *****************************************************************************/
    public GenepixFileParser(String genepixFileName) {
        this(new File(genepixFileName));
    }

    /****************************************************************************
   * <b>Constructor: </b>
   * <p><b>Parameters: </b>
   * <br> genepixFile --- the GenePix files to be loaded.
   *****************************************************************************/
    public GenepixFileParser(File genepixFile) {
        this(genepixFile, false);
    }

    /****************************************************************************
   * <b>Constructor: </b>
   * <p><b>Parameters: </b>
   * <br> genepixFileName --- the name of GenePix file to be loaded.
   * <br> useThread --- true for using thread to complete the process.
   *****************************************************************************/
    public GenepixFileParser(String genepixFileName, boolean useThread) {
        this(new File(genepixFileName), useThread);
    }

    /****************************************************************************
   * <b>Constructor: </b>
   * <p><b>Parameters: </b>
   * <br> genepixFile --- the GenePix files to be loaded.
   * <br> useThread --- true for using thread to complete the process.
   *****************************************************************************/
    public GenepixFileParser(File genepixFile, boolean useThread) {
        if (genepixFile != null) {
            gprFile = genepixFile;
            errorMsg = new String("");
            ready = false;
            hasError = false;
            taskEnd = 0;
            if (!useThread) {
                startLoad();
            }
        }
    }

    /***************************************************************************
   * <b>Description: </b>
   *   returns the current step of the loading.
   **************************************************************************/
    public int getCurrentStep() {
        return counter;
    }

    /***************************************************************************
   * <b>Description: </b>
   *   if error occurs, calling this method to get error message.
   **************************************************************************/
    public String getErrorMessage() {
        return errorMsg;
    }

    /***************************************************************************
   * <b>Description: </b>
   *   returns the total number of the steps for completing the file loading.
   *   Normally, this is the number of spots (rows).
   **************************************************************************/
    public int getProgressEnd() {
        return taskEnd;
    }

    /***************************************************************************
   * <b>Description: </b>
   *   returns a vector that contains a number of vector with 8 elements as
   *   following:
   * <table boarder=0, cellpadding=1>
   * <tr><th>Index</th><th>Data Type</th><th>Contents</th></tr>
   * <tr><td>1</td><td>Integer</td><td>Intensity of Channel A</td></tr>
   * <tr><td>2</td><td>Integer</td><td>Intensity of Channel B</td></tr>
   * <tr><td>3</td><td>Integer</td><td>Slide Row</td></tr>
   * <tr><td>4</td><td>Integer</td><td>Slide Column</td></tr>
   * <tr><td>5</td><td>Integer</td><td>Meta Row</td></tr>
   * <tr><td>6</td><td>Integer</td><td>Meta Column</td></tr>
   * <tr><td>7</td><td>String</td><td>Name (in GenePix File)</td></tr>
   * <tr><td>8</td><td>String</td><td>ID (in GenePix File)</td></tr>
   * </table>
   * <b>Note: </b> Before calling this method, isCompleted() should be called
   * to ensure the loading is completed.
   **************************************************************************/
    public Vector getTavFile() {
        return tavFile;
    }

    /***************************************************************************
   * <b>Description: </b>
   *   return ture if there is an error during the loading; otherwise, false.
   **************************************************************************/
    public boolean hasError() {
        return hasError;
    }

    /***************************************************************************
   * <b>Description: </b>
   *   return ture if the loading is done; otherwise, false.
   **************************************************************************/
    public boolean isCompleted() {
        return ready;
    }

    /***************************************************************************
   * <b>Description: </b>
   *   overrides an abstract method to implement the main function of class.
   **************************************************************************/
    public void run() {
        startLoad();
    }

    public static void main(String[] args) {
        if (args != null) {
            GenepixFileParser genepixFileLoader1 = new GenepixFileParser(args[0]);
        } else {
            GenepixFileParser genepixFileLoader1 = new GenepixFileParser();
        }
    }

    /****************************************************************************
   * <b>Description: </b>
   *   calculate density for a channel
   * <p><b>Parameters: </b>
   * <br> tuple -- all information in one row
   * <br> med -- address for either channel A or B median column
   * <br> area -- spot area in number of pixels
   * <br> backg -- address for background medien column
   * <p><b>Return: </b> the density
   ***************************************************************************/
    private String calculateDensity(String[] tuple, int med, int sat, int backg) {
        float fMedian, bMedian, fPix, bPix, fSat;
        int temp;
        String den = new String("");
        fMedian = Float.parseFloat(tuple[med]);
        bMedian = Float.parseFloat(tuple[backg]);
        fPix = Float.parseFloat(tuple[fPixAddr]);
        fSat = Float.parseFloat(tuple[sat]);
        fSat = fSat / 100;
        temp = (int) (fPix * (fMedian - bMedian) * (1 - fSat));
        den += temp;
        return den;
    }

    /****************************************************************************
   * <b>Description: </b>
   *    assignes values to the keys for searching right columns.
   * <p><b>Parameter: </b>
   * <br> wvs --- the wave lenghts.
   ***************************************************************************/
    private void defineKeys(String[] wvs) {
        chA = new String("\"F" + wvs[0] + " Median\"");
        chB = new String("\"F" + wvs[1] + " Median\"");
        chAs = new String("\"F" + wvs[0] + " % Sat.\"");
        chBs = new String("\"F" + wvs[1] + " % Sat.\"");
        bgChA = new String("\"B" + wvs[0] + " Median\"");
        bgChB = new String("\"B" + wvs[1] + " Median\"");
        chAn = new String("F" + wvs[0] + " Median");
        chBn = new String("F" + wvs[1] + " Median");
        chAsn = new String("F" + wvs[0] + " % Sat.");
        chBsn = new String("F" + wvs[1] + " % Sat.");
        bgChAn = new String("B" + wvs[0] + " Median");
        bgChBn = new String("B" + wvs[1] + " Median");
    }

    /****************************************************************************
   * <b>Description: </b>
   *    find the address of the column that is interested.
   * <p><b>Parameters: </b>
   * <br> str -- a line of the file
   * <br> key -- column name to be searched
   * <br> noQuato -- column name withouth quato.
   * <P><b>Return: </b> the index of the column
   ****************************************************************************/
    private int findRightCol(String str[], String key, String noQuato) {
        int addr = 0;
        int size = str.length;
        int i;
        String temp = new String("");
        for (i = 0; i < str.length; i++) {
            if (str[i].equalsIgnoreCase(key) || str[i].equalsIgnoreCase(noQuato)) {
                addr = i;
                i = str.length;
            }
        }
        return addr;
    }

    /****************************************************************************
   * <b>Description: </b>
   *    find the wave lengths from the header information
   * <p><b>Parameters: </b>
   * <br> str -- a line of the file that contains wave lengths.
   * <P><b>Return: </b> the wave lengths as an array.
   ****************************************************************************/
    private String[] getWaveLengths(String str) {
        int num;
        String wv;
        String tempWave[] = new String[2];
        String wavStr = new String(str.substring(str.indexOf('=') + 1, str.lastIndexOf('\"')));
        StringTokenizer token = new StringTokenizer(wavStr);
        num = 0;
        while (token.hasMoreTokens()) {
            wv = token.nextToken();
            if (isNumber(wv)) {
                tempWave[num] = new String(wv);
                num++;
            }
        }
        return tempWave;
    }

    /****************************************************************************
   *<b>Description: </b>
   *  check if a parameter is a character.
   *<p><b>Parameter: </b>
   *<br> x --- the input to be checked.
   *<p><b>Return: </b> true if the input is a character; otherwise, false.
   ****************************************************************************/
    private boolean isChar(char x) {
        boolean b = true;
        switch(x) {
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '0':
                b = false;
                break;
            default:
                b = true;
        }
        return b;
    }

    /****************************************************************************
   *<b>Description: </b>
   *  check if a input string is a number.
   *<p><b>Parameter: </b>
   *<br> x --- the input to be checked.
   *<p><b>Return: </b> true if the input is a character; otherwise, false.
   ****************************************************************************/
    private boolean isNumber(String x) {
        boolean b = true;
        int length = x.length();
        for (int i = 0; i < length; i++) {
            if (isChar(x.charAt(i))) {
                b = false;
                i = length;
            }
        }
        return b;
    }

    /****************************************************************************
   *<b>Description: </b>
   *  check if a string contains wave length information.
   *<p><b>Parameter: </b>
   *<br> str --- the input to be checked.
   *<p><b>Return: </b> true if the input contains the info; otherwise, false.
   ****************************************************************************/
    private boolean isWaveLengthDefination(String str) {
        String temp = str;
        if (temp.charAt(0) == '\"' && temp.indexOf('=') > 0) {
            temp = temp.substring(1, temp.indexOf('='));
            if (temp.equalsIgnoreCase(WAVE_KEY_1) || temp.equalsIgnoreCase(WAVE_KEY_2)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /***************************************************************************
   * <b>Description: </b>
   *    reads the genepix file into a vector; each line in the file is an element
   *    in the vector.
   * <p>Return: </b> the contents of genepix file.
   ***************************************************************************/
    private Vector readGenePixFile() {
        FileInputStream fileInput;
        BufferedReader buf = null;
        Vector temp = new Vector(2000, 500);
        String aLine = new String("");
        try {
            fileInput = new FileInputStream(gprFile);
            buf = new BufferedReader(new InputStreamReader(fileInput));
        } catch (IOException ex) {
            hasError = true;
            errorMsg = "Failed to open " + gprFile.getName() + ": " + ex.getMessage();
            return temp;
        }
        while (true) {
            try {
                aLine = buf.readLine();
                if (aLine == null) break;
                temp.add(aLine);
            } catch (IOException e) {
                hasError = true;
                errorMsg = "Failed to read " + gprFile.getName() + ": " + e.getMessage();
                temp.removeAllElements();
                return temp;
            }
        }
        return temp;
    }

    /**************************************************************************
   * <b>Description: </b>
   *   remove the all end spaces from a string, if it contains.
   * <p><b>Parameter: </b>
   * <br>  str -- the string that has multiple spaces at the end.
   * <p><b>Return: </b>  a string without space at the end.
   ***************************************************************************/
    private String removeAllEndSpaces(String str) {
        String temp = new String(str);
        if (temp.length() > 0) {
            while (temp.charAt(temp.length() - 1) == ' ') {
                temp = temp.substring(0, temp.length() - 1);
                if (temp.length() <= 0) break;
            }
        }
        return temp;
    }

    /**************************************************************************
   * <b>Description: </b>
   *   remove the all head spaces from a string, if it contains.
   * <p><b>Parameter: </b>
   * <br>  str -- the string that has multiple spaces at the end.
   * <p><b>Return: </b>  a string without space at the end.
   ***************************************************************************/
    private String removeAllHeadSpaces(String str) {
        int length, i;
        boolean spc = false;
        String temp = new String(str);
        length = temp.length();
        if (length > 0) {
            while (temp.charAt(0) == ' ') {
                temp = temp.substring(1);
                if (temp.length() <= 0) break;
            }
        }
        return temp;
    }

    /****************************************************************************
   *<b>Description: </b>
   *   splits a string and set the elemet to an array.
   *<p><b>Parameter: </b>
   *<br> str --- the input to be splited.
   *<br> num --- the number of columns.
   *<p><b>Return: </b> the name.
   ****************************************************************************/
    private String[] separateLine(String str) {
        String name[];
        String tempStr = new String("");
        StringSplitter spliter = new StringSplitter('\t');
        spliter.init(str);
        int num = spliter.countTokens() + 1;
        int length = 0;
        name = new String[num];
        if (num > 1) {
            while (spliter.hasMoreTokens()) {
                tempStr = spliter.nextToken();
                tempStr = removeAllEndSpaces(tempStr);
                tempStr = removeAllHeadSpaces(tempStr);
                name[length++] = tempStr;
            }
        }
        return name;
    }

    /****************************************************************************
   * <b>Description: </b>
   *   starts the process to convert the file and ensure output to be ready.
   **************************************************************************/
    private void startLoad() {
        boolean going, noError;
        String aLine = new String("");
        String densA = new String("");
        String densB = new String("");
        String name = new String("");
        String id = new String("");
        String tavFileName;
        String colValues[];
        String colNames[];
        String waves[] = new String[2];
        int hLines;
        int max = 0;
        int maxRow = 0;
        int maxCol = 0;
        int maxMetaCol, block, row, col, metaRow, metaCol, slideRow, slideCol;
        int i;
        int flagInt, blockIndex, colIndex, rowIndex, nameIndex, idIndex, flagIndex, flag;
        int f635m, b635m, f635s, f532m, f532s, b532m;
        int fSize;
        Vector gprFileContents;
        Vector tavLine;
        gprFileContents = readGenePixFile();
        taskEnd = gprFileContents.size();
        if (taskEnd <= 0) {
            return;
        }
        counter = 0;
        noError = true;
        fSize = gprFileContents.size();
        hLines = 0;
        going = true;
        while (going) {
            aLine = (String) gprFileContents.elementAt(hLines);
            if (aLine.charAt(0) == '"' || isChar(aLine.charAt(0))) {
                if (isWaveLengthDefination(aLine)) {
                    waves = getWaveLengths(aLine);
                }
                going = true;
                hLines++;
            } else {
                if (hLines == FIRST_NUM) {
                    going = true;
                    hLines++;
                } else {
                    going = false;
                }
            }
        }
        defineKeys(waves);
        aLine = (String) gprFileContents.elementAt(hLines - 1);
        colNames = separateLine(aLine);
        blockIndex = findRightCol(colNames, BLOCK, BLOCK_N);
        colIndex = findRightCol(colNames, COLUMN, COLUMN_N);
        rowIndex = findRightCol(colNames, RW, ROW_N);
        nameIndex = findRightCol(colNames, NAME, NAME_N);
        idIndex = findRightCol(colNames, ID, ID_N);
        flagIndex = findRightCol(colNames, FLAGS, FLAGS_N);
        aLine = (String) gprFileContents.elementAt(fSize - 1);
        colValues = separateLine(aLine);
        if (colValues.length > 2) {
            max = Integer.parseInt(colValues[blockIndex]);
            maxCol = Integer.parseInt(colValues[colIndex]);
            maxRow = Integer.parseInt(colValues[rowIndex]);
        } else {
            JOptionPane.showMessageDialog(null, "You might add extra space at the end of file.\n(" + gprFile.getName() + "). \nPlease remove it and reload again.", "GenePix File Loader", JOptionPane.ERROR_MESSAGE);
            noError = false;
        }
        if (noError) {
            if (max <= 12) {
                maxMetaCol = 2;
            } else {
                maxMetaCol = 4;
            }
            noError = true;
            if (noError) {
                fPixAddr = findRightCol(colNames, F_PIX, F_PIX_N);
                f635m = findRightCol(colNames, chA, chAn);
                f635s = findRightCol(colNames, chAs, chAsn);
                b635m = findRightCol(colNames, bgChA, bgChAn);
                f532m = findRightCol(colNames, chB, chBn);
                f532s = findRightCol(colNames, chBs, chBsn);
                b532m = findRightCol(colNames, bgChB, bgChBn);
                flagIndex = findRightCol(colNames, FLAGS, FLAGS_N);
                tavFile = new Vector(fSize - hLines, (fSize - hLines) >> 2);
                counter = hLines;
                for (i = hLines; i < fSize; i++) {
                    aLine = (String) gprFileContents.elementAt(i);
                    colValues = separateLine(aLine);
                    block = Integer.parseInt(colValues[blockIndex]);
                    col = Integer.parseInt(colValues[colIndex]);
                    row = Integer.parseInt(colValues[rowIndex]);
                    flag = Integer.parseInt(colValues[flagIndex]);
                    densA = calculateDensity(colValues, f532m, f532s, b532m);
                    densB = calculateDensity(colValues, f635m, f635s, b635m);
                    metaRow = ((block - 1) / maxMetaCol) + 1;
                    metaCol = ((block - 1) % maxMetaCol) + 1;
                    slideRow = (metaRow - 1) * maxRow + row;
                    slideCol = (metaCol - 1) * maxCol + col;
                    name = colValues[nameIndex];
                    id = colValues[idIndex];
                    tavLine = new Vector(9);
                    tavLine.add(new Integer(densA));
                    tavLine.add(new Integer(densB));
                    tavLine.add(new Integer(slideRow));
                    tavLine.add(new Integer(slideCol));
                    tavLine.add(new Integer(metaRow));
                    tavLine.add(new Integer(metaCol));
                    tavLine.add(name);
                    tavLine.add(id);
                    tavLine.add(new Integer(flag));
                    tavFile.add(tavLine);
                    counter++;
                }
                ready = true;
                counter = taskEnd;
            }
        }
    }
}
