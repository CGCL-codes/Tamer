package org.jato.tags;

import org.jato.*;
import org.jdom.*;

public class GetAttributeScriptTag extends AttributeScriptTag {

    private String fObjectKey;

    public GetAttributeScriptTag() {
    }

    public GetAttributeScriptTag(String key) {
        fObjectKey = key;
    }

    protected StringBuffer describeAttributes(StringBuffer buf) {
        return attr(super.describeAttributes(buf), "key", fObjectKey);
    }

    public String getObjectKey() {
        return fObjectKey;
    }

    public void setObjectKey(String key) {
        fObjectKey = key;
    }

    public Object getAttributeValue(Interpreter jato, Class thisClass, Object thisObj, Element xmlIn, Element xmlOut) throws JatoException {
        if (!fObjectKey.equals("this")) {
            thisObj = jato.getObject(fObjectKey, null);
        }
        return thisObj;
    }
}
