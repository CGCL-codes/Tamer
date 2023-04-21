package org.openscience.jchempaint.controller;

import javax.vecmath.Point2d;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.jchempaint.renderer.selection.AbstractSelection;

/**
 * Deletes closest atom on click
 * 
 * @author Niels Out
 * @cdk.svnrev  $Revision: 9162 $
 * @cdk.module  controlbasic
 */
public class RemoveModule extends ControllerModuleAdapter {

    private String ID;

    public RemoveModule(IChemModelRelay chemObjectRelay) {
        super(chemObjectRelay);
    }

    public void mouseClickedDown(Point2d worldCoordinate) {
        IAtomContainer selectedAC = getSelectedAtomContainer(worldCoordinate);
        if (selectedAC == null) return;
        for (IAtom atom : selectedAC.atoms()) {
            chemModelRelay.removeAtom(atom);
        }
        for (IBond bond : selectedAC.bonds()) {
            chemModelRelay.removeBondAndLoneAtoms(bond);
        }
        setSelection(AbstractSelection.EMPTY_SELECTION);
    }

    public String getDrawModeString() {
        return "Delete";
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
