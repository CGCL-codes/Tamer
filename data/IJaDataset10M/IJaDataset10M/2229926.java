package com.peterhi.client.ui.events;

import java.util.EventListener;

/**
 * Style bar event listener.
 * @author YUN TAO HAI
 *
 */
public interface StyleBarListener extends EventListener {

    /**
	 * The data of the style bar has updated.
	 * @param e The event object.
	 */
    void onUpdate(StyleBarEvent e);
}
