package org.infoset.xml;

/**
 * This interface represents an end of a element.  It is only constructed
 * when processing infosets as a sequence of events.
 * @author  alex
 */
public interface ElementEnd extends Named, Item, Location, Validity {

    NamespaceScope getNamespaceScope();
}
