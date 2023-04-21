package net.itsite.os.catalog;

import java.util.ArrayList;
import java.util.Collection;
import net.itsite.os.OSCatalog;
import net.itsite.os.OSUtils;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeHandle;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;
import net.simpleframework.web.page.component.ui.tree.TreeBean;
import net.simpleframework.web.page.component.ui.tree.TreeNode;

/**
 * 显示查询的树结构
 */
public class OSCatalogAddTreeHandle extends AbstractTreeHandle {

    @Override
    public Collection<? extends AbstractTreeNode> getTreenodes(final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
        final IQueryEntitySet<OSCatalog> catalogs = OSUtils.applicationModule.queryCatalogs(compParameter, treeNode == null ? null : (ITreeBeanAware) treeNode.getDataObject());
        if (catalogs != null) {
            final Collection<TreeNode> nodes = new ArrayList<TreeNode>();
            OSCatalog os = null;
            while ((os = catalogs.next()) != null) {
                final TreeNode treeNode2 = new TreeNode((TreeBean) compParameter.componentBean, treeNode, os);
                treeNode2.setText(os.getText());
                if (treeNode == null) {
                    treeNode2.setJsDblclickCallback("return false;");
                }
                nodes.add(treeNode2);
            }
            return nodes;
        } else {
            return null;
        }
    }
}
