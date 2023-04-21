package com.innovative.main.event;

import java.util.EventListener;

/**
 * Listens for {@link MainEvent}s
 *
 * @author Dylon Edwards
 * @since 0.3
 */
public interface CanvasListener extends EventListener {

    /**
	 * Fired whenever {@link DocumentCanvas#paperSize} changes
	 *
	 * @param event The {@link MainEvent} fired after the paper size changed
	 */
    void paperSizeChanged(CanvasEvent event);
}
