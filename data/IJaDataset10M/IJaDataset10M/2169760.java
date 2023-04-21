package net.sf.simplecq;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ RowTests.class, MutableRowSetTests.class, ContinuousQueryServiceTests1.class, DataSourceDefinitionServiceImplTests.class, MessagingEndpointServiceImplTests.class })
public class AllTests {
}
