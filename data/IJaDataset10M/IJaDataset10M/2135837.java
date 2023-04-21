package org.geoforge.guitlc.dialog.edit.data;

import org.geoforge.guitlc.dialog.edit.data.panel.PnlContentsOkTextNewSettingsLineOpenAbs;
import org.geoforge.guitlc.dialog.edit.GfrDlgNewSettingsAbs;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.geoforge.guillc.dialog.panel.GfrPnlCmdCancelOk;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public abstract class GfrDlgNewSettingsLineOpenAbs extends GfrDlgNewSettingsAbs implements TableModelListener, MouseListener {

    public ArrayList<Point2D.Double> getValueLine() {
        return ((PnlContentsOkTextNewSettingsLineOpenAbs) super._pnlContents).getValueLine();
    }

    protected GfrDlgNewSettingsLineOpenAbs(JFrame frmOwner, String strWhat, int intW, int intH, String[] strsExistingItems) {
        super(frmOwner, strWhat, intW, intH, strsExistingItems);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        super._updateFromSettings_();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!(e.getSource() instanceof JTable)) {
            String str = "! (e.getSource() instanceof JTable)";
            System.err.println(str);
            System.exit(1);
        }
        JTable tbl = (JTable) e.getSource();
        int row = tbl.getSelectedRow();
        int column = tbl.getSelectedColumn();
        if (row == -1 || column == -1) return;
        if (!tbl.getModel().isCellEditable(row, column)) {
            super._updateFromSettings_();
            return;
        }
        ((GfrPnlCmdCancelOk) super._pnlCommands).setEnabledOk(false);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
