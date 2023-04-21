package mvt.tools.numerical;

import mvt.tools.*;

public class FindRootTool extends ToolPanel {

    public FindRootTool(String name) {
        super(name, new ToolPanel[] { new Root1DTool("One Dimension"), new Root2DTool("Two Dimensions"), new Root3DTool("Three Dimensions") });
    }
}
