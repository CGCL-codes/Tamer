package ingenias.jade.smachines;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.core.behaviours.*;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import jade.core.*;
import jade.core.behaviours.*;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.domain.DFService;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import java.util.*;
import ingenias.jade.*;
import ingenias.editor.entities.*;
import ingenias.jade.comm.DefaultCommControl;
import ingenias.jade.comm.StateBehavior;
import ingenias.jade.comm.CommActCreator;
import ingenias.jade.exception.NoAgentsFound;

public class NetworkRCoordIntStateBehavior extends StateBehavior {

    private MentalStateReader msr = null;

    public NetworkRCoordIntStateBehavior(String agentName, MentalStateReader msr, MentalStateUpdater msu, RuntimeConversation conv, String playedRole, DFAgentDescription[] actors, DefaultCommControl dcc, String protocol) {
        super(conv, playedRole, msu, actors, dcc, protocol, agentName);
        this.msr = msr;
        try {
            if (IAFProperties.getGraphicsOn()) {
                smf.add("disabled", "CanYouHelpIU");
                smf.add("AnswerHelp", "waiting for AnswerHelp");
                smf.add("waiting for AnswerHelp", "endAnswerHelp");
                smf.add("CanYouHelpIU", "AnswerHelp");
                this.updateStates(agentName);
            }
        } catch (ingenias.exception.CycleInProtocol cip) {
            cip.printStackTrace();
        }
        this.addState("disabled");
    }

    public synchronized void action() {
        boolean cond1 = true;
        boolean cond2 = true;
        if (this.isState("disabled")) {
            try {
                String[] options = new String[] { "CanYouHelpIU" };
                AID[] actors = null;
                Vector<AID> actorsv = new Vector<AID>();
                Vector<String> rolesv = new Vector<String>();
                actorsv.addAll(this.getActor("CoordinatorR"));
                {
                    Vector<AID> receivers = this.getActor("CoordinatorR");
                    for (AID aid : receivers) {
                        rolesv.add("CoordinatorR");
                    }
                }
                actors = new AID[actorsv.size()];
                actorsv.toArray(actors);
                CommActCreator.generateSObject((JADEAgent) myAgent, rolesv, actors, this.getConversationID(), "enable", "CoordInt", this.getAgentExternalDescription());
                this.getDCC().notifyMessageSent("disabled", options, this);
                this.setRunning();
            } catch (NoAgentsFound e) {
                e.printStackTrace();
            }
        }
        if (this.isState("AnswerHelp")) {
            Vector options = new Vector();
            options.add("endAnswerHelp");
            String[] optionsA = new String[options.size()];
            options.toArray(optionsA);
            boolean allexist = true;
            int cardinality = 1;
            if ("n".equals("n")) {
                try {
                    Vector<AID> receivers = this.getActor("CoordinatorR");
                    cardinality = receivers.size();
                } catch (NoAgentsFound ex) {
                    ex.printStackTrace();
                }
            }
            if (allexist && true) {
                CommActCreator.generateR((JADEAgent) myAgent, this.getConversationID(), "AnswerHelp", "CoordInt", optionsA, this, cardinality, 0);
            }
            this.removeState("AnswerHelp");
            this.addState("waiting for AnswerHelp");
        }
        if (this.isState("CanYouHelpIU")) {
            try {
                AID[] actors = null;
                Vector actorsv = new Vector();
                Vector<String> rolesv = new Vector<String>();
                {
                    Vector<AID> receivers = this.getActor("CoordinatorR");
                    actorsv.addAll(receivers);
                    for (AID aid : receivers) {
                        rolesv.add("CoordinatorR");
                    }
                }
                actors = new AID[actorsv.size()];
                actorsv.toArray(actors);
                Vector options = new Vector();
                options.add("AnswerHelp");
                String[] optionsA = new String[options.size()];
                options.toArray(optionsA);
                if (this.getDCC().notifyMessageSent("CanYouHelpIU", optionsA, this)) {
                    CommActCreator.generateSObject((JADEAgent) myAgent, rolesv, actors, this.getConversationID(), "CanYouHelpIU", "CoordInt", this.getContentForNextMessage());
                    getTimeout().stop();
                } else {
                    if (getTimeout().isStarted() && getTimeout().isFinished()) {
                        this.abortDueTimeout();
                    } else {
                        if (!getTimeout().isStarted()) getTimeout().start(0);
                    }
                }
            } catch (NoAgentsFound e) {
                e.printStackTrace();
            }
        }
        if (this.isState("endAnswerHelp")) {
            this.setFinished();
            this.getDCC().removeDefaultLocks();
        }
        if (this.isState("ABORTED")) {
            this.getDCC().removeDefaultLocks();
        }
    }
}
