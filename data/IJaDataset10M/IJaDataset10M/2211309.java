package org.hibernate.search.test.worker;

import org.hibernate.cfg.Configuration;
import org.hibernate.search.Environment;
import org.apache.lucene.analysis.StopAnalyzer;

/**
 * @author Emmanuel Bernard
 */
public class SyncWorkerTest extends WorkerTestCase {

    protected void configure(Configuration cfg) {
        super.configure(cfg);
        cfg.setProperty("hibernate.search.default.directory_provider", "ram");
        cfg.setProperty(Environment.ANALYZER_CLASS, StopAnalyzer.class.getName());
        cfg.setProperty(Environment.WORKER_SCOPE, "transaction");
        cfg.setProperty(Environment.WORKER_PREFIX, "sync");
    }
}
