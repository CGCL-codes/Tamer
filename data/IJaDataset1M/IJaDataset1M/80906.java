package org.t2framework.commons.ut;

import java.lang.reflect.Method;
import org.t2framework.commons.Constants;
import org.t2framework.commons.exception.NoSuchMethodRuntimeException;
import org.t2framework.commons.module.ModulesBootstrap;
import org.t2framework.commons.util.Reflections.MethodUtil;

/**
 * <#if locale="en">
 * <p>
 * Default {@link UtPlugin} class.Just delegate to {@link BaseTestCase}.
 * 
 * </p>
 * <#else>
 * <p>
 * 
 * </p>
 * </#if>
 * 
 * @author shot
 * 
 */
public class DefaultUtPlugin implements UtPlugin {

    protected BaseTestCase testCase;

    protected boolean verbose = false;

    public DefaultUtPlugin(BaseTestCase testCase) {
        this(testCase, false);
    }

    public DefaultUtPlugin(BaseTestCase testCase, boolean verbose) {
        this.testCase = testCase;
        this.verbose = verbose;
    }

    @Override
    public void begin() throws Throwable {
    }

    @Override
    public void setUp() throws Throwable {
        ModulesBootstrap.init();
        testCase.setUp();
    }

    @Override
    public void setUpEach() throws Throwable {
        setUpEach0();
    }

    @Override
    public void runTest() throws Throwable {
        testCase.runTest();
    }

    @Override
    public void handleRunTestThrowable(Throwable running) {
    }

    @Override
    public void handleTearDownThrowable(Throwable tearingDown) {
    }

    @Override
    public void tearDownEach() throws Throwable {
        tearDownEach0();
    }

    protected void setUpEach0() throws Throwable {
        String targetName = getTargetName();
        if (targetName.length() > 0) {
            invoke("setUp" + targetName);
        }
    }

    protected void tearDownEach0() throws Throwable {
        String targetName = getTargetName();
        if (targetName.length() > 0) {
            invoke("tearDown" + targetName);
        }
    }

    protected String getTargetName() {
        return testCase.getName().substring("test".length());
    }

    protected Object invoke(String methodName) throws Throwable {
        try {
            Method method = MethodUtil.getDeclaredMethod(testCase.getClass(), methodName, Constants.EMPTY_CLASS_ARRAY);
            return MethodUtil.invoke(method, testCase, Constants.EMPTY_ARRAY);
        } catch (NoSuchMethodRuntimeException ignore) {
            return null;
        }
    }

    @Override
    public void tearDown() throws Throwable {
        testCase.tearDown();
        ModulesBootstrap.dispose();
    }

    @Override
    public void end() throws Throwable {
    }

    @Override
    public long beginMeasure() {
        return System.currentTimeMillis();
    }

    @Override
    public void endMeasure(long start) {
        long end = System.currentTimeMillis();
        String testname = testCase.getName();
        if (verbose) {
            System.out.println("[begin\t" + testname + "]" + (start) + "ms");
            System.out.println("[end\t" + testname + "]" + (end) + "ms");
        }
        System.out.println("[result\t" + testname + "]" + (end - start) + "ms");
    }
}
