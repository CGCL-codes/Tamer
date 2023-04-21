package es.eucm.eadventure.editor.control.tools.scene;

import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.scene.ElementContainer;
import es.eucm.eadventure.editor.control.controllers.scene.ReferencesListDataControl;
import es.eucm.eadventure.editor.control.tools.Tool;
import es.eucm.eadventure.editor.gui.elementpanels.scene.ElementReferencesTable;
import es.eucm.eadventure.editor.gui.otherpanels.ScenePreviewEditionPanel;

public class DeleteReferenceTool extends Tool {

    private ReferencesListDataControl referencesListDataControl;

    private ScenePreviewEditionPanel spep;

    private ElementReferencesTable table;

    private ElementContainer element;

    private int selectedRow;

    public DeleteReferenceTool(ReferencesListDataControl referencesListDataControl, ElementReferencesTable table, ScenePreviewEditionPanel spep) {
        this.table = table;
        this.referencesListDataControl = referencesListDataControl;
        this.spep = spep;
        this.selectedRow = table.getSelectedRow();
    }

    @Override
    public boolean canRedo() {
        return true;
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public boolean combine(Tool other) {
        return false;
    }

    @Override
    public boolean doTool() {
        element = referencesListDataControl.getAllReferencesDataControl().get(selectedRow);
        if (referencesListDataControl.deleteElement(element.getErdc(), true)) {
            if (!element.isPlayer()) {
                spep.removeElement(ReferencesListDataControl.transformType(element.getErdc().getType()), element.getErdc());
                table.clearSelection();
                table.changeSelection(0, 1, false, false);
                table.updateUI();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean redoTool() {
        ElementContainer element = referencesListDataControl.getAllReferencesDataControl().get(selectedRow);
        if (referencesListDataControl.deleteElement(element.getErdc(), true)) {
            if (!element.isPlayer()) {
                spep.removeElement(ReferencesListDataControl.transformType(element.getErdc().getType()), element.getErdc());
                table.clearSelection();
                table.changeSelection(0, 1, false, false);
                table.updateUI();
            }
            Controller.getInstance().updatePanel();
            return true;
        }
        return false;
    }

    @Override
    public boolean undoTool() {
        referencesListDataControl.addElement(element);
        spep.addElement(element.getErdc().getType(), element.getErdc());
        table.clearSelection();
        table.changeSelection(selectedRow, 1, false, false);
        table.updateUI();
        Controller.getInstance().updatePanel();
        return true;
    }
}
