package robot;

import jade.core.*;
import jade.core.behaviours.*;
import jade.domain.*;
import jade.domain.FIPAAgentManagement.*;
import jade.content.*;
import jade.content.lang.*;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.leap.LEAPCodec;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import jade.content.onto.basic.*;
import jade.lang.acl.*;
import jade.proto.ContractNetInitiator;
import jade.proto.ContractNetResponder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JFrame;
import utils.*;
import ontologies.*;

public class SimpleRobotAgent extends Agent implements RobotsVocabulary {

    protected int xPos;

    protected int yPos;

    protected int id;

    protected boolean inMove;

    protected PriorityQueue<Task> tasks;

    protected ArrayList<Fact> facts;

    protected ArrayList<String> conversations;

    protected Codec codec = new LEAPCodec();

    protected Ontology ontology = RobotsOntology.getInstance();

    protected MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchLanguage(codec.getName()), MessageTemplate.MatchOntology(ontology.getName()));

    public int getId() {
        return this.id;
    }

    public boolean propagated(String convID) {
        return this.conversations.contains(convID);
    }

    public void addConvId(String convID) {
        if (!propagated(convID)) this.conversations.add(convID);
    }

    public int countDeadline(Task task) {
        if (tasks.peek() != null) return 1;
        return 0;
    }

    /**
 * lists agents knowledge base
 */
    public void list() {
        System.out.println("**********************************");
        System.out.println("Agent's " + id + "knowledge base:");
        for (Fact fact : facts) System.out.println("- object id: " + fact.getId() + " X pos: " + fact.getPosX() + " Y pos: " + fact.getPosY() + " time: " + fact.getTime());
        System.out.println("**********************************");
    }

    protected class EnvRequestBehav extends OneShotBehaviour {

        public void action() {
            DFAgentDescription dfd = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("ENVIRONMENT");
            dfd.addServices(sd);
            ArrayList<AID> aids = new ArrayList<AID>();
            try {
                DFAgentDescription[] result = DFService.search(myAgent, dfd);
                if (result.length > 0) for (int i = 0; i < result.length; i++) {
                    MessageInfo m = new MessageInfo(id, 0, xPos, yPos, (float) 1000.0);
                    m.setF(new Fact(id, xPos, yPos, new Date()));
                    ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                    msg.addReceiver(result[i].getName());
                    msg.setLanguage(codec.getName());
                    msg.setOntology(ontology.getName());
                    msg.setConversationId("forYourEyesOnly");
                    try {
                        getContentManager().fillContent(msg, m);
                        send(msg);
                    } catch (CodecException ce) {
                        ce.printStackTrace();
                    } catch (OntologyException oe) {
                        oe.printStackTrace();
                    }
                }
            } catch (FIPAException e) {
                e.printStackTrace();
            }
        }
    }

    /**
 * Gives location data to the Environment agent
 *
 */
    protected class GiveLocateTaskBehav extends OneShotBehaviour {

        private AID employeeAID;

        private int employeeId;

        private int objectId;

        private int priority;

        public GiveLocateTaskBehav(AID employeeAID, int employeeId, int objectId, int priority) {
            this.employeeAID = employeeAID;
            this.employeeId = employeeId;
            this.objectId = objectId;
            this.priority = priority;
        }

        public void action() {
            MessageInfo mi = new MessageInfo(id, employeeId, xPos, yPos, (float) 100.0);
            LocateTask lt = new LocateTask(id, priority, objectId);
            mi.setLt(lt);
            ACLMessage msg = new ACLMessage(ACLMessage.CFP);
            msg.addReceiver(employeeAID);
            msg.setLanguage(codec.getName());
            msg.setOntology(ontology.getName());
            msg.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
            try {
                getContentManager().fillContent(msg, mi);
            } catch (CodecException ce) {
                ce.printStackTrace();
            } catch (OntologyException oe) {
                oe.printStackTrace();
            }
            System.out.println(getName() + " gives LOCATE task to " + employeeAID.getName());
            addBehaviour(new RobotsContractNetInitiator(myAgent, msg, countDeadline(lt)));
        }
    }

    /** Give someone a task to locate something
 * 
 * 
 */
    protected class GiveCheckLocationTaskBehav extends OneShotBehaviour {

        private AID employeeAID;

        private int employeeId;

        private int xCoord;

        private int yCoord;

        private int priority;

        public GiveCheckLocationTaskBehav(AID employeeAID, int employeeId, int xCoord, int yCoord, int priority) {
            this.employeeAID = employeeAID;
            this.employeeId = employeeId;
            this.xCoord = xCoord;
            this.yCoord = yCoord;
            this.priority = priority;
        }

        public void action() {
            MessageInfo mi = new MessageInfo(id, employeeId, xPos, yPos, (float) 100.0);
            CheckLocationTask clt = new CheckLocationTask(id, priority, xCoord, yCoord);
            mi.setClt(clt);
            ACLMessage msg = new ACLMessage(ACLMessage.CFP);
            msg.addReceiver(employeeAID);
            msg.setLanguage(codec.getName());
            msg.setOntology(ontology.getName());
            msg.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
            try {
                getContentManager().fillContent(msg, mi);
            } catch (CodecException ce) {
                ce.printStackTrace();
            } catch (OntologyException oe) {
                oe.printStackTrace();
            }
            System.out.println(getName() + " gives CHECK LOCATION task to " + employeeAID.getName());
            addBehaviour(new RobotsContractNetInitiator(myAgent, msg, countDeadline(clt)));
        }
    }

    /** FIPA ContractNet protocol Implementation
 * 
 * 
 */
    protected class RobotsContractNetInitiator extends ContractNetInitiator {

        private int myDeadline;

        public RobotsContractNetInitiator(Agent a, ACLMessage cfp, int myDeadline) {
            super(a, cfp);
            this.myDeadline = myDeadline;
        }

        protected void handlePropose(ACLMessage propose, Vector v) {
            System.out.println("Agent " + propose.getSender().getName() + " proposed something to " + myAgent.getName());
            try {
                ContentElement content = getContentManager().extractContent(propose);
                MessageInfo info = (MessageInfo) content;
                int deadline = info.getDeadline();
                if (deadline == 0) {
                    ACLMessage reply = propose.createReply();
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    v.addElement(reply);
                    System.out.println(myAgent.getName() + " accepted proposition of " + propose.getSender().getName());
                } else {
                    ACLMessage reply = propose.createReply();
                    reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    v.addElement(reply);
                    System.out.println(myAgent.getName() + " rejected proposition of " + propose.getSender().getName());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    protected class RobotsContractNetResponder extends ContractNetResponder {

        private MessageInfo taskMI;

        public RobotsContractNetResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        protected ACLMessage handleCfp(ACLMessage cfp) {
            ACLMessage reply = cfp.createReply();
            reply.setPerformative(ACLMessage.PROPOSE);
            int deadline;
            try {
                ContentElement content = getContentManager().extractContent(cfp);
                MessageInfo info = (MessageInfo) content;
                taskMI = info;
                if (info.getLt() != null) {
                    deadline = countDeadline(info.getLt());
                } else {
                    deadline = countDeadline(info.getClt());
                }
                MessageInfo mi = new MessageInfo(id, info.getMainSenderId(), xPos, yPos, (float) 100.0);
                mi.setDeadline(deadline);
                getContentManager().fillContent(reply, mi);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println(myAgent.getName() + " received job proposal from " + cfp.getSender().getName());
            return reply;
        }

        protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
            ACLMessage result = accept.createReply();
            try {
                MessageInfo mi = new MessageInfo();
                if (taskMI.getLt() != null) {
                    LocateTask lt = taskMI.getLt();
                    tasks.add(lt);
                } else {
                    CheckLocationTask clt = taskMI.getClt();
                    tasks.add(clt);
                }
                System.out.println(myAgent.getName() + " works for " + accept.getSender().getName());
                getContentManager().fillContent(result, mi);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return result;
        }
    }

    /**
 * Some deep stuff to check if we see something (communicates with env)
 * @author lisu
 *
 */
    protected class ListenBehav extends CyclicBehaviour {

        MessageTemplate mTempl = MessageTemplate.and(mt, MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE), MessageTemplate.MatchPerformative(ACLMessage.INFORM)));

        public void action() {
            ACLMessage msg = receive(mTempl);
            if (msg != null) {
                if (msg.getPerformative() == ACLMessage.PROPAGATE) {
                    addBehaviour(new PropBehav(msg));
                } else if (msg.getPerformative() == ACLMessage.INFORM) {
                    try {
                        ContentElement content = getContentManager().extractContent(msg);
                        MessageInfo info = (MessageInfo) content;
                        Fact fact = info.getF();
                        if (fact.getId() != id) {
                            if (fact.getId() < 100) {
                                for (Fact newFact : facts) {
                                    if (fact.getId() == newFact.getId()) {
                                        if (newFact.getTime().getTime() - fact.getTime().getTime() < 5000) {
                                            addBehaviour(new SendAllIKnowBehav(fact.getId()));
                                            if (tasks.peek() != null) {
                                                if (tasks.element() instanceof LocateTask) {
                                                    LocateTask current = (LocateTask) tasks.element();
                                                    addBehaviour(new GiveLocateTaskBehav(new AID((new Integer(fact.getId())).toString(), AID.ISLOCALNAME), fact.getId(), current.getObjectId(), current.getPriority()));
                                                } else {
                                                    CheckLocationTask current = (CheckLocationTask) tasks.element();
                                                    addBehaviour(new GiveCheckLocationTaskBehav(new AID((new Integer(fact.getId())).toString(), AID.ISLOCALNAME), fact.getId(), current.getPosX(), current.getPosY(), current.getPriority()));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            ArrayList<Fact> toRemove = new ArrayList<Fact>();
                            for (Fact newFact : facts) if (fact.getId() == newFact.getId() && fact.getTime().after(newFact.getTime())) toRemove.add(newFact);
                            for (Fact newFact : toRemove) facts.remove(newFact);
                            facts.add(fact);
                            if (facts.size() > 100) facts.remove(0);
                            list();
                        }
                        if (tasks.peek() != null) {
                            Task currentTask = tasks.element();
                            if (currentTask instanceof LocateTask) {
                                if (((LocateTask) currentTask).getObjectId() == fact.getId()) {
                                    FoundIt raport = new FoundIt("Object " + fact.getId() + " is in (" + fact.getPosX() + "," + fact.getPosY() + ")");
                                    raport.show();
                                    System.out.println("Task completed!");
                                    System.out.println("Object " + fact.getId() + " is in (" + fact.getPosX() + "," + fact.getPosY() + ")");
                                    ACLMessage message = new ACLMessage(ACLMessage.PROPAGATE);
                                    message.setLanguage(codec.getName());
                                    message.setOntology(ontology.getName());
                                    MessageInfo mi = new MessageInfo(id, currentTask.getEmployerId(), xPos, yPos, Globals.RANGE);
                                    mi.setTaskCompletion(1);
                                    getContentManager().fillContent(message, mi);
                                    send(message);
                                    tasks.remove();
                                } else {
                                    for (Fact newFact : facts) {
                                        if (newFact.getId() == ((LocateTask) currentTask).getObjectId()) {
                                            Date now = new Date();
                                            if (now.getTime() - newFact.getTime().getTime() < 10000) {
                                                FoundIt raport = new FoundIt("Object " + newFact.getId() + " is in (" + newFact.getPosX() + "," + newFact.getPosY() + ")");
                                                raport.show();
                                                System.out.println("Task completed!");
                                                System.out.println("Object " + newFact.getId() + " is in (" + newFact.getPosX() + "," + newFact.getPosY() + ")");
                                                ACLMessage message = new ACLMessage(ACLMessage.PROPAGATE);
                                                message.setLanguage(codec.getName());
                                                message.setOntology(ontology.getName());
                                                MessageInfo mi = new MessageInfo(id, currentTask.getEmployerId(), xPos, yPos, Globals.RANGE);
                                                mi.setTaskCompletion(1);
                                                getContentManager().fillContent(message, mi);
                                                send(message);
                                                tasks.remove();
                                            } else {
                                                tasks.add(new CheckLocationTask(id, currentTask.getPriority() - 1, newFact.getPosX(), newFact.getPosY()));
                                            }
                                            break;
                                        }
                                    }
                                }
                            } else {
                                if (((CheckLocationTask) currentTask).getPosX() == fact.getPosX() && ((CheckLocationTask) currentTask).getPosY() == fact.getPosY()) {
                                    FoundIt raport = new FoundIt("In (" + fact.getPosX() + "," + fact.getPosY() + ") is object " + fact.getId());
                                    raport.show();
                                    System.out.println("Task completed!");
                                    System.out.println("In (" + fact.getPosX() + "," + fact.getPosY() + ") is object " + fact.getId());
                                    ACLMessage message = new ACLMessage(ACLMessage.PROPAGATE);
                                    message.setLanguage(codec.getName());
                                    message.setOntology(ontology.getName());
                                    MessageInfo mi = new MessageInfo(id, currentTask.getEmployerId(), xPos, yPos, Globals.RANGE);
                                    mi.setTaskCompletion(1);
                                    getContentManager().fillContent(message, mi);
                                    send(message);
                                    tasks.remove();
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else if (!inMove) {
                double x, y;
                if (tasks.peek() != null) {
                    Task currentTask = tasks.element();
                    if (currentTask instanceof CheckLocationTask) {
                        x = ((CheckLocationTask) currentTask).getPosX();
                        y = ((CheckLocationTask) currentTask).getPosY();
                        if ((xPos - x) * (xPos - x) + (yPos - y) * (yPos - y) < Globals.SIGHT_RANGE * Globals.SIGHT_RANGE) {
                            tasks.remove();
                            x = (Math.random() * 500);
                            y = (Math.random() * 500);
                        }
                    } else {
                        x = (Math.random() * 500);
                        y = (Math.random() * 500);
                    }
                } else {
                    x = (Math.random() * 500);
                    y = (Math.random() * 500);
                }
                addBehaviour(new MoveBehav((int) x, (int) y));
            } else {
            }
        }
    }

    /** Propagate behaviour - to implement propagated messages
   * 
   * 
   */
    protected class PropBehav extends OneShotBehaviour {

        ACLMessage message;

        MessageInfo mi;

        public PropBehav(ACLMessage msg) {
            message = new ACLMessage(ACLMessage.PROPAGATE);
            message.setLanguage(codec.getName());
            message.setOntology(ontology.getName());
            message.setConversationId(msg.getConversationId());
            try {
                ContentElement ce = getContentManager().extractContent(msg);
                mi = (MessageInfo) ce;
                getContentManager().fillContent(message, ce);
            } catch (Exception fe) {
                fe.printStackTrace();
            }
        }

        public void action() {
            if (!propagated(message.getConversationId())) {
                if ((mi.getSenderPosX() - xPos) * (mi.getSenderPosX() - xPos) + (mi.getSenderPosY() - yPos) * (mi.getSenderPosY() - yPos) < Globals.RANGE * Globals.RANGE) {
                    if (mi.getMainReceiverId() == id) {
                        if (mi.getTaskCompletion() == 1) {
                            Fact fact = mi.getF();
                            if (fact.getId() != id) {
                                ArrayList<Fact> toRemove = new ArrayList<Fact>();
                                for (Fact newFact : facts) if (fact.getId() == newFact.getId() && fact.getTime().after(newFact.getTime())) toRemove.add(newFact);
                                for (Fact newFact : toRemove) facts.remove(newFact);
                                facts.add(fact);
                                if (facts.size() > 100) facts.remove(0);
                                list();
                            }
                            ACLMessage message = new ACLMessage(ACLMessage.PROPAGATE);
                            message.setLanguage(codec.getName());
                            message.setOntology(ontology.getName());
                            MessageInfo mes = new MessageInfo(id, mi.getMainSenderId(), xPos, yPos, Globals.RANGE);
                            mes.setAcomplishment(1);
                            try {
                                getContentManager().fillContent(message, mes);
                                send(message);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (mi.getAcomplishment() == 1) ; else {
                            Fact fact = mi.getF();
                            if (fact.getId() != id) {
                                ArrayList<Fact> toRemove = new ArrayList<Fact>();
                                for (Fact newFact : facts) if (fact.getId() == newFact.getId() && fact.getTime().after(newFact.getTime())) toRemove.add(newFact);
                                for (Fact newFact : toRemove) facts.remove(newFact);
                                facts.add(fact);
                                if (facts.size() > 100) facts.remove(0);
                                list();
                            }
                        }
                    } else {
                        DFAgentDescription dfd = new DFAgentDescription();
                        ServiceDescription sd = new ServiceDescription();
                        sd.setType("ROBOT");
                        dfd.addServices(sd);
                        ArrayList<AID> aids = new ArrayList<AID>();
                        try {
                            DFAgentDescription[] result = DFService.search(myAgent, dfd);
                            if (result.length > 0) for (int i = 0; i < result.length; i++) {
                                if (!myAgent.getAID().equals(result[i].getName())) {
                                    aids.add(result[i].getName());
                                }
                            }
                        } catch (FIPAException e) {
                            e.printStackTrace();
                        } catch (Exception fe) {
                            fe.printStackTrace();
                        }
                        for (int i = 0; i < aids.size(); i++) {
                            message.addReceiver(aids.get(i));
                        }
                        addConvId(message.getConversationId());
                        send(message);
                    }
                }
            }
        }
    }

    protected class MoveBehav extends OneShotBehaviour {

        protected int xBeg, xDest, yBeg, yDest;

        protected double x, y, distance;

        public MoveBehav(int xd, int yd) {
            xBeg = xPos;
            yBeg = yPos;
            xDest = xd;
            yDest = yd;
            distance = Math.sqrt((xBeg - xDest) * (xBeg - xDest) + (yBeg - yDest) * (yBeg - yDest)) / 10;
            inMove = true;
        }

        public void action() {
            x = xBeg;
            y = yBeg;
            TickerBehaviour loop = new TickerBehaviour(myAgent, 150) {

                public void onTick() {
                    if (Math.abs(xPos - xDest) < 10 && Math.abs(yPos - yDest) < 10) {
                        inMove = false;
                        stop();
                    } else {
                        x += (xDest - xBeg) / distance;
                        y += (yDest - yBeg) / distance;
                        xPos = (int) x;
                        yPos = (int) y;
                        addBehaviour(new EnvRequestBehav());
                    }
                }
            };
            addBehaviour(loop);
        }
    }

    /**
 * Name tells everything
 *
 */
    protected class SendAllIKnowBehav extends OneShotBehaviour {

        private int receiverId;

        public SendAllIKnowBehav(int id) {
            receiverId = id;
        }

        public void action() {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n " + "I'M SHARING WITH AGENT " + receiverId);
            for (Fact fact : facts) {
                ACLMessage message = new ACLMessage(ACLMessage.PROPAGATE);
                message.setLanguage(codec.getName());
                message.setOntology(ontology.getName());
                MessageInfo mi = new MessageInfo(id, receiverId, xPos, yPos, Globals.RANGE);
                mi.setF(fact);
                try {
                    getContentManager().fillContent(message, mi);
                    send(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            id = Integer.parseInt((String) args[0]);
            xPos = Integer.parseInt((String) args[1]);
            yPos = Integer.parseInt((String) args[2]);
        }
        tasks = new PriorityQueue<Task>(10, new Comparator<Task>() {

            public int compare(Task i, Task j) {
                return i.getPriority() - j.getPriority();
            }
        });
        facts = new ArrayList<Fact>();
        conversations = new ArrayList<String>();
        inMove = false;
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
        ServiceDescription sd = new ServiceDescription();
        sd.setType("ROBOT");
        sd.setName(getLocalName());
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        } catch (Exception fe) {
            fe.printStackTrace();
        }
        addBehaviour(new EnvRequestBehav());
        addBehaviour(new RobotsContractNetResponder(this, ContractNetResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET)));
        addBehaviour(new ListenBehav());
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
