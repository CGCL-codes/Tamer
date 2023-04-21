package org.happy.collections;

import org.happy.collections.decorators.BufferedCollection_1x0Test;
import org.happy.collections.decorators.CacheCollection_1x0Test;
import org.happy.collections.decorators.CollectionDecorator_1x0Test;
import org.happy.collections.decorators.EventCollection_1x0Test;
import org.happy.collections.decorators.SynchronizedCollection_1x2Test;
import org.happy.collections.decorators.UnmodifiableCollection_1x0Test01;
import org.happy.collections.decorators.UnmodifiableCollection_1x0Test02;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * all tests for collections decorators
 * @author Andreas Hollmann
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ BufferedCollection_1x0Test.class, CacheCollection_1x0Test.class, CollectionDecorator_1x0Test.class, EventCollection_1x0Test.class, UnmodifiableCollection_1x0Test01.class, UnmodifiableCollection_1x0Test02.class, EmptyCollection_1x0Test.class, SynchronizedCollection_1x2Test.class })
public class AllTests_For_Collections {
}
