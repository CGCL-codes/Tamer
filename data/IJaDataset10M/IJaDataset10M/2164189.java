package com.potix.zul.html;

import java.io.IOException;
import com.potix.zk.ui.Component;
import com.potix.zk.ui.UiException;
import com.potix.zk.ui.WrongValueException;
import com.potix.zul.html.impl.XulElement;

/**
 * A container for {@link Popup} elements.
 * You should declare all popup elements as children of a popupset.
 * This element does not directly display on screen.
 * Child popups will be displayed when asked to by other elements.
 * 
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 * @version $Revision: 1.3 $ $Date: 2006/02/27 03:55:14 $
 */
public class Popupset extends XulElement {

    public Popupset() {
        super.setVisible(false);
    }

    /** Not allowd. */
    public boolean setVisible(boolean visible) {
        throw new UnsupportedOperationException("You cannot make it visible manually");
    }

    public boolean insertBefore(Component child, Component insertBefore) {
        if (!(child instanceof Menupopup)) throw new UiException("Unsupported child for popupset: " + child);
        return super.insertBefore(child, insertBefore);
    }
}
