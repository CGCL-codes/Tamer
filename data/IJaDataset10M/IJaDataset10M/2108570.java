package com.softaspects.jsf.plugin.facelets.tag.flexmenu;

import com.sun.facelets.tag.TagConfig;

public class FlexSubmenuConfigurationTagHandler extends BaseFlexSubmenuConfigurationTagHandler {

    public FlexSubmenuConfigurationTagHandler(TagConfig tagConfig) {
        super(tagConfig);
        setAttribute("activeBackgroundCSSRule", String.class, null);
        setAttribute("activeFontColor", String.class, null);
        setAttribute("activeFontCSSRule", String.class, null);
        setAttribute("activeStyle", String.class, null);
        setAttribute("activeStyleClass", String.class, null);
        setAttribute("binding", String.class, null);
        setAttribute("disabledBackgroundCSSRule", String.class, null);
        setAttribute("disabledFontColor", String.class, null);
        setAttribute("disabledFontCSSRule", String.class, null);
        setAttribute("disabledStyle", String.class, null);
        setAttribute("disabledStyleClass", String.class, null);
        setAttribute("hintText", String.class, null);
        setAttribute("normalStyle", String.class, null);
        setAttribute("normalBackgroundCSSRule", String.class, null);
        setAttribute("normalFontColor", String.class, null);
        setAttribute("normalFontCSSRule", String.class, null);
        setAttribute("normalStyleClass", String.class, null);
        setAttribute("menuItemHeight", String.class, null);
        setAttribute("submenuIndicatorImageHeight", String.class, null);
        setAttribute("submenuIndicatorImageSrc", String.class, null);
        setAttribute("submenuIndicatorImageWidth", String.class, null);
        setAttribute("submenuWidth", String.class, null);
        applyAttributes();
    }
}
