package org.argouml.uml.diagram.ui;

import org.argouml.model.Model;

/**
 * A Mode to interpret user input while creating a generalization edge.
 * The generalization can connect any model element including those
 * represented by edges as well as nodes. The source and destination
 * must be of the same type.
 * TODO: It should be possible to delete this class when issue 4632 is fixed.
 */
public final class ModeCreateGeneralization extends ModeCreateGraphEdge {

    /**
     * Return the meta type of the element that this mode is designed to
     * create. In the case the dependency metatype.
     * @return the dependency meta type.
     */
    protected final Object getMetaType() {
        return Model.getMetaTypes().getGeneralization();
    }
}
