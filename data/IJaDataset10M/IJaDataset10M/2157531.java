package org.maveryx.jRobot.guiObject;

import java.awt.Component;
import javax.accessibility.AccessibleState;
import org.jdom.Element;
import abbot.tester.JButtonTester;

public class AUTToggleButton extends AUTObject {

    private JButtonTester toggleButton = new JButtonTester();

    public Object click(Component c, Element e) {
        Object object = null;
        toggleButton.actionClick(c);
        return object;
    }

    public Boolean isDown(Component c, Element e) {
        return c.getAccessibleContext().getAccessibleStateSet().contains(AccessibleState.CHECKED);
    }
}
