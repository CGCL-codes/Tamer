package CED;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 *
 * @author emre
 */
public class ConflictDetector {

    public static int documentCount = 5000;

    public static HashSet<Integer>[] similarities;

    public static HashSet<Integer>[] conflicts;

    public static String resultsFolderStr;

    public static void init() {
        similarities = new HashSet[documentCount];
        conflicts = new HashSet[documentCount];
        for (int i = 0; i < documentCount; i++) {
            similarities[i] = new HashSet<Integer>();
            conflicts[i] = new HashSet<Integer>();
        }
    }

    public static void read() throws IOException {
        File resultsFolder = new File(resultsFolderStr);
        File[] resultFiles = resultsFolder.listFiles();
        for (int i = 0; i < resultFiles.length; i++) {
            BufferedReader br = Helper.getBufferedReader(resultFiles[i]);
            String line;
            for (int j = 0; j < documentCount; j++) {
                line = br.readLine();
                if (line == null) continue;
                StringTokenizer st = new StringTokenizer(line, "\t");
                while (st.hasMoreTokens()) {
                    int docNo = Integer.parseInt(st.nextToken());
                    int state = Integer.parseInt(st.nextToken());
                    if (state == 1) {
                        if (similarities[j].contains(docNo)) conflicts[j].remove(docNo); else {
                            similarities[j].add(docNo);
                            conflicts[j].add(docNo);
                        }
                    }
                }
            }
            br.close();
        }
    }

    public static void print() throws IOException {
        BufferedWriter bw = Helper.getBufferedWriter(resultsFolderStr + "groundTruth.dat");
        int groundTruthDocCount = 0;
        for (int i = 0; i < documentCount; i++) {
            Iterator<Integer> it = similarities[i].iterator();
            while (it.hasNext()) {
                int val = it.next();
                if (!conflicts[i].contains(val)) {
                    bw.write(val + "\t");
                    groundTruthDocCount++;
                }
            }
            bw.write("\n");
        }
        bw.close();
        System.out.println("groundTruthDocCount: " + groundTruthDocCount);
    }

    public static void main(String[] args) throws IOException {
        resultsFolderStr = "/home/emre/research/ced/data/annotations/groundTruth/";
        if (args.length != 1) {
            System.out.println("Usage: <Results Folder>");
        } else resultsFolderStr = args[1];
        init();
        read();
        print();
    }
}
