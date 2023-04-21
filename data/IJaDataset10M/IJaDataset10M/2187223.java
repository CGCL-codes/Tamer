package org.akrogen.tkui.css.swing.engine;

public class CSSSwingLazyHandlerEngineImpl extends AbstractCSSSwingEngineImpl {

    public CSSSwingLazyHandlerEngineImpl() {
        super();
    }

    public CSSSwingLazyHandlerEngineImpl(boolean lazyApplyingStyles) {
        super(lazyApplyingStyles);
    }

    protected void initializeCSSPropertyHandlers() {
        super.registerPackage("org.akrogen.tkui.css.swing.properties.css2.lazy.classification");
        super.registerPackage("org.akrogen.tkui.css.swing.properties.css2.lazy.border");
        super.registerPackage("org.akrogen.tkui.css.swing.properties.css2.lazy.font");
        super.registerPackage("org.akrogen.tkui.css.swing.properties.css2.lazy.background");
        super.registerPackage("org.akrogen.tkui.css.swing.properties.css2.lazy.text");
    }
}
