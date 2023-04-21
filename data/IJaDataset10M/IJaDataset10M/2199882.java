package mw.server.model.effect;

import mw.server.model.Card;

public class ControllerPlaysRevealed extends ContiniousEffect {

    private static final long serialVersionUID = 1L;

    public ControllerPlaysRevealed(Card source) {
        super(source);
    }

    @Override
    public void applyEffect() {
        source.getGame().getPlayer(source.getController()).setRevealed(true);
    }

    @Override
    public void discardEffect() {
        source.getGame().getPlayer(source.getController()).setRevealed(false);
    }

    @Override
    public Layer getLayer() {
        return Layer.RULE;
    }
}
