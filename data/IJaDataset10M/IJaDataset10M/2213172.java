package org.openscience.jchempaint.controller.undoredo;

import org.openscience.cdk.interfaces.IAtom;

/**
 * Undo/Redo Edit class for the ChangeIsotopeAction, containing the methods for
 * undoing and redoing the regarding changes
 * 
 * @cdk.module controlextra
 * @cdk.svnrev  $Revision: 10979 $
 */
public class ChangeIsotopeEdit implements IUndoRedoable {

    private static final long serialVersionUID = -8177452346351978213L;

    private IAtom atom;

    private Integer formerIsotopeNumber;

    private Integer isotopeNumber;

    private String type;

    /**
	 * @param atom
	 *            The atom been changed
	 * @param formerIsotopeNumber
	 *            The former mass number
	 * @param isotopeNumber
	 *            The new mass number
	 */
    public ChangeIsotopeEdit(IAtom atom, Integer formerIsotopeNumber, Integer isotopeNumber, String type) {
        this.atom = atom;
        this.formerIsotopeNumber = formerIsotopeNumber;
        this.isotopeNumber = isotopeNumber;
        this.type = type;
    }

    public void redo() {
        this.atom.setMassNumber(isotopeNumber);
    }

    public void undo() {
        this.atom.setMassNumber(formerIsotopeNumber);
    }

    public boolean canRedo() {
        return true;
    }

    public boolean canUndo() {
        return true;
    }

    public String getPresentationName() {
        return type;
    }
}
