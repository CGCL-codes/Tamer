package org.nakedobjects.object.help;

import org.nakedobjects.object.reflect.AbstractOneToManyPeer;
import org.nakedobjects.object.reflect.OneToManyPeer;

public class OneToManyHelp extends AbstractOneToManyPeer {

    private final HelpManager helpManager;

    public OneToManyHelp(OneToManyPeer local, HelpManager helpManager) {
        super(local);
        this.helpManager = helpManager;
    }

    public String getDescription() {
        return helpManager == null ? "" : helpManager.help(getIdentifier());
    }
}
