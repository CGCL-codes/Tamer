package net.sf.joafip.kvstore.record;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.kvstore.record.entity.KVRecordEntityTests;
import net.sf.joafip.kvstore.record.service.KVRecordServiceTests;

@NotStorableClass
public class KVRecordTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for kvstore.record");
        suite.addTest(KVRecordEntityTests.suite());
        suite.addTest(KVRecordServiceTests.suite());
        return suite;
    }
}
