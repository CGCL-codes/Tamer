package org.geoforge.guitlcecl.dialog.edit.data;

import java.awt.event.MouseListener;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelListener;
import org.geoforge.guitlcecl.dialog.edit.data.panel.PnlContentsOkTextNewSettingsTlcOlgEclPath;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class GfrDlgNewSettingsTloOlgEclEditPath extends GfrDlgNewSettingsTloOlgEclEditAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(GfrDlgNewSettingsTloOlgEclEditPath.class.getName());

    static {
        GfrDlgNewSettingsTloOlgEclEditPath._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    private static final String _STR_WHAT = "Path";

    public GfrDlgNewSettingsTloOlgEclEditPath(JFrame frmOwner, String[] strsExistingItems) {
        super(frmOwner, GfrDlgNewSettingsTloOlgEclEditPath._STR_WHAT, strsExistingItems);
        super._pnlContents = new PnlContentsOkTextNewSettingsTlcOlgEclPath(GfrDlgNewSettingsTloOlgEclEditPath._STR_WHAT, (DocumentListener) this, (TableModelListener) this, (MouseListener) this, (java.awt.event.KeyListener) this, strsExistingItems);
    }
}
