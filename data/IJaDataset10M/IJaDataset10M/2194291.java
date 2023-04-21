package com.jgoodies.looks.windows;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.plaf.ComponentUI;
import com.jgoodies.looks.common.ExtBasicMenuItemUI;
import com.jgoodies.looks.common.MenuItemRenderer;

/**
 * The JGoodies Windows look&amp;feel implementation of <code>MenuItemUI</code>.<p>
 *
 * It differs from the superclass in that it uses a Windows specific
 * menu item renderer that checks if mnemonics shall be shown or hidden
 * and may paint disabled text with a shadow.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.4 $
 */
public final class WindowsMenuItemUI extends ExtBasicMenuItemUI {

    public static ComponentUI createUI(JComponent b) {
        return new WindowsMenuItemUI();
    }

    protected MenuItemRenderer createRenderer(JMenuItem menuItem, boolean iconBorderEnabled, Font acceleratorFont, Color selectionForeground, Color disabledForeground, Color acceleratorForeground, Color acceleratorSelectionForeground) {
        return new WindowsMenuItemRenderer(menuItem, iconBorderEnabled(), acceleratorFont, selectionForeground, disabledForeground, acceleratorForeground, acceleratorSelectionForeground);
    }
}
