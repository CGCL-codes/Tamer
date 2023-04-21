package com.bluesky.plum.uimodels.render.swing.components.containers.layout;

import javax.swing.Box;
import javax.swing.JComponent;
import com.bluesky.plum.uimodels.standard.UIComponent;
import com.bluesky.plum.uimodels.standard.components.containers.layout.VerticalBox;

public class SVerticalBox extends VerticalBox {

    Box box;

    public SVerticalBox() {
        box = javax.swing.Box.createVerticalBox();
    }

    @Override
    public Object getNativeComponent() {
        return box;
    }

    @Override
    public void addChildComponent(UIComponent uic) {
        super.addChildComponent(uic);
        box.add((JComponent) uic.getNativeComponent());
    }

    @Override
    public void removeAllChildren() {
        super.removeAllChildren();
        box.removeAll();
    }
}
