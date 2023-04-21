package com.continuent.tungsten.replicator.database;

import junit.framework.Assert;
import org.junit.Test;
import com.continuent.tungsten.replicator.consistency.ConsistencyTable;

public class TestTable {

    @Test
    public void testClone() {
        Table original = ConsistencyTable.getConsistencyTableDefinition("test");
        Table clone = original.clone();
        int originalKeyCount = original.getKeys().size();
        Key primaryKey = clone.getPrimaryKey();
        Assert.assertNotNull("Consistency table has a PK (did declaration changed?)", primaryKey);
        Assert.assertTrue("PK is successfully removed", clone.getKeys().remove(primaryKey));
        Assert.assertFalse("Clone table has no PK in key array after removing it", clone.getKeys().contains(primaryKey));
        Key originalPrimaryKey = original.getPrimaryKey();
        Assert.assertNotNull("Original table contains PK after PK was removed from the clone", originalPrimaryKey);
        Assert.assertTrue("Original table key array still contains PK", original.getKeys().contains(originalPrimaryKey));
        Assert.assertEquals("Original table key count didn't change after removing PK from the clone", originalKeyCount, original.getKeys().size());
    }
}
