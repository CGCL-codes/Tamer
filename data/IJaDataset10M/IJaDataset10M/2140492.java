package de.fraunhofer.isst.axbench.timing.test.legacy;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import de.fraunhofer.isst.axbench.timing.test.FunctionTests;

@SuppressWarnings("deprecation")
@RunWith(Suite.class)
@Suite.SuiteClasses({ FunctionTests.class, PerformanceTests.class })
@Deprecated
public class AllTests {
}
