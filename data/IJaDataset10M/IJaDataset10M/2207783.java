package n3_project.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import n3_project.ResultEditorN3Action;
import att.grappa.Edge;
import att.grappa.Graph;
import att.grappa.Node;
import att.grappa.Subgraph;
import euler.Euler;
import euler.IRDFIterator;
import euler.RDFIterator;
import euler.TripleHandler;
import eulergui.project.N3Source;
import eulergui.project.Project;
import eulergui.util.StringHelper;

/** See http://www.graphviz.org/pdf/dotguide.pdf */
public class N3toGraphviz {

    Project project;

    public N3toGraphviz(Project project) {
        this.project = project;
    }

    public Graph doGraphviz(N3Source rdfModel) {
        final Euler eulerRdfModel = new Euler();
        eulerRdfModel.load(rdfModel.getLocalN3().toString());
        return doGraphviz(new RDFIterator(eulerRdfModel));
    }

    /** enhance N-Triple by adding prefixes from the Project */
    File enhanceN3(N3Source rdfModel) {
        FileReader fr;
        try {
            fr = new FileReader(rdfModel.getLocalN3());
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("enhanceN3", e);
        }
        return ResultEditorN3Action.convertReaderIntoN3(fr, project);
    }

    private Graph doGraphviz(IRDFIterator iter) {
        iter.setCompactN3Resource(true);
        final GraphvizHandler handler = new GraphvizHandler();
        iter.visitAllURI(handler);
        handler.addImplicationEdges();
        return handler.getGraph();
    }

    class GraphvizHandler extends AbstractTripleHandler implements TripleHandler {

        private final Graph graph;

        public GraphvizHandler() {
            graph = new Graph("graph");
            graph.setAttribute("compound", "true");
        }

        @Override
        public void acceptTriple(String subject, String verb, String object, String source) {
            acceptTriple(subject, verb, object, source, graph);
        }

        private void acceptTriple(String subject, String verb, String object, String source, Subgraph container) {
            if (subject == null) {
                return;
            }
            if (object == null) {
                return;
            }
            final Edge newEdge = new Edge(container, findNodeOrAddIfNotExists(subject), findNodeOrAddIfNotExists(object));
            newEdge.setAttribute("label", "\"" + StringHelper.escapeNewLine(verb) + "\"");
            container.addEdge(newEdge);
        }

        /** PENDING maybe add to subgraph ? */
        private Node findNodeOrAddIfNotExists(String nodeName) {
            final String nodeHash = Integer.toString(nodeName.hashCode());
            Node node = graph.findNodeByName(nodeHash);
            if (node == null) {
                node = new Node(graph, nodeHash);
            }
            final String nodeLabel = "\"" + StringHelper.escapeNewLine(nodeName) + "\"";
            node.setAttribute("label", nodeLabel);
            return node;
        }

        @Override
        public void acceptList(String subject, String verb, List<String> list, String src) {
            final Node tail = findNodeOrAddIfNotExists(subject);
            for (final String object : list) {
                final Edge newEdge = new Edge(graph, tail, findNodeOrAddIfNotExists(object));
                graph.addEdge(newEdge);
            }
        }

        private final transient Map<String, Subgraph> subgraphsAntecedents = new TreeMap<String, Subgraph>();

        private final transient Map<String, Subgraph> subgraphsConsequents = new TreeMap<String, Subgraph>();

        /** one of the nodes for a rule number */
        private final transient Map<String, Node> nodesAntecedents = new TreeMap<String, Node>();

        private final transient Map<String, Node> nodesConsequents = new TreeMap<String, Node>();

        @Override
        public void acceptAntecedent(String subject, String verb, String object, String source, String ruleId) {
            final Subgraph sg = findSubgraph(ruleId, "A", subgraphsAntecedents);
            acceptTriple(subject, verb, object, source, sg);
            nodesAntecedents.put(ruleId, findNodeOrAddIfNotExists(subject));
        }

        /**
		 * generate 2 nodes and and a vertex for part of the Consequent of a
		 * rule
		 */
        @Override
        public void acceptConsequent(String subject, String verb, String object, String source, String ruleId) {
            if (subject == null) {
                return;
            }
            if (object == null) {
                return;
            }
            final Subgraph sg = findSubgraph(ruleId, "C", subgraphsConsequents);
            acceptTriple(subject, verb, object, source, sg);
            nodesConsequents.put(ruleId, findNodeOrAddIfNotExists(subject));
        }

        private Subgraph findSubgraph(String ruleId, String side, Map<String, Subgraph> subgraphMap) {
            Subgraph result = subgraphMap.get(ruleId);
            if (result == null) {
                result = new Subgraph(graph, "cluster" + side + ruleId);
                subgraphMap.put(ruleId, result);
            }
            return result;
        }

        /** add '=>' to connect Antecedents and Consequents subgraphs */
        private void addImplicationEdges() {
            final Set<String> ks = subgraphsAntecedents.keySet();
            for (final String ruleId : ks) {
                final Node nodeConsequent = nodesConsequents.get(ruleId);
                final Node nodeAntecedent = nodesAntecedents.get(ruleId);
                if (nodeAntecedent != null && nodeConsequent != null) {
                    final Edge newEdge = new Edge(graph, nodeAntecedent, nodeConsequent);
                    newEdge.setAttribute("lhead", "cluster" + "A" + ruleId);
                    newEdge.setAttribute("ltail", "cluster" + "C" + ruleId);
                    graph.addEdge(newEdge);
                }
            }
        }

        public Graph getGraph() {
            return graph;
        }
    }
}
