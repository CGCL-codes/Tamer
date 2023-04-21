package edu.iastate.pdlreasoner.message;

import edu.iastate.pdlreasoner.master.graph.GlobalNodeID;
import edu.iastate.pdlreasoner.tableau.TracedConcept;

public abstract class ConceptReport implements MessageToSlave, MessageToMaster {

    private static final long serialVersionUID = 1L;

    protected GlobalNodeID m_ImportSource;

    protected GlobalNodeID m_ImportTarget;

    protected TracedConcept m_Concept;

    protected ConceptReport(GlobalNodeID importSource, GlobalNodeID importTarget, TracedConcept concept) {
        m_ImportSource = importSource;
        m_ImportTarget = importTarget;
        m_Concept = concept;
    }

    public GlobalNodeID getImportSource() {
        return m_ImportSource;
    }

    public GlobalNodeID getImportTarget() {
        return m_ImportTarget;
    }

    public TracedConcept getConcept() {
        return m_Concept;
    }
}
