package org.opennms.netmgt.snmp;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import junit.framework.TestSuite;
import org.opennms.netmgt.collectd.MibObject;
import org.opennms.netmgt.snmp.mock.BulkPdu;
import org.opennms.netmgt.snmp.mock.NextPdu;
import org.opennms.netmgt.snmp.mock.RequestPdu;
import org.opennms.netmgt.snmp.mock.ResponsePdu;
import org.opennms.netmgt.snmp.mock.TestPdu;
import org.opennms.netmgt.snmp.mock.TestVarBind;

public class SnmpCollectionTrackerTest extends SnmpCollectorTestCase {

    public static TestSuite suite() {
        Class testClass = SnmpCollectionTrackerTest.class;
        TestSuite suite = new TestSuite(testClass.getName());
        suite.addTest(new VersionSettingTestSuite(testClass, "SNMPv1 Tests", V1));
        suite.addTest(new VersionSettingTestSuite(testClass, "SNMPv2 Tests", V2));
        return suite;
    }

    private abstract static class TestPduBuilder extends PduBuilder {

        public TestPduBuilder(int maxVarsPerPdu) {
            super(maxVarsPerPdu);
        }

        public abstract RequestPdu getPdu();
    }

    private static class GetNextBuilder extends TestPduBuilder {

        private final NextPdu m_nextPdu;

        private GetNextBuilder(int maxVarsPerPdu) {
            super(maxVarsPerPdu);
            m_nextPdu = TestPdu.getNext();
        }

        public RequestPdu getPdu() {
            return m_nextPdu;
        }

        public void addOid(SnmpObjId snmpObjId) {
            m_nextPdu.addVarBind(snmpObjId);
        }

        public void setNonRepeaters(int numNonRepeaters) {
        }

        public void setMaxRepetitions(int maxRepititions) {
        }
    }

    private class GetBulkBuilder extends TestPduBuilder {

        private BulkPdu m_bulkPdu;

        private int m_nonRepeaters = 0;

        public GetBulkBuilder(int maxVarsPerPdu) {
            super(maxVarsPerPdu);
            m_bulkPdu = TestPdu.getBulk();
        }

        public RequestPdu getPdu() {
            return m_bulkPdu;
        }

        public void addOid(SnmpObjId snmpObjId) {
            m_bulkPdu.addVarBind(snmpObjId);
            MibObject mibObj = findMibObjectFor(snmpObjId);
            if (!"ifIndex".equals(mibObj.getInstance())) m_nonRepeaters++;
        }

        public void setNonRepeaters(int numNonRepeaters) {
            m_bulkPdu.setNonRepeaters(numNonRepeaters);
            assertEquals(m_nonRepeaters, numNonRepeaters);
        }

        public void setMaxRepetitions(int maxRepititions) {
            m_bulkPdu.setMaxRepititions(maxRepititions);
            assertTrue(maxRepititions >= 1);
        }
    }

    public static final int V1 = 1;

    public static final int V2 = 2;

    public int m_version = V2;

    private int m_maxLoopCount = 100;

    public void setVersion(int version) {
        m_version = version;
    }

    public MibObject findMibObjectFor(SnmpObjId snmpObjId) {
        for (Iterator it = m_objList.iterator(); it.hasNext(); ) {
            MibObject mibObj = (MibObject) it.next();
            if (SnmpObjId.get(mibObj.getOid()).isPrefixOf(snmpObjId)) {
                return mibObj;
            }
        }
        return null;
    }

    protected void setUp() throws Exception {
        super.setUp();
        switch(m_version) {
            case V1:
                m_agent.setBehaviorToV1();
                break;
            case V2:
                m_agent.setBehaviorToV2();
                break;
            default:
                throw new IllegalStateException("Don't understand version number " + m_version);
        }
        m_agent.loadSnmpTestData(getClass(), "snmpTestData1.properties");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetNextVarbindsForZeroInstanceVars() {
        addSysDescr();
        addSysName();
        SnmpCollectionTracker tracker = new SnmpCollectionTracker(m_objList);
        TestPduBuilder pduBuilder = getPduBuilder(50);
        ResponseProcessor rp = tracker.buildNextPdu(pduBuilder);
        RequestPdu next = pduBuilder.getPdu();
        assertNotNull(rp);
        assertEquals(2, next.size());
        ResponsePdu resp = m_agent.send(next);
        assertNotNull(resp);
        assertEquals(2, resp.size());
        assertEquals(CollectionTracker.NO_ERR, resp.getErrorStatus());
        for (int i = 0; i < resp.size(); i++) {
            TestVarBind varBind = (TestVarBind) resp.getVarBindAt(i);
            rp.processResponse(varBind.getObjId(), getVarBindValue(varBind));
        }
        for (int i = 0; i < m_objList.size(); i++) {
            MibObject mibObj = (MibObject) m_objList.get(i);
            verifyColumnData(tracker, SnmpObjId.get(mibObj.getOid()));
        }
    }

    public void testCollectSystemGroup() {
        addSystemGroup();
        verifyCollection(5);
    }

    public void testCollectIfTable() {
        addIfTable();
        verifyCollection(5);
    }

    public void testCollectIpAddrTable() {
        addIpAddrTable();
        verifyCollection(5);
    }

    public void testSpecificPlusColumn() {
        addSysName();
        addIfSpeed();
        verifyCollection(5);
    }

    public void testAllTables() {
        addSystemGroup();
        addIfTable();
        addIpAddrTable();
        verifyCollection(50);
    }

    public void testAllTablesFewVarsPerPdu() {
        addSystemGroup();
        addIfTable();
        addIpAddrTable();
        verifyCollection(7);
    }

    public void testAllTablesFewVarsPerPduWithFewResponses() {
        addSystemGroup();
        addIfTable();
        addIpAddrTable();
        m_agent.setMaxResponseSize(23);
        verifyCollection(7);
    }

    public void testGenErr() {
        m_agent.introduceGenErr(SnmpObjId.get(".1.3.6.1.2.1.2.2.1.16", "2"));
        addIfOutOctets();
        m_agent.setMaxResponseSize(23);
        verifyCollection(7);
    }

    public void testTooBig() {
        addSystemGroup();
        addIfTable();
        addIpAddrTable();
        m_agent.setMaxResponseSize(1);
        verifyCollection(100);
    }

    public void testNoSuchName() {
        addSystemGroup();
        addIfTable();
        addInvalid();
        addIpAddrTable();
        verifyCollection(10);
    }

    public void testNoSuchNameOnSingleVarBind() {
        addInvalid();
        verifyCollection(5);
    }

    private void verifyCollection(int maxVarsPerPdu) {
        SnmpCollectionTracker tracker = new SnmpCollectionTracker(m_objList);
        int loopCount = 0;
        while (!tracker.isFinished()) {
            assertTrue("loopCount exceeded " + m_maxLoopCount + ". Possible infinite loop!", loopCount++ < m_maxLoopCount);
            TestPduBuilder pduBuilder = getPduBuilder(maxVarsPerPdu);
            ResponseProcessor rp = tracker.buildNextPdu(pduBuilder);
            RequestPdu request = pduBuilder.getPdu();
            assertTrue(request.size() <= maxVarsPerPdu);
            ResponsePdu response = m_agent.send(request);
            boolean retry = rp.processErrors(response.getErrorStatus(), response.getErrorIndex());
            assertEquals(response.getErrorStatus() != ResponsePdu.NO_ERR, retry);
            if (retry) {
                maxVarsPerPdu = pduBuilder.getMaxVarsPerPdu();
                continue;
            }
            System.err.println("Get a response with " + response.size() + " vars");
            for (Iterator it = response.getVarBinds().iterator(); it.hasNext(); ) {
                TestVarBind varBind = (TestVarBind) it.next();
                rp.processResponse(varBind.getObjId(), getVarBindValue(varBind));
            }
        }
        verifyCollectedData(tracker);
    }

    private SnmpValue getVarBindValue(TestVarBind varBind) {
        return varBind.getValue();
    }

    private TestPduBuilder getPduBuilder(int maxVarsPerPdu) {
        switch(m_version) {
            case V1:
                return new GetNextBuilder(maxVarsPerPdu);
            case V2:
                return new GetBulkBuilder(maxVarsPerPdu);
            default:
                throw new IllegalStateException("Don't understand version " + m_version);
        }
    }

    private void verifyCollectedData(SnmpCollectionTracker tracker) {
        for (Iterator it = m_objList.iterator(); it.hasNext(); ) {
            MibObject mibObj = (MibObject) it.next();
            SnmpObjId base = SnmpObjId.get(mibObj.getOid());
            verifyColumnData(tracker, base);
        }
        Set instances = tracker.getInstances();
        for (Iterator iter = instances.iterator(); iter.hasNext(); ) {
            SnmpInstId inst = (SnmpInstId) iter.next();
            Map store = tracker.getDataForInstance(inst);
            for (Iterator it = store.keySet().iterator(); it.hasNext(); ) {
                SnmpObjId base = (SnmpObjId) it.next();
                verifyBaseRequested(base);
                SnmpObjId reqId = SnmpObjId.get(base, inst);
                Object val = getValueFor(reqId);
                assertEquals(store.get(base), val);
            }
        }
    }

    private Object getValueFor(SnmpObjId reqId) {
        return m_agent.getValueFor(reqId);
    }

    private void verifyBaseRequested(SnmpObjId base) {
        for (Iterator it = m_objList.iterator(); it.hasNext(); ) {
            MibObject mibObj = (MibObject) it.next();
            SnmpObjId mibOid = SnmpObjId.get(mibObj.getOid());
            if (base.equals(mibOid)) return;
        }
        fail("Expected " + base + " to be in the m_objList");
    }

    private void verifyColumnData(SnmpCollectionTracker tracker, SnmpObjId column) {
        try {
            Map columnData = new TreeMap();
            for (SnmpObjId mib = m_agent.getFollowingObjId(column); column.isPrefixOf(mib); mib = m_agent.getFollowingObjId(mib)) {
                Object result = getValueFor(mib);
                columnData.put(mib, result);
            }
            for (Iterator it = columnData.keySet().iterator(); it.hasNext(); ) {
                SnmpObjId mib = (SnmpObjId) it.next();
                SnmpInstId inst = mib.getInstance(column);
                Object result = columnData.get(mib);
                Map store = tracker.getDataForInstance(inst);
                assertNotNull("No Instance data for " + mib, store);
                assertEquals("Unexpected value collected for oid " + mib, result, store.get(column));
            }
        } catch (RuntimeException e) {
        }
    }
}
