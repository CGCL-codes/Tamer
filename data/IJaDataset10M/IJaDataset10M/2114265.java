package ontorama.model.graph.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import junit.framework.TestCase;
import ontorama.model.graph.Node;
import ontorama.model.graph.NodeImpl;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: DSTC</p>
 * @author nataliya
 * @version 1.0
 *
 * methods not tested:
 * - all private and protected methods
 * - calculateDepths()
 * - setDepth() because there are no way to check it since getDepth is protected.
 */
public class TestNode extends TestCase {

    private ontorama.model.graph.Node node1;

    private ontorama.model.graph.Node node2;

    private ontorama.model.graph.Node node3;

    private String nodeIdentifier1 = "some.node.identifier";

    private ontorama.model.graph.Node cloneNode2;

    private ontorama.model.graph.Node cloneNode3;

    private URI creatorUri1;

    private URI creatorUri2;

    /**
     *
     */
    public TestNode(String name) {
        super(name);
    }

    /**
     *
     */
    protected void setUp() throws URISyntaxException {
        creatorUri1 = new URI("ontoMailto:someone@ontorama.org");
        creatorUri2 = new URI("ontoHttp://ontorama.ort/someone.html");
        node1 = new ontorama.model.graph.NodeImpl("node1", nodeIdentifier1);
        node2 = new ontorama.model.graph.NodeImpl("node2");
        node3 = new ontorama.model.graph.NodeImpl("node3");
        cloneNode2 = node2.makeClone();
        cloneNode3 = node2.makeClone();
        node1.setFoldState(true);
        node3.setFoldState(false);
        node1.setCreatorUri(creatorUri1);
        node2.setCreatorUri(creatorUri2);
    }

    /**
     * test method getName()
     */
    public void testGetName() {
        assertEquals("node1 name", "node1", node1.getName());
        assertEquals("node2 name", "node2", node2.getName());
    }

    /**
     *
     */
    public void testGetFullName() {
        assertEquals("node1 full name", nodeIdentifier1, node1.getIdentifier());
        assertEquals("node2 full name", "node2", node2.getIdentifier());
    }

    /**
     *
     */
    public void testSetFullName() throws URISyntaxException {
        String testIdentifier1 = "htpp://somewhere.com/ontorama/node1";
        String testIdentifier2 = "http://somewhere.com/ontorama/node2";
        node1.setIdentifier(testIdentifier1);
        node2.setIdentifier(testIdentifier2);
        assertEquals("testing setIdentifier() for node1", testIdentifier1, node1.getIdentifier());
        assertEquals("testing setIdentifier() for node2", testIdentifier2, node2.getIdentifier());
    }

    /**
     * test method getClones()
     */
    public void testGetClones() {
        assertEquals("number of clones for node2", 2, node2.getClones().size());
        assertEquals("number of clones for cloneNode2", 2, cloneNode2.getClones().size());
        assertEquals("clones for node2 contain cloneNode2", true, node2.getClones().contains(cloneNode2));
        assertEquals("clones for cloneNode3 contain node2", true, cloneNode3.getClones().contains(node2));
        assertEquals("number of clones for node1", 0, node1.getClones().size());
    }

    /**
     * test method makeClone()
     *
     * if clone for node2 is added correctly - it should be added to
     * all clones of node2 as well. Vice Versa, testCloneNode should
     * have node2 and all it's clones in the clones list
     */
    public void testMakeClone() {
        ontorama.model.graph.Node testCloneNode = node2.makeClone();
        assertEquals("number of clones for node2 and cloneNode2 should be the same", node2.getClones().size(), cloneNode2.getClones().size());
        assertEquals("number of clones for node2 and testCloneNode should be the same", node2.getClones().size(), testCloneNode.getClones().size());
        assertEquals("testCloneNode is within node2 clones", true, node2.getClones().contains(testCloneNode));
        assertEquals("testCloneNode is within cloneNode2 clones", true, cloneNode2.getClones().contains(testCloneNode));
        assertEquals("testCloneNode is within cloneNode3 clones", true, cloneNode3.getClones().contains(testCloneNode));
        assertEquals("node2 is within testCloneNode clones", true, testCloneNode.getClones().contains(node2));
        assertEquals("cloneNode2 is within testCloneNode clones", true, testCloneNode.getClones().contains(cloneNode2));
        assertEquals("cloneNode3 is within testCloneNode clones", true, testCloneNode.getClones().contains(cloneNode3));
    }

    /**
     * test method hasClones()
     */
    public void testHasClones() {
        assertEquals("node2 should have clones", true, node2.hasClones());
        assertEquals("node1 shouln't have any clones", false, node1.hasClones());
    }

    /**
     * test method setFoldedState
     */
    public void testSetFoldedState() {
        cloneNode2.setFoldState(true);
        cloneNode3.setFoldState(false);
        assertEquals("cloneNode2 should have folded state true", true, cloneNode2.getFoldedState());
        assertEquals("cloneNode3 should have folded state false", false, cloneNode3.getFoldedState());
    }

    /**
     * test method getFoldedState
     */
    public void testGetFoldedState() {
        assertEquals("node1 is folded, folded state should be true", true, node1.getFoldedState());
        assertEquals("node3 is unfolded, folded state should be false", false, node3.getFoldedState());
    }

    public void testGetCreator() {
        assertEquals("creatorUri for node1", creatorUri1, node1.getCreatorUri());
        assertEquals("creatorUri for node2", creatorUri2, node2.getCreatorUri());
    }

    public void testSetCreator() throws URISyntaxException {
        URI creatorUri = new URI("mailto:someone@ontorama.org");
        node3.setCreatorUri(creatorUri);
        assertEquals("creatorUri for node3", creatorUri, node3.getCreatorUri());
    }

    /**
     * check for given graph node name in the given iterator
     */
    public static boolean graphNodeNameIsInIterator(String name, Iterator it) {
        while (it.hasNext()) {
            ontorama.model.graph.Node cur = (ontorama.model.graph.Node) it.next();
            if (name.equals(cur.getName())) {
                return true;
            }
        }
        return false;
    }
}
