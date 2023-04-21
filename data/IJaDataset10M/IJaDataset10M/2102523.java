package self.amigo.editor.tool;

import javax.swing.Action;
import javax.swing.Icon;
import self.amigo.elem.RectangleView;
import self.swing.IconUtils;

public class RectangleTool extends ABasicCreationTool {

    public RectangleTool() {
        userInterfaceSink.putValue(Action.NAME, "Rectangle");
        Icon gif = IconUtils.getIconWithDescription("/self/amigo/images/Rectangle.gif", "Rectangle Tool");
        userInterfaceSink.putValue(Action.SMALL_ICON, gif);
    }

    protected void constructElement() {
        view = new RectangleView();
    }
}
