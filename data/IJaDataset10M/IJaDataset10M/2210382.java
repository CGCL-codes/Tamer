package org.jowidgets.workbench.toolkit.impl;

import java.util.LinkedList;
import java.util.List;
import org.jowidgets.util.Assert;
import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.api.IComponentContext;
import org.jowidgets.workbench.api.IComponentNode;
import org.jowidgets.workbench.api.IComponentNodeContext;
import org.jowidgets.workbench.toolkit.api.IComponentFactory;
import org.jowidgets.workbench.toolkit.api.IComponentNodeContainerModel;
import org.jowidgets.workbench.toolkit.api.IComponentNodeInitializeCallback;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;

class ModelBasedComponentNodeContainerContext {

    private final IComponentNodeContainerModel nodeModel;

    private final List<IComponentNode> addedComponentNodes;

    ModelBasedComponentNodeContainerContext(final IComponentNodeContainerModel nodeModel) {
        Assert.paramNotNull(nodeModel, "nodeModel");
        this.nodeModel = nodeModel;
        this.addedComponentNodes = new LinkedList<IComponentNode>();
    }

    public final void add(final int index, final IComponentNode componentNode) {
        Assert.paramNotNull(componentNode, "componentNode");
        final ComponentNodeModelBuilder modelBuilder = new ComponentNodeModelBuilder();
        modelBuilder.setId(componentNode.getId());
        modelBuilder.setLabel(componentNode.getLabel());
        modelBuilder.setTooltip(componentNode.getTooltip());
        modelBuilder.setIcon(componentNode.getIcon());
        modelBuilder.setComponentFactory(new IComponentFactory() {

            @Override
            public IComponent createComponent(final IComponentNodeModel treeNodeModel, final IComponentContext context) {
                return componentNode.createComponent(context);
            }
        });
        modelBuilder.setInitializeCallback(new IComponentNodeInitializeCallback() {

            @Override
            public void onContextInitialize(final IComponentNodeContext context) {
                componentNode.onContextInitialize(context);
            }
        });
        addedComponentNodes.add(index, componentNode);
        nodeModel.addChild(index, modelBuilder);
    }

    public final void add(final IComponentNode componentNode) {
        add(addedComponentNodes.size(), componentNode);
    }

    public final void remove(final IComponentNode componentNode) {
        final int index = addedComponentNodes.indexOf(componentNode);
        if (index != -1) {
            nodeModel.remove(index);
        }
    }
}
