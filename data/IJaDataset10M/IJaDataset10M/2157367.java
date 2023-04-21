package tests.taskgraph.tasks.data;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AssignTest.class, AverageTest.class, ComposeTest.class, CountTest.class, GlobalUniqueTest.class, MergeTest.class, ReadArrayTest.class, ReadCollectionTest.class, ReadNativeArrayTest.class, ReplicateTest.class, SelectTest.class, SortTest.class, SplitTest.class, SumTest.class, UniqueTest.class, WriteArrayTest.class, WriteCollectionTest.class, WriteNativeArrayTest.class })
public class RunAllTests {
}
