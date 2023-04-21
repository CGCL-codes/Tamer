package org.opennms.netmgt.ticketd;

import org.springframework.transaction.annotation.Transactional;

/**
 * OpenNMS Trouble Ticket API
 * 
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:david@opennms.org">David Hustace</a>
 *
 */
@Transactional
public interface TicketerServiceLayer {

    /**
	 * Implement to manage creation of tickets through registered plugin.
	 * @param alarmId
	 */
    public void createTicketForAlarm(int alarmId);

    /**
	 * Implement to manage updating of tickets through registered plugin.
	 * @param alarmId
	 * @param ticketId
	 */
    public void updateTicketForAlarm(int alarmId, String ticketId);

    /**
	 * Implement to manage closing of tickets through registered plugin.
	 * @param alarmId
	 * @param ticketId
	 */
    public void closeTicketForAlarm(int alarmId, String ticketId);

    /**
	 * Implement to manage canceling of tickets through registered plugin.
	 * @param alarmId
	 * @param ticketId
	 */
    public void cancelTicketForAlarm(int alarmId, String ticketId);
}
