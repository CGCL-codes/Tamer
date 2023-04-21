package org.digitall.lib.components.buttons;

import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.icons.IconTypes;

public class ModifyButton extends BasicButton {

    public ModifyButton() {
        setIcon(IconTypes.modify_16x16);
        setMnemonic('m');
        setToolTipText("Modify data");
        setSize(40, 25);
        setIcon(IconTypes.modify_16x16);
        setRolloverIcon(IconTypes.modify_ro_16x16);
        setDisabledIcon(IconTypes.modify_ne_16x16);
        setPressedIcon(IconTypes.modify_pr_16x16);
    }
}
