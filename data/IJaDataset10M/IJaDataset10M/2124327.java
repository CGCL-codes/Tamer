package org.sourceforge.jemm.database.memory;

import org.sourceforge.jemm.database.components.se.AbstractStorageEngineEnumIFTest;

public class MemDbStorageEngineEnumIFTest extends AbstractStorageEngineEnumIFTest {

    public MemDbStorageEngineEnumIFTest() {
        super(new MemDbStorageEngineWrapper());
    }
}
