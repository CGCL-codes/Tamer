package net.sf.brico.cmd.support.spring;

import net.sf.brico.util.AbstractMessageManagerTestCase;
import net.sf.brico.util.MessageManager;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class TestSpringEnabledMessageManager extends AbstractMessageManagerTestCase {

    public TestSpringEnabledMessageManager(String testName) {
        super(testName);
    }

    protected MessageManager getMessageManager() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setBasename(RESOURCE_FILE);
        MessageManager mm = new SpringEnabledMessageManager(ms);
        return mm;
    }
}
