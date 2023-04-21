package com.atosorigin.nl.agreement.dispatcher;

import com.atosorigin.nl.agreement.events.Event;

/**
 * @author Jeroen Benckhuijsen
 */
public interface PostingRule {

    /**
	 * @param event
	 */
    void process(Event event);

    /**
	 * @return
	 */
    String getAccount();
}
