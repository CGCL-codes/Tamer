package es.eucm.eadventure.editor.control.tools.adaptation;

import es.eucm.eadventure.common.data.adaptation.AdaptationRule;
import es.eucm.eadventure.common.data.adaptation.UOLProperty;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.tools.Tool;

public class DeleteUOLPropertyTool extends Tool {

    protected UOLProperty propertyDeleted;

    protected AdaptationRule parent;

    protected int index;

    public DeleteUOLPropertyTool(AdaptationRule parent, int index) {
        this.parent = parent;
        this.index = index;
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
        if (index >= 0 && index < parent.getUOLProperties().size()) {
            propertyDeleted = parent.getUOLProperties().remove(index);
            return true;
        }
        return false;
    }

    @Override
    public boolean redoTool() {
        parent.getUOLProperties().remove(index);
        Controller.getInstance().updatePanel();
        return true;
    }

    @Override
    public boolean undoTool() {
        parent.getUOLProperties().add(index, propertyDeleted);
        Controller.getInstance().updatePanel();
        return true;
    }
}
