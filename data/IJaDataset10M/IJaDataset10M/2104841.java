package net.sf.vgap4.assistant.figures.draw2d;

import java.util.logging.Logger;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import net.sf.vgap4.assistant.ui.AssistantConstants;

public class ZeroPaddingLabel extends Label {

    private static Logger logger = Logger.getLogger("net.sf.vgap4.assistant.figures.draw2d");

    public ZeroPaddingLabel(final String text) {
        setFont(AssistantConstants.FONT_MAP_DEFAULT);
        setLabelAlignment(PositionConstants.LEFT);
        setText(text);
    }

    @Override
    public void setText(final String newText) {
        super.setText(newText);
    }
}
