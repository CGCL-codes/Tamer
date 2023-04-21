package com.memoire.dt;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.text.JTextComponent;
import com.memoire.fu.FuLog;

public final class DtStringFieldHandler extends TransferHandler {

    public static final int INSERT = 0;

    public static final int LINESTART = 1;

    public static final int APPEND = 2;

    public static final int REPLACE = 3;

    private boolean single_, convert_;

    private int mode_;

    public DtStringFieldHandler(boolean _single, int _mode, boolean _convert) {
        single_ = _single;
        mode_ = _mode;
        convert_ = _convert;
    }

    public boolean canImport(JComponent _c, DataFlavor[] _flavors) {
        if (!(_c instanceof JTextComponent)) return false;
        return DtStringsSelection.canConvert(_flavors);
    }

    public boolean importData(JComponent _c, Transferable _t) {
        if (!canImport(_c, _t.getTransferDataFlavors())) return false;
        DtStringsSelection s = DtStringsSelection.convert(_t);
        if (s == null) return false;
        FuLog.debug("DFS: importData");
        String r = s.getWholeString();
        if (single_) {
            int p = r.indexOf('\n');
            if (p >= 0) r = r.substring(0, p);
        }
        if (convert_) {
            r = r.replace('\t', '=');
        }
        if (!"".equals(r)) {
            String t = ((JTextComponent) _c).getText();
            int p = ((JTextComponent) _c).getCaretPosition();
            if (mode_ == LINESTART) while ((p > 0) && (t.charAt(p) != '\n')) p--;
            switch(mode_) {
                case LINESTART:
                    if (!r.endsWith("\n")) r += "\n";
                case INSERT:
                    t = t.substring(0, p) + r + t.substring(p);
                    break;
                case APPEND:
                    if (!"".equals(t) && !t.endsWith("\n")) t += "\n";
                    t = t + r;
                    break;
                case REPLACE:
                    t = r;
            }
            ((JTextComponent) _c).setText(t);
            return true;
        }
        return false;
    }
}
