package org.fpdev.apps.cart.actions;

import org.fpdev.apps.cart.Cartographer;
import org.fpdev.apps.cart.Editor;
import org.fpdev.apps.cart.network.Corridor;

/**
 *
 * @author demory
 */
public class FlipCorridorAction extends EditorBasedAction {

    private Corridor corr_;

    public FlipCorridorAction(Editor ed, Corridor corr) {
        super(ed);
        corr_ = corr;
    }

    public boolean doAction(Cartographer cart) {
        corr_.flip();
        ed_.getDocument().getNetwork().rebundle();
        return true;
    }

    public boolean undoAction(Cartographer cart) {
        corr_.flip();
        ed_.getDocument().getNetwork().rebundle();
        return true;
    }

    public String getName() {
        return "Flip Corridor";
    }
}
