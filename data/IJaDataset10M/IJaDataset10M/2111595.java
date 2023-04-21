package org.middleheaven.ui.desktop.swing;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;

public class SWindowRender extends UIRender {

    @Override
    protected UIComponent build(RenderingContext context, UIComponent parent, UIComponent component) {
        return new SWindow();
    }
}
