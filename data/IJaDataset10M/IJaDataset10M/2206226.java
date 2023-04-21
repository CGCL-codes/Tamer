package net.sourceforge.jruntimedesigner.events;

import java.util.EventListener;

public interface IPanelStateListener extends EventListener {

    void panelActivated(PanelStateEvent event);

    void panelDeactivated(PanelStateEvent event);
}
