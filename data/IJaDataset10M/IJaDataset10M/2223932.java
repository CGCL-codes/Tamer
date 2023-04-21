package org.svgobjects.examples.hello;

import com.webobjects.foundation.*;
import com.webobjects.appserver.*;

public class SVGRingsText extends WOComponent {

    public SVGRingsText(WOContext context) {
        super(context);
    }

    public boolean synchronizesVariablesWithBindings() {
        return false;
    }
}
