package rogue.netbeans.module.importsources;

import org.openide.WizardDescriptor;
import rogue.netbeans.module.importsources.ui.WizardPanel1UI;
import java.awt.Component;
import java.io.File;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author Rogue
 */
public class WizardPanel1 extends AbstractWizardPanel implements WizardDescriptor.Panel, DocumentListener {

    private WizardPanel1UI component;

    private WizardDescriptor wd = null;

    private String selectedDirectory = null;

    public Component getComponent() {
        if (component == null) {
            component = new WizardPanel1UI(this);
        }
        return component;
    }

    public boolean isValid() {
        if ((selectedDirectory == null) || (selectedDirectory.length() == 0)) {
            if (wd != null) {
                wd.putProperty("WizardPanel_errorMessage", "Specify a valid directory.");
            }
            return false;
        }
        File f = new File(selectedDirectory);
        if (!f.isDirectory()) {
            if (wd != null) {
                wd.putProperty("WizardPanel_errorMessage", "Specify a valid directory.");
            }
            return false;
        }
        if (wd != null) {
            wd.putProperty("WizardPanel_errorMessage", null);
        }
        return true;
    }

    public void readSettings(Object settings) {
        if (settings instanceof WizardDescriptor) {
            wd = (WizardDescriptor) settings;
            fireChangeEvent();
        }
    }

    public void storeSettings(Object settings) {
        wd.putProperty(ImportSourcesConstants.SELECTED_DIRECTORY.toString(), selectedDirectory);
        wd.putProperty(ImportSourcesConstants.FILE_EXTENSIONS.toString(), component.getFileExensions());
        wd.putProperty(ImportSourcesConstants.SCAN_FOR_FILES.toString(), Boolean.TRUE);
    }

    public void insertUpdate(DocumentEvent e) {
        selectedDirectory = component.getSelectedDirectory();
        fireChangeEvent();
    }

    public void removeUpdate(DocumentEvent e) {
        selectedDirectory = component.getSelectedDirectory();
        fireChangeEvent();
    }

    public void changedUpdate(DocumentEvent e) {
    }

    public boolean isPanelProcessingComplete() {
        return true;
    }

    public void terminateProcessing() {
    }
}
