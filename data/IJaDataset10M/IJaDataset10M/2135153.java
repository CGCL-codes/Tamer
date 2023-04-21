package ingenias.tests;

import static org.junit.Assert.*;
import java.util.Vector;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import jade.core.*;
import jade.wrapper.StaleProxyException;
import ingenias.editor.entities.MentalEntity;
import ingenias.exception.InvalidEntity;
import ingenias.jade.MentalStateManager;
import ingenias.jade.MentalStateProcessor;
import ingenias.jade.graphics.MainInteractionManager;
import ingenias.jade.mental.*;
import ingenias.testing.*;
import ingenias.testing.BasicMASTest;
import ingenias.jade.IAFProperties;

public class CheckingATicketWasObtained extends BasicMASTest {

    public void testDemo() {
        TestUtils.doNothing(3000);
        MentalStateManager msmBuyerAgent_0multipleBuyers = MSMRepository.getInstance().get("BuyerAgent_0multipleBuyers");
        MentalStateManager msmBuyerAgent_1multipleBuyers = MSMRepository.getInstance().get("BuyerAgent_1multipleBuyers");
        MentalStateManager msmBuyerAgent_2multipleBuyers = MSMRepository.getInstance().get("BuyerAgent_2multipleBuyers");
        MentalStateManager msmBuyerAgent_3multipleBuyers = MSMRepository.getInstance().get("BuyerAgent_3multipleBuyers");
        MentalStateManager msmBuyerAgent_4multipleBuyers = MSMRepository.getInstance().get("BuyerAgent_4multipleBuyers");
        MentalStateManager msmBuyerAgent_5multipleBuyers = MSMRepository.getInstance().get("BuyerAgent_5multipleBuyers");
        MentalStateManager msmBuyerAgent_6multipleBuyers = MSMRepository.getInstance().get("BuyerAgent_6multipleBuyers");
        MentalStateManager msmBuyerAgent_7multipleBuyers = MSMRepository.getInstance().get("BuyerAgent_7multipleBuyers");
        MentalStateManager msmBuyerAgent_8multipleBuyers = MSMRepository.getInstance().get("BuyerAgent_8multipleBuyers");
        MentalStateManager msmBuyerAgent_9multipleBuyers = MSMRepository.getInstance().get("BuyerAgent_9multipleBuyers");
        MentalStateManager msmInterfaceAgent_0multipleInterfaceAgents = MSMRepository.getInstance().get("InterfaceAgent_0multipleInterfaceAgents");
        MentalStateManager msmInterfaceAgent_1multipleInterfaceAgents = MSMRepository.getInstance().get("InterfaceAgent_1multipleInterfaceAgents");
        MentalStateManager msmInterfaceAgent_2multipleInterfaceAgents = MSMRepository.getInstance().get("InterfaceAgent_2multipleInterfaceAgents");
        MentalStateManager msmInterfaceAgent_3multipleInterfaceAgents = MSMRepository.getInstance().get("InterfaceAgent_3multipleInterfaceAgents");
        MentalStateManager msmInterfaceAgent_4multipleInterfaceAgents = MSMRepository.getInstance().get("InterfaceAgent_4multipleInterfaceAgents");
        MentalStateManager msmInterfaceAgent_5multipleInterfaceAgents = MSMRepository.getInstance().get("InterfaceAgent_5multipleInterfaceAgents");
        MentalStateManager msmInterfaceAgent_6multipleInterfaceAgents = MSMRepository.getInstance().get("InterfaceAgent_6multipleInterfaceAgents");
        MentalStateManager msmInterfaceAgent_7multipleInterfaceAgents = MSMRepository.getInstance().get("InterfaceAgent_7multipleInterfaceAgents");
        MentalStateManager msmInterfaceAgent_8multipleInterfaceAgents = MSMRepository.getInstance().get("InterfaceAgent_8multipleInterfaceAgents");
        MentalStateManager msmInterfaceAgent_9multipleInterfaceAgents = MSMRepository.getInstance().get("InterfaceAgent_9multipleInterfaceAgents");
        MentalStateManager msmSellerAgent1_0multipleSellers = MSMRepository.getInstance().get("SellerAgent1_0multipleSellers");
        MentalStateManager msmSellerAgent1_1multipleSellers = MSMRepository.getInstance().get("SellerAgent1_1multipleSellers");
        MentalStateManager msmSellerAgent1_2multipleSellers = MSMRepository.getInstance().get("SellerAgent1_2multipleSellers");
        MentalStateManager msmSellerAgent1_3multipleSellers = MSMRepository.getInstance().get("SellerAgent1_3multipleSellers");
        MentalStateManager msmSellerAgent1_4multipleSellers = MSMRepository.getInstance().get("SellerAgent1_4multipleSellers");
        User_wants_to_watch_a_movie nevent = new User_wants_to_watch_a_movie();
        try {
            msmInterfaceAgent_0multipleInterfaceAgents.addMentalEntity(nevent);
            msmInterfaceAgent_1multipleInterfaceAgents.addMentalEntity(nevent);
            msmInterfaceAgent_2multipleInterfaceAgents.addMentalEntity(nevent);
            msmInterfaceAgent_3multipleInterfaceAgents.addMentalEntity(nevent);
            msmInterfaceAgent_4multipleInterfaceAgents.addMentalEntity(nevent);
            msmInterfaceAgent_5multipleInterfaceAgents.addMentalEntity(nevent);
            msmInterfaceAgent_6multipleInterfaceAgents.addMentalEntity(nevent);
            msmInterfaceAgent_7multipleInterfaceAgents.addMentalEntity(nevent);
            msmInterfaceAgent_8multipleInterfaceAgents.addMentalEntity(nevent);
            msmInterfaceAgent_9multipleInterfaceAgents.addMentalEntity(nevent);
        } catch (InvalidEntity e) {
            e.printStackTrace();
        }
        checkTicketsPreDeliver(msmBuyerAgent_0multipleBuyers);
        checkTicketsPreDeliver(msmBuyerAgent_1multipleBuyers);
        checkTicketsPreDeliver(msmBuyerAgent_2multipleBuyers);
        checkTicketsPreDeliver(msmBuyerAgent_3multipleBuyers);
        checkTicketsPreDeliver(msmBuyerAgent_4multipleBuyers);
        checkTicketsPreDeliver(msmBuyerAgent_5multipleBuyers);
        checkTicketsPreDeliver(msmBuyerAgent_6multipleBuyers);
        checkTicketsPreDeliver(msmBuyerAgent_7multipleBuyers);
        checkTicketsPreDeliver(msmBuyerAgent_8multipleBuyers);
        checkTicketsPreDeliver(msmBuyerAgent_9multipleBuyers);
        MainInteractionManager.goAutomatic();
        TestUtils.doNothing(5000);
        checkTicketsPostDeliver(msmBuyerAgent_0multipleBuyers);
        checkTicketsPostDeliver(msmBuyerAgent_1multipleBuyers);
        checkTicketsPostDeliver(msmBuyerAgent_2multipleBuyers);
        checkTicketsPostDeliver(msmBuyerAgent_3multipleBuyers);
        checkTicketsPostDeliver(msmBuyerAgent_4multipleBuyers);
        checkTicketsPostDeliver(msmBuyerAgent_5multipleBuyers);
        checkTicketsPostDeliver(msmBuyerAgent_6multipleBuyers);
        checkTicketsPostDeliver(msmBuyerAgent_7multipleBuyers);
        checkTicketsPostDeliver(msmBuyerAgent_8multipleBuyers);
        checkTicketsPostDeliver(msmBuyerAgent_9multipleBuyers);
    }

    @Test
    public void testBuyerAssignment() {
        TestUtils.doNothing(3000);
        MentalStateManager msmBuyerAgent_0multipleBuyers = MSMRepository.getInstance().get("BuyerAgent_0multipleBuyers");
        MentalStateManager msmBuyerAgent_1multipleBuyers = MSMRepository.getInstance().get("BuyerAgent_1multipleBuyers");
        MentalStateManager msmBuyerAgent_2multipleBuyers = MSMRepository.getInstance().get("BuyerAgent_2multipleBuyers");
        MentalStateManager msmBuyerAgent_3multipleBuyers = MSMRepository.getInstance().get("BuyerAgent_3multipleBuyers");
        MentalStateManager msmBuyerAgent_4multipleBuyers = MSMRepository.getInstance().get("BuyerAgent_4multipleBuyers");
        MentalStateManager msmBuyerAgent_5multipleBuyers = MSMRepository.getInstance().get("BuyerAgent_5multipleBuyers");
        MentalStateManager msmBuyerAgent_6multipleBuyers = MSMRepository.getInstance().get("BuyerAgent_6multipleBuyers");
        MentalStateManager msmBuyerAgent_7multipleBuyers = MSMRepository.getInstance().get("BuyerAgent_7multipleBuyers");
        MentalStateManager msmBuyerAgent_8multipleBuyers = MSMRepository.getInstance().get("BuyerAgent_8multipleBuyers");
        MentalStateManager msmBuyerAgent_9multipleBuyers = MSMRepository.getInstance().get("BuyerAgent_9multipleBuyers");
        MentalStateManager msmInterfaceAgent_0multipleInterfaceAgents = MSMRepository.getInstance().get("InterfaceAgent_0multipleInterfaceAgents");
        MentalStateManager msmInterfaceAgent_1multipleInterfaceAgents = MSMRepository.getInstance().get("InterfaceAgent_1multipleInterfaceAgents");
        MentalStateManager msmInterfaceAgent_2multipleInterfaceAgents = MSMRepository.getInstance().get("InterfaceAgent_2multipleInterfaceAgents");
        MentalStateManager msmInterfaceAgent_3multipleInterfaceAgents = MSMRepository.getInstance().get("InterfaceAgent_3multipleInterfaceAgents");
        MentalStateManager msmInterfaceAgent_4multipleInterfaceAgents = MSMRepository.getInstance().get("InterfaceAgent_4multipleInterfaceAgents");
        MentalStateManager msmInterfaceAgent_5multipleInterfaceAgents = MSMRepository.getInstance().get("InterfaceAgent_5multipleInterfaceAgents");
        MentalStateManager msmInterfaceAgent_6multipleInterfaceAgents = MSMRepository.getInstance().get("InterfaceAgent_6multipleInterfaceAgents");
        MentalStateManager msmInterfaceAgent_7multipleInterfaceAgents = MSMRepository.getInstance().get("InterfaceAgent_7multipleInterfaceAgents");
        MentalStateManager msmInterfaceAgent_8multipleInterfaceAgents = MSMRepository.getInstance().get("InterfaceAgent_8multipleInterfaceAgents");
        MentalStateManager msmInterfaceAgent_9multipleInterfaceAgents = MSMRepository.getInstance().get("InterfaceAgent_9multipleInterfaceAgents");
        MentalStateProcessor mspInterfaceAgent_0multipleInterfaceAgents = MSPRepository.getInstance().get("InterfaceAgent_0multipleInterfaceAgents");
        MentalStateProcessor mspInterfaceAgent_1multipleInterfaceAgents = MSPRepository.getInstance().get("InterfaceAgent_1multipleInterfaceAgents");
        MentalStateProcessor mspInterfaceAgent_2multipleInterfaceAgents = MSPRepository.getInstance().get("InterfaceAgent_2multipleInterfaceAgents");
        MentalStateProcessor mspInterfaceAgent_3multipleInterfaceAgents = MSPRepository.getInstance().get("InterfaceAgent_3multipleInterfaceAgents");
        MentalStateProcessor mspInterfaceAgent_4multipleInterfaceAgents = MSPRepository.getInstance().get("InterfaceAgent_4multipleInterfaceAgents");
        MentalStateProcessor mspInterfaceAgent_5multipleInterfaceAgents = MSPRepository.getInstance().get("InterfaceAgent_5multipleInterfaceAgents");
        MentalStateProcessor mspInterfaceAgent_6multipleInterfaceAgents = MSPRepository.getInstance().get("InterfaceAgent_6multipleInterfaceAgents");
        MentalStateProcessor mspInterfaceAgent_7multipleInterfaceAgents = MSPRepository.getInstance().get("InterfaceAgent_7multipleInterfaceAgents");
        MentalStateProcessor mspInterfaceAgent_8multipleInterfaceAgents = MSPRepository.getInstance().get("InterfaceAgent_8multipleInterfaceAgents");
        MentalStateProcessor mspInterfaceAgent_9multipleInterfaceAgents = MSPRepository.getInstance().get("InterfaceAgent_9multipleInterfaceAgents");
        TestUtils.waitForAgentInitialised(mspInterfaceAgent_0multipleInterfaceAgents);
        TestUtils.waitForAgentInitialised(mspInterfaceAgent_1multipleInterfaceAgents);
        TestUtils.waitForAgentInitialised(mspInterfaceAgent_2multipleInterfaceAgents);
        TestUtils.waitForAgentInitialised(mspInterfaceAgent_3multipleInterfaceAgents);
        TestUtils.waitForAgentInitialised(mspInterfaceAgent_4multipleInterfaceAgents);
        TestUtils.waitForAgentInitialised(mspInterfaceAgent_5multipleInterfaceAgents);
        TestUtils.waitForAgentInitialised(mspInterfaceAgent_6multipleInterfaceAgents);
        TestUtils.waitForAgentInitialised(mspInterfaceAgent_7multipleInterfaceAgents);
        TestUtils.waitForAgentInitialised(mspInterfaceAgent_8multipleInterfaceAgents);
        TestUtils.waitForAgentInitialised(mspInterfaceAgent_9multipleInterfaceAgents);
        MentalStateManager msmSellerAgent1_0multipleSellers = MSMRepository.getInstance().get("SellerAgent1_0multipleSellers");
        MentalStateManager msmSellerAgent1_1multipleSellers = MSMRepository.getInstance().get("SellerAgent1_1multipleSellers");
        MentalStateManager msmSellerAgent1_2multipleSellers = MSMRepository.getInstance().get("SellerAgent1_2multipleSellers");
        MentalStateManager msmSellerAgent1_3multipleSellers = MSMRepository.getInstance().get("SellerAgent1_3multipleSellers");
        MentalStateManager msmSellerAgent1_4multipleSellers = MSMRepository.getInstance().get("SellerAgent1_4multipleSellers");
        User_wants_to_watch_a_movie nevent = new User_wants_to_watch_a_movie();
        try {
            msmInterfaceAgent_0multipleInterfaceAgents.addMentalEntity(nevent);
            msmInterfaceAgent_1multipleInterfaceAgents.addMentalEntity(nevent);
            msmInterfaceAgent_2multipleInterfaceAgents.addMentalEntity(nevent);
            msmInterfaceAgent_3multipleInterfaceAgents.addMentalEntity(nevent);
            msmInterfaceAgent_4multipleInterfaceAgents.addMentalEntity(nevent);
            msmInterfaceAgent_5multipleInterfaceAgents.addMentalEntity(nevent);
            msmInterfaceAgent_6multipleInterfaceAgents.addMentalEntity(nevent);
            msmInterfaceAgent_7multipleInterfaceAgents.addMentalEntity(nevent);
            msmInterfaceAgent_8multipleInterfaceAgents.addMentalEntity(nevent);
            msmInterfaceAgent_9multipleInterfaceAgents.addMentalEntity(nevent);
        } catch (InvalidEntity e) {
            e.printStackTrace();
        }
        checkTicketsPreDeliver(msmBuyerAgent_0multipleBuyers);
        checkTicketsPreDeliver(msmBuyerAgent_1multipleBuyers);
        checkTicketsPreDeliver(msmBuyerAgent_2multipleBuyers);
        checkTicketsPreDeliver(msmBuyerAgent_3multipleBuyers);
        checkTicketsPreDeliver(msmBuyerAgent_4multipleBuyers);
        checkTicketsPreDeliver(msmBuyerAgent_5multipleBuyers);
        checkTicketsPreDeliver(msmBuyerAgent_6multipleBuyers);
        checkTicketsPreDeliver(msmBuyerAgent_7multipleBuyers);
        checkTicketsPreDeliver(msmBuyerAgent_8multipleBuyers);
        checkTicketsPreDeliver(msmBuyerAgent_9multipleBuyers);
        MainInteractionManager.goAutomatic();
        TestUtils.doNothing(40000);
        checkTicketsPostDeliver(msmBuyerAgent_0multipleBuyers);
        checkTicketsPostDeliver(msmBuyerAgent_1multipleBuyers);
        checkTicketsPostDeliver(msmBuyerAgent_2multipleBuyers);
        checkTicketsPostDeliver(msmBuyerAgent_3multipleBuyers);
        checkTicketsPostDeliver(msmBuyerAgent_4multipleBuyers);
        checkTicketsPostDeliver(msmBuyerAgent_5multipleBuyers);
        checkTicketsPostDeliver(msmBuyerAgent_6multipleBuyers);
        checkTicketsPostDeliver(msmBuyerAgent_7multipleBuyers);
        checkTicketsPostDeliver(msmBuyerAgent_8multipleBuyers);
        checkTicketsPostDeliver(msmBuyerAgent_9multipleBuyers);
    }

    private void checkTicketsPostDeliver(MentalStateManager msmBuyerAgent_0multipleBuyers) {
        Vector<MentalEntity> tickets = msmBuyerAgent_0multipleBuyers.getMentalEntityByType("Ticket_data");
        assertTrue("There should be exactly no Ticket_data " + "and there were " + tickets + " in agent " + msmBuyerAgent_0multipleBuyers.getAgentName() + ". Current state is " + msmBuyerAgent_0multipleBuyers.getAllMentalEntities(), tickets.size() == 0);
    }

    private void checkTicketsPreDeliver(MentalStateManager msmBuyerAgent_0multipleBuyers) {
        Vector<MentalEntity> tickets = msmBuyerAgent_0multipleBuyers.getMentalEntityByType("Ticket_data");
        assertTrue("There should be no any Ticket_data entities " + "and there were " + tickets + " at " + msmBuyerAgent_0multipleBuyers.getAgentName(), tickets.size() == 0);
    }
}
