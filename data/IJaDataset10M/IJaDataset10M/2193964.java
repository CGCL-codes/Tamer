package nz.ac.waikato.mcennis.rat.graph.query.actor;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.query.QueryTestGraphs;

/**
 *
 * @author Daniel McEnnis
 */
public class ActorByModeTest extends TestCase {

    public ActorByModeTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testNullExecute() {
        System.out.println("execute");
        Graph g = null;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(".*", ".*", false);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testActorLimitExecutePositive() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.subgraph1;
        Collection<Actor> actorList = new LinkedList<Actor>();
        actorList.add(QueryTestGraphs.a);
        actorList.add(QueryTestGraphs.d);
        Collection<Link> linkList = null;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(".*", ".*", false);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(1, result.size());
        Iterator<Actor> it = result.iterator();
        Actor a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("a", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testLinkLimitExecutePositive() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.musicGraph;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = new LinkedList<Link>();
        linkList.add(QueryTestGraphs.listensTo_aA);
        ActorByMode instance = new ActorByMode();
        instance.buildQuery("Artist", ".*", false);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(4, result.size());
        Iterator<Actor> it = result.iterator();
        Actor a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("A", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("B", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("C", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("D", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testActorLinkLimitExecutePositive() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.subgraph1;
        Collection<Actor> actorList = new LinkedList<Actor>();
        actorList.add(QueryTestGraphs.A);
        actorList.add(QueryTestGraphs.C);
        Collection<Link> linkList = new LinkedList<Link>();
        linkList.add(QueryTestGraphs.listensTo_aA);
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(".*", ".*", false);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(1, result.size());
        Iterator<Actor> it = result.iterator();
        Actor a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("A", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testActorLimitExecuteNegative() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.subgraph1;
        Collection<Actor> actorList = new LinkedList<Actor>();
        actorList.add(QueryTestGraphs.a);
        actorList.add(QueryTestGraphs.d);
        Collection<Link> linkList = null;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery("[^a-zA-Z].*", "[^a-zA-Z].*", true);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(1, result.size());
        Iterator<Actor> it = result.iterator();
        Actor a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("a", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testLinkLimitExecuteNegative() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.musicGraph;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = new LinkedList<Link>();
        linkList.add(QueryTestGraphs.listensTo_aA);
        ActorByMode instance = new ActorByMode();
        instance.buildQuery("[UT].*", ".*", true);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(4, result.size());
        Iterator<Actor> it = result.iterator();
        Actor a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("A", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("B", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("C", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("D", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testActorLinkLimitExecuteNegative() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.subgraph1;
        Collection<Actor> actorList = new LinkedList<Actor>();
        actorList.add(QueryTestGraphs.A);
        actorList.add(QueryTestGraphs.C);
        Collection<Link> linkList = new LinkedList<Link>();
        linkList.add(QueryTestGraphs.listensTo_aA);
        ActorByMode instance = new ActorByMode();
        instance.buildQuery("[^a-zA-Z].*", "[^a-zA-Z].*", true);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(1, result.size());
        Iterator<Actor> it = result.iterator();
        Actor a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("A", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testSimpleSimplePositiveExecute() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.musicGraph;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery("User", "a", false);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(1, result.size());
        Iterator<Actor> it = result.iterator();
        Actor a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("a", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testSimpleRegExpPositiveExecute() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.musicGraph;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery("Artist", ".*", false);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(4, result.size());
        Iterator<Actor> it = result.iterator();
        Actor a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("A", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("B", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("C", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("D", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testRegExpRegExpPositiveExecute() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.musicGraph;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery("A.*", "[BC]", false);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(2, result.size());
        Iterator<Actor> it = result.iterator();
        Actor a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("B", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("C", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testSimpleSimpleNegativeExecute() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.subgraph1;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery("Track", "A1", true);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(13, result.size());
        Iterator<Actor> it = result.iterator();
        Actor a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("A", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("B", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("C", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("D", a.getID());
        a = it.next();
        assertEquals("Track", a.getMode());
        assertEquals("A2", a.getID());
        a = it.next();
        assertEquals("Track", a.getMode());
        assertEquals("B1", a.getID());
        a = it.next();
        assertEquals("Track", a.getMode());
        assertEquals("B2", a.getID());
        a = it.next();
        assertEquals("Track", a.getMode());
        assertEquals("C1", a.getID());
        a = it.next();
        assertEquals("Track", a.getMode());
        assertEquals("C2", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("a", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("b", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("c", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("d", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testSimpleRegExpNegativeExecute() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.musicGraph;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery("Track", ".*", true);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(8, result.size());
        Iterator<Actor> it = result.iterator();
        Actor a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("A", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("B", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("C", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("D", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("a", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("b", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("c", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("d", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testRegExpRegExpNegativeExecute() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.musicGraph;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(".*", "[ABC].*", true);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(5, result.size());
        Iterator<Actor> it = result.iterator();
        Actor a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("D", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("a", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("b", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("c", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("d", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testNullExecuteIterator() {
        System.out.println("execute");
        Graph g = null;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(".*", ".*", false);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testActorLimitExecutePositiveIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.subgraph1;
        Collection<Actor> actorList = new LinkedList<Actor>();
        actorList.add(QueryTestGraphs.a);
        actorList.add(QueryTestGraphs.d);
        Collection<Link> linkList = null;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(".*", ".*", false);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        Actor a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("a", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testLinkLimitExecutePositiveIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.musicGraph;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = new LinkedList<Link>();
        linkList.add(QueryTestGraphs.listensTo_aA);
        ActorByMode instance = new ActorByMode();
        instance.buildQuery("Artist", ".*", false);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        Actor a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("A", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("B", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("C", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("D", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testActorLinkLimitExecutePositiveIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.subgraph1;
        Collection<Actor> actorList = new LinkedList<Actor>();
        actorList.add(QueryTestGraphs.A);
        actorList.add(QueryTestGraphs.C);
        Collection<Link> linkList = new LinkedList<Link>();
        linkList.add(QueryTestGraphs.listensTo_aA);
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(".*", ".*", false);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        Actor a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("A", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testActorLimitExecuteNegativeIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.subgraph1;
        Collection<Actor> actorList = new LinkedList<Actor>();
        actorList.add(QueryTestGraphs.a);
        actorList.add(QueryTestGraphs.d);
        Collection<Link> linkList = null;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery("[^a-zA-Z].*", "[^a-zA-Z].*", true);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        Actor a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("a", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testLinkLimitExecuteNegativeIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.musicGraph;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = new LinkedList<Link>();
        linkList.add(QueryTestGraphs.listensTo_aA);
        ActorByMode instance = new ActorByMode();
        instance.buildQuery("[UT].*", ".*", true);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        Actor a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("A", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("B", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("C", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("D", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testActorLinkLimitExecuteNegativeIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.subgraph1;
        Collection<Actor> actorList = new LinkedList<Actor>();
        actorList.add(QueryTestGraphs.A);
        actorList.add(QueryTestGraphs.C);
        Collection<Link> linkList = new LinkedList<Link>();
        linkList.add(QueryTestGraphs.listensTo_aA);
        ActorByMode instance = new ActorByMode();
        instance.buildQuery("[^a-zA-Z].*", "[^a-zA-Z].*", true);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        Actor a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("A", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testSimpleSimplePositiveExecuteIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.musicGraph;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery("User", "a", false);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        Actor a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("a", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testSimpleRegExpPositiveExecuteIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.musicGraph;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery("Artist", ".*", false);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        Actor a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("A", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("B", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("C", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("D", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testRegExpRegExpPositiveExecuteIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.musicGraph;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery("A.*", "[BC]", false);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        Actor a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("B", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("C", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testSimpleSimpleNegativeExecuteIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.subgraph1;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery("Track", "A1", true);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        Actor a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("A", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("B", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("C", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("D", a.getID());
        a = it.next();
        assertEquals("Track", a.getMode());
        assertEquals("A2", a.getID());
        a = it.next();
        assertEquals("Track", a.getMode());
        assertEquals("B1", a.getID());
        a = it.next();
        assertEquals("Track", a.getMode());
        assertEquals("B2", a.getID());
        a = it.next();
        assertEquals("Track", a.getMode());
        assertEquals("C1", a.getID());
        a = it.next();
        assertEquals("Track", a.getMode());
        assertEquals("C2", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("a", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("b", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("c", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("d", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testSimpleRegExpNegativeExecuteIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.musicGraph;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery("Track", ".*", true);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        Actor a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("A", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("B", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("C", a.getID());
        a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("D", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("a", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("b", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("c", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("d", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByMode.
     */
    public void testRegExpRegExpNegativeExecuteIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.musicGraph;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(".*", "[ABC].*", true);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        Actor a = it.next();
        assertEquals("Artist", a.getMode());
        assertEquals("D", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("a", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("b", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("c", a.getID());
        a = it.next();
        assertEquals("User", a.getMode());
        assertEquals("d", a.getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testNNFBuildQuery() {
        System.out.println("buildQuery");
        String mode = null;
        String id = null;
        boolean not = false;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testNNTBuildQuery() {
        System.out.println("buildQuery");
        String mode = null;
        String id = null;
        boolean not = true;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testNBFBuildQuery() {
        System.out.println("buildQuery");
        String mode = null;
        String id = "[";
        boolean not = false;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testNBTBuildQuery() {
        System.out.println("buildQuery");
        String mode = null;
        String id = "[";
        boolean not = true;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testNSFBuildQuery() {
        System.out.println("buildQuery");
        String mode = null;
        String id = "Simple";
        boolean not = false;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testNSTBuildQuery() {
        System.out.println("buildQuery");
        String mode = null;
        String id = "Simple";
        boolean not = true;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testNRFBuildQuery() {
        System.out.println("buildQuery");
        String mode = null;
        String id = ".*";
        boolean not = false;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testNRTBuildQuery() {
        System.out.println("buildQuery");
        String mode = null;
        String id = ".*";
        boolean not = true;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testSNFBuildQuery() {
        System.out.println("buildQuery");
        String mode = "Simple";
        String id = null;
        boolean not = false;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testSNTBuildQuery() {
        System.out.println("buildQuery");
        String mode = "Simple";
        String id = ".*";
        boolean not = true;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testSSFBuildQuery() {
        System.out.println("buildQuery");
        String mode = "Simple";
        String id = "Simple";
        boolean not = false;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testSSTBuildQuery() {
        System.out.println("buildQuery");
        String mode = "Simple";
        String id = "Simple";
        boolean not = true;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testSBFBuildQuery() {
        System.out.println("buildQuery");
        String mode = "Simple";
        String id = "[";
        boolean not = false;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testSBTBuildQuery() {
        System.out.println("buildQuery");
        String mode = "Simple";
        String id = "[";
        boolean not = true;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testSRFBuildQuery() {
        System.out.println("buildQuery");
        String mode = "Simple";
        String id = ".*";
        boolean not = true;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testSRTBuildQuery() {
        System.out.println("buildQuery");
        String mode = "Simple";
        String id = ".*";
        boolean not = true;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testBNFBuildQuery() {
        System.out.println("buildQuery");
        String mode = "[";
        String id = null;
        boolean not = false;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testBNTBuildQuery() {
        System.out.println("buildQuery");
        String mode = "[";
        String id = null;
        boolean not = true;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testBSFBuildQuery() {
        System.out.println("buildQuery");
        String mode = "[";
        String id = "Simple";
        boolean not = false;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testBSTBuildQuery() {
        System.out.println("buildQuery");
        String mode = "[";
        String id = "Simple";
        boolean not = true;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testBRFBuildQuery() {
        System.out.println("buildQuery");
        String mode = "[";
        String id = ".*";
        boolean not = false;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testBRTBuildQuery() {
        System.out.println("buildQuery");
        String mode = "[";
        String id = ".*";
        boolean not = true;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testRNFBuildQuery() {
        System.out.println("buildQuery");
        String mode = ".*";
        String id = null;
        boolean not = false;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testRNTBuildQuery() {
        System.out.println("buildQuery");
        String mode = ".*";
        String id = null;
        boolean not = true;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testRSFBuildQuery() {
        System.out.println("buildQuery");
        String mode = ".*";
        String id = "Simple";
        boolean not = false;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testRSTBuildQuery() {
        System.out.println("buildQuery");
        String mode = ".*";
        String id = "Simple";
        boolean not = true;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testRBFBuildQuery() {
        System.out.println("buildQuery");
        String mode = ".*";
        String id = "[";
        boolean not = false;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testRBTBuildQuery() {
        System.out.println("buildQuery");
        String mode = ".*";
        String id = "[";
        boolean not = true;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testRRFBuildQuery() {
        System.out.println("buildQuery");
        String mode = ".*";
        String id = ".*";
        boolean not = false;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of buildQuery method, of class ActorByMode.
     */
    public void testRRTBuildQuery() {
        System.out.println("buildQuery");
        String mode = ".*";
        String id = ".*";
        boolean not = true;
        ActorByMode instance = new ActorByMode();
        instance.buildQuery(mode, id, not);
    }

    /**
     * Test of exportQuery method, of class ActorByMode.
     */
    public void testExportQuery() throws Exception {
        System.out.println("exportQuery");
        Writer writer = new OutputStreamWriter(new ByteArrayOutputStream());
        ActorByMode instance = new ActorByMode();
        instance.exportQuery(writer);
    }

    /**
     * Test of buildingStatus method, of class ActorByMode.
     */
    public void testBuildingStatus() {
        System.out.println("buildingStatus");
        ActorByMode instance = new ActorByMode();
        assertEquals(State.UNINITIALIZED, instance.buildingStatus());
        instance.buildQuery("", "", false);
        assertEquals(State.READY, instance.buildingStatus());
    }

    /**
     * Test of prototype method, of class ActorByMode.
     */
    public void testPrototype() {
        System.out.println("prototype");
        ActorByMode instance = new ActorByMode();
        ActorByMode result = instance.prototype();
        assertNotSame(instance, result);
    }
}
