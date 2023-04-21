package org.kommando.proxyobjects;

import org.kommando.core.action.AbstractAction;
import org.kommando.core.action.ActionExecutionException;
import org.kommando.core.catalog.CatalogObject;

/**
 * @author Peter De Bruycker
 */
public class ResolveAction extends AbstractAction {

    @Override
    public CatalogObject execute(CatalogObject direct, CatalogObject indirect) throws ActionExecutionException {
        ProxyCatalogObject proxyCatalogObject = (ProxyCatalogObject) direct;
        return null;
    }

    @Override
    public boolean isApplicable(CatalogObject direct) {
        return direct instanceof ProxyCatalogObject;
    }

    @Override
    public Class<?>[] getDirectExtensionTypes() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Resolves the proxy object to its current value.";
    }

    @Override
    public String getName() {
        return "Resolve";
    }

    @Override
    public String getId() {
        return "proxy:resolve";
    }
}
