package info.olteanu.utils.sort;

import info.olteanu.utils.*;
import info.olteanu.utils.chron.*;
import info.olteanu.utils.io.*;
import java.io.*;
import java.util.*;

public class GroupByPrefix {

    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.err.println("Merge Sort using temporary files");
            System.err.println("Usage: ");
            System.err.println(" <program> <no. pieces> <marker> <in file> <out file>");
            System.exit(1);
        }
        Chronometer c = new Chronometer(true);
        group(args[2], args[3], Integer.parseInt(args[0]), args[1]);
        System.err.println("Total time: " + (c.getValue() / 1000.0) + " seconds");
    }

    public static String getPartName(int partNo, String fileName) {
        return fileName + ".part" + partNo;
    }

    private static void group(String fileIn, String fileOut, int noPieces, String marker) throws IOException {
        splitFile(fileIn, fileOut, noPieces, marker);
        sortAndMergePieces(fileOut, noPieces);
    }

    public static void splitFile(String fileNameIn, String fileNameMaskOut, int partCnt, String marker) throws IOException {
        BufferedPrintStream[] out = new BufferedPrintStream[partCnt];
        for (int i = 0; i < out.length; i++) out[i] = new BufferedPrintStream(new PrintStream(IOTools.getOutputStream(getPartName(i, fileNameMaskOut))));
        BufferedReader file = new BufferedReader(new InputStreamReader(IOTools.getInputStream(fileNameIn)));
        String lineFile;
        while ((lineFile = file.readLine()) != null) out[Math.abs(StringTools.isNull(StringTools.substringBefore(lineFile, marker), lineFile).hashCode()) % out.length].println(lineFile);
        file.close();
        for (int i = 0; i < out.length; i++) out[i].close();
    }

    public static void sortAndMergePieces(String fileName, int length) throws IOException {
        BufferedPrintStream outFile = new BufferedPrintStream(IOTools.getOutputStream(fileName));
        for (int i = 0; i < length; i++) sortAndDump(getPartName(i, fileName), outFile);
        outFile.close();
    }

    private static void sortAndDump(String fileName, BufferedPrintStream out) throws IOException {
        BufferedReader fileTmp = new BufferedReader(new InputStreamReader(IOTools.getInputStream(fileName)));
        String lineFile;
        ArrayList<String> v = new ArrayList<String>();
        while ((lineFile = fileTmp.readLine()) != null) v.add(lineFile);
        fileTmp.close();
        new File(fileName).delete();
        Collections.sort(v);
        for (String k : v) out.println(k);
    }
}
