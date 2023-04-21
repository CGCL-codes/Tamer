package org.geoforge.guitlcolg.dialog.edit.data.opr;

import org.geoforge.guitlcolg.dialog.edit.data.opr.panel.PnlContentsOkTextNewSettingsLineOpenPipeline;
import org.geoforge.guitlc.dialog.edit.data.GfrDlgNewSettingsLineOpenAbs;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelListener;
import org.geoforge.guitlc.dialog.edit.GfrDlgOkNewTextAbs;
import org.geoforge.guitlc.dialog.edit.panel.PnlSelEditTblAbs;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class GfrDlgNewSettingsLineOpenPathPipeline extends GfrDlgNewSettingsLineOpenAbs {

    private static final String _STR_WHAT = "Path";

    private static final int _F_INT_H_ = 140 + PnlSelEditTblAbs.INT_H;

    public GfrDlgNewSettingsLineOpenPathPipeline(JFrame frmOwner, String[] strsExistingItems) {
        super(frmOwner, GfrDlgNewSettingsLineOpenPathPipeline._STR_WHAT, GfrDlgOkNewTextAbs.INT_DIM_W, GfrDlgNewSettingsLineOpenPathPipeline._F_INT_H_, strsExistingItems);
        super._pnlContents = new PnlContentsOkTextNewSettingsLineOpenPipeline(GfrDlgNewSettingsLineOpenPathPipeline._STR_WHAT, (DocumentListener) this, (TableModelListener) this, (MouseListener) this, (java.awt.event.KeyListener) this, strsExistingItems);
    }
}
