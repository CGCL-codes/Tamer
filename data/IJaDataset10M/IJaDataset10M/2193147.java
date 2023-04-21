package mirrormonkey.rpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Random;
import mirrormonkey.framework.entity.SyncEntity;
import mirrormonkey.rpc.member.RpcMethodData;
import mirrormonkey.rpc.proxy.EntityProxy;
import mirrormonkey.rpc.tools.listeners.MockRpcListener;
import mirrormonkey.rpc.tools.result.entities.NoInboundPositionRequestEntity;
import mirrormonkey.rpc.tools.result.entities.PositionRequestEntity;
import mirrormonkey.rpc.tools.result.listeners.SetTranslationListener;
import mirrormonkey.rpc.tools.result.specs.PositionRequestable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.jme3.math.Vector3f;
import com.jme3.network.MessageConnection;

public class ResultListeningTest extends SingleClientTestProto {

    public MockRpcListener resultListener;

    public Random r;

    public PositionRequestEntity serverBox;

    public PositionRequestEntity clientBox;

    public EntityProxy<PositionRequestable> serverProxy;

    public EntityProxy<PositionRequestable> clientProxy;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        resultListener = new MockRpcListener();
        r = new Random();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        resultListener = null;
        r = null;
        serverBox = null;
        clientBox = null;
        serverProxy = null;
        clientProxy = null;
    }

    public void createBox() {
        serverBox = new PositionRequestEntity(Vector3f.ZERO);
        serverProxy = serverApplication.rpcModule.createRpcProxy(PositionRequestable.class);
        serverProxy.addTargetEntity(serverBox);
        serverProxy.addMessageConnection(getFirstConnection());
        serverApplication.coreModule.addMapping(getFirstConnection(), serverBox, PositionRequestEntity.class, Vector3f.ZERO);
        sleepNoInterrupt(100);
        processEvents();
        clientBox = (PositionRequestEntity) clientApplication.appState.entityProvider.getData(serverBox.getData().id).entity;
        clientProxy = clientApplication.rpcModule.createRpcProxy(PositionRequestable.class);
        clientProxy.addTargetEntity(clientBox);
        clientProxy.addMessageConnection(clientApplication.appState.client);
        clientProxy.addRpcListener(resultListener);
        sleepNoInterrupt(100);
        processEvents();
    }

    public void checkResultReceived(MessageConnection source, Object result) {
        resultListener.checkResultArrived(source, result);
    }

    public void checkErrorArrived(MessageConnection source, String message) {
        resultListener.checkErrorArrived(source, message);
    }

    public void checkTimeoutArrived() {
        resultListener.checkTimeoutArrived();
    }

    public Vector3f createRandomVector() {
        return new Vector3f((2 * r.nextFloat() - 1), (2 * r.nextFloat() - 1), (2 * r.nextFloat() - 1));
    }

    @Test
    public void testResultListening() throws Exception {
        sleepNoInterrupt(1000);
        createBox();
        clientProxy.addRpcListener(new SetTranslationListener());
        for (int i = 0; i < 10; i++) {
            Vector3f newPos = createRandomVector();
            serverBox.setLocalTranslation(newPos);
            clientProxy.getCallTarget().getLocalTranslation();
            sleepNoInterrupt(100);
            processEvents();
            sleepNoInterrupt(100);
            processEvents();
            assertEquals(1, resultListener.recordedEvents.size());
            checkResultReceived(clientApplication.client, newPos);
            assertEquals(newPos, clientBox.getLocalTranslation());
            sleepNoInterrupt(400);
        }
        sleepNoInterrupt(1000);
    }

    /**
	 * Tests if everything still works if we have multiple messages queued up at
	 * the same time (which causes them to actually have different call ids).
	 * Also tests if reliable RPC happens in the expected order.
	 */
    @Test
    public void testMultiMessages() throws Exception {
        sleepNoInterrupt(1000);
        createBox();
        Vector3f last = null;
        final int vecCount = 10;
        for (int i = 0; i < vecCount; i++) {
            last = createRandomVector();
            serverBox.setLocalTranslation(last);
            clientProxy.getCallTarget().getLocalTranslation();
        }
        sleepNoInterrupt(10);
        processEvents();
        sleepNoInterrupt(10);
        processEvents();
        assertEquals(vecCount, resultListener.recordedEvents.size());
        int lastId = 1;
        for (int i = 0; i < vecCount; i++) {
            assertEquals(lastId++, resultListener.recordedEvents.peekFirst().call.id);
            checkResultReceived(clientApplication.client, last);
        }
        sleepNoInterrupt(1000);
    }

    @Test
    public void testNullResult() throws Exception {
        sleepNoInterrupt(1000);
        createBox();
        for (int i = 0; i < 5; i++) {
            clientProxy.getCallTarget().returnNull();
            sleepNoInterrupt(100);
            processEvents();
            sleepNoInterrupt(100);
            processEvents();
            assertEquals(1, resultListener.recordedEvents.size());
            checkResultReceived(clientApplication.client, null);
            sleepNoInterrupt(100);
        }
        sleepNoInterrupt(1000);
    }

    @Test
    public void testResultError() throws Exception {
        sleepNoInterrupt(1000);
        createBox();
        for (int i = 0; i < 1; i++) {
            String msg = "" + i;
            clientProxy.getCallTarget().throwSomeError(msg);
            sleepNoInterrupt(100);
            processEvents();
            sleepNoInterrupt(100);
            processEvents();
            assertEquals(1, resultListener.recordedEvents.size());
            checkErrorArrived(clientApplication.client, msg);
            sleepNoInterrupt(100);
        }
        sleepNoInterrupt(1000);
    }

    @Test
    public void testResultTimeout() throws Exception {
        sleepNoInterrupt(1000);
        createBox();
        for (int i = 0; i < 1; i++) {
            clientProxy.getCallTarget().justReturn();
            sleepNoInterrupt(1000);
            assertEquals(1, resultListener.recordedEvents.size());
            checkTimeoutArrived();
            processEvents();
            sleepNoInterrupt(100);
            processEvents();
            assertTrue(resultListener.recordedEvents.isEmpty());
            sleepNoInterrupt(100);
        }
        sleepNoInterrupt(1000);
    }

    @Test
    public void testDisallowInboundError() throws Exception {
        sleepNoInterrupt(1000);
        serverBox = new NoInboundPositionRequestEntity(Vector3f.ZERO);
        serverProxy = serverApplication.rpcModule.createRpcProxy(PositionRequestable.class);
        serverProxy.addTargetEntity(serverBox);
        serverProxy.addMessageConnection(getFirstConnection());
        serverApplication.coreModule.addMapping(getFirstConnection(), serverBox, PositionRequestEntity.class, Vector3f.ZERO);
        sleepNoInterrupt(100);
        processEvents();
        clientBox = (PositionRequestEntity) clientApplication.appState.entityProvider.getData(serverBox.getData().id).entity;
        clientProxy = clientApplication.rpcModule.createRpcProxy(PositionRequestable.class);
        clientProxy.addTargetEntity(clientBox);
        clientProxy.addMessageConnection(clientApplication.appState.client);
        clientProxy.addRpcListener(resultListener);
        clientProxy.getCallTarget().returnNull();
        sleepNoInterrupt(10);
        processEvents();
        sleepNoInterrupt(10);
        processEvents();
        assertEquals(1, resultListener.recordedEvents.size());
        checkErrorArrived(clientApplication.client, RpcMethodData.DISALLOWED_INBOUND_ERROR);
        sleepNoInterrupt(1000);
    }

    public void checkEntityArrived(SyncEntity entity) {
        resultListener.checkResultSame(clientApplication.client, entity);
    }

    @Test
    public void testIdentityAwareResult() throws Exception {
        sleepNoInterrupt(1000);
        createBox();
        clientProxy.getCallTarget().returnThis();
        sleepNoInterrupt(100);
        processEvents();
        sleepNoInterrupt(100);
        processEvents();
        assertEquals(1, resultListener.recordedEvents.size());
        checkEntityArrived(clientBox);
        sleepNoInterrupt(1000);
    }

    @Test
    public void testInterfaceBasedIdentityAwareResult() throws Exception {
        sleepNoInterrupt(1000);
        createBox();
        clientProxy.getCallTarget().returnThisByInterface();
        sleepNoInterrupt(100);
        processEvents();
        sleepNoInterrupt(100);
        processEvents();
        assertEquals(1, resultListener.recordedEvents.size());
        checkEntityArrived(clientBox);
        sleepNoInterrupt(1000);
    }

    @Test
    public void testUnknownIdentityAwareResult() throws Exception {
        sleepNoInterrupt(1000);
        createBox();
        clientProxy.getCallTarget().returnUnknownEntity();
        sleepNoInterrupt(100);
        processEvents();
        sleepNoInterrupt(100);
        processEvents();
        assertEquals(1, resultListener.recordedEvents.size());
        checkEntityArrived(null);
        sleepNoInterrupt(1000);
    }

    @Test
    public void testNullIdentityAwareResult() throws Exception {
        sleepNoInterrupt(1000);
        createBox();
        clientProxy.getCallTarget().returnNull();
        sleepNoInterrupt(100);
        processEvents();
        sleepNoInterrupt(100);
        processEvents();
        assertEquals(1, resultListener.recordedEvents.size());
        checkEntityArrived(null);
        sleepNoInterrupt(1000);
    }
}
