package org.zkoss.zkdemo.userguide;

import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;

/**
 * The "Try me!" button.
 * 
 * @author tomyeh
 */
public class TryMeButton extends Button {

    public void onClick() {
        ((Groupbox) getFellow("tryView")).setOpen(true);
        ((CodeView) getFellow("codeView")).execute();
    }
}
