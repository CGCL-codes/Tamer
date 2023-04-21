package org.ofbiz.minilang.operation;

import java.util.*;
import org.w3c.dom.*;
import org.ofbiz.base.util.*;

/**
 * A single operation, does the specified operation on the given field
 */
public abstract class SimpleMapOperation {

    String message = null;

    String propertyResource = null;

    boolean isProperty = false;

    SimpleMapProcess simpleMapProcess;

    String fieldName;

    public SimpleMapOperation(Element element, SimpleMapProcess simpleMapProcess) {
        Element failMessage = UtilXml.firstChildElement(element, "fail-message");
        Element failProperty = UtilXml.firstChildElement(element, "fail-property");
        if (failMessage != null) {
            this.message = failMessage.getAttribute("message");
            this.isProperty = false;
        } else if (failProperty != null) {
            this.propertyResource = failProperty.getAttribute("resource");
            this.message = failProperty.getAttribute("property");
            this.isProperty = true;
        }
        this.simpleMapProcess = simpleMapProcess;
        this.fieldName = simpleMapProcess.getFieldName();
    }

    public abstract void exec(Map inMap, Map results, List messages, Locale locale, ClassLoader loader);

    public void addMessage(List messages, ClassLoader loader, Locale locale) {
        if (!isProperty && message != null) {
            messages.add(new MessageString(message, fieldName, true));
        } else if (isProperty && propertyResource != null && message != null) {
            String propMsg = UtilProperties.getMessage(propertyResource, message, locale);
            if (propMsg == null || propMsg.length() == 0) {
                messages.add(new MessageString("Simple Map Processing error occurred, but no message was found, sorry.", fieldName, propertyResource, message, locale, true));
            } else {
                messages.add(new MessageString(propMsg, fieldName, propertyResource, message, locale, true));
            }
        } else {
            messages.add(new MessageString("Simple Map Processing error occurred, but no message was found, sorry.", fieldName, true));
        }
    }
}
