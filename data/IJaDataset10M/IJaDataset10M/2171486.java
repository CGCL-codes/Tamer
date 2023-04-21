package ingenias.tests;

import static org.junit.Assert.*;
import java.util.Vector;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import jade.core.*;
import jade.wrapper.StaleProxyException;
import jade.wrapper.State;
import ingenias.editor.entities.MentalEntity;
import ingenias.exception.TimeOut;
import ingenias.jade.MentalStateManager;
import ingenias.jade.MentalStateProcessor;
import ingenias.jade.graphics.MainInteractionManager;
import ingenias.jade.mental.*;
import ingenias.testing.*;
import ingenias.jade.IAFProperties;

public class TimedOutcommunicationInitiatorSending {

    @Test
    public void testDemo() throws TimeOut {
        IAFProperties.setGarbageCollectionInterval(100);
        MentalStateManager msmA = MSMRepository.getInstance().waitFor("ATimeOut_0");
        MentalStateProcessor mspA = MSPRepository.getInstance().waitFor("ATimeOut_0");
        TestUtils.waitForAgentInitialised(mspA);
        MentalStateManager msmB0 = MSMRepository.getInstance().waitFor("B_0");
        MentalStateProcessor mspB0 = MSPRepository.getInstance().waitFor("B_0");
        TestUtils.waitForAgentInitialised(mspB0);
        TestUtils.snapShot(msmB0, "snapB0Initial");
        MentalStateManager msmB1 = MSMRepository.getInstance().waitFor("B_1");
        MentalStateProcessor mspB1 = MSPRepository.getInstance().waitFor("B_1");
        TestUtils.waitForAgentInitialised(mspB0);
        TestUtils.snapShot(msmB1, "snapB1Initial");
        MentalStateManager msmC0 = MSMRepository.getInstance().waitFor("C_0");
        MentalStateProcessor mspC0 = MSPRepository.getInstance().waitFor("C_0");
        TestUtils.waitForAgentInitialised(mspC0);
        TestUtils.snapShot(msmC0, "snapC0Initial");
        MentalStateManager msmC1 = MSMRepository.getInstance().waitFor("C_1");
        MentalStateProcessor mspC1 = MSPRepository.getInstance().waitFor("C_1");
        TestUtils.waitForAgentInitialised(mspC1);
        TestUtils.snapShot(msmC1, "snapC1Initial");
        MentalStateManager msmC2 = MSMRepository.getInstance().waitFor("C_2");
        MentalStateProcessor mspC2 = MSPRepository.getInstance().waitFor("C_2");
        TestUtils.waitForAgentInitialised(mspC2);
        TestUtils.snapShot(msmC2, "snapC2Initial");
        MentalStateManager msmC3 = MSMRepository.getInstance().waitFor("C_3");
        MentalStateProcessor mspC3 = MSPRepository.getInstance().waitFor("C_3");
        TestUtils.waitForAgentInitialised(mspC2);
        TestUtils.snapShot(msmC3, "snapC3Initial");
        MentalStateManager msmC4 = MSMRepository.getInstance().waitFor("C_4");
        MentalStateProcessor mspC4 = MSPRepository.getInstance().waitFor("C_4");
        TestUtils.waitForAgentInitialised(mspC2);
        TestUtils.snapShot(msmC4, "snapC4Initial");
        assertTrue("There should be once instance of  SenderFailure", msmA.getMentalEntityByType("SenderFailure").size() == 1);
        MainInteractionManager.goAutomatic();
        TestUtils.doNothing(5000);
        Vector<MentalEntity> differencesB0 = TestUtils.compareCurrentStateAgainstSnapshot(msmB0, "snapB0Initial");
        assertTrue("Agent B_0 should remain the same and I found the following differences " + differencesB0 + ". Original mental state was " + TestUtils.getSnapshotString("snapB0Initial"), differencesB0.size() == 0);
        ingenias.editor.entities.RuntimeConversation conv = TestUtils.checkFirstConversation(msmB1, "B_1", "ABORTED");
        TestUtils.checkNOExistenceMEWithinConv(conv, "SingleAnswer", "B_1");
        TestUtils.checkNOExistenceMEWithinMS(msmB1, "SingleAnswer", "B_1");
        TestUtils.checkNOExistenceMEWithinConv(conv, "SingleQuestion", "B_1");
        TestUtils.checkNOExistenceMEWithinMS(msmB1, "SingleQuestion", "B_1");
        conv = TestUtils.checkFirstConversation(msmA, "ATimeOut", "aborted");
        TestUtils.checkNOExistenceMEWithinMS(msmA, "InitialQuestion", "ATimeOut");
        TestUtils.checkExistenceMEWithinConv(conv, "InitialQuestion", "ATimeOut", 1);
        TestUtils.checkNOExistenceMEWithinMS(msmA, "MultipleAnswer", "ATimeOut");
        TestUtils.checkNOExistenceMEWithinConv(conv, "MultipleAnswer", "ATimeOut");
        TestUtils.checkNOExistenceMEWithinMS(msmA, "SingleQuestion", "ATimeOut");
        TestUtils.checkNOExistenceMEWithinConv(conv, "SingleQuestion", "ATimeOut");
        TestUtils.checkNOExistenceMEWithinMS(msmA, "SenderFailure", "ATimeOut");
        TestUtils.checkNOExistenceMEWithinConv(conv, "SenderFailure", "ATimeOut");
        Vector<MentalEntity> differencesC0 = TestUtils.compareCurrentStateAgainstSnapshot(msmC0, "snapC0Initial");
        assertTrue("Agent C_0 should remain the same and I found the following differences " + differencesC0 + ". Original mental state was " + TestUtils.getSnapshotString("snapC0Initial"), differencesC0.size() == 0);
        Vector<MentalEntity> differencesC1 = TestUtils.compareCurrentStateAgainstSnapshot(msmC1, "snapC1Initial");
        assertTrue("Agent C_1 should remain the same and I found the following differences " + differencesC1 + ". Original mental state was " + TestUtils.getSnapshotString("snapC1Initial"), differencesC1.size() == 0);
        conv = TestUtils.checkFirstConversation(msmC2, "C_2", "FINISHED");
        checkAgent(msmC2, conv, "C_2");
        conv = TestUtils.checkFirstConversation(msmC3, "C_3", "FINISHED");
        checkAgent(msmC3, conv, "C_3");
        Vector<MentalEntity> differencesC4 = TestUtils.compareCurrentStateAgainstSnapshot(msmC4, "snapC4Initial");
        assertTrue("Agent C_4 should remain the same and I found the following differences " + differencesC4 + ". Original mental state was " + TestUtils.getSnapshotString("snapC4Initial"), differencesC4.size() == 0);
    }

    private void checkAgent(MentalStateManager msmC0, ingenias.editor.entities.RuntimeConversation conv, String agentName) {
        TestUtils.checkNOExistenceMEWithinMS(msmC0, "MultipleAnswer", agentName);
        TestUtils.checkExistenceMEWithinConv(conv, "MultipleAnswer", agentName, 1);
        TestUtils.checkNOExistenceMEWithinMS(msmC0, "InitialQuestion", agentName);
        TestUtils.checkNOExistenceMEWithinConv(conv, "InitialQuestion", agentName);
    }
}
