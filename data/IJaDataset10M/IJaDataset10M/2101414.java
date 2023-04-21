package org.svgobjects.examples.componenttour;

import com.webobjects.foundation.*;
import com.webobjects.appserver.*;
import com.webobjects.eocontrol.*;

public class EmbossImage extends WOComponent {

    public EmbossImage(WOContext context) {
        super(context);
    }

    public void appendToResponse(WOResponse response, WOContext context) {
        super.appendToResponse(response, context);
        response.setHeader("image/svg-xml", "Content-Type");
    }
}
