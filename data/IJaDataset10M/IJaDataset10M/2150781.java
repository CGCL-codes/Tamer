package CED;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author emre
 */
public class SimilarityComparator extends Constants {

    public static Vector[] documents;

    public static Vector<HashSet> groundTruth;

    public static String outputFolder = "//home//emre//research//ced//data//results//similarityComparison";

    public static HashSet[] differences;

    public static HashMap[] agreements;

    public static int documentCount;

    public static int totalContainmentCount;

    public static boolean[] isRunned = new boolean[ALGORITHM_COUNT];

    public static void readSimilarities(String similarityDataFolder) throws IOException {
        for (int i = 0; i < ALGORITHM_COUNT; i++) if (isRunned[i]) {
            BufferedReader br = Helper.getBufferedReader(similarityDataFolder + "/similarDocuments.dat." + algorithmExt[i]);
            String line = "";
            for (int j = 0; (line = br.readLine()) != null; j++) {
                HashSet similarDocuments = new HashSet<Integer>();
                StringTokenizer st = new StringTokenizer(line, "\t");
                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    similarDocuments.add(Integer.parseInt(token));
                }
                documents[i].add(similarDocuments);
            }
            br.close();
        }
    }

    public static void readGroundTruth(String groundTruthFile) throws IOException {
        BufferedReader br = Helper.getBufferedReader(groundTruthFile);
        String line = "";
        for (int j = 0; (line = br.readLine()) != null; j++) {
            HashSet<Integer> similarDocuments = new HashSet<Integer>();
            StringTokenizer st = new StringTokenizer(line, "\t");
            while (st.hasMoreTokens()) {
                similarDocuments.add(Integer.parseInt(st.nextToken()));
                totalContainmentCount++;
            }
            groundTruth.add(similarDocuments);
        }
        br.close();
    }

    public static void pairwiseComparison(int algorithm1, int algorithm2) throws IOException {
        int agreementCount = 0, disagreementCount1 = 0, disagreementCount2 = 0;
        BufferedWriter bw = Helper.getBufferedWriter(outputFolder + "/agreements." + algorithmExt[algorithm1] + "." + algorithmExt[algorithm2]);
        BufferedWriter bw2 = Helper.getBufferedWriter(outputFolder + "/disagreements." + algorithmExt[algorithm1] + "." + algorithmExt[algorithm2]);
        BufferedWriter bw3 = Helper.getBufferedWriter(outputFolder + "/disagreements." + algorithmExt[algorithm2] + "." + algorithmExt[algorithm1]);
        BufferedWriter bw4 = Helper.getBufferedWriter(outputFolder + "/agreementCount." + algorithmExt[algorithm1] + "." + algorithmExt[algorithm2]);
        BufferedWriter bw5 = Helper.getBufferedWriter(outputFolder + "/disagreementCount." + algorithmExt[algorithm1] + "." + algorithmExt[algorithm2]);
        BufferedWriter bw6 = Helper.getBufferedWriter(outputFolder + "/disagreementCount." + algorithmExt[algorithm2] + "." + algorithmExt[algorithm1]);
        for (int i = 0; i < documentCount; i++) {
            HashSet similarities1 = (HashSet) documents[algorithm1].get(i);
            HashSet similarities2 = (HashSet) documents[algorithm2].get(i);
            Iterator<Integer> it = similarities1.iterator();
            while (it.hasNext()) {
                int docNo = it.next();
                if (similarities2.contains(docNo)) {
                    agreementCount++;
                    bw.write(docNo + "\t");
                } else {
                    differences[i].add(docNo);
                    disagreementCount1++;
                    bw2.write(docNo + "\t");
                }
            }
            it = similarities2.iterator();
            while (it.hasNext()) {
                int docNo = it.next();
                if (!similarities1.contains(docNo)) {
                    differences[i].add(docNo);
                    disagreementCount2++;
                    bw3.write(docNo + "\t");
                }
            }
            bw.write("\n");
            bw2.write("\n");
            bw3.write("\n");
        }
        bw4.write(agreementCount + "\n");
        bw5.write(disagreementCount1 + "\n");
        bw6.write(disagreementCount2 + "\n");
        bw.close();
        bw2.close();
        bw3.close();
        bw4.close();
        bw5.close();
        bw6.close();
    }

    public static void proc() throws IOException {
        documentCount = documents[0].size();
        differences = new HashSet[documentCount];
        agreements = new HashMap[documentCount];
        System.out.println(documentCount);
        for (int i = 0; i < documentCount; i++) {
            differences[i] = new HashSet<Integer>();
            agreements[i] = new HashMap<Integer, Integer>();
        }
        for (int i = 0; i < ALGORITHM_COUNT; i++) if (isRunned[i]) {
            for (int j = 0; j < documentCount; j++) {
                HashSet<Integer> similarities = (HashSet) documents[i].get(j);
                Iterator<Integer> it = similarities.iterator();
                while (it.hasNext()) {
                    int val = 0, docNo = it.next();
                    if (agreements[j].containsKey(docNo)) val = (Integer) agreements[j].get(docNo);
                    val++;
                    agreements[j].put(docNo, val);
                }
            }
            for (int j = 0; j < i; j++) if (isRunned[j]) pairwiseComparison(i, j);
        }
    }

    public static void reportAllAgreementsAndDisagreements() throws IOException {
        BufferedWriter bw = Helper.getBufferedWriter(outputFolder + "/agreements." + "all");
        BufferedWriter bw2 = Helper.getBufferedWriter(outputFolder + "/disagreements." + "all");
        int agreementCount = 0, disagreementCount = 0;
        for (int i = 0; i < documentCount; i++) {
            Iterator<Integer> it = agreements[i].keySet().iterator();
            while (it.hasNext()) {
                int docNo = it.next();
                if (((Integer) agreements[i].get(docNo)) == ALGORITHM_COUNT) {
                    bw.write(docNo + "\t");
                    agreementCount++;
                } else {
                    bw2.write(docNo + "\t");
                    disagreementCount++;
                }
            }
            bw.write("\n");
            bw2.write("\n");
        }
        bw.close();
        bw2.close();
        System.out.println("Total agreement count: " + agreementCount);
        System.out.println("Total disagreement count: " + disagreementCount);
    }

    public static void init() {
        documents = new Vector[ALGORITHM_COUNT];
        for (int i = 0; i < ALGORITHM_COUNT; i++) if (isRunned[i]) documents[i] = new Vector<HashSet>();
    }

    public static void main(String[] args) throws IOException {
        String similarityDataFolder = "//home//emre//research//ced//data//results";
        boolean useGroundTruth = false;
        String groundTruthFile = "";
        if (args.length != 9) {
            System.out.println("Usage: <similarityDataFolder> <outputFolder> <useGroundTruth> <groundTruthFile> <runCode> <runFullFingerPrinting> <runDsc> <runSimhash> <runImatch>");
            System.exit(1);
        } else {
            similarityDataFolder = args[0];
            outputFolder = args[1];
            useGroundTruth = Boolean.parseBoolean(args[2]);
            groundTruthFile = args[3];
            for (int i = 0; i < 5; i++) isRunned[i] = Boolean.parseBoolean(args[i + 4]);
        }
        init();
        readSimilarities(similarityDataFolder);
        proc();
        reportAllAgreementsAndDisagreements();
        if (useGroundTruth) {
            groundTruth = new Vector<HashSet>();
            readGroundTruth(groundTruthFile);
            groundTruthComparison();
        }
    }

    public static void groundTruthComparison() {
        System.out.println("Ground Truth Information");
        System.out.println("Document Count:\t" + documentCount);
        System.out.println("Total Containment Count:\t" + totalContainmentCount);
        System.out.println("Near Duplicate Count per document:\t" + ((double) totalContainmentCount / documentCount));
        for (int i = 0; i < ALGORITHM_COUNT; i++) if (isRunned[i]) {
            Vector<HashSet> similarities = documents[i];
            int detectedContainmentCount = 0;
            int falseDetectedContainmentCount = 0;
            for (int j = 0; j < documentCount; j++) {
                Iterator<Integer> it = similarities.get(j).iterator();
                while (it.hasNext()) {
                    int docNo = it.next();
                    if (docNo == j) continue;
                    if (groundTruth.get(j).contains(docNo)) detectedContainmentCount++; else falseDetectedContainmentCount++;
                }
            }
            double precision = ((double) detectedContainmentCount / (detectedContainmentCount + falseDetectedContainmentCount));
            double recall = ((double) detectedContainmentCount / totalContainmentCount);
            double f1Measure = 2 * precision * recall / (precision + recall);
            System.out.println("\n\n");
            System.out.println("Algorithm:\t" + algorithmExt[i]);
            System.out.println("Precision:\t" + precision);
            System.out.println("Recall:\t" + recall);
            System.out.println("F1Measure:\t" + f1Measure);
            System.out.println("Found Containment Count:\t" + (detectedContainmentCount + falseDetectedContainmentCount));
            System.out.println("True Detected Containment Count:\t" + detectedContainmentCount);
        }
    }
}
