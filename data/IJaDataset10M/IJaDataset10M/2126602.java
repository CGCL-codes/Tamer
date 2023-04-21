package at.ac.tuwien.ifs.alviz.smallworld.types;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultGraph;
import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.graph.io.GraphReader;

/**
 * Reads graphs of the format where each line =
 * "node1 node2"
 * indicates an edge between node1 and node2.
 * No nodes of degree zero allowed.
 *
 * @author Stephen
 */
public class BasicGraphReader implements GraphReader {

    public Graph loadGraph(String filename) throws FileNotFoundException, IOException {
        return loadGraph(new File(filename));
    }

    public Graph loadGraph(URL url) throws IOException {
        return null;
    }

    public Graph loadGraph(File f) throws FileNotFoundException, IOException {
        Graph graph = new DefaultGraph();
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line = "";
        int node_counter = 0;
        int foo = 0;
        Map<String, Node> node_map = new HashMap<String, Node>();
        while ((line = br.readLine()) != null) {
            String[] items = line.split("\\s+");
            if (items.length >= 2) {
                foo++;
                Node node1 = null;
                Node node2 = null;
                if ((node1 = node_map.get(items[0])) == null) {
                    node1 = new DefaultCluster(0, 0);
                    node1.setAttribute("label", items[0]);
                    node1.setAttribute("id", "" + node_counter);
                    node_counter++;
                    graph.addNode(node1);
                    node_map.put(items[0], node1);
                }
                if ((node2 = node_map.get(items[1])) == null) {
                    node2 = new DefaultCluster(0, 0);
                    node2.setAttribute("label", items[1]);
                    node2.setAttribute("id", "" + node_counter);
                    node_counter++;
                    graph.addNode(node2);
                    node_map.put(items[1], node2);
                }
                Edge edge = new DefaultEdge(node1, node2);
                graph.addEdge(edge);
            }
        }
        System.out.println("edges = " + foo);
        return graph;
    }

    public Graph loadGraph(InputStream is) throws IOException {
        Graph graph = new DefaultGraph();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = "";
        int node_counter = 0;
        int foo = 0;
        Map<String, Node> node_map = new HashMap<String, Node>();
        while ((line = br.readLine()) != null) {
            String[] items = line.split("\\s+");
            if (items.length >= 2) {
                foo++;
                Node node1 = null;
                Node node2 = null;
                if ((node1 = node_map.get(items[0])) == null) {
                    node1 = new DefaultCluster(0, 0);
                    node1.setAttribute("label", items[0]);
                    node1.setAttribute("id", "" + node_counter);
                    node_counter++;
                    graph.addNode(node1);
                    node_map.put(items[0], node1);
                }
                if ((node2 = node_map.get(items[1])) == null) {
                    node2 = new DefaultCluster(0, 0);
                    node2.setAttribute("label", items[1]);
                    node2.setAttribute("id", "" + node_counter);
                    node_counter++;
                    graph.addNode(node2);
                    node_map.put(items[1], node2);
                }
                Edge edge = new DefaultEdge(node1, node2);
                graph.addEdge(edge);
            }
        }
        System.out.println("edges = " + foo);
        return graph;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            BasicGraphReader bgr = new BasicGraphReader();
            try {
                Graph graph = bgr.loadGraph(args[0]);
                System.out.println("Graph Successfully Loaded with " + graph.getNodeCount() + " nodes and " + graph.getEdgeCount() + " edges.");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
