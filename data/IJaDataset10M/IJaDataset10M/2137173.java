package org.odlabs.wiquery.core.events;

import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;

/**
 * $Id: WiQueryEventBehavior.java 1714 2011-09-22 20:38:30Z hielke.hoeve $
 * <p>
 * Calls a JavaScript statement when the given {@link Event} is triggered.
 * </p>
 * 
 * @author Lionel Armanet
 * @since 0.5
 */
public class WiQueryEventBehavior extends WiQueryAbstractBehavior {

    private static final long serialVersionUID = -5984090566307323188L;

    /**
	 * The trigger event.
	 */
    private final Event event;

    /**
	 * Builds a new instance of {@link WiQueryEventBehavior}.
	 * 
	 * @param event
	 *            the {@link Event} triggering the JavaScript statement.
	 */
    public WiQueryEventBehavior(Event event) {
        super();
        this.event = event;
    }

    @Override
    public JsStatement statement() {
        return new JsQuery(this.getComponent()).$().chain(event);
    }
}
