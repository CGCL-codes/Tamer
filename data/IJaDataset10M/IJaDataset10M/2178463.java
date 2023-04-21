package org.mobicents.protocols.ss7.sccp.impl.translation;

import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.impl.User;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author amit bhayani
 * @author kulikov
 * @author baranowb
 */
public class PCSSNSccpStackImplTest extends SccpHarness {

    private SccpAddress a1, a2;

    public PCSSNSccpStackImplTest() {
    }

    @BeforeClass
    public void setUpClass() throws Exception {
        this.sccpStack1Name = "PCSSNSccTestSccpStack1";
        this.sccpStack2Name = "PCSSNSccTestSccpStack2";
    }

    @AfterClass
    public void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() throws IllegalStateException {
        super.setUp();
    }

    @AfterMethod
    public void tearDown() {
        super.tearDown();
    }

    /**
	 * Test of configure method, of class SccpStackImpl.
	 */
    @Test(groups = { "gtt", "functional.route" })
    public void testRemoteRoutingBasedOnSsn() throws Exception {
        a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 1, null, 8);
        a2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 2, null, 8);
        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());
        u1.register();
        u2.register();
        u1.send();
        u2.send();
        Thread.currentThread().sleep(3000);
        assertTrue(u1.check(), "Message not received");
        assertTrue(u2.check(), "Message not received");
    }
}
