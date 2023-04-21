package org.t2framework.commons.ut;

/**
 * <#if locale="en">
 * <p>
 * UtPlugin is simple plugin for extending test case.
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
public interface UtPlugin {

    /**
	 * <#if locale="en">
	 * <p>
	 * Begin a test case.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @throws Throwable
	 */
    void begin() throws Throwable;

    /**
	 * <#if locale="en">
	 * <p>
	 * Set up for a test case.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @throws Throwable
	 */
    void setUp() throws Throwable;

    /**
	 * <#if locale="en">
	 * <p>
	 * Set up for each test method.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @throws Throwable
	 */
    void setUpEach() throws Throwable;

    /**
	 * <#if locale="en">
	 * <p>
	 * Begin measuring performance.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @return
	 */
    long beginMeasure();

    /**
	 * <#if locale="en">
	 * <p>
	 * Run test method.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @throws Throwable
	 */
    void runTest() throws Throwable;

    /**
	 * <#if locale="en">
	 * <p>
	 * End measurement.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param start
	 */
    void endMeasure(long start);

    /**
	 * <#if locale="en">
	 * <p>
	 * Handle if test throws {@link Throwable}.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param running
	 */
    void handleRunTestThrowable(Throwable running);

    /**
	 * <#if locale="en">
	 * <p>
	 * Handle if tearDown throws {@link Throwable}.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param tearingDown
	 */
    void handleTearDownThrowable(Throwable tearingDown);

    /**
	 * <#if locale="en">
	 * <p>
	 * Tear down for each method.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @throws Throwable
	 */
    void tearDownEach() throws Throwable;

    /**
	 * <#if locale="en">
	 * <p>
	 * Tear down a test case.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @throws Throwable
	 */
    void tearDown() throws Throwable;

    /**
	 * <#if locale="en">
	 * <p>
	 * End a test case.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @throws Throwable
	 */
    void end() throws Throwable;
}
