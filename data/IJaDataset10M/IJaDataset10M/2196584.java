package org.fudaa.fudaa.commun.exec;

import org.fudaa.ctulu.gui.CtuluCellDialogEditor;
import org.fudaa.ctulu.gui.CtuluDialog;

/**
 * @author deniger
 * @version $Id: FudaaExecCellEditor.java,v 1.10 2006-09-19 15:01:53 deniger Exp $
 */
public class FudaaExecCellEditor extends CtuluCellDialogEditor {

    public FudaaExecCellEditor() {
        super(new CtuluDialog(new FudaaExecPanel()));
    }

    public void setValue(final Object _o) {
        final FudaaExec e = (FudaaExec) _o;
        setText(e.getViewedName());
        setIcon(e.getIcon());
    }
}
