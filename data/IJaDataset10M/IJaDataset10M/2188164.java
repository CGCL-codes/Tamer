package org.kommando.core.command;

import org.kommando.core.catalog.Catalog;
import org.kommando.core.catalog.CatalogObject;

/**
 * @author Peter De Bruycker
 * 
 */
public abstract class AbstractCommandContext implements CommandContext {

    private Catalog catalog;

    public AbstractCommandContext(Catalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public void setResult(CatalogObject result) {
        if (result != null) {
            catalog.enhance(result);
        }
        doSetResult(result);
    }

    protected abstract void doSetResult(CatalogObject result);
}
