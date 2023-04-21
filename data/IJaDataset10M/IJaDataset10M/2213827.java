package org.nakedobjects.nof.reflect.remote.data;

import java.io.Serializable;
import org.nakedobjects.noa.adapter.Oid;
import org.nakedobjects.noa.adapter.Version;

public interface ReferenceData extends Data, Serializable {

    Oid getOid();

    Version getVersion();
}
