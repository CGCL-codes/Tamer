package tests;

import java.io.InputStream;
import junit.framework.TestCase;
import tests.helpers.SnmpHelper;
import ch.unibas.snmp.SnmpAgent;
import edu.drexel.cs.snmp.SNMPBadValueException;
import edu.drexel.cs.snmp.SNMPGetException;
import edu.drexel.cs.snmp.SNMPObjectIdentifier;
import edu.drexel.cs.snmp.SNMPOctetString;

public class SnmpAgentTest extends TestCase {

    private static final String DEFAULT_TEST_VALUE = "OID 1.3.6.1.4.1.22865.1.1 Test String";

    private static final String DEFAULT_TEST_OID = "1.3.6.1.4.1.22865.1.1";

    private static final String DEFAULT_TEST_VALUE_2 = "OID 1.3.6.1.4.1.22865.1.2 Test String";

    private static final String DEFAULT_TEST_OID_2 = "1.3.6.1.4.1.22865.1.2";

    private static final String COMMUNITY = "public";

    private static final int PORT = 1622;

    private SnmpAgent agent;

    protected void setUp() throws Exception {
        super.setUp();
        agent = new SnmpAgent(COMMUNITY, PORT, DEFAULT_TEST_OID);
        agent.add(DEFAULT_TEST_OID, DEFAULT_TEST_VALUE);
    }

    protected void tearDown() throws Exception {
        agent.stop();
        super.tearDown();
    }

    public void testCommunity() throws Exception {
        assertEquals(DEFAULT_TEST_VALUE, SnmpHelper.snmpGet(DEFAULT_TEST_OID, COMMUNITY, PORT));
        String new_community = "irgendwas";
        agent.setSnmpCommunity(new_community);
        assertEquals(DEFAULT_TEST_VALUE, SnmpHelper.snmpGet(DEFAULT_TEST_OID, new_community, PORT));
        try {
            assertEquals(DEFAULT_TEST_VALUE, SnmpHelper.snmpGet(DEFAULT_TEST_OID, COMMUNITY, PORT));
            assertTrue(false);
        } catch (SNMPGetException e) {
            assertEquals("OID " + DEFAULT_TEST_OID + " not available for retrieval", e.getMessage());
        }
    }

    public void testAdd() throws Exception {
        try {
            assertEquals(DEFAULT_TEST_VALUE_2, SnmpHelper.snmpGet(DEFAULT_TEST_OID_2, COMMUNITY, PORT));
            assertTrue(false);
        } catch (SNMPGetException e) {
            assertEquals("OID " + DEFAULT_TEST_OID_2 + " not available for retrieval", e.getMessage());
        }
        agent.add(DEFAULT_TEST_OID_2, DEFAULT_TEST_VALUE_2);
        assertEquals(DEFAULT_TEST_VALUE_2, SnmpHelper.snmpGet(DEFAULT_TEST_OID_2, COMMUNITY, PORT));
    }

    public void testGetNext() throws Exception {
        agent.add(new SNMPObjectIdentifier(DEFAULT_TEST_OID_2), new SNMPOctetString(DEFAULT_TEST_VALUE_2));
        assertEquals("JMeter SNMP Agent", SnmpHelper.snmpGetNext("", COMMUNITY, PORT));
        assertEquals(DEFAULT_TEST_VALUE, SnmpHelper.snmpGetNext("1", COMMUNITY, PORT));
        assertEquals(DEFAULT_TEST_VALUE_2, SnmpHelper.snmpGetNext(DEFAULT_TEST_OID, COMMUNITY, PORT));
        try {
            SnmpHelper.snmpGetNext(DEFAULT_TEST_OID_2, COMMUNITY, PORT);
            assertTrue(false);
        } catch (SNMPGetException e) {
            assertEquals("OID " + DEFAULT_TEST_OID_2 + " not available for retrieval", e.getMessage());
        }
    }

    public void testSnmpWalk() throws Exception {
        add("1.3.6.1.4.1.22865.1");
        add("1.3.6.1.4.1.22865.1.2.100");
        add("1.3.6.1.4.1.22865.1.2.120");
        add("1.3.6.1.4.1.22865.1.2.120.1");
        agent.add(new SNMPObjectIdentifier(DEFAULT_TEST_OID_2), new SNMPOctetString(DEFAULT_TEST_VALUE_2));
        String cmd = "/usr/bin/snmpwalk -v 1 -c " + COMMUNITY + " localhost:" + PORT + " ";
        Process process = Runtime.getRuntime().exec(cmd);
        InputStream inputStream = process.getInputStream();
        int i;
        while ((i = inputStream.read()) != -1) {
            System.out.write(i);
        }
    }

    private void add(String string) {
        try {
            agent.add(string, "value of " + string);
        } catch (SNMPBadValueException e) {
            e.printStackTrace();
        }
    }
}
