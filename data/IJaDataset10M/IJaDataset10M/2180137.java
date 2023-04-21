package auo.cms.hsvinteger;

import shu.cms.colorformat.ascii.*;
import java.io.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public class HSVIPComparator {

    public static int[] getDatas(String filename) throws IOException {
        ASCIIFileFormatParser parser = new ASCIIFileFormatParser(filename);
        ASCIIFileFormat asciiFormat = parser.parse();
        int size = asciiFormat.size();
        int[] datas = new int[size];
        for (int x = 0; x < size; x++) {
            ASCIIFileFormat.LineObject lo = asciiFormat.getLine(x);
            datas[x] = Integer.parseInt(lo.stringArray[0]);
        }
        return datas;
    }

    public static int[][] getDatasArray(String filename) throws IOException {
        ASCIIFileFormatParser parser = new ASCIIFileFormatParser(filename);
        ASCIIFileFormat asciiFormat = parser.parse();
        int size = asciiFormat.size();
        int[][] datas = new int[size][];
        for (int x = 0; x < size; x++) {
            ASCIIFileFormat.LineObject lo = asciiFormat.getLine(x);
            String[] stringArray = lo.stringArray;
            int width = stringArray.length;
            datas[x] = new int[width];
            for (int y = 0; y < 3; y++) {
                datas[x][y] = Integer.parseInt(stringArray[y]);
            }
            if (x % 10000 == 0) {
                System.gc();
            }
        }
        return datas;
    }

    public static void oldCompare(String[] args) throws IOException {
        String dumpFilename = "dump normal.txt";
        String hardwareDumpDir = "normal/";
        int[][] softwareDatas = getDatasArray(dumpFilename);
        System.out.println("sw load ok");
        int[] new_R = getDatas(hardwareDumpDir + "new_R.txt");
        int[] new_G = getDatas(hardwareDumpDir + "new_G.txt");
        int[] new_B = getDatas(hardwareDumpDir + "new_B.txt");
        System.out.println("hw load ok");
        int[][] hardwareData = new int[][] { new_R, new_G, new_B };
        int size = softwareDatas.length;
        for (int[] data : hardwareData) {
            if (data.length != size) {
                throw new IllegalStateException("");
            }
        }
        int width = softwareDatas[0].length;
        int errorCount = 0;
        System.out.println("begin compare");
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < 3; y++) {
                if (softwareDatas[x][y + 10] != hardwareData[x][y]) {
                    errorCount++;
                    System.out.println(x);
                }
            }
        }
        System.out.println("comapre ok");
        System.out.println("Error Count: " + errorCount);
    }

    public static void newCompare(String[] args) {
    }

    public static void main(String[] args) throws IOException {
        oldCompare(args);
    }
}
