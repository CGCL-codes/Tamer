package hcrypto.analyzer;

import applications.cryptotoolj.AnalyzerFrame;
import hcrypto.cipher.*;
import hcrypto.provider.*;
import hcrypto.engines.*;
import hcrypto.analyzer.tool.*;

/**
 *  Description: This class assumes that the text it is passed
 *  is encrypted with an Affine engine. 
 *
 */
public class AffineAnalyzer extends CryptoAnalyzer implements ExpertAnalyzer {

    private String text;

    private StringBuffer resultSB;

    private FrequencyTable ft;

    private TextStatistics stats = null;

    private int minA, minB;

    private AnalyzerFrame mFrame;

    public AffineAnalyzer() {
    }

    public AffineAnalyzer(AnalyzerFrame f) {
        mFrame = f;
    }

    public AffineAnalyzer(TextStatistics ts) throws NullPointerException {
        stats = ts;
        if (ts == null) throw new NullPointerException("CaesarAnalyzer: TextStatistics object is not instantiated");
    }

    public void setup(String text) {
        stats = new TextStatistics(text, true);
        this.text = text.toLowerCase();
        resultSB = new StringBuffer();
        ft = stats.getFrequencyTable();
        if (mFrame != null) mFrame.append("Affine Analyzer: Begin Analysis\n"); else System.out.println("Affine Analyzer: Begin Analysis\n");
    }

    public void run() {
        doAnalysis();
        if (mFrame != null) mFrame.append(getReport()); else System.out.println(getReport());
    }

    public String getReport() {
        return toString();
    }

    public String getKeywordString() {
        return minA + "," + minB;
    }

    public String toString() {
        return resultSB.toString();
    }

    public void doAnalysis(String text) {
        stats = new TextStatistics(text, true);
        this.text = text.toLowerCase();
        resultSB = new StringBuffer();
        ft = stats.getFrequencyTable();
        if (mFrame != null) mFrame.append("Affine Analyzer: Begin Analysis\n"); else System.out.println("Affine Analyzer: Begin Analysis\n");
        doAnalysis();
    }

    /**
	 *  This method prints an analysis of the text assuming a Affine
	 *  (linear) cipher was used. The goal is to find the values of
	 *  A and B in y = Ax + B. If we assume an alphabet of a..z,
	 *  then A can take on the values 1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25.
	 *  These are the values for which gcd(A, 26) = 1. B can take on the 
	 *  values 0..25. So there are 12 x 26 = 312 possible keys. This method
	 *  does an exhaustive search of these keys using Chi-square test to
	 *  identify the optimum key.
	 */
    public void doAnalysis() {
        try {
            Provider.addProvider(new DefaultProvider("Default"));
            minA = 0;
            minB = 0;
            double minChiSqr = Double.MAX_VALUE;
            for (int b = 0; b < 26; b++) for (int a = 1; a < 26; a += 2) {
                if (a != 13) {
                    Cipher cipher = Cipher.getInstance("Affine", "Default");
                    AffineKey key = (AffineKey) HistoricalKey.getInstance("Affine", "Default");
                    key.init(a + "," + b + "/az");
                    cipher.init(key);
                    String decryption;
                    if (text.length() <= DECIPHER_LIMIT) decryption = cipher.decrypt(text); else decryption = cipher.decrypt(text.substring(0, DECIPHER_LIMIT));
                    double chiSquare = getChiSquare(decryption);
                    if (chiSquare < minChiSqr) {
                        minA = a;
                        minB = b;
                        minChiSqr = chiSquare;
                    }
                }
            }
            Cipher cipher = Cipher.getInstance("Affine", "Default");
            AffineKey key = (AffineKey) HistoricalKey.getInstance("Affine", "Default");
            key.init(minA + "," + minB + "/az");
            cipher.init(key);
            String decryption;
            if (text.length() <= DECIPHER_LIMIT) decryption = cipher.decrypt(text); else decryption = cipher.decrypt(text.substring(0, DECIPHER_LIMIT));
            resultSB.append("\nHere's a possible decryption for A = " + minA + " and B = " + minB + " \n");
            resultSB.append(decryption + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getChiSquare(String text) {
        double chi = 0;
        int tFreqs[] = new int[26];
        for (int k = 0; k < 26; k++) tFreqs[k] = 0;
        for (int k = 0; k < text.length(); k++) {
            char ch = text.charAt(k);
            if (Character.isLetter(ch)) {
                ch = Character.toLowerCase(ch);
                ++tFreqs[ch - 'a'];
            }
        }
        for (char k = 'a'; k <= 'z'; k++) {
            double freq = (TextStatistics.getEnglishFrequency(k) - tFreqs[k - 'a']);
            freq = freq * freq;
            freq = freq / TextStatistics.getEnglishFrequency(k);
            chi = chi + freq;
        }
        return chi;
    }
}
