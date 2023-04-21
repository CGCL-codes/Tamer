package org.jato.tags;

import java.util.*;
import org.jato.*;
import org.jdom.*;

class KeyTranslateScriptTag extends TranslateScriptTag {

    String fObjectKey;

    public KeyTranslateScriptTag() {
    }

    public String getObjectKey() {
        return fObjectKey;
    }

    public void setObjectKey(String key) {
        fObjectKey = key;
    }

    public void process(Interpreter jato, Class thisClass, Object thisObj, Element xmlIn, Element xmlOut) throws JatoException {
        try {
            ScriptTag params[] = getChildren(ParamScriptTag.sTypes, jato, thisClass, thisObj, xmlIn, xmlOut);
            Properties parmList = new Properties();
            ParamScriptTag parm;
            for (int i = 0, len = params.length; i < len; i++) {
                if (params[i] instanceof ParamScriptTag) {
                    parm = (ParamScriptTag) params[i];
                    parm.getParameters(parmList, jato, thisClass, thisObj, xmlIn, xmlOut);
                } else {
                    params[i].process(jato, thisClass, thisObj, xmlIn, xmlOut);
                }
            }
            Object obj = jato.getObject(fObjectKey, parmList);
            processChildrenWith(obj, jato, thisClass, thisObj, xmlIn, xmlOut);
        } catch (Exception ex) {
            throw new JatoException("Unable to get object key=" + fObjectKey, ex);
        }
    }
}
