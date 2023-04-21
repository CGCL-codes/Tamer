package org.systemsbiology.apps.utils.targetanalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Args:  an interact file (in need of RT correction and a regression equation 
 * 		(slope and intercept) (from regressionInfluences.R, file: Stats_[expnames].tsv)
 * 
 * Usage:  java RetentionTimeCorrect.java [Stats file from R] [interact file] [outliers file} [new interact output file]
 * 
 * Description: In this module, the interact.pep.xls file retention times will be shifted
 * 	in order to utilize this interact file in combination with another one with reliable
 * 	retention times.  Outliers are also removed.
 * 
 * Additional info:  regressionInfluences.R produces the regression coefficients as well as a list of outliers
 * 
 * @author Micheleen Harris
 */
public class RetentionTimeShiftApp {

    public static void main(String[] args) {
        if (args.length != 4) {
            printUsage("Incorrect number of arguments");
        }
        try {
            correctRT(args[0], args[1], args[2], args[3]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void correctRT(String statfile, String ifile, String outlierF, String ofile) throws IOException {
        BufferedReader readStat = new BufferedReader(new FileReader(statfile));
        BufferedReader readIn = new BufferedReader(new FileReader(ifile));
        BufferedReader readOutlier = new BufferedReader(new FileReader(outlierF));
        PrintWriter OUT = new PrintWriter(new FileWriter(ofile));
        String statHeader = readStat.readLine();
        String statLine = readStat.readLine();
        String[] stats = statLine.split("\t");
        float slope = Float.valueOf(stats[0]);
        float intercept = Float.valueOf(stats[1]);
        float rsq = Float.valueOf(stats[2]);
        float numOut = Float.valueOf(stats[3]);
        String readStr;
        int idxOutlr = -1;
        if ((readStr = readOutlier.readLine()) != null) {
            idxOutlr = getIndex(readStr, "peptide");
        } else {
            printUsage("Problem with outlier file");
        }
        ArrayList<String> outliers = new ArrayList<String>();
        while ((readStr = readOutlier.readLine()) != null) {
            readStr = readStr.replaceAll("\"", "");
            String[] lineA = readStr.split("\t");
            outliers.add(lineA[idxOutlr]);
            System.out.println(lineA[idxOutlr]);
        }
        int idxRT = -1;
        int idxPep = -1;
        if ((readStr = readIn.readLine()) != null) {
            idxRT = getIndex(readStr, "retention_time_sec");
            idxPep = getIndex(readStr, "peptide");
            OUT.println(readStr);
        } else {
            printUsage("Interact file missing required fields");
        }
        while ((readStr = readIn.readLine()) != null) {
            String[] lineSpl = readStr.split("\t");
            if (!(outliers.contains(lineSpl[idxPep]))) {
                Float oldRT = new Float(lineSpl[idxRT]);
                Float newRT = (oldRT - intercept) / slope;
                lineSpl[idxRT] = newRT.toString();
                if (newRT >= 0) {
                    OUT.println(join(lineSpl));
                }
            }
        }
        OUT.close();
    }

    private static void printUsage(String msg) {
        System.err.println(msg + "\n");
        System.err.println("Usage:  java RetentionTimeCorrect.java [Stats file from R] [interact file] [new interact output file]");
        System.exit(0);
    }

    private static int getIndex(String line, String field) {
        line = line.replaceAll("\"", "");
        ArrayList<String> fields = new ArrayList<String>(Arrays.asList(line.split("\t")));
        int idx = fields.indexOf(field);
        return idx;
    }

    private static String join(String[] strArray) {
        String s = "";
        for (int i = 0; i < strArray.length; i++) {
            s = s.concat(strArray[i] + "\t");
        }
        return s;
    }
}
