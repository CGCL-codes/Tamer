package org.javatech.benchmark.sql;

import org.logicalcobwebs.proxool.ProxoolFacade;
import org.logicalcobwebs.proxool.admin.SnapshotIF;

public class ProxoolBenchmark extends AbstractConnectionPoolBenchmark {

    public void warmUp() throws Exception {
        for (int i = 0; i < 25; i++) doSimpleSqlStatement(proxoolDataSource);
    }

    public void runInternalTrial() throws Exception {
        doSimpleSqlStatement(this.proxoolDataSource);
    }

    public void afterTest() throws Exception {
        SnapshotIF snapshot = ProxoolFacade.getSnapshot("proxoolDataSource");
        System.out.println(" Proxool Open Connections: " + snapshot.getActiveConnectionCount());
    }
}
