package br.com.sysmap.crux.widgets.client.event;

import br.com.sysmap.crux.core.client.event.Event;
import br.com.sysmap.crux.core.client.event.bind.EvtBind;
import br.com.sysmap.crux.core.client.event.bind.EvtBinder;
import com.google.gwt.dom.client.Element;

public class OkEvtBind implements EvtBinder<HasOkHandlers> {

    private static final String EVENT_NAME = "onOk";

    /**
	 * @param element
	 * @param widget
	 */
    public void bindEvent(Element element, HasOkHandlers widget) {
        final Event okEvent = EvtBind.getWidgetEvent(element, EVENT_NAME);
        if (okEvent != null) {
            widget.addOkHandler(new OkHandler() {

                public void onOk(OkEvent event) {
                    br.com.sysmap.crux.core.client.event.Events.callEvent(okEvent, event);
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
