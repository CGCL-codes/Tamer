package org.softmed.action.resolver;

import org.softmed.action.ActionStep;
import org.softmed.action.description.ActionStepDescription;

public interface ActionStepResolver {

    public ActionStep instantiate(ActionStepDescription description) throws Throwable;
}
