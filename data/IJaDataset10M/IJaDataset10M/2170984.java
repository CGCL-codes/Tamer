package au.edu.qut.yawl.editor.actions.palette;

import javax.swing.Action;
import au.edu.qut.yawl.editor.swing.TooltipTogglingWidget;
import au.edu.qut.yawl.editor.swing.menu.Palette;

public class CompositeTaskAction extends YAWLPaletteAction implements TooltipTogglingWidget {

    {
        putValue(Action.SHORT_DESCRIPTION, getDisabledTooltipText());
        putValue(Action.NAME, "Composite Task");
        putValue(Action.LONG_DESCRIPTION, "Add a new Composite Task");
        putValue(Action.SMALL_ICON, getPaletteIconByName("PaletteCompositeTask"));
        setIdentifier(Palette.COMPOSITE_TASK);
    }

    public String getEnabledTooltipText() {
        return " Add a new Composite Task ";
    }

    public String getDisabledTooltipText() {
        return " You must have an open specification, and selected net to use the palette ";
    }
}
