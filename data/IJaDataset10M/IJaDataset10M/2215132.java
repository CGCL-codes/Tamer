package com.ixora.rms.tests;

import junit.framework.TestCase;
import com.ixora.rms.EntityId;

public class EntityIdTest extends TestCase {

    private static final String[] ENTITY_IDS = new String[] { "root/com.bea/ServerRuntimes/(.*)/([^\\\\.]+\\\\.jms)", "root/com.bea/ServerRuntimes/(.*)/([^////.]+////.jms)" };

    private static final String[] UNESCAPED_PATH_COMPS = new String[] { "([^\\.]+\\.jms)", "([^//.]+//.jms)" };

    private EntityId[] entityIds;

    protected void setUp() throws Exception {
        super.setUp();
        entityIds = new EntityId[ENTITY_IDS.length];
        for (int i = 0; i < entityIds.length; i++) {
            entityIds[i] = new EntityId(ENTITY_IDS[i]);
        }
    }

    public void testEscape() {
        for (int i = 0; i < entityIds.length; i++) {
            assertTrue(entityIds[i].getPathComponents()[4].equals(UNESCAPED_PATH_COMPS[i]));
        }
    }
}
