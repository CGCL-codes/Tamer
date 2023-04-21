package org.xmlcml.textalign.algorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.IntArray;

/** probably obsolete
 * 
 * @author pm286
 *
 */
public class FileAligner {

    private static Logger LOG = Logger.getLogger(FileAligner.class);

    static {
        LOG.setLevel(Level.DEBUG);
    }

    public double GAP0 = -5;

    DynamicAligner aligner;

    public FileAligner() {
    }

    public void setFiles(String filename1, String filename2) {
        String[][] arrayOfStrings = new String[2][];
        arrayOfStrings[0] = readStringsFromFile(filename1);
        arrayOfStrings[1] = readStringsFromFile(filename2);
        AlphabetMatrix am = new AlphabetMatrix();
        double penaltyMatrix[][] = am.fillPenaltyMatrixWithInterStringDistances(arrayOfStrings);
        aligner = new DynamicAligner();
        aligner.setPenalties(penaltyMatrix);
    }

    private String[] readStringsFromFile(String fileName) {
        List<String> v = new ArrayList<String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            while (true) {
                String s = br.readLine();
                if (s == null) break;
                v.add(s);
            }
            br.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String[] ss = new String[v.size()];
        for (int i = 0; i < v.size(); i++) {
            ss[i] = (String) v.get(i);
        }
        return ss;
    }

    public void align(double gap) {
        aligner.getAlphabetMatrix().setStringStringGapPenalty(gap);
        aligner.align();
    }

    public IntegerAlignment extractAlignment() {
        return aligner.extractAlignmentFromMatrix();
    }
}
