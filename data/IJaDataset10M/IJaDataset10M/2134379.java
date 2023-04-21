package org.ofbiz.minilang.operation;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.ObjectType;
import org.w3c.dom.Element;

/**
 * Convert the current field from the in-map and place it in the out-map
 */
public class Convert extends SimpleMapOperation {

    public static final String module = Convert.class.getName();

    String toField;

    String type;

    boolean replace = true;

    boolean setIfNull = true;

    String format;

    public Convert(Element element, SimpleMapProcess simpleMapProcess) {
        super(element, simpleMapProcess);
        this.toField = element.getAttribute("to-field");
        if (this.toField == null || this.toField.length() == 0) {
            this.toField = this.fieldName;
        }
        type = element.getAttribute("type");
        replace = !"false".equals(element.getAttribute("replace"));
        setIfNull = !"false".equals(element.getAttribute("set-if-null"));
        format = element.getAttribute("format");
    }

    public void exec(Map inMap, Map results, List messages, Locale locale, ClassLoader loader) {
        Object fieldObject = inMap.get(fieldName);
        if (fieldObject == null) {
            if (setIfNull && (replace || !results.containsKey(toField))) results.put(toField, null);
            return;
        }
        if (fieldObject instanceof java.lang.String) {
            if (((String) fieldObject).length() == 0) {
                if (setIfNull && (replace || !results.containsKey(toField))) results.put(toField, null);
                return;
            }
        }
        Object convertedObject = null;
        try {
            convertedObject = ObjectType.simpleTypeConvert(fieldObject, type, format, locale);
        } catch (GeneralException e) {
            addMessage(messages, loader, locale);
            Debug.logError(e, "Error in convert simple-map-processor operation: " + e.toString(), module);
            return;
        }
        if (convertedObject == null) return;
        if (replace) {
            results.put(toField, convertedObject);
        } else {
            if (results.containsKey(toField)) {
            } else {
                results.put(toField, convertedObject);
            }
        }
    }
}
