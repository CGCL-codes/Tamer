package com.softaspects.jsf.plugin.facelets.tag.layout;

import com.sun.facelets.tag.TagConfig;
import com.softaspects.jsf.component.container.OrderingBoxLayoutManager;
import javax.faces.component.UIComponent;

public class BaseOrderingBoxLayoutTagHandler extends LayoutTagHandler {

    public BaseOrderingBoxLayoutTagHandler(TagConfig tagConfig) {
        super(tagConfig);
        setComponent(new OrderingBoxLayoutManager());
    }

    protected void setRendererType(UIComponent parent) {
    }
}
