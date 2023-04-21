package com.samskivert.depot;

import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the handling of our various supported field types.
 */
public class AllTypesTest extends TestBase {

    @Test
    public void testCreateReadDelete() {
        AllTypesRecord in = AllTypesRecord.createRecord(1);
        _repo.insert(in);
        AllTypesRecord out = _repo.loadNoCache(in.recordId);
        assertNotNull(out != null);
        assertTrue(in != out);
        assertEquals(in, out);
        _repo.delete(AllTypesRecord.getKey(in.recordId));
        assertNull(_repo.loadNoCache(in.recordId));
    }

    protected static class TestRepository extends DepotRepository {

        public AllTypesRecord loadNoCache(int recordId) {
            return load(AllTypesRecord.getKey(recordId), CacheStrategy.NONE);
        }

        public TestRepository(PersistenceContext perCtx) {
            super(perCtx);
        }

        @Override
        protected void getManagedRecords(Set<Class<? extends PersistentRecord>> classes) {
            classes.add(AllTypesRecord.class);
        }
    }

    protected TestRepository _repo = new TestRepository(createPersistenceContext());
}
