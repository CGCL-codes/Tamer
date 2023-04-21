package com.ramsayconz.wocore.woc;

import com.webobjects.appserver.WOAssociation;
import com.webobjects.appserver.WOElement;
import com.webobjects.appserver._private.WOConstantValueAssociation;
import com.webobjects.appserver._private.WOHTMLURLValuedElement;
import com.webobjects.foundation.NSDictionary;

public class WOStyleSheet extends WOHTMLURLValuedElement {

    public WOStyleSheet(String name, NSDictionary<String, WOAssociation> assocationsDictionary, WOElement template) {
        super("link", assocationsDictionary, template);
        if (this._associations.objectForKey("rel") == null) {
            this._associations.setObjectForKey(new WOConstantValueAssociation("stylesheet"), "rel");
        }
        if (this._associations.objectForKey("type") == null) {
            this._associations.setObjectForKey(new WOConstantValueAssociation("text/css"), "type");
        }
    }

    @Override
    protected String urlAttributeName() {
        return "href";
    }

    @Override
    protected boolean hasContent() {
        return false;
    }
}
