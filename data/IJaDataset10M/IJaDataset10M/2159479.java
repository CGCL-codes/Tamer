package org.jsemantic.support.annotations.interceptors;

import org.jsemantic.core.session.factory.SemanticSessionFactory;
import org.jservicerules.support.annotations.SemanticFactory;
import org.jservicerules.support.annotations.SemanticService;
import org.jservicerules.support.annotations.SessionVariables;
import org.jservicerules.support.annotations.Variable;

/**
 * The Class MockServiceFive.
 */
@SemanticService(rulesFile = "org/jsemantic/servicerules/support/par-impar.drl")
@SessionVariables(variables = { @Variable(key = "1", value = "10") })
public class MockServiceFive {

    /** The s s factory. */
    @SemanticFactory(rulesFile = "org/jsemantic/servicerules/support/par-impar.drl")
    @SessionVariables(variables = { @Variable(key = "1", value = "10") })
    private SemanticSessionFactory sSFactory = null;

    /**
	 * Gets the semantic session factory.
	 * 
	 * @return the semantic session factory
	 */
    public SemanticSessionFactory getSemanticSessionFactory() {
        return sSFactory;
    }

    /**
	 * Sets the semantic session factory.
	 * 
	 * @param sSFactory the new semantic session factory
	 */
    public void setSemanticSessionFactory(SemanticSessionFactory sSFactory) {
        this.sSFactory = sSFactory;
    }
}
