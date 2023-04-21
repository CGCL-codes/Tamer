package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import fr.soleil.mambo.components.view.VCCustomTabbedPane;
import fr.soleil.mambo.containers.view.dialogs.VCEditDialog;

public class VCBackAction extends AbstractAction {

    private static final long serialVersionUID = -8564093936632385054L;

    private VCEditDialog editDialog;

    /**
	 * @param name
	 */
    public VCBackAction(String name, VCEditDialog editDialog) {
        super.putValue(Action.NAME, name);
        super.putValue(Action.SHORT_DESCRIPTION, name);
        this.editDialog = editDialog;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        VCCustomTabbedPane tabbedPane = editDialog.getVcCustomTabbedPane();
        int oldValue = tabbedPane.getSelectedIndex();
        int newValue = oldValue - 1;
        if (oldValue == 2) {
            editDialog.getAttributesPlotPropertiesTab().getVcAttributesPropertiesTree().saveLastSelectionPath();
            editDialog.getAttributesPlotPropertiesTab().getVcAttributesPropertiesTree().getVcAttributesPropertiesTreeSelectionListener().treeSelectionAttributeSave();
        }
        if (oldValue == 3) {
            editDialog.getExpressionTab().getExpressionTree().saveCurrentSelection();
            editDialog.getExpressionTab().getExpressionTree().getExpressionTreeListener().treeSelectionSave();
        }
        tabbedPane.setEnabledAt(newValue, true);
        tabbedPane.setSelectedIndex(newValue);
        switch(newValue) {
            case 0:
                tabbedPane.setEnabledAt(1, false);
                tabbedPane.setEnabledAt(2, false);
                tabbedPane.setEnabledAt(3, false);
                break;
            case 1:
                tabbedPane.setEnabledAt(0, false);
                tabbedPane.setEnabledAt(2, false);
                tabbedPane.setEnabledAt(3, false);
                break;
            case 2:
                tabbedPane.setEnabledAt(0, false);
                tabbedPane.setEnabledAt(1, false);
                tabbedPane.setEnabledAt(3, false);
                break;
            default:
                throw new IllegalStateException();
        }
    }
}
