package org.xsocket.connection;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import org.junit.Assert;
import org.junit.Test;
import org.xsocket.ILifeCycle;
import org.xsocket.QAUtil;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.INonBlockingConnection;
import org.xsocket.connection.IServer;
import org.xsocket.connection.Server;
import org.xsocket.connection.ConnectionUtils;

/**
*
* @author grro@xsocket.org
*/
public final class LifeCycleTest {

    @Test
    public void testInstanceScoped() throws Exception {
        InstanceTestHandler instanceTestHdl = new InstanceTestHandler();
        IServer server = new Server(instanceTestHdl);
        ConnectionUtils.start(server);
        Assert.assertTrue(instanceTestHdl.isInitCalled());
        Assert.assertFalse(instanceTestHdl.isDestroyedCalled());
        server.close();
        QAUtil.sleep(200);
        Assert.assertTrue("init for old handler should have been called", instanceTestHdl.isInitCalled());
        Assert.assertTrue("destroyed for old handler should have been called", instanceTestHdl.isDestroyedCalled());
    }

    private static final class InstanceTestHandler implements IDataHandler, ILifeCycle {

        private boolean initCalled = false;

        private boolean detroyCalled = false;

        public void onInit() {
            initCalled = true;
        }

        public void onDestroy() {
            detroyCalled = true;
        }

        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException {
            return true;
        }

        public boolean isInitCalled() {
            return initCalled;
        }

        public boolean isDestroyedCalled() {
            return detroyCalled;
        }
    }
}
