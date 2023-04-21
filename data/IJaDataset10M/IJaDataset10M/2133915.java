package org.tagbox.engine;

import org.tagbox.xpath.primitive.Primitive;

/**
 * encapsulates a user session persistent over several requests
 */
public interface Session {

    public Primitive getAttribute(String name);

    public void setAttribute(String name, Primitive value);

    public void removeAttribute(String name);
}
