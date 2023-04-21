package org.starobjects.wicket.viewer.app.compregistry;

import org.starobjects.wicket.viewer.components.ComponentFactory;
import org.starobjects.wicket.viewer.components.appactions.cooldata.ApplicationActionsJsCoolDataMenuFactory;
import org.starobjects.wicket.viewer.components.appactions.cssmenu.ApplicationActionsCssMenuFactory;
import org.starobjects.wicket.viewer.components.collection.ajaxtable.EntityCollectionAsAjaxTableFactory;
import org.starobjects.wicket.viewer.components.collection.table.EntityCollectionAsTableFactory;

public class ComponentFactoryComparatorDefault implements ComponentFactoryComparator {

    /**
	 * Compares both arguments both ways using {@link #precedes(ComponentFactory, ComponentFactory)},
	 * so no need to implement symmetric checks.
	 */
    public final int compare(ComponentFactory o1, ComponentFactory o2) {
        if (precedes(o1, o2)) {
            return -1;
        }
        if (precedes(o2, o1)) {
            return +1;
        }
        return o1.getClass().getName().compareTo(o2.getClass().getName());
    }

    /**
	 * Overridable to allow extensions or replacements in precedence.
	 */
    protected boolean precedes(ComponentFactory o1, ComponentFactory o2) {
        if (o1 instanceof ApplicationActionsCssMenuFactory && o2 instanceof ApplicationActionsJsCoolDataMenuFactory) {
            return true;
        }
        if (o1 instanceof EntityCollectionAsAjaxTableFactory && o2 instanceof EntityCollectionAsTableFactory) {
            return true;
        }
        return false;
    }
}
