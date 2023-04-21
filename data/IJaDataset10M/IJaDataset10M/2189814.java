package net.sf.dynxform.container;

import net.sf.dynxform.exception.business.SessionContextException;
import java.util.Enumeration;

/**
 *  Interface for a SessionContext.
 *  This interface describes a SessionContext. The SessionContext is a data
 *  container containing simple data which can be retrieved/set by the
 *  session context manager.
 *
 * net.sf.dynxform.form.container Mar 1, 2004 12:06:39 PM andreyp
 * Copyright (c) dynxform.sf.net. All Rights Reserved
 *
 * @author <a href="mailto:andreyp@sf.net">andreyp</a>
 */
public interface SessionContext {

    /**
   * Returns a string containing the unique identifier assigned
   * to this session context. The identifier is assigned
   * by the context container and is implementation dependent.
   * @return a string specifying the identifier
   *         assigned to this session
   */
    String getId();

    /**
   * Returns the object bound with the specified name in this session, or
   * <code>null</code> if no object is bound under the name.
   * @param name                a string specifying the name of the object
   * @param mandatory
   * @return                        the object with the specified name
   */
    Object getAttribute(String name, boolean mandatory) throws SessionContextException;

    /**
   * Returns an <code>Enumeration</code> of <code>String</code> objects
   * containing the names of all the objects bound to this session.
   * @return                        an <code>Enumeration</code> of
   *                                <code>String</code> objects specifying the
   *                                names of all the objects bound to
   *                                this session
   */
    Enumeration getAttributeNames() throws SessionContextException;

    /**
   * Binds an object to this session, using the name specified.
   * If an object of the same name is already bound to the session,
   * the object is replaced.
   * @param name                        the name to which the object is bound;
   *                                        cannot be null
   * @param value                        the object to be bound; cannot be null
   */
    void setAttribute(String name, Object value) throws SessionContextException;

    /**
   * Removes the object bound with the specified name from
   * this session. If the session does not have an object
   * bound with the specified name, this method does nothing.
   * @param name                                the name of the object to
   *                                                remove from this session
   */
    void removeAttribute(String name) throws SessionContextException;
}
