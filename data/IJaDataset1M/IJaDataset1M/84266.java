package com.example.possessed;

import java.io.Serializable;
import java.util.Map;
import com.vaadin.data.Property;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;

/**
 * Server side component for the VDisableButton widget.
 */
@com.vaadin.ui.ClientWidget(com.example.possessed.widgetset.client.ui.VDisableButton.class)
public class DisableButton extends Button implements Serializable {

    public DisableButton() {
        super();
    }

    public DisableButton(String caption, boolean initialState) {
        super(caption, initialState);
    }

    public DisableButton(String caption, ClickListener listener) {
        super(caption, listener);
    }

    public DisableButton(String caption, Object target, String methodName) {
        super(caption, target, methodName);
    }

    public DisableButton(String caption, Property dataSource) {
        super(caption, dataSource);
    }

    public DisableButton(String caption) {
        super(caption);
    }
}
