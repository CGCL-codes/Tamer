package org.hip.vif.bom.impl;

import org.hip.kernel.bom.impl.DomainObjectImpl;

/**
 * This class implements the join between question-author/reviewer and questions.
 * 
 * @author: Benno Luthiger
 */
public class JoinAuthorReviewerToQuestion extends DomainObjectImpl {

    public static final String HOME_CLASS_NAME = "org.hip.vif.bom.impl.JoinAuthorReviewerToQuestionHome";

    /**
	 * Constructor for JoinQuestionToAuthorReviewer.
	 */
    public JoinAuthorReviewerToQuestion() {
        super();
    }

    /**
	 * @see org.hip.kernel.bom.GeneralDomainObject#getHomeClassName()
	 */
    public String getHomeClassName() {
        return HOME_CLASS_NAME;
    }
}
