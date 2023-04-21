package org.geoforge.guitlc.dialog.edit.space;

import java.awt.event.MouseListener;
import org.geoforge.guitlc.dialog.edit.GfrDlgOkOpenTextAbs;
import javax.swing.JFrame;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import org.geoforge.guitlc.dialog.edit.panel.PnlContentsOkTextSelect;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class GfrDlgOpenSpaceProject extends GfrDlgOkOpenTextAbs {

    private static final String _STR_WHAT_ = "Project";

    public GfrDlgOpenSpaceProject(JFrame frmOwner, String[] strsExistingItems) {
        super(frmOwner, _STR_WHAT_, strsExistingItems);
        super._pnlContents = new PnlContentsOkTextSelect((DocumentListener) this, _STR_WHAT_, _STR_WHAT_ + "s", strsExistingItems, (ListSelectionListener) this, (MouseListener) this);
        String strTitle = System.getProperty("_geoforge.appli.title");
        strTitle += " - " + "Open " + _STR_WHAT_.toLowerCase();
        super.setTitle(strTitle);
    }
}
