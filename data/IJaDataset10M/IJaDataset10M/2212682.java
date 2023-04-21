package com.wgo.precise.client.ui.view.editor;

import com.wgo.precise.client.ui.controller.Registry;
import com.wgo.precise.client.ui.controller.RequirementPlugin;
import com.wgo.precise.client.ui.model.ClientModelItem;
import com.wgo.precise.client.ui.view.util.ViewerAction;

public class OpenModelEditorAction extends ViewerAction {

    private ClientModelItem item;

    public OpenModelEditorAction(ClientModelItem item) {
        super("Edit");
        this.item = item;
    }

    @Override
    public void run() {
        RequirementPlugin.getInstance().getEditorWindowManager().startEditorPart(Registry.MODEL_EDITOR, item);
        super.run();
    }
}
