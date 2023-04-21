package org.lightframework.mvc.internal.ognl;

import junit.framework.TestCase;
import static org.lightframework.mvc.internal.ognl.SimpleOgnl.*;

/**
 * <code>{@link TestSimple}</code>
 *
 * @author fenghm (fenghm@bingosoft.net)
 *
 * @since 1.1.0
 */
public class TestSimple extends TestCase {

    public void testSimpleOgnl() {
        assertEquals(null, parse("name"));
        assertEquals("name[1]", deparse(parse("name[1]")));
        assertEquals("name.name", deparse(parse("name.name")));
        assertEquals("name.name.name", deparse(parse("name.name.name")));
        assertEquals("name[0].name.name", deparse(parse("name[0].name.name")));
        assertEquals("name[0].name.name[name]", deparse(parse("name[0].name.name[name]")));
        assertEquals("name[name].name.name[1]", deparse(parse("name['name'].name.name[1]")));
        assertEquals("name[0].name[1].name[name]", deparse(parse("name[0].name[1].name[name]")));
    }
}
