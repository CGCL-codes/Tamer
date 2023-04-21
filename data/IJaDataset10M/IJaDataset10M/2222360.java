package org.sourceforge.jemm.database.persistent.berkeley;

import java.util.Iterator;
import org.sourceforge.jemm.database.ClientId;
import org.sourceforge.jemm.types.ID;

public interface BDbStorageEngineClientRefIF {

    boolean recordReference(ClientId clientId, ID id);

    boolean clearReference(ClientId clientId, ID id);

    boolean hasReference(ClientId clientId, ID id);

    Iterator<ID> referenceIterator(ClientId clientId);

    void removeAllRefs(ClientId clientId);
}
