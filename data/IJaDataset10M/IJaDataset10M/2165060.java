package org.hironico.dbtool2.dbexplorer;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.hironico.database.SQLTable;
import org.hironico.database.SQLProcedure;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

/**
 * Permet de supprimer en base un objet sélectionné par le db explorer.
 * @author hironico
 * @since 2.0.0
 */
public class DropDbObjectAction extends AbstractAction {

    protected static final Logger logger = Logger.getLogger("org.hironico.dbtool2.dbexplorer");

    protected JXTreeTable dbExplorerTreeTable = null;

    public DropDbObjectAction(JXTreeTable dbExplorerTreeTable) {
        this.dbExplorerTreeTable = dbExplorerTreeTable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (dbExplorerTreeTable == null) {
            logger.error("Cannot drop db objets from a null tree table explorer !");
            return;
        }
        int[] rows = dbExplorerTreeTable.getSelectedRows();
        if (rows.length == 0) return;
        int confirm = JOptionPane.showConfirmDialog(dbExplorerTreeTable.getParent(), "Are you sure you want to drop the selected objets ?\n" + "(" + rows.length + " objets are selected)", "Please confirm...", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        List<String> errors = new ArrayList<String>();
        List<TreePath> nodesToRefresh = new ArrayList<TreePath>();
        for (int row : rows) {
            TreePath tp = dbExplorerTreeTable.getPathForRow(row);
            DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) tp.getLastPathComponent();
            Object obj = node.getUserObject();
            if (obj instanceof SQLTable) {
                SQLTable table = (SQLTable) obj;
                if (!table.drop()) errors.add(table.getName());
                if (!nodesToRefresh.contains(tp.getParentPath())) nodesToRefresh.add(tp.getParentPath());
            } else if (obj instanceof SQLProcedure) {
                SQLProcedure proc = (SQLProcedure) obj;
                if (!proc.drop()) errors.add(proc.getName());
                if (!nodesToRefresh.contains(tp.getParentPath())) nodesToRefresh.add(tp.getParentPath());
            }
        }
        if (errors.size() > 0) {
            String errorMsg = "";
            for (int cpt = 0; cpt < errors.size(); cpt++) {
                if (cpt >= 10) break;
                errorMsg += errors.get(cpt) + "\n";
            }
            if (errors.size() >= 10) errorMsg += "...";
            errorMsg = "The following objects could not be dropped:\n" + errorMsg;
            JOptionPane.showMessageDialog(dbExplorerTreeTable, errorMsg, "Ohoh...", JOptionPane.ERROR_MESSAGE);
        }
        TreePath[] tpArray = new TreePath[nodesToRefresh.size()];
        for (int cpt = 0; cpt < nodesToRefresh.size(); cpt++) tpArray[cpt] = nodesToRefresh.get(cpt);
        dbExplorerTreeTable.getTreeSelectionModel().addSelectionPaths(tpArray);
        RefreshDbObjectAction refreshAction = new RefreshDbObjectAction(dbExplorerTreeTable);
        refreshAction.actionPerformed(e);
    }
}
