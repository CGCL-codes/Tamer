package org.testtoolinterfaces.testresultinterface;

import java.util.Enumeration;
import java.util.Hashtable;
import org.testtoolinterfaces.testresult.TestResult;
import org.testtoolinterfaces.testresult.TestStepResult;
import org.testtoolinterfaces.testsuite.ParameterTable;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestStepImpl;
import org.testtoolinterfaces.utils.GenericTagAndStringXmlHandler;
import org.testtoolinterfaces.utils.Trace;
import org.testtoolinterfaces.utils.XmlHandler;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

/**
 * @author Arjan Kranenburg 
 * 
 * <action sequence=...>
 *  <description>
 *  ...
 *  </description>
 *  <command>
 *  ...
 *  </command>
 * </action>
 */
public class ActionTypeResultXmlHandler extends XmlHandler {

    public static final String PARAM_SEQUENCE = "sequence";

    private static final String COMMAND_ELEMENT = "command";

    private static final String RESULT_ELEMENT = "result";

    private GenericTagAndStringXmlHandler myCommandXmlHandler;

    private GenericTagAndStringXmlHandler myResultXmlHandler;

    private LogFilesXmlHandler myLogFilesXmlHandler;

    private int myCurrentSequence = 0;

    private String myCommand = "";

    private TestResult.VERDICT myResult = TestResult.UNKNOWN;

    private Hashtable<String, String> myLogFiles = new Hashtable<String, String>();

    public ActionTypeResultXmlHandler(XMLReader anXmlReader, TestStep.ActionType aTag) {
        super(anXmlReader, aTag.toString());
        Trace.println(Trace.CONSTRUCTOR, "ActionTypeResultXmlHandler( anXmlreader, " + aTag + " )", true);
        myCommandXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, COMMAND_ELEMENT);
        this.addStartElementHandler(COMMAND_ELEMENT, myCommandXmlHandler);
        myCommandXmlHandler.addEndElementHandler(COMMAND_ELEMENT, this);
        myResultXmlHandler = new GenericTagAndStringXmlHandler(anXmlReader, RESULT_ELEMENT);
        this.addStartElementHandler(RESULT_ELEMENT, myResultXmlHandler);
        myResultXmlHandler.addEndElementHandler(RESULT_ELEMENT, this);
        myLogFilesXmlHandler = new LogFilesXmlHandler(anXmlReader);
        this.addStartElementHandler(LogFilesXmlHandler.START_ELEMENT, myLogFilesXmlHandler);
        myLogFilesXmlHandler.addEndElementHandler(LogFilesXmlHandler.START_ELEMENT, this);
    }

    public void processElementAttributes(String aQualifiedName, Attributes att) {
        Trace.println(Trace.SUITE, "processElementAttributes( " + aQualifiedName + " )", true);
        if (aQualifiedName.equalsIgnoreCase(this.getStartElement())) {
            for (int i = 0; i < att.getLength(); i++) {
                if (att.getQName(i).equalsIgnoreCase(PARAM_SEQUENCE)) {
                    myCurrentSequence = Integer.valueOf(att.getValue(i)).intValue();
                    Trace.println(Trace.LEVEL.ALL, "        myCurrentSequence -> " + myCurrentSequence);
                }
            }
        }
    }

    @Override
    public void handleStartElement(String aQualifiedName) {
    }

    @Override
    public void handleCharacters(String aValue) {
    }

    @Override
    public void handleEndElement(String aQualifiedName) {
    }

    @Override
    public void handleGoToChildElement(String aQualifiedName) {
    }

    @Override
    public void handleReturnFromChildElement(String aQualifiedName, XmlHandler aChildXmlHandler) {
        Trace.println(Trace.SUITE);
        if (aQualifiedName.equalsIgnoreCase(COMMAND_ELEMENT)) {
            myCommand = myCommandXmlHandler.getValue();
            myCommandXmlHandler.reset();
        }
        if (aQualifiedName.equalsIgnoreCase(RESULT_ELEMENT)) {
            myResult = TestResult.VERDICT.valueOf(myResultXmlHandler.getValue().toUpperCase());
            myResultXmlHandler.reset();
        }
        if (aQualifiedName.equalsIgnoreCase(LogFilesXmlHandler.START_ELEMENT)) {
            myLogFiles = myLogFilesXmlHandler.getLogFiles();
            myLogFilesXmlHandler.reset();
        }
    }

    public TestStepResult getActionStep() {
        Trace.println(Trace.SUITE);
        TestStep.ActionType action = TestStep.ActionType.valueOf(this.getStartElement());
        TestStep testStep = new TestStepImpl(action, myCurrentSequence, "", myCommand, new ParameterTable());
        TestStepResult testStepResult = new TestStepResult(testStep);
        testStepResult.setResult(myResult);
        if (!myLogFiles.isEmpty()) {
            for (Enumeration<String> keys = myLogFiles.keys(); keys.hasMoreElements(); ) {
                String key = keys.nextElement();
                testStepResult.addTestLog(key, myLogFiles.get(key));
            }
        }
        return testStepResult;
    }

    public void reset() {
        Trace.println(Trace.LEVEL.SUITE);
        myCurrentSequence = 0;
    }
}
