package org.hip.kernel.bom.directory.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ LDAPAdapterTest.class, LDAPObjectHomeTest.class, LDAPObjectTest.class, LDAPQueryResultTest.class, LDAPQueryStatementTest.class })
public class AllTests {
}
