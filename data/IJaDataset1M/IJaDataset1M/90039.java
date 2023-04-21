package org.openscience.cdk.applications.taverna.io.action;

import java.awt.Dimension;
import java.io.File;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import org.embl.ebi.escience.scufl.Processor;
import org.embl.ebi.escience.scuflui.TavernaIcons;
import org.openscience.cdk.applications.taverna.actions.AbstractCDKProcessorAction;
import org.openscience.cdk.applications.taverna.io.ReadSMILESFromFile;
import org.openscience.cdk.applications.taverna.scuflworkers.cdk.CDKProcessor;

/**
 * This class handles the action of the Read from Smiles file and creates an menu entry for this local processor
 * 
 * @author Thomas Kuhn
 */
public class ReadSMILESFromFileAction extends AbstractCDKProcessorAction {

    private String descriptionAdder = "";

    @Override
    public JComponent getComponent(Processor processor) {
        if (!(processor instanceof CDKProcessor)) {
            return null;
        }
        JFileChooser fileChooser = new JFileChooser();
        CDKProcessor proc = (CDKProcessor) processor;
        final ReadSMILESFromFile reader = (ReadSMILESFromFile) proc.getWorker();
        boolean fileSearch = true;
        try {
            ;
            String fileName = null;
            fileChooser.setMultiSelectionEnabled(true);
            while (fileSearch) {
                if (reader.getFileNames() != null && reader.getFileNames().size() != 0) {
                    fileName = reader.getFileNames().get(0);
                }
                if (fileName != null && fileName.length() > 0) {
                    fileChooser.setCurrentDirectory(new java.io.File(fileName));
                }
                fileChooser.setMultiSelectionEnabled(true);
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File[] files = fileChooser.getSelectedFiles();
                    for (int i = 0; i < files.length; i++) {
                        reader.addFileName(files[i].getAbsolutePath());
                    }
                    fileSearch = false;
                } else {
                    fileSearch = false;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public boolean canHandle(Processor processor) {
        if (!(processor instanceof CDKProcessor)) {
            return false;
        }
        CDKProcessor proc = (CDKProcessor) processor;
        if (proc.getWorker() instanceof ReadSMILESFromFile) {
            ReadSMILESFromFile reader = (ReadSMILESFromFile) proc.getWorker();
            File file;
            String temp = "";
            if (reader.getFileNames().size() != 0) {
                for (Iterator<String> iterator = reader.getFileNames().iterator(); iterator.hasNext(); ) {
                    String fileName = iterator.next();
                    file = new File(fileName);
                    if (file.isFile()) {
                        temp += file.getName() + "; ";
                    }
                }
                if (temp.length() != 0) {
                    this.descriptionAdder = " - selected file: " + temp;
                }
            } else {
                this.descriptionAdder = "";
            }
            return true;
        } else {
            return false;
        }
    }

    public String getDescription() {
        return "Open File" + this.descriptionAdder;
    }

    public Dimension getFrameSize() {
        return new Dimension(1, 1);
    }

    public ImageIcon getIcon() {
        return TavernaIcons.openIcon;
    }

    @Override
    public JFrame getFrame(Processor processor) {
        return null;
    }
}
