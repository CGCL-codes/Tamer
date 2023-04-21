package com.gwtext.client.widgets.event;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.menu.Menu;

/**
 *
 * @author Sanjiv Jivan
 */
public class ButtonListenerAdapter extends ComponentListenerAdapter implements ButtonListener {

    public void onClick(Button button, EventObject e) {
    }

    public void onMenuHide(Button button, Menu menu) {
    }

    public void onMenuShow(Button button, Menu menu) {
    }

    public void onMenuTriggerOut(Button button, Menu menu, EventObject e) {
    }

    public void onMenuTriggerOver(Button button, Menu menu, EventObject e) {
    }

    public void onMouseOut(Button button, EventObject e) {
    }

    public void onMouseOver(Button button, EventObject e) {
    }

    public void onToggle(Button button, boolean pressed) {
    }
}
