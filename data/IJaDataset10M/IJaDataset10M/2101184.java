package maze.commons.examples.auction.common.ui.impl;

import maze.commons.examples.auction.common.ui.BasicInteraction;
import maze.commons.examples.auction.common.ui.UiBasicInteraction;

/**
 * @author Normunds Mazurs (MAZE)
 * 
 */
public class UiBasicInteractionImpl implements UiBasicInteraction {

    protected final BasicInteraction basicInteraction;

    protected UiBasicInteractionImpl(final BasicInteraction basicInteraction) {
        this.basicInteraction = basicInteraction;
    }

    @Override
    public BasicInteraction getBasicInteraction() {
        return basicInteraction;
    }
}
