package net.ep.db4o.activator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import java.io.File;
import java.io.IOException;
import java.util.List;
import net.ep.db4o.javassist.testclasses.BrownBag;
import net.ep.db4o.javassist.testclasses.LunchItem;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.collections.ArrayList4;
import com.db4o.config.Configuration;
import com.db4o.config.QueryEvaluationMode;
import com.db4o.diagnostic.DiagnosticToConsole;
import com.db4o.events.EventRegistry;
import com.db4o.events.EventRegistryFactory;
import com.db4o.internal.InternalObjectContainer;
import com.db4o.internal.query.Db4oQueryExecutionListener;
import com.db4o.internal.query.NQOptimizationInfo;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

public class QueryTest {

    private static Configuration configureTA(boolean enable) {
        Configuration configuration = Db4o.newConfiguration();
        configuration.activationDepth(1);
        configuration.queries().evaluationMode(QueryEvaluationMode.LAZY);
        if (enable) configuration.add(ProxyActivationSupport.create());
        configuration.lockDatabaseFile(false);
        configuration.diagnostic().addListener(new DiagnosticToConsole());
        return configuration;
    }

    private File tmpFile;

    @AfterClass
    public void cleanup() {
        if (tmpFile != null && !tmpFile.delete()) tmpFile.deleteOnExit();
        tmpFile = null;
    }

    public void createDBIfNecessary(int instances) {
        if (tmpFile != null) return;
        Configuration conf = configureTA(false);
        try {
            tmpFile = File.createTempFile("db4o", ".db");
            ObjectContainer db = Db4o.openFile(conf, tmpFile.getAbsolutePath());
            System.out.println("creating db " + tmpFile.getAbsolutePath() + " with " + instances + " instances");
            BrownBag bag = new BrownBag("erik");
            List<LunchItem> items = new ArrayList4<LunchItem>();
            for (int il = 0; il < instances; il++) {
                items.add(new LunchItem(bag, "stirfry" + il));
            }
            bag.setItems(items);
            db.set(bag);
            bag = new BrownBag("stacey");
            items = new ArrayList4<LunchItem>();
            for (int il = 0; il < instances; il++) {
                items.add(new LunchItem(bag, "stirfry" + il));
            }
            bag.setItems(items);
            db.set(bag);
            db.commit();
            db.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void proxyPerformanceAnalysisTest() {
        proxyPerformanceAnalysisTest(3000);
    }

    private void proxyPerformanceAnalysisTest(int instances) {
        createDBIfNecessary(instances);
        ObjectContainer db = Db4o.openFile(configureTA(true), tmpFile.getAbsolutePath());
        db = Db4o.openFile(configureTA(true), tmpFile.getAbsolutePath());
        EventRegistry registry = EventRegistryFactory.forObjectContainer(db);
        InternalObjectContainer idb = (InternalObjectContainer) db;
        idb.getNativeQueryHandler().addListener(new Db4oQueryExecutionListener() {

            public void notifyQueryExecuted(NQOptimizationInfo info) {
                System.err.println("msg=" + info.message());
                System.err.println("optimized=" + info.optimized());
            }
        });
        Query qr = db.query();
        qr.constrain(BrownBag.class);
        final BrownBag bag1 = (BrownBag) qr.execute().iterator().next();
        assertNotNull(bag1);
        assertEquals(bag1.getOwner(), "erik");
        int total = 0;
        long startTime = System.currentTimeMillis();
        List<LunchItem> items = db.query(new Predicate<LunchItem>() {

            @Override
            public boolean match(LunchItem candidate) {
                return candidate.getParent() == bag1;
            }
        });
        for (LunchItem it : items) {
            total++;
        }
        assertEquals(total, instances);
        long endTime = System.currentTimeMillis();
        long durationNQ = endTime - startTime;
        startTime = System.currentTimeMillis();
        qr = db.query();
        qr.constrain(LunchItem.class);
        qr.descend("_parent").constrain(bag1).identity();
        items = qr.execute();
        total = 0;
        for (LunchItem it : items) {
            total++;
        }
        endTime = System.currentTimeMillis();
        assertEquals(total, instances);
        long durationNoTA = endTime - startTime;
        db.close();
        System.out.println("with NQ= " + durationNQ);
        System.out.println("with SODA= " + durationNoTA);
        cleanup();
    }
}
