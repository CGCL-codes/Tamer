package org.nakedobjects.nof.reflect.java.reflect;

import org.nakedobjects.noa.spec.ExtensionHolder;
import org.nakedobjects.nof.reflect.peer.FieldPeer;

/**
 * Subinterface of {@link ExtensionHolder} implemented by all concrete
 * instances of {@link FieldPeer}, allowing extensions to be
 * added.
 * 
 * <p>
 * This is used by the {@link AbstractOneToManyStrategy}.
 */
public interface ExtensionHolderMutableFieldPeer extends ExtensionHolderMutable, FieldPeer {
}
