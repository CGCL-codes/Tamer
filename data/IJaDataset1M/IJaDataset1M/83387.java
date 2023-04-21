package org.examcity.webtest.util;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.server.SeleniumServer;
import org.testng.annotations.BeforeSuite;
import java.util.Locale;
import java.net.URL;
import java.io.File;
import com.thoughtworks.selenium.HttpCommandProcessor;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.CommandProcessor;

public class AbstractWebTests {

    private Log logger = LogFactory.getLog(AbstractWebTests.class);

    private static JettyRunner container;

    protected static ConfigurableApplicationContext applicationContext;

    protected MessageSource theMessageSource;

    protected static Selenium selenium;

    protected static SeleniumServer seleniumServer;

    protected static CommandProcessor commandProcessor;

    private String urlWithContext;

    private static String browser = "*firefox";

    @BeforeSuite
    public void setUpSuite() throws Exception {
        logger.debug("Start: setUpSuite");
        Locale.setDefault(Locale.US);
        applicationContext = new ClassPathXmlApplicationContext(getConfigLocations());
        applicationContext.getBeanFactory().autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
        if (container == null) {
            container = new JettyRunner();
            container.run();
        }
        seleniumServer = new SeleniumServer();
        SeleniumServer.setAvoidProxy(true);
        seleniumServer.start();
        urlWithContext = container.getUrl();
        commandProcessor = new HttpCommandProcessor("localhost", 4444, browser, urlWithContext);
        selenium = new DefaultSelenium(commandProcessor);
        selenium.start();
    }

    public void tearDownSuite() throws Exception {
        logger.debug("Start: tearDownSuite");
        if (container != null) {
            try {
                container.stop();
            } catch (Throwable e) {
                logger.error(e);
            }
        }
        container = null;
        logger.debug("End: tearDownSuite");
    }

    protected String[] getConfigLocations() {
        return new String[] { "classpath*:/**/context-*.xml", "classpath*:/**/test/resources/context-*.xml" };
    }
}
