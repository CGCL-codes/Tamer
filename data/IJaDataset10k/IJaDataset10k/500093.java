package net.solarnetwork.central.dras.dao;

import java.util.Set;
import net.solarnetwork.central.dao.FilterableDao;
import net.solarnetwork.central.dao.GenericDao;
import net.solarnetwork.central.dras.domain.Event;
import net.solarnetwork.central.dras.domain.EventExecutionTargets;
import net.solarnetwork.central.dras.domain.EventTargets;
import net.solarnetwork.central.dras.domain.Match;
import net.solarnetwork.central.dras.domain.Member;
import org.joda.time.DateTime;

/**
 * DAO API for Event entities.
 * 
 * @author matt
 * @version $Revision: 1691 $
 */
public interface EventDao extends GenericDao<Event, Long>, FilterableDao<Match, Long, EventFilter> {

    /**
	 * Resolve all user members for an event, including those users indirectly
	 * assigned via user groups, participants, etc.
	 * 
	 * @param eventId the event ID
	 * @param effectiveDate the effective date, or <em>null</em> for
	 * the current date
	 * @return set of User members, never <em>null</em>
	 */
    Set<Member> resolveUserMembers(Long eventId, DateTime effectiveDate);

    /**
	 * Get the set of event User members.
	 * 
	 * <p>This returns only the users directly assigned to this event.
	 * See {@link #resolveUserMembers(Long, DateTime)} for access to 
	 * every resolvable user.</p>
	 * 
	 * @param eventId the event ID to get the members of
	 * @param effectiveDate the effective date, or <em>null</em> for
	 * the current date
	 * @return set of User members, never <em>null</em>
	 */
    Set<Member> getUserMembers(Long eventId, DateTime effectiveDate);

    /**
	 * Assign all User members of a event.
	 * 
	 * @param eventId the event ID to assign members to
	 * @param userIdSet the set of user IDs that define the event membership
	 * @param effectiveId the effective ID
	 */
    void assignUserMembers(Long eventId, Set<Long> userIdSet, Long effectiveId);

    /**
	 * Get the set of event UserGroup members.
	 * 
	 * @param eventId the event ID to get the members of
	 * @param effectiveDate the effective date, or <em>null</em> for
	 * the current date
	 * @return set of UserGroup members, never <em>null</em>
	 */
    Set<Member> getUserGroupMembers(Long eventId, DateTime effectiveDate);

    /**
	 * Assign all UserGroup members of an event.
	 * 
	 * @param programId the event ID to assign members to
	 * @param userGroupIdSet the set of user group IDs that define the event membership
	 * @param effectiveId the effective ID
	 */
    void assignUserGroupMembers(Long eventId, Set<Long> userGroupIdSet, Long effectiveId);

    /**
	 * Get the set of event Participant members.
	 * 
	 * @param eventId the event ID to get the participants of
	 * @param effectiveDate the effective date, or <em>null</em> for
	 * the current date
	 * @return set of Participant members, never <em>null</em>
	 */
    Set<Member> getParticipantMembers(Long eventId, DateTime effectiveDate);

    /**
	 * Assign all Participant members of a event.
	 * 
	 * @param eventId the event ID to assign members to
	 * @param participantIdSet the set of participant IDs that define the event participants
	 * @param effectiveId the effective ID
	 */
    void assignParticipantMembers(Long eventId, Set<Long> participantIdSet, Long effectiveId);

    /**
	 * Get the set of event Participant members.
	 * 
	 * @param eventId the event ID to get the participants of
	 * @param effectiveDate the effective date, or <em>null</em> for
	 * the current date
	 * @return set of ParticipantGroup members, never <em>null</em>
	 */
    Set<Member> getParticipantGroupMembers(Long eventId, DateTime effectiveDate);

    /**
	 * Assign all Participant members of a event.
	 * 
	 * @param eventId the event ID to assign members to
	 * @param participantGroupIdSet the set of participant group IDs that define the event participants
	 * @param effectiveId the effective ID
	 */
    void assignParticipantGroupMembers(Long eventId, Set<Long> participantGroupIdSet, Long effectiveId);

    /**
	 * Get the set of event Location members.
	 * 
	 * @param eventId the event ID to get the locations of
	 * @param effectiveDate the effective date, or <em>null</em> for
	 * the current date
	 * @return set of Location members, never <em>null</em>
	 */
    Set<Member> getLocationMembers(Long eventId, DateTime effectiveDate);

    /**
	 * Assign all Location members of an event.
	 * 
	 * @param eventId the event ID to assign members to
	 * @param locationIdSet the set of location IDs that define the event locations
	 * @param effectiveId the effective ID
	 */
    void assignLocationMembers(Long eventId, Set<Long> locationIdSet, Long effectiveId);

    /**
	 * Get the set of event Participant members.
	 * 
	 * @param eventId the event ID to get the participants of
	 * @param effectiveDate the effective date, or <em>null</em> for
	 * the current date
	 * @return set of EventTargets members, never <em>null</em>
	 */
    Set<EventTargets> getEventTargets(Long eventId, DateTime effectiveDate);

    /**
	 * Assign all EventTargets of a event.
	 * 
	 * @param eventId the event ID to assign members to
	 * @param targetsIdSet the set of EventTargets IDs that define the event targets
	 * @param effectiveId the effective ID
	 */
    void assignEventTargets(Long eventId, Set<Long> targetsIdSet, Long effectiveId);

    /**
	 * Get the event targets, as a {@link EventExecutionTargets} instances.
	 * 
	 * @param eventId the event ID
	 * @param effectiveDate the effective date, or <em>null</em> for
	 * the current date
	 * @return the EventExecutionTargets, or <em>null</em> if none available
	 */
    Set<EventExecutionTargets> getEventExecutionTargets(Long eventId, DateTime effectiveDate);
}
