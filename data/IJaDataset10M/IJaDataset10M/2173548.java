package calclipse.caldron.gui.script.editor;

import javax.swing.JToggleButton;
import calclipse.caldron.data.DataProject;
import calclipse.caldron.gui.script.ScriptItemEditor;

/**
 * An executable editor wrapping a toggle button.
 * @author T. Sommerland
 */
public class ToggleButtonExecutableEditor implements ScriptItemEditor.ExecutableEditor {

    private final JToggleButton button;

    public ToggleButtonExecutableEditor(final JToggleButton button) {
        this.button = button;
    }

    @Override
    public boolean isExecutable() {
        return button.isSelected();
    }

    @Override
    public void setExecutable(final boolean executable) {
        button.setSelected(executable);
        DataProject.getInstance().setModified(true);
    }
}
