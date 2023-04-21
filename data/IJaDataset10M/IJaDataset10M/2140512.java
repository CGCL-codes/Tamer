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
import nz.ac.waikato.mcennis.rat.graph.query.property.NullPropertyQuery;
import nz.ac.waikato.mcennis.rat.graph.query.property.StringQuery;
import nz.ac.waikato.mcennis.rat.graph.query.property.StringQuery.Operation;

/**
 *
 * @author Daniel McEnnis
 */
public class ActorByPropertyTest extends TestCase {

    public ActorByPropertyTest(String testName) {
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
     * Test of execute method, of class ActorByProperty.
     */
    public void testNullExecute() {
        System.out.println("execute");
        Graph g = null;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        String propertyID = "Gender";
        boolean not = false;
        StringQuery query = new StringQuery();
        query.buildQuery("m", false, Operation.EQUALS_IGNORE_CASE);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(0, result.size());
        Iterator<Actor> it = result.iterator();
        assertNotNull(it);
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testEmptyExecute() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.getMusicGraph();
        Collection<Actor> actorList = new LinkedList<Actor>();
        Collection<Link> linkList = new LinkedList<Link>();
        String propertyID = "Gender";
        boolean not = false;
        StringQuery query = new StringQuery();
        query.buildQuery("m", false, Operation.EQUALS_IGNORE_CASE);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(0, result.size());
        Iterator<Actor> it = result.iterator();
        assertNotNull(it);
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testActorLimitExecute() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.getMusicGraph();
        Collection<Actor> actorList = new LinkedList<Actor>();
        actorList.add(QueryTestGraphs.A);
        actorList.add(QueryTestGraphs.A1);
        Collection<Link> linkList = null;
        String propertyID = "Gender";
        boolean not = false;
        StringQuery query = new StringQuery();
        query.buildQuery("m", false, Operation.EQUALS_IGNORE_CASE);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(1, result.size());
        Iterator<Actor> it = result.iterator();
        assertNotNull(it);
        assertEquals("A", it.next().getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testLinkLimitExecute() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.getMusicGraph();
        Collection<Actor> actorList = null;
        Collection<Link> linkList = new LinkedList<Link>();
        linkList.add(QueryTestGraphs.listensTo_aA1);
        linkList.add(QueryTestGraphs.listensTo_aB1);
        String propertyID = "Genre";
        boolean not = false;
        StringQuery query = new StringQuery();
        query.buildQuery(".*", false, Operation.MATCHES);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(4, result.size());
        Iterator<Actor> it = result.iterator();
        assertNotNull(it);
        assertEquals("A1", it.next().getID());
        assertEquals("A2", it.next().getID());
        assertEquals("C1", it.next().getID());
        assertEquals("C2", it.next().getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testBothLimitExecute() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.getMusicGraph();
        Collection<Actor> actorList = new LinkedList<Actor>();
        actorList.add(QueryTestGraphs.b);
        actorList.add(QueryTestGraphs.A1);
        Collection<Link> linkList = new LinkedList<Link>();
        linkList.add(QueryTestGraphs.listensTo_aA1);
        String propertyID = "Gender";
        boolean not = false;
        StringQuery query = new StringQuery();
        query.buildQuery("m", false, Operation.EQUALS_IGNORE_CASE);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(1, result.size());
        Iterator<Actor> it = result.iterator();
        assertNotNull(it);
        assertEquals("b", it.next().getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testNoLimitExecute() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.getMusicGraph();
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        String propertyID = "Genre";
        boolean not = false;
        StringQuery query = new StringQuery();
        query.buildQuery("rock", false, Operation.EQUALS_IGNORE_CASE);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(2, result.size());
        Iterator<Actor> it = result.iterator();
        assertNotNull(it);
        assertEquals("A1", it.next().getID());
        assertEquals("A2", it.next().getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testNullNegatedExecute() {
        System.out.println("execute");
        Graph g = null;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        String propertyID = "Gender";
        boolean not = true;
        StringQuery query = new StringQuery();
        query.buildQuery("m", false, Operation.EQUALS_IGNORE_CASE);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(0, result.size());
        Iterator<Actor> it = result.iterator();
        assertNotNull(it);
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testEmptyNegatedExecute() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.getMusicGraph();
        Collection<Actor> actorList = new LinkedList<Actor>();
        Collection<Link> linkList = new LinkedList<Link>();
        String propertyID = "Gender";
        boolean not = true;
        StringQuery query = new StringQuery();
        query.buildQuery("m", false, Operation.EQUALS_IGNORE_CASE);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(0, result.size());
        Iterator<Actor> it = result.iterator();
        assertNotNull(it);
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testActorLimitNegatedExecute() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.getMusicGraph();
        Collection<Actor> actorList = new LinkedList<Actor>();
        actorList.add(QueryTestGraphs.A);
        actorList.add(QueryTestGraphs.A1);
        Collection<Link> linkList = null;
        String propertyID = "Gender";
        boolean not = true;
        StringQuery query = new StringQuery();
        query.buildQuery("m", false, Operation.EQUALS_IGNORE_CASE);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(1, result.size());
        Iterator<Actor> it = result.iterator();
        assertNotNull(it);
        assertEquals("A1", it.next().getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testLinkLimitNegatedExecute() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.getMusicGraph();
        Collection<Actor> actorList = null;
        Collection<Link> linkList = new LinkedList<Link>();
        linkList.add(QueryTestGraphs.listensTo_aA1);
        linkList.add(QueryTestGraphs.listensTo_aB1);
        String propertyID = "Gender";
        boolean not = true;
        StringQuery query = new StringQuery();
        query.buildQuery(".*", false, Operation.MATCHES);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(6, result.size());
        Iterator<Actor> it = result.iterator();
        assertNotNull(it);
        assertEquals("A1", it.next().getID());
        assertEquals("A2", it.next().getID());
        assertEquals("B1", it.next().getID());
        assertEquals("B2", it.next().getID());
        assertEquals("C1", it.next().getID());
        assertEquals("C2", it.next().getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testBothLimitNegatedExecute() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.getMusicGraph();
        Collection<Actor> actorList = new LinkedList<Actor>();
        actorList.add(QueryTestGraphs.b);
        actorList.add(QueryTestGraphs.A1);
        Collection<Link> linkList = new LinkedList<Link>();
        linkList.add(QueryTestGraphs.listensTo_aA1);
        String propertyID = "Gender";
        boolean not = true;
        StringQuery query = new StringQuery();
        query.buildQuery(".*", false, Operation.MATCHES);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(6, result.size());
        Iterator<Actor> it = result.iterator();
        assertNotNull(it);
        assertEquals("A1", it.next().getID());
        assertEquals("A2", it.next().getID());
        assertEquals("B1", it.next().getID());
        assertEquals("B2", it.next().getID());
        assertEquals("C1", it.next().getID());
        assertEquals("C2", it.next().getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testNoLimitNegatedExecute() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.getMusicGraph();
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        String propertyID = "Gender";
        boolean not = true;
        StringQuery query = new StringQuery();
        query.buildQuery(".*", false, Operation.MATCHES);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Collection<Actor> result = instance.execute(g, actorList, linkList);
        assertNotNull(result);
        assertEquals(6, result.size());
        Iterator<Actor> it = result.iterator();
        assertNotNull(it);
        assertEquals("A1", it.next().getID());
        assertEquals("A2", it.next().getID());
        assertEquals("B1", it.next().getID());
        assertEquals("B2", it.next().getID());
        assertEquals("C1", it.next().getID());
        assertEquals("C2", it.next().getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testNullIterator() {
        System.out.println("execute");
        Graph g = null;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        String propertyID = "Gender";
        boolean not = false;
        StringQuery query = new StringQuery();
        query.buildQuery("m", false, Operation.EQUALS_IGNORE_CASE);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testEmptyIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.getMusicGraph();
        Collection<Actor> actorList = new LinkedList<Actor>();
        Collection<Link> linkList = new LinkedList<Link>();
        String propertyID = "Gender";
        boolean not = false;
        StringQuery query = new StringQuery();
        query.buildQuery("m", false, Operation.EQUALS_IGNORE_CASE);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testActorLimitIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.getMusicGraph();
        Collection<Actor> actorList = new LinkedList<Actor>();
        actorList.add(QueryTestGraphs.A);
        actorList.add(QueryTestGraphs.A1);
        Collection<Link> linkList = null;
        String propertyID = "Gender";
        boolean not = false;
        StringQuery query = new StringQuery();
        query.buildQuery("m", false, Operation.EQUALS_IGNORE_CASE);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        assertEquals("A", it.next().getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testLinkLimitIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.getMusicGraph();
        Collection<Actor> actorList = null;
        Collection<Link> linkList = new LinkedList<Link>();
        linkList.add(QueryTestGraphs.listensTo_aA1);
        linkList.add(QueryTestGraphs.listensTo_aB1);
        String propertyID = "Genre";
        boolean not = false;
        StringQuery query = new StringQuery();
        query.buildQuery(".*", false, Operation.MATCHES);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        assertEquals("A1", it.next().getID());
        assertEquals("A2", it.next().getID());
        assertEquals("C1", it.next().getID());
        assertEquals("C2", it.next().getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testBothLimitIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.getMusicGraph();
        Collection<Actor> actorList = new LinkedList<Actor>();
        actorList.add(QueryTestGraphs.b);
        actorList.add(QueryTestGraphs.A1);
        Collection<Link> linkList = new LinkedList<Link>();
        linkList.add(QueryTestGraphs.listensTo_aA1);
        String propertyID = "Gender";
        boolean not = false;
        StringQuery query = new StringQuery();
        query.buildQuery("m", false, Operation.EQUALS_IGNORE_CASE);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        assertEquals("b", it.next().getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testNoLimitIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.getMusicGraph();
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        String propertyID = "Genre";
        boolean not = false;
        StringQuery query = new StringQuery();
        query.buildQuery("rock", false, Operation.EQUALS_IGNORE_CASE);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        assertEquals("A1", it.next().getID());
        assertEquals("A2", it.next().getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testNullNegatedIterator() {
        System.out.println("execute");
        Graph g = null;
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        String propertyID = "Gender";
        boolean not = true;
        StringQuery query = new StringQuery();
        query.buildQuery("m", false, Operation.EQUALS_IGNORE_CASE);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testEmptyNegatedIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.getMusicGraph();
        Collection<Actor> actorList = new LinkedList<Actor>();
        Collection<Link> linkList = new LinkedList<Link>();
        String propertyID = "Gender";
        boolean not = true;
        StringQuery query = new StringQuery();
        query.buildQuery("m", false, Operation.EQUALS_IGNORE_CASE);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testActorLimitNegatedIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.getMusicGraph();
        Collection<Actor> actorList = new LinkedList<Actor>();
        actorList.add(QueryTestGraphs.A);
        actorList.add(QueryTestGraphs.A1);
        Collection<Link> linkList = null;
        String propertyID = "Gender";
        boolean not = true;
        StringQuery query = new StringQuery();
        query.buildQuery("m", false, Operation.EQUALS_IGNORE_CASE);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        assertEquals("A1", it.next().getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testLinkLimitNegatedIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.getMusicGraph();
        Collection<Actor> actorList = null;
        Collection<Link> linkList = new LinkedList<Link>();
        linkList.add(QueryTestGraphs.listensTo_aA1);
        linkList.add(QueryTestGraphs.listensTo_aB1);
        String propertyID = "Gender";
        boolean not = true;
        StringQuery query = new StringQuery();
        query.buildQuery(".*", false, Operation.MATCHES);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        assertEquals("A1", it.next().getID());
        assertEquals("A2", it.next().getID());
        assertEquals("B1", it.next().getID());
        assertEquals("B2", it.next().getID());
        assertEquals("C1", it.next().getID());
        assertEquals("C2", it.next().getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testBothLimitNegatedIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.getMusicGraph();
        Collection<Actor> actorList = new LinkedList<Actor>();
        actorList.add(QueryTestGraphs.b);
        actorList.add(QueryTestGraphs.A1);
        Collection<Link> linkList = new LinkedList<Link>();
        linkList.add(QueryTestGraphs.listensTo_aA1);
        String propertyID = "Gender";
        boolean not = true;
        StringQuery query = new StringQuery();
        query.buildQuery(".*", false, Operation.MATCHES);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        assertEquals("A1", it.next().getID());
        assertEquals("A2", it.next().getID());
        assertEquals("B1", it.next().getID());
        assertEquals("B2", it.next().getID());
        assertEquals("C1", it.next().getID());
        assertEquals("C2", it.next().getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of execute method, of class ActorByProperty.
     */
    public void testNoLimitNegatedIterator() {
        System.out.println("execute");
        Graph g = QueryTestGraphs.getMusicGraph();
        Collection<Actor> actorList = null;
        Collection<Link> linkList = null;
        String propertyID = "Gender";
        boolean not = true;
        StringQuery query = new StringQuery();
        query.buildQuery(".*", false, Operation.MATCHES);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
        Iterator<Actor> it = instance.executeIterator(g, actorList, linkList);
        assertNotNull(it);
        assertEquals("A1", it.next().getID());
        assertEquals("A2", it.next().getID());
        assertEquals("B1", it.next().getID());
        assertEquals("B2", it.next().getID());
        assertEquals("C1", it.next().getID());
        assertEquals("C2", it.next().getID());
        assertFalse(it.hasNext());
    }

    /**
     * Test of buildQuery method, of class ActorByProperty.
     */
    public void testBuildQuery() {
        System.out.println("buildQuery");
        String propertyID = "Value";
        boolean not = true;
        NullPropertyQuery query = new NullPropertyQuery();
        query.buildQuery(true);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
    }

    /**
     * Test of buildQuery method, of class ActorByProperty.
     */
    public void testNullPropertyBuildQuery() {
        System.out.println("buildQuery");
        String propertyID = null;
        boolean not = true;
        NullPropertyQuery query = new NullPropertyQuery();
        query.buildQuery(true);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
    }

    /**
     * Test of buildQuery method, of class ActorByProperty.
     */
    public void testTrueBuildQuery() {
        System.out.println("buildQuery");
        String propertyID = "Value";
        boolean not = true;
        NullPropertyQuery query = new NullPropertyQuery();
        query.buildQuery(true);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
    }

    /**
     * Test of buildQuery method, of class ActorByProperty.
     */
    public void testNullQueryBuildQuery() {
        System.out.println("buildQuery");
        String propertyID = "Value";
        boolean not = true;
        NullPropertyQuery query = null;
        query.buildQuery(true);
        ActorByProperty instance = new ActorByProperty();
        instance.buildQuery(propertyID, not, query);
    }

    /**
     * Test of exportQuery method, of class ActorByProperty.
     */
    public void testExportQuery() throws Exception {
        System.out.println("exportQuery");
        Writer writer = new OutputStreamWriter(new ByteArrayOutputStream());
        ActorByProperty instance = new ActorByProperty();
        instance.exportQuery(writer);
    }

    /**
     * Test of buildingStatus method, of class ActorByProperty.
     */
    public void testBuildingStatus() {
        System.out.println("buildingStatus");
        ActorByProperty instance = new ActorByProperty();
        assertEquals(State.UNINITIALIZED, instance.buildingStatus());
        instance.buildQuery("", false, new NullPropertyQuery());
        assertEquals(State.READY, instance.buildingStatus());
    }

    /**
     * Test of prototype method, of class ActorByProperty.
     */
    public void testPrototype() {
        System.out.println("prototype");
        ActorByProperty instance = new ActorByProperty();
        ActorByProperty result = instance.prototype();
        assertNotSame(instance, result);
    }
}
