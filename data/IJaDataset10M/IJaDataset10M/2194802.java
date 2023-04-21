package org.ofbiz.minilang.method.eventops;

import java.util.Map;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.collections.FlexibleServletAccessor;
import org.ofbiz.minilang.SimpleMethod;
import org.ofbiz.minilang.method.ContextAccessor;
import org.ofbiz.minilang.method.MethodContext;
import org.ofbiz.minilang.method.MethodOperation;
import org.w3c.dom.Element;

/**
 * Copies a map field to a Servlet session attribute
 */
public class FieldToSession extends MethodOperation {

    public static final String module = FieldToSession.class.getName();

    ContextAccessor mapAcsr;

    ContextAccessor fieldAcsr;

    FlexibleServletAccessor sessionAcsr;

    public FieldToSession(Element element, SimpleMethod simpleMethod) {
        super(element, simpleMethod);
        mapAcsr = new ContextAccessor(element.getAttribute("map-name"));
        fieldAcsr = new ContextAccessor(element.getAttribute("field-name"));
        sessionAcsr = new FlexibleServletAccessor(element.getAttribute("session-name"), element.getAttribute("field-name"));
    }

    public boolean exec(MethodContext methodContext) {
        if (methodContext.getMethodType() == MethodContext.EVENT) {
            Object fieldVal = null;
            if (!mapAcsr.isEmpty()) {
                Map fromMap = (Map) mapAcsr.get(methodContext);
                if (fromMap == null) {
                    Debug.logWarning("Map not found with name " + mapAcsr, module);
                    return true;
                }
                fieldVal = fieldAcsr.get(fromMap, methodContext);
            } else {
                fieldVal = fieldAcsr.get(methodContext);
            }
            if (fieldVal == null) {
                Debug.logWarning("Field value not found with name " + fieldAcsr + " in Map with name " + mapAcsr, module);
                return true;
            }
            sessionAcsr.put(methodContext.getRequest().getSession(), fieldVal, methodContext.getEnvMap());
        }
        return true;
    }

    public String rawString() {
        return "<field-to-session field-name=\"" + this.fieldAcsr + "\" map-name=\"" + this.mapAcsr + "\"/>";
    }

    public String expandedString(MethodContext methodContext) {
        return this.rawString();
    }
}
