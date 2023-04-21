package org.neodatis.odb.core.server.layers.layer1;

import org.neodatis.odb.core.OldDatabaseEngine;
import org.neodatis.odb.core.layers.layer1.ObjectIntrospectorImpl;

public class ServerObjectIntrospector extends ObjectIntrospectorImpl {

    public ServerObjectIntrospector(OldDatabaseEngine storageEngine) {
        super(storageEngine);
    }
}
