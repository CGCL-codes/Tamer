package de.mguennewig.pobjects.metadata;

import org.w3c.dom.*;

/** Common base type for HTML inline and block elements.
 *
 * @author Michael G�nnewig
 * @see    Description
 */
public abstract class FlowDesc extends Object {

    private final String name;

    /** Creates a new FlowDesc. */
    public FlowDesc(final String name) {
        super();
        this.name = name;
    }

    /** Returns the name of this element. */
    public final String getName() {
        return name;
    }

    public abstract Node toXml(Document doc);
}
