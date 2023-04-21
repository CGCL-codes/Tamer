package com.androinject.core.test.factory;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.junit.Test;
import com.androinject.core.test.AiTestCase;
import com.androinject.test.components.TestComponentA;
import com.androinject.test.components.TestComponentB;
import com.androinject.test.components.TestComponentC;

public class GetTypeTest extends AiTestCase {

    @Test
    public void basicTest1() throws IOException {
        this.createFactoryFromXml("config/xml/getType.xml");
        assertEquals(TestComponentA.class, this.getType("componentA"));
        assertEquals(TestComponentB.class, this.getType("componentB"));
        assertEquals(TestComponentC.class, this.getType("componentC"));
    }

    @Test
    public void managedComponentType() throws IOException {
        this.createFactoryFromXml("config/xml/getType.xml");
        assertEquals(null, this.getType("managedA"));
    }

    @Test
    public void nonExistendComponentType() throws IOException {
        this.createFactoryFromXml("config/xml/getType.xml");
        assertEquals(null, this.getType("invalidId"));
    }
}
