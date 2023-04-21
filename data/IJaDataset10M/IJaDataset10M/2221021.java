package smooth;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import util.nlp.LM;

/**
 * 
 * This class implements the second part of the assignment: Cross entropy and language modeling
 *
 */
public class Smooth implements Constants {

    Hashtable<String, Integer> uni = new Hashtable<String, Integer>(24, 1f);

    Hashtable<String, Hashtable<String, Integer>> bi = new Hashtable<String, Hashtable<String, Integer>>(24, 1f);

    Hashtable<String, Integer> uniHO = new Hashtable<String, Integer>(24, 1f);

    Hashtable<String, Hashtable<String, Integer>> biHO = new Hashtable<String, Hashtable<String, Integer>>(24, 1f);

    int N = 0;

    int N2 = 0;

    int V = 0;

    String path;

    String hoPath;

    /**
	 * Initializes the unigrams, bigrams and trigrams of the text data
	 * @param path: path of the training data
	 * @param hoPath: path of hte held out data
	 * @param tPath path of the text data
	 * @param voc flag to include held out data in vocabulary , default is false
	 */
    Smooth(Hashtable<String, Integer> uni, Hashtable<String, Hashtable<String, Integer>> bi, Hashtable<String, Integer> uniHO, Hashtable<String, Hashtable<String, Integer>> biHO, boolean voc, int n, int n2) {
        this.uni = uni;
        this.bi = bi;
        this.uniHO = uniHO;
        this.biHO = biHO;
        N = n;
        N2 = n2;
        initVoc(uni, uniHO, voc);
    }

    private void initVoc(Hashtable<String, Integer> uni2, Hashtable<String, Integer> uniHO2, boolean voc) {
        HashSet<String> set = new HashSet<String>();
        Enumeration<String> keys = uni2.keys();
        while (keys.hasMoreElements()) {
            set.add(keys.nextElement());
        }
        if (voc) {
            keys = uniHO2.keys();
            while (keys.hasMoreElements()) {
                set.add(keys.nextElement());
            }
        }
        V = set.size();
    }

    /**
 * Load the words into the array list lines, data is access using object data_
 * @param voc flag to specify if the held out data 
 * @param data_ object to acces the file
 * @param lines array list where the words are stored
 * @return
 */
    private int iniData(boolean voc, FileInput data_) {
        String j;
        int n = 0;
        while (!data_.eof()) {
            j = data_.readString();
            if (!j.equals(DUMMY)) {
                n++;
            }
        }
        return n;
    }

    /**
	 * This function performs the EM algorithm, finds the optimal lambda for the smoothed model 
	 * the array of lambdas is returned
	 * @return
	 */
    Double[] p_lambda() {
        Double lam[] = new Double[3];
        Double lam_bef[] = new Double[3];
        lam_bef[0] = 0.0;
        lam_bef[1] = 0.0;
        lam_bef[2] = 0.0;
        lam[0] = 0.25;
        lam[1] = 0.25;
        lam[2] = 0.25;
        Double lam_c[] = new Double[3];
        lam_c[0] = 0.0;
        lam_c[1] = 0.0;
        lam_c[2] = 0.0;
        Double p_1 = 0.0;
        Double cp[] = new Double[3];
        cp[0] = cp[1] = cp[2] = 0.0;
        Double p[] = new Double[3];
        p[0] = p[1] = p[2] = 0.0;
        String j, i;
        Double entropy = 0.0;
        while (!endCondition(lam, lam_bef)) {
            entropy = 0.0;
            Enumeration<String> it = biHO.keys();
            lam_c[0] = 0.0;
            lam_c[1] = 0.0;
            lam_c[2] = 0.0;
            cp[0] = cp[1] = cp[2] = 0.0;
            p_1 = 0.0;
            while (it.hasMoreElements()) {
                String key = (String) it.nextElement();
                Enumeration<String> keys2 = biHO.get(key).keys();
                while (keys2.hasMoreElements()) {
                    String key2 = keys2.nextElement();
                    p[0] = (double) 1 / (double) V;
                    p[1] = e_uni(key);
                    p[2] = e_bi(key, key2);
                    p_1 = lam[2] * p[2] + lam[1] * p[1] + lam[0] * p[0];
                    lambda_counts(p, p_1, lam_c, lam);
                    entropy += (double) Math.log(p_1) / (double) Math.log(2);
                }
            }
            update_lamba(lam_c, lam, lam_bef);
        }
        entropy = -1 * entropy / (double) N2;
        for (int ii = 0; ii < lam.length; ii++) {
            System.out.println("lamda " + ii + " :" + lam[ii]);
        }
        System.out.println("Entropy: " + entropy);
        return lam;
    }

    /**
	 * Calculates value the conditional probability p(j|i)
	 * @param j
	 * @param i
	 * @return
	 */
    Double e_bi(String j, String i) {
        Integer a, b = null;
        a = null;
        boolean flag1 = false;
        boolean flag2 = false;
        Double eval = 0.0;
        if (bi.containsKey(j)) {
            if (bi.get(j).containsKey(i)) {
                a = bi.get(j).get(i);
                flag1 = true;
                flag2 = true;
            }
        }
        if (flag2 != true && uni.containsKey(i)) {
            flag2 = true;
        }
        if (flag1 == true) {
            if (i.equals(DUMMY_2)) {
                b = 1;
            } else {
                b = uni.get(i);
            }
            eval = (double) ((double) a / (double) b);
        } else {
            if (flag2 == true) {
                return 0.0;
            } else {
                return (double) ((double) 1 / (double) V);
            }
        }
        return eval;
    }

    /**
	 * Calculates value the conditional probability p(j)
	 * @param j
	 * @return
	 */
    Double e_uni(String j) {
        Integer a, b = null;
        a = null;
        boolean flag1 = false;
        Double eval = 0.0;
        if (bi.containsKey(j)) {
            a = uni.get(j);
            flag1 = true;
        }
        if (flag1 == true) {
            b = N;
            eval = (double) ((double) a / (double) b);
        } else {
        }
        return eval;
    }

    /**
	 * This method stablish if the value of the lambdas converge
	 * @param lam
	 * @param lam_bef
	 * @return
	 */
    boolean endCondition(Double lam[], Double lam_bef[]) {
        for (int i = 0; i < lam.length; i++) {
            if (Math.abs(lam[i] - lam_bef[i]) > EPSILON) {
                return false;
            }
        }
        return true;
    }

    /**
	 * Update the value  of lambda using the EM algoritihm
	 * 
	 * @param lam_c   counts
	 * @param lam  lambdas
	 * @param lam_bef lambdas of the previous iteration
	 */
    private void update_lamba(Double[] lam_c, Double[] lam, Double lam_bef[]) {
        Double total = 0.0;
        for (int i = 0; i < lam_c.length; i++) {
            total += lam_c[i];
        }
        for (int i = 0; i < lam.length; i++) {
            lam_bef[i] = lam[i];
            lam[i] = lam_c[i] / total;
        }
    }

    /**
	 * Obtain the counts of lambdas according to the EM algorithm
	
	 */
    private void lambda_counts(Double p[], Double p_1, Double[] lam_c, Double lam[]) {
        for (int ii = 0; ii < lam_c.length; ii++) {
            lam_c[ii] += lam[ii] * p[ii] / p_1;
        }
    }

    /**
	 * 
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        String path1 = "/home/sergio/Dropbox/data/delicious/delicious_kids_final_thesaurus_filtered.txt";
        String training = "/home/sergio/Dropbox/data/delicious/training3.txt";
        String ho = "/home/sergio/Dropbox/data/delicious/heldout3.txt";
        LM t = new LM();
        LM he = new LM();
        t.generateModel(training, -1, Integer.MAX_VALUE);
        he.generateModel(ho, -1, Integer.MAX_VALUE);
        Smooth s = new Smooth(t.uni2, t.getBi(), he.uni2, he.getBi(), true, (int) t.getTotal_unigrams(), (int) he.getTotal_unigrams());
        Double[] lam = s.p_lambda();
    }

    public static void createHO(String path) {
        String path1 = "/home/sergio/Dropbox/data/delicious/training3.txt";
        FileOutput out = new FileOutput(path1);
        String path2 = "/home/sergio/Dropbox/data/delicious/heldout3.txt";
        FileOutput out1 = new FileOutput(path2);
        LinkedList<String> t = new LinkedList<String>();
        LinkedList<String> tHO = new LinkedList<String>();
        FileInput in = new FileInput(path);
        String line = in.readString();
        while (line != null && !line.equals("<s>")) {
            double r = Math.random();
            System.out.println(line);
            if (r < 0.05) {
                tHO.add(line);
            } else {
                t.add(line);
            }
            line = in.readString();
        }
        out.createFile(path1, t);
        out1.createFile(path2, tHO);
    }
}
