package galronnlp.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import galronnlp.io.PennTreeReader;

/**
 *
 * @author Daniel A. Galron
 */
public class Treebank extends LinkedList<Tree> {

    public static final int FULL = 0;

    public static final int SAMPLE = 1;

    public static final int WSJ = 2;

    public static final int BROWN = 3;

    public static final int LEFTCNF = 4;

    public static final int RIGHTCNF = 5;

    public static final int NOFACTOR = 6;

    private int sample = FULL;

    private int factor = NOFACTOR;

    private String CorpusRoot = "";

    private int corpus = WSJ;

    private TreeFactorizer tf = new TreeFactorizer();

    /** Creates a new instance of Treebank */
    public Treebank(String root, int sample, int corpus, int f) {
        if (root.charAt(root.length() - 1) != '/') {
            CorpusRoot = root + "/";
        } else {
            CorpusRoot = root;
        }
        this.sample = sample;
        this.corpus = corpus;
        this.factor = f;
    }

    public Treebank(String root) {
        this(root, FULL, WSJ, NOFACTOR);
    }

    public void build() {
        if (corpus == WSJ) this.readInWSJ(); else if (corpus == BROWN) System.err.println("NOT YET IMPLEMENTED");
    }

    private void readInWSJ() {
        if (sample == FULL) this.readInWSJFull(); else if (sample == SAMPLE) this.readInWSJSample();
    }

    private void readInWSJFull() {
        int fileNum = 1;
        int dirNum = 0;
        CorpusRoot += "parsed/mrg/wsj/";
        for (dirNum = 0; dirNum < 25; dirNum++) {
            String dirString = "";
            if (dirNum < 10) dirString = "0" + Integer.toString(dirNum); else dirString = Integer.toString(dirNum);
            if (dirNum == 0) {
                for (fileNum = 1; fileNum < 100; fileNum++) {
                    String fileString = "";
                    if (fileNum < 10) fileString = "0" + Integer.toString(fileNum); else fileString = Integer.toString(fileNum);
                    System.err.println(CorpusRoot + dirString + "/wsj_" + dirString + fileString + ".mrg");
                    this.readInWSJTreeFile(CorpusRoot + dirString + "/wsj_" + dirString + fileString + ".mrg");
                }
            } else if (dirNum == 3) {
                for (fileNum = 0; fileNum < 81; fileNum++) {
                    String fileString = "";
                    if (fileNum < 10) fileString = "0" + Integer.toString(fileNum); else fileString = Integer.toString(fileNum);
                    System.err.println(CorpusRoot + dirString + "/wsj_" + dirString + fileString + ".mrg");
                    this.readInWSJTreeFile(CorpusRoot + dirString + "/wsj_" + dirString + fileString + ".mrg");
                }
            } else if (dirNum == 8) {
                for (fileNum = 0; fileNum < 21; fileNum++) {
                    String fileString = "";
                    if (fileNum < 10) fileString = "0" + Integer.toString(fileNum); else fileString = Integer.toString(fileNum);
                    System.err.println(CorpusRoot + dirString + "/wsj_" + dirString + fileString + ".mrg");
                    this.readInWSJTreeFile(CorpusRoot + dirString + "/wsj_" + dirString + fileString + ".mrg");
                }
            } else if (dirNum == 21) {
                for (fileNum = 0; fileNum < 73; fileNum++) {
                    String fileString = "";
                    if (fileNum < 10) fileString = "0" + Integer.toString(fileNum); else fileString = Integer.toString(fileNum);
                    System.err.println(CorpusRoot + dirString + "/wsj_" + dirString + fileString + ".mrg");
                    this.readInWSJTreeFile(CorpusRoot + dirString + "/wsj_" + dirString + fileString + ".mrg");
                }
            } else if (dirNum == 21) {
                for (fileNum = 0; fileNum < 73; fileNum++) {
                    String fileString = "";
                    if (fileNum < 10) fileString = "0" + Integer.toString(fileNum); else fileString = Integer.toString(fileNum);
                    System.err.println(CorpusRoot + dirString + "/wsj_" + dirString + fileString + ".mrg");
                    this.readInWSJTreeFile(CorpusRoot + dirString + "/wsj_" + dirString + fileString + ".mrg");
                }
            } else if (dirNum == 22) {
                for (fileNum = 0; fileNum < 83; fileNum++) {
                    String fileString = "";
                    if (fileNum < 10) fileString = "0" + Integer.toString(fileNum); else fileString = Integer.toString(fileNum);
                    System.err.println(CorpusRoot + dirString + "/wsj_" + dirString + fileString + ".mrg");
                    this.readInWSJTreeFile(CorpusRoot + dirString + "/wsj_" + dirString + fileString + ".mrg");
                }
            } else {
                for (fileNum = 0; fileNum < 100; fileNum++) {
                    String fileString = "";
                    if (fileNum < 10) fileString = "0" + Integer.toString(fileNum); else fileString = Integer.toString(fileNum);
                    System.err.println(CorpusRoot + dirString + "/wsj_" + dirString + fileString + ".mrg");
                    this.readInWSJTreeFile(CorpusRoot + dirString + "/wsj_" + dirString + fileString + ".mrg");
                }
            }
        }
    }

    private void readInWSJSample() {
        CorpusRoot += "parsed/mrg/wsj/";
        for (int fileNum = 1; fileNum < 100; fileNum++) {
            String fileString = "";
            if (fileNum < 10) fileString = "0" + Integer.toString(fileNum); else fileString = Integer.toString(fileNum);
            System.err.println("wsj_00" + fileString + ".mrg");
            this.readInWSJTreeFile(CorpusRoot + "00" + "/wsj_" + "00" + fileString + ".mrg");
        }
    }

    private String retrieveSentence(Tree t) {
        if (t.type() == Tree.LEXICAL) {
            return ((Symbol) t.children.getFirst()).toString();
        } else if (t.type() == Tree.UNARY) {
            return retrieveSentence((Tree) t.children.getFirst());
        } else if (t.type() == Tree.BINARY) {
            return retrieveSentence((Tree) t.children.getFirst()) + " " + retrieveSentence((Tree) t.children.getLast());
        } else if (t.type() == Tree.NARY) {
            String ret = "";
            Iterator<Tree> it = t.children.iterator();
            ret = retrieveSentence((Tree) it.next());
            for (; it.hasNext(); ) {
                ret += " " + retrieveSentence((Tree) it.next());
            }
            return ret;
        } else {
            System.err.println("ERROR: Unknown Tree type");
        }
        return "";
    }

    private void readInWSJTreeFile(String file) {
        try {
            PennTreeReader reader = new PennTreeReader(new BufferedReader(new FileReader(file)));
            while (reader.hasNext()) {
                Tree tree = reader.readTree();
                if (tree != null) {
                    if (this.factor == NOFACTOR) this.addLast(tree); else if (this.factor == RIGHTCNF) this.addLast(tf.rightFactorizeCNF(tree)); else if (this.factor == LEFTCNF) this.addLast(tf.leftFactorizeCNF(tree));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        String ret = "---------------------------------------\n";
        for (Iterator<Tree> i = this.iterator(); i.hasNext(); ) {
            ret += i.next().toString(false);
            ret += "---------------------------------------\n";
        }
        return ret;
    }

    public static void main(String[] args) {
        Treebank bank = new Treebank(args[0], FULL, WSJ, NOFACTOR);
        bank.build();
        System.out.println("Done! Treebank size = " + bank.size());
        System.out.println("---------------------------------------\n");
    }
}
