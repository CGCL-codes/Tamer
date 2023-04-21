package net.firefly.client.gui.context.listeners;

import java.util.EventListener;
import net.firefly.client.gui.context.events.SelectedPlaylistChangedEvent;

public interface SelectedPlaylistChangedEventListener extends EventListener {

    public void onSelectedPlaylistChange(SelectedPlaylistChangedEvent evt);
}
