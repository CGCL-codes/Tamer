package org.vramework.vow;

import java.lang.reflect.Field;

/**
 * Defines events for changes of {@link IIDentity}.
 * 
 * @author thomas.mahringer
 */
public interface IIdentityListener {

    /**
   * Informs the listener that the identity of the passed object has changed. If
   * an identity consists of several fields ("compound identity"), this method
   * will be called for each field.
   * 
   * @param objectWithIdentity -
   *          The changed object.
   * @param field -
   *          The changed field.
   * @param newValue -
   *          The new value.
   */
    void beforeIdentityChange(IIDentity objectWithIdentity, Field field, Object newValue);

    /**
   * Informs the listener that the object has been reloaded.
   * 
   * @param reloadedValues -
   *          The reloaded values.
   */
    void objectReloaded(Object reloadedValues);
}
