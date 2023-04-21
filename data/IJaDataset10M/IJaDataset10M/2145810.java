package org.yawlfoundation.yawl.editor.actions.palette;

import javax.swing.Action;
import org.yawlfoundation.yawl.editor.swing.TooltipTogglingWidget;
import org.yawlfoundation.yawl.editor.swing.menu.ControlFlowPalette;

public class ConditionAction extends ControlFlowPaletteAction implements TooltipTogglingWidget {

    private static final long serialVersionUID = 1L;

    {
        putValue(Action.SHORT_DESCRIPTION, getDisabledTooltipText());
        putValue(Action.NAME, "Condition");
        putValue(Action.LONG_DESCRIPTION, "Add a new Condition");
        putValue(Action.SMALL_ICON, getPaletteIconByName("PaletteCondition"));
    }

    public ConditionAction(ControlFlowPalette palette) {
        super(palette);
    }

    public String getEnabledTooltipText() {
        return " Add a Condition ";
    }

    public String getDisabledTooltipText() {
        return " You must have an open specification, and selected net to use the palette ";
    }

    public String getButtonStatusText() {
        return getClickAnywhereText() + "condition.";
    }

    public ControlFlowPalette.SelectionState getSelectionID() {
        return ControlFlowPalette.SelectionState.CONDITION;
    }
}
