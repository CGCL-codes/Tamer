package net.chrisrichardson.umangite;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public abstract class LaunchOncePerSuiteWebTest extends WebTestSupport {

    @BeforeSuite
    @Override
    protected void startSeleniumAndWebApplication() throws Exception {
        super.startSeleniumAndWebApplication();
    }

    @AfterSuite
    public void stopSeleniumAndWebApplication() {
        super.stopSeleniumAndWebApplication();
    }
}
