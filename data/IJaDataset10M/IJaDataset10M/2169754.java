package au.edu.qut.yawl.editor.actions;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JComponent;
import au.edu.qut.yawl.editor.specification.ArchivingThread;
import au.edu.qut.yawl.editor.specification.SpecificationFileModel;
import au.edu.qut.yawl.editor.swing.TooltipTogglingWidget;
import au.edu.qut.yawl.editor.actions.specification.YAWLOpenSpecificationAction;

public class ExitAction extends YAWLOpenSpecificationAction implements TooltipTogglingWidget {

    {
        putValue(Action.SHORT_DESCRIPTION, getEnabledTooltipText());
        putValue(Action.NAME, "Exit");
        putValue(Action.LONG_DESCRIPTION, "Exit the application.");
        putValue(Action.SMALL_ICON, getIconByName("Blank"));
        putValue(Action.MNEMONIC_KEY, new Integer(java.awt.event.KeyEvent.VK_X));
    }

    public ExitAction(JComponent menu) {
    }

    public void actionPerformed(ActionEvent event) {
        ArchivingThread.getInstance().exit();
    }

    public void specificationFileModelStateChanged(int state) {
        switch(state) {
            case SpecificationFileModel.BUSY:
                {
                    setEnabled(false);
                    break;
                }
            default:
                {
                    setEnabled(true);
                }
        }
    }

    public String getEnabledTooltipText() {
        return " Exit the application ";
    }

    public String getDisabledTooltipText() {
        return " You cannot exit the application until there the current file operation completes ";
    }
}
