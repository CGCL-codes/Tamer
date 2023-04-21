package net.sf.bbarena.ds;

import java.util.List;
import net.sf.bbarena.model.Arena;
import net.sf.bbarena.model.MatchInfo;
import net.sf.bbarena.model.event.Event;

/**
 * This interface allows to prepare a Match on the persistence and to store/load
 * all of his events
 *
 * @author f.bellentani
 */
public interface MatchDS {

    /**
	 * Prepares a Match on the persistence
	 *
	 * @param ruleSet
	 *            RuleSet name to be used
	 * @param pitchType
	 *            Name of the pitch type
	 * @param teamsId
	 *            ID List of the Teams that will play
	 * @return The Arena
	 */
    public Arena createMatch(String ruleSet, String pitchType, List<Long> teamsId);

    /**
	 * Loads the initial state of a Match
	 *
	 * @param matchId
	 *            ID of the match to load
	 * @return The Arena
	 */
    public Arena getMatch(Long matchId);

    /**
	 * Loads the list of the active Matches (all Matches with status different
	 * from FINISHED)
	 *
	 * @return Active Matches
	 */
    public List<MatchInfo> getActiveMatches();

    /**
	 * Loads all the Matches played by a Team
	 *
	 * @param teamId
	 *            Team ID
	 * @return Matches
	 */
    public List<MatchInfo> getMatchesByTeam(Long teamId);

    /**
	 * Load all the Matches played by a Coach
	 *
	 * @param coachId
	 *            Coach ID
	 * @return Matches
	 */
    public List<MatchInfo> getMatchesByCoach(Long coachId);

    /**
	 * Adds an Event to a Match
	 *
	 * @param matchId
	 *            Match ID
	 * @param event
	 *            Event to add
	 * @return Event ID for the Match
	 */
    public Long addEvent(Long matchId, Event event);

    /**
	 * Loads a Match Event
	 *
	 * @param matchId
	 *            Match ID
	 * @param eventId
	 *            Event ID in the Match
	 * @return The Event
	 */
    public Event getEvent(Long matchId, Long eventId);

    /**
	 * Returns the number of Events for a Match
	 *
	 * @param matchId
	 *            Match ID
	 * @return Events size
	 */
    public Long getEventSize(Long matchId);

    /**
	 * Loads the full list of Events for a Match
	 *
	 * @param matchId
	 *            Match ID
	 * @return The Match Events
	 */
    public List<Event> getEvents(Long matchId);

    /**
	 * Loads a subset of the Events for a Match. If the toEvent is null or is
	 * greater than the Events size the last Event ID will be used
	 *
	 * @param matchId
	 *            Match ID
	 * @param fromEvent
	 *            First Event of the subset
	 * @param toEvent
	 *            Last Event of the subset
	 * @return A Match Events subset
	 */
    public List<Event> getEvents(Long matchId, Long fromEvent, Long toEvent);
}
