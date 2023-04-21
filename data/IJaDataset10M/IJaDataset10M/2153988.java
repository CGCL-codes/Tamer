package org.springframework.webflow.config.scope;

import org.springframework.beans.factory.config.Scope;
import org.springframework.webflow.core.collection.MutableAttributeMap;

/**
 * View {@link Scope scope} implementation.
 * @author Keith Donald
 */
public class ViewScope extends AbstractWebFlowScope {

    protected MutableAttributeMap getScope() {
        return getRequiredRequestContext().getViewScope();
    }
}
