package org.openscience.cdk.applications.jchempaint.action;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JFrame;
import org.openscience.cdk.ChemModel;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.applications.jchempaint.dialogs.TextViewDialog;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.layout.HydrogenPlacer;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tools.HydrogenAdder;
import org.openscience.cdk.tools.manipulator.ChemModelManipulator;

/**
 * Creates a SMILES from the current model
 *
 * @cdk.module jchempaint
 * @author     steinbeck
 */
public class CreateSmilesAction extends JCPAction {

    private static final long serialVersionUID = -4886982931009753342L;

    TextViewDialog dialog = null;

    JFrame frame = null;

    public void actionPerformed(ActionEvent e) {
        logger.debug("Trying to create smile: ", type);
        if (dialog == null) {
            dialog = new TextViewDialog(frame, "SMILES", null, false, 40, 2);
        }
        String smiles = "";
        String chiralsmiles = "";
        try {
            smiles = getSmiles((ChemModel) jcpPanel.getJChemPaintModel().getChemModel(), jcpPanel.getJChemPaintModel().getRendererModel().getRenderingCoordinates());
            chiralsmiles = getChiralSmiles((ChemModel) jcpPanel.getJChemPaintModel().getChemModel(), jcpPanel.getJChemPaintModel().getRendererModel().getRenderingCoordinates());
            dialog.setMessage("Generated SMILES:", "SMILES: " + smiles + System.getProperty("line.separator") + "chiral SMILES: " + chiralsmiles);
        } catch (Exception exception) {
            String message = "Error while creating SMILES: " + exception.getMessage();
            logger.error(message);
            logger.debug(exception);
            dialog.setMessage("Error", message);
        }
        dialog.show();
    }

    public static String getSmiles(ChemModel model, HashMap renderingCoordinates) throws CDKException, ClassNotFoundException, IOException, CloneNotSupportedException {
        String smiles = "";
        SmilesGenerator generator = new SmilesGenerator();
        Iterator containers = ChemModelManipulator.getAllAtomContainers(model).iterator();
        while (containers.hasNext()) {
            IAtomContainer container = (IAtomContainer) containers.next();
            Molecule molecule = new Molecule(container);
            Molecule moleculewithh = (Molecule) molecule.clone();
            new HydrogenAdder().addExplicitHydrogensToSatisfyValency(moleculewithh);
            double bondLength = GeometryTools.getBondLengthAverage(container, renderingCoordinates);
            new HydrogenPlacer().placeHydrogens2D(moleculewithh, bondLength);
            smiles += generator.createSMILES(molecule);
            boolean[] bool = new boolean[moleculewithh.getBondCount()];
            SmilesGenerator sg = new SmilesGenerator();
            for (int i = 0; i < bool.length; i++) {
                if (sg.isValidDoubleBondConfiguration(moleculewithh, moleculewithh.getBond(i))) bool[i] = true;
            }
            if (containers.hasNext()) {
                smiles += ".";
            }
        }
        return smiles;
    }

    public static String getChiralSmiles(ChemModel model, HashMap renderingCoordinates) throws CDKException, ClassNotFoundException, IOException, CloneNotSupportedException {
        String chiralsmiles = "";
        SmilesGenerator generator = new SmilesGenerator();
        Iterator containers = ChemModelManipulator.getAllAtomContainers(model).iterator();
        while (containers.hasNext()) {
            IAtomContainer container = (IAtomContainer) containers.next();
            Molecule molecule = new Molecule(container);
            Molecule moleculewithh = (Molecule) molecule.clone();
            new HydrogenAdder().addExplicitHydrogensToSatisfyValency(moleculewithh);
            double bondLength = GeometryTools.getBondLengthAverage(container, renderingCoordinates);
            new HydrogenPlacer().placeHydrogens2D(moleculewithh, bondLength);
            boolean[] bool = new boolean[moleculewithh.getBondCount()];
            SmilesGenerator sg = new SmilesGenerator();
            for (int i = 0; i < bool.length; i++) {
                if (sg.isValidDoubleBondConfiguration(moleculewithh, moleculewithh.getBond(i))) bool[i] = true;
            }
            chiralsmiles += generator.createChiralSMILES(moleculewithh, bool);
            if (containers.hasNext()) {
                chiralsmiles += ".";
            }
        }
        return chiralsmiles;
    }
}
