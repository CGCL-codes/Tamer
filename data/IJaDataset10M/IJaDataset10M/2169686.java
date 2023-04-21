package ingenias.jade.agents;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.core.behaviours.*;
import ingenias.jade.*;
import ingenias.jade.smachines.*;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import jade.core.*;
import jade.core.behaviours.*;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.domain.DFService;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import ingenias.jade.*;
import java.util.*;
import ingenias.jade.exception.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import ingenias.editor.entities.RuntimeFact;
import ingenias.jade.components.*;
import ingenias.jade.mental.*;
import ingenias.editor.entities.ApplicationEventSlots;
import ingenias.editor.entities.Interaction;
import ingenias.editor.entities.MentalEntity;
import ingenias.editor.entities.ObjectSlot;
import ingenias.editor.entities.RuntimeEvent;
import ingenias.editor.entities.RuntimeFact;
import ingenias.editor.entities.RuntimeConversation;
import ingenias.editor.entities.Slot;
import ingenias.jade.components.Task;
import ingenias.jade.graphics.*;
import ingenias.jade.MentalStateManager;
import ingenias.exception.InvalidEntity;

public class Provider1JADEAgent extends JADEAgent {

    public Provider1JADEAgent() {
        super(new Provider1Protocol(), new Provider1InteractionLocks());
    }

    private boolean initialiseNonConversationalTask(Task tobject) {
        boolean initialised = false;
        Vector<String> repeatedOutputs = new Vector<String>();
        Vector<String> nonExistingInputs = new Vector<String>();
        if (tobject.getType().equals("DeleteNonUsedEntitiesTask")) {
            Vector<MentalEntity> expectedInput = null;
            RuntimeFact expectedOutput = null;
            RuntimeEvent expectedOutputEvent = null;
            RuntimeConversation expectedInt = null;
            ingenias.jade.components.Resource expectedResource = null;
            ingenias.jade.components.Application expectedApp = null;
            boolean allEntitiesExist = true;
            TaskOutput to = null;
            to = new TaskOutput("default");
            expectedInput = this.getMSM().getMentalEntityByType("TicketProposal");
            if (expectedInput.size() == 0) {
                nonExistingInputs.add("TicketProposal");
            } else {
                addExpectedInputs(tobject, "TicketProposal", "1", expectedInput);
                addConsumedInput(to, "TicketProposal", expectedInput);
            }
            allEntitiesExist = allEntitiesExist && expectedInput.size() != 0;
            tobject.addOutput(to);
            initialised = allEntitiesExist;
            if (!allEntitiesExist) {
                StringBuffer nonexisting = new StringBuffer();
                for (int j = 0; j < nonExistingInputs.size(); j++) {
                    nonexisting.append(nonExistingInputs.elementAt(j).toString() + ",");
                }
            }
            return initialised;
        }
        return false;
    }

    private boolean initialiseConversationalTask(RuntimeConversation conversation, Task tobject) {
        boolean initialised = false;
        Vector<String> nonExistingInputs = new Vector<String>();
        Vector<String> repeatedOutputs = new Vector<String>();
        boolean validConversationType = false;
        validConversationType = validConversationType || conversation.getInteraction().getId().equalsIgnoreCase("RFP");
        if (validConversationType) {
            nonExistingInputs.clear();
            repeatedOutputs.clear();
            if (tobject.getType().equals("GenerateProposal1")) {
                Vector<MentalEntity> expectedInput = null;
                RuntimeFact expectedOutput = null;
                RuntimeConversation expectedInt = null;
                ingenias.jade.components.Resource expectedResource = null;
                ingenias.jade.components.Application expectedApp = null;
                TaskOutput to = null;
                to = new TaskOutput("default");
                tobject.setConversationContext(conversation);
                boolean allEntitiesExist = true;
                expectedInput = this.getMSM().obtainConversationalMentalEntityByType(conversation, "TicketRequest");
                if (expectedInput.size() == 0 && !("1".equals("0..n"))) nonExistingInputs.add("TicketRequest"); else {
                    addExpectedInputs(tobject, "TicketRequest", "1", expectedInput);
                    addConsumedInput(to, "1", expectedInput);
                }
                allEntitiesExist = allEntitiesExist && expectedInput.size() != 0;
                expectedApp = (ingenias.jade.components.Application) getAM().getApplication("YellowPages");
                tobject.addApplication("YellowPages", expectedApp);
                boolean alreadyExists = true;
                {
                    TicketProposal expectedOutputTicketProposal = new TicketProposal(MentalStateManager.generateMentalEntityID());
                    to.add(new OutputEntity(expectedOutputTicketProposal, TaskOperations.CreateWF));
                }
                tobject.addOutput(to);
                if (!allEntitiesExist) {
                    StringBuffer nonexisting = new StringBuffer();
                    for (int j = 0; j < nonExistingInputs.size(); j++) {
                        nonexisting.append(nonExistingInputs.elementAt(j).toString() + ",");
                    }
                    MainInteractionManager.log("Conversational intialisation discarded task " + tobject.getType() + " to achieve goal GetTickets because did not have all preconditions. Missing elements are:" + nonexisting + ". Currently, the MS has the following elements:" + this.getMSM().getAllMentalEntities(), getLocalName() + "-" + tobject.getType());
                }
                initialised = allEntitiesExist;
                return initialised;
            }
        }
        validConversationType = false;
        return false;
    }

    public Vector tasksThatSatisfyGoal(String goalname) {
        Vector tasks = new Vector();
        Vector<String> typesOfConversation = null;
        typesOfConversation = new Vector<String>();
        typesOfConversation.add("RFP");
        if (goalname.equals("GetTickets")) {
            {
                Task tobject = null;
                Vector<RuntimeConversation> conversations = getCM().getCurrentActiveConversations(typesOfConversation);
                boolean canbescheduled = false;
                for (int k = 0; k < conversations.size(); k++) {
                    tobject = new GenerateProposal1Task(ingenias.jade.MentalStateManager.generateMentalEntityID());
                    canbescheduled = initialiseConversationalTask(conversations.elementAt(k), tobject);
                    if (canbescheduled) {
                        MainInteractionManager.log("Scheduled task " + tobject.getType() + " to achieve goal GetTickets", getLocalName() + "-" + tobject.getType());
                        tasks.add(tobject);
                    }
                }
            }
        }
        Task tobject = new DeleteNonUsedEntitiesTask("DeleteNonUsedEntitiesTask", "DeleteNonUsedEntitiesTask");
        boolean canbescheduled = initialiseNonConversationalTask(tobject);
        if (canbescheduled) {
            tasks.add(tobject);
        }
        return tasks;
    }

    /**
	 *  Initializes the agent
	 */
    public void setup() {
        super.setup();
        Vector<String> ttypes = new Vector<String>();
        ttypes.add("GenerateProposal1");
        if (IAFProperties.getGraphicsOn()) this.getGraphics().setKnownTasks(ttypes);
        boolean continueInit = false;
        getCM().addKnownProtocol("RFP");
        ingenias.editor.entities.StateGoal sg = null;
        ingenias.editor.entities.RuntimeFact ff = null;
        Slot slot = null;
        ObjectSlot oslot = null;
        ingenias.jade.components.Application app = null;
        sg = new ingenias.editor.entities.StateGoal("GetTickets");
        sg.setState("pending");
        try {
            this.getMSM().addMentalEntity(sg);
        } catch (InvalidEntity e1) {
            e1.printStackTrace();
        }
        Vector events = null;
        RuntimeEvent event = null;
        Vector actions = null;
        Vector evetns = null;
        java.awt.event.ActionListener ifPressed = null;
        if (getGraphics() != null) getGraphics().startAgentDebug();
        this.agentInitialised();
    }

    /**
	 *  Obtains a DFAgentDescription array that describes the different roles an
       *  agent can play
	 *
	 *@return    Roles played
	 */
    public DFAgentDescription[] getDescription() {
        DFAgentDescription[] result = null;
        Vector playedRoles = new Vector();
        DFAgentDescription dfd = null;
        dfd = new DFAgentDescription();
        ServiceDescription sd = null;
        dfd.setName(getAID());
        sd = new ServiceDescription();
        sd.setName(getLocalName() + "-sub-df");
        sd.setType("Requestee");
        sd.setOwnership("JADE");
        dfd.addServices(sd);
        playedRoles.add(dfd);
        result = new DFAgentDescription[playedRoles.size()];
        playedRoles.toArray(result);
        return result;
    }
}
