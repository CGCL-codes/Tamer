package org.zkoss.zktest.test2.tree;

import org.zkoss.zul.Label;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

/**
 * @author Dennis.Chen
 *
 */
public class HostIconTreeRenderer implements TreeitemRenderer {

    public void render(Treeitem treeitem, Object data) throws Exception {
        Treerow row;
        if (treeitem.getTreerow() == null) {
            row = new Treerow();
            row.setParent(treeitem);
        } else {
            row = treeitem.getTreerow();
            row.getChildren().clear();
        }
        if (data instanceof HostTreeModel.FakeRoot) {
            treeitem.getTreerow().appendChild(new Treecell(((HostTreeModel.FakeRoot) data).getName()));
        } else if (data instanceof HostTreeModel.FakeGroup) {
            treeitem.getTreerow().appendChild(new Treecell(((HostTreeModel.FakeGroup) data).getName()));
        } else if (data instanceof HostTreeModel.FakeHost) {
            HostTreeModel.FakeHost host = ((HostTreeModel.FakeHost) data);
            Treecell cell = new Treecell(host.getName());
            cell.setId(host.getId());
            treeitem.getTreerow().appendChild(cell);
        } else if (data instanceof HostTreeModel.FakeProcess) {
            HostTreeModel.FakeProcess process = ((HostTreeModel.FakeProcess) data);
            Treecell cell = new Treecell();
            Label fakelabel = new Label();
            fakelabel.setValue(process.getName());
            fakelabel.setContext("editPopup");
            fakelabel.setParent(cell);
            treeitem.getTreerow().appendChild(cell);
        }
    }
}
