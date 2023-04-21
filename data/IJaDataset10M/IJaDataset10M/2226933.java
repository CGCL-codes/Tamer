package br.com.sysmap.crux.widgets.client.event.collapseexpand;

import br.com.sysmap.crux.core.client.event.Event;
import br.com.sysmap.crux.core.client.event.Events;
import br.com.sysmap.crux.core.client.event.bind.EvtBind;
import br.com.sysmap.crux.core.client.event.bind.EvtBinder;
import com.google.gwt.dom.client.Element;

public class BeforeCollapseEvtBind implements EvtBinder<HasBeforeCollapseHandlers> {

    private static final String EVENT_NAME = "onBeforeCollapse";

    /**
	 * @param element
	 * @param widget
	 */
    public void bindEvent(Element element, HasBeforeCollapseHandlers widget) {
        final Event beforeCollapseEvent = EvtBind.getWidgetEvent(element, EVENT_NAME);
        if (beforeCollapseEvent != null) {
            widget.addBeforeCollapseHandler(new BeforeCollapseHandler() {

                public void onBeforeCollapse(BeforeCollapseEvent event) {
                    Events.callEvent(beforeCollapseEvent, event);
                }
            });
        }
    }

    /**
	 * @see br.com.sysmap.crux.core.client.event.bind.EvtBinder#getEventName()
	 */
    public String getEventName() {
        return EVENT_NAME;
    }
}
