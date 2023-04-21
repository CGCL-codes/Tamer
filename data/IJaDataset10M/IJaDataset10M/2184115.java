package com.daffodilwoods.daffodildb.server.datadictionarysystem.information;

import com.daffodilwoods.database.resource.*;

public interface _IndexInfo extends java.io.Externalizable {

    String getName();

    String[] getColumns();

    boolean[] getOrderOfColumns();
}
