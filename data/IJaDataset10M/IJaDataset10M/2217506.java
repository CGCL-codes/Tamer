package com.tensegrity.palowebviewer.modules.widgets.client;

import com.tensegrity.palowebviewer.modules.widgets.client.dnd.DnDEngine;
import com.tensegrity.palowebviewer.modules.widgets.client.dnd.IDnDContrloller;
import com.tensegrity.palowebviewer.modules.widgets.client.dnd.IDnDEngine;
import com.tensegrity.palowebviewer.modules.widgets.client.dnd.IDnDTarget;

public class DnDItemConfigurator extends ChainItemConfigurator {

    private static final IDnDEngine dndEngine = DnDEngine.getInstance();

    private static final IDnDContrloller controller = dndEngine.getController();

    private IDnDTarget dndTarget;

    public void setDndTarget(IDnDTarget target) {
        this.dndTarget = target;
    }

    public DnDItemConfigurator(ITreeItemConfigurator baseConfigurator) {
        super(baseConfigurator);
    }

    protected void configure(ITreeViewItem item) {
        Object node = item.getNode();
        IDnDEngine engine = controller.isDraggable(node) ? dndEngine : null;
        item.setDnDEngine(engine);
        item.setDnDTarget(dndTarget);
    }
}
