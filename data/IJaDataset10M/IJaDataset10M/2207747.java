package com.softaspects.jsf.plugin.facelets.tag.selector;

import com.sun.facelets.tag.AbstractTagLibrary;

public class SelectorTagLibrary extends AbstractTagLibrary {

    public static final String Namespace = "http://www.softaspects.com/wgf/facelets/selector";

    public static final SelectorTagLibrary Instance = new SelectorTagLibrary();

    public SelectorTagLibrary() {
        super(Namespace);
        addComponent("selector", "com.softaspects.jsf.component.selector.Selector", "com.softaspects.jsf.renderer.selector.SelectorRenderer");
    }
}
