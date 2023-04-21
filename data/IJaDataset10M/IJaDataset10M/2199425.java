package ise.plugin.svn.gui.br;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import ise.plugin.svn.action.*;
import ise.plugin.svn.gui.*;
import ise.plugin.svn.data.*;
import ise.plugin.svn.library.GUIUtils;
import java.util.*;

public class Branch extends BRAction {

    public void actionPerformed(ActionEvent ae) {
        TreePath[] tree_paths = tree.getSelectionPaths();
        if (tree_paths.length == 0) {
            return;
        }
        if (tree_paths.length > 1) {
            JOptionPane.showMessageDialog(view, "Please select a single entry.", "Too many selections", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String from_url = null;
        String defaultDestination = null;
        for (TreePath path : tree_paths) {
            if (path != null) {
                Object[] parts = path.getPath();
                StringBuilder from = new StringBuilder();
                StringBuilder to = new StringBuilder();
                String preface = parts[0].toString();
                if (preface.endsWith("/")) {
                    preface = preface.substring(0, preface.length() - 1);
                }
                from.append(preface);
                to.append(preface);
                for (int i = 1; i < parts.length; i++) {
                    from.append("/").append(parts[i].toString());
                }
                for (int i = 1; i < parts.length - 1; i++) {
                    to.append("/").append(parts[i].toString());
                }
                from_url = from.toString();
                defaultDestination = to.append("/branches").toString();
                break;
            }
        }
        TagBranchDialog dialog = new TagBranchDialog(view, TagBranchDialog.BRANCH_DIALOG, from_url, defaultDestination);
        GUIUtils.center(view, dialog);
        dialog.setVisible(true);
        CopyData cd = dialog.getData();
        if (cd != null) {
            if (username != null && password != null) {
                cd.setUsername(username);
                cd.setPassword(password);
            }
            cd.setTitle("Branch");
            CopyAction action = new CopyAction(view, cd);
            action.actionPerformed(null);
        }
    }
}
