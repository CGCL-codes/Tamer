package org.maveryx.jRobot.guiObject;

import java.awt.Component;
import javax.accessibility.AccessibleState;
import org.jdom.Element;
import abbot.tester.DialogTester;

public class AUTDialog extends AUTObject {

    private DialogTester dialog = new DialogTester();

    public Object close(Component c, Element e) {
        Object obj = null;
        dialog.actionClose(c);
        return obj;
    }

    public Boolean isModal(Component c, Element e) {
        return c.getAccessibleContext().getAccessibleStateSet().contains(AccessibleState.MODAL);
    }
}
