package org.dllearner.core.owl;

/**
 * Marker interface for those OWL elements, which have a name, e.g.
 * the name of an individual, an object property, a data type property
 * or a named class.
 * 
 * @author Jens Lehmann
 *
 */
public interface NamedKBElement extends KBElement {

    public String getName();
}
