package org.nakedobjects.plugins.dnd.list;

import org.nakedobjects.plugins.dnd.icon.IconElementFactory;
import org.nakedobjects.plugins.dnd.view.ViewFactory;
import org.nakedobjects.plugins.dnd.view.composite.AbstractCollectionViewSpecification;

public class SimpleListSpecification extends AbstractCollectionViewSpecification {

    protected ViewFactory createElementFactory() {
        return new IconElementFactory();
    }

    public String getName() {
        return "List";
    }
}
