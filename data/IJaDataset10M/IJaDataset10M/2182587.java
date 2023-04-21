package com.yawnefpark.scorekeeper.state;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ NoOutsBasesEmptyUnitTest.class, NoOutsBasesLoadedUnitTest.class, NoOutsOnFirstUnitTest.class, NoOutsOnFirstSecondUnitTest.class })
public class AllStateTestSuite {
}
