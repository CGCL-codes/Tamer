package org.openscience.cdk.applications.jchempaint.action;

import java.awt.event.ActionEvent;
import org.openscience.cdk.Atom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.EnzymeResidueLocator;
import org.openscience.cdk.PseudoAtom;
import org.openscience.cdk.applications.jchempaint.JChemPaintModel;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.ChemModelManipulator;

/**
 * Triggers the conversion of an Atom to a residueLocator 
 * or other objects
 *
 * @cdk.module jchempaint
 * @author     steinbeck
 */
public class ConvertToAction extends JCPAction {

    private static final long serialVersionUID = 4239327873015560552L;

    public void actionPerformed(ActionEvent event) {
        logger.debug("Converting to: ", type);
        IChemObject object = getSource(event);
        JChemPaintModel jcpmodel = jcpPanel.getJChemPaintModel();
        org.openscience.cdk.interfaces.IChemModel model = jcpmodel.getChemModel();
        if (object != null) {
            if (object instanceof Atom) {
                if (type.equals("atomToPseudoAtom")) {
                    Atom atom = (Atom) object;
                    IAtomContainer relevantContainer = ChemModelManipulator.getRelevantAtomContainer(model, atom);
                    AtomContainerManipulator.replaceAtomByAtom(relevantContainer, atom, new PseudoAtom(atom));
                } else if (type.equals("atomToEnzymeResidueLocator")) {
                    Atom atom = (Atom) object;
                    IAtomContainer relevantContainer = ChemModelManipulator.getRelevantAtomContainer(model, atom);
                    AtomContainerManipulator.replaceAtomByAtom(relevantContainer, atom, new EnzymeResidueLocator(atom));
                } else {
                    logger.error("Unknown convertTo type!");
                }
            } else {
                logger.error("Object not an Atom! Cannot convert into a PseudoAtom!");
            }
        } else {
            logger.warn("Cannot convert a null object!");
        }
        jcpmodel.fireChange();
    }
}
