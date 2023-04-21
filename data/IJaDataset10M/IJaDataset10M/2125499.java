package net.simpleframework.content.bbs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeHandle;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class BbsForumDictTree extends AbstractTreeHandle {

    @Override
    public Collection<? extends AbstractTreeNode> getTreenodes(final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
        return BbsUtils.applicationModule.getForumDictTree(compParameter, treeNode);
    }

    @Override
    public Map<String, Object> getTreenodeAttributes(final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("selected", treeNode.getParent() != null);
        return attributes;
    }
}
