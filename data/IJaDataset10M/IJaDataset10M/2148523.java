package ontorama.webkbtools.writer.rdf.test;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import junit.framework.TestCase;
import ontorama.OntoramaConfig;
import ontorama.model.Edge;
import ontorama.model.EdgeImpl;
import ontorama.model.EdgeType;
import ontorama.model.Graph;
import ontorama.model.GraphImpl;
import ontorama.model.Node;
import ontorama.model.NodeImpl;
import ontorama.model.util.GraphModificationException;
import ontorama.webkbtools.TestWebkbtoolsPackage;
import ontorama.webkbtools.query.parser.Parser;
import ontorama.webkbtools.query.parser.ParserResult;
import ontorama.webkbtools.query.parser.rdf.RdfDamlParser;
import ontorama.webkbtools.util.NoSuchRelationLinkException;
import ontorama.webkbtools.util.ParserException;
import ontorama.webkbtools.writer.ModelWriter;
import ontorama.webkbtools.writer.ModelWriterException;
import ontorama.webkbtools.writer.rdf.RdfModelWriter;
import org.tockit.events.EventBroker;

public class TestRdfWriter extends TestCase {

    private Graph _graph;

    private StringWriter _writer;

    private List _testNodesList;

    private List _testEdgesList;

    private String _chairName = "Chair";

    private String _armchairName = "Armchair";

    private String _furnitureName = "Furniture";

    private String _backrestName = "Backrest";

    private String _legName = "Leg";

    private String _otherChairsName = "OtherChairs";

    private String _synonymChairName = "_chair";

    private static String _ontoramaNamespace = "http://www.webkb.org/ontorama/test#";

    private static String _rdfsNamespace = "http://www.w3.org/2000/01/rdf-schema#";

    private static String _dcNamespace = "http://purl.org/metadata/dublin_core#";

    private static String _damlNamespace = "http://www.daml.org/2000/10/daml-ont#";

    private static String _pmNamespace = "http://www.webkb.org/kb/theKB_terms.rdf/pm#";

    private static String _rdfNamespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    private EdgeType _edgeType_subtype;

    private EdgeType _edgeType_part;

    private EdgeType _edgeType_similar;

    private EdgeType _edgeType_synonym;

    private Node _chair;

    private Node _armchair;

    private Node _furniture;

    private Node _backrest;

    private Node _leg;

    private Node _otherChairs;

    private Node _synonym;

    private Edge _edge1;

    private Edge _edge6;

    /**
     * Test Rdf Writer.
     * The way we are going to test this is: Write a stream from our model, then read and parse
     * it and then check if it is consistent with the model.
     */
    public TestRdfWriter(String s) {
        super(s);
    }

    public void setUp() throws NoSuchRelationLinkException, ModelWriterException, GraphModificationException, ParserException {
        _testEdgesList = new LinkedList();
        _testNodesList = new LinkedList();
        _graph = new GraphImpl(new EventBroker());
        _chair = new NodeImpl(_chairName, _ontoramaNamespace + _chairName);
        _armchair = new NodeImpl(_armchairName, _ontoramaNamespace + _armchairName);
        _furniture = new NodeImpl(_furnitureName, _ontoramaNamespace + _furnitureName);
        _backrest = new NodeImpl(_backrestName, _ontoramaNamespace + _backrestName);
        _leg = new NodeImpl(_legName, _ontoramaNamespace + _legName);
        _otherChairs = new NodeImpl(_otherChairsName, _ontoramaNamespace + _otherChairsName);
        _synonym = new NodeImpl(_synonymChairName);
        _edgeType_subtype = OntoramaConfig.getEdgeType(TestWebkbtoolsPackage.edgeName_subtype);
        _edgeType_subtype.setNamespace(_rdfsNamespace);
        _edgeType_part = OntoramaConfig.getEdgeType(TestWebkbtoolsPackage.edgeName_part);
        _edgeType_part.setNamespace(_pmNamespace);
        _edgeType_similar = OntoramaConfig.getEdgeType(TestWebkbtoolsPackage.edgeName_similar);
        _edgeType_similar.setNamespace(_pmNamespace);
        _edgeType_synonym = OntoramaConfig.getEdgeType(TestWebkbtoolsPackage.edgeName_synonym);
        _edgeType_synonym.setNamespace(_rdfsNamespace);
        _edge1 = new EdgeImpl(_chair, _armchair, _edgeType_subtype);
        _graph.addEdge(_edge1);
        Edge edge2 = new EdgeImpl(_furniture, _chair, _edgeType_subtype);
        _graph.addEdge(edge2);
        Edge edge3 = new EdgeImpl(_chair, _backrest, _edgeType_part);
        _graph.addEdge(edge3);
        Edge edge4 = new EdgeImpl(_chair, _leg, _edgeType_part);
        _graph.addEdge(edge4);
        Edge edge5 = new EdgeImpl(_chair, _otherChairs, _edgeType_similar);
        _graph.addEdge(edge5);
        _edge6 = new EdgeImpl(_chair, _synonym, _edgeType_synonym);
        _graph.addEdge(_edge6);
        ModelWriter modelWriter = new RdfModelWriter();
        _writer = new StringWriter();
        modelWriter.write(_graph, _writer);
        String str = _writer.toString();
        Reader r = new StringReader(str);
        Parser parser = new RdfDamlParser();
        ParserResult parserResult = parser.getResult(r);
        _testNodesList = parserResult.getNodesList();
        _testEdgesList = parserResult.getEdgesList();
    }

    public void testResultingNodesList() {
        assertEquals("number of nodes read from RDF should be the same as in the original model", 7, _testNodesList.size());
    }

    public void testResultingEdgesList() {
        assertEquals("number of edges read from RDF should be the same as in the original model", 6, _testEdgesList.size());
    }

    public void testNodeInResultingModel_chair() {
        Node chairNode = findNodeInListByName("test#" + _chairName);
        assertEquals("node exist in the model ", true, (chairNode != null));
        assertEquals("identifiers should match ", _chair.getIdentifier(), chairNode.getIdentifier());
    }

    public void testNodeInResultingModel_armchair() {
        Node armchairNode = findNodeInListByName("test#" + _armchairName);
        assertEquals("node exist in the model ", true, (armchairNode != null));
        assertEquals("identifiers should match ", armchairNode.getIdentifier(), armchairNode.getIdentifier());
    }

    public void testEdgeInResultModel_edge1() {
        Edge edge = findEdgeInList("test#" + _chairName, "test#" + _armchairName, _edgeType_subtype);
        assertEquals("edge exist in the model", true, (edge != null));
    }

    public void testEdgeInResultModel_edge6() {
        Edge edge = findEdgeInList("test#" + _chairName, _synonymChairName, _edgeType_synonym);
        assertEquals("edge exist in the model", true, (edge != null));
    }

    private Node findNodeInListByName(String nodeName) {
        Iterator it = _testNodesList.iterator();
        while (it.hasNext()) {
            Node curNode = (Node) it.next();
            if (curNode.getName().equals(nodeName)) {
                return curNode;
            }
        }
        return null;
    }

    private Edge findEdgeInList(String fromNodeName, String toNodeName, EdgeType edgeType) {
        Iterator it = _testEdgesList.iterator();
        while (it.hasNext()) {
            Edge curEdge = (Edge) it.next();
            Node fromNode = curEdge.getFromNode();
            Node toNode = curEdge.getToNode();
            if (fromNode.getName().equals(fromNodeName)) {
                if (toNode.getName().equals(toNodeName)) {
                    if (curEdge.getEdgeType().equals(edgeType)) {
                        return curEdge;
                    }
                }
            }
        }
        return null;
    }
}
