package doors.midifileplayer.event;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * EventList.  This structure represents a static list of Events.
 */
public class EventListPlayerList extends Hashtable {

    /**
	 * Shuttles all contained EventLists to <code>beat</code>, and returns the
	 * 'state-changing' events (Program Change, Volume etc.) which need to be
	 * sent to the MIDI hardware in order for subsequent Events to sound as
	 * intended.
	 *
	 * This function cycles through all internal EventListPlayers and calls
	 * <code>shuttle(toBeat)</code>.
	 *
	 * This function returns an EventList rather than an EventListPlayer,
	 * because we know that the contents of the EventList will be dumped
	 * straight to the MIDI hardware, and we therefore don't need a stateful
	 * EventListPlayer.
	 *
	 * @see EventListPlayer#shuttle(double)
	 * @param toBeat shuttle to this beat, which is relative to global beat 0
	 */
    public EventList shuttle(double toBeat) {
        EventList rel = new EventList(EventList.NULL);
        for (Iterator it = values().iterator(); it.hasNext(); ) {
            EventListPlayer eventListPlayer = (EventListPlayer) it.next();
            EventList eventList = eventListPlayer.shuttle(toBeat);
            rel.addAll(eventList);
        }
        return rel;
    }

    /**
	 * Examines the next Events from all internal EventListPlayers and returns
	 * the EventListPlayer containing the Event with the lowest time value.
	 * This function returns null if there are no more events in any of the
	 * event lists.
	 */
    EventListPlayer getSoonestEventListPlayer() {
        double soonestEventTime = -1;
        EventListPlayer soonestEventListPlayer = null;
        for (Iterator it = values().iterator(); it.hasNext(); ) {
            EventListPlayer eventListPlayer = (EventListPlayer) it.next();
            Event event = eventListPlayer.peek();
            if (!event.isNull()) {
                if ((event.time < soonestEventTime) || (soonestEventTime == -1)) {
                    soonestEventTime = event.time;
                    soonestEventListPlayer = eventListPlayer;
                }
            }
        }
        return soonestEventListPlayer;
    }

    /**
	 * Returns true if any EventListPlayer hasMore() 
	 */
    public boolean hasMore() {
        for (Iterator it = values().iterator(); it.hasNext(); ) {
            EventListPlayer eventListPlayer = (EventListPlayer) it.next();
            if (eventListPlayer.hasMore(true)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Examines the (unpopped) notes from all internal EventListPlayers and
	 * returns the event with the smallest time value.  This function
	 * increments the currentEventIndex of the corresponding EventList.
	 * This function returns the Event.NULL if there are no more events in any
	 * of the event lists.
	 * 
	 * Any modifiers are applied to the event before it is returned, and
	 * therefore may be Event.NULL even if hasMore() returns true. 
	 *
	 * Returned event has time converted to relative to BeatZero.
	 */
    public Event popSoonestEvent() {
        return getSoonestEvent(true);
    }

    /**
	 * Examines the (unpopped) notes from all EventLists in 'elpl' and returns
	 * the event with the smallest time value.  This function returns
	 * Event.NULL if there are no more events in any of the event lists.
	 * 
	 * Any modifiers are applied to the event before it is returned, and
	 * therefore may be Event.NULL even if hasMore() returns true. 
	 *
	 * Returned event has time converted to relative to BeatZero.
	 */
    public Event peekSoonestEvent() {
        return getSoonestEvent(false);
    }

    /**
	 * getSoonestEvent
	 */
    private Event getSoonestEvent(boolean pop) {
        EventListPlayer soonestEventListPlayer = getSoonestEventListPlayer();
        Event soonestEvent = new Event(Event.NULL);
        if (soonestEventListPlayer != null) {
            if (pop) {
                soonestEvent = soonestEventListPlayer.pop();
            } else {
                soonestEvent = soonestEventListPlayer.peek();
            }
            if (soonestEventListPlayer.hasModifier()) {
                soonestEvent = soonestEvent.applyModifier(soonestEventListPlayer.getModifier());
            }
        }
        return soonestEvent;
    }
}
