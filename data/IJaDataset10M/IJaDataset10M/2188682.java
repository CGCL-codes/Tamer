package com.controltier.ctl.common;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class TestNodesXMLParser extends TestCase {

    NodesXMLParser nodesXMLParser;

    File xmlfile1;

    File xmlfile2;

    File xmlfile3;

    File dneFile1;

    public TestNodesXMLParser(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestNodesXMLParser.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        xmlfile1 = new File("src/test/com/controltier/ctl/common/test-nodes1.xml");
        xmlfile2 = new File("src/test/com/controltier/ctl/common/test-nodes2.xml");
        xmlfile3 = new File("src/test/com/controltier/ctl/common/test-nodes3.xml");
        dneFile1 = new File("src/test/com/controltier/ctl/common/DNE-file.xml");
    }

    protected void tearDown() throws Exception {
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }

    class nodeReceiver implements NodeReceiver {

        HashMap<String, INodeEntry> map = new HashMap<String, INodeEntry>();

        public void putNode(final INodeEntry iNodeEntry) {
            map.put(iNodeEntry.getNodename(), iNodeEntry);
        }
    }

    public void testParse() throws Exception {
        try {
            new NodesXMLParser(dneFile1, null).parse();
            fail("Should have thrown an Exception");
        } catch (NodeFileParserException ex) {
        }
        {
            final nodeReceiver receiver = new nodeReceiver();
            nodesXMLParser = new NodesXMLParser(xmlfile1, receiver);
            nodesXMLParser.parse();
            assertEquals("wrong number of nodes parsed", 2, receiver.map.size());
            INodeEntry node1 = receiver.map.get("testnode1");
            assertNotNull(node1);
            assertEquals("testnode1", node1.getNodename());
            assertEquals("SubNode", node1.getType());
            assertEquals("this is the testnode1 node", node1.getDescription());
            assertNotNull(node1.getTags());
            assertEquals(new HashSet<String>(Arrays.asList("boring", "priority1")), node1.getTags());
            assertEquals("host1.local", node1.getHostname());
            assertEquals("i386", node1.getOsArch());
            assertEquals("unix", node1.getOsFamily());
            assertEquals("Mac OS X", node1.getOsName());
            assertEquals("10.5.1", node1.getOsVersion());
            assertEquals("ctlBase1", node1.getCtlBase());
            assertEquals("ctlHome1", node1.getCtlHome());
            assertEquals("ctlUsername1", node1.getCtlUsername());
            assertTrue(node1.hasPasswordSet());
            assertNull(node1.getFrameworkProject());
            INodeEntry node2 = receiver.map.get("testnode2");
            assertNotNull(node2);
            assertEquals("testnode2", node2.getNodename());
            assertEquals("Node", node2.getType());
            assertEquals("ControlTier registered Node asdf", node2.getDescription());
            assertNotNull(node2.getTags());
            assertEquals(new HashSet<String>(Arrays.asList("boring")), node2.getTags());
            assertEquals("testnode2", node2.getHostname());
            assertEquals("x86", node2.getOsArch());
            assertEquals("windows", node2.getOsFamily());
            assertEquals("Windows XP", node2.getOsName());
            assertEquals("5.1", node2.getOsVersion());
            assertNull(node2.getCtlBase());
            assertNull(node2.getCtlHome());
            assertNull(node2.getCtlUsername());
            assertFalse(node2.hasPasswordSet());
            assertNull(node2.getFrameworkProject());
        }
        {
            final nodeReceiver receiver = new nodeReceiver();
            nodesXMLParser = new NodesXMLParser(xmlfile2, receiver);
            nodesXMLParser.parse();
            assertEquals("wrong number of nodes parsed", 3, receiver.map.size());
            INodeEntry node1 = receiver.map.get("testnode3");
            assertEquals("testnode3", node1.getNodename());
            assertEquals("Node", node1.getType());
            assertEquals("This is the third test node", node1.getDescription());
            assertNotNull(node1.getTags());
            assertEquals(new HashSet<String>(Arrays.asList("priority1", "elf")), node1.getTags());
            assertEquals("testnode3.local", node1.getHostname());
            assertEquals("intel", node1.getOsArch());
            assertEquals("solaris", node1.getOsFamily());
            assertEquals("Solaris Something", node1.getOsName());
            assertEquals("3.7", node1.getOsVersion());
            assertNull("ctlBase1", node1.getCtlBase());
            assertNull("ctlHome1", node1.getCtlHome());
            assertNull("ctlUsername1", node1.getCtlUsername());
            assertFalse(node1.hasPasswordSet());
            assertNull(node1.getFrameworkProject());
        }
    }

    public void testParseSettings() throws Exception {
        {
            final nodeReceiver receiver = new nodeReceiver();
            nodesXMLParser = new NodesXMLParser(xmlfile3, receiver);
            nodesXMLParser.parse();
            assertEquals("wrong number of nodes parsed", 3, receiver.map.size());
            INodeEntry node0 = receiver.map.get("testnode1");
            assertEquals("testnode1", node0.getNodename());
            assertEquals("SubNode", node0.getType());
            assertNull(node0.getSettings());
            INodeEntry node1 = receiver.map.get("testnode2");
            assertEquals("testnode2", node1.getNodename());
            assertEquals("Node", node1.getType());
            assertNotNull(node1.getSettings());
            assertEquals(2, node1.getSettings().size());
            assertEquals("set1val", node1.getSettings().get("set1"));
            assertEquals("set2val", node1.getSettings().get("set2"));
            INodeEntry node2 = receiver.map.get("testnode3");
            assertEquals("testnode3", node2.getNodename());
            assertEquals("Node", node2.getType());
            assertNotNull(node2.getSettings());
            assertEquals(3, node2.getSettings().size());
            assertEquals("set2val", node2.getSettings().get("set2"));
            assertEquals("set3val", node2.getSettings().get("set3"));
            assertEquals("set4val", node2.getSettings().get("set4"));
        }
    }
}
