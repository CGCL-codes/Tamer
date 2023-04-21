package org.apache.zookeeper.test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.junit.Assert;
import org.junit.Test;

public class ClientRetry extends ClientBase {

    @Override
    public void setUp() throws Exception {
        maxCnxns = 1;
        super.setUp();
    }

    @Test
    public void testClientRetry() throws IOException, InterruptedException, TimeoutException {
        CountdownWatcher cdw1 = new CountdownWatcher();
        CountdownWatcher cdw2 = new CountdownWatcher();
        ZooKeeper zk = new ZooKeeper(hostPort, 10000, cdw1);
        try {
            cdw1.waitForConnected(CONNECTION_TIMEOUT);
            ZooKeeper zk2 = new ZooKeeper(hostPort, 10000, cdw2);
            try {
                States s1 = zk.getState();
                States s2 = zk2.getState();
                Assert.assertSame(s1, States.CONNECTED);
                Assert.assertSame(s2, States.CONNECTING);
                cdw1.reset();
                cdw1.waitForDisconnected(CONNECTION_TIMEOUT);
                cdw2.waitForConnected(CONNECTION_TIMEOUT);
                Assert.assertSame(zk2.getState(), States.CONNECTED);
            } finally {
                zk2.close();
            }
        } finally {
            zk.close();
        }
    }
}
