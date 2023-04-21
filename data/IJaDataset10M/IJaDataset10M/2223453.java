package ontorama.webkbtools.writer.rdf.test;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import ontorama.OntoramaConfig;
import ontorama.backends.p2p.model.P2PEdge;
import ontorama.backends.p2p.model.P2PEdgeImpl;
import ontorama.backends.p2p.model.P2PGraph;
import ontorama.backends.p2p.model.P2PGraphImpl;
import ontorama.backends.p2p.model.P2PNode;
import ontorama.backends.p2p.model.P2PNodeImpl;
import ontorama.model.EdgeType;
import ontorama.model.Node;
import ontorama.model.util.GraphModificationException;
import ontorama.webkbtools.TestWebkbtoolsPackage;
import ontorama.webkbtools.query.parser.Parser;
import ontorama.webkbtools.query.parser.ParserResult;
import ontorama.webkbtools.query.parser.rdf.RdfP2pParser;
import ontorama.webkbtools.util.NoSuchRelationLinkException;
import ontorama.webkbtools.util.ParserException;
import ontorama.webkbtools.writer.ModelWriter;
import ontorama.webkbtools.writer.ModelWriterException;
import ontorama.webkbtools.writer.rdf.RdfP2PWriter;

public class TestRdfP2PWriter extends TestCase {

    private P2PGraph _p2pGraph;

    private String _ns_webkb = "http://www.webkb.org/kb/theKB_terms.rdf/";

    private String _ns_dc = "http://www.cogsi.princeton.edu/~wn/";

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

    private EdgeType _edgeType_creator;

    private EdgeType _edgeType_description;

    private P2PNode _tail;

    private P2PNode _wnCreator;

    private P2PNode _tailComment;

    private P2PNode _outgrowth;

    private P2PNode _dock;

    private P2PNode _tailSynonym;

    private P2PEdge _e1;

    private P2PEdge _e2;

    private P2PEdge _e3;

    private P2PEdge _e4;

    private P2PEdge _e5;

    List _testNodesList;

    List _testEdgesList;

    public TestRdfP2PWriter(String s) {
        super(s);
    }

    public void setUp() throws ModelWriterException, GraphModificationException, NoSuchRelationLinkException, URISyntaxException, ParserException {
        _p2pGraph = new P2PGraphImpl();
        _edgeType_subtype = OntoramaConfig.getEdgeType(TestWebkbtoolsPackage.edgeName_subtype);
        _edgeType_subtype.setNamespace(_rdfsNamespace);
        _edgeType_part = OntoramaConfig.getEdgeType(TestWebkbtoolsPackage.edgeName_part);
        _edgeType_part.setNamespace(_pmNamespace);
        _edgeType_similar = OntoramaConfig.getEdgeType(TestWebkbtoolsPackage.edgeName_similar);
        _edgeType_similar.setNamespace(_pmNamespace);
        _edgeType_synonym = OntoramaConfig.getEdgeType(TestWebkbtoolsPackage.edgeName_synonym);
        _edgeType_synonym.setNamespace(_rdfsNamespace);
        _edgeType_creator = OntoramaConfig.getEdgeType(TestWebkbtoolsPackage.edgeName_creator);
        _edgeType_creator.setNamespace(_dcNamespace);
        _edgeType_description = OntoramaConfig.getEdgeType(TestWebkbtoolsPackage.edgeName_description);
        _edgeType_description.setNamespace(_rdfsNamespace);
        _tail = new P2PNodeImpl("wn#Tail", _ns_webkb + "wn#Tail");
        _wnCreator = new P2PNodeImpl(_ns_dc, _ns_dc);
        _tailComment = new P2PNodeImpl("some comment for tail node", "some comment for tail node");
        _outgrowth = new P2PNodeImpl("wn#Outgrowth", _ns_webkb + "wn#Outgrowth");
        _dock = new P2PNodeImpl("wn#Dock_4", _ns_webkb + "wn#Dock_4");
        _tailSynonym = new P2PNodeImpl("tail", "tail");
        _p2pGraph.addNode(_tail);
        _p2pGraph.addNode(_wnCreator);
        _p2pGraph.addNode(_tailComment);
        _p2pGraph.addNode(_outgrowth);
        _p2pGraph.addNode(_dock);
        _e1 = new P2PEdgeImpl(_outgrowth, _tail, _edgeType_subtype);
        _e2 = new P2PEdgeImpl(_dock, _tail, _edgeType_subtype);
        _e3 = new P2PEdgeImpl(_tail, _wnCreator, _edgeType_creator);
        _e4 = new P2PEdgeImpl(_tail, _tailComment, _edgeType_description);
        _e5 = new P2PEdgeImpl(_tail, _tailSynonym, _edgeType_synonym);
        URI userUri1 = new URI("mailto:johan@ontorama.org");
        URI userUri2 = new URI("mailto:henrik@ontorama.org");
        URI userUri3 = new URI("mailto:nataliya@ontorama.org");
        URI userUri4 = new URI("mailto:joeDoe@ontorama.org");
        _tail.addAssertion(userUri1);
        _tail.addAssertion(userUri2);
        _tail.addRejection(userUri4);
        _e1.addRejection(userUri3);
        _e2.addAssertion(userUri1);
        _p2pGraph.addEdge(_e1);
        _p2pGraph.addEdge(_e2);
        _p2pGraph.addEdge(_e3);
        _p2pGraph.addEdge(_e4);
        _p2pGraph.addEdge(_e5);
        ModelWriter modelWriter = new RdfP2PWriter();
        Writer _writer = new StringWriter();
        modelWriter.write(_p2pGraph, _writer);
        String str = _writer.toString();
        System.out.println(str);
        Reader r = new StringReader(str);
        Parser parser = new RdfP2pParser();
        ParserResult parserResult = parser.getResult(r);
        _testNodesList = parserResult.getNodesList();
        _testEdgesList = parserResult.getEdgesList();
    }

    public void testNodesNum() {
        assertEquals("read back number of nodes ", 6, _testNodesList.size());
    }

    public void testEdgesNum() {
        assertEquals("read back number of edges ", 5, _testEdgesList.size());
    }

    public void testNodeAssertionsRejections() {
        P2PNode node = findNodeInListByName("wn#Tail");
        assertEquals("result should contain node wn#Tail", true, (node != null));
        assertEquals("node wn#Tail should have num of assertions ", 2, node.getAssertionsList().size());
        assertEquals("node wn#Tail should have num of rejections ", 1, node.getRejectionsList().size());
    }

    public void testEdgeAssertionsRejections_e2() {
        P2PEdge edge = findEdgeInList("wn#Dock_4", "wn#Tail", _edgeType_subtype);
        assertEquals("result should contain edge ", true, (edge != null));
        assertEquals("edge should have num of assertions", 1, edge.getAssertionsList().size());
        assertEquals("edge shouldn't have any rejections ", 0, edge.getRejectionsList().size());
    }

    public void testEdgeAssertionsRejections_e1() {
        P2PEdge edge = findEdgeInList("wn#Outgrowth", "wn#Tail", _edgeType_subtype);
        assertEquals("result should contain edge ", true, (edge != null));
        assertEquals("edge should have num of assertions", 0, edge.getAssertionsList().size());
        assertEquals("edge shouldn't have any rejections ", 1, edge.getRejectionsList().size());
    }

    private P2PNode findNodeInListByName(String nodeName) {
        Iterator it = _testNodesList.iterator();
        while (it.hasNext()) {
            P2PNode curNode = (P2PNode) it.next();
            if (curNode.getName().endsWith(nodeName)) {
                return curNode;
            }
        }
        return null;
    }

    private P2PEdge findEdgeInList(String fromNodeName, String toNodeName, EdgeType edgeType) {
        Iterator it = _testEdgesList.iterator();
        while (it.hasNext()) {
            P2PEdge curEdge = (P2PEdge) it.next();
            Node fromNode = curEdge.getFromNode();
            Node toNode = curEdge.getToNode();
            if (fromNode.getName().endsWith(fromNodeName)) {
                if (toNode.getName().endsWith(toNodeName)) {
                    if (curEdge.getEdgeType().equals(edgeType)) {
                        return curEdge;
                    }
                }
            }
        }
        return null;
    }
}
