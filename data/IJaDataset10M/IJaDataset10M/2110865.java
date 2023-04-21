package cn.edu.dutir.test.unit.labtraining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.aliasi.util.Strings;
import cn.edu.dutir.utility.Constants;
import cn.edu.dutir.utility.SymbolTable;

public class ClassifierEvaluator {

    /**
	 * Standard Result
	 */
    private static Map<String, String> stdMap;

    /**
	 * Classification result
	 */
    private static Map<String, String> retMap;

    private final String mClassLabels[];

    private final int mClassCount;

    private final int mClassReferenceCounts[];

    private final int mClassResultCounts[];

    private final int mClassCorrectCounts[];

    private final double mPrecs[];

    private final double mRecalls[];

    private final double mFscores[];

    private SymbolTable mSymbolTable;

    private double mOverallPrec;

    private double mOverallRecall;

    private double mOverallFscore;

    public ClassifierEvaluator(String classLabels[]) {
        if (classLabels == null || classLabels.length <= 0) {
            String msg = "classLabels should at least contain one class, but found classLabels = " + classLabels;
            new IllegalArgumentException(msg);
        }
        mClassCount = classLabels.length;
        mClassLabels = classLabels;
        mClassReferenceCounts = new int[mClassCount];
        mClassResultCounts = new int[mClassCount];
        mClassCorrectCounts = new int[mClassCount];
        mPrecs = new double[mClassCount];
        mRecalls = new double[mClassCount];
        mFscores = new double[mClassCount];
        mSymbolTable = new SymbolTable(0);
        for (int i = 0; i < mClassCount; i++) {
            mSymbolTable.getOrAddSymbolInteger(mClassLabels[i]);
        }
        Arrays.fill(mClassReferenceCounts, 0);
        Arrays.fill(mClassResultCounts, 0);
        Arrays.fill(mClassCorrectCounts, 0);
    }

    public Map<String, String> loadMap(File inFile) throws IOException {
        return loadMap(inFile, Constants.DEFAULT_DEMILTER);
    }

    /**
	 * Load a map from a plain text file.
	 * 
	 * @param inFile
	 * @param delimiter
	 * @return
	 * @throws IOException
	 */
    public Map<String, String> loadMap(File inFile, String delimiter) throws IOException {
        if (inFile == null || !inFile.exists() || !inFile.isFile()) {
            String msg = "Input file must exist and be a plain file";
            new IllegalArgumentException(msg);
        }
        BufferedReader bf = null;
        Map<String, String> map = new HashMap<String, String>();
        int lineNumber = 1;
        try {
            bf = new BufferedReader(new FileReader(inFile));
            String text = null;
            while ((text = bf.readLine()) != null) {
                String tokens[] = Strings.normalizeWhitespace(text).split(delimiter);
                if (tokens.length != 2) {
                    System.err.println("Format error at line " + lineNumber + " : " + text);
                    continue;
                }
                if (map.containsKey(tokens[1])) {
                    System.err.println("Duplicate key found at line " + lineNumber + " : " + text);
                } else {
                    map.put(tokens[1], tokens[0]);
                }
                lineNumber++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            bf.close();
        }
        return map;
    }

    public void reset() {
        Arrays.fill(mClassReferenceCounts, 0);
        Arrays.fill(mClassCorrectCounts, 0);
        Arrays.fill(mClassResultCounts, 0);
    }

    public void statistic(Map<String, String> mapA, Map<String, String> mapB) {
        reset();
        Iterator<Map.Entry<String, String>> iter = mapA.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            String valueA = entry.getValue();
            String valueB = mapB.get(entry.getKey());
            if (valueA == null || valueB == null) {
                String msg = "Category labels can not be null";
                System.err.println(msg);
            }
            int classId = mSymbolTable.symbolToID(valueA);
            if (classId >= 0) {
                if (valueA.equalsIgnoreCase(valueB)) {
                    mClassCorrectCounts[classId]++;
                    mClassReferenceCounts[classId]++;
                    mClassResultCounts[classId]++;
                } else {
                    mClassReferenceCounts[classId]++;
                    classId = mSymbolTable.symbolToID(valueB);
                    if (classId >= 0) {
                        mClassResultCounts[classId]++;
                    }
                }
            }
        }
    }

    public void evaluateClassifier(File retFile, File stdFile) throws IOException {
        evaluateClassifier(retFile, stdFile, System.out, Constants.DEFAULT_DEMILTER);
    }

    public void evaluateClassifier(File retFile, File stdFile, String delimiter) throws IOException {
        evaluateClassifier(retFile, stdFile, System.out, delimiter);
    }

    public void evaluateClassifier(File retFile, File stdFile, OutputStream os) throws IOException {
        evaluateClassifier(retFile, stdFile, os, Constants.DEFAULT_DEMILTER);
    }

    public void evaluateClassifier(File retFile, File stdFile, OutputStream os, String delimiter) throws IOException {
        os.write((retFile.getName() + "\r\n").getBytes());
        reset();
        stdMap = loadMap(stdFile, delimiter);
        retMap = loadMap(retFile, delimiter);
        statistic(retMap, stdMap);
        for (int i = 0; i < mClassCount; i++) {
            mPrecs[i] = (mClassCorrectCounts[i] + 0.0d) / mClassResultCounts[i];
            mRecalls[i] = (mClassCorrectCounts[i] + 0.0d) / mClassReferenceCounts[i];
            mFscores[i] = 2 * mPrecs[i] * mRecalls[i] / (mPrecs[i] + mRecalls[i]);
        }
        int sum = sum(mClassCorrectCounts);
        mOverallPrec = sum / (sum(mClassResultCounts) + 0.0d);
        mOverallRecall = sum / (sum(mClassReferenceCounts) + 0.0d);
        mOverallFscore = 2 * mOverallPrec * mOverallRecall / (mOverallPrec + mOverallRecall);
        os.write(toString().getBytes());
    }

    public void eval(File stdFile, File retFile) throws IOException {
        eval(stdFile, retFile, System.out, Constants.DEFAULT_DEMILTER);
    }

    public void eval(File stdFile, File retFile, OutputStream os) throws IOException {
        eval(stdFile, retFile, os, Constants.DEFAULT_DEMILTER);
    }

    public void eval(File stdFile, File retFile, String delimmiter) throws IOException {
        eval(stdFile, retFile, System.out, delimmiter);
    }

    public void eval(File inDir, File stdFile, OutputStream os, String delimiter) throws IOException {
        if (inDir.isDirectory()) {
            File fileList[] = inDir.listFiles();
            for (File file : fileList) {
                eval(file, stdFile, os, delimiter);
            }
        } else {
            evaluateClassifier(inDir, stdFile, os, delimiter);
        }
    }

    public void addSpamMeasure(StringBuffer sb) {
        int hamId = mSymbolTable.symbolToID("ham");
        int spamId = mSymbolTable.symbolToID("spam");
        double hm = (mClassReferenceCounts[hamId] - mClassCorrectCounts[hamId] + 0.0d) / mClassReferenceCounts[hamId];
        double sm = (mClassReferenceCounts[spamId] - mClassCorrectCounts[spamId] + 0.0d) / mClassReferenceCounts[spamId];
        double lam = logit_1(logit(hm) + logit(sm) / 2) * 100;
        sb.append("[ TREC Spam Track Measures ]: \r\n");
        sb.append("hm%: " + hm * 100 + ", sm%: " + sm * 100 + ", lam%: " + lam + "\r\n");
    }

    public double logit(double x) {
        return Math.log(x / (1 - x));
    }

    public double logit_1(double x) {
        double tmp = Math.exp(x);
        return tmp / (tmp + 1);
    }

    public int sum(int array[]) {
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[ The performance of the classifiers(precison-recall) ]: \r\n");
        for (int i = 0; i < mClassCount; i++) {
            sb.append(mClassLabels[i] + ":\tprecison: " + mPrecs[i] + ", recall: " + mRecalls[i] + ", Fscore: " + mFscores[i] + "\r\n");
        }
        sb.append("[ Overall performance ]:\r\nPrecision : " + mOverallPrec + ", Recall: " + mOverallRecall + ", F-Score: " + mOverallFscore + "\r\n");
        addSpamMeasure(sb);
        sb.append("\r\n");
        return sb.toString();
    }

    public static void main(String args[]) throws IOException {
        String classLabels[] = { "spam", "ham" };
        ClassifierEvaluator evaluator = new ClassifierEvaluator(classLabels);
        File stdFile = new File("E:/ѵ���ƻ�/Reference.txt");
        File retFile = new File("E:/ѵ���ƻ�/BBR����ύ/");
        evaluator.eval(retFile, stdFile, Constants.DEFAULT_DEMILTER);
    }
}
