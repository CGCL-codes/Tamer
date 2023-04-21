package org.apache.wml.dom;

import org.apache.wml.*;

/**
 * @xerces.internal
 * @version $Id: WMLOneventElementImpl.java 447257 2006-09-18 05:40:07Z mrglavas $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */
public class WMLOneventElementImpl extends WMLElementImpl implements WMLOneventElement {

    private static final long serialVersionUID = -4279215241146570871L;

    public WMLOneventElementImpl(WMLDocumentImpl owner, String tagName) {
        super(owner, tagName);
    }

    public void setClassName(String newValue) {
        setAttribute("class", newValue);
    }

    public String getClassName() {
        return getAttribute("class");
    }

    public void setId(String newValue) {
        setAttribute("id", newValue);
    }

    public String getId() {
        return getAttribute("id");
    }

    public void setType(String newValue) {
        setAttribute("type", newValue);
    }

    public String getType() {
        return getAttribute("type");
    }
}
