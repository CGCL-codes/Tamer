package Ihm;

import Mediatheque.*;
import javax.swing.TransferHandler;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class transferH extends TransferHandler {

    public boolean importData(JComponent comp, Transferable t) {
        JTree destination = (JTree) comp;
        String nomPlayList = new String();
        TreePath tp = destination.getSelectionPath();
        String base = destination.getSelectionPath().getLastPathComponent().toString();
        if (base.endsWith(".mp3")) {
            base = destination.getSelectionPath().getParentPath().getLastPathComponent().toString();
        }
        try {
            playlist p = new playlist();
            p.load(base);
            morceau m = new morceau(t.getTransferData(DataFlavor.stringFlavor).toString());
            p.ajout(m);
        } catch (Exception e) {
        }
        try {
            ((DefaultTreeModel) destination.getModel()).setRoot(new FileBddModel());
            destination.setSelectionPath(tp);
        } catch (Exception e) {
        }
        return true;
    }

    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        return true;
    }
}
