package org.sgodden.echo.ext20.peers.tree;

import java.io.Serializable;
import nextapp.echo.app.Component;
import nextapp.echo.app.util.Context;
import nextapp.echo.extras.app.Tree;
import nextapp.echo.extras.app.tree.TreePath;
import org.sgodden.echo.ext20.ExtendedTreeNode;
import org.w3c.dom.Element;

public class TreeStructureRenderer extends nextapp.echo.extras.webcontainer.sync.component.tree.TreeStructureRenderer {

    public TreeStructureRenderer(Element propertyElement, Tree tree) {
        super(propertyElement, tree);
    }

    @Override
    protected Element doRenderNode(TreePath path, Component component, Context context, Serializable value, boolean isRoot) {
        Element eElement = super.doRenderNode(path, component, context, value, isRoot);
        if (path != null && path.getLastPathComponent() instanceof ExtendedTreeNode) {
            ExtendedTreeNode treeNode = (ExtendedTreeNode) path.getLastPathComponent();
            eElement.setAttribute("ck", treeNode.canBeSelected() ? "1" : "0");
        }
        return eElement;
    }
}
