package net.sf.jmms;

import java.util.EventListener;

public interface PlayerListener extends EventListener {

    public void playerEvent(PlayerEvent e);
}
