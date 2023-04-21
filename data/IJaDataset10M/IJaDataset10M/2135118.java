package kohary.datamodel.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import kohary.datamodel.DatamodelCreator;
import kohary.datamodel.util.GraphicsTools;
import kohary.datamodel.variables.VariableBox;
import kohary.datamodel.variables.VariablePanel;

/**
 *
 * @author Godric
 */
public class ConfirmVariableAction extends Action {

    private VariablePanel atbPanel;

    private VariableBox atbBox;

    public ConfirmVariableAction(VariablePanel atbPanel, VariableBox atbBox) {
        this.atbPanel = atbPanel;
        this.atbBox = atbBox;
        String name = "Confirm";
        putValue(NAME, name);
        putValue(SHORT_DESCRIPTION, name);
        putValue(SMALL_ICON, GraphicsTools.getIcon("formsIcon.png"));
        putValue(SHORT_DESCRIPTION, "");
        putValue(MNEMONIC_KEY, KeyEvent.VK_H);
    }

    public void actionPerformed(ActionEvent e) {
        DatamodelCreator.getInstance().getDocument().setVariables(atbPanel.getVariables());
        atbBox.getAtCrFrame().setVisible(false);
        if (atbPanel.getVariables().isEmpty()) {
            DatamodelCreator.getInstance().getMainFrame().choosingVariableAction.setEnabled(false);
        } else DatamodelCreator.getInstance().getMainFrame().choosingVariableAction.setEnabled(true);
    }
}
