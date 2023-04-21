package org.snmp4j.event;

import java.util.EventObject;
import org.snmp4j.security.UsmUserEntry;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUserTable;

/**
 * This Event is issued whenever a user of the {@link USM} is created
 * modified or deleted.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class UsmUserEvent extends EventObject {

    private static final long serialVersionUID = -2650579887988635391L;

    /**
   * Constant: a new user was created.
   */
    public static final int USER_ADDED = 1;

    /**
   * Constant: a user was deleted.
   */
    public static final int USER_REMOVED = 2;

    /**
   * Constant: a user was changed (but not deleted).
   */
    public static final int USER_CHANGED = 3;

    private org.snmp4j.security.UsmUserEntry user;

    private int type;

    /**
   * Construct a UsmUserEvent.
   *
   * @param source
   *    the object that emitts this event
   * @param changedEntry
   *    the changed entry
   * @param type
   *    can be USER_ADDED, USER_REMOVED or USER_CHANGED.
   */
    public UsmUserEvent(Object source, UsmUserEntry changedEntry, int type) {
        super(source);
        this.user = changedEntry;
        this.type = type;
    }

    /**
   * Get the modified entry of the {@link UsmUserTable}.
   *
   * @return the entry <ul>
   *     <li> after the modification if the user was added or modified
   *     <li> before the modification if the user was deleted </ul>
   */
    public org.snmp4j.security.UsmUserEntry getUser() {
        return user;
    }

    /**
   * Return the type of operation that triggered this event.
   *
   * @return One of USER_ADDED, USER_REMOVED or USER_CHANGED.
   */
    public int getType() {
        return type;
    }
}
