package com.bluesky.plum.uimodels.render.html.components.containers;

import com.bluesky.javawebbrowser.domain.html.tags.Tag;
import com.bluesky.javawebbrowser.domain.html.tags.layout.DIV;
import com.bluesky.plum.uimodels.standard.UIComponent;

public class HPanel extends com.bluesky.plum.uimodels.standard.components.containers.Panel {

    DIV panel;

    public HPanel() {
        panel = new DIV();
    }

    @Override
    public Object getNativeComponent() {
        return panel;
    }

    @Override
    public void addChildComponent(UIComponent uic) {
        super.addChildComponent(uic);
        panel.addChild((Tag) uic.getNativeComponent());
    }

    @Override
    public void removeAllChildren() {
        super.removeAllChildren();
        panel.removeAllChildren();
    }
}
