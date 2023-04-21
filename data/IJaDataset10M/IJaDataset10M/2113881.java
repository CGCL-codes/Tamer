package net.sf.beatrix.ui.events;

import net.sf.beatrix.util.event.SimpleEvent;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.widgets.Control;

public class ControlMessageEvent extends SimpleEvent<Control> implements IMessageProvider {

    private static final long serialVersionUID = 3378935438215820567L;

    private Control control;

    private String message;

    private int messageType;

    public ControlMessageEvent(Control control, String message, int messageType) {
        super(control);
        this.control = control;
        this.message = message;
        this.messageType = messageType;
    }

    public Control getControl() {
        return control;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getMessageType() {
        return messageType;
    }
}
