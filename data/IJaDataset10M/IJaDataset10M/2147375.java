package net.woodstock.rockapi.pojo.converter.xml.internal;

import java.util.Properties;
import java.util.Map.Entry;
import net.woodstock.rockapi.pojo.converter.ConverterException;
import net.woodstock.rockapi.pojo.converter.xml.XmlAttributeConverter;
import net.woodstock.rockapi.xml.dom.XmlElement;

class PropertiesConverter implements XmlAttributeConverter<Properties> {

    private static final String CLASS_ATTRIBUTE = "class";

    private static final String ENTRY_ELEMENT = "entry";

    private static final String KEY_ELEMENT = "key";

    private static final String VALUE_ELEMENT = "value";

    @SuppressWarnings("unchecked")
    public Properties fromXml(XmlElement element) {
        try {
            Properties properties = new Properties();
            for (XmlElement e : element.getElements(PropertiesConverter.ENTRY_ELEMENT)) {
                XmlElement xmlKey = e.getElement(PropertiesConverter.KEY_ELEMENT);
                XmlElement xmlValue = e.getElement(PropertiesConverter.VALUE_ELEMENT);
                Class<?> keyClass = Class.forName(xmlKey.getAttribute(PropertiesConverter.CLASS_ATTRIBUTE));
                Class<?> valueClass = Class.forName(xmlValue.getAttribute(PropertiesConverter.CLASS_ATTRIBUTE));
                XmlAttributeConverter keyAttributeConverter = XmlConverterBase.getAttributeConverter(keyClass);
                XmlAttributeConverter valueAttributeConverter = XmlConverterBase.getAttributeConverter(valueClass);
                Object key = keyAttributeConverter.fromXml(xmlKey);
                Object value = valueAttributeConverter.fromXml(xmlValue);
                properties.put(key, value);
            }
            return properties;
        } catch (Exception e) {
            throw new ConverterException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public XmlElement toXml(XmlElement element, String name, Properties p) {
        if (p == null) {
            return element.addElement(name);
        }
        XmlElement e = element.addElement(name);
        for (Entry<?, ?> entry : p.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            XmlAttributeConverter keyAttributeConverter = XmlConverterBase.getNullAttributeConverter();
            XmlAttributeConverter valueAttributeConverter = XmlConverterBase.getNullAttributeConverter();
            if (key != null) {
                keyAttributeConverter = XmlConverterBase.getAttributeConverter(key.getClass());
            }
            if (value != null) {
                valueAttributeConverter = XmlConverterBase.getAttributeConverter(value.getClass());
            }
            XmlElement xmlEntry = e.addElement(PropertiesConverter.ENTRY_ELEMENT);
            XmlElement xmlKey = keyAttributeConverter.toXml(xmlEntry, PropertiesConverter.KEY_ELEMENT, key);
            xmlKey.setAttribute(PropertiesConverter.CLASS_ATTRIBUTE, key.getClass().getCanonicalName());
            XmlElement xmlValue = valueAttributeConverter.toXml(xmlEntry, PropertiesConverter.VALUE_ELEMENT, value);
            xmlValue.setAttribute(PropertiesConverter.CLASS_ATTRIBUTE, value.getClass().getCanonicalName());
        }
        return e;
    }
}
