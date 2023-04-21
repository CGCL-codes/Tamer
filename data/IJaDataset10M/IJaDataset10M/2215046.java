package com.softaspects.jsf.plugin.facelets.tag.flexmenu;

import com.softaspects.jsf.plugin.facelets.tag.BaseInternalTagHandler;
import com.sun.facelets.tag.TagConfig;
import com.softaspects.jsf.component.flexMenu.FlexMenuConfiguration;
import com.softaspects.jsf.component.flexMenu.FlexMenu;

public class BaseFlexMenuConfigurationTagHandler extends BaseInternalTagHandler {

    public BaseFlexMenuConfigurationTagHandler(TagConfig tagConfig) {
        super(tagConfig);
        setComponent(new FlexMenuConfiguration());
    }

    protected Class getParentComponentType() {
        return FlexMenu.class;
    }

    protected String getChildComponentFieldName() {
        return "menuConfiguration";
    }

    protected Class getComponentType() {
        return FlexMenuConfiguration.class;
    }
}
