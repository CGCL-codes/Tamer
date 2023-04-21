package il.ac.tau.dbcourse.test.db;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ConnectTest.class, PersistenceTest.class, ObjectMappingTest.class, WordIndexTest.class })
public class AllTests {
}
