package org.hepotaiya.sample.main;

import org.zzz.hepotaiya.executor.BatchExecutor;
import org.zzz.hepotaiya.executor.SimpleExecutor;

public class SimpleMain {

    public static void main(String[] args) throws Exception {
        BatchExecutor executor = new SimpleExecutor();
        executor.doExecute("org.hepotaiya.sample.simple");
    }
}
