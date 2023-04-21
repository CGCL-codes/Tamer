package org.openscience.jchempaint.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import javax.swing.JFileChooser;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.listener.SwingGUIListener;
import org.openscience.cdk.io.program.GaussianInputWriter;
import org.openscience.cdk.tools.manipulator.ChemModelManipulator;
import org.openscience.jchempaint.io.JCPCompChemInputSaveFileFilter;
import org.openscience.jchempaint.io.JCPFileView;

/**
 * Export current model to computational chemistry programs
 * 
 */
public class ExportCompChemAction extends SaveAction {

    private static final long serialVersionUID = -407195104869621963L;

    /**
    * Opens a dialog frame and manages the saving of a file.
    */
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(jcpPanel.getCurrentWorkDirectory());
        JCPCompChemInputSaveFileFilter.addChoosableFileFilters(chooser);
        chooser.setFileView(new JCPFileView());
        int returnVal = chooser.showSaveDialog(jcpPanel);
        String type = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            type = ((JCPCompChemInputSaveFileFilter) chooser.getFileFilter()).getType();
            File outFile = chooser.getSelectedFile();
            if (type.equals(JCPCompChemInputSaveFileFilter.gin)) {
                logger.info("Saving the contents as Gaussian input...");
                try {
                    cow = new GaussianInputWriter(new FileWriter(outFile));
                    if (cow != null) {
                        cow.addChemObjectIOListener(new SwingGUIListener(jcpPanel, 4));
                    }
                    Iterator containers = ChemModelManipulator.getAllAtomContainers(jcpPanel.getChemModel()).iterator();
                    while (containers.hasNext()) {
                        IAtomContainer ac = (IAtomContainer) containers.next();
                        if (ac != null) {
                            cow.write(new Molecule(ac));
                        } else {
                            logger.error("AtomContainer is empty!!!");
                            System.err.println("AC == null!");
                        }
                    }
                    cow.close();
                } catch (Exception exception) {
                    logger.error("Exception while trying to save Gaussian input");
                    logger.debug(exception);
                }
            }
        }
        jcpPanel.setCurrentWorkDirectory(chooser.getCurrentDirectory());
    }
}
