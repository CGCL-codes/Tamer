package com.samskivert.depot;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests row counting.
 */
public class CountTest extends TestBase {

    @Test
    public void testCount() {
        for (int ii = 1; ii < 100; ii++) {
            _repo.insert(createTestRecord(ii));
        }
        assertEquals(99, _repo.from(TestRecord.class).selectCount());
        assertEquals(49, _repo.from(TestRecord.class).where(TestRecord.RECORD_ID.lessThan(50)).selectCount());
        _repo.from(TestRecord.class).whereTrue().delete();
        assertEquals(0, _repo.from(TestRecord.class).where(TestRecord.RECORD_ID.lessThan(50)).selectCount());
        assertEquals(0, _repo.from(TestRecord.class).selectCount());
    }

    protected TestRepository _repo = createTestRepository();
}
