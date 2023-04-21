package JFlex.gui;

import java.awt.Component;

/**
 * Constraints for layout elements of GridLayout
 *
 * @author Gerwin Klein
 * @version $Revision: 1.4.3 $, $Date: 2009/12/21 15:58:48 $
 */
public class GridPanelConstraint {

    int x, y, width, height, handle;

    Component component;

    public GridPanelConstraint(int x, int y, int width, int height, int handle, Component component) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.handle = handle;
        this.component = component;
    }
}
