package net.sf.joafip.java.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import net.sf.joafip.AbstractJoafipCommonTestCase;
import net.sf.joafip.DoNotTransform;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.TestException;
import net.sf.joafip.logger.JoafipLogger;
import net.sf.joafip.meminspector.service.MemInspectorException;
import net.sf.joafip.meminspector.service.inspect.MemInspector;

@NotStorableClass
@DoNotTransform
public class TestJavaUtilMemoryLeak extends AbstractJoafipCommonTestCase {

    private static final String OBJECTS_STAY_IN_MEMORY = "objects stay in memory";

    private static final JoafipLogger _log = JoafipLogger.getLogger(TestJavaUtilMemoryLeak.class);

    private MemInspector memInspector;

    public TestJavaUtilMemoryLeak() throws TestException {
        super();
    }

    public TestJavaUtilMemoryLeak(final String name) throws TestException {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        memInspector = new MemInspector();
    }

    @Override
    protected void tearDown() throws Exception {
        memInspector = null;
        super.tearDown();
    }

    public void testPLinkedHashSetMemoryLeak1() throws MemInspectorException, FileNotFoundException {
        final PLinkedHashSet<String> set = new PLinkedHashSet<String>();
        memInspector.inspect(set, false);
        set.add("b");
        set.remove("b");
        memInspector.inspect(set, true);
        if (memInspector.added()) {
            final PrintStream stream = new PrintStream("logs" + File.separator + "log");
            memInspector.log(stream);
            _log.fatal(OBJECTS_STAY_IN_MEMORY);
            fail(OBJECTS_STAY_IN_MEMORY);
            stream.close();
        }
    }

    public void testPLinkedHashSetMemoryLeak2() throws MemInspectorException, FileNotFoundException {
        final PLinkedHashSet<String> set = new PLinkedHashSet<String>();
        memInspector.inspect(set, false);
        set.add("b");
        set.clear();
        memInspector.inspect(set, true);
        if (memInspector.added()) {
            final PrintStream stream = new PrintStream("logs" + File.separator + "log");
            memInspector.log(stream);
            _log.fatal(OBJECTS_STAY_IN_MEMORY);
            fail(OBJECTS_STAY_IN_MEMORY);
            stream.close();
        }
    }
}
