package org.genxdm.typed.io;

import org.genxdm.io.DocumentHandler;
import org.genxdm.typed.Validator;

/** A handler for creating typed documents.
 * 
 * This subinterface changes the semantics of the parse methods,
 * changing almost nothing else.  A TypedDocumentHandler is created with a
 * supplied Validator, which is then made available from the
 * TypedDocumentHandler (use it to check errors, for instance).
 * 
 * @param <N> The node abstraction
 * @param <A> The atom abstraction
 */
public interface TypedDocumentHandler<N, A> extends DocumentHandler<N> {

    Validator<A> getValidator();
}
