package foa.attributes;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Emiliano Grigis
 * @version 0.0.1
 */
public class AttributeSetPopupMenu extends JPopupMenu {

    private Container container;

    private String attributeSet;

    public AttributeSetPopupMenu(Container c, String file, String className, String attributeSet) {
        container = c;
        this.attributeSet = attributeSet;
        JMenuItem menuItem;
        menuItem = add(new NewAttributeSetAction(c, file, className));
        menuItem.setIcon(null);
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.getAccessibleContext().setAccessibleDescription("New Attribute Set");
        menuItem = add(new NewVariantAttributeSetAction(c, file, className, attributeSet));
        menuItem.setIcon(null);
        menuItem.setMnemonic(KeyEvent.VK_V);
        menuItem.getAccessibleContext().setAccessibleDescription("New Variant of Attribute Set");
        menuItem = add(new AddModifyAttributeSetAction(c, file, className, attributeSet));
        menuItem.setIcon(null);
        menuItem.setMnemonic(KeyEvent.VK_A);
        menuItem.getAccessibleContext().setAccessibleDescription("Add/Modify Attribute Set");
        menuItem = add(new DeleteAttributeSetAction(c, className, attributeSet));
        menuItem.setIcon(null);
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.getAccessibleContext().setAccessibleDescription("Delete Attribute Set");
    }
}
