package Watermill.util.codes;

import java.util.Set;
import Watermill.interfaces.Fingerprint;

/**
 * This class implements the Boneh Shaw anti-collusion code
 * with the enhancements by Schaathun
 * @see http://www.ii.uib.no/publikasjoner/texrap/pdf/2003-256.pdf
 *
 * @author Julien Lafaye
 *
 */
public class BonehShawCodebook implements Codebook {

    private int maxCollusions;

    private double errorProbability;

    private int maxNumberUsers;

    private int lengthOfWords;

    public BonehShawCodebook(int maxCollusions, double errorProbability, int maxNumberUsers) {
        this.maxCollusions = maxCollusions;
        this.errorProbability = errorProbability;
        this.maxNumberUsers = maxNumberUsers;
    }

    private double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    private double computeD(final double value1, final double value2) {
        return value1 * log2(value1 / value2) + (1 - value1) * log2((1 - value1) / (1 - value2));
    }

    public void prepare() {
        double t = (double) this.maxCollusions;
        double eps = this.errorProbability;
        int M = this.maxNumberUsers;
        int n2 = (int) (Math.max(-log2(eps), log2(M) - log2(eps)) / computeD(1.0d / (t + 1.0d), 1.0d / (2 * t)));
        this.lengthOfWords = (int) ((2 * t - 1) * n2 * Math.floor(8 * t * t * (3 + 2 * log2(t))));
    }

    public int getLengthOfWords() {
        return this.lengthOfWords;
    }

    public void accuse(boolean[] suspectWord) {
    }

    public Fingerprint getNewWord() {
        return null;
    }

    public Fingerprint getNewWord(byte[] seed) {
        return null;
    }

    public Set<Fingerprint> trace(Fingerprint suspectWord) {
        return null;
    }
}
