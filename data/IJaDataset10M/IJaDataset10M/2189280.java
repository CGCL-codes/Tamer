package net.grinder.engine.process.instrumenter.dcr;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import net.grinder.engine.process.Instrumenter;
import net.grinder.testutility.RandomStubFactory;
import net.grinder.util.weave.agent.ExposeInstrumentation;

/**
 * Unit tests for {@link DCRInstrumenterFactory}.
 *
 * @author Philip Aston
 * @version $Revision:$
 */
public class TestDCRInstrumenterFactory extends TestCase {

    private Instrumentation m_originalInstrumentation;

    @Override
    protected void setUp() throws Exception {
        m_originalInstrumentation = ExposeInstrumentation.getInstrumentation();
    }

    @Override
    protected void tearDown() throws Exception {
        ExposeInstrumentation.premain("", m_originalInstrumentation);
    }

    private final RandomStubFactory<Instrumentation> m_instrumentationStubFactory = RandomStubFactory.create(Instrumentation.class);

    private final Instrumentation m_instrumentation = m_instrumentationStubFactory.getStub();

    public void testCreateWithNoInstrumentation() throws Exception {
        ExposeInstrumentation.premain("", null);
        assertNull(DCRInstrumenterFactory.createFactory());
    }

    public void testCreateWithNoRetransformation() throws Exception {
        ExposeInstrumentation.premain("", m_instrumentation);
        m_instrumentationStubFactory.setResult("isRetransformClassesSupported", false);
        assertNull(DCRInstrumenterFactory.createFactory());
        m_instrumentationStubFactory.setThrows("isRetransformClassesSupported", new NoSuchMethodError());
        assertNull(DCRInstrumenterFactory.createFactory());
    }

    public void testAddJavaInstrumentation() throws Exception {
        final DCRInstrumenterFactory factory = DCRInstrumenterFactory.createFactory();
        final List<Instrumenter> instrumenters = new ArrayList<Instrumenter>();
        final boolean result = factory.addJavaInstrumenter(instrumenters);
        assertTrue(result);
        assertEquals(1, instrumenters.size());
        assertEquals("byte code transforming instrumenter for Java", instrumenters.get(0).getDescription());
    }

    public void testAddJythonInstrumentation() throws Exception {
        final DCRInstrumenterFactory factory = DCRInstrumenterFactory.createFactory();
        final List<Instrumenter> instrumenters = new ArrayList<Instrumenter>();
        final boolean result = factory.addJythonInstrumenter(instrumenters);
        assertTrue(result);
        assertEquals(1, instrumenters.size());
        assertEquals("byte code transforming instrumenter for Jython 2.1/2.2", instrumenters.get(0).getDescription());
    }

    public void testAddAllInstrumentation() throws Exception {
        final DCRInstrumenterFactory factory = DCRInstrumenterFactory.createFactory();
        final List<Instrumenter> instrumenters = new ArrayList<Instrumenter>();
        final boolean result = factory.addJythonInstrumenter(instrumenters);
        assertTrue(result);
        final boolean result2 = factory.addJavaInstrumenter(instrumenters);
        assertTrue(result2);
        assertEquals(2, instrumenters.size());
    }

    public void testWithBadAdvice() throws Exception {
        try {
            new DCRInstrumenterFactory(m_instrumentation, TestDCRInstrumenterFactory.class, RecorderLocator.getRecorderRegistry());
            fail("Expected AssertionError");
        } catch (AssertionError e) {
        }
    }
}
