package com.softaspects.jsf.plugin.facelets.tag.layout;

import com.sun.facelets.tag.TagConfig;

public class NoLayoutTagHandler extends BaseNoLayoutTagHandler {

    public NoLayoutTagHandler(TagConfig tagConfig) {
        super(tagConfig);
        applyAttributes();
    }
}
