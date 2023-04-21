package org.fpdev.apps.cart.actions;

import org.fpdev.apps.cart.Cartographer;
import org.fpdev.apps.cart.Editor;
import org.fpdev.apps.cart.network.AnchorPoint;
import org.fpdev.apps.cart.network.Corridor;

/**
 *
 * @author demory
 */
public class CreateCorridorAction extends EditorBasedAction {

    private Corridor corridor_;

    private AnchorPoint from_, to_;

    public CreateCorridorAction(Editor ed, AnchorPoint from, AnchorPoint to) {
        super(ed);
        from_ = from;
        to_ = to;
    }

    public boolean doAction(Cartographer cart) {
        corridor_ = new Corridor(ed_.getDocument().getNetwork().newCorridorID(), from_, to_, true);
        ed_.getDocument().getNetwork().addCorridor(corridor_);
        return true;
    }

    public boolean undoAction(Cartographer cart) {
        if (corridor_ == null) return false;
        ed_.getDocument().getNetwork().deleteCorridor(corridor_);
        return true;
    }

    public String getName() {
        return "Create Corridor";
    }
}
