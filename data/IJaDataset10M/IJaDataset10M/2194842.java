package com.tensegrity.webetlclient.modules.model.client;

import com.tensegrity.webetlclient.modules.core.client.UIHelper;
import com.tensegrity.webetlclient.modules.core.client.model.SectionNode;

public class SectionEditorModel extends CompositeEditor {

    private final SectionNode node;

    private String config;

    public SectionNode getNode() {
        return node;
    }

    public SectionEditorModel(SectionNode node) {
        this.node = node;
        setTitle(UIHelper.getLocalNodeName(node));
    }

    public void saveChanges() {
        node.setConfig(getConfig());
        setModified(false);
    }

    public String getConfig() {
        if (config == null) {
            config = getNode().getConfig();
        }
        return config;
    }

    public void setConfig(String value) {
        config = value;
        setModified(true);
    }
}
