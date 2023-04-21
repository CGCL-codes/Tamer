package org.eclipse.gef.examples.logicdesigner.model;

import org.eclipse.swt.graphics.Image;
import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class GroundOutput extends SimpleOutput {

    private static Image GROUND_ICON = createImage(BuddyClass.class, "icons/ground16.gif");

    static final long serialVersionUID = 1;

    public Image getIconImage() {
        return GROUND_ICON;
    }

    public boolean getResult() {
        return false;
    }

    public String toString() {
        return LogicMessages.GroundOutput_LabelText;
    }
}
