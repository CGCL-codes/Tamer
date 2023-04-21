package joptsimple;

import static java.util.Collections.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:pholser@alumni.rice.edu">Paul Holser</a>
 */
public class LongOptionOptionalArgumentTest extends AbstractOptionParserFixture {

    @Before
    public final void initializeParser() {
        parser.accepts("output").withOptionalArg();
        parser.accepts("a");
    }

    @Test
    public void argumentMissingTrailedByAnotherOption() {
        OptionSet options = parser.parse("--output", "-a");
        assertTrue(options.has("output"));
        assertTrue(options.has("a"));
        assertEquals(emptyList(), options.valuesOf("output"));
        assertEquals(emptyList(), options.nonOptionArguments());
    }

    @Test
    public void argumentSeparate() {
        OptionSet options = parser.parse("--output", "opt");
        assertTrue(options.has("output"));
        assertEquals(singletonList("opt"), options.valuesOf("output"));
        assertEquals(emptyList(), options.nonOptionArguments());
    }

    @Test
    public void argumentTogether() {
        OptionSet options = parser.parse("--output=opt");
        assertTrue(options.has("output"));
        assertEquals(singletonList("opt"), options.valuesOf("output"));
        assertEquals(emptyList(), options.nonOptionArguments());
    }
}
