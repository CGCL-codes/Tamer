package org.akrogen.tkui.ui.swt.controls.menus;

import org.akrogen.tkui.core.ui.elements.menus.IUIMenuPopup;
import org.eclipse.swt.SWT;

/**
 * Menu Popup implemented with SWT Menu.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 */
public class SWTMenuPopupImpl extends SWTMenuImpl implements IUIMenuPopup {

    protected int getMenuStyle() {
        return SWT.POP_UP;
    }
}
