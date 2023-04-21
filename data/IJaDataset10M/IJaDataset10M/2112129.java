package com.intellij.uiDesigner.lw;

import com.intellij.uiDesigner.UIFormXmlConstants;
import org.jdom.Element;

/**
 * @author yole
 */
public class LwIntroComponentProperty extends LwIntrospectedProperty {

    public LwIntroComponentProperty(final String name, final String propertyClassName) {
        super(name, propertyClassName);
    }

    public Object read(Element element) throws Exception {
        return LwXmlReader.getRequiredString(element, UIFormXmlConstants.ATTRIBUTE_VALUE);
    }
}
