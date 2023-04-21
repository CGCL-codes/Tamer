package com.thoughtworks.acceptance;

import com.thoughtworks.xstream.XStream;

public class RelativeXPathCircularReferenceTest extends AbstractCircularReferenceTest {

    protected void setUp() throws Exception {
        super.setUp();
        xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
    }

    public void testCircularReferenceXml() {
        Person bob = new Person("bob");
        Person jane = new Person("jane");
        bob.likes = jane;
        jane.likes = bob;
        String expected = "" + "<person>\n" + "  <firstname>bob</firstname>\n" + "  <likes>\n" + "    <firstname>jane</firstname>\n" + "    <likes reference=\"../..\"/>\n" + "  </likes>\n" + "</person>";
        assertEquals(expected, xstream.toXML(bob));
    }

    public void testCircularReferenceToSelfXml() {
        Person bob = new Person("bob");
        bob.likes = bob;
        String expected = "" + "<person>\n" + "  <firstname>bob</firstname>\n" + "  <likes reference=\"..\"/>\n" + "</person>";
        assertEquals(expected, xstream.toXML(bob));
    }

    static class LinkedElement {

        String name;

        LinkedElement next;

        LinkedElement(String name) {
            this.name = name;
        }
    }

    public void testRing() {
        LinkedElement tom = new LinkedElement("Tom");
        LinkedElement dick = new LinkedElement("Dick");
        LinkedElement harry = new LinkedElement("Harry");
        tom.next = dick;
        dick.next = harry;
        harry.next = tom;
        xstream.alias("elem", LinkedElement.class);
        String expected = "" + "<elem>\n" + "  <name>Tom</name>\n" + "  <next>\n" + "    <name>Dick</name>\n" + "    <next>\n" + "      <name>Harry</name>\n" + "      <next reference=\"../../..\"/>\n" + "    </next>\n" + "  </next>\n" + "</elem>";
        assertEquals(expected, xstream.toXML(tom));
    }
}
