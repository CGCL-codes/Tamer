package net.sf.spring.batch.sample;

import net.sf.spring.batch.BatchLauncher;
import net.sf.spring.batch.impl.DefaultLog4jConfigurator;
import net.sf.spring.batch.impl.DefaultPropertiesLoader;

public class MyBatchLauncher extends BatchLauncher {

    public static void main(String[] args) {
        MyBatchLauncher batchLauncher = new MyBatchLauncher();
        batchLauncher.setLog4jConfigurator(new DefaultLog4jConfigurator());
        batchLauncher.setPropertiesLoader(new DefaultPropertiesLoader());
        int result = batchLauncher.init(args);
        if (result == STOP) batchLauncher.exit(0);
        if (result == ERROR) batchLauncher.exit(-9999);
        result = batchLauncher.run();
        batchLauncher.exit(result);
    }
}
