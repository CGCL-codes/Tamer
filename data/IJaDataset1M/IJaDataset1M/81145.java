package au.edu.qut.yawl.editor.actions.palette;

import au.edu.qut.yawl.editor.swing.TooltipTogglingWidget;
import au.edu.qut.yawl.editor.swing.menu.Palette;
import javax.swing.Action;

public class MarqueeAction extends YAWLPaletteAction implements TooltipTogglingWidget {

    {
        putValue(Action.SHORT_DESCRIPTION, getDisabledTooltipText());
        putValue(Action.NAME, "Marquee");
        putValue(Action.LONG_DESCRIPTION, "Net Element Selection Mode");
        putValue(Action.SMALL_ICON, getPaletteIconByName("PaletteMarquee"));
        setIdentifier(Palette.MARQUEE);
    }

    public String getEnabledTooltipText() {
        return " Net Element Selection Mode ";
    }

    public String getDisabledTooltipText() {
        return " You must have an open specification, and selected net to use the palette ";
    }
}
