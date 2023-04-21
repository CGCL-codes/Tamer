package org.vspirit.doveide.project.manager.ui;

import javax.swing.Action;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.vspirit.doveide.project.manager.ui.actions.DoveProjectSubNodeActionProvider;

/**
 * 定义项目子节点的外观和操作,子节点包括源文件结点,资源文件结点,
 * @author codekitten
 */
public class DoveProjectSubNode extends FilterNode {

    private String mDisplayName;

    /**
     * 由与特定文件相关的结点来生成子节点,并定义子节点的显示名称
     * @param node 与特定文件对象相关的结点
     * @param displayName 子节点显示名称
     */
    public DoveProjectSubNode(Node node, String displayName) {
        super(node);
        mDisplayName = displayName;
    }

    /**
     * 获取子节点的显示名称
     * @return 子节点的显示名称
     */
    @Override
    public String getDisplayName() {
        return mDisplayName;
    }

    /**
     * 获取用于子节点弹出式菜单的操作组
     */
    @Override
    public Action[] getActions(boolean context) {
        return DoveProjectSubNodeActionProvider.getProjectAction(context);
    }
}
