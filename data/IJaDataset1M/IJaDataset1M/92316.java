package ise.plugin.svn.gui.br;

import java.awt.event.ActionEvent;
import javax.swing.tree.TreePath;
import ise.plugin.svn.action.*;
import ise.plugin.svn.gui.*;
import ise.plugin.svn.data.*;
import java.util.*;

public class Unlock extends BRAction {

    public void actionPerformed(ActionEvent ae) {
        TreePath[] tree_paths = tree.getSelectionPaths();
        if (tree_paths.length == 0) {
            return;
        }
        List<String> paths = new ArrayList<String>();
        for (TreePath path : tree_paths) {
            if (path != null) {
                Object[] parts = path.getPath();
                StringBuilder sb = new StringBuilder();
                sb.append(parts[0]);
                for (int i = 1; i < parts.length; i++) {
                    sb.append("/").append(parts[i].toString());
                }
                String url = sb.toString();
                paths.add(url);
            }
        }
        UnlockAction action = new UnlockAction(view, paths, username, password, true);
        action.actionPerformed(ae);
    }
}
