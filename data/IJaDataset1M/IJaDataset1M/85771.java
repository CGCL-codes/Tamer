package org.rakiura.cpn.event;

import java.util.EventListener;

/**
 * Represents a transition listener.
 * 
 *<br><br>
 * TransitionListener.java<br>
 * Created: Thu Apr 11 17:20:39 2002<br>
 *
 *@author  <a href="mariusz@rakiura.org">Mariusz Nowostawski</a>
 *@version @version@ $Revision: 1.3 $
 *@since 2.0
 */
public interface TransitionListener extends EventListener {

    void notify(final TransitionEvent anEvent);

    void notify(final TransitionStartedEvent anEvent);

    void notify(final TransitionFinishedEvent anEvent);

    void notify(final TransitionStateChangedEvent anEvent);
}
