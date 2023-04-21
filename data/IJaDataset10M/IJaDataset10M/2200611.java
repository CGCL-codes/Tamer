package org.zkoss.jquery4j.jqueryui.slider.events;

import java.util.Map;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.au.AuRequests;
import org.zkoss.zk.mesg.MZk;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;

public class ChangedEvent extends Event {

    private final int _value;

    public static final ChangedEvent getChangedEvent(AuRequest request) {
        final Component comp = request.getComponent();
        if (comp == null) throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, request);
        final Map data = request.getData();
        if (data == null) throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA, new Object[] { data, request });
        return new ChangedEvent(request.getCommand(), comp, AuRequests.getInt(data, "value", 0));
    }

    public ChangedEvent(String name, Component target, int value) {
        super(name, target);
        this._value = value;
    }

    public final int getValue() {
        return this._value;
    }
}
