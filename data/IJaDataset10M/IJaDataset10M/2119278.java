package org.maveryx.jRobot.guiObject;

import java.awt.Component;
import java.util.Locale;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import org.jdom.Element;
import abbot.tester.JMenuItemTester;

public class AUTMenuItem extends AUTObject {

    JMenuItemTester menuItem = new JMenuItemTester();

    public Object click(Component c, Element e) {
        Object obj = null;
        menuItem.selectMenuItem(c);
        return obj;
    }

    public String[] getSubMenu(Component c, Element e) {
        String[] out = new String[4];
        for (int i = 0; i < 4; i++) {
            out[i] = null;
        }
        Element parent = e;
        String childName = null;
        AccessibleRole childRole = null;
        AccessibleContext menu = c.getAccessibleContext().getAccessibleParent().getAccessibleContext();
        AccessibleContext menuItem = c.getAccessibleContext();
        Accessible child = null;
        int count = menu.getAccessibleChildrenCount();
        int j = 0;
        do {
            child = menu.getAccessibleChild(j);
            childName = child.getAccessibleContext().getAccessibleName();
            childRole = child.getAccessibleContext().getAccessibleRole();
            j++;
            if (childName == null) childName = "null";
        } while ((!childName.equals(menuItem.getAccessibleName()) && childRole.equals(AccessibleRole.MENU)) || j < count);
        if (childName.equals(menuItem.getAccessibleName()) && childRole.equals(AccessibleRole.MENU)) {
            out[0] = childName;
            out[1] = childRole.toDisplayString(Locale.UK);
            out[2] = parent.getAttributeValue("accessibleName");
            out[3] = parent.getAttributeValue("accessibleRole");
        } else {
            out = null;
        }
        return out;
    }
}
