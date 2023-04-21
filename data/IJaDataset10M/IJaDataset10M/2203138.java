package joptsimple;

import java.util.Collections;
import java.util.List;
import static java.util.Collections.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:pholser@alumni.rice.edu">Paul Holser</a>
 */
public class PopulatedOptionSetTest {

    private OptionSet populated;

    @Before
    public void setUp() {
        populated = new OptionSet(Collections.<String, List<?>>emptyMap());
        populated.add(new NoArgumentOptionSpec("a"));
        populated.addWithArgument(new RequiredArgumentOptionSpec<String>("b"), "arg-of-b");
    }

    @Test
    public void hasArgument() {
        assertFalse(populated.hasArgument("a"));
        assertTrue(populated.hasArgument("b"));
    }

    @Test
    public void valueOf() {
        assertNull(populated.valueOf("a"));
        assertEquals("arg-of-b", populated.valueOf("b"));
    }

    @Test
    public void valuesOf() {
        assertEquals(emptyList(), populated.valuesOf("a"));
        assertEquals(singletonList("arg-of-b"), populated.valuesOf("b"));
    }

    @Test
    public void hasOptions() {
        assertTrue(populated.hasOptions());
    }
}
