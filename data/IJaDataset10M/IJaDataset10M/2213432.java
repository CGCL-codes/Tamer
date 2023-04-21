package org.openscience.jchempaint.undoredo;

import java.util.Map;
import javax.swing.undo.UndoableEdit;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.jchempaint.controller.IChemModelRelay;
import org.openscience.jchempaint.controller.undoredo.AdjustBondOrdersEdit;

/**
 * A swing undo-redo implementation for adding Atoms and Bonds
 *
 */
public class SwingAdjustBondOrdersEdit extends AdjustBondOrdersEdit implements UndoableEdit {

    public SwingAdjustBondOrdersEdit(Map<IBond, IBond.Order[]> changedBonds, Map<IBond, IBond.Stereo[]> changedBondsStereo, String type, IChemModelRelay chemModelRelay) {
        super(changedBonds, changedBondsStereo, type, chemModelRelay);
    }

    public boolean addEdit(UndoableEdit arg0) {
        return false;
    }

    public void die() {
    }

    public String getRedoPresentationName() {
        return getPresentationName();
    }

    public String getUndoPresentationName() {
        return getPresentationName();
    }

    public boolean isSignificant() {
        return true;
    }

    public boolean replaceEdit(UndoableEdit arg0) {
        return false;
    }
}
