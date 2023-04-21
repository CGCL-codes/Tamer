package preprocessing.methods.Import.databasedata.gui.controllers.actions;

import preprocessing.methods.Import.databasedata.database.Column;
import preprocessing.methods.Import.databasedata.database.Table;
import preprocessing.methods.Import.databasedata.facade.Facade;
import preprocessing.methods.Import.databasedata.gui.components.trees.SelectableNode;
import java.awt.event.ActionEvent;
import java.util.Collection;
import javax.swing.AbstractAction;
import preprocessing.methods.Import.databasedata.gui.components.icons.DatabaseDataIcon;
import preprocessing.methods.Import.databasedata.gui.components.trees.SelectableTree;

/**
 * This class represents action which selects node as an input.
 *
 * @author Jiri Petnik
 */
public class SelectNodeInputTreeAction extends AbstractAction {

    private SelectableTree tree;

    /**
     * Creates a new instance.
     *
     * @param tree selectable tree
     */
    public SelectNodeInputTreeAction(SelectableTree tree) {
        super("Select Input");
        this.tree = tree;
        String imageName = "inputIcon.png";
        String desc = "Select node you want to be in SQL query.";
        putValue(SHORT_DESCRIPTION, desc);
        putValue(SMALL_ICON, DatabaseDataIcon.assignIcon(imageName, desc));
    }

    public void actionPerformed(ActionEvent e) {
        SelectableNode selectedNode = tree.getSelectedNode();
        if (selectedNode == null) return;
        Object o = selectedNode.getUserObject();
        if (o instanceof Column) {
            Column col = (Column) o;
            if (col.isConvertable()) {
                Facade.getInstance().setInputAttributeIWS(col);
                Facade.getInstance().selectColumnIWS(col);
            }
        }
        if (o instanceof Table) {
            Table tab = (Table) o;
            Collection<Column> cols = tab.getAllColumns();
            for (Column c : cols) {
                if (c.isConvertable()) {
                    Facade.getInstance().setInputAttributeIWS(c);
                    Facade.getInstance().selectColumnIWS(c);
                }
            }
        }
        selectedNode.setInput();
        selectedNode.setSelected(true);
        tree.repaint();
    }
}
