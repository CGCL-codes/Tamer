package org.openscience.jchempaint.undoredo;

import javax.swing.undo.UndoableEdit;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.jchempaint.controller.IChemModelRelay;
import org.openscience.jchempaint.controller.undoredo.ChangeChargeEdit;

/**
 * A swing undo-redo implementation for changing charge of an Atom.
 *
 */
public class SwingChangeChargeEdit extends ChangeChargeEdit implements UndoableEdit {

    public SwingChangeChargeEdit(IAtom atomInRange, int formerCharge, int newCharge, String type, IChemModelRelay chemModelRelay) {
        super(atomInRange, formerCharge, newCharge, type, chemModelRelay);
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
