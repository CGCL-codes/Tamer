package org.vardb.web.selenium;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(ManagedSeleniumServerSuite.class)
@SuiteClasses({ UnmanagedAllBrowsersTest.class })
public class ManagedAllBrowsersSuiteTest {
}
