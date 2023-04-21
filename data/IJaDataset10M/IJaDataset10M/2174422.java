package org.kunyit.tst;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ InsertSortedArrayTest.class, TwoItemTest.class, QuiteLongKeyTstTest.class, TstBasicSerialization.class, TooSmallTstTest.class, EmptyTstTest.class, TstBasicTest.class })
public class TstTestSuite {
}
