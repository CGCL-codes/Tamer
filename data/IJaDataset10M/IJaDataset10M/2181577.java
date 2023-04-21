package org.fest.swing.testng;

import java.lang.reflect.Method;
import org.testng.IClass;
import org.testng.ITestClass;
import org.testng.ITestNGMethod;

/**
 * Understands an <code>{@link ITestNGMethod}</code> stub for testing purposes.
 *
 * @author Alex Ruiz
 */
public class TestNGMethodStub implements ITestNGMethod {

    private static final long serialVersionUID = 1L;

    private String methodName;

    private Method method;

    /** @see org.testng.ITestNGMethod#addMethodDependedUpon(java.lang.String) */
    public void addMethodDependedUpon(String methodName) {
    }

    /** @see org.testng.ITestNGMethod#canRunFromClass(org.testng.IClass) */
    public boolean canRunFromClass(IClass testClass) {
        return false;
    }

    /** @see org.testng.ITestNGMethod#getAfterGroups() */
    public String[] getAfterGroups() {
        return null;
    }

    /** @see org.testng.ITestNGMethod#getBeforeGroups() */
    public String[] getBeforeGroups() {
        return null;
    }

    /** @see org.testng.ITestNGMethod#getCurrentInvocationCount() */
    public int getCurrentInvocationCount() {
        return 0;
    }

    /** @see org.testng.ITestNGMethod#getDate() */
    public long getDate() {
        return 0;
    }

    /** @see org.testng.ITestNGMethod#getDescription() */
    public String getDescription() {
        return null;
    }

    /** @see org.testng.ITestNGMethod#getGroups() */
    public String[] getGroups() {
        return null;
    }

    /** @see org.testng.ITestNGMethod#getGroupsDependedUpon() */
    public String[] getGroupsDependedUpon() {
        return null;
    }

    /** @see org.testng.ITestNGMethod#getId() */
    public String getId() {
        return null;
    }

    /** @see org.testng.ITestNGMethod#getInstanceHashCodes() */
    public long[] getInstanceHashCodes() {
        return null;
    }

    /** @see org.testng.ITestNGMethod#getInstances() */
    public Object[] getInstances() {
        return null;
    }

    /** @see org.testng.ITestNGMethod#getInvocationCount() */
    public int getInvocationCount() {
        return 0;
    }

    /** @see org.testng.ITestNGMethod#getMethod() */
    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    /** @see org.testng.ITestNGMethod#getMethodName() */
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /** @see org.testng.ITestNGMethod#getMethodsDependedUpon() */
    public String[] getMethodsDependedUpon() {
        return null;
    }

    /** @see org.testng.ITestNGMethod#getMissingGroup() */
    public String getMissingGroup() {
        return null;
    }

    /** @see org.testng.ITestNGMethod#getParameterInvocationCount() */
    public int getParameterInvocationCount() {
        return 0;
    }

    /** @see org.testng.ITestNGMethod#getRealClass() */
    public Class<?> getRealClass() {
        return null;
    }

    /** @see org.testng.ITestNGMethod#getSuccessPercentage() */
    public int getSuccessPercentage() {
        return 0;
    }

    /** @see org.testng.ITestNGMethod#getTestClass() */
    public ITestClass getTestClass() {
        return null;
    }

    /** @see org.testng.ITestNGMethod#getThreadPoolSize() */
    public int getThreadPoolSize() {
        return 0;
    }

    /** @see org.testng.ITestNGMethod#getTimeOut() */
    public long getTimeOut() {
        return 0;
    }

    /** @see org.testng.ITestNGMethod#incrementCurrentInvocationCount() */
    public void incrementCurrentInvocationCount() {
    }

    /** @see org.testng.ITestNGMethod#isAfterClassConfiguration() */
    public boolean isAfterClassConfiguration() {
        return false;
    }

    /** @see org.testng.ITestNGMethod#isAfterGroupsConfiguration() */
    public boolean isAfterGroupsConfiguration() {
        return false;
    }

    /** @see org.testng.ITestNGMethod#isAfterMethodConfiguration() */
    public boolean isAfterMethodConfiguration() {
        return false;
    }

    /** @see org.testng.ITestNGMethod#isAfterSuiteConfiguration() */
    public boolean isAfterSuiteConfiguration() {
        return false;
    }

    /** @see org.testng.ITestNGMethod#isAfterTestConfiguration() */
    public boolean isAfterTestConfiguration() {
        return false;
    }

    /** @see org.testng.ITestNGMethod#isAlwaysRun() */
    public boolean isAlwaysRun() {
        return false;
    }

    /** @see org.testng.ITestNGMethod#isBeforeClassConfiguration() */
    public boolean isBeforeClassConfiguration() {
        return false;
    }

    /** @see org.testng.ITestNGMethod#isBeforeGroupsConfiguration() */
    public boolean isBeforeGroupsConfiguration() {
        return false;
    }

    /** @see org.testng.ITestNGMethod#isBeforeMethodConfiguration() */
    public boolean isBeforeMethodConfiguration() {
        return false;
    }

    /** @see org.testng.ITestNGMethod#isBeforeSuiteConfiguration() */
    public boolean isBeforeSuiteConfiguration() {
        return false;
    }

    /** @see org.testng.ITestNGMethod#isBeforeTestConfiguration() */
    public boolean isBeforeTestConfiguration() {
        return false;
    }

    /** @see org.testng.ITestNGMethod#isTest() */
    public boolean isTest() {
        return false;
    }

    /** @see org.testng.ITestNGMethod#setDate(long) */
    public void setDate(long date) {
    }

    /** @see org.testng.ITestNGMethod#setId(java.lang.String) */
    public void setId(String arg0) {
    }

    /** @see org.testng.ITestNGMethod#setInvocationCount(int) */
    public void setInvocationCount(int arg0) {
    }

    /** @see org.testng.ITestNGMethod#setMissingGroup(java.lang.String) */
    public void setMissingGroup(String group) {
    }

    /** @see org.testng.ITestNGMethod#setParameterInvocationCount(int) */
    public void setParameterInvocationCount(int arg0) {
    }

    /** @see org.testng.ITestNGMethod#setTestClass(org.testng.ITestClass) */
    public void setTestClass(ITestClass cls) {
    }

    /** @see org.testng.ITestNGMethod#setThreadPoolSize(int) */
    public void setThreadPoolSize(int arg0) {
    }

    /** @see java.lang.Comparable#compareTo(java.lang.Object) */
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public ITestNGMethod clone() {
        return null;
    }
}
