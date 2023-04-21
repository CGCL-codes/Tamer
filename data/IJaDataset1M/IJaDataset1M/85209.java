package net.disy.ogc.wps.v_1_0_0.model;

import javax.xml.namespace.QName;
import org.apache.commons.lang.Validate;

public class SchemaReference {

    public static char DELIMITER = '#';

    public static String TYPE_PREFIX = "~";

    public static String ELEMENT_PREFIX = "";

    private final String location;

    private final String namespaceUri;

    private final String defaultPrefix;

    public SchemaReference(String location, String namespaceUri) {
        this(location, namespaceUri, null);
    }

    public SchemaReference(String location, String namespaceUri, String defaultPrefix) {
        super();
        Validate.notNull(location);
        Validate.notNull(namespaceUri);
        this.location = location;
        this.namespaceUri = namespaceUri;
        this.defaultPrefix = defaultPrefix;
    }

    public QName qname(String localPart) {
        if (this.defaultPrefix == null) {
            return new QName(this.namespaceUri, localPart);
        } else {
            return new QName(this.namespaceUri, localPart, this.defaultPrefix);
        }
    }

    public String typeDesignator(String localTypeName) {
        Validate.notNull(localTypeName);
        return this.location + DELIMITER + TYPE_PREFIX + localTypeName;
    }

    public String elementDesignator(String localElementName) {
        Validate.notNull(localElementName);
        return this.location + DELIMITER + ELEMENT_PREFIX + localElementName;
    }

    public <T> DataType<T> literalDataType(String localTypeName, Class<T> defaultClass) {
        return new DefaultDataType<T>(typeDesignator(localTypeName), defaultClass, DataTypeType.LITERAL);
    }

    public <T> DataType<T> bboxDataType(String localElementName, Class<T> defaultClass) {
        return new DefaultDataType<T>(elementDesignator(localElementName), defaultClass, DataTypeType.BBOX);
    }

    public <T> DataType<T> complexDataType(String localElementName, Class<T> defaultClass) {
        return new DefaultDataType<T>(elementDesignator(localElementName), defaultClass, DataTypeType.COMPLEX);
    }
}
