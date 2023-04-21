package gtkwire.listener;

import gtkwire.GTKWireMessage;

public interface ToggleListener {

    public void buttonToggled(boolean buttonIsDown, GTKWireMessage msg);
}
