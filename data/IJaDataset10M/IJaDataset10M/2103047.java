package com.androinject.core.test.factory;

import java.io.IOException;
import org.junit.Test;
import com.androinject.core.test.AiTestCase;
import com.androinject.test.components.TestComponent;

public class InitializeManagedComponentTest extends AiTestCase {

    @Test
    public void testInitialization1() throws IOException {
        this.createFactoryFromXml("config/xml/initializeManagedComponent.xml");
        TestComponent managed1 = new TestComponent();
        TestComponent managed2 = new TestComponent();
        TestComponent managed3 = new TestComponent();
        this.initializeManagedComponent(managed1, "managed1");
        this.initializeManagedComponent(managed2, "managed2");
        this.initializeManagedComponent(managed3, "managed3");
        TestComponent a = this.getComponent("a", TestComponent.class);
        TestComponent b = this.getComponent("b", TestComponent.class);
        TestComponent c = this.getComponent("c", TestComponent.class);
        TestComponent ab = this.getComponent("ab", TestComponent.class);
        this.checkDependencies(managed1, null, null, null);
        this.checkDependencies(managed2, ab, c, null);
        this.checkDependencies(managed3, a, b, c);
    }

    @Test
    public void testInitialization2() throws IOException {
        this.createFactoryFromXml("config/xml/initializeManagedComponent.xml");
        TestComponent testComponent = new TestComponent();
        this.initializeManagedComponent(testComponent);
        TestComponent c = this.getComponent("c", TestComponent.class);
        TestComponent ab = this.getComponent("ab", TestComponent.class);
        this.checkDependencies(testComponent, ab, c, null);
    }
}
