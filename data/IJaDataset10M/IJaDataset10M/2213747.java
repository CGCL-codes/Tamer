package com.nokia.ats4.appmodel.script.aste;

import com.nokia.ats4.appmodel.script.aste.AsteScriptBuilder;
import com.nokia.ats4.appmodel.MainApplication;
import com.nokia.ats4.appmodel.model.KendoProject;
import com.nokia.ats4.appmodel.model.domain.ResponseVerificationCommand;
import com.nokia.ats4.appmodel.model.domain.State;
import com.nokia.ats4.appmodel.model.domain.testdata.TestData;
import com.nokia.ats4.appmodel.model.domain.testdata.TestDataItem;
import com.nokia.ats4.appmodel.model.domain.testdata.TestDataItem.Category;
import com.nokia.ats4.appmodel.model.domain.usecase.UseCase;
import com.nokia.ats4.appmodel.model.domain.usecase.UseCasePath;
import com.nokia.ats4.appmodel.model.impl.MainApplicationModel;
import com.nokia.ats4.appmodel.script.TestScript;
import com.nokia.ats4.appmodel.script.TestScriptBuilder;
import com.nokia.ats4.appmodel.util.DummyProjectBuilder;
import junit.framework.*;
import com.nokia.ats4.appmodel.util.Settings;

/**
 * AsteScriptBuilderTest
 *
 * @author Kimmo Tuukkanen
 * @version $Revision: 2 $
 */
public class AsteScriptBuilderTest extends TestCase {

    KendoProject project = null;

    public AsteScriptBuilderTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        Settings.load(MainApplication.FILE_PROPERTIES);
        Settings.setProperty("aste.captureImages", "true");
        project = DummyProjectBuilder.createProjectWithUseCasePaths();
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of processTransition method, of class com.nokia.kendo.script.aste.AsteScriptBuilder.
     */
    public void testBuildModel() {
        TestScriptBuilder builder = new AsteScriptBuilder();
        TestScript script = builder.build(project.getDefaultModel());
        assertNotNull(script);
        assertNotNull(script.getLines());
        assertFalse(script.getLines().isEmpty());
        assertTrue(script.getScript().length() > 0);
    }

    /**
     * Test of processState method, of class com.nokia.kendo.script.aste.AsteScriptBuilder.
     */
    public void testBuildUseCase() {
        TestScriptBuilder builder = new AsteScriptBuilder();
        TestScript script = null;
        for (UseCase uc : project.getUseCaseModel().getUseCases()) {
            script = builder.build(uc);
            break;
        }
        assertNotNull(script);
        assertNotNull(script.getLines());
        assertFalse(script.getLines().isEmpty());
        assertTrue(script.getScript().length() > 0);
    }

    /**
     * Test of formatTestCase method, of class com.nokia.kendo.script.aste.AsteScriptBuilder.
     */
    public void testBuildUseCasePath() {
        TestScriptBuilder builder = new AsteScriptBuilder();
        TestScript script = null;
        for (UseCase uc : project.getUseCaseModel().getUseCases()) {
            script = builder.build(uc.getUseCasePath(0));
            break;
        }
        assertNotNull(script);
        assertNotNull(script.getLines());
        assertFalse(script.getLines().isEmpty());
        assertTrue(script.getScript().length() > 0);
    }

    /**
     * Test that test data in System Events (specifically for keyword ISI_MSG_RECV) 
     * is handled correctly. 
     * 
     * KND-571 "Test data result field doesn't work correctly when using 
     * ISI_MSG_SEND and ISI_MSG_RECV keywords" is related to this. 
     */
    public void testTestDataForKeywordIsiMsgRecv() {
        final String tdResult = "testDataResult";
        MainApplicationModel.getInstance().setActiveProject(project);
        final TestScriptBuilder builder = new AsteScriptBuilder();
        final TestData testDataVariable = new TestData("td");
        final TestDataItem tdItem = new TestDataItem("SOCKET_REQ_Success_Parm_list", "");
        tdItem.setResult(tdResult);
        tdItem.setPriority(Category.HIGH);
        testDataVariable.addTestDataItem(tdItem);
        project.getTestDataModel().addTestData(testDataVariable);
        final UseCase firstUseCase = project.getUseCaseModel().getUseCases().get(0);
        final UseCasePath firstUseCasePath = firstUseCase.getUseCasePath(0);
        final State firstSystemState = firstUseCasePath.iterator().next().getTargetState();
        final String receiveIsiMsgKw = "ISI_MSG_RECV";
        ResponseVerificationCommand command = new ResponseVerificationCommand(receiveIsiMsgKw, testDataVariable.getLogicalName(), false);
        firstSystemState.getSystemResponse().addCommand(command);
        final TestScript script = builder.build(firstUseCasePath);
        assertNotNull(script);
        assertNotNull(script.getLines());
        assertFalse(script.getLines().isEmpty());
        assertTrue(script.getScript().length() > 0);
        assertTrue("test script should contain test data result \"" + tdResult + "\"", script.getScript().contains(tdResult));
    }
}
