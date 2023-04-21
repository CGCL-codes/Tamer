package org.happy.collections.ver1x1;

import org.happy.collections.util.comperators.Comperator1x0Test;
import org.happy.collections.ver1x1.decorators.AllTests_For_Collections;
import org.happy.collections.ver1x1.iterators.ConditionIterator1x0Test;
import org.happy.collections.ver1x1.lists.decorators.AllTests_For_Lists;
import org.happy.collections.ver1x1.maps.decorators.AllTests_For_Maps;
import org.happy.collections.ver1x1.util.UnmodifiableStrategyTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ UnmodifiableStrategyTest.class, AllTests_For_Collections.class, AllTests_For_Lists.class, AllTests_For_Maps.class, Comperator1x0Test.class, Collections_1x0Test.class, ConditionIterator1x0Test.class })
public class AllTests_1x1 {
}
