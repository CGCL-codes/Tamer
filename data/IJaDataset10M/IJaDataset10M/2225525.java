package org.openscience.cdk.applications.undoredo;

import java.util.Iterator;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.tools.manipulator.ChemModelManipulator;

/**
 * @cdk.module control
 */
public class RemoveAtomsAndBondsEdit extends AbstractUndoableEdit {

    private static final long serialVersionUID = -143712173063846054L;

    private String type;

    private IAtomContainer undoRedoContainer;

    private IChemModel chemModel;

    private IAtomContainer container;

    public RemoveAtomsAndBondsEdit(IChemModel chemModel, IAtomContainer undoRedoContainer, String type) {
        this.chemModel = chemModel;
        this.undoRedoContainer = undoRedoContainer;
        this.container = chemModel.getBuilder().newAtomContainer();
        Iterator containers = ChemModelManipulator.getAllAtomContainers(chemModel).iterator();
        while (containers.hasNext()) {
            container.add((IAtomContainer) containers.next());
        }
        this.type = type;
    }

    public void redo() throws CannotRedoException {
        for (int i = 0; i < undoRedoContainer.getBondCount(); i++) {
            IBond bond = undoRedoContainer.getBond(i);
            container.removeBond(bond);
        }
        for (int i = 0; i < undoRedoContainer.getAtomCount(); i++) {
            IAtom atom = undoRedoContainer.getAtom(i);
            container.removeAtom(atom);
        }
        IMolecule molecule = container.getBuilder().newMolecule(container);
        IMoleculeSet moleculeSet = ConnectivityChecker.partitionIntoMolecules(molecule);
        chemModel.setMoleculeSet(moleculeSet);
    }

    public void undo() throws CannotUndoException {
        for (int i = 0; i < undoRedoContainer.getBondCount(); i++) {
            IBond bond = undoRedoContainer.getBond(i);
            container.addBond(bond);
        }
        for (int i = 0; i < undoRedoContainer.getAtomCount(); i++) {
            IAtom atom = undoRedoContainer.getAtom(i);
            container.addAtom(atom);
        }
        IMolecule molecule = container.getBuilder().newMolecule(container);
        IMoleculeSet moleculeSet = ConnectivityChecker.partitionIntoMolecules(molecule);
        chemModel.setMoleculeSet(moleculeSet);
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
