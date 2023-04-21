package org.authorsite.vocab.core;

import org.authorsite.vocab.exceptions.*;

/**
 * 
 * @author jejking
 * @version $Revision: 1.3 $
 */
public class StubAbstractSemanticRel extends AbstractSemanticRel {

    public StubAbstractSemanticRel(VocabNode fromNode, String rel, Integer toNodeId) throws VocabException {
        super(fromNode, rel, toNodeId);
    }

    public StubAbstractSemanticRel(VocabNode fromNode, String rel, VocabNode toNode) throws VocabException {
        super(fromNode, rel, toNode);
    }

    /**
	 * @see org.authorsite.vocab.core.SemanticRel#getToNode()
	 */
    public VocabNode getToNode() {
        return null;
    }
}
