package org.nakedobjects.plugins.dnd.view;

import org.nakedobjects.metamodel.adapter.NakedObject;

public interface Workspace extends View {

    View addIconFor(NakedObject nakedObject, Placement placement);

    View addWindowFor(NakedObject object, Placement placement);

    void addWindow(View window, Placement placement);

    void addDialog(View dialog, Placement placement);

    /**
     * Lower the specified view so it is below all the other views.
     */
    void lower(View view);

    /**
     * Raise the specified view so it is above all the other views.
     */
    void raise(View view);
}
