package repeatmap.controller;

import repeatmap.util.Compression;
import repeatmap.util.KmerReader;
import repeatmap.util.NioFileReader;
import repeatmap.types.Statics;
import java.util.Arrays;
import java.util.Vector;
import java.lang.Math;
import java.io.*;

/**
 * Searches for hairpin structures in a variety of ways
 *
 * @author Aaron Arvey
 */
public class Hairpin {

    protected String seq_filename;

    protected StringBuffer[] sequence_records;

    protected StringBuffer sequence;

    protected int kmer_length;

    public Hairpin(String filename, int kmer_length) {
        this.seq_filename = filename;
        this.kmer_length = kmer_length;
    }

    public void outputQuartets() {
        try {
            PrintWriter graph_writer = new PrintWriter(new FileWriter("quartet_graph.dat"));
            for (int i = 0; i < sequence.length() - 4; i++) {
                if (sequence.substring(i, i + 4).equals("GGGG")) {
                    graph_writer.println("" + i + " " + sequence.charAt(i) + " 1");
                } else {
                    graph_writer.println("" + i + " " + sequence.charAt(i) + " 0");
                }
            }
            graph_writer.flush();
            graph_writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void outputGC(int winsize) {
        try {
            PrintWriter graph_writer = new PrintWriter(new FileWriter("gc_graph.dat"));
            int k = 0;
            int gc_count = 0;
            char c;
            for (int i = 0; i < sequence.length() - winsize; i++) {
                gc_count = 0;
                for (int j = i; j < i + winsize; j++) {
                    c = sequence.charAt(j);
                    if (c == 'G' || c == 'C') {
                        gc_count++;
                    }
                }
                graph_writer.println("" + i + " " + (double) gc_count / (double) winsize);
            }
            graph_writer.flush();
            graph_writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void outputWindows(int winsize) {
        try {
            PrintWriter graph_writer = new PrintWriter(new FileWriter("window_graph.dat"));
            int k = 0;
            for (int i = 0; i < sequence_records.length; i++) {
                k = slideWindow(sequence_records[i]);
                if (k > 3) graph_writer.println("" + i + " " + k); else graph_writer.println("" + i + " 0");
            }
            graph_writer.flush();
            graph_writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * 
     */
    protected int slideWindow(StringBuffer s) {
        StringBuffer r = new StringBuffer(s);
        r.reverse();
        int max_count = 0;
        int max_i = 0;
        int max_j = 0;
        for (int i = 0; i < s.length(); i++) {
            int counter = 0;
            for (int j = i; j < r.length(); j++) {
                if (s.charAt(j) == Statics.getAcidComp(r.charAt(j))) {
                    counter++;
                } else {
                    if (counter > max_count) {
                        max_count = counter;
                        max_i = j - counter;
                        max_j = j;
                    }
                    counter = 0;
                }
            }
        }
        if (max_count > 9999) {
            System.out.println(s);
            for (int i = 0; i < r.length(); i++) {
                System.out.print(Statics.getAcidComp(r.charAt(i)));
            }
            System.out.print("\n");
            if ((max_i + max_j) == s.length()) {
                System.out.println("Is Palindrome");
            }
            System.out.println("Max Count: " + max_count);
            System.out.println("Start: " + max_i);
            System.out.println("End: " + max_j);
        }
        return max_count;
    }

    public void read_sequence_file() {
        NioFileReader reader = new NioFileReader(seq_filename);
        reader.loadFile();
        int filesize = (int) reader.getFileSize();
        if (filesize > 50000) {
            Statics.logger.info("WARNIGN: file is pretty big: " + filesize + " bytes");
        }
        int num_records = filesize - kmer_length;
        StringBuffer sequence = new StringBuffer();
        sequence.append(reader.readBuffer());
        sequence_records = new StringBuffer[sequence.length() - kmer_length];
        for (int i = 0; i < sequence.length() - kmer_length; i++) {
            boolean good = true;
            char[] carray = sequence.substring(i, i + kmer_length).toCharArray();
            for (int j = 0; j < carray.length; j++) {
                if (Statics.getAcidCode(carray[j]) < 0) {
                    good = false;
                    System.err.println("ERROR: Bad input symbol!");
                }
            }
            sequence_records[i] = new StringBuffer(new String(carray));
        }
        this.sequence = sequence;
        reader.close();
    }

    public static void main(String[] args) {
        String filename = args[0];
        int k = Integer.parseInt(args[1]);
        int winsize = Integer.parseInt(args[2]);
        Hairpin hp = new Hairpin(filename, k);
        hp.read_sequence_file();
        int res = 0;
        hp.outputWindows(winsize);
        hp.outputQuartets();
        hp.outputGC(50);
    }
}
