package org.obe.xpdl.model.data;

import org.obe.OBERuntimeException;
import org.obe.XMLException;
import org.obe.util.SchemaUtils;
import org.obe.util.W3CNames;
import org.w3c.dom.Document;
import java.io.StringReader;

/**
 * A schema type refers to a data type that is defined by an inline XML schema.
 *
 * @author Adrian Price
 */
public final class SchemaType extends XMLFragment implements Type {

    private static final long serialVersionUID = 6285607010686717240L;

    private transient Type _xpdlType;

    /**
     * Constructs a new SchemaType object.
     */
    public SchemaType() {
    }

    public SchemaType(String text) throws XMLException {
        super(text);
    }

    public SchemaType(Document document) throws XMLException {
        super(document);
    }

    protected String getDocumentElementName() {
        return W3CNames.XSD_SCHEMA;
    }

    protected String getDocumentElementNamespaceURI() {
        return W3CNames.XSD_NS_URI;
    }

    public int value() {
        return SCHEMA_TYPE;
    }

    public Type getImpliedType() {
        if (_xpdlType == null) {
            if (getText() != null) {
                try {
                    Class javaType = SchemaUtils.classForSchemaType(new StringReader(getText()), null);
                    _xpdlType = javaType == Object.class ? this : DataTypes.dataTypeForClass(javaType).getType();
                } catch (XMLException e) {
                    throw new OBERuntimeException(e);
                }
            } else {
                _xpdlType = this;
            }
        }
        return _xpdlType;
    }

    public boolean isAssignableFrom(Type fromType) {
        return equals(fromType) || getImpliedType() != this && _xpdlType.isAssignableFrom(fromType);
    }
}
