package org.nakedobjects.plugins.remoting.shared.data;

import java.io.Serializable;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.commons.encoding.ByteDecoder;
import org.nakedobjects.metamodel.commons.encoding.ByteEncoder;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.commons.lang.ToString;

public class IdentityDataImpl implements IdentityData, Encodable, Serializable {

    private static final long serialVersionUID = 1L;

    private final Oid oid;

    private final String type;

    private final Version version;

    public IdentityDataImpl(final String type, final Oid oid, final Version version) {
        this.type = type;
        this.oid = oid;
        this.version = version;
    }

    public IdentityDataImpl(final ByteDecoder decoder) {
        type = decoder.getString();
        oid = (Oid) decoder.getObject();
        version = (Version) decoder.getObject();
    }

    public void encode(final ByteEncoder encoder) {
        encoder.add(type);
        encoder.add(oid);
        encoder.add(version);
    }

    public Oid getOid() {
        return oid;
    }

    public String getType() {
        return type;
    }

    public Version getVersion() {
        return version;
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("type", type);
        str.append("oid", oid);
        str.append("version", version);
        return str.toString();
    }
}
