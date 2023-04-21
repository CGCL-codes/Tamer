package org.testtoolinterfaces.testresultinterface;

import java.util.ArrayList;
import java.util.Hashtable;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.testtoolinterfaces.testresult.TestCaseResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan Kranenburg
 *
 */
public class TestCaseResultXmlWriter extends TestResultXmlWriter {

    /**
	 * @param aTestCaseName
	 */
    public TestCaseResultXmlWriter(TestCaseResult aResult, int anIndentLevel) {
        super(aResult, anIndentLevel);
        Trace.println(Trace.CONSTRUCTOR);
    }

    /**
	 * @param aFile
	 * @throws IOException 
	 */
    public void printXml(OutputStreamWriter aStream) throws IOException {
        Trace.println(Trace.UTIL);
        TestCaseResult result = (TestCaseResult) getResult();
        aStream.write("      <testCase");
        aStream.write(" id='" + result.getId() + "'");
        aStream.write(" sequence='" + result.getSequenceNr() + "'");
        aStream.write(">\n");
        String description = result.getDescription();
        aStream.write("        <description>");
        aStream.write(description);
        aStream.write("</description>\n");
        ArrayList<String> requirements = result.getRequirements();
        for (int key = 0; key < requirements.size(); key++) TestResultXmlWriter.printXmlRequirement(aStream, requirements.get(key));
        Hashtable<Integer, TestStepResult> initializationResults = result.getInitializationResults();
        for (int key = 0; key < initializationResults.size(); key++) {
            TestStepResultXmlWriter tsResultWriter = new TestStepResultXmlWriter(initializationResults.get(key), 0);
            tsResultWriter.printXml(aStream);
        }
        aStream.write("        <execution>\n");
        Hashtable<Integer, TestStepResult> executionResults = result.getExecutionResults();
        for (int key = 0; key < initializationResults.size(); key++) {
            TestStepResultXmlWriter tsResultWriter = new TestStepResultXmlWriter(executionResults.get(key), 0);
            tsResultWriter.printXml(aStream);
        }
        aStream.write("        </execution>\n");
        Hashtable<Integer, TestStepResult> restoreResults = result.getRestoreResults();
        for (int key = 0; key < restoreResults.size(); key++) {
            TestStepResultXmlWriter tsResultWriter = new TestStepResultXmlWriter(restoreResults.get(key), 0);
            tsResultWriter.printXml(aStream);
        }
        aStream.write("        <result>" + result.getResult().toString() + "</result>\n");
        printXmlLogFiles(aStream);
        printXmlComment(aStream);
        aStream.write("      </testCase>\n");
        aStream.flush();
    }
}
