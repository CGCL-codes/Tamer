package edu.gatech.ealf.secretservantplaf;

import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TreeUI;
import javax.swing.tree.TreePath;

public class SSTreeUI extends TreeUI {

    private static final String UI_CLASS_ID = "TreeUI";

    public SSTreeUI() {
        ;
    }

    public static ComponentUI createUI(JComponent c) {
        return new SSTreeUI();
    }

    @Override
    public void installUI(JComponent c) {
        SecretServantLookAndFeel.getInstance().registerComponentInstallation(UI_CLASS_ID, c);
    }

    @Override
    public void uninstallUI(JComponent c) {
        SecretServantLookAndFeel.getInstance().registerComponentUninstallation(UI_CLASS_ID, c);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
    }

    @Override
    public void update(Graphics g, JComponent c) {
    }

    @Override
    public void cancelEditing(JTree arg0) {
    }

    @Override
    public TreePath getClosestPathForLocation(JTree arg0, int arg1, int arg2) {
        return null;
    }

    @Override
    public TreePath getEditingPath(JTree arg0) {
        return null;
    }

    @Override
    public Rectangle getPathBounds(JTree arg0, TreePath arg1) {
        return null;
    }

    @Override
    public TreePath getPathForRow(JTree arg0, int arg1) {
        return null;
    }

    @Override
    public int getRowCount(JTree arg0) {
        return 0;
    }

    @Override
    public int getRowForPath(JTree arg0, TreePath arg1) {
        return 0;
    }

    @Override
    public boolean isEditing(JTree arg0) {
        return false;
    }

    @Override
    public void startEditingAtPath(JTree arg0, TreePath arg1) {
    }

    @Override
    public boolean stopEditing(JTree arg0) {
        return false;
    }
}
