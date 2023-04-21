package org.arastreju.api.ontology.model;

import org.arastreju.api.context.ScenarioContext;
import org.arastreju.api.context.SecurityContext;
import org.arastreju.api.ontology.model.values.TimeSpec;

/**
 * The context of an association describes it's validity, consisting of:
 * <ul>
 * 	<li>security context (owner, visibility)</li>
 *  <li>time context (valid from - valid until)</li>
 *  <li>scenario context</li>
 * </ul>
 * 
 * Created: 14.07.2009
 *
 * @author Oliver Tigges
 */
public class AssociationContext {

    private final SecurityContext securityContext;

    private final ScenarioContext scenarioContext;

    private final TimeSpec timeSpec;

    /**
	 * Constructor.
	 * @param securityContext
	 * @param scenarioContext
	 * @param timeSpec
	 */
    public AssociationContext(SecurityContext securityContext, ScenarioContext scenarioContext, TimeSpec timeSpec) {
        this.securityContext = securityContext;
        this.scenarioContext = scenarioContext;
        this.timeSpec = timeSpec;
    }

    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    public ScenarioContext getScenarioContext() {
        return scenarioContext;
    }

    public TimeSpec getTimeSpec() {
        return timeSpec;
    }
}
