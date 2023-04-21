package ontorama.webkbtools.query.parser.rdf.test;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import ontorama.OntoramaConfig;
import ontorama.backends.p2p.model.P2PEdge;
import ontorama.backends.p2p.model.P2PNode;
import ontorama.util.TestingUtils;
import ontorama.webkbtools.inputsource.Source;
import ontorama.webkbtools.query.Query;
import ontorama.webkbtools.query.parser.Parser;
import ontorama.webkbtools.query.parser.ParserResult;
import ontorama.webkbtools.query.parser.rdf.RdfP2pParser;

public class TestRdfP2pParser extends TestCase {

    private List _nodesList;

    private List _edgesList;

    public TestRdfP2pParser(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        System.out.println("\nsetUp method");
        OntoramaConfig.loadAllConfig("examples/test/p2p/examplesConfig.xml", "ontorama.properties", "examples/test/p2p/config.xml");
        OntoramaConfig.setCurrentExample(TestingUtils.getExampleByName("p2p_test1"));
        Source source = (Source) (Class.forName(OntoramaConfig.sourcePackageName).newInstance());
        Reader r = source.getSourceResult(OntoramaConfig.sourceUri, new Query("wn#Tail")).getReader();
        Parser parser = new RdfP2pParser();
        ParserResult parserResult = parser.getResult(r);
        _nodesList = parserResult.getNodesList();
        _edgesList = parserResult.getEdgesList();
        System.out.println("\n\n\n");
        Iterator it = parserResult.getNodesList().iterator();
        while (it.hasNext()) {
            P2PNode node = (P2PNode) it.next();
            System.out.println("node = " + node);
            System.out.println("\tassertions: " + node.getAssertionsList());
            System.out.println("\trejections: " + node.getRejectionsList());
        }
        it = parserResult.getEdgesList().iterator();
        while (it.hasNext()) {
            P2PEdge edge = (P2PEdge) it.next();
            System.out.println("edge = " + edge);
            System.out.println("\trejections: " + edge.getRejectionsList());
            System.out.println("\tassertions: " + edge.getAssertionsList());
        }
    }

    public void testNodesNum() {
        assertEquals("number of nodes ", 11, _nodesList.size());
    }

    public void testEdgesNum() {
        assertEquals("number of edges ", 11, _edgesList.size());
    }

    public void testNode_tail() {
        P2PNode tailNode = getNodeFromList("http://www.webkb.org/kb/theKB_terms.rdf/wn#Tail");
        assertEquals("returned list should contain node wn#Tail ", true, (tailNode != null));
        assertEquals("number of assertions for node wn#Tail ", 2, tailNode.getAssertionsList().size());
        assertEquals("number of rejections for node wn#Tail ", 1, tailNode.getRejectionsList().size());
    }

    public void testNode_outgrowth() {
        P2PNode node = getNodeFromList("http://www.webkb.org/kb/theKB_terms.rdf/wn#Outgrowth");
        assertEquals("returned list should contain node wn#Outgrowth ", true, (node != null));
        assertEquals("number of assertions for node wn#Outgrowth ", 0, node.getAssertionsList().size());
        assertEquals("number of rejections for node wn#Outgrowth ", 0, node.getRejectionsList().size());
    }

    public void testEdge() {
        P2PNode toNode = getNodeFromList("http://www.webkb.org/kb/theKB_terms.rdf/wn#Tail");
        P2PNode fromNode = getNodeFromList("http://www.webkb.org/kb/theKB_terms.rdf/wn#Outgrowth");
        P2PEdge edge = getEdgeFromList(fromNode, toNode);
        assertEquals("edge should exist in the edgesList ", true, (edge != null));
        assertEquals("number of asserstions for edge ", 1, edge.getAssertionsList().size());
        assertEquals("number of rejections for edge ", 1, edge.getRejectionsList().size());
    }

    private P2PNode getNodeFromList(String nodeName) {
        Iterator it = _nodesList.iterator();
        while (it.hasNext()) {
            P2PNode node = (P2PNode) it.next();
            if (node.getName().equals(nodeName)) {
                return node;
            }
        }
        return null;
    }

    private P2PEdge getEdgeFromList(P2PNode fromNode, P2PNode toNode) {
        Iterator it = _edgesList.iterator();
        while (it.hasNext()) {
            P2PEdge edge = (P2PEdge) it.next();
            if (edge.getFromNode().equals(fromNode)) {
                if (edge.getToNode().equals(toNode)) {
                    return edge;
                }
            }
        }
        return null;
    }
}
