package org.nakedobjects.applib.marker;

import org.nakedobjects.applib.annotation.Immutable;

/**
 * Marker interface to show that an object cannot be changed before it is persisted.
 * 
 * Use {@link Immutable} annotation in preference to this marker interface.
 */
public interface ImmutableUntilPersisted {
}
