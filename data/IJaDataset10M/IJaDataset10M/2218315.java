package nz.ac.waikato.mcennis.rat.parser.xmlHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import junit.framework.*;
import nz.ac.waikato.mcennis.rat.graph.MemGraph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;
import nz.ac.waikato.mcennis.rat.parser.XMLParser;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesFactory;

/**


 *


 * @author Daniel McEnnis


 */
public class GraphReaderTest extends TestCase {

    Actor a;

    Actor b;

    Actor c;

    Actor d;

    Actor e;

    Actor art1;

    Actor art3;

    Actor art5;

    Actor art7;

    Link ab;

    Link ba;

    Link bc;

    Link cb;

    Link ac;

    Link ca;

    Link ae;

    Link bd;

    Link dc;

    Link a1;

    Link b1;

    Link b3;

    Link c3;

    Link c5;

    Link d7;

    MemGraph test;

    MemGraph child1;

    MemGraph child2;

    MemGraph child11;

    byte[] savedGraph;

    String[] referenceOutput;

    String allOutput;

    public GraphReaderTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        test = new MemGraph();
        test.setID("test");
        nz.ac.waikato.mcennis.rat.graph.descriptors.Properties props = PropertiesFactory.newInstance().create();
        props.set("PropertyID", "graphProperty");
        props.set("PropertyValueClass", java.lang.Double.class);
        props.set("PropertyType", "Basic");
        Property graphProp = PropertyFactory.newInstance().create(props);
        graphProp.add(new Double(1.0));
        graphProp.add(new Double(2.0));
        test.add(graphProp);
        props.set("Relation", "Interest");
        props.set("ActorMode", "User");
        props.set("ActorID", "A");
        a = ActorFactory.newInstance().create(props);
        props.set("PropertyValueClass", java.lang.String.class);
        props.set("PropertyID", "interest");
        Property ia = PropertyFactory.newInstance().create(props);
        ia.add("1");
        ia.add("2");
        a.add(ia);
        test.add(a);
        props.set("ActorMode", "User");
        props.set("ActorID", "B");
        b = ActorFactory.newInstance().create(props);
        Property ib = PropertyFactory.newInstance().create(props);
        ib.add("1");
        ib.add("2");
        ib.add("3");
        ib.add("4");
        b.add(ib);
        test.add(b);
        props.set("ActorID", "C");
        c = ActorFactory.newInstance().create(props);
        Property ic = PropertyFactory.newInstance().create(props);
        ic.add("3");
        ic.add("4");
        ic.add("5");
        ic.add("6");
        c.add(ic);
        test.add(c);
        props.set("ActorID", "D");
        d = ActorFactory.newInstance().create(props);
        Property id = PropertyFactory.newInstance().create(props);
        id.add("7");
        id.add("8");
        d.add(id);
        test.add(d);
        props.set("ActorID", "E");
        e = ActorFactory.newInstance().create(props);
        test.add(e);
        props.set("Relation", "Knows");
        ab = LinkFactory.newInstance().create(props);
        ab.set(a, 1.0, b);
        test.add(ab);
        ba = LinkFactory.newInstance().create(props);
        ba.set(b, 1.0, a);
        test.add(ba);
        cb = LinkFactory.newInstance().create(props);
        cb.set(c, 1.0, b);
        test.add(cb);
        bc = LinkFactory.newInstance().create(props);
        bc.set(b, 1.0, c);
        test.add(bc);
        ca = LinkFactory.newInstance().create(props);
        ca.set(c, 1.0, a);
        test.add(ca);
        ac = LinkFactory.newInstance().create(props);
        ac.set(a, 1.0, c);
        test.add(ac);
        ae = LinkFactory.newInstance().create(props);
        ae.set(a, 1.0, e);
        test.add(ae);
        bd = LinkFactory.newInstance().create(props);
        bd.set(b, 1.0, d);
        test.add(bd);
        dc = LinkFactory.newInstance().create(props);
        dc.set(d, 1.0, c);
        test.add(dc);
        props.set("ActorMode", "Artist");
        props.set("ActorID", "1");
        art1 = ActorFactory.newInstance().create(props);
        test.add(art1);
        props.set("ActorID", "3");
        art3 = ActorFactory.newInstance().create(props);
        test.add(art3);
        props.set("ActorID", "5");
        art5 = ActorFactory.newInstance().create(props);
        test.add(art5);
        props.set("ActorID", "7");
        art7 = ActorFactory.newInstance().create(props);
        test.add(art7);
        props.set("Relation", "Given");
        a1 = LinkFactory.newInstance().create(props);
        a1.set(a, 1.79769E+308, art1);
        test.add(a1);
        b1 = LinkFactory.newInstance().create(props);
        b1.set(b, 1.79769E+308, art1);
        test.add(b1);
        b3 = LinkFactory.newInstance().create(props);
        b3.set(b, 1.79769E+308, art3);
        test.add(b3);
        c3 = LinkFactory.newInstance().create(props);
        c3.set(c, 1.79769E+308, art3);
        props.set("PropertyID", "DummyProperty");
        props.set("PropertyValueClass", java.lang.Double.class);
        Property linkProp = PropertyFactory.newInstance().create(props);
        linkProp.add(new Double(1.0));
        linkProp.add(new Double(3.0));
        c3.add(linkProp);
        test.add(c3);
        c5 = LinkFactory.newInstance().create(props);
        c5.set(c, 1.79769E+308, art5);
        test.add(c5);
        d7 = LinkFactory.newInstance().create(props);
        d7.set(d, 1.79769E+308, art7);
        test.add(d7);
        child1 = new MemGraph();
        child1.setID("child1");
        child1.add(a);
        child1.add(b);
        child1.add(art1);
        child1.add(art3);
        child1.add(a1);
        child1.add(b1);
        child1.add(b3);
        child1.add(ab);
        test.addChild(child1);
        child11 = new MemGraph();
        child11.setID("child11");
        child11.add(a);
        child11.add(b);
        child11.add(ab);
        child1.addChild(child11);
        child2 = new MemGraph();
        child2.setID("child2");
        child2.add(d);
        child2.add(art7);
        child2.add(d7);
        props = PropertiesFactory.newInstance().create();
        props.set("PropertyID", "graphProperty");
        props.set("PropertyValueClass", java.lang.Double.class);
        props.set("PropertyType", "Basic");
        graphProp = PropertyFactory.newInstance().create(props);
        graphProp.add(new Double(1.0));
        graphProp.add(new Double(2.0));
        child2.add(graphProp);
        test.addChild(child2);
        ByteArrayOutputStream outputContent = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputContent);
        test.outputXML(writer, false);
        writer.flush();
        savedGraph = outputContent.toByteArray();
        allOutput = outputContent.toString();
        referenceOutput = (allOutput).split("\r*\n+");
    }

    protected void tearDown() throws Exception {
    }

    /**


     * Test of startElement method, of class nz.ac.waikato.mcennis.arm.parser.xmlHandler.GraphReader.


     */
    public void testLoadGraph() throws Exception {
        System.out.println("test base graph");
        ByteArrayInputStream reader = new ByteArrayInputStream(savedGraph);
        MemGraph graph = new MemGraph();
        GraphReader handler = new GraphReader();
        handler.setGraph(graph);
        XMLParser parser = new XMLParser();
        parser.setHandler(handler);
        parser.parse(reader, "");
        System.out.println("Evaluating Actors of graph test");
        assertEquals("test", graph.getID());
        List<Actor> actors = graph.getActor();
        assertNotNull(actors);
        assertEquals(9, actors.size());
        for (int i = 0; i < actors.size(); ++i) {
            if (actors.get(i).getID().equals("A")) {
                evaluateA(actors.get(i));
            } else if (actors.get(i).getID().equals("B")) {
                evaluateB(actors.get(i));
            } else if (actors.get(i).getID().equals("C")) {
                evaluateC(actors.get(i));
            } else if (actors.get(i).getID().equals("D")) {
                evaluateD(actors.get(i));
            } else if (actors.get(i).getID().equals("E")) {
                evaluateE(actors.get(i));
            } else if (actors.get(i).getID().equals("1")) {
                evaluate1(actors.get(i));
            } else if (actors.get(i).getID().equals("3")) {
                evaluate3(actors.get(i));
            } else if (actors.get(i).getID().equals("5")) {
                evaluate5(actors.get(i));
            } else if (actors.get(i).getID().equals("7")) {
                evaluate7(actors.get(i));
            }
        }
        System.out.println("Evaluating Links of Graph test");
        List<Link> links = graph.getLink();
        assertNotNull(links);
        assertEquals(15, links.size());
        for (int i = 0; i < links.size(); ++i) {
            if (links.get(i).getSource().equals(a) && links.get(i).getDestination().equals(b)) {
                evaluateAB(links.get(i));
            } else if (links.get(i).getSource().equals(a) && links.get(i).getDestination().equals(c)) {
                evaluateAC(links.get(i));
            } else if (links.get(i).getSource().equals(b) && links.get(i).getDestination().equals(a)) {
                evaluateBA(links.get(i));
            } else if (links.get(i).getSource().equals(b) && links.get(i).getDestination().equals(c)) {
                evaluateBC(links.get(i));
            } else if (links.get(i).getSource().equals(a) && links.get(i).getDestination().equals(e)) {
                evaluateAE(links.get(i));
            } else if (links.get(i).getSource().equals(b) && links.get(i).getDestination().equals(d)) {
                evaluateBD(links.get(i));
            } else if (links.get(i).getSource().equals(c) && links.get(i).getDestination().equals(a)) {
                evaluateCA(links.get(i));
            } else if (links.get(i).getSource().equals(c) && links.get(i).getDestination().equals(b)) {
                evaluateCB(links.get(i));
            } else if (links.get(i).getSource().equals(d) && links.get(i).getDestination().equals(c)) {
                evaluateDC(links.get(i));
            } else if (links.get(i).getSource().equals(a) && links.get(i).getDestination().equals(art1)) {
                evaluateA1(links.get(i));
            } else if (links.get(i).getSource().equals(b) && links.get(i).getDestination().equals(art1)) {
                evaluateB1(links.get(i));
            } else if (links.get(i).getSource().equals(b) && links.get(i).getDestination().equals(art3)) {
                evaluateB3(links.get(i));
            } else if (links.get(i).getSource().equals(c) && links.get(i).getDestination().equals(art3)) {
                evaluateC3(links.get(i));
            } else if (links.get(i).getSource().equals(c) && links.get(i).getDestination().equals(art5)) {
                evaluateC5(links.get(i));
            } else if (links.get(i).getSource().equals(d) && links.get(i).getDestination().equals(art7)) {
                evaluateD7(links.get(i));
            } else {
                fail("Unexpected Link with source " + links.get(i).getSource().getID() + " and destination of " + links.get(i).getDestination().getID());
            }
        }
        System.out.println("Evaluating Children");
        List<Graph> child = graph.getChildren();
        assertNotNull(child);
        assertEquals(2, child.size());
    }

    public void testLoadGraphChild1() throws Exception {
        System.out.println("Test child1");
        ByteArrayInputStream reader = new ByteArrayInputStream(savedGraph);
        MemGraph graph = new MemGraph();
        GraphReader handler = new GraphReader();
        handler.setGraph(graph);
        XMLParser parser = new XMLParser();
        parser.setHandler(handler);
        parser.parse(reader, "");
        Graph testChild1 = graph.getChildren("child1");
        assertNotNull(testChild1);
        List<Actor> actors = testChild1.getActor();
        assertNotNull(actors);
        assertEquals(4, actors.size());
        for (int i = 0; i < actors.size(); ++i) {
            if (actors.get(i).getID().equals("A")) {
                evaluateA(actors.get(i));
            } else if (actors.get(i).getID().equals("B")) {
                evaluateB(actors.get(i));
            } else if (actors.get(i).getID().equals("1")) {
                evaluate1(actors.get(i));
            } else if (actors.get(i).getID().equals("3")) {
                evaluate3(actors.get(i));
            } else {
                fail("Unexpected actor " + actors.get(i).getID() + " found");
            }
        }
        List<Link> links = testChild1.getLink();
        assertNotNull(links);
        assertEquals(4, links.size());
        for (int i = 0; i < links.size(); ++i) {
            if (links.get(i).getSource().equals(a) && links.get(i).getDestination().equals(b)) {
                evaluateAB(links.get(i));
            } else if (links.get(i).getSource().equals(a) && links.get(i).getDestination().equals(art1)) {
                evaluateA1(links.get(i));
            } else if (links.get(i).getSource().equals(b) && links.get(i).getDestination().equals(art1)) {
                evaluateB1(links.get(i));
            } else if (links.get(i).getSource().equals(b) && links.get(i).getDestination().equals(art3)) {
                evaluateB3(links.get(i));
            } else {
                fail("Unexpected link detected source " + links.get(i).getSource().getID() + " and dest " + links.get(i).getDestination().getID());
            }
        }
    }

    public void testLoadGraphChild11() throws Exception {
        System.out.println("Test child11");
        ByteArrayInputStream reader = new ByteArrayInputStream(savedGraph);
        MemGraph graph = new MemGraph();
        GraphReader handler = new GraphReader();
        handler.setGraph(graph);
        XMLParser parser = new XMLParser();
        parser.setHandler(handler);
        parser.parse(reader, "");
        Graph testChild1 = graph.getChildren("child1");
        assertNotNull(testChild1);
        Graph testChild11 = testChild1.getChildren("child11");
        assertNotNull(testChild11);
        List<Actor> actors = testChild11.getActor();
        assertNotNull(actors);
        assertEquals(2, actors.size());
        for (int i = 0; i < actors.size(); ++i) {
            if (actors.get(i).equals(a)) {
                evaluateA(actors.get(i));
            } else if (actors.get(i).equals(b)) {
                evaluateB(actors.get(i));
            } else {
                fail("Unexpected actor " + actors.get(i).getID());
            }
        }
        List<Link> links = testChild11.getLink();
        assertNotNull(links);
        assertEquals(1, links.size());
        assertTrue(links.get(0).getSource().equals(a));
        assertTrue(links.get(0).getDestination().equals(b));
        evaluateAB(links.get(0));
    }

    public void testLoadGraphChild2() throws Exception {
        System.out.println("Test child2");
        ByteArrayInputStream reader = new ByteArrayInputStream(savedGraph);
        MemGraph graph = new MemGraph();
        GraphReader handler = new GraphReader();
        handler.setGraph(graph);
        XMLParser parser = new XMLParser();
        parser.setHandler(handler);
        parser.parse(reader, "");
        Graph testChild2 = graph.getChildren("child2");
        assertNotNull(testChild2);
        List<Actor> actors = testChild2.getActor();
        assertNotNull(actors);
        assertEquals(2, actors.size());
        for (int i = 0; i < actors.size(); ++i) {
            if (actors.get(i).equals(d)) {
                evaluateD(actors.get(i));
            } else if (actors.get(i).equals(art7)) {
                evaluate7(actors.get(i));
            } else {
                fail("Unexpected actor " + actors.get(i).getID());
            }
        }
        List<Link> links = testChild2.getLink();
        assertNotNull(links);
        assertEquals(1, links.size());
        assertTrue(links.get(0).getSource().equals(d));
        assertTrue(links.get(0).getDestination().equals(art7));
        evaluateD7(links.get(0));
    }

    private void evaluateA(Actor actor) {
        assertEquals("User", actor.getMode());
        assertEquals("A", actor.getID());
        List<Property> props = actor.getProperty();
        assertNotNull(props);
        assertEquals(1, props.size());
        assertNotNull(props.get(0));
        assertEquals("interest", props.get(0).getType());
        assertEquals(java.lang.String.class, props.get(0).getPropertyClass());
        List<Object> values = props.get(0).getValue();
        assertNotNull(values);
        assertEquals(2, values.size());
        for (int i = 0; i < props.size(); ++i) {
            if (values.get(i).equals("1")) {
            } else if (values.get(i).equals("2")) {
            } else {
                fail("Unexpected property value " + values.get(i).toString());
            }
        }
    }

    private void evaluateB(Actor actor) {
        assertEquals("User", actor.getMode());
        assertEquals("B", actor.getID());
        List<Property> props = actor.getProperty();
        assertNotNull(props);
        assertEquals(1, props.size());
        assertNotNull(props.get(0));
        assertEquals("interest", props.get(0).getType());
        assertEquals(java.lang.String.class, props.get(0).getPropertyClass());
        List<Object> values = props.get(0).getValue();
        assertNotNull(values);
        assertEquals(4, values.size());
        for (int i = 0; i < props.size(); ++i) {
            if (values.get(i).equals("1")) {
            } else if (values.get(i).equals("2")) {
            } else if (values.get(i).equals("3")) {
            } else if (values.get(i).equals("4")) {
            } else {
                fail("Unexpected property value " + values.get(i).toString());
            }
        }
    }

    private void evaluateC(Actor actor) {
        assertEquals("User", actor.getMode());
        assertEquals("C", actor.getID());
        List<Property> props = actor.getProperty();
        assertNotNull(props);
        assertEquals(1, props.size());
        assertNotNull(props.get(0));
        assertEquals("interest", props.get(0).getType());
        assertEquals(java.lang.String.class, props.get(0).getPropertyClass());
        List<Object> values = props.get(0).getValue();
        assertNotNull(values);
        assertEquals(4, values.size());
        for (int i = 0; i < props.size(); ++i) {
            if (values.get(i).equals("3")) {
            } else if (values.get(i).equals("4")) {
            } else if (values.get(i).equals("5")) {
            } else if (values.get(i).equals("6")) {
            } else {
                fail("Unexpected property value " + values.get(i).toString());
            }
        }
    }

    private void evaluateD(Actor actor) {
        assertEquals("User", actor.getMode());
        assertEquals("D", actor.getID());
        List<Property> props = actor.getProperty();
        assertNotNull(props);
        assertEquals(1, props.size());
        assertNotNull(props.get(0));
        assertEquals("interest", props.get(0).getType());
        assertEquals(java.lang.String.class, props.get(0).getPropertyClass());
        List<Object> values = props.get(0).getValue();
        assertNotNull(values);
        assertEquals(2, values.size());
        for (int i = 0; i < props.size(); ++i) {
            if (values.get(i).equals("6")) {
            } else if (values.get(i).equals("7")) {
            } else {
                fail("Unexpected property value " + values.get(i).toString());
            }
        }
    }

    private void evaluate1(Actor actor) {
        assertEquals("Artist", actor.getMode());
        assertEquals("1", actor.getID());
        List<Property> props = actor.getProperty();
        assertNull(props);
    }

    private void evaluate3(Actor actor) {
        assertEquals("Artist", actor.getMode());
        assertEquals("3", actor.getID());
        List<Property> props = actor.getProperty();
        assertNull(props);
    }

    private void evaluate5(Actor actor) {
        assertEquals("Artist", actor.getMode());
        assertEquals("5", actor.getID());
        List<Property> props = actor.getProperty();
        assertNull(props);
    }

    private void evaluate7(Actor actor) {
        assertEquals("Artist", actor.getMode());
        assertEquals("7", actor.getID());
        List<Property> props = actor.getProperty();
        assertNull(props);
    }

    private void evaluateE(Actor actor) {
        assertEquals("User", actor.getMode());
        assertEquals("E", actor.getID());
        List<Property> props = actor.getProperty();
        assertNull(props);
    }

    private void evaluateAB(Link l) {
        assertEquals("Knows", l.getRelation());
        assertEquals(a, l.getSource());
        assertEquals(b, l.getDestination());
        assertEquals(ab.getStrength(), l.getStrength(), 0.0001);
    }

    private void evaluateBA(Link l) {
        assertEquals("Knows", l.getRelation());
        assertEquals(b, l.getSource());
        assertEquals(a, l.getDestination());
        assertEquals(ba.getStrength(), l.getStrength(), 0.0001);
    }

    private void evaluateAC(Link l) {
        assertEquals("Knows", l.getRelation());
        assertEquals(a, l.getSource());
        assertEquals(c, l.getDestination());
        assertEquals(ac.getStrength(), l.getStrength(), 0.0001);
    }

    private void evaluateCA(Link l) {
        assertEquals("Knows", l.getRelation());
        assertEquals(c, l.getSource());
        assertEquals(a, l.getDestination());
        assertEquals(ca.getStrength(), l.getStrength(), 0.0001);
    }

    private void evaluateBC(Link l) {
        assertEquals("Knows", l.getRelation());
        assertEquals(b, l.getSource());
        assertEquals(c, l.getDestination());
        assertEquals(bc.getStrength(), l.getStrength(), 0.0001);
    }

    private void evaluateCB(Link l) {
        assertEquals("Knows", l.getRelation());
        assertEquals(c, l.getSource());
        assertEquals(b, l.getDestination());
        assertEquals(cb.getStrength(), l.getStrength(), 0.0001);
    }

    private void evaluateBD(Link l) {
        assertEquals("Knows", l.getRelation());
        assertEquals(b, l.getSource());
        assertEquals(d, l.getDestination());
        assertEquals(bd.getStrength(), l.getStrength(), 0.0001);
    }

    private void evaluateDC(Link l) {
        assertEquals("Knows", l.getRelation());
        assertEquals(d, l.getSource());
        assertEquals(c, l.getDestination());
        assertEquals(dc.getStrength(), l.getStrength(), 0.0001);
    }

    private void evaluateAE(Link l) {
        assertEquals("Knows", l.getRelation());
        assertEquals(a, l.getSource());
        assertEquals(e, l.getDestination());
        assertEquals(ae.getStrength(), l.getStrength(), 0.0001);
    }

    private void evaluateA1(Link l) {
        assertEquals("Given", l.getRelation());
        assertEquals(a, l.getSource());
        assertEquals(art1, l.getDestination());
        assertEquals(a1.getStrength(), l.getStrength(), 0.0001);
    }

    private void evaluateB1(Link l) {
        assertEquals("Given", l.getRelation());
        assertEquals(b, l.getSource());
        assertEquals(art1, l.getDestination());
        assertEquals(b1.getStrength(), l.getStrength(), 0.0001);
    }

    private void evaluateB3(Link l) {
        assertEquals("Given", l.getRelation());
        assertEquals(b, l.getSource());
        assertEquals(art3, l.getDestination());
        assertEquals(b3.getStrength(), l.getStrength(), 0.0001);
    }

    private void evaluateC3(Link l) {
        assertEquals("Given", l.getRelation());
        assertEquals(c, l.getSource());
        assertEquals(art3, l.getDestination());
        assertEquals(c3.getStrength(), l.getStrength(), 0.0001);
        List<Property> props = c3.getProperty();
        assertNotNull(props);
        assertEquals(1, props.size());
        assertEquals(java.lang.Double.class, props.get(0).getPropertyClass());
        assertEquals("DummyProperty", props.get(0).getType());
        List<Object> values = props.get(0).getValue();
        assertNotNull(values);
        assertEquals(2, values.size());
    }

    private void evaluateC5(Link l) {
        assertEquals("Given", l.getRelation());
        assertEquals(c, l.getSource());
        assertEquals(art5, l.getDestination());
        assertEquals(c5.getStrength(), l.getStrength(), 0.0001);
    }

    private void evaluateD7(Link l) {
        assertEquals("Given", l.getRelation());
        assertEquals(d, l.getSource());
        assertEquals(art7, l.getDestination());
        assertEquals(d7.getStrength(), l.getStrength(), 0.0001);
    }
}
