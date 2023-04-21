package graphmeasures;

import dynamicpajeknetreader.DynamicPajekNetReader;
import dynamicpajeknetreader.DynamicSparseGraph;
import dynamicpajeknetreader.Edges;
import dynamicpajeknetreader.Nodes;
import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.DegreeScorer;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.io.PajekNetReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections15.Factory;

/**
 *
 * @author krg85
 */
public class GraphMeasures {

    static ArrayList<String> inputGraphFiles;

    static ArrayList<String> measures;

    static String outputFile;

    static int type;

    static final int DYNAMIC_GRAPH = 2;

    static final int STATIC_GRAPH = 1;

    static Vector inputGraphs;

    static final String[] GRAPHMEASURES = { "DegreeScorer", "BetweennessCentrality", "ClosenessCentrality" };

    static List graphMeasureList;

    static String format = "%1$-10s%2$-10s";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        graphMeasureList = Arrays.asList(GRAPHMEASURES);
        List<String> cmdArgs = Arrays.asList(args);
        inputGraphFiles = new ArrayList<String>();
        measures = new ArrayList<String>();
        int index;
        if ((index = cmdArgs.indexOf("-t")) != -1) {
            try {
                type = Integer.parseInt(cmdArgs.get(index + 1));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(" The graph type must be an Integer (1 -Static graphs or" + " 2 - dynamic graphs)  ");
            }
            if (type != STATIC_GRAPH && type != DYNAMIC_GRAPH) throw new IllegalArgumentException(" The graph type can either be 1 -Static graphs or" + " 2 - dynamic graphs instead of  " + type);
        } else type = STATIC_GRAPH;
        if ((index = cmdArgs.indexOf("-d")) != -1) {
            String dirName = cmdArgs.get(index + 1);
            File graphDir = new File(dirName);
            if (!graphDir.isDirectory()) throw new IllegalArgumentException(dirName + " is not a valid directory"); else {
                File[] files = graphDir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile() && files[i].getName().contains(".net")) inputGraphFiles.add(files[i].getName());
                }
            }
        } else if ((index = cmdArgs.indexOf("-l")) != -1) {
            if (type == DYNAMIC_GRAPH) {
                File graphFile = new File(cmdArgs.get(index + 1));
                if (graphFile.isFile() && graphFile.getName().contains(".net")) inputGraphFiles.add(cmdArgs.get(index + 1)); else throw new IllegalArgumentException(graphFile + " is not a valid .net File");
            } else {
                while (index + 1 < cmdArgs.size()) {
                    index++;
                    if (!cmdArgs.get(index).startsWith("-")) {
                        File graphFile = new File(cmdArgs.get(index));
                        if (graphFile.isFile() && graphFile.getName().matches("*.net")) inputGraphFiles.add(cmdArgs.get(index + 1)); else throw new IllegalArgumentException(graphFile + " is not a valid .net File");
                    } else break;
                }
            }
        } else throw new IllegalArgumentException("No Valid Input graph Files Provided");
        if (inputGraphFiles.size() == 0) throw new IllegalArgumentException("No Valid Input graph Files Provided");
        if ((index = cmdArgs.indexOf("-o")) != -1) {
            File output = new File(cmdArgs.get(index + 1));
            outputFile = cmdArgs.get(index + 1);
        } else throw new IllegalArgumentException("Output Graph File Must be Provided");
        index = cmdArgs.indexOf("-m");
        while (++index < cmdArgs.size()) {
            if (!cmdArgs.get(index).startsWith("-")) {
                if (graphMeasureList.contains(cmdArgs.get(index))) measures.add(cmdArgs.get(index)); else throw new IllegalArgumentException("Currently no measure named " + cmdArgs.get(index) + " is Supported");
            } else break;
        }
        if (measures.isEmpty()) {
            throw new IllegalArgumentException("Please provide atleast one measure that needs to be " + " calculated ");
        }
        System.out.println(" Command Line Arguments Successfully Parsed");
        generateInputGraphs();
        generateMeasures();
    }

    private static void generateInputGraphs() {
        Factory<Edges> edge = new Factory<Edges>() {

            public Edges create() {
                return new Edges();
            }
        };
        Factory<Nodes> vertex = new Factory<Nodes>() {

            public Nodes create() {
                return new Nodes();
            }
        };
        try {
            if (type == DYNAMIC_GRAPH) {
                DynamicPajekNetReader reader = new DynamicPajekNetReader(vertex, edge);
                DynamicSparseGraph<Nodes, Edges> gv = new DynamicSparseGraph<Nodes, Edges>();
                gv = (DynamicSparseGraph<Nodes, Edges>) reader.load(inputGraphFiles.get(0), gv);
                Factory<Graph<Nodes, Edges>> factory = SparseGraph.getFactory();
                inputGraphs = (Vector<Graph<Nodes, Edges>>) gv.getStaticTimeGraphs(factory);
            } else {
                inputGraphs = new Vector();
                for (int i = 0; i < inputGraphFiles.size(); i++) {
                    PajekNetReader reader = new PajekNetReader(vertex, edge);
                    SparseGraph<Nodes, Edges> gv = new SparseGraph<Nodes, Edges>();
                    gv = (SparseGraph<Nodes, Edges>) reader.load(inputGraphFiles.get(0), gv);
                    inputGraphs.add(gv);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GraphMeasures.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void generateHeading(PrintWriter out) {
        String[] heading = { "Period", "Vertices" };
        out.format(format, (Object[]) heading);
        for (int j = 0; j < measures.size(); j++) out.format("\t\t  %1$-10s", measures.get(j));
    }

    private static void generateMeasures() {
        try {
            FileWriter writer = new FileWriter(outputFile);
            PrintWriter out = new PrintWriter(writer);
            generateHeading(out);
            for (int i = 0; i < inputGraphs.size(); i++) {
                System.err.println(" Period " + (i + 1));
                out.print("\n");
                SparseGraph<Nodes, Edges> gv = (SparseGraph<Nodes, Edges>) inputGraphs.get(i);
                String[][] vertexMeasures = new String[measures.size()][gv.getVertexCount()];
                String[][] edgeMeasures = new String[measures.size()][gv.getEdgeCount()];
                for (int j = 0; j < measures.size(); j++) {
                    getMeasureValue(j, gv, vertexMeasures, edgeMeasures);
                }
                int k = 0;
                for (Nodes v : gv.getVertices()) {
                    out.format("\n" + format, (i + 1), v.toString());
                    for (int j = 0; j < measures.size(); j++) out.format("\t\t  %1$10s", vertexMeasures[j][k]);
                    k++;
                }
                k = 0;
                for (Edges e : gv.getEdges()) {
                    out.format("\n" + format, (i + 1), e.toString());
                    for (int j = 0; j < measures.size(); j++) out.format("\t\t  %1$10s", edgeMeasures[j][k]);
                    k++;
                }
            }
            out.close();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(GraphMeasures.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void getMeasureValue(int j, SparseGraph<Nodes, Edges> gv, String[][] vertexMeasure, String[][] edgeMeasure) {
        String get = measures.get(j);
        int index = graphMeasureList.indexOf(get);
        int i = 0;
        DecimalFormat doubleFormat = new DecimalFormat("0.000000");
        switch(index) {
            case 0:
                DegreeScorer scorer = new DegreeScorer(gv);
                i = 0;
                for (Nodes v : gv.getVertices()) {
                    vertexMeasure[j][i++] = scorer.getVertexScore(v).toString();
                }
                i = 0;
                for (Edges e : gv.getEdges()) {
                    edgeMeasure[j][i++] = "-NA-";
                }
                return;
            case 1:
                BetweennessCentrality ranker = new BetweennessCentrality(gv);
                ranker.setRemoveRankScoresOnFinalize(false);
                ranker.evaluate();
                i = 0;
                for (Nodes v : gv.getVertices()) {
                    vertexMeasure[j][i++] = doubleFormat.format(ranker.getVertexRankScore(v));
                }
                i = 0;
                for (Edges e : gv.getEdges()) {
                    edgeMeasure[j][i++] = doubleFormat.format(ranker.getEdgeRankScore(e));
                }
                return;
        }
    }
}
