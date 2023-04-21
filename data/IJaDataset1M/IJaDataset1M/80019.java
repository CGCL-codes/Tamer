package mirrormonkey.rpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import mirrormonkey.rpc.member.RpcMethodData;
import mirrormonkey.rpc.proxy.EntityProxy;
import mirrormonkey.rpc.tools.calltimeout.entities.CallTimeoutEntity;
import mirrormonkey.rpc.tools.calltimeout.specs.CallTimeout;
import mirrormonkey.rpc.tools.listeners.MockRpcListener;
import org.junit.Test;

/**
 * Contains test cases that check whether the callTimeout attribute on
 * <tt>RpcTarget</tt> works as intended
 * 
 * @author Philipp Christian Loewner
 * 
 */
public class CallTimeoutTest extends SingleClientTestProto {

    @Test
    public void testCallTimeout() {
        sleepNoInterrupt(1000);
        MockRpcListener resultListener = new MockRpcListener();
        CallTimeoutEntity serverEntity = new CallTimeoutEntity();
        EntityProxy<CallTimeout> serverProxy = serverApplication.rpcModule.createRpcProxy(CallTimeout.class);
        serverProxy.addTargetEntity(serverEntity);
        serverProxy.addMessageConnection(getFirstConnection());
        serverProxy.addRpcListener(resultListener);
        serverApplication.coreModule.addMapping(getFirstConnection(), serverEntity, CallTimeoutEntity.class);
        sleepNoInterrupt(100);
        processEvents();
        CallTimeoutEntity clientEntity = (CallTimeoutEntity) clientApplication.appState.entityProvider.getData(serverEntity.getData().id).entity;
        sleepNoInterrupt(100);
        processEvents();
        sleepNoInterrupt(100);
        processEvents();
        sleepNoInterrupt(100);
        processEvents();
        sleepNoInterrupt(100);
        processEvents();
        serverProxy.getCallTarget().shouldTimeOut();
        sleepNoInterrupt(100);
        processEvents();
        sleepNoInterrupt(100);
        processEvents();
        assertFalse(clientEntity.called);
        assertEquals(1, resultListener.recordedEvents.size());
        resultListener.checkErrorArrived(getFirstConnection(), RpcMethodData.REMOTE_TIMEOUT_ERROR);
        sleepNoInterrupt(1000);
    }
}
