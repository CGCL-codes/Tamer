package org.dllearner.utilities.owl;

import java.util.LinkedList;
import org.dllearner.core.owl.Description;
import org.dllearner.core.owl.ObjectProperty;
import org.dllearner.core.owl.ObjectSomeRestriction;
import org.dllearner.core.owl.Thing;

/**
 * A property context is a utility class which specifies the
 * position of constructs with respect to properties of a 
 * construct in a class description. For instance, the A
 * in \exists r.\exists s.A occurs in property context [r,s].
 * 
 * @author Jens Lehmann
 *
 */
public class PropertyContext extends LinkedList<ObjectProperty> implements Comparable<PropertyContext> {

    private static final long serialVersionUID = -4403308689522524077L;

    @Override
    public int compareTo(PropertyContext context) {
        int diff = context.size() - size();
        if (diff != 0) {
            return diff;
        }
        for (int i = 0; i < size(); i++) {
            int cmp = get(i).getName().compareTo(context.get(i).getName());
            if (cmp != 0) {
                return cmp;
            }
        }
        return 0;
    }

    /**
	 * Transforms context [r,s] to \exists r.\exists s.\top.
	 * @return A description with existential quantifiers and \top corresponding
	 * to the context.
	 */
    public Description toExistentialContext() {
        Description d = Thing.instance;
        for (int i = size() - 1; i >= 0; i--) {
            d = new ObjectSomeRestriction(get(i), d);
        }
        return d;
    }
}
