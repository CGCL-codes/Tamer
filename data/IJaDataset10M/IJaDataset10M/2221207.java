package ontorama.backends.p2p.test;

import org.tockit.events.EventBroker;
import ontorama.OntoramaConfig;
import ontorama.backends.Backend;
import ontorama.backends.p2p.P2PBackend;
import ontorama.model.graph.Edge;
import ontorama.model.graph.Graph;
import ontorama.model.graph.GraphImpl;
import ontorama.model.graph.GraphModificationException;
import ontorama.model.graph.Node;
import ontorama.model.graph.test.TestGraphPackage;
import ontorama.ontotools.NoSuchRelationLinkException;
import junit.framework.TestCase;

/**
 * @author nataliya
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class TestP2PBackend2 extends TestCase {

    private Graph _graph;

    private P2PBackend _p2pBackend;

    private Backend _backend = OntoramaConfig.getBackend();

    /**
	 * Constructor for TestP2PBackend2.
	 * @param name
	 */
    public TestP2PBackend2(String name) {
        super(name);
    }

    protected void setUp() throws NoSuchRelationLinkException, GraphModificationException {
        _graph = new GraphImpl(new EventBroker());
        Node root = _backend.createNode("root", "root");
        Node node1 = _backend.createNode("node1", "node1");
        Node node2 = _backend.createNode("node2", "node2");
        Node node1_1 = _backend.createNode("node1.1", "node1.1");
        Node node1_2 = _backend.createNode("node1.2", "node1.2");
        Node node1_3 = _backend.createNode("node1.3", "node1.3");
        Node node2_1 = _backend.createNode("node2.1", "node2.1");
        Node node2_2 = _backend.createNode("node2.2", "node2.2");
        Edge edge1 = _backend.createEdge(root, node1, OntoramaConfig.getEdgeType(TestGraphPackage.edgeName_subtype));
        Edge edge2 = _backend.createEdge(root, node2, OntoramaConfig.getEdgeType(TestGraphPackage.edgeName_subtype));
        Edge edge3 = _backend.createEdge(node1, node1_1, OntoramaConfig.getEdgeType(TestGraphPackage.edgeName_subtype));
        Edge edge4 = _backend.createEdge(node1, node1_2, OntoramaConfig.getEdgeType(TestGraphPackage.edgeName_subtype));
        Edge edge5 = _backend.createEdge(node1, node1_3, OntoramaConfig.getEdgeType(TestGraphPackage.edgeName_subtype));
        Edge edge6 = _backend.createEdge(node2, node2_1, OntoramaConfig.getEdgeType(TestGraphPackage.edgeName_subtype));
        Edge edge7 = _backend.createEdge(node2, node2_2, OntoramaConfig.getEdgeType(TestGraphPackage.edgeName_subtype));
        Edge edge8 = _backend.createEdge(node1_3, node2_2, OntoramaConfig.getEdgeType(TestGraphPackage.edgeName_similar));
        _graph.addNode(root);
        _graph.addNode(node1);
        _graph.addNode(node2);
        _graph.addNode(node1_1);
        _graph.addNode(node1_2);
        _graph.addNode(node1_3);
        _graph.addNode(node2_1);
        _graph.addNode(node2_2);
        _graph.addEdge(edge1);
        _graph.addEdge(edge2);
        _graph.addEdge(edge3);
        _graph.addEdge(edge4);
        _graph.addEdge(edge5);
        _graph.addEdge(edge6);
        _graph.addEdge(edge7);
        _graph.addEdge(edge8);
        _p2pBackend = new P2PBackend();
    }
}
