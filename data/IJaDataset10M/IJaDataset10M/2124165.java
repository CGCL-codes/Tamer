package org.openscience.jchempaint.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.openscience.cdk.config.IsotopeFactory;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.IIsotope;
import org.openscience.jchempaint.controller.AddAtomModule;
import org.openscience.jchempaint.controller.AddBondDragModule;
import org.openscience.jchempaint.dialog.PeriodicTableDialog;

/**
 * Changes the isotope for a selected atom
 * 
 */
public class ChangeIsotopeAction extends JCPAction {

    private static final long serialVersionUID = -4692219842740123315L;

    public void actionPerformed(ActionEvent event) {
        AddAtomModule newActiveModule = new AddAtomModule(jcpPanel.get2DHub(), IBond.Stereo.NONE);
        if (jcpPanel.get2DHub().getActiveDrawModule() instanceof AddBondDragModule) newActiveModule = new AddAtomModule(jcpPanel.get2DHub(), ((AddBondDragModule) jcpPanel.get2DHub().getActiveDrawModule()).getStereoForNewBond()); else if (jcpPanel.get2DHub().getActiveDrawModule() instanceof AddAtomModule) newActiveModule = new AddAtomModule(jcpPanel.get2DHub(), ((AddAtomModule) jcpPanel.get2DHub().getActiveDrawModule()).getStereoForNewBond());
        logger.debug("About to change atom type of relevant atom!");
        IChemObject object = getSource(event);
        logger.debug("Source of call: ", object);
        Iterator<IAtom> atomsInRange = null;
        if (object == null) {
            if (jcpPanel.getRenderPanel().getRenderer().getRenderer2DModel().getSelection().isFilled()) atomsInRange = jcpPanel.getRenderPanel().getRenderer().getRenderer2DModel().getSelection().getConnectedAtomContainer().atoms().iterator();
        } else if (object instanceof IAtom) {
            List<IAtom> atoms = new ArrayList<IAtom>();
            atoms.add((IAtom) object);
            atomsInRange = atoms.iterator();
        } else {
            List<IAtom> atoms = new ArrayList<IAtom>();
            atoms.add(jcpPanel.getRenderPanel().getRenderer().getRenderer2DModel().getHighlightedAtom());
            atomsInRange = atoms.iterator();
        }
        if (atomsInRange == null) return;
        while (atomsInRange.hasNext()) {
            IAtom atom = atomsInRange.next();
            int isotopeNumber = 0;
            try {
                IIsotope isotope = IsotopeFactory.getInstance(atom.getBuilder()).getMajorIsotope(atom.getSymbol());
                isotopeNumber = isotope.getMassNumber();
            } catch (Exception exception) {
                logger.error("Error while configuring atom");
                logger.debug(exception);
            }
            if (type.equals("major")) {
            } else if (type.equals("majorPlusOne")) {
                isotopeNumber += 1;
            } else if (type.equals("majorPlusTwo")) {
                isotopeNumber += 2;
            } else if (type.equals("majorPlusThree")) {
                isotopeNumber += 3;
            } else if (type.equals("majorMinusOne")) {
                isotopeNumber -= 1;
            } else if (type.equals("majorMinusTwo")) {
                isotopeNumber -= 2;
            } else if (type.equals("majorMinusThree")) {
                isotopeNumber -= 3;
            } else if (type.indexOf("specific") == 0) {
                isotopeNumber = Integer.parseInt(type.substring(8));
            }
            jcpPanel.get2DHub().setMassNumber(atom, isotopeNumber);
            jcpPanel.get2DHub().updateView();
            newActiveModule.setID(atom.getSymbol());
            jcpPanel.get2DHub().getController2DModel().setDrawElement(atom.getSymbol());
            jcpPanel.get2DHub().getController2DModel().setDrawIsotopeNumber(isotopeNumber);
            jcpPanel.get2DHub().getController2DModel().setDrawPseudoAtom(false);
        }
        jcpPanel.get2DHub().setActiveDrawModule(newActiveModule);
    }
}
