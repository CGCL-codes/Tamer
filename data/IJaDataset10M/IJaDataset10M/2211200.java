package org.akrogen.tkui.ui.swing.controls.menus;

import java.awt.Container;
import javax.swing.JPopupMenu;
import org.akrogen.tkui.core.ui.elements.IUIElementInfo;
import org.akrogen.tkui.core.ui.elements.menus.IUIMenuSeparator;
import org.akrogen.tkui.ui.swing.AbstractSwingElementImpl;

/**
 * GUI Menu Item implemented with Swing MenuItem.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 */
public class SwingMenuSeparatorImpl extends AbstractSwingElementImpl implements IUIMenuSeparator {

    protected Container buildContainer(Container parent, IUIElementInfo info) {
        JPopupMenu popupMenu = (JPopupMenu) parent;
        return new JPopupMenu.Separator();
    }
}
