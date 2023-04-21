package net.sf.lucis.core.impl;

import static net.sf.lucis.core.impl.DocumentSupport.document;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import net.sf.lucis.core.Adder;
import net.sf.lucis.core.Delays;
import net.sf.lucis.core.FullIndexer;
import net.sf.lucis.core.Queryable;
import net.sf.lucis.core.support.Queryables;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests for ReindexingIndexerService.
 * @author Andres Rodriguez
 */
public class ReindexingManagedIndexerServiceTest extends AbstractDirectoryTest {

    private String checkpoint = null;

    private ReindexingFSStore store;

    private DefaultReindexingWriter writer;

    private Queryable queryable;

    private ReindexingIndexerService service = null;

    @BeforeClass
    public void init() {
        store = new ReindexingFSStore(getIndexDir());
        assertNotNull(store);
        queryable = Queryables.managed(store);
        assertNotNull(queryable);
        writer = new DefaultReindexingWriter();
    }

    private void check() {
        assertEquals(DocumentSupport.count(queryable), 1000);
        DocumentSupport.found(queryable, 353);
        DocumentSupport.notFound(queryable, 35300);
    }

    @Test
    public void create() {
        service = new ReindexingIndexerService(store, writer, new Indexer());
        service.setDelays(Delays.constant(10L));
        assertNotNull(service);
        service.start();
    }

    private long cp() {
        String s = store.getCheckpoint();
        if (s == null) {
            return 0;
        }
        return Long.parseLong(s);
    }

    @Test(dependsOnMethods = "create", timeOut = 60000L)
    public void test() throws InterruptedException {
        long cp = 0;
        do {
            Thread.sleep(50L);
            cp = cp();
            if (cp > 1) {
                check();
            }
        } while (cp < 20);
    }

    @Test(dependsOnMethods = "test")
    public void shutdown() {
        service.stop();
    }

    private class Indexer implements FullIndexer {

        private int version = 0;

        public void index(Adder adder) throws InterruptedException {
            version++;
            checkpoint = Integer.toString(version);
            for (int i = 0; i < 1000; i++) {
                adder.add(document(i));
            }
            adder.setCheckpoint(checkpoint);
        }
    }
}
