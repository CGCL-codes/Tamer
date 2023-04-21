package org.columba.api.gui.frame.event;

import java.util.EventObject;
import org.columba.api.gui.frame.IFrameMediator;

public class FrameEvent extends EventObject {

    private String text;

    private boolean visible;

    private IFrameMediator mediator;

    public FrameEvent(Object source) {
        super(source);
    }

    public FrameEvent(Object source, IFrameMediator mediator) {
        this(source);
        this.mediator = mediator;
    }

    public FrameEvent(Object source, String text) {
        this(source);
        this.text = text;
    }

    public FrameEvent(Object source, boolean visible) {
        this(source);
        this.visible = visible;
    }

    public String getText() {
        return text;
    }

    public boolean isVisible() {
        return visible;
    }

    /**
	 * @return Returns the mediator.
	 */
    public IFrameMediator getMediator() {
        return mediator;
    }
}
