package org.jcvi.glk.hibernate;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestPositionsUserType.class, TestQualitiesUserType.class, TestEUIDBigDecimalUserType.class, TestEUIDStringUserType.class, AllTrashUserTypeUnitTests.class })
public class AllUserTypeUnitTests {
}
