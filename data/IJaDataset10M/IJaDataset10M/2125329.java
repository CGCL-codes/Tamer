package org.opennms.netmgt.collectd;

import java.net.InetAddress;
import java.net.UnknownHostException;
import junit.framework.TestSuite;
import org.opennms.netmgt.model.OnmsEntity;
import org.opennms.netmgt.model.OnmsSnmpInterface;
import org.opennms.netmgt.snmp.SnmpTestSuiteUtils;

public class SnmpIfCollectorTest extends SnmpCollectorTestCase {

    private final class IfInfoVisitor extends ResourceVisitor {

        public int ifInfoCount = 0;

        public void visitResource(CollectionResource resource) {
            if (!(resource instanceof IfInfo)) return;
            ifInfoCount++;
            IfInfo ifInfo = (IfInfo) resource;
            assertMibObjectsPresent(ifInfo, getAttributeList());
        }
    }

    public static TestSuite suite() {
        return SnmpTestSuiteUtils.createSnmpVersionTestSuite(SnmpIfCollectorTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testZeroVars() throws Exception {
        createSnmpInterface(1, 24, "lo", true);
        SnmpIfCollector collector = createSnmpIfCollector();
        waitForSignal();
        assertInterfaceMibObjectsPresent(collector.getCollectionSet(), 1);
    }

    private void assertInterfaceMibObjectsPresent(CollectionSet collectionSet, int expectedIfCount) {
        assertNotNull(collectionSet);
        if (getAttributeList().isEmpty()) return;
        IfInfoVisitor ifInfoVisitor = new IfInfoVisitor();
        collectionSet.visit(ifInfoVisitor);
        assertEquals("Unexpected number of interfaces", expectedIfCount, ifInfoVisitor.ifInfoCount);
    }

    public void testInvalidVar() throws Exception {
        addAttribute("invalid", "1.3.6.1.2.1.2.2.2.10", "ifIndex", "counter");
        assertFalse(getAttributeList().isEmpty());
        createSnmpInterface(1, 24, "lo", true);
        SnmpIfCollector collector = createSnmpIfCollector();
        waitForSignal();
        getAttributeList().remove(0);
        assertInterfaceMibObjectsPresent(collector.getCollectionSet(), 1);
    }

    public void testBadApple() throws Exception {
        addIfSpeed();
        addIfInOctets();
        addAttribute("invalid", "1.3.66.1.2.1.2.2.299.16", "ifIndex", "counter");
        addIfInErrors();
        addIfOutErrors();
        addIfInDiscards();
        assertFalse(getAttributeList().isEmpty());
        createSnmpInterface(1, 24, "lo", true);
        SnmpIfCollector collector = createSnmpIfCollector();
        waitForSignal();
        getAttributeList().remove(2);
        assertInterfaceMibObjectsPresent(collector.getCollectionSet(), 1);
    }

    public void testManyVars() throws Exception {
        addIfTable();
        assertFalse(getAttributeList().isEmpty());
        createSnmpInterface(1, 24, "lo", true);
        SnmpIfCollector collector = createSnmpIfCollector();
        waitForSignal();
        assertInterfaceMibObjectsPresent(collector.getCollectionSet(), 1);
    }

    private SnmpIfCollector createSnmpIfCollector() throws UnknownHostException {
        initializeAgent();
        SnmpIfCollector collector = new SnmpIfCollector(InetAddress.getLocalHost(), getCollectionSet().getCombinedIndexedAttributes(), getCollectionSet());
        createWalker(collector);
        return collector;
    }

    private OnmsEntity createSnmpInterface(int ifIndex, int ifType, String ifName, boolean collectionEnabled) {
        OnmsSnmpInterface m_snmpIface = new OnmsSnmpInterface();
        m_snmpIface.setIfIndex(new Integer(ifIndex));
        m_snmpIface.setIfType(new Integer(ifType));
        m_snmpIface.setIfName(ifName);
        m_snmpIface.setCollectionEnabled(collectionEnabled);
        m_node.addSnmpInterface(m_snmpIface);
        return m_snmpIface;
    }

    public void testManyIfs() throws Exception {
        addIfTable();
        assertFalse(getAttributeList().isEmpty());
        createSnmpInterface(1, 24, "lo0", true);
        createSnmpInterface(2, 55, "gif0", true);
        createSnmpInterface(3, 57, "stf0", true);
        SnmpIfCollector collector = createSnmpIfCollector();
        waitForSignal();
        assertInterfaceMibObjectsPresent(collector.getCollectionSet(), 3);
    }
}
