package net.sf.vgap4.assistant.actions;

import org.eclipse.gef.commands.Command;
import net.sf.vgap4.assistant.models.AssistantMap;

public class ZoomPlusDataCommand extends Command {

    private final AssistantMap mapModel;

    public ZoomPlusDataCommand(final String label, final AssistantMap assistantMap) {
        super(label);
        mapModel = assistantMap;
    }

    @Override
    public boolean canExecute() {
        return true;
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    /** Change the zoom factor of the underlying Map model and recalculate the figures location */
    @Override
    public void execute() {
        mapModel.zoomPlus();
    }

    @Override
    public void undo() {
        mapModel.zoomMinus();
    }
}
