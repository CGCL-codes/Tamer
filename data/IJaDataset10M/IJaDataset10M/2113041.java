package com.chimshaw.jblogeditor.editors.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import com.chimshaw.jblogeditor.dialogs.InsertFontDialog;

public class FontActionDelegate extends GenericFormatActionDelegate {

    @Override
    public void run(IAction action) {
        if (targetEditor == null) {
            return;
        }
        InsertFontDialog dlg = new InsertFontDialog(targetEditor.getSite().getShell());
        if (dlg.open() != Dialog.OK) {
            return;
        }
        String face = dlg.getFace();
        String size = dlg.getSize();
        String color = dlg.getColor();
        StringBuffer sb = new StringBuffer();
        sb.append("<font");
        if (face != null) {
            sb.append(" face=\"");
            sb.append(face);
            sb.append("\"");
        }
        if (size != null) {
            sb.append(" size=\"");
            sb.append(size);
            sb.append("\"");
        }
        if (color != null) {
            sb.append(" color=\"");
            sb.append(color);
            sb.append("\"");
        }
        sb.append(">%s</font>");
        snippet = sb.toString();
        super.run(action);
    }
}
