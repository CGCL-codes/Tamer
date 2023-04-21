package edu.arizona.cs.learn.timeseries.visualization;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import edu.arizona.cs.learn.timeseries.model.Instance;
import edu.arizona.cs.learn.timeseries.model.SequenceType;
import edu.arizona.cs.learn.timeseries.model.signature.CompleteSignature;
import edu.arizona.cs.learn.timeseries.model.signature.Signature;
import edu.arizona.cs.learn.timeseries.model.symbols.Symbol;
import edu.arizona.cs.learn.timeseries.visualization.graph.GraphMethods;
import edu.arizona.cs.learn.timeseries.visualization.graph.Node;
import edu.arizona.cs.learn.util.Range;
import edu.arizona.cs.learn.util.Utils;
import edu.arizona.cs.learn.util.graph.Edge;
import edu.uci.ics.jung.graph.DirectedGraph;

public class SignatureGraphs {

    private static Logger logger = Logger.getLogger(SignatureGraphs.class);

    public static void main(String[] args) {
        Utils.LIMIT_RELATIONS = true;
        Utils.WINDOW = 5;
        singleGraph("chpt1-approach", SequenceType.starts, 2);
    }

    public static void singleGraph(String name, SequenceType type, int min) {
        List<Instance> instances = Instance.load(name, new File("data/input/" + name + ".lisp"), type);
        CompleteSignature signature = new CompleteSignature(name);
        signature.train(instances);
        List<Symbol[]> subset = TableFactory.subset(signature, min);
        DirectedGraph<Node, Edge> graph = TableFactory.buildGraph(subset, signature);
        GraphMethods.toDot(graph, name + "-before.dot");
        TableFactory.collapseGraph(graph);
        GraphMethods.toDot(graph, name + "-after.dot");
    }

    public static void mergeGraph(List<String> names, SequenceType type) {
        List<CompleteSignature> signatures = new ArrayList<CompleteSignature>();
        List<Range> ranges = new ArrayList<Range>();
        int min = 0;
        for (String name : names) {
            List<Instance> instances = Instance.load(name, new File("data/input/" + name + ".lisp"), type);
            CompleteSignature signature = new CompleteSignature(name);
            signature.train(instances);
            signatures.add((CompleteSignature) signature.prune(signature.trainingSize() / 2));
            ranges.add(new Range(min, min + signature.trainingSize()));
            min += signature.trainingSize();
        }
        CompleteSignature starter = signatures.get(0);
        for (int i = 1; i < signatures.size(); ++i) {
            starter.merge(signatures.get(i));
        }
        List<Symbol[]> subset = TableFactory.subset(starter, 10);
        DirectedGraph<Node, Edge> graph = TableFactory.buildGraph(subset, starter);
        GraphMethods.color(graph, subset, ranges);
        GraphMethods.toDot(graph, "prop-finish.dot");
    }

    public static void subgraphBuilder() {
    }
}
