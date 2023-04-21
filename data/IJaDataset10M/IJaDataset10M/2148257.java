package pl.org.minions.stigma.client.ui.event.listeners;

import pl.org.minions.stigma.client.ui.event.UiEventListener;

/**
 * Listener for event: item type downloaded.
 */
public interface ItemTypeLoadedListener extends UiEventListener {

    /**
     * Called when event occurred.
     * @param id
     *            id of loaded item type
     */
    void itemTypeLoaded(short id);
}
