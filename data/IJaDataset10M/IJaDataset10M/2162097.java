package br.unb.bioagents.behaviours;

import jade.lang.acl.MessageTemplate;
import br.unb.bioagents.agents.BioAgent;
import br.unb.bioagents.ontology.RequestSugestion;

public abstract class CfpResponderBehaviour<AGENT_TYPE extends BioAgent<?, ?>> extends ResponderBehaviour<AGENT_TYPE> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8047417123005766868L;

    public CfpResponderBehaviour(AGENT_TYPE a, AGENT_TYPE.WorkingGroup workingGroup, RequestSugestion requestSugestion, MessageTemplate template) {
        super(a, workingGroup, requestSugestion, template);
    }
}
