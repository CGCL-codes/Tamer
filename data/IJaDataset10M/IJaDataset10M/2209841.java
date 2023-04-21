package net.sf.archimede.migration;

import java.util.ArrayList;
import java.util.List;
import net.sf.archimede.model.metadata.Attribute;
import net.sf.archimede.model.metadata.AttributeImpl;
import net.sf.archimede.model.metadata.AttributeTypeEnum;
import net.sf.archimede.model.metadata.DefaultElementImpl;
import net.sf.archimede.model.metadata.Element;
import net.sf.archimede.model.metadata.Metadata;
import net.sf.archimede.model.metadata.ValueElementImpl;

public class ConversionUtil {

    public static Element createValueElement(String title, String value, Metadata metadata, Element rootElement) {
        Element element = new DefaultElementImpl(metadata, rootElement);
        element.setName(title);
        List elementValues = new ArrayList();
        Element valueElement = new ValueElementImpl(metadata, element);
        List attributes = new ArrayList();
        Attribute attribute = AttributeImpl.createValueAttributeInstance(element, AttributeTypeEnum.STRING_TYPE);
        attribute.setValue(value);
        attributes.add(attribute);
        valueElement.setAttributes(attributes);
        elementValues.add(valueElement);
        element.setElements(elementValues);
        return element;
    }
}
