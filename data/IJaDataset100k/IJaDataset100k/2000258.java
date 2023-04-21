package org.liris.schemerger.core.pattern;

/**
 * Any object that can match an event implements this interface, mostly event
 * declarations.
 * 
 * @author Damien Cram
 * 
 * @param <E>
 *            the type of event this matcher object matches.
 */
public interface Matcher<E> {

    /**
	 * @param event
	 *            the event to test the match to.
	 * @return true if this matches event, false otherwise.
	 */
    public abstract boolean matchEvent(E event);
}
