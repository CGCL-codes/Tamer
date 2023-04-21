package pogvue.gui.schemes;

import java.awt.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public final class ResidueProperties {

    public static final Hashtable aaHash = new Hashtable();

    private static final Hashtable aa3Hash = new Hashtable();

    static {
        aaHash.put("A", 0);
        aaHash.put("R", 1);
        aaHash.put("N", 2);
        aaHash.put("D", 3);
        aaHash.put("C", 4);
        aaHash.put("Q", 5);
        aaHash.put("E", 6);
        aaHash.put("G", 7);
        aaHash.put("H", 8);
        aaHash.put("I", 9);
        aaHash.put("L", 10);
        aaHash.put("K", 11);
        aaHash.put("M", 12);
        aaHash.put("F", 13);
        aaHash.put("P", 14);
        aaHash.put("S", 15);
        aaHash.put("T", 16);
        aaHash.put("W", 17);
        aaHash.put("Y", 18);
        aaHash.put("V", 19);
        aaHash.put("B", 20);
        aaHash.put("Z", 21);
        aaHash.put("X", 22);
        aaHash.put("-", 23);
        aaHash.put("*", 23);
        aaHash.put(".", 23);
        aaHash.put(" ", 23);
    }

    public static final Hashtable aaSpecialsHash = new Hashtable();

    static {
        aaSpecialsHash.put("-", 23);
        aaSpecialsHash.put("*", 24);
        aaSpecialsHash.put(".", 25);
        aaSpecialsHash.put(" ", 26);
    }

    static {
        aa3Hash.put("ALA", 0);
        aa3Hash.put("ARG", 1);
        aa3Hash.put("ASN", 2);
        aa3Hash.put("ASP", 3);
        aa3Hash.put("CYS", 4);
        aa3Hash.put("GLN", 5);
        aa3Hash.put("GLU", 6);
        aa3Hash.put("GLY", 7);
        aa3Hash.put("HIS", 8);
        aa3Hash.put("ILE", 9);
        aa3Hash.put("LEU", 10);
        aa3Hash.put("LYS", 11);
        aa3Hash.put("MET", 12);
        aa3Hash.put("PHE", 13);
        aa3Hash.put("PRO", 14);
        aa3Hash.put("SER", 15);
        aa3Hash.put("THR", 16);
        aa3Hash.put("TRP", 17);
        aa3Hash.put("TYR", 18);
        aa3Hash.put("VAL", 19);
        aa3Hash.put("B", 20);
        aa3Hash.put("Z", 21);
        aa3Hash.put("X", 22);
        aa3Hash.put("-", 23);
        aa3Hash.put("*", 23);
        aa3Hash.put(".", 23);
        aa3Hash.put(" ", 23);
    }

    public static final String[] aa = { "A", "R", "N", "D", "C", "Q", "E", "G", "H", "I", "L", "K", "M", "F", "P", "S", "T", "W", "Y", "V", "B", "Z", "X", "_", "*", ".", " " };

    private static final Color midBlue = new Color(100, 100, 255);

    private static final Vector scaleColours = new Vector();

    static {
        scaleColours.addElement(new Color(114, 0, 147));
        scaleColours.addElement(new Color(156, 0, 98));
        scaleColours.addElement(new Color(190, 0, 0));
        scaleColours.addElement(Color.red);
        scaleColours.addElement(new Color(255, 125, 0));
        scaleColours.addElement(Color.orange);
        scaleColours.addElement(new Color(255, 194, 85));
        scaleColours.addElement(Color.yellow);
        scaleColours.addElement(new Color(255, 255, 181));
        scaleColours.addElement(Color.white);
    }

    public static final Color[] taylor = { new Color(204, 255, 0), new Color(0, 0, 255), new Color(204, 0, 255), new Color(255, 0, 0), new Color(255, 255, 0), new Color(255, 0, 204), new Color(255, 0, 102), new Color(255, 153, 0), new Color(0, 102, 255), new Color(102, 255, 0), new Color(51, 255, 0), new Color(102, 0, 255), new Color(0, 255, 0), new Color(0, 255, 102), new Color(255, 204, 0), new Color(255, 51, 0), new Color(255, 102, 0), new Color(0, 204, 255), new Color(0, 255, 204), new Color(153, 255, 0), Color.white, Color.white, Color.white, Color.white, Color.white, Color.white };

    public static final Color[] color = { Color.pink, midBlue, Color.green, Color.red, Color.yellow, Color.green, Color.red, Color.magenta, Color.red, Color.pink, Color.pink, midBlue, Color.pink, Color.orange, Color.magenta, Color.green, Color.green, Color.orange, Color.orange, Color.pink, Color.white, Color.white, Color.white, Color.white, Color.white, Color.white, Color.white };

    public static final double[] hyd2 = { 0.62, 0.29, -0.90, -0.74, 1.19, 0.48, -0.40, 1.38, -1.50, 1.06, 0.64, -0.78, 0.12, -0.85, -2.53, -0.18, -0.05, 1.08, 0.81, 0.0, 0.26, 0.0, 0.0 };

    public static final double[] helix = { 1.42, 0.98, 0.67, 1.01, 0.70, 1.11, 1.51, 0.57, 1.00, 1.08, 1.21, 1.16, 1.45, 1.13, 0.57, 0.77, 0.83, 1.08, 0.69, 1.06, 0.84, 1.31, 1.00 };

    public static final double helixmin = 0.57;

    public static final double helixmax = 1.51;

    public static final double[] strand = { 0.83, 0.93, 0.89, 0.54, 1.19, 1.10, 0.37, 0.75, 0.87, 1.60, 1.30, 0.74, 1.05, 1.38, 0.55, 0.75, 1.19, 1.37, 1.47, 1.70, 0.72, 0.74, 1.0 };

    public static final double strandmin = 0.37;

    public static final double strandmax = 1.7;

    public static final double[] turn = { 0.66, 0.95, 1.56, 1.46, 1.19, 0.98, 0.74, 1.56, 0.95, 0.47, 0.59, 1.01, 0.60, 0.60, 1.52, 1.43, 0.96, 0.96, 1.14, 0.50, 1.51, 0.86, 1.00 };

    public static final double turnmin = 0.47;

    public static final double turnmax = 1.56;

    public static final double[] buried = { 1.7, 0.1, 0.4, 0.4, 4.6, 0.3, 0.3, 1.8, 0.8, 3.1, 2.4, 0.05, 1.9, 2.2, 0.6, 0.8, 0.7, 1.6, 0.5, 2.9, 0.4, 0.3, 1.358 };

    public static final double buriedmin = 0.05;

    public static final double buriedmax = 4.6;

    public static final double[] hyd = { 1.8, -4.5, -3.5, -3.5, 2.5, -3.5, -3.5, -0.4, -3.2, 4.5, 3.8, -3.9, 1.9, 2.8, -1.6, -0.8, -0.7, -0.9, -1.3, 4.2, -3.5, -3.5, -0.49 };

    public static final double hydmax = 4.5;

    public static final double hydmin = -3.9;

    public static double getHydmax() {
        return hydmax;
    }

    public static double getHydmin() {
        return hydmin;
    }

    public static double[] getHyd() {
        return hyd;
    }

    public static final int[][] BLOSUM62 = { { 4, -1, -2, -2, 0, -1, -1, 0, -2, -1, -1, -1, -1, -2, -1, 1, 0, -3, -2, 0, -2, -1, 0, -4 }, { -1, 5, 0, -2, -3, 1, 0, -2, 0, -3, -2, 2, -1, -3, -2, -1, -1, -3, -2, -3, -1, 0, -1, -4 }, { -2, 0, 6, 1, -3, 0, 0, 0, 1, -3, -3, 0, -2, -3, -2, 1, 0, -4, -2, -3, 3, 0, -1, -4 }, { -2, -2, 1, 6, -3, 0, 2, -1, -1, -3, -4, -1, -3, -3, -1, 0, -1, -4, -3, -3, 4, 1, -1, -4 }, { 0, 3, -3, -3, 9, -3, -4, -3, -3, -1, -1, -3, -1, -2, -3, -1, -1, -2, -2, -1, -3, -3, -2, -4 }, { -1, 1, 0, 0, -3, 5, 2, -2, 0, -3, -2, 1, 0, -3, -1, 0, -1, -2, -1, -2, 0, 3, -1, -4 }, { -1, 0, 0, 2, -4, 2, 5, -2, 0, -3, -3, 1, -2, -3, -1, 0, -1, -3, -2, -2, 1, 4, -1, -4 }, { 0, -2, 0, -1, -3, -2, -2, 6, -2, -4, -4, -2, -3, -3, -2, 0, -2, -2, -3, -3, -1, -2, -1, -4 }, { -2, 0, 1, -1, -3, 0, 0, -2, 8, -3, -3, -1, -2, -1, -2, -1, -2, -2, 2, -3, 0, 0, -1, -4 }, { -1, -3, -3, -3, -1, -3, -3, -4, -3, 4, 2, -3, 1, 0, -3, -2, -1, -3, -1, 3, -3, -3, -1, -4 }, { -1, -2, -3, -4, -1, -2, -3, -4, -3, 2, 4, -2, 2, 0, -3, -2, -1, -2, -1, 1, -4, -3, -1, -4 }, { -1, 2, 0, -1, -3, 1, 1, -2, -1, -3, -2, 5, -1, -3, -1, 0, -1, -3, -2, -2, 0, 1, -1, -4 }, { -1, -1, -2, -3, -1, 0, -2, -3, -2, 1, 2, -1, 5, 0, -2, -1, -1, -1, -1, 1, -3, -1, -1, -4 }, { -2, -3, -3, -3, -2, -3, -3, -3, -1, 0, 0, -3, 0, 6, -4, -2, -2, 1, 3, -1, -3, -3, -1, -4 }, { -1, -2, -2, -1, -3, -1, -1, -2, -2, -3, -3, -1, -2, -4, 7, -1, -1, -4, -3, -2, -2, -1, -2, -4 }, { 1, -1, 1, 0, -1, 0, 0, 0, -1, -2, -2, 0, -1, -2, -1, 4, 1, -3, -2, -2, 0, 0, 0, -4 }, { 0, -1, 0, -1, -1, -1, -1, -2, -2, -1, -1, -1, -1, -2, -1, 1, 5, -2, -2, 0, -1, -1, 0, -4 }, { -3, -3, -4, -4, -2, -2, -3, -2, -2, -3, -2, -3, -1, 1, -4, -3, -2, 11, 2, -3, -4, -3, -2, -4 }, { -2, -2, -2, -3, -2, -1, -2, -3, 2, -1, -1, -2, -1, 3, -3, -2, -2, 2, 7, -1, -3, -2, -1, -4 }, { 0, -3, -3, -3, -1, -2, -2, -3, -3, 3, 1, -2, 1, -1, -2, -2, 0, -3, -1, 4, -3, -2, -1, -4 }, { -2, -1, 3, 4, -3, 0, 1, -1, 0, -3, -4, 0, -3, -3, -2, 0, -1, -4, -3, -3, 4, 1, -1, -4 }, { -1, 0, 0, 1, -3, 3, 4, -2, 0, -3, -3, 1, -1, -3, -1, 0, -1, -3, -2, -2, 1, 4, -1, -4 }, { 0, -1, -1, -1, -2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -2, 0, 0, -2, -1, -1, -1, -1, -1, -4 }, { -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, 1 } };

    private static final int[][] PAM250 = { { 2, -2, 0, 0, -2, 0, 0, 1, -1, -1, -2, -1, -1, -3, 1, 1, 1, -6, -3, 0, 0, 0, 0, -8 }, { -2, 6, 0, -1, -4, 1, -1, -3, 2, -2, -3, 3, 0, -4, 0, 0, -1, 2, -4, -2, -1, 0, -1, -8 }, { 0, 0, 2, 2, -4, 1, 1, 0, 2, -2, -3, 1, -2, -3, 0, 1, 0, -4, -2, -2, 2, 1, 0, -8 }, { 0, -1, 2, 4, -5, 2, 3, 1, 1, -2, -4, 0, -3, -6, -1, 0, 0, -7, -4, -2, 3, 3, -1, -8 }, { -2, -4, -4, -5, 12, -5, -5, -3, -3, -2, -6, -5, -5, -4, -3, 0, -2, -8, 0, -2, -4, -5, -3, -8 }, { 0, 1, 1, 2, -5, 4, 2, -1, 3, -2, -2, 1, -1, -5, 0, -1, -1, -5, -4, -2, 1, 3, -1, -8 }, { 0, -1, 1, 3, -5, 2, 4, 0, 1, -2, -3, 0, -2, -5, -1, 0, 0, -7, -4, -2, 3, 3, -1, -8 }, { 1, -3, 0, 1, -3, -1, 0, 5, -2, -3, -4, -2, -3, -5, 0, 1, 0, -7, -5, -1, 0, 0, -1, -8 }, { -1, 2, 2, 1, -3, 3, 1, -2, 6, -2, -2, 0, -2, -2, 0, -1, -1, -3, 0, -2, 1, 2, -1, -8 }, { -1, -2, -2, -2, -2, -2, -2, -3, -2, 5, 2, -2, 2, 1, -2, -1, 0, -5, -1, 4, -2, -2, -1, -8 }, { -2, -3, -3, -4, -6, -2, -3, -4, -2, 2, 6, -3, 4, 2, -3, -3, -2, -2, -1, 2, -3, -3, -1, -8 }, { -1, 3, 1, 0, -5, 1, 0, -2, 0, -2, -3, 5, 0, -5, -1, 0, 0, -3, -4, -2, 1, 0, -1, -8 }, { -1, 0, -2, -3, -5, -1, -2, -3, -2, 2, 4, 0, 6, 0, -2, -2, -1, -4, -2, 2, -2, -2, -1, -8 }, { -3, -4, -3, -6, -4, -5, -5, -5, -2, 1, 2, -5, 0, 9, -5, -3, -3, 0, 7, -1, -4, -5, -2, -8 }, { 1, 0, 0, -1, -3, 0, -1, 0, 0, -2, -3, -1, -2, -5, 6, 1, 0, -6, -5, -1, -1, 0, -1, -8 }, { 1, 0, 1, 0, 0, -1, 0, 1, -1, -1, -3, 0, -2, -3, 1, 2, 1, -2, -3, -1, 0, 0, 0, -8 }, { 1, -1, 0, 0, -2, -1, 0, 0, -1, 0, -2, 0, -1, -3, 0, 1, 3, -5, -3, 0, 0, -1, 0, -8 }, { -6, 2, -4, -7, -8, -5, -7, -7, -3, -5, -2, -3, -4, 0, -6, -2, -5, 17, 0, -6, -5, -6, -4, -8 }, { -3, -4, -2, -4, 0, -4, -4, -5, 0, -1, -1, -4, -2, 7, -5, -3, -3, 0, 10, -2, -3, -4, -2, -8 }, { 0, -2, -2, -2, -2, -2, -2, -1, -2, 4, 2, -2, 2, -1, -1, -1, 0, -6, -2, 4, -2, -2, -1, -8 }, { 0, -1, 2, 3, -4, 1, 3, 0, 1, -2, -3, 1, -2, -4, -1, 0, 0, -5, -3, -2, 3, 2, -1, -8 }, { 0, 0, 1, 3, -5, 3, 3, 0, 2, -2, -3, 0, -2, -5, 0, 0, -1, -6, -4, -2, 2, 3, -1, -8 }, { 0, -1, 0, -1, -3, -1, -1, -1, -1, -1, -1, -1, -1, -2, -1, 0, 0, -4, -2, -1, -1, -1, -1, -8 }, { -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, 1 } };

    public static final Hashtable ssHash = new Hashtable();

    static {
        ssHash.put("H", Color.magenta);
        ssHash.put("E", Color.yellow);
        ssHash.put("-", Color.white);
        ssHash.put(".", Color.white);
        ssHash.put("S", Color.cyan);
        ssHash.put("T", Color.blue);
        ssHash.put("G", Color.pink);
        ssHash.put("I", Color.pink);
        ssHash.put("B", Color.yellow);
    }

    private static final int[][] DNA = { { 5, -4, -4, -4, 1 }, { -4, 5, -4, -4, 1 }, { -4, -4, 5, -4, 1 }, { -4, -4, -4, 5, 1 }, { 1, 1, 1, 1, 1 } };

    public static Hashtable getAAHash() {
        return aaHash;
    }

    public static Hashtable getAA3Hash() {
        return aa3Hash;
    }

    public static int[][] getDNA() {
        return ResidueProperties.DNA;
    }

    public static int[][] getBLOSUM62() {
        return ResidueProperties.BLOSUM62;
    }

    public static int getPAM250(String A1, String A2) {
        Integer pog1 = (Integer) aaHash.get(A1);
        Integer pog2 = (Integer) aaHash.get(A2);
        int pog = ResidueProperties.PAM250[pog1][pog2];
        return pog;
    }

    public static int getBLOSUM62(String A1, String A2) {
        int pog = 0;
        try {
            Integer pog1 = (Integer) aaHash.get(A1);
            Integer pog2 = (Integer) aaHash.get(A2);
            pog = ResidueProperties.BLOSUM62[pog1][pog2];
        } catch (Exception e) {
        }
        return pog;
    }

    public static final Color[] pidColours = { midBlue, new Color(153, 153, 255), new Color(204, 204, 255) };

    public static final float[] pidThresholds = { 80, 60, 40 };

    private ResidueProperties() {
    }

    private static final Hashtable codonHash = new Hashtable();

    private static final Vector Lys = new Vector();

    private static final Vector Asn = new Vector();

    private static final Vector Gln = new Vector();

    private static final Vector His = new Vector();

    private static final Vector Glu = new Vector();

    private static final Vector Asp = new Vector();

    private static final Vector Tyr = new Vector();

    private static final Vector Thr = new Vector();

    private static final Vector Pro = new Vector();

    private static final Vector Ala = new Vector();

    private static final Vector Ser = new Vector();

    private static final Vector Arg = new Vector();

    private static final Vector Gly = new Vector();

    private static final Vector Trp = new Vector();

    private static final Vector Cys = new Vector();

    private static final Vector Ile = new Vector();

    private static final Vector Met = new Vector();

    private static final Vector Leu = new Vector();

    private static final Vector Val = new Vector();

    private static final Vector Phe = new Vector();

    private static final Vector STOP = new Vector();

    static {
        codonHash.put("K", Lys);
        codonHash.put("N", Asn);
        codonHash.put("Q", Gln);
        codonHash.put("H", His);
        codonHash.put("E", Glu);
        codonHash.put("D", Asp);
        codonHash.put("Y", Tyr);
        codonHash.put("T", Thr);
        codonHash.put("P", Pro);
        codonHash.put("A", Ala);
        codonHash.put("S", Ser);
        codonHash.put("R", Arg);
        codonHash.put("G", Gly);
        codonHash.put("W", Trp);
        codonHash.put("C", Cys);
        codonHash.put("I", Ile);
        codonHash.put("M", Met);
        codonHash.put("L", Leu);
        codonHash.put("V", Val);
        codonHash.put("F", Phe);
        codonHash.put("STOP", STOP);
    }

    public static Vector getCodons(String res) {
        if (codonHash.containsKey(res)) return (Vector) codonHash.get(res);
        return null;
    }

    public static String codonTranslate(String codon) {
        Enumeration e = codonHash.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            Vector tmp = (Vector) codonHash.get(key);
            if (tmp.contains(codon)) {
                if (key.equals("STOP")) {
                    return "*";
                } else {
                    return key;
                }
            }
        }
        return null;
    }

    public static final Hashtable codonHash2 = new Hashtable();

    static {
        codonHash2.put("AAA", "K");
        codonHash2.put("AAG", "K");
        codonHash2.put("AAC", "N");
        codonHash2.put("AAT", "N");
        codonHash2.put("CAA", "E");
        codonHash2.put("CAG", "E");
        codonHash2.put("CAC", "H");
        codonHash2.put("CAT", "H");
        codonHash2.put("GAA", "Q");
        codonHash2.put("GAG", "Q");
        codonHash2.put("GAC", "D");
        codonHash2.put("GAT", "D");
        codonHash2.put("TAC", "Y");
        codonHash2.put("TAT", "Y");
        codonHash2.put("ACA", "T");
        codonHash2.put("ACG", "T");
        codonHash2.put("ACC", "T");
        codonHash2.put("ACT", "T");
        codonHash2.put("CCA", "P");
        codonHash2.put("CCG", "P");
        codonHash2.put("CCC", "P");
        codonHash2.put("CCT", "P");
        codonHash2.put("GCA", "A");
        codonHash2.put("GCG", "A");
        codonHash2.put("GCC", "A");
        codonHash2.put("GCT", "A");
        codonHash2.put("TCA", "S");
        codonHash2.put("TCG", "S");
        codonHash2.put("TCC", "S");
        codonHash2.put("TCT", "S");
        codonHash2.put("AGC", "S");
        codonHash2.put("AGT", "S");
        codonHash2.put("AGA", "R");
        codonHash2.put("AGG", "R");
        codonHash2.put("CGA", "R");
        codonHash2.put("CGG", "R");
        codonHash2.put("CGC", "R");
        codonHash2.put("CGT", "R");
        codonHash2.put("GGA", "G");
        codonHash2.put("GGG", "G");
        codonHash2.put("GGC", "G");
        codonHash2.put("GGT", "G");
        codonHash2.put("TGA", "*");
        codonHash2.put("TAA", "*");
        codonHash2.put("TAG", "*");
        codonHash2.put("TGG", "W");
        codonHash2.put("TGC", "C");
        codonHash2.put("TGT", "C");
        codonHash2.put("ATA", "I");
        codonHash2.put("ATC", "I");
        codonHash2.put("ATT", "I");
        codonHash2.put("ATG", "M");
        codonHash2.put("CTA", "L");
        codonHash2.put("CTG", "L");
        codonHash2.put("CTC", "L");
        codonHash2.put("CTT", "L");
        codonHash2.put("TTA", "L");
        codonHash2.put("TTG", "L");
        codonHash2.put("GTA", "V");
        codonHash2.put("GTG", "V");
        codonHash2.put("GTC", "V");
        codonHash2.put("GTT", "V");
        codonHash2.put("TTC", "F");
        codonHash2.put("TTT", "F");
    }

    static {
        Lys.addElement("AAA");
        Lys.addElement("AAG");
        Asn.addElement("AAC");
        Asn.addElement("AAT");
        Gln.addElement("CAA");
        Gln.addElement("CAG");
        His.addElement("CAC");
        His.addElement("CAT");
        Glu.addElement("GAA");
        Glu.addElement("GAG");
        Asp.addElement("GAC");
        Asp.addElement("GAT");
        Tyr.addElement("TAC");
        Tyr.addElement("TAT");
        Thr.addElement("ACA");
        Thr.addElement("ACG");
        Thr.addElement("ACC");
        Thr.addElement("ACT");
        Pro.addElement("CCA");
        Pro.addElement("CCG");
        Pro.addElement("CCC");
        Pro.addElement("CCT");
        Ala.addElement("GCA");
        Ala.addElement("GCG");
        Ala.addElement("GCC");
        Ala.addElement("GCT");
        Ser.addElement("TCA");
        Ser.addElement("TCG");
        Ser.addElement("TCC");
        Ser.addElement("TCT");
        Ser.addElement("AGC");
        Ser.addElement("AGT");
        Arg.addElement("AGA");
        Arg.addElement("AGG");
        Arg.addElement("CGA");
        Arg.addElement("CGG");
        Arg.addElement("CGC");
        Arg.addElement("CGT");
        Gly.addElement("GGA");
        Gly.addElement("GGG");
        Gly.addElement("GGC");
        Gly.addElement("GGT");
        STOP.addElement("TGA");
        STOP.addElement("TAA");
        STOP.addElement("TAG");
        Trp.addElement("TGG");
        Cys.addElement("TGC");
        Cys.addElement("TGT");
        Ile.addElement("ATA");
        Ile.addElement("ATC");
        Ile.addElement("ATT");
        Met.addElement("ATG");
        Leu.addElement("CTA");
        Leu.addElement("CTG");
        Leu.addElement("CTC");
        Leu.addElement("CTT");
        Leu.addElement("TTA");
        Leu.addElement("TTG");
        Val.addElement("GTA");
        Val.addElement("GTG");
        Val.addElement("GTC");
        Val.addElement("GTT");
        Phe.addElement("TTC");
        Phe.addElement("TTT");
    }

    public static final Color[][] groupColors = { { Color.red, Color.red.brighter(), Color.red.brighter().brighter() }, { Color.orange, Color.orange.brighter(), Color.orange.brighter().brighter() }, { Color.green, Color.green.brighter(), Color.green.brighter().brighter() }, { Color.blue, Color.blue.brighter(), Color.blue.brighter().brighter() }, { Color.magenta, Color.magenta.brighter(), Color.magenta.brighter().brighter() }, { Color.cyan, Color.cyan.brighter(), Color.cyan.brighter().brighter() }, { Color.pink, Color.pink.brighter(), Color.pink.brighter().brighter() } };

    private static final Hashtable propHash = new Hashtable();

    private static final Hashtable hydrophobic = new Hashtable();

    private static final Hashtable polar = new Hashtable();

    private static final Hashtable small = new Hashtable();

    private static final Hashtable positive = new Hashtable();

    private static final Hashtable negative = new Hashtable();

    private static final Hashtable charged = new Hashtable();

    private static final Hashtable aromatic = new Hashtable();

    private static final Hashtable aliphatic = new Hashtable();

    private static final Hashtable tiny = new Hashtable();

    private static final Hashtable proline = new Hashtable();

    static {
        hydrophobic.put("I", 1);
        hydrophobic.put("L", 1);
        hydrophobic.put("V", 1);
        hydrophobic.put("C", 1);
        hydrophobic.put("A", 1);
        hydrophobic.put("G", 1);
        hydrophobic.put("M", 1);
        hydrophobic.put("F", 1);
        hydrophobic.put("Y", 1);
        hydrophobic.put("W", 1);
        hydrophobic.put("H", 1);
        hydrophobic.put("K", 1);
        hydrophobic.put("X", 1);
        hydrophobic.put("-", 1);
        hydrophobic.put("*", 1);
        hydrophobic.put("R", 0);
        hydrophobic.put("E", 0);
        hydrophobic.put("Q", 0);
        hydrophobic.put("D", 0);
        hydrophobic.put("N", 0);
        hydrophobic.put("S", 0);
        hydrophobic.put("T", 0);
        hydrophobic.put("P", 0);
    }

    static {
        polar.put("Y", 1);
        polar.put("W", 1);
        polar.put("H", 1);
        polar.put("K", 1);
        polar.put("R", 1);
        polar.put("E", 1);
        polar.put("Q", 1);
        polar.put("D", 1);
        polar.put("N", 1);
        polar.put("S", 1);
        polar.put("T", 1);
        polar.put("X", 1);
        polar.put("-", 1);
        polar.put("*", 1);
        polar.put("I", 0);
        polar.put("L", 0);
        polar.put("V", 0);
        polar.put("C", 0);
        polar.put("A", 0);
        polar.put("G", 0);
        polar.put("M", 0);
        polar.put("F", 0);
        polar.put("P", 0);
    }

    static {
        small.put("I", 0);
        small.put("L", 0);
        small.put("V", 1);
        small.put("C", 1);
        small.put("A", 1);
        small.put("G", 1);
        small.put("M", 0);
        small.put("F", 0);
        small.put("Y", 0);
        small.put("W", 0);
        small.put("H", 0);
        small.put("K", 0);
        small.put("R", 0);
        small.put("E", 0);
        small.put("Q", 0);
        small.put("D", 1);
        small.put("N", 1);
        small.put("S", 1);
        small.put("T", 1);
        small.put("P", 1);
        small.put("-", 1);
        small.put("*", 1);
    }

    static {
        positive.put("I", 0);
        positive.put("L", 0);
        positive.put("V", 0);
        positive.put("C", 0);
        positive.put("A", 0);
        positive.put("G", 0);
        positive.put("M", 0);
        positive.put("F", 0);
        positive.put("Y", 0);
        positive.put("W", 0);
        positive.put("H", 1);
        positive.put("K", 1);
        positive.put("R", 1);
        positive.put("E", 0);
        positive.put("Q", 0);
        positive.put("D", 0);
        positive.put("N", 0);
        positive.put("S", 0);
        positive.put("T", 0);
        positive.put("P", 0);
        positive.put("-", 1);
        positive.put("*", 1);
    }

    static {
        negative.put("I", 0);
        negative.put("L", 0);
        negative.put("V", 0);
        negative.put("C", 0);
        negative.put("A", 0);
        negative.put("G", 0);
        negative.put("M", 0);
        negative.put("F", 0);
        negative.put("Y", 0);
        negative.put("W", 0);
        negative.put("H", 0);
        negative.put("K", 0);
        negative.put("R", 0);
        negative.put("E", 1);
        negative.put("Q", 0);
        negative.put("D", 1);
        negative.put("N", 0);
        negative.put("S", 0);
        negative.put("T", 0);
        negative.put("P", 0);
        negative.put("-", 1);
        negative.put("*", 1);
    }

    static {
        charged.put("I", 0);
        charged.put("L", 0);
        charged.put("V", 0);
        charged.put("C", 0);
        charged.put("A", 0);
        charged.put("G", 0);
        charged.put("M", 0);
        charged.put("F", 0);
        charged.put("Y", 0);
        charged.put("W", 0);
        charged.put("H", 1);
        charged.put("K", 1);
        charged.put("R", 1);
        charged.put("E", 1);
        charged.put("Q", 0);
        charged.put("D", 1);
        charged.put("N", 1);
        charged.put("S", 0);
        charged.put("T", 0);
        charged.put("P", 0);
        charged.put("-", 1);
        charged.put("*", 1);
    }

    static {
        aromatic.put("I", 0);
        aromatic.put("L", 0);
        aromatic.put("V", 0);
        aromatic.put("C", 0);
        aromatic.put("A", 0);
        aromatic.put("G", 0);
        aromatic.put("M", 0);
        aromatic.put("F", 1);
        aromatic.put("Y", 1);
        aromatic.put("W", 1);
        aromatic.put("H", 1);
        aromatic.put("K", 0);
        aromatic.put("R", 0);
        aromatic.put("E", 0);
        aromatic.put("Q", 0);
        aromatic.put("D", 0);
        aromatic.put("N", 0);
        aromatic.put("S", 0);
        aromatic.put("T", 0);
        aromatic.put("P", 0);
        aromatic.put("-", 1);
        aromatic.put("*", 1);
    }

    static {
        aliphatic.put("I", 1);
        aliphatic.put("L", 1);
        aliphatic.put("V", 1);
        aliphatic.put("C", 0);
        aliphatic.put("A", 0);
        aliphatic.put("G", 0);
        aliphatic.put("M", 0);
        aliphatic.put("F", 0);
        aliphatic.put("Y", 0);
        aliphatic.put("W", 0);
        aliphatic.put("H", 0);
        aliphatic.put("K", 0);
        aliphatic.put("R", 0);
        aliphatic.put("E", 0);
        aliphatic.put("Q", 0);
        aliphatic.put("D", 0);
        aliphatic.put("N", 0);
        aliphatic.put("S", 0);
        aliphatic.put("T", 0);
        aliphatic.put("P", 0);
        aliphatic.put("-", 1);
        aliphatic.put("*", 1);
    }

    static {
        tiny.put("I", 0);
        tiny.put("L", 0);
        tiny.put("V", 0);
        tiny.put("C", 0);
        tiny.put("A", 1);
        tiny.put("G", 1);
        tiny.put("M", 0);
        tiny.put("F", 0);
        tiny.put("Y", 0);
        tiny.put("W", 0);
        tiny.put("H", 0);
        tiny.put("K", 0);
        tiny.put("R", 0);
        tiny.put("E", 0);
        tiny.put("Q", 0);
        tiny.put("D", 0);
        tiny.put("N", 0);
        tiny.put("S", 1);
        tiny.put("T", 0);
        tiny.put("P", 0);
        tiny.put("-", 1);
        tiny.put("*", 1);
    }

    static {
        proline.put("I", 0);
        proline.put("L", 0);
        proline.put("V", 0);
        proline.put("C", 0);
        proline.put("A", 0);
        proline.put("G", 0);
        proline.put("M", 0);
        proline.put("F", 0);
        proline.put("Y", 0);
        proline.put("W", 0);
        proline.put("H", 0);
        proline.put("K", 0);
        proline.put("R", 0);
        proline.put("E", 0);
        proline.put("Q", 0);
        proline.put("D", 0);
        proline.put("N", 0);
        proline.put("S", 0);
        proline.put("T", 0);
        proline.put("P", 1);
        proline.put("-", 1);
        proline.put("*", 1);
    }

    static {
        propHash.put("hydrophobic", hydrophobic);
        propHash.put("small", small);
        propHash.put("positive", positive);
        propHash.put("negative", negative);
        propHash.put("charged", charged);
        propHash.put("aromatic", aromatic);
        propHash.put("aliphatic", aliphatic);
        propHash.put("tiny", tiny);
        propHash.put("proline", proline);
        propHash.put("polar", polar);
    }

    private static final Hashtable chainColours = new Hashtable();

    static {
        chainColours.put("A", Color.red);
        chainColours.put("B", Color.orange);
        chainColours.put("C", Color.yellow);
        chainColours.put("D", Color.green);
        chainColours.put("E", Color.cyan);
        chainColours.put("F", Color.blue);
        chainColours.put("G", Color.magenta);
        chainColours.put("H", Color.pink);
    }

    public static Hashtable getChainColours() {
        return chainColours;
    }

    public static String reverseComplement(String seq) {
        StringBuffer out = new StringBuffer();
        seq = seq.toUpperCase();
        for (int i = seq.length() - 1; i >= 0; i--) {
            String tmp = seq.substring(i, i + 1);
            if (tmp.equals("A")) {
                out.append("T");
            } else if (tmp.equals("T")) {
                out.append("A");
            } else if (tmp.equals("G")) {
                out.append("C");
            } else if (tmp.equals("C")) {
                out.append("G");
            } else {
                out.append(tmp);
            }
        }
        return out.toString();
    }
}
