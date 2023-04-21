package com.chimshaw.jblogeditor.editors.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import com.chimshaw.jblogeditor.util.UIUtil;

public class ColorActionDelegate extends GenericFormatActionDelegate {

    @Override
    public void run(IAction action) {
        if (targetEditor == null) {
            return;
        }
        ColorDialog dlg = new ColorDialog(getHTMLEditor().getSite().getShell());
        RGB color = dlg.open();
        if (color == null) {
            return;
        }
        snippet = UIUtil.colorAsHexString(color);
        super.run(action);
    }
}
