package org.nakedobjects.viewer.skylark;

import org.nakedobjects.object.control.Consent;

public interface UserAction {

    /**
     * Indicate that this action is disabled
     */
    Consent disabled(View view);

    /**
     * Invoke this action.
     */
    void execute(Workspace workspace, View view, Location at);

    /**
     * Returns the description of the action.
     */
    String getDescription(View view);

    /**
     * Returns the name of the action as the user will refer to it.
     */
    String getName(View view);
}
