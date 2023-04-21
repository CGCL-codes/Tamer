package org.nakedobjects.plugins.remoting.shared.data;

import java.io.Serializable;
import org.nakedobjects.metamodel.commons.encoding.ByteDecoder;
import org.nakedobjects.metamodel.commons.encoding.ByteEncoder;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.commons.lang.ToString;

public class EncodeableObjectDataImpl implements EncodeableObjectData, Encodable, Serializable {

    private static final long serialVersionUID = 1L;

    private final String type;

    private final String encodedObject;

    protected EncodeableObjectDataImpl(final String type, final String encodedObject) {
        this.type = type == null ? null : type.equals(String.class.getName()) ? "s" : type;
        this.encodedObject = encodedObject;
    }

    public EncodeableObjectDataImpl(final ByteDecoder decoder) {
        type = decoder.getString();
        encodedObject = decoder.getString();
    }

    public void encode(final ByteEncoder encoder) {
        encoder.add(type);
        encoder.add(encodedObject);
    }

    public String getEncodedObjectData() {
        return encodedObject;
    }

    public String getType() {
        return type.equals("s") ? String.class.getName() : type;
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("type", type);
        str.append("value", encodedObject);
        return str.toString();
    }
}
