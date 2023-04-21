package org.xaware.designer.lint;

import org.xaware.designer.lint.validator.ResultList;

/**
 * This class validates the XALint handling of BizComp references.
 * 
 * @author hcurtis
 * 
 */
public class ValidateBizComp extends ValidateXALintBase {

    public ValidateBizComp() {
        super("ValidateBizDoc");
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test a simple BizDoc calling a BizComp that requires an input stream. The input stream is provided in the BIzDoc
     * with the xa:input attribute.
     */
    public void testStreamingBizDoc() {
        loadRules("BasicRules.xml");
        lint.checkBizFile("StreamingBizDoc.xbd", resourcePath, messages);
        final int msgCnt = messages.getMessageList().size();
        assertTrue("Reported messages [" + msgCnt + "] must be zero", msgCnt == 0);
    }

    /**
     * Test a simple BizDoc calling a BizComp that requires an input stream. The input stream is not provided. The
     * reference is missing the xa:input attribute. There should be one error message reported in the messages array.
     */
    public void testMissingStreamBizDocRef() {
        final String expectedMsg = "Message[ name: XMLMapper," + " description: Missing xa:input to supply BizFile 'XMLMapper' with input XML" + " location: MissInputStreamingBizDoc.xbd#/Document/Element" + " configData: " + " level: ERROR ]";
        loadRules("BasicRules.xml");
        lint.checkBizFile("MissInputStreamingBizDoc.xbd", resourcePath, messages);
        final int msgCnt = messages.getMessageList().size();
        assertTrue("Reported messages [" + msgCnt + "] must be zero", msgCnt == 1);
        final ResultList.Message msg = (ResultList.Message) messages.getMessageList().get(0);
        assertTrue("Invalid validation error message: " + msg.toString(), expectedMsg.equals(msg.toString()));
    }

    /**
     * Test a simple BizDoc calling a two BizComps that require input streams. The two BizComps are inseparate folders.
     * The input stream is provided in the BIzDoc with the xa:input attribute. No errors are expected from the
     * validators.
     */
    public void testTwoBizCompRefs() {
        loadRules("BasicRules.xml");
        lint.checkBizFile("Good2RefBizComp.xbd", resourcePath, messages);
        final int msgCnt = messages.getMessageList().size();
        assertTrue("Reported messages [" + msgCnt + "] must be zero", msgCnt == 0);
    }

    /**
     * Test a simple BizDoc validation. This test has one BizDoc with two embeded references to two other BizComps. The
     * first reference in the Document/BizFolder1 is invalid. The second references is valid. There should be one error
     * message reported in the messages array.
     */
    public void JT_XtestBizCompRef1InBizDocBad() {
        final String expectedMsg = "Message[ name: folder1/XMLBadMapper.xbc," + " description: Can't find referenced BizFile," + " folder1/XMLBadMapper.xbc," + " in resource path: " + resourcePath + " location: BadBizCompRef1.xbd#/Document/BizFolder1" + " configData: " + " level: WARNING ]";
        loadRules("BasicRules.xml");
        lint.checkBizFile("BadBizCompRef1.xbd", resourcePath, messages);
        final int msgCnt = messages.getMessageList().size();
        assertTrue("Reported messages [" + msgCnt + "] must be 1", msgCnt == 1);
        final ResultList.Message msg = (ResultList.Message) messages.getMessageList().get(0);
        assertTrue("Invalid validation error message: " + msg.toString(), expectedMsg.equals(msg.toString()));
    }

    /**
     * Test a simple BizDoc validation. This test has one BizDoc with two embeded references to two other BizComps. The
     * second reference in the Document/BizFolder2 is invalid. The first Bizcomp references is valid. There should be
     * one error message reported in the messages array.
     */
    public void JT_XtestBizCompRef2InBizDocBad() {
        final String expectedMsg = "Message[ name: folder2/XMLBadMapper.xbc," + " description: Can't find referenced BizFile," + " folder2/XMLBadMapper.xbc," + " in resource path: " + resourcePath + " location: BadBizCompRef2.xbd#/Document/BizFolder2" + " configData: " + " level: WARNING ]";
        loadRules("BasicRules.xml");
        lint.checkBizFile("BadBizCompRef2.xbd", resourcePath, messages);
        final int msgCnt = messages.getMessageList().size();
        assertTrue("Reported messages [" + msgCnt + "] must be 1", msgCnt == 1);
        final ResultList.Message msg = (ResultList.Message) messages.getMessageList().get(0);
        assertTrue("Invalid validation error message: " + msg.toString(), expectedMsg.equals(msg.toString()));
    }

    /**
     * Test a simple BizDoc validation. This test has one BizDoc with two embeded references to two other BizComps. Both
     * references are invalid. There should be two error message reported in the messages array.
     */
    public void JT_XtestBothBizCompRefsInBizDocBad() {
        final String expectedMsgs[] = { "Message[ name: folder1/XMLBadMapping.xbc," + " description: Can't find referenced BizFile," + " folder1/XMLBadMapping.xbc," + " in resource path: " + resourcePath + " location: BadBizCompBothRefs.xbd#/Document/BizFolder1" + " configData: " + " level: WARNING ]", "Message[ name: folder2/XMLBadMapper.xbc," + " description: Can't find referenced BizFile," + " folder2/XMLBadMapper.xbc," + " in resource path: " + resourcePath + " location: BadBizCompBothRefs.xbd#/Document/BizFolder2" + " configData: " + " level: WARNING ]" };
        loadRules("BasicRules.xml");
        lint.checkBizFile("BadBizCompBothRefs.xbd", resourcePath, messages);
        final int msgCnt = messages.getMessageList().size();
        assertTrue("Reported messages [" + msgCnt + "] must be 2", msgCnt == 2);
        ResultList.Message msg = (ResultList.Message) messages.getMessageList().get(0);
        assertTrue("Invalid validation on first error message: " + msg.toString(), expectedMsgs[0].equals(msg.toString()));
        msg = (ResultList.Message) messages.getMessageList().get(1);
        assertTrue("Invalid validation on second error message: " + msg.toString(), expectedMsgs[1].equals(msg.toString()));
    }

    /**
     * Test the Interface validation of one BizDoc referencing a BizComp. The referenced BizComp has one parameter,
     * filename, specified in the input. The filename parameter is supplied by the calling BizDoc. No error is expected.
     */
    public void testBizCompInterfaceValid() {
        loadRules("BasicRules.xml");
        lint.checkBizFile("GoodReadFixedLength.xbd", resourcePath, messages);
        final int msgCnt = messages.getMessageList().size();
        assertTrue("Reported messages [" + msgCnt + "] must be zero", msgCnt == 0);
    }

    /**
     * Test the Interface validation of one BizDoc referencing a BizComp. The referenced BizComp has one parameter,
     * filename, specified in the input. The filename parameter is not supplied by the calling BizDoc. Instead the
     * parameter is called fName, a simple miss-spelling of the parameter. In the BizComp the filename parameter has no
     * xa:required setting so a warning is expected.
     * 
     * In this case two error messages are expected. One for the missing fileName parameter and another where the BizDoc
     * is supplying a parameter, fname, that is not required by the BizComp
     */
    public void testBizCompInterfaceNoRequired() {
        final String expectedMsgs[] = { "Message[ name: fileName," + " description: No actual parameter supplied for defined parameter 'fileName'" + " location: MissSpelledParamReadFixedLength.xbd#/Document/MFTest" + " configData: " + " level: WARNING ]", "Message[ name: fName," + " description: Actual parameter 'fName' has no corresponding definition in the called BizView file interface" + " location: MissSpelledParamReadFixedLength.xbd#/Document/MFTest" + " configData: " + " level: WARNING ]" };
        loadRules("BasicRules.xml");
        lint.checkBizFile("MissSpelledParamReadFixedLength.xbd", resourcePath, messages);
        final int msgCnt = messages.getMessageList().size();
        assertTrue("Reported messages [" + msgCnt + "] must be one", msgCnt == 2);
        ResultList.Message msg = (ResultList.Message) messages.getMessageList().get(0);
        assertTrue("Invalid validation on first error message: " + msg.toString(), expectedMsgs[0].equals(msg.toString()));
        msg = (ResultList.Message) messages.getMessageList().get(1);
        assertTrue("Invalid validation on second error message: " + msg.toString(), expectedMsgs[1].equals(msg.toString()));
    }

    /**
     * Test the Interface validation of one BizDoc referencing a BizComp. The referenced BizComp has one
     * xa:required="yes" parameter, filename, specified in the input. The filename parameter is not supplied by the
     * calling BizDoc. Instead the parameter is called fName, a simple miss-spelling of the parameter. In the BizComp
     * the filename parameter has an xa:required="yes" setting so a error is expected.
     * 
     * In this case two error messages are expected. One for the missing fileName parameter and another where the BizDoc
     * is supplying a parameter, fname, that is not required by the BizComp. The missing requied parameter is identified
     * as an ERROR instead of a WARNING.
     */
    public void testBizCompInterfaceParamRequired() {
        final String expectedMsgs[] = { "Message[ name: fileName," + " description: No actual parameter supplied for required parameter 'fileName'" + " location: MissReqParamReadFixedLength.xbd#/Document/MFTest" + " configData: " + " level: ERROR ]", "Message[ name: fName," + " description: Actual parameter 'fName' has no corresponding definition in the called BizView file interface" + " location: MissReqParamReadFixedLength.xbd#/Document/MFTest" + " configData: " + " level: WARNING ]" };
        loadRules("BasicRules.xml");
        lint.checkBizFile("MissReqParamReadFixedLength.xbd", resourcePath, messages);
        final int msgCnt = messages.getMessageList().size();
        assertTrue("Reported messages [" + msgCnt + "] must be one", msgCnt == 2);
        ResultList.Message msg = (ResultList.Message) messages.getMessageList().get(0);
        assertTrue("Invalid validation on first error message: " + msg.toString(), expectedMsgs[0].equals(msg.toString()));
        msg = (ResultList.Message) messages.getMessageList().get(1);
        assertTrue("Invalid validation on second error message: " + msg.toString(), expectedMsgs[1].equals(msg.toString()));
    }

    /**
     * Test the Interface validation of one BizDoc referencing a BizComp. The referenced BizComp has one
     * xa:required="no" parameter, filename, specified in the input. The filename parameter has a default value which
     * will support the BizComp. The BizDoc has a parameter called fName, a simple miss-spelling of the parameter. In
     * the BizComp the filename parameter has an xa:required="no" setting so a error is expected.
     * 
     * In this case one error messages is expected. The fName parameter supplied by the BizDoc has no corresponding
     * parameter in the called BizComp. This is reported as a WARNING.
     */
    public void testBizCompInterfaceParamOptional() {
        final String expectedMsg = "Message[ name: fName," + " description: Actual parameter 'fName' has no corresponding definition in the called BizView file interface" + " location: MissOptParamReadFixedLength.xbd#/Document/MFTest" + " configData: " + " level: WARNING ]";
        loadRules("BasicRules.xml");
        lint.checkBizFile("MissOptParamReadFixedLength.xbd", resourcePath, messages);
        final int msgCnt = messages.getMessageList().size();
        assertTrue("Reported messages [" + msgCnt + "] must be one", msgCnt == 1);
        final ResultList.Message msg = (ResultList.Message) messages.getMessageList().get(0);
        assertTrue("Invalid validation on first error message: " + msg.toString(), expectedMsg.equals(msg.toString()));
    }
}
