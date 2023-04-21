package org.mobicents.protocols.ss7.m3ua.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javolution.util.FastMap;
import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.AssociationListener;
import org.mobicents.protocols.api.AssociationType;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.api.PayloadData;
import org.mobicents.protocols.api.Server;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.transfer.PayloadDataImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ProtocolDataImpl;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.asptm.ASPActiveAck;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.mobicents.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3Primitive;
import org.mobicents.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartListener;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the FSM of client side AS and ASP's
 * 
 * @author amit bhayani
 * 
 */
public class RemSgFSMTest {

    private ParameterFactoryImpl parmFactory = new ParameterFactoryImpl();

    private MessageFactoryImpl messageFactory = new MessageFactoryImpl();

    private M3UAManagement clientM3UAMgmt = null;

    private Mtp3UserPartListenerimpl mtp3UserPartListener = null;

    private Semaphore semaphore = null;

    private TransportManagement transportManagement = null;

    public RemSgFSMTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() throws Exception {
        semaphore = new Semaphore(0);
        this.transportManagement = new TransportManagement();
        this.clientM3UAMgmt = new M3UAManagement("RemSgFSMTest");
        this.clientM3UAMgmt.setTransportManagement(this.transportManagement);
        this.mtp3UserPartListener = new Mtp3UserPartListenerimpl();
        this.clientM3UAMgmt.addMtp3UserPartListener(this.mtp3UserPartListener);
        this.clientM3UAMgmt.start();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        clientM3UAMgmt.getAppServers().clear();
        clientM3UAMgmt.getAspfactories().clear();
        clientM3UAMgmt.getRoute().clear();
        clientM3UAMgmt.stop();
    }

    private AspState getAspState(FSM fsm) {
        return AspState.getState(fsm.getState().getName());
    }

    private AsState getAsState(FSM fsm) {
        return AsState.getState(fsm.getState().getName());
    }

    /**
	 * Test with RC Set
	 * @throws Exception
	 */
    @Test
    public void testSingleAspInAsWithRC() throws Exception {
        this.transportManagement.addAssociation(null, 0, null, 0, "testAssoc1");
        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });
        As as = this.clientM3UAMgmt.createAs("testas", Functionality.AS, ExchangeType.SE, null, rc, null, null);
        AspFactory localAspFactory = this.clientM3UAMgmt.createAspFactory("testasp", "testAssoc1");
        localAspFactory.start();
        Asp asp = clientM3UAMgmt.assignAspToAs("testas", "testasp");
        this.clientM3UAMgmt.addRoute(3, -1, -1, "testas");
        this.clientM3UAMgmt.addRoute(2, 10, -1, "testas");
        this.clientM3UAMgmt.addRoute(2, -1, -1, "testas");
        TestAssociation testAssociation = (TestAssociation) this.transportManagement.getAssociation("testAssoc1");
        testAssociation.signalCommUp();
        FSM aspLocalFSM = asp.getLocalFSM();
        assertEquals(AspState.UP_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));
        M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        localAspFactory.read(message);
        Notify notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc);
        Status status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
        notify.setStatus(status);
        localAspFactory.read(notify);
        assertEquals(AspState.ACTIVE_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));
        FSM asPeerFSM = as.getPeerFSM();
        assertEquals(AsState.INACTIVE, this.getAsState(asPeerFSM));
        ASPActiveAck aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK);
        aspActiveAck.setRoutingContext(rc);
        localAspFactory.read(aspActiveAck);
        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc);
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
        notify.setStatus(status);
        localAspFactory.read(notify);
        assertEquals(AspState.ACTIVE, this.getAspState(aspLocalFSM));
        assertEquals(AsState.ACTIVE, this.getAsState(asPeerFSM));
        semaphore.tryAcquire(2000, TimeUnit.MILLISECONDS);
        Mtp3Primitive mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.RESUME, mtp3Primitive.getType());
        assertEquals(3, mtp3Primitive.getAffectedDpc());
        semaphore.tryAcquire(2000, TimeUnit.MILLISECONDS);
        mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.RESUME, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());
        localAspFactory.stop();
        assertEquals(AspState.DOWN_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN, -1, -1));
        assertEquals(AsState.PENDING, this.getAsState(asPeerFSM));
        semaphore.tryAcquire(3000, TimeUnit.MILLISECONDS);
        mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.PAUSE, mtp3Primitive.getType());
        assertEquals(3, mtp3Primitive.getAffectedDpc());
        semaphore.tryAcquire(3000, TimeUnit.MILLISECONDS);
        mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.PAUSE, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());
        assertNull(testAssociation.txPoll());
    }

    /**
	 * Test with RC Set
	 * @throws Exception
	 */
    @Test
    public void testSingleAspInAsWithoutRC() throws Exception {
        this.transportManagement.addAssociation(null, 0, null, 0, "testAssoc1");
        As as = this.clientM3UAMgmt.createAs("testas", Functionality.AS, ExchangeType.SE, null, null, null, null);
        AspFactory localAspFactory = this.clientM3UAMgmt.createAspFactory("testasp", "testAssoc1");
        localAspFactory.start();
        Asp asp = clientM3UAMgmt.assignAspToAs("testas", "testasp");
        this.clientM3UAMgmt.addRoute(2, -1, -1, "testas");
        TestAssociation testAssociation = (TestAssociation) this.transportManagement.getAssociation("testAssoc1");
        testAssociation.signalCommUp();
        FSM aspLocalFSM = asp.getLocalFSM();
        assertEquals(AspState.UP_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));
        M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        localAspFactory.read(message);
        Notify notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        Status status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
        notify.setStatus(status);
        localAspFactory.read(notify);
        assertEquals(AspState.ACTIVE_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));
        FSM asPeerFSM = as.getPeerFSM();
        assertEquals(AsState.INACTIVE, this.getAsState(asPeerFSM));
        ASPActiveAck aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK);
        localAspFactory.read(aspActiveAck);
        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
        notify.setStatus(status);
        localAspFactory.read(notify);
        assertEquals(AspState.ACTIVE, this.getAspState(aspLocalFSM));
        assertEquals(AsState.ACTIVE, this.getAsState(asPeerFSM));
        semaphore.tryAcquire(2000, TimeUnit.MILLISECONDS);
        Mtp3Primitive mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.RESUME, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());
        assertEquals(TrafficModeType.Loadshare, as.getTrafficModeType().getMode());
        localAspFactory.stop();
        assertEquals(AspState.DOWN_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN, -1, -1));
        assertEquals(AsState.PENDING, this.getAsState(asPeerFSM));
        semaphore.tryAcquire(3000, TimeUnit.MILLISECONDS);
        mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.PAUSE, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());
        assertNull(testAssociation.txPoll());
    }

    @Test
    public void testSingleAspInMultipleAs() throws Exception {
        this.transportManagement.addAssociation(null, 0, null, 0, "testAssoc1");
        RoutingContext rc1 = parmFactory.createRoutingContext(new long[] { 100 });
        As remAs1 = this.clientM3UAMgmt.createAs("testas1", Functionality.AS, ExchangeType.SE, null, rc1, null, null);
        RoutingContext rc2 = parmFactory.createRoutingContext(new long[] { 200 });
        As remAs2 = clientM3UAMgmt.createAs("testas2", Functionality.AS, ExchangeType.SE, null, rc2, null, null);
        AspFactory aspFactory = clientM3UAMgmt.createAspFactory("testasp", "testAssoc1");
        aspFactory.start();
        Asp remAsp1 = clientM3UAMgmt.assignAspToAs("testas1", "testasp");
        Asp remAsp2 = clientM3UAMgmt.assignAspToAs("testas2", "testasp");
        this.clientM3UAMgmt.addRoute(2, -1, -1, "testas1");
        this.clientM3UAMgmt.addRoute(2, -1, -1, "testas2");
        this.clientM3UAMgmt.addRoute(3, -1, -1, "testas1");
        this.clientM3UAMgmt.addRoute(3, -1, -1, "testas2");
        TestAssociation testAssociation = (TestAssociation) this.transportManagement.getAssociation("testAssoc1");
        testAssociation.signalCommUp();
        FSM asp1LocalFSM = remAsp1.getLocalFSM();
        FSM asp2LocalFSM = remAsp2.getLocalFSM();
        assertNull(remAsp1.getPeerFSM());
        assertNull(remAsp2.getPeerFSM());
        assertEquals(AspState.UP_SENT, this.getAspState(asp1LocalFSM));
        assertEquals(AspState.UP_SENT, this.getAspState(asp2LocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));
        FSM as1PeerFSM = remAs1.getPeerFSM();
        FSM as2PeerFSM = remAs2.getPeerFSM();
        assertNull(remAs1.getLocalFSM());
        assertNull(remAs2.getLocalFSM());
        assertEquals(AsState.DOWN, this.getAsState(as1PeerFSM));
        assertEquals(AsState.DOWN, this.getAsState(as2PeerFSM));
        M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        aspFactory.read(message);
        assertEquals(AspState.ACTIVE_SENT, this.getAspState(asp1LocalFSM));
        assertEquals(AspState.ACTIVE_SENT, this.getAspState(asp2LocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));
        Notify notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc1);
        Status status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
        notify.setStatus(status);
        aspFactory.read(notify);
        assertEquals(AsState.INACTIVE, this.getAsState(as1PeerFSM));
        assertEquals(AsState.DOWN, this.getAsState(as2PeerFSM));
        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc2);
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
        notify.setStatus(status);
        aspFactory.read(notify);
        assertEquals(AsState.INACTIVE, this.getAsState(as2PeerFSM));
        ASPActiveAck aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK);
        aspActiveAck.setRoutingContext(this.parmFactory.createRoutingContext(new long[] { 100, 200 }));
        aspFactory.read(aspActiveAck);
        assertEquals(AspState.ACTIVE, this.getAspState(asp1LocalFSM));
        assertEquals(AspState.ACTIVE, this.getAspState(asp2LocalFSM));
        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc1);
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
        notify.setStatus(status);
        aspFactory.read(notify);
        assertEquals(AsState.ACTIVE, this.getAsState(as1PeerFSM));
        assertEquals(AsState.INACTIVE, this.getAsState(as2PeerFSM));
        semaphore.tryAcquire(2000, TimeUnit.MILLISECONDS);
        Mtp3Primitive mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.RESUME, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());
        semaphore.tryAcquire(2000, TimeUnit.MILLISECONDS);
        mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.RESUME, mtp3Primitive.getType());
        assertEquals(3, mtp3Primitive.getAffectedDpc());
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());
        assertEquals(TrafficModeType.Loadshare, remAs1.getTrafficModeType().getMode());
        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc2);
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
        notify.setStatus(status);
        aspFactory.read(notify);
        assertEquals(AsState.ACTIVE, this.getAsState(as1PeerFSM));
        assertEquals(AsState.ACTIVE, this.getAsState(as2PeerFSM));
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());
        assertEquals(TrafficModeType.Loadshare, remAs2.getTrafficModeType().getMode());
        aspFactory.stop();
        assertEquals(AspState.DOWN_SENT, this.getAspState(asp1LocalFSM));
        assertEquals(AspState.DOWN_SENT, this.getAspState(asp2LocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN, -1, -1));
        assertEquals(AsState.PENDING, this.getAsState(as1PeerFSM));
        assertEquals(AsState.PENDING, this.getAsState(as2PeerFSM));
        semaphore.tryAcquire(3000, TimeUnit.MILLISECONDS);
        mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.PAUSE, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());
        semaphore.tryAcquire(3000, TimeUnit.MILLISECONDS);
        mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.PAUSE, mtp3Primitive.getType());
        assertEquals(3, mtp3Primitive.getAffectedDpc());
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());
        assertNull(testAssociation.txPoll());
    }

    @Test
    public void testTwoAspInAsOverride() throws Exception {
        TestAssociation testAssociation1 = (TestAssociation) this.transportManagement.addAssociation(null, 0, null, 0, "testAssoc1");
        TestAssociation testAssociation2 = (TestAssociation) this.transportManagement.addAssociation(null, 0, null, 0, "testAssoc2");
        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });
        TrafficModeType trModType = parmFactory.createTrafficModeType(TrafficModeType.Override);
        As remAs = this.clientM3UAMgmt.createAs("testas", Functionality.AS, ExchangeType.SE, null, rc, null, null);
        AspFactory aspFactory1 = this.clientM3UAMgmt.createAspFactory("testasp1", "testAssoc1");
        aspFactory1.start();
        AspFactory aspFactory2 = this.clientM3UAMgmt.createAspFactory("testasp2", "testAssoc2");
        aspFactory2.start();
        Asp remAsp1 = clientM3UAMgmt.assignAspToAs("testas", "testasp1");
        Asp remAsp2 = clientM3UAMgmt.assignAspToAs("testas", "testasp2");
        this.clientM3UAMgmt.addRoute(2, -1, -1, "testas");
        FSM asp1LocalFSM = remAsp1.getLocalFSM();
        FSM asp2LocalFSM = remAsp2.getLocalFSM();
        assertNull(remAsp1.getPeerFSM());
        assertNull(remAsp2.getPeerFSM());
        testAssociation1.signalCommUp();
        assertEquals(AspState.UP_SENT, this.getAspState(asp1LocalFSM));
        assertTrue(validateMessage(testAssociation1, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));
        FSM asPeerFSM = remAs.getPeerFSM();
        assertEquals(AsState.DOWN, this.getAsState(asPeerFSM));
        M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        aspFactory1.read(message);
        assertEquals(AspState.ACTIVE_SENT, this.getAspState(asp1LocalFSM));
        assertTrue(validateMessage(testAssociation1, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));
        Notify notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc);
        Status status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
        notify.setStatus(status);
        aspFactory1.read(notify);
        assertEquals(AsState.INACTIVE, this.getAsState(asPeerFSM));
        ASPActiveAck aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK);
        aspActiveAck.setRoutingContext(rc);
        aspActiveAck.setTrafficModeType(trModType);
        aspFactory1.read(aspActiveAck);
        assertEquals(AspState.ACTIVE, this.getAspState(asp1LocalFSM));
        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc);
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
        notify.setStatus(status);
        aspFactory1.read(notify);
        aspFactory2.read(notify);
        assertEquals(AsState.ACTIVE, this.getAsState(asPeerFSM));
        semaphore.tryAcquire(2000, TimeUnit.MILLISECONDS);
        Mtp3Primitive mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.RESUME, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());
        testAssociation2.signalCommUp();
        assertEquals(AspState.UP_SENT, this.getAspState(asp2LocalFSM));
        assertTrue(validateMessage(testAssociation2, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));
        message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        aspFactory2.read(message);
        assertEquals(AspState.INACTIVE, this.getAspState(asp2LocalFSM));
        testAssociation1.signalCommLost();
        assertEquals(AspState.DOWN, this.getAspState(asp1LocalFSM));
        assertEquals(AsState.PENDING, this.getAsState(asPeerFSM));
        assertEquals(AspState.ACTIVE_SENT, this.getAspState(asp2LocalFSM));
        assertTrue(validateMessage(testAssociation2, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));
        aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK);
        aspActiveAck.setRoutingContext(rc);
        aspFactory2.read(aspActiveAck);
        assertEquals(AspState.ACTIVE, this.getAspState(asp2LocalFSM));
        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc);
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
        notify.setStatus(status);
        aspFactory2.read(notify);
        assertEquals(AsState.ACTIVE, this.getAsState(asPeerFSM));
        assertNull(testAssociation1.txPoll());
        assertNull(testAssociation2.txPoll());
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());
    }

    @Test
    public void testPendingQueue() throws Exception {
        TestAssociation testAssociation = (TestAssociation) this.transportManagement.addAssociation(null, 0, null, 0, "testAssoc");
        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });
        As as = this.clientM3UAMgmt.createAs("testas", Functionality.AS, ExchangeType.SE, null, rc, null, null);
        FSM asPeerFSM = as.getPeerFSM();
        AspFactory localAspFactory = clientM3UAMgmt.createAspFactory("testasp", "testAssoc");
        localAspFactory.start();
        Asp asp = clientM3UAMgmt.assignAspToAs("testas", "testasp");
        this.clientM3UAMgmt.addRoute(2, -1, -1, "testas");
        FSM aspLocalFSM = asp.getLocalFSM();
        testAssociation.signalCommUp();
        assertEquals(AspState.UP_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));
        M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        localAspFactory.read(message);
        Notify notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc);
        Status status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
        notify.setStatus(status);
        localAspFactory.read(notify);
        assertEquals(AspState.ACTIVE_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));
        assertEquals(AsState.INACTIVE, this.getAsState(asPeerFSM));
        ASPActiveAck aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK);
        aspActiveAck.setRoutingContext(rc);
        localAspFactory.read(aspActiveAck);
        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc);
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
        notify.setStatus(status);
        localAspFactory.read(notify);
        assertEquals(AspState.ACTIVE, this.getAspState(aspLocalFSM));
        assertEquals(AsState.ACTIVE, this.getAsState(asPeerFSM));
        semaphore.tryAcquire(2000, TimeUnit.MILLISECONDS);
        Mtp3Primitive mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.RESUME, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());
        localAspFactory.stop();
        assertEquals(AspState.DOWN_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN, -1, -1));
        assertEquals(AsState.PENDING, this.getAsState(asPeerFSM));
        message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN_ACK);
        localAspFactory.read(message);
        localAspFactory.start();
        PayloadDataImpl payload = (PayloadDataImpl) messageFactory.createMessage(MessageClass.TRANSFER_MESSAGES, MessageType.PAYLOAD);
        ProtocolDataImpl p1 = (ProtocolDataImpl) parmFactory.createProtocolData(1408, 123, 3, 1, 0, 1, new byte[] { 1, 2, 3, 4 });
        payload.setRoutingContext(rc);
        payload.setData(p1);
        as.write(payload);
        testAssociation.signalCommUp();
        assertEquals(AspState.UP_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));
        message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        localAspFactory.read(message);
        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc);
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
        notify.setStatus(status);
        localAspFactory.read(notify);
        assertEquals(AspState.ACTIVE_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));
        assertEquals(AsState.PENDING, this.getAsState(asPeerFSM));
        aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK);
        aspActiveAck.setRoutingContext(rc);
        localAspFactory.read(aspActiveAck);
        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc);
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
        notify.setStatus(status);
        localAspFactory.read(notify);
        assertEquals(AspState.ACTIVE, this.getAspState(aspLocalFSM));
        assertEquals(AsState.ACTIVE, this.getAsState(asPeerFSM));
        M3UAMessage payLoadTemp = testAssociation.txPoll();
        assertNotNull(payLoadTemp);
        assertEquals(MessageClass.TRANSFER_MESSAGES, payLoadTemp.getMessageClass());
        assertEquals(MessageType.PAYLOAD, payLoadTemp.getMessageType());
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());
        assertNull(testAssociation.txPoll());
    }

    private boolean validateMessage(TestAssociation testAssociation, int msgClass, int msgType, int type, int info) {
        M3UAMessage message = testAssociation.txPoll();
        if (message == null) {
            return false;
        }
        if (message.getMessageClass() != msgClass || message.getMessageType() != msgType) {
            return false;
        }
        if (message.getMessageClass() == MessageClass.MANAGEMENT) {
            if (message.getMessageType() == MessageType.NOTIFY) {
                Status s = ((Notify) message).getStatus();
                if (s.getType() != type || s.getInfo() != info) {
                    return false;
                } else {
                    return true;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    class TestAssociation implements Association {

        private AssociationListener associationListener = null;

        private String name = null;

        private LinkedList<M3UAMessage> messageRxFromUserPart = new LinkedList<M3UAMessage>();

        TestAssociation(String name) {
            this.name = name;
        }

        M3UAMessage txPoll() {
            return messageRxFromUserPart.poll();
        }

        @Override
        public AssociationListener getAssociationListener() {
            return this.associationListener;
        }

        @Override
        public String getHostAddress() {
            return null;
        }

        @Override
        public int getHostPort() {
            return 0;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getPeerAddress() {
            return null;
        }

        @Override
        public int getPeerPort() {
            return 0;
        }

        @Override
        public String getServerName() {
            return null;
        }

        @Override
        public boolean isStarted() {
            return false;
        }

        @Override
        public void send(PayloadData payloadData) throws Exception {
            M3UAMessage m3uaMessage = messageFactory.createSctpMessage(payloadData.getData());
            this.messageRxFromUserPart.add(m3uaMessage);
        }

        @Override
        public void setAssociationListener(AssociationListener associationListener) {
            this.associationListener = associationListener;
        }

        public void signalCommUp() {
            this.associationListener.onCommunicationUp(this);
        }

        public void signalCommLost() {
            this.associationListener.onCommunicationLost(this);
        }

        @Override
        public IpChannelType getIpChannelType() {
            return null;
        }

        @Override
        public AssociationType getAssociationType() {
            return null;
        }

        @Override
        public String[] getExtraHostAddresses() {
            return null;
        }
    }

    class TransportManagement implements Management {

        private FastMap<String, Association> associations = new FastMap<String, Association>();

        @Override
        public Association addAssociation(String hostAddress, int hostPort, String peerAddress, int peerPort, String assocName) throws Exception {
            TestAssociation testAssociation = new TestAssociation(assocName);
            this.associations.put(assocName, testAssociation);
            return testAssociation;
        }

        @Override
        public Server addServer(String serverName, String hostAddress, int port) throws Exception {
            return null;
        }

        @Override
        public Association addServerAssociation(String peerAddress, int peerPort, String serverName, String assocName) throws Exception {
            return null;
        }

        @Override
        public Association getAssociation(String assocName) throws Exception {
            return this.associations.get(assocName);
        }

        @Override
        public Map<String, Association> getAssociations() {
            return associations.unmodifiable();
        }

        @Override
        public int getConnectDelay() {
            return 0;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public List<Server> getServers() {
            return null;
        }

        @Override
        public int getWorkerThreads() {
            return 0;
        }

        @Override
        public boolean isSingleThread() {
            return false;
        }

        @Override
        public void removeAssociation(String assocName) throws Exception {
        }

        @Override
        public void removeServer(String serverName) throws Exception {
        }

        @Override
        public void setConnectDelay(int connectDelay) {
        }

        @Override
        public void setSingleThread(boolean arg0) {
        }

        @Override
        public void setWorkerThreads(int arg0) {
        }

        @Override
        public void start() throws Exception {
        }

        @Override
        public void startAssociation(String arg0) throws Exception {
        }

        @Override
        public void startServer(String arg0) throws Exception {
        }

        @Override
        public void stop() throws Exception {
        }

        @Override
        public void stopAssociation(String arg0) throws Exception {
        }

        @Override
        public void stopServer(String arg0) throws Exception {
        }

        @Override
        public String getPersistDir() {
            return null;
        }

        @Override
        public void setPersistDir(String arg0) {
        }

        @Override
        public Association addAssociation(String arg0, int arg1, String arg2, int arg3, String arg4, IpChannelType arg5, String[] extraHostAddresses) throws Exception {
            return null;
        }

        @Override
        public Server addServer(String arg0, String arg1, int arg2, IpChannelType arg3, String[] extraHostAddresses) throws Exception {
            return null;
        }

        @Override
        public Association addServerAssociation(String arg0, int arg1, String arg2, String arg3, IpChannelType arg4) throws Exception {
            return null;
        }

        @Override
        public void removeAllResourses() throws Exception {
        }
    }

    class Mtp3UserPartListenerimpl implements Mtp3UserPartListener {

        private LinkedList<Mtp3Primitive> mtp3Primitives = new LinkedList<Mtp3Primitive>();

        private LinkedList<Mtp3TransferPrimitive> mtp3TransferPrimitives = new LinkedList<Mtp3TransferPrimitive>();

        Mtp3Primitive rxMtp3PrimitivePoll() {
            return this.mtp3Primitives.poll();
        }

        Mtp3TransferPrimitive rxMtp3TransferPrimitivePoll() {
            return this.mtp3TransferPrimitives.poll();
        }

        @Override
        public void onMtp3PauseMessage(Mtp3PausePrimitive pause) {
            this.mtp3Primitives.add(pause);
            semaphore.release();
        }

        @Override
        public void onMtp3ResumeMessage(Mtp3ResumePrimitive resume) {
            this.mtp3Primitives.add(resume);
            semaphore.release();
        }

        @Override
        public void onMtp3StatusMessage(Mtp3StatusPrimitive status) {
            this.mtp3Primitives.add(status);
            semaphore.release();
        }

        @Override
        public void onMtp3TransferMessage(Mtp3TransferPrimitive transfer) {
            this.mtp3TransferPrimitives.add(transfer);
            semaphore.release();
        }
    }
}
