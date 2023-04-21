package org.nakedobjects.plugins.remoting.shared.data;

import java.io.Serializable;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;

public interface ReferenceData extends Data, Serializable {

    Oid getOid();

    Version getVersion();
}
