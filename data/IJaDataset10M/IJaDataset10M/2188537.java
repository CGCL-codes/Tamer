package layout.client.impl;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;

public class MoveTask extends Task {

    public MoveTask(Element element, TaskAgent agent) {
        super(element, agent);
    }

    public void onMouseDown(Event event) {
        originalMouseX = DOM.eventGetClientX(event);
        originalMouseY = DOM.eventGetClientY(event);
        originalPositionX = DOM.getIntStyleAttribute(element, "left");
        originalPositionY = DOM.getIntStyleAttribute(element, "top");
    }

    public void onMouseMove(Event event) {
        deltaMouseX = DOM.eventGetClientX(event) - originalMouseX;
        deltaMouseY = DOM.eventGetClientY(event) - originalMouseY;
        DOM.setStyleAttribute(element, "left", (deltaMouseX + originalPositionX) + "px");
        DOM.setStyleAttribute(element, "top", (deltaMouseY + originalPositionY) + "px");
    }

    public void onMouseUp(Event event) {
        passivate();
    }

    private int originalMouseX, originalMouseY;

    private int deltaMouseX, deltaMouseY;

    private int originalPositionX, originalPositionY;
}
