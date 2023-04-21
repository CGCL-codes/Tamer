package net.sf.jawp.api.ai;

import net.sf.jawp.api.service.JAWPGameService;

/**
 * Basic interface for all kinds of AI players
 * @author jarek
 * @version $Revision$
 *
 */
public interface AIPlayer {

    /**
	 * notifies AIPlayer that it is connected to game service
	 * @param service
	 */
    void start(final JAWPGameService service);

    /**
	 * the game has stopped - it is time to write all data
	 *
	 */
    void stop();

    /**
	 * the game has made a small step - maybe we should react somehow
	 * @param service
	 */
    void step(final JAWPGameService service);
}
