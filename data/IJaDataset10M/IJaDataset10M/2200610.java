package fiswidgets.fisgui;

import javax.swing.*;
import java.awt.*;

/**
 *  This is a tabbed component that can be placed onto a fis application.
 */
public class FisTabbedComponent extends JTabbedPane {

    public static int LEFT = JTabbedPane.LEFT;

    public static int RIGHT = JTabbedPane.RIGHT;

    public static int TOP = JTabbedPane.TOP;

    public static int BOTTOM = JTabbedPane.BOTTOM;

    /**
     * Constructor for a FisTabbedComponent that takes a frame
     * @param frame is the FisFrame that this tabbed component is to be added to
     */
    public FisTabbedComponent(FisFrame frame) {
        super();
        FisComponent.addToFrame(this, frame);
    }

    /**
     * Constructor that takes a placement and a frame to be added to
     * @param tabPlacement is the placement of the tabs on the pane.  They can be
     * FisTabbedComponent.TOP, FisTabbedComponent.BOTTOM, FisTabbedComponent.LEFT, FisTabbedComponent.RIGHT
     * @param frame is the FisFrame that this tabbed component is to be added to
     */
    public FisTabbedComponent(int tabPlacement, FisFrame frame) {
        super(tabPlacement);
        FisComponent.addToFrame(this, frame);
    }
}
