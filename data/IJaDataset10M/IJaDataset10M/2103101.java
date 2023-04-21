package net.simpleframework.content.news;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.simpleframework.content.component.newspager.NewsCatalog;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeHandle;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;
import net.simpleframework.web.page.component.ui.tree.TreeBean;
import net.simpleframework.web.page.component.ui.tree.TreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NewsLayoutCatalogSelect extends AbstractTreeHandle {

    @Override
    public Collection<? extends AbstractTreeNode> getTreenodes(final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
        final List<NewsCatalog> catalogs;
        if (treeNode == null && (catalogs = NewsUtils.applicationModule.listNewsCatalog(compParameter)) != null) {
            final Collection<TreeNode> nodes = new ArrayList<TreeNode>();
            for (final NewsCatalog catalog : catalogs) {
                final TreeNode treeNode2 = new TreeNode((TreeBean) compParameter.componentBean, treeNode, catalog);
                nodes.add(treeNode2);
            }
            return nodes;
        } else {
            return null;
        }
    }
}
