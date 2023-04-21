package com.carsongee.jsshmacro;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JTable;

/**
 * Empty container class (for file organization, contains two AbstractAction 
 * classes for cutting and pasting HostTable entries.
 * @author Carson Gee
 */
public class JHostTable {
}

class JHostTableCut extends AbstractAction {

    HostTableModel htm;

    JTable hostTable;

    JHostTableCut(JTable hostTable, HostTableModel htm) {
        super(null, null);
        this.htm = htm;
        this.hostTable = hostTable;
    }

    @Override
    public boolean isEnabled() {
        if (hostTable.getSelectedRowCount() > 0) return true;
        return false;
    }

    public void actionPerformed(ActionEvent evt) {
        String cbStr = htm.cut(hostTable.getSelectedRows(), hostTable.getRowSorter());
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection tmp = new StringSelection(cbStr);
        cb.setContents(tmp, tmp);
    }
}

class JHostTablePaste extends AbstractAction {

    HostTableModel htm;

    JTable hostTable;

    JHostTablePaste(JTable hostTable, HostTableModel htm) {
        super(null, null);
        this.hostTable = hostTable;
        this.htm = htm;
    }

    public void actionPerformed(ActionEvent evt) {
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = cb.getContents(null);
        boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                String result = (String) contents.getTransferData(DataFlavor.stringFlavor);
                htm.paste(result, hostTable.getSelectedRow());
            } catch (UnsupportedFlavorException ex) {
                System.out.println(ex);
                ex.printStackTrace();
            } catch (IOException ex) {
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean isEnabled() {
        if (hostTable.getSelectedRowCount() != 1) return false;
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = cb.getContents(null);
        boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (!hasTransferableText) return false;
        return true;
    }
}

class JHostTableDelete extends AbstractAction {

    HostTableModel htm;

    JTable hostTable;

    JHostTableDelete(JTable hostTable, HostTableModel htm) {
        super(null, null);
        this.htm = htm;
        this.hostTable = hostTable;
    }

    @Override
    public boolean isEnabled() {
        if (hostTable.getSelectedRowCount() > 0) return true;
        return false;
    }

    public void actionPerformed(ActionEvent evt) {
        if (hostTable.getSelectedRowCount() < 1) return;
        htm.removeRows(hostTable.getSelectedRows(), hostTable.getRowSorter());
        String cbStr = htm.cut(hostTable.getSelectedRows(), hostTable.getRowSorter());
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection tmp = new StringSelection(cbStr);
        cb.setContents(tmp, tmp);
    }
}
