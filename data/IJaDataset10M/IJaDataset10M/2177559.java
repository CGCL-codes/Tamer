package org.sourceforge.jemm.database.remote;

import org.junit.Ignore;

@Ignore
public class StressTest {

    public static void main(String args[]) throws Exception {
        for (int i = 0; i < 100; i++) {
            RemoteDatabaseTest rdt = new RemoteDatabaseTest();
            rdt.setup();
            rdt.testRemoveLockAcquireListener();
            rdt.shutdown();
        }
    }
}
