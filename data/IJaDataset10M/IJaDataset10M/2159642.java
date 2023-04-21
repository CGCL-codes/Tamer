package org.ourgrid;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.ourgrid.acceptance.AllAcceptanceTests;

@RunWith(Suite.class)
@SuiteClasses({ AllUnitTests.class, AllAcceptanceTests.class })
public class AllFastTests {
}
