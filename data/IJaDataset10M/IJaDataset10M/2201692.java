package org.testium.executor;

import java.io.File;
import java.util.Calendar;
import org.testium.systemundertest.SutControl;
import org.testtoolinterfaces.testresult.TestRunResult;
import org.testtoolinterfaces.testsuite.TestGroup;

public interface TestSuiteExecutor {

    TestRunResult execute(TestGroup aTestGroup, File aScriptDir, File aLogDir, Calendar aDate) throws TestExecutionException;

    public void setSutControl(SutControl aSutControl);

    public void setTestGroupExecutor(TestGroupExecutor aTestGroupExecutor);
}
