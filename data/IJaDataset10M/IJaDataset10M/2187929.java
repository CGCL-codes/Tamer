package org.nakedobjects.nos.client.web.request;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.adapter.NakedObjectLoader;
import org.nakedobjects.noa.adapter.Oid;
import org.nakedobjects.noa.adapter.Version;
import org.nakedobjects.nof.core.util.DebugString;

interface ObjectMapping {

    Oid getOid();

    NakedObject getObject();

    Version getVersion();

    void checkVersion(NakedObject object);

    void restoreToLoader(NakedObjectLoader objectLoader);

    void debug(DebugString debug);

    void updateVersion();
}
