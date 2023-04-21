package org.apache.fop.layout;

import org.apache.fop.fo.*;
import org.apache.fop.extensions.*;
import org.apache.fop.render.*;

public class ExtensionArea extends Area {

    private ExtensionObj _extensionObj;

    public ExtensionArea(ExtensionObj obj) {
        super(null);
        _extensionObj = obj;
    }

    public FObj getExtensionObj() {
        return _extensionObj;
    }

    public void render(Renderer renderer) {
    }
}
