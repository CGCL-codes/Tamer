package org.apache.velocity.test;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;
import org.apache.velocity.test.provider.ForeachMethodCallHelper;

/**
 * This class tests the Foreach loop.
 *
 * @author Daniel Rall
 * @author <a href="mailto:wglass@apache.org">Will Glass-Husain</a>
 */
public class ForeachTestCase extends TestCase {

    private VelocityContext context;

    public ForeachTestCase(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        Velocity.setProperty(RuntimeConstants.MAX_NUMBER_LOOPS, new Integer(3));
        Velocity.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, NullLogChute.class.getName());
        Velocity.init();
        context = new VelocityContext();
    }

    /**
     * Tests limiting of the number of loop iterations.
     */
    public void testMaxNbrLoopsConstraint() throws Exception {
        StringWriter writer = new StringWriter();
        String template = "#foreach ($item in [1..10])$item #end";
        Velocity.evaluate(context, writer, "test", template);
        assertEquals("Max number loops not enforced", "1 2 3 ", writer.toString());
    }

    /**
     * Tests proper method execution during a Foreach loop with items
     * of varying classes.
     */
    public void testMethodCall() throws Exception {
        List col = new ArrayList();
        col.add(new Integer(100));
        col.add("STRVALUE");
        context.put("helper", new ForeachMethodCallHelper());
        context.put("col", col);
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "test", "#foreach ( $item in $col )$helper.getFoo($item) " + "#end");
        assertEquals("Method calls while looping over varying classes failed", "int 100 str STRVALUE ", writer.toString());
    }
}
