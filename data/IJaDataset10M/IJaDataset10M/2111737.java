package graphbuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class RelationshipCluster {

    protected Vector<String> index = new Vector<String>();

    protected HashMap<String, Integer> reverseIndex = new HashMap<String, Integer>();

    /**
	 * Reads the row label file
	 * @param rowLabel row label file name
	 */
    public void readIndex(String rowLabel) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(rowLabel));
            int i = 1;
            while (reader.ready()) {
                String line = reader.readLine();
                index.add(line.trim());
                reverseIndex.put(line.trim(), i);
                i++;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected HashMap<Integer, Vector<String>> clusters = new HashMap<Integer, Vector<String>>();

    /**
	 * Read the cluster file and populate the clusters
	 * @param clustersResults read the cluster results file
	 */
    public void readClusterResults(String clustersResults) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(clustersResults));
            int i = 0;
            while (reader.ready()) {
                String line = reader.readLine();
                int clusterNumber = Integer.parseInt(line);
                if (clusterNumber != -1) {
                    if (!clusters.containsKey(clusterNumber)) clusters.put(clusterNumber, new Vector<String>());
                    clusters.get(clusterNumber).add(index.get(i));
                }
                i++;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Prints the clusters with size > 1
	 * @param outputFile output file to write to
	 */
    public void printClusters(String outputFile) {
        try {
            PrintWriter outWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
            for (Iterator itr = clusters.keySet().iterator(); itr.hasNext(); ) {
                Integer clusterNumber = (Integer) itr.next();
                Vector<String> relationships = clusters.get(clusterNumber);
                if (relationships.size() > 1) {
                    for (int i = 0; i < relationships.size(); i++) {
                        System.out.println(relationships.get(i));
                        outWriter.println(relationships.get(i));
                    }
                    System.out.println("-----------------------------");
                    outWriter.println("-----------------------------");
                }
            }
            outWriter.flush();
            outWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * @param args row label file, cluster file from cluto, annotated clusters output file
	 */
    public static void main(String[] args) {
        RelationshipCluster rc = new RelationshipCluster();
        rc.readIndex(args[0]);
        rc.readClusterResults(args[1]);
        rc.printClusters(args[2]);
    }
}
