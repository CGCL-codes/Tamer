package net.sf.jml.event;

import net.sf.jml.MsnContact;
import net.sf.jml.MsnSwitchboard;

/**
 * Msn switchboard listener, used for chat. 
 * 
 * @author Roger Chen
 */
public interface MsnSwitchboardListener {

    /**
     * Switchboard started. Maybe start by user or join a
     * switchboard.
     * 
     * @param switchboard
     * 		MsnSwitchboard
     */
    public void switchboardStarted(MsnSwitchboard switchboard);

    /**
     * Switchboard closed.
     * 
     * @param switchboard
     *    	MsnSwitchboard
     */
    public void switchboardClosed(MsnSwitchboard switchboard);

    /**
     * A contact join switchboard.
     * 
     * @param switchboard
     * 		the MsnSwitchboard which contact joins.
     * @param contact
     *		the join contact
     */
    public void contactJoinSwitchboard(MsnSwitchboard switchboard, MsnContact contact);

    /**
     * A contact leave switchboard.
     * 
     * @param switchboard
     *   	the MsnSwitchboard which friend leave.
     * @param contact
     *      the leave contact
     */
    public void contactLeaveSwitchboard(MsnSwitchboard switchboard, MsnContact contact);
}
