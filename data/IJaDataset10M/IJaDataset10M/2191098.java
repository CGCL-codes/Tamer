package org.phramer.tools;

import info.olteanu.utils.*;
import info.olteanu.utils.io.*;
import java.io.*;
import java.util.zip.*;

public class CapProbabilityInTranslationTable {

    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.err.println("Cap probabitity in a translation table");
            System.err.println("Parameters: <input translation table> <output translation table> <column> <minvalue>");
            System.exit(1);
        }
        String fileTableIn = args[0];
        String fileTableOut = args[1];
        int column = Integer.parseInt(args[2]);
        String minValue = args[3];
        double minValueD = Double.parseDouble(minValue);
        BufferedPrintStream outFile = new BufferedPrintStream(IOTools.getOutputStream(fileTableOut));
        BufferedReader inputFile = new BufferedReader(new InputStreamReader(IOTools.getInputStream(fileTableIn)));
        String lineFile;
        while ((lineFile = inputFile.readLine()) != null) {
            String preP = StringTools.lastSubstringBefore(lineFile, " ||| ");
            String p = StringTools.lastSubstringAfter(lineFile, " ||| ");
            String[] pTok = StringTools.tokenize(p);
            double pC = Double.parseDouble(pTok[column - 1]);
            if (pC < minValueD) pTok[column - 1] = minValue;
            outFile.println(preP + " ||| " + StringTools.untokenize(pTok));
        }
        inputFile.close();
        outFile.close();
    }
}
