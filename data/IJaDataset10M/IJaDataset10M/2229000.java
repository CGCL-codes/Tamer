package com.qspin.qtaste.reporter.testresults;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import com.qspin.qtaste.reporter.testresults.TestResultImpl.StepResult;
import com.qspin.qtaste.testsuite.TestData;

/**
 * Description of all the fields present in the testresult reports
 * @author lvboque
 */
public interface TestResult {

    public enum Status {

        NOT_EXECUTED, RUNNING, NOT_AVAILABLE, SUCCESS, FAIL
    }

    public TestData getTestData();

    public String getExtraResultDetails();

    public void setExtraResultDetails(String extraResultDetails);

    public String getReturnValue();

    public void setReturnValue(String returnValue);

    public String getId();

    public String getComment();

    public void setName(String name);

    public String getName();

    public void setTestScriptVersion(String version);

    public String getTestScriptVersion();

    public void setComment(String comment);

    public Status getStatus();

    public void start();

    public void stop();

    public void setStatus(Status status);

    public Date getStartDate();

    public Date getEndDate();

    public long getElapsedTimeMs();

    public String getFormattedElapsedTime(boolean showMilliseconds);

    public void setStackTrace(String stackTrace);

    public String getStackTrace();

    public void addStackTraceElement(StackTraceElement stackElement);

    public void setStack(ArrayList<StackTraceElement> stack);

    public ArrayList<StackTraceElement> getStack();

    /**
     * 
     * @return diretory containing the testcase
     */
    public String getTestCaseDirectory();

    /**
     * Set the testcase directory to the directory value 
     */
    public void setTestCaseDirectory(String directory);

    public int getFailedLineNumber();

    public void setFailedLineNumber(int failedLineNumber);

    public String getFailedFunctionId();

    public void setFailedFunctionId(String failedFunctionId);

    public int getRetryCount();

    public void setRetryCount(int retryCount);

    public void addStepResult(String stepId, String functionName, String stepDescription, String expectedResult, Status stepStatus, double elapsedTime);

    public Collection<StepResult> getStepResults();

    public int getCurrentRowIndex();

    public int getNumberRows();
}
