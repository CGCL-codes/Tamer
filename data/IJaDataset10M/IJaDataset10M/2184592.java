package org.testium.executor;

import java.io.File;
import java.util.Hashtable;
import org.testtoolinterfaces.testresult.TestCaseResultLink;
import org.testtoolinterfaces.testresult.TestResult.VERDICT;
import org.testtoolinterfaces.testsuite.TestCaseLink;
import org.testtoolinterfaces.utils.RunTimeData;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.Warning;

public class TestCaseMetaExecutor {

    private Hashtable<String, TestCaseExecutor> myExecutors;

    public TestCaseMetaExecutor() {
        Trace.println(Trace.CONSTRUCTOR);
        myExecutors = new Hashtable<String, TestCaseExecutor>();
    }

    public TestCaseResultLink execute(TestCaseLink aTestCaseLink, File aLogDir, RunTimeData aRTData) {
        Trace.println(Trace.EXEC, "execute( " + aTestCaseLink.getId() + ", " + aLogDir.getPath() + ", " + aRTData.size() + " Variables )", true);
        TestCaseResultLink result;
        if (myExecutors.containsKey(aTestCaseLink.getScriptType())) {
            TestCaseExecutor executor = myExecutors.get(aTestCaseLink.getScriptType());
            result = executor.execute(aTestCaseLink, aLogDir, aRTData);
        } else {
            result = new TestCaseResultLink(aTestCaseLink, VERDICT.ERROR, null);
            String message = "Cannot execute test case scripts of type " + aTestCaseLink.getScriptType() + "\n";
            result.addComment(message);
            Warning.println(message);
            Trace.print(Trace.ALL, "Cannot execute " + aTestCaseLink.toString());
        }
        return result;
    }

    public void put(String type, TestCaseExecutor aTestCaseExecutor) {
        myExecutors.put(type, aTestCaseExecutor);
    }
}
