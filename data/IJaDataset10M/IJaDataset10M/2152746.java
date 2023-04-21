package net.sourceforge.powerswing.panel;

import java.awt.Component;
import javax.swing.JSplitPane;

public class PLeftRightSplitPane extends JSplitPane {

    public PLeftRightSplitPane(int theDividerLocation, Component theLeftComponent, Component theRightComponent) {
        super();
        super.setDividerLocation(theDividerLocation);
        super.setLeftComponent(theLeftComponent);
        super.setRightComponent(theRightComponent);
    }
}
