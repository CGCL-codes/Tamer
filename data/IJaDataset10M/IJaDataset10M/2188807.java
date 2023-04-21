package gr.demokritos.iit.jinsect.distributed;

import jade.proto.*;
import jade.lang.acl.*;
import jade.domain.JADEAgentManagement.*;
import jade.domain.mobility.MobilityOntology;
import jade.domain.FIPANames;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;

/**
   * This behaviour extends SimpleAchieveREInitiator in order
   * to request to the AMS the list of available locations where
   * the agent can move.
   * Then, it displays these locations into the GUI
   * @author Fabio Bellifemine - CSELT S.p.A.
   * @version $Date: 2003-02-25 13:29:42 +0100 (mar, 25 feb 2003) $ $Revision: 3687 $
   */
public class GetAvailableLocationsBehaviour extends SimpleAchieveREInitiator {

    private ACLMessage request;

    public GetAvailableLocationsBehaviour(MobileAgent a) {
        super(a, new ACLMessage(ACLMessage.REQUEST));
        request = (ACLMessage) getDataStore().get(REQUEST_KEY);
        request.clearAllReceiver();
        request.addReceiver(a.getAMS());
        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
        request.setOntology(MobilityOntology.NAME);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        try {
            Action action = new Action();
            action.setActor(a.getAMS());
            action.setAction(new QueryPlatformLocationsAction());
            a.getContentManager().fillContent(request, action);
        } catch (Exception fe) {
            fe.printStackTrace();
        }
        reset(request);
    }

    protected void handleNotUnderstood(ACLMessage reply) {
        System.out.println(myAgent.getLocalName() + " handleNotUnderstood : " + reply.toString());
    }

    protected void handleRefuse(ACLMessage reply) {
        System.out.println(myAgent.getLocalName() + " handleRefuse : " + reply.toString());
    }

    protected void handleFailure(ACLMessage reply) {
        System.out.println(myAgent.getLocalName() + " handleFailure : " + reply.toString());
    }

    protected void handleAgree(ACLMessage reply) {
    }

    protected void handleInform(ACLMessage inform) {
        String content = inform.getContent();
        try {
            Result results = (Result) myAgent.getContentManager().extractContent(inform);
            ((MobileAgent) myAgent).updateLocations(results.getItems().iterator());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
