package org.fest.swing.exception;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Understands an error thrown when looking up a component using a <code>{@link org.fest.swing.core.ComponentFinder}</code>.
 *
 * @author Alex Ruiz
 */
public class ComponentLookupException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final Collection<Component> found = new ArrayList<Component>();

    /**
   * Creates a new </code>{@link ComponentLookupException}</code>.
   * @param message the detail message.
   * @param found the <code>Component</code>s found by the lookup (if any.)
   */
    public ComponentLookupException(String message, Collection<? extends Component> found) {
        this(message);
        this.found.addAll(found);
    }

    /**
   * Creates a new <code>{@link ComponentLookupException}</code>.
   * @param message the detail message.
   */
    public ComponentLookupException(String message) {
        super(message);
    }

    /**
   * Returns the <code>Component</code>s found by the lookup (if any.)
   * @return the <code>Component</code>s found by the lookup (if any.)
   */
    public final Collection<? extends Component> found() {
        return Collections.<Component>unmodifiableCollection(found);
    }
}
