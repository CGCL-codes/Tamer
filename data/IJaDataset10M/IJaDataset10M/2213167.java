package br.com.caelum.testslicer.registry;

import java.util.HashMap;
import java.util.Map;
import br.com.caelum.testslicer.log.LogWriter;
import br.com.caelum.testslicer.servlet.TestManager;

public class TestTraceController {

    private static final String AUTODETECT = "br.com.caelum.testslicer.autodetect";

    private static final boolean shouldNotAutoDetect = "false".equals(System.getProperty(AUTODETECT));

    private final TestManager manager;

    private Map<Thread, String> lastTest = new HashMap<Thread, String>();

    private final RegistryData data;

    private int depth = 0;

    private final LogWriter log;

    public TestTraceController(TestManager manager, RegistryData data, LogWriter log) {
        this.manager = manager;
        this.data = data;
        this.log = log;
    }

    public void control(String testName, String currentVisitedType) {
        String currentTestName = testName;
        if (shouldNotAutoDetect) {
            currentTestName = manager.getCurrentTestName(thread());
        }
        if (!isNewTest()) {
            currentTestName = lastTest.get(thread());
        } else if (isNewTest()) {
            lastTest.put(thread(), currentTestName);
        }
        data.registerAffects(currentVisitedType, currentTestName);
    }

    private boolean isNewTest() {
        return depth == 1;
    }

    public void control(String currentVisitedType) {
        String currentTestName = manager.getCurrentTestName(thread());
        if (!isNewTest()) {
            currentTestName = lastTest.get(thread());
        } else if (isNewTest()) {
            lastTest.put(thread(), currentTestName);
        }
        data.registerAffects(currentVisitedType, currentTestName);
    }

    public void increase() {
        depth++;
        log.debug("Increasing level to " + depth);
    }

    private Thread thread() {
        return Thread.currentThread();
    }

    public void decrease() {
        depth--;
        log.debug("Decreasing level to " + depth);
    }

    public void shutdown() {
        if (depth != 0) {
            System.err.println("Controller shutting down found depth != 0. This is a problem while navigating through your code. Report to testslicer developers.");
        }
    }
}
