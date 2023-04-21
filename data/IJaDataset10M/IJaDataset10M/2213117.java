package org.kommando.alias.actions;

import org.kommando.alias.objectsource.AliasObjectSource;
import org.kommando.core.action.AbstractAction;
import org.kommando.core.action.ActionExecutionException;
import org.kommando.core.catalog.CatalogObject;

/**
 * @author Peter De Bruycker
 */
public class RemoveAliasAction extends AbstractAction {

    private static final String ID = "alias:remove";

    private AliasObjectSource aliasObjectSource;

    public RemoveAliasAction(AliasObjectSource aliasObjectSource) {
        this.aliasObjectSource = aliasObjectSource;
    }

    @Override
    public CatalogObject execute(CatalogObject direct, CatalogObject indirect) throws ActionExecutionException {
        aliasObjectSource.removeAlias(direct);
        return null;
    }

    @Override
    public Class<?>[] getDirectExtensionTypes() {
        return null;
    }

    @Override
    public boolean isApplicable(CatalogObject direct) {
        return aliasObjectSource.hasAlias(direct);
    }

    @Override
    public String getDescription() {
        return "Remove the alias";
    }

    @Override
    public String getName() {
        return "Remove Alias";
    }

    @Override
    public String getId() {
        return ID;
    }
}
