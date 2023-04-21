package au.edu.qut.yawl.editor.actions.palette;

import javax.swing.Action;
import au.edu.qut.yawl.editor.swing.TooltipTogglingWidget;
import au.edu.qut.yawl.editor.swing.menu.Palette;

public class FlowRelationAction extends YAWLPaletteAction implements TooltipTogglingWidget {

    {
        putValue(Action.SHORT_DESCRIPTION, getDisabledTooltipText());
        putValue(Action.NAME, "Flow Relation");
        putValue(Action.LONG_DESCRIPTION, "Add a new Flow Relation");
        putValue(Action.SMALL_ICON, getPaletteIconByName("PaletteFlowRelation"));
        setIdentifier(Palette.FLOW_RELATION);
    }

    public String getEnabledTooltipText() {
        return " Add a Flow Relation ";
    }

    public String getDisabledTooltipText() {
        return " You must have an open specification, and selected net to use the palette ";
    }
}
