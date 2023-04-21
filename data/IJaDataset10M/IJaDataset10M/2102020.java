package org.objetdirect.wickext.core.events;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.objetdirect.wickext.core.commons.CoreJavaScriptHeaderContributor;
import org.objetdirect.wickext.core.javascript.JsQuery;

/**
 * $Id: WickextEventBehavior.java 71 2008-11-17 23:11:34Z lionel.armanet $
 * <p>
 * Calls a JavaScript statement when the given {@link Event} is triggered.
 * </p>
 * 
 * @author Lionel Armanet
 * @since 0.5
 */
public class WickextEventBehavior extends AbstractBehavior {

    private static final long serialVersionUID = -5984090566307323188L;

    /**
	 * The trigger event.
	 */
    private Event event;

    /**
	 * The event is attached to this component.
	 */
    private Component component;

    /**
	 * Builds a new instance of {@link WickextEventBehavior}.
	 * 
	 * @param event
	 *            the {@link Event} triggering the JavaScript statement.
	 */
    public WickextEventBehavior(Event event) {
        super();
        this.event = event;
    }

    @Override
    public void bind(Component component) {
        super.bind(component);
        this.component = component;
        component.getPage().add(new HeaderContributor(new CoreJavaScriptHeaderContributor()));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        JsQuery query = new JsQuery(component);
        query.$().chain(event);
        query.renderHead(response);
    }
}
