package org.rakiura.cpn;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.w3c.dom.Document;

/**
 * Test utility for XML stuff. Tests if a net can be put into XML and if a net
 * can be put to java and loaded.
 *
 *<br>
 * TestXMLSerializer.java<br><br>
 * Created: Fri May 15 15:50:11 2003<br>
 *
 * @author Mariusz Nowostawski
 * @version $Revision: 1.6 $
 */
public class TestXMLSerializer extends TestCase {

    Net net;

    Place p1, p2, sp1, sp2;

    Transition t1;

    InputArc a1;

    OutputArc a2;

    public TestXMLSerializer(String name) {
        super(name);
    }

    /** Setup. */
    protected void setUp() {
        this.net = new BasicNet();
        final Multiset m1 = new Multiset();
        m1.add(new Integer(1));
        this.p1 = new Place(new Multiset(m1));
        this.p2 = new Place();
        this.t1 = new Transition();
        this.a1 = new InputArc(this.p1, this.t1);
        this.a2 = new OutputArc(this.t1, this.p2);
        this.a1.setExpressionText("var(1);");
        this.a2.setExpressionText("return new Multiset(getMultiset().getAny());");
        this.net.add(this.p1).add(this.p2);
        this.net.add(this.t1);
        this.net.add(this.a1).add(this.a2);
        Net subnet = new BasicNet();
        this.sp1 = new Place();
        this.sp2 = new Place();
        Transition st1 = new Transition();
        InputArc sa1 = new InputArc(this.sp1, st1);
        OutputArc sa2 = new OutputArc(st1, this.sp2);
        subnet.add(this.sp1).add(this.sp2).add(st1);
        subnet.add(sa1).add(sa2);
        this.net.add(subnet);
        System.out.println("p1 has ids: " + java.util.Arrays.asList(this.p1.getFusedPlacesIDs()));
        this.p1.addPlace(this.sp1);
        System.out.println("p1 has ids: " + java.util.Arrays.asList(this.p1.getFusedPlacesIDs()));
        this.p2.addPlace(this.sp2);
    }

    /**
   */
    public void testSimpleNetXMLDump() {
        Document doc = XMLSerializer.buildXML(this.net);
        try {
            XmlUtil.writeXML(doc, System.out);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void testJavaDump() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Document doc = XMLSerializer.buildXML(this.net);
        try {
            XmlUtil.writeXML(doc, os);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        Net netCopy = NetGenerator.createNet(os.toString());
        try {
            os.close();
        } catch (IOException ex) {
        }
        assertTrue(((Place) netCopy.forID(this.p1.getID())).getFusedPlacesIDs().length == 2);
        assertTrue(((Place) netCopy.forID(this.p2.getID())).getFusedPlacesIDs().length == 2);
        assertTrue(((Place) netCopy.forID(this.sp1.getID())).getFusedPlacesIDs().length == 1);
        assertTrue(((Place) netCopy.forID(this.sp1.getID())).getFusedPlacesIDs().length == 1);
    }

    /**
   * Test suite. */
    public static Test suite() {
        return new TestSuite(TestXMLSerializer.class);
    }
}
