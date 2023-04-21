package org.jabber.jabberbeans;

import org.jabber.jabberbeans.Extension.Roster;

/**
 * RosterListener is an interface you can implement in a client to get any 
 * roster changes as notifications to your code.
 * <p>
 *
 * @see RosterAdapter
 *
 * @author  David Waite <a href="mailto:dwaite@jabber.com">
 *                      <i>&lt;dwaite@jabber.com&gt;</i></a>
 * @author  $Author: mass $
 * @version $Revision: 1.10 $
 */
public interface RosterListener {

    /**
     * The <code>changedRoster</code> event is fired when there has been a
     * change to the roster state. The passed-in Roster is a 'list' of
     * changes. The cached value in the RosterBean is the new, full list.
     *
     * @param r a <code>Roster Extension</code> describing the changes
     */
    void changedRoster(Roster r);

    /**
     * The <code>replacedRoster</code> event is fired when a completely new
     * roster has replaced the existing one. The passed-in Roster is a
     * full new list of Roster entries. The cached value in the RosterBean
     * is equivalent to this value.
     *
     * @param r a <code>Roster Extension</code> containing the new roster
     */
    void replacedRoster(Roster r);
}
