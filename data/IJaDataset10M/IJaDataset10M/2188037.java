package org.hip.vif.bom.impl;

import org.hip.kernel.bom.impl.DomainObjectImpl;

/**
 * Model for entries connecting ratings to questions.
 *
 * @author Luthiger
 * Created: 29.08.2009
 */
public class RatingsQuestion extends DomainObjectImpl {

    public static final String HOME_CLASS_NAME = "org.hip.vif.bom.impl.RatingsQuestionHome";

    /**
	 * This Method returns the class name of the home.
	 *
	 * @return java.lang.String
	 */
    public String getHomeClassName() {
        return HOME_CLASS_NAME;
    }
}
