package org.exoplatform.container;

import java.util.Collection;
import java.util.List;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.monitor.jvm.J2EEServerInfo;
import org.exoplatform.container.xml.Component;
import org.exoplatform.test.BasicTestCase;
import junit.framework.TestCase;

public class ExoContainerTest extends BasicTestCase {

    public void testRootContainer() {
        RootContainer rootContainer = RootContainer.getInstance();
        ConfigurationManager componentManager = (ConfigurationManager) rootContainer.getComponentInstanceOfType(ConfigurationManager.class);
        Collection<Component> components2 = componentManager.getComponents();
        System.out.println("\n\n\n ===========" + components2.size() + "===========\n\n\n");
        int counter = 0;
        for (Component comp : components2) {
            System.out.println("\n" + ++counter + " ==========> " + comp.getType());
        }
    }

    public void testPortalContainer() {
        PortalContainer portalContainer = PortalContainer.getInstance();
        ConfigurationManager componentManager = (ConfigurationManager) portalContainer.getComponentInstanceOfType(ConfigurationManager.class);
        Collection<Component> components2 = componentManager.getComponents();
        System.out.println("\n\n\n ===========" + components2.size() + "===========\n\n\n");
        int counter = 0;
        for (Component comp : components2) {
            System.out.println("\n" + ++counter + " ==========> " + comp.getType());
        }
    }
}
