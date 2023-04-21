package edu.ucla.stat.SOCR.analyses.command;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.data.DataCase;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;
import edu.ucla.stat.SOCR.analyses.result.TwoIndependentFriedmanResult;
import edu.ucla.stat.SOCR.analyses.result.TwoIndependentKruskalWallisResult;
import edu.ucla.stat.SOCR.util.AnalysisUtility;

public class TwoIndependentKruskalWallisCSV {

    private static final String MISSING_MARK = ".";

    public static void main(String[] args) {
        String fileName1 = null;
        boolean filesLoaded = false;
        boolean header = false;
        System.out.println("Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLine");
        try {
            fileName1 = args[0];
            filesLoaded = true;
        } catch (Exception e) {
        }
        if (!filesLoaded) {
            return;
        }
        int independentLength = 4;
        String length = "4";
        boolean len = false;
        if (args.length >= 2) {
            if (args[1].equals("-h")) header = true; else {
                length = args[1];
                len = true;
            }
            if (args.length == 3) {
                length = args[2];
                len = true;
            }
        }
        try {
            if (len) independentLength = (Double.valueOf((String) length)).intValue();
        } catch (Exception e) {
        }
        if (independentLength <= 1) {
            System.out.println("Error! At least 2 groups.");
            return;
        }
        StringTokenizer st = null;
        String[] input = new String[independentLength];
        int xLength = 0;
        ArrayList[] xList = new ArrayList[independentLength];
        double[][] xDataArray = new double[independentLength][xLength];
        for (int i = 0; i < xList.length; i++) xList[i] = new ArrayList<String>();
        String line = null;
        boolean read = true;
        if (header) read = false;
        String[] varHeader = new String[independentLength];
        try {
            BufferedReader bReader = new BufferedReader(new FileReader(fileName1));
            while ((line = bReader.readLine()) != null) {
                st = new StringTokenizer(line, ",; \t");
                try {
                    for (int k = 0; k < independentLength; k++) {
                        input[k] = st.nextToken().trim();
                        if (header && !read) varHeader[k] = input[k];
                        if (read && !input[k].equalsIgnoreCase(MISSING_MARK)) {
                            xList[k].add(input[k]);
                        }
                    }
                    read = true;
                } catch (NoSuchElementException e) {
                    System.out.println(Utility.getErrorMessage("Friedman Test"));
                    return;
                } catch (Exception e) {
                    System.out.println(Utility.getErrorMessage("Friedman Test"));
                    return;
                }
            }
        } catch (Exception e) {
        }
        xLength = xList[1].size();
        double[] xData = null;
        if (!header) {
            for (int k = 0; k < independentLength; k++) varHeader[k] = "variable " + k;
        }
        Data data = new Data();
        for (int i = 0; i < independentLength; i++) {
            xData = new double[xLength];
            for (int j = 0; j < xLength; j++) {
                try {
                    xData[j] = (Double.valueOf((String) xList[i].get(j))).doubleValue();
                } catch (NumberFormatException e) {
                    System.out.println("Line " + (j + 1) + " is not in correct numerical format.");
                    return;
                }
            }
            xDataArray[i] = xData;
            data.appendX(varHeader[i], xDataArray[i], DataType.QUANTITATIVE);
        }
        TwoIndependentKruskalWallisResult result;
        result = null;
        String className = null;
        String[] groupNames = null;
        double[] rankSum = null;
        double tStat = 0, s2 = 0, cp = 0;
        String dataAndRankString = null;
        String[] dataAndRankStringArray = null;
        int[] groupCount = null;
        String df = null;
        String[] multipleComparisonInfo = null;
        String multipleComparisonHeader = null;
        String sampleSize = null;
        try {
            result = (TwoIndependentKruskalWallisResult) data.getAnalysis(AnalysisType.TWO_INDEPENDENT_KRUSKAL_WALLIS);
        } catch (Exception e) {
        }
        try {
            sampleSize = result.getSampleSize();
        } catch (Exception e) {
        }
        try {
            groupNames = result.getGroupNameList();
        } catch (NullPointerException e) {
        }
        try {
            rankSum = result.getRankSumList();
        } catch (NullPointerException e) {
        }
        try {
            tStat = result.getTStat();
        } catch (NullPointerException e) {
        }
        try {
            cp = result.getCriticalValue();
        } catch (NullPointerException e) {
        }
        try {
            s2 = result.getSSqaured();
        } catch (NullPointerException e) {
        }
        try {
            df = result.getDegreesOfFreedom();
        } catch (NullPointerException e) {
        }
        try {
            groupCount = result.getGroupCount();
        } catch (NullPointerException e) {
        }
        try {
            dataAndRankString = result.getDataRankInformation();
        } catch (NullPointerException e) {
        }
        try {
            dataAndRankStringArray = result.getDataRankSepratedInformation();
        } catch (NullPointerException e) {
        }
        try {
            multipleComparisonInfo = result.getMultipleComparisonInformation();
        } catch (NullPointerException e) {
        }
        try {
            multipleComparisonHeader = result.getMultipleComparisonHeader();
        } catch (NullPointerException e) {
        }
        if (result == null) return;
        System.out.println("\n");
        System.out.println("\n\tNumber of Groups = " + independentLength);
        double groupLength = groupNames.length;
        System.out.println("\n\tTotal Number of Cases = " + sampleSize);
        for (int i = 0; i < dataAndRankStringArray.length; i++) {
            System.out.println("\n\n\tGroup = " + groupNames[i] + ": " + dataAndRankStringArray[i]);
        }
        System.out.println("\n\n");
        for (int i = 0; i < groupLength; i++) {
            System.out.println("\n\tGroup " + groupNames[i] + ":\tSample Size = " + groupCount[i] + "\tRank Sum = " + rankSum[i]);
        }
        System.out.println("\n\n\tSignificance Level = 0.05");
        System.out.println("\n\tDegrees of Freedom = " + df);
        System.out.println("\n\tCritical Value = " + cp);
        System.out.println("\n\tT-Statistics = " + tStat);
        System.out.println("\n\tS * S = " + s2);
        System.out.println("\n\n\tNotation: Ri -- Rank of group i; ni -- size of group i.\n");
        System.out.println("\n\t\t\t" + multipleComparisonHeader + "\n");
        for (int i = 0; i < multipleComparisonInfo.length; i++) {
            if (multipleComparisonInfo[i] != null) System.out.println("\n\t" + multipleComparisonInfo[i]);
        }
        System.out.println("\n");
    }
}
