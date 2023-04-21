package com.tensegrity.palowebviewer.modules.widgets.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.IAction;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.IPropertyListener;

public class ActionButton extends Button {

    private final ClickListener listener = new ClickListener() {

        public void onClick(Widget sender) {
            if (action != null && action.isEnabled()) {
                action.onActionPerformed(null);
            }
        }
    };

    private IAction action;

    private final IPropertyListener propertyListener = new IPropertyListener() {

        public void onDisabled() {
            checkEnabled();
            setEnabled(action.isEnabled());
        }

        public void onEnabled() {
            checkEnabled();
        }
    };

    public ActionButton() {
        this.addClickListener(listener);
        setStyleName("action-button");
    }

    public ActionButton(String string) {
        this();
        setText(string);
    }

    public void setAction(IAction action) {
        if (this.action != null) {
            action.removePropertyListener(propertyListener);
        }
        this.action = action;
        if (action != null) {
            action.addPropertyListener(propertyListener);
        }
        checkEnabled();
    }

    public IAction getAction() {
        return action;
    }

    private void checkEnabled() {
        if (action != null) {
            setEnabled(action.isEnabled());
        } else {
            setEnabled(false);
        }
    }
}
