package org.nakedobjects.metamodel.commons.lang;

import static org.junit.Assert.assertEquals;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class JavaClassUtilsTest {

    @SuppressWarnings("unused")
    private final Mockery context = new JUnit4Mockery();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void voidBuiltIns() throws ClassNotFoundException {
        assertEquals(JavaClassUtils.getBuiltIn("void"), void.class);
        assertEquals(JavaClassUtils.getBuiltIn("boolean"), boolean.class);
        assertEquals(JavaClassUtils.getBuiltIn("byte"), byte.class);
        assertEquals(JavaClassUtils.getBuiltIn("short"), short.class);
        assertEquals(JavaClassUtils.getBuiltIn("int"), int.class);
        assertEquals(JavaClassUtils.getBuiltIn("long"), long.class);
        assertEquals(JavaClassUtils.getBuiltIn("char"), char.class);
        assertEquals(JavaClassUtils.getBuiltIn("float"), float.class);
        assertEquals(JavaClassUtils.getBuiltIn("double"), double.class);
    }
}
