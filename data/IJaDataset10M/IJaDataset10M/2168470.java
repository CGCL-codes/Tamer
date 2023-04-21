package org.kommando.text.actions;

import org.kommando.core.action.ActionExecutionException;
import org.kommando.core.catalog.CatalogObject;
import org.kommando.text.actions.support.TextAction;

/**
 * Display text in a dialog.
 * 
 * @author Peter De Bruycker
 */
public class DisplayLargeAction extends TextAction {

    private static final String ID = "text:display:large";

    @Override
    public String getDescription() {
        return "Display the text in a large font";
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Display Large";
    }

    @Override
    protected CatalogObject execute(final String text, CatalogObject indirect) throws ActionExecutionException {
        return null;
    }

    @Override
    public boolean isApplicable(CatalogObject direct) {
        return direct.hasExtension(String.class);
    }
}
