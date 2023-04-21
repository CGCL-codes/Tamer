package com.ezware.dialog.task.design;

import javax.swing.JCheckBox;
import javax.swing.UIManager;
import com.ezware.dialog.task.IContentDesign;

class DetailsToggleButton extends JCheckBox {

    private static final long serialVersionUID = 1L;

    public DetailsToggleButton() {
        super();
        setIcon(UIManager.getIcon(IContentDesign.ICON_MORE_DETAILS));
        setSelectedIcon(UIManager.getIcon(IContentDesign.ICON_FEWER_DETAILS));
    }
}
