package org.nakedobjects.runtime.help;

import org.nakedobjects.applib.Identifier;

public class HelpManagerAssist extends HelpManagerAbstract {

    private HelpManager underlyingHelpManager;

    private boolean showIdentifier = false;

    public void setShowIdentifier(final boolean showIdentifier) {
        this.showIdentifier = showIdentifier;
    }

    public void setDecorated(final HelpManager decorated) {
        this.underlyingHelpManager = decorated;
    }

    @Override
    public String help(final Identifier identifier) {
        String help = "";
        if (underlyingHelpManager != null) {
            help = underlyingHelpManager.help(identifier);
        }
        return showIdentifier ? (identifier.toString() + "\n") : "" + help;
    }
}
