package net.community.chest.awt.event;

import java.util.EventListener;
import java.util.EventObject;

/**
 * <P>Copyright GPLv2</P>
 *
 * <P>Implemented by {@link Enum}-s that represent the possible API calls
 * for some kind of listener</P>
 * 
 * @param <L> Type of {@link EventListener}
 * @param <E> Type of {@link EventObject} expected as input to the listener's methods
 * @author Lyor G.
 * @since May 4, 2009 12:14:24 PM
 */
public interface ListenerEventEnum<L extends EventListener, E extends EventObject> {

    /**
	 * @return The <code>int</code> value of the event expected for handling by
	 * the listener method
	 */
    int getEventId();

    /**
	 * @return Type of {@link EventListener} being represented
	 */
    Class<L> getListenerClass();

    /**
	 * @return Type of {@link EventObject} expected as input to the listener's methods
	 */
    Class<E> getEventClass();

    /**
	 * Invokes the method being represented by the {@link Enum} value
	 * @param l The {@link EventListener} instance to invoke
	 * @param e The  {@link EventObject} to be provided as input
	 */
    void invoke(L l, E e);
}
