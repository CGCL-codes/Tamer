package org.svgobjects;

import com.webobjects.foundation.*;
import com.webobjects.appserver.*;
import java.util.*;

public class SODynamicElement extends WODynamicElement {

    private NSMutableDictionary _associations;

    private WOAssociation _styleAssociation;

    private WOAssociation _hrefAssociation;

    private WOElement _children;

    protected String elementName;

    public static final String StyleKey = "style";

    public static final String HrefKey = "href";

    public SODynamicElement(String name, NSDictionary associations, WOElement element) {
        super(name, associations, element);
        _children = element;
        _associations = new NSMutableDictionary(associations);
        _styleAssociation = (WOAssociation) associations.objectForKey(StyleKey);
        _associations.removeObjectForKey(StyleKey);
        _hrefAssociation = (WOAssociation) associations.objectForKey(HrefKey);
        _associations.removeObjectForKey(HrefKey);
    }

    public void appendToResponse(WOResponse response, WOContext context) {
        WOComponent component = context.component();
        String style = new String();
        response.appendContentString("<" + elementName);
        if (_styleAssociation != null) style = (String) _styleAssociation.valueInComponent(component);
        if (_associations.count() > 0) {
            Enumeration keyEnumerator = _associations.keyEnumerator();
            while (keyEnumerator.hasMoreElements()) {
                String key = (String) keyEnumerator.nextElement();
                WOAssociation association = (WOAssociation) _associations.objectForKey(key);
                Object value = association.valueInComponent(component);
                if (value != null) {
                    if (key.startsWith("$")) style = (style + key.substring(1) + ":" + value + ";"); else response.appendContentString(" " + key + "=" + "\"" + value + "\"");
                }
            }
        }
        if (!style.equals("")) response.appendContentString(" style=" + "\"" + style + "\"");
        if (_hrefAssociation != null) {
            String href = (String) _hrefAssociation.valueInComponent(component);
            response.appendContentString(" " + "xlink:href" + "=" + "\"" + href + "\"");
        }
        response.appendContentString(">");
        if (_children != null) _children.appendToResponse(response, context);
        response.appendContentString("</" + elementName + ">");
    }
}
