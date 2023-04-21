package org.eucontract.agents;

import org.eucontract.agents.domain.AgentException;
import org.eucontract.agents.domain.Behaviour;
import org.eucontract.agents.domain.OntologyConcept;
import org.eucontract.agents.performatives.Performative;

public class ThProcess {

    private Processor th;

    private AgentEndpoint ae;

    private Performative performative;

    public ThProcess() {
    }

    /**
	 */
    public ThProcess(AgentEndpoint ae, Performative performative) {
        th = new Processor(ae, performative);
        this.ae = ae;
        this.performative = performative;
    }

    public void start() {
        if (th == null) {
            th = new Processor(ae, performative);
        }
        th.start();
    }

    public void join() throws InterruptedException {
        th.join();
    }

    public OntologyConcept getResult() throws AgentException {
        return th.getResult();
    }

    public AgentException getException() {
        return th.getException();
    }

    public void setBehaviour(Behaviour behaviour) {
        th.setBehaviour(behaviour);
    }

    public void setReady(boolean b) {
        th.setReady(true);
    }
}
