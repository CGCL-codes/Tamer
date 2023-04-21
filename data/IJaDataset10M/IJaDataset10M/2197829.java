package MyRemoteSorter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GeneratorTest.class, JoinTest.class, QuickSortTest.class, ServerTest.class, SortClientTest.class, SorterTest.class, SortFactoryTest.class, SpliceTest.class, AutoTest.class })
public class AllTests {
}
