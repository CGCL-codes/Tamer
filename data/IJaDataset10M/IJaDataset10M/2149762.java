package org.digitall.lib.components.buttons;

import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.icons.IconTypes;

public class ReloadButton extends BasicButton {

    public ReloadButton() {
        setIcon(IconTypes.reload_16x16);
        setToolTipText(Environment.lang.getProperty("ReloadButton"));
        setSize(40, 25);
        setIcon(IconTypes.reload_16x16);
        setRolloverIcon(IconTypes.reload_ro_16x16);
        setDisabledIcon(IconTypes.reload_ne_16x16);
        setPressedIcon(IconTypes.reload_pr_16x16);
    }
}
