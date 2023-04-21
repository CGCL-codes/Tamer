package edu.upc.lsi.kemlg.aws.test;

import edu.upc.lsi.kemlg.aws.SimplePercept;
import edu.upc.lsi.kemlg.aws.communication.performative.Suggest;
import edu.upc.lsi.kemlg.aws.engine.AgentKernel;
import edu.upc.lsi.kemlg.aws.exception.AgentException;
import edu.upc.lsi.kemlg.aws.input.domain.AgentScenario;
import edu.upc.lsi.kemlg.aws.io.Percept;
import edu.upc.lsi.kemlg.aws.knowledge.ontology.OntologyConcept;
import edu.upc.lsi.kemlg.aws.reasoning.behaviour.Behaviour;
import edu.upc.lsi.kemlg.aws.utils.AgentLogger;
import edu.upc.lsi.kemlg.aws.utils.MessageIDGenerator;

public class SimplePerceptBehaviour extends Behaviour {

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public boolean isSuitable(Percept percept) {
        return percept instanceof SimplePercept;
    }

    @Override
    protected void runRequest(AgentKernel comm) throws AgentException {
        SimplePercept sp;
        sp = (SimplePercept) percept;
        AgentLogger.log("SimplePercept received: " + sp.getStInput());
        if (sp.getStInput().equals("suggest")) {
            comm.putPercept(new Suggest(MessageIDGenerator.getMessageID(), MessageIDGenerator.getMessageID(), MessageIDGenerator.getMessageID(), "CTU", "UPC", new OntologyConcept(new AgentScenario())));
        }
    }
}
