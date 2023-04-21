package org.qtitools.qti.group.result;

import org.qtitools.qti.group.AbstractNodeGroup;
import org.qtitools.qti.node.result.CandidateResponse;
import org.qtitools.qti.node.result.ResponseVariable;

/**
 * Group of candidateResponse child.
 * 
 * @author Jiri Kajaba
 */
public class CandidateResponseGroup extends AbstractNodeGroup {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructs group.
	 *
	 * @param parent parent of created group
	 */
    public CandidateResponseGroup(ResponseVariable parent) {
        super(parent, CandidateResponse.CLASS_TAG, true);
    }

    /**
	 * Gets child.
	 *
	 * @return child
	 * @see #setCandidateResponse
	 */
    public CandidateResponse getCandidateResponse() {
        return (CandidateResponse) getChild();
    }

    /**
	 * Sets new child.
	 *
	 * @param candidateResponse new child
	 * @see #getCandidateResponse
	 */
    public void setCandidateResponse(CandidateResponse candidateResponse) {
        setChild(candidateResponse);
    }

    /**
	 * Creates child with given QTI class name.
	 * <p>
	 * Parameter classTag is needed only if group can contain children with different QTI class names.
	 *
	 * @param classTag QTI class name (this parameter is ignored)
	 * @return created child
	 */
    public CandidateResponse create(String classTag) {
        return new CandidateResponse((ResponseVariable) getParent());
    }
}
