package paperscope;

import java.lang.reflect.*;
import java.util.Vector;
import prefuse.data.Graph;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.tuple.TupleSet;
import prefuse.data.Schema;
import prefuse.data.io.GraphMLWriter;
import prefuse.data.io.GraphMLReader;
import java.io.*;

public class ResultXML {

    Graph graphML;

    public ResultXML() {
    }

    public boolean writeFile(Graph graph, String fileName) {
        GraphMLWriter graphWriter = new GraphMLWriter();
        try {
            File newFile = new File(fileName);
            if (newFile.exists()) {
                graphWriter.writeGraph(graph, newFile);
            } else {
                newFile.createNewFile();
                graphWriter.writeGraph(graph, newFile);
            }
            return true;
        } catch (Exception e) {
            System.out.println("Exception " + e);
            return false;
        }
    }

    public boolean openFile(File openMe) {
        if (openMe.exists()) {
            try {
                GraphMLReader graphReader = new GraphMLReader();
                this.graphML = graphReader.readGraph(openMe);
                return true;
            } catch (Exception e) {
                System.out.println("Exception " + e);
                return false;
            }
        } else return false;
    }

    public Graph getGraph() {
        return this.graphML;
    }

    public String writeManualXML(Graph g) {
        Table nodes = g.getNodeTable();
        Table edges = g.getEdgeTable();
        Schema nodeSchema = nodes.getSchema();
        Schema edgeSchema = edges.getSchema();
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<!--  A paper and all its citations and references  -->\n" + "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\">\n" + "<graph edgedefault=\"directed\">\n\n";
        String nodesTableXML = "<!-- nodes -->\n";
        for (int j = 0; j < nodes.getRowCount(); j++) {
            String row = "";
            for (int k = 0; k < nodeSchema.getColumnCount(); k++) {
                if (k == 0) {
                    row = "<node id=\"" + nodes.get(j, k) + "\">\n";
                } else {
                    row = row + "   <data key=\"" + nodeSchema.getColumnName(k) + "\">" + nodes.get(j, k) + "</data>\n";
                }
            }
            row = row + "</node>\n";
            nodesTableXML = nodesTableXML + row;
        }
        String edgesTableXML = "<!-- edges -->\n";
        for (int j = 0; j < edges.getRowCount(); j++) {
            String row = "<edge source=\"" + edges.get(j, 0) + "\" " + "target=\"" + edges.get(j, 1) + "\"></edge>\n";
            edgesTableXML = edgesTableXML + row;
        }
        String footer = "</graph>\n</graphml>";
        return (header + nodesTableXML + edgesTableXML + footer);
    }
}
