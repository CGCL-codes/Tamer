package net.solarnetwork.central.dras.biz;

import net.solarnetwork.central.dras.domain.EffectiveCollection;
import net.solarnetwork.central.dras.domain.Event;
import net.solarnetwork.central.dras.domain.Member;
import net.solarnetwork.central.dras.support.MembershipCommand;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Event administrator API.
 * 
 * @author matt
 * @version $Revision: 1533 $
 */
@PreAuthorize("hasRole('ROLE_OPERATOR')")
public interface EventAdminBiz {

    /**
	 * Create or update a new Event.
	 * 
	 * @param template the program template
	 * @return the persisted Event instance
	 */
    Event storeEvent(Event template);

    /**
	 * Manage the participants of a program.
	 * 
	 * @param eventId the event ID to assign members to
	 * @param participants the participant membership
	 * @param participantGroups the participant group membership
	 * @return the EffectiveCollection
	 */
    EffectiveCollection<Event, Member> assignMembers(Long eventId, MembershipCommand participants, MembershipCommand participantGroups);
}
