package org.nakedobjects.plugins.remoting.shared.data;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.remoting.shared.encoding.object.data.IdentityData;

public class DummyReferenceData implements IdentityData {

    private static final long serialVersionUID = 1L;

    private final Oid oid;

    private final String type;

    private final Version version;

    public DummyReferenceData(final Oid oid, final String type, final Version version) {
        this.oid = oid;
        this.type = type;
        this.version = version;
    }

    public DummyReferenceData() {
        this(null, null, null);
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
        str.setUseLineBreaks(true);
        toString(str);
        return str.toString();
    }

    protected void toString(final ToString str) {
        str.append("oid", oid);
        str.append("type", type);
        str.append("version", version);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof DummyReferenceData) {
            final DummyReferenceData ref = (DummyReferenceData) obj;
            return (oid == null ? ref.oid == null : oid.equals(ref.oid)) && (type == null ? ref.type == null : type.equals(ref.type)) && (version == null ? ref.version == null : version.equals(ref.version));
        }
        return false;
    }
}
