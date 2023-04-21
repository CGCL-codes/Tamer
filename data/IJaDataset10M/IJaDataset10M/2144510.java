package org.hitchhackers.tools.jmx.commands.dto;

import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;

public class ObjectNameAttributeNameTest {

    @Test
    @Ignore
    public void testObjectNameWithSlash() {
        ObjectNameAttributeName objectNameAttributeName = new ObjectNameAttributeName("org.hitchhackers.tools.jmy:type=SimpleMBean2,FunnyName=//contains/some/slashes/SomeValue");
        assertEquals("org.hitchhackers.tools.jmy:type=SimpleMBean2,FunnyName=//contains/some/slashes", objectNameAttributeName.getObjectName());
        assertEquals("SomeValue", objectNameAttributeName.getAttributeName());
    }
}
