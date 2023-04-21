package com.tilab.wsig.examples;

import java.util.Date;
import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FIPAManagementOntology;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import org.apache.log4j.Logger;

public class MathAgent extends Agent {

    public static final String WSIG_FLAG = "wsig";

    public static final String WSIG_MAPPER = "wsig-mapper";

    public static final String WSIG_PREFIX = "wsig-prefix";

    private Logger log = Logger.getLogger(MathAgent.class.getName());

    public static AID myAID = null;

    private SLCodec codec = new SLCodec();

    private Date startDate;

    protected void setup() {
        log.info("A MathAgent is starting...");
        log.info("Agent name: " + getLocalName());
        Object[] args = getArguments();
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(FIPAManagementOntology.getInstance());
        getContentManager().registerOntology(MathOntology.getInstance());
        DFAgentDescription dfad = new DFAgentDescription();
        dfad.setName(this.getAID());
        dfad.addLanguages(codec.getName());
        dfad.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
        ServiceDescription sd;
        sd = new ServiceDescription();
        sd.addLanguages(codec.getName());
        sd.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
        sd.setType("MathAgent");
        sd.setOwnership("MathOwner");
        sd.addOntologies(MathOntology.getInstance().getName());
        sd.addProperties(new Property(WSIG_FLAG, "true"));
        String wsigServiceName = "Math";
        if (args.length >= 1) {
            wsigServiceName = (String) args[0];
        }
        log.info("Service name: " + wsigServiceName);
        sd.setName(wsigServiceName);
        boolean isMapperPresent = false;
        if (args.length >= 2) {
            isMapperPresent = Boolean.parseBoolean((String) args[1]);
        }
        log.info("Mapper present: " + isMapperPresent);
        if (isMapperPresent) {
            sd.addProperties(new Property(WSIG_MAPPER, "com.tilab.wsig.examples.MathOntologyMapper"));
        }
        String wsigPrefix = "";
        if (args.length >= 3) {
            wsigPrefix = (String) args[2];
        }
        log.info("Prefix: " + wsigPrefix);
        if (wsigPrefix != null && !wsigPrefix.equals("")) {
            sd.addProperties(new Property(WSIG_PREFIX, wsigPrefix));
        }
        dfad.addServices(sd);
        try {
            DFService.register(this, dfad);
        } catch (Exception e) {
            log.error("Problem during DF registration", e);
            doDelete();
        }
        log.debug("A MathAgent is started.");
        startDate = new Date();
        this.addBehaviour(new CyclicBehaviour(this) {

            private MessageTemplate template = MessageTemplate.MatchOntology(MathOntology.getInstance().getName());

            public void action() {
                ACLMessage msg = myAgent.receive(template);
                if (msg != null) {
                    Action actExpr;
                    try {
                        actExpr = (Action) myAgent.getContentManager().extractContent(msg);
                        AgentAction action = (AgentAction) actExpr.getAction();
                        if (action instanceof Sum) {
                            serveSumAction((Sum) action, actExpr, msg);
                        } else if (action instanceof Diff) {
                            serveDiffAction((Diff) action, actExpr, msg);
                        } else if (action instanceof Abs) {
                            serveAbsAction((Abs) action, actExpr, msg);
                        } else if (action instanceof Multiplication) {
                            serveMultiplicationAction((Multiplication) action, actExpr, msg);
                        } else if (action instanceof SumComplex) {
                            serveSumComplexAction((SumComplex) action, actExpr, msg);
                        } else if (action instanceof GetComponents) {
                            serveGetComponentsAction((GetComponents) action, actExpr, msg);
                        } else if (action instanceof GetRandom) {
                            serveGetRandomAction((GetRandom) action, actExpr, msg);
                        } else if (action instanceof PrintComplex) {
                            servePrintComplexAction((PrintComplex) action, actExpr, msg);
                        } else if (action instanceof GetAgentInfo) {
                            serveGetAgentInfoAction((GetAgentInfo) action, actExpr, msg);
                        } else if (action instanceof ConvertDate) {
                            serveConvertDateAction((ConvertDate) action, actExpr, msg);
                        } else if (action instanceof PrintTime) {
                            servePrintTimeAction((PrintTime) action, actExpr, msg);
                        }
                    } catch (Exception e) {
                        log.error("Exception: " + e.getMessage(), e);
                    }
                } else {
                    block();
                }
            }
        });
    }

    private void serveAbsAction(Abs abs, Action actExpr, ACLMessage msg) {
        log.debug("MathAgent.serveAbsAction");
        float real = abs.getComplex().getReal();
        float immaginary = abs.getComplex().getImmaginary();
        float result = Float.parseFloat(Double.toString(Math.sqrt(Math.pow(real, 2) + Math.pow(immaginary, 2))));
        sendNotification(actExpr, msg, ACLMessage.INFORM, result);
    }

    private void serveDiffAction(Diff diff, Action actExpr, ACLMessage msg) {
        log.debug("MathAgent.serveDiffAction");
        float result = diff.getFirstElement() - diff.getSecondElement();
        sendNotification(actExpr, msg, ACLMessage.INFORM, result);
    }

    private void serveSumAction(Sum sum, Action actExpr, ACLMessage msg) {
        log.debug("MathAgent.serveSumAction");
        float result = sum.getFirstElement() + sum.getSecondElement();
        sendNotification(actExpr, msg, ACLMessage.INFORM, result);
    }

    private void serveMultiplicationAction(Multiplication multiplication, Action actExpr, ACLMessage msg) {
        log.debug("MathAgent.serveMultiplicationAction");
        double result = 1;
        for (int i = 0; i < multiplication.getNumbers().size(); i++) {
            result *= ((Double) multiplication.getNumbers().get(i));
        }
        sendNotification(actExpr, msg, ACLMessage.INFORM, result);
    }

    private void serveSumComplexAction(SumComplex sumComplex, Action actExpr, ACLMessage msg) {
        log.debug("MathAgent.serveSumComplexAction");
        Complex result = new Complex();
        result.setReal(sumComplex.getFirstComplexElement().getReal() + sumComplex.getSecondComplexElement().getReal());
        result.setImmaginary(sumComplex.getFirstComplexElement().getImmaginary() + sumComplex.getSecondComplexElement().getImmaginary());
        sendNotification(actExpr, msg, ACLMessage.INFORM, result);
    }

    private void serveGetComponentsAction(GetComponents getComponets, Action actExpr, ACLMessage msg) {
        log.debug("MathAgent.serveGetComponentsAction");
        List result = new ArrayList();
        result.add(getComponets.getComplex().getReal());
        result.add(getComponets.getComplex().getImmaginary());
        sendNotification(actExpr, msg, ACLMessage.INFORM, result);
    }

    private void serveGetRandomAction(GetRandom rnd, Action actExpr, ACLMessage msg) {
        log.debug("MathAgent.serveGetRandomAction");
        Complex result = new Complex();
        result.setReal((float) Math.random() * 10);
        result.setImmaginary((float) Math.random() * 10);
        sendNotification(actExpr, msg, ACLMessage.INFORM, result);
    }

    private void serveGetAgentInfoAction(GetAgentInfo getAgentInfo, Action actExpr, ACLMessage msg) {
        log.debug("MathAgent.serveGetAgentInfoAction");
        AgentInfo result = new AgentInfo();
        result.setAgentAid(getAID());
        result.setStartDate(startDate);
        sendNotification(actExpr, msg, ACLMessage.INFORM, result);
    }

    private void serveConvertDateAction(ConvertDate convertDate, Action actExpr, ACLMessage msg) {
        log.debug("MathAgent.serveConvertDateAction");
        long result = convertDate.getDate().getTime();
        sendNotification(actExpr, msg, ACLMessage.INFORM, Long.valueOf(result).toString());
    }

    private void servePrintComplexAction(PrintComplex printComplex, Action actExpr, ACLMessage msg) {
        log.debug("MathAgent.servePrintComplexAction");
        log.info("Complex number is " + printComplex.getComplex());
        sendNotification(actExpr, msg, ACLMessage.INFORM, null);
    }

    private void servePrintTimeAction(PrintTime printTime, Action actExpr, ACLMessage msg) {
        log.debug("MathAgent.servePrintTimeAction");
        log.info("Time is " + (new Date()).toString());
        sendNotification(actExpr, msg, ACLMessage.INFORM, null);
    }

    private void sendNotification(Action actExpr, ACLMessage request, int performative, Object result) {
        ACLMessage reply = request.createReply();
        if (performative == ACLMessage.INFORM) {
            reply.setPerformative(ACLMessage.INFORM);
            try {
                ContentElement ce = null;
                if (result != null) {
                    if (result instanceof java.util.List) {
                        ArrayList l = new ArrayList();
                        l.fromList((java.util.List) result);
                        result = l;
                    }
                    ce = new Result(actExpr, result);
                } else {
                    ce = new Done(actExpr);
                }
                getContentManager().fillContent(reply, ce);
            } catch (Exception e) {
                log.error("Agent " + getName() + ": Unable to send notification" + e);
                e.printStackTrace();
            }
        } else {
            reply.setPerformative(performative);
        }
        reply.addUserDefinedParameter(ACLMessage.IGNORE_FAILURE, "true");
        send(reply);
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (Exception e) {
            log.error(e);
        }
        log.debug("A MathAgent is taken down now.");
    }
}
