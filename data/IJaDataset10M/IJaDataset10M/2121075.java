package com.softaspects.jsf.plugin.facelets.tag.menu;

import com.sun.facelets.tag.TagConfig;

public class MenuDataModelTagHandler extends BaseMenuDataModelTagHandler {

    public MenuDataModelTagHandler(TagConfig tagConfig) {
        super(tagConfig);
        setAttribute("beanName", String.class, null);
        setAttribute("binding", String.class, null);
        setAttribute("scope", String.class, null);
        applyAttributes();
    }
}
