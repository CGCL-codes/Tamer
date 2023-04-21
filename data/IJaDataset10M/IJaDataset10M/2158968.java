package net.itsite.ad;

import java.util.ArrayList;
import java.util.Collection;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeHandle;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;
import net.simpleframework.web.page.component.ui.tree.TreeNode;

/**
 * 添加的时候显示的树结构
 */
public class AdTreeHandle extends AbstractTreeHandle {

    @Override
    public Collection<? extends AbstractTreeNode> getTreenodes(final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
        final Collection<TreeNode> nodes = new ArrayList<TreeNode>();
        if (treeNode == null) {
            for (EAd ad : EAd.values()) {
                final TreeNode node = new TreeNode((AbstractTreeBean) compParameter.componentBean, treeNode, ad.toString());
                node.setJsClickCallback("$IT.A('adAct','adId=" + ad.name() + "');");
                node.setId(ad.name());
                nodes.add(node);
            }
        }
        return nodes;
    }
}
