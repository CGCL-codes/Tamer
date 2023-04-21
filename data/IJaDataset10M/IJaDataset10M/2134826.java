package org.xmlvm.iphone.internal.renderer;

import org.xmlvm.XMLVMIgnore;
import org.xmlvm.iphone.UIBarItem;
import org.xmlvm.iphone.UIButton;

@XMLVMIgnore
public abstract class UIBarItemRenderer extends UIButtonRenderer {

    protected UIBarItem item;

    public UIBarItemRenderer(UIBarItem item, UIButton view) {
        super(view);
        this.item = item;
    }
}
