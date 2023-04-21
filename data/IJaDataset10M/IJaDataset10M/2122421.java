package geovista.network.gui;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.commons.collections15.Factory;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;

/**
 * This is the class to read matrix file for undirected graph without weight.
 * 
 * @author weiluo
 * @version 1.0
 * 
 */
public class ReadMatrix {

    Graph g;

    Factory<Graph<Integer, Number>> graphFactory = new Factory<Graph<Integer, Number>>() {

        public Graph<Integer, Number> create() {
            return new SparseMultigraph<Integer, Number>();
        }
    };

    Factory<Integer> vertexFactory = new Factory<Integer>() {

        int count;

        public Integer create() {
            return count++;
        }
    };

    Factory<Number> edgeFactory = new Factory<Number>() {

        int count;

        public Number create() {
            return count++;
        }
    };

    public Graph readMatrixtoDirectedGraph(String FilePath) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FilePath)));
        String s = "";
        g = new DirectedSparseGraph();
        int vertex = 0;
        while ((s = br.readLine()) != null) {
            if (s.length() == 0) continue;
            String[] ss = s.split("\t");
            boolean isolate = true;
            for (int i = 0; i < ss.length; i++) {
                if (ss[i].equals("1")) {
                    g.addEdge(edgeFactory.create(), vertex, i);
                    isolate = false;
                }
            }
            if (isolate) {
                g.addVertex(vertex);
            }
            vertex++;
        }
        return g;
    }

    public Graph readMatrixtoUndirectedGraph(String FilePath) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FilePath)));
        String s = "";
        g = new UndirectedSparseGraph();
        int vertex = 0;
        while ((s = br.readLine()) != null) {
            if (s.length() == 0) continue;
            String[] ss = s.split("\t");
            boolean isolate = true;
            for (int i = vertex + 1; i < ss.length; i++) {
                if (ss[i].equals("1")) {
                    g.addEdge(edgeFactory.create(), vertex, i);
                    isolate = false;
                }
            }
            if (isolate) {
                g.addVertex(vertex);
            }
            vertex++;
        }
        return g;
    }

    public double[][][] ReadData(String[] file) {
        double[][][] matrix = new double[file.length][][];
        for (int i = 0; i < file.length; i++) matrix[i] = ReadData(file[i]);
        return matrix;
    }

    public double[][] ReadData(String file) {
        ArrayList<String> content = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String s = " ";
            while ((s = br.readLine()) != null) {
                if (s.length() == 0) continue;
                content.add(s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        double[][] matrix = new double[content.size()][content.size()];
        for (int i = 0; i < matrix.length; i++) {
            String s = content.get(i);
            String[] ss = s.split("\t");
            int counter = 0;
            for (int j = 0; j < ss.length; j++) {
                if (ss[j].length() == 0) {
                    continue;
                }
                matrix[i][counter] = Double.parseDouble(ss[j]);
                counter++;
            }
        }
        return matrix;
    }
}
