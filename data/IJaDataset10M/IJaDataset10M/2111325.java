package ostf.gui.frame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JToolBar;

public class XSFormDefaultActions extends XSFormActions {

    private static final String NEW = "New";

    private static final String LOAD = "Load";

    private static final String SAVE = "Save";

    private static final String EDITABLE = "Editable";

    private boolean newEnabled = true;

    private boolean loadEnabled = true;

    private boolean saveEnabled = true;

    private boolean editableEnabled = true;

    private File currentFile = null;

    private JFileChooser fileChooser = new JFileChooser();

    public boolean isLoadEnabled() {
        return loadEnabled;
    }

    public void setLoadEnabled(boolean loadEnabled) {
        this.loadEnabled = loadEnabled;
    }

    public boolean isNewEnabled() {
        return newEnabled;
    }

    public void setNewEnabled(boolean newEnabled) {
        this.newEnabled = newEnabled;
    }

    public boolean isSaveEnabled() {
        return saveEnabled;
    }

    public void setSaveEnabled(boolean saveEnabled) {
        this.saveEnabled = saveEnabled;
    }

    public boolean isEditableEnabled() {
        return editableEnabled;
    }

    public void setEditableEnabled(boolean editableEnabled) {
        this.editableEnabled = editableEnabled;
    }

    public void addButtons() {
        JButton button = null;
        JToolBar toolBar = xsForm.getToolBar();
        if (newEnabled) {
            button = makeButton(NEW, "New " + xsForm.getFormTag());
            toolBar.add(button);
        }
        if (loadEnabled) {
            button = makeButton(LOAD, "Load " + xsForm.getFormTag());
            toolBar.add(button);
        }
        if (saveEnabled) {
            button = makeButton(SAVE, "Save " + xsForm.getFormTag());
            toolBar.add(button);
        }
        if (editableEnabled) {
            button = makeButton(EDITABLE, EDITABLE);
            toolBar.add(button);
        }
    }

    public void doCommand(String command) {
        if (NEW.equals(command)) {
            currentFile = null;
            xsForm.reloadForm(null);
        } else if (LOAD.equals(command)) {
            if (fileChooser.showOpenDialog(xsForm) == 0) {
                try {
                    FileInputStream fis = new FileInputStream(fileChooser.getSelectedFile());
                    if (xsForm.reloadForm(fis)) currentFile = fileChooser.getSelectedFile();
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (SAVE.equals(command)) {
            if (currentFile == null && fileChooser.showOpenDialog(xsForm) == 0) {
                currentFile = fileChooser.getSelectedFile();
            }
            if (currentFile != null) {
                try {
                    FileOutputStream fos = new FileOutputStream(currentFile);
                    xsForm.writeToOutput(fos, true);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (EDITABLE.equals(command)) {
            xsForm.setMode((xsForm.getMode() + 1) % 3);
        }
    }
}
