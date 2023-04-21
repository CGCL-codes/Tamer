package org.yarl.db;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

public class WorkoutTypeDA {

    public PrimaryIndex<Long, WorkoutType> pIdx;

    public SecondaryIndex<String, Long, WorkoutType> sIdxByName;

    public WorkoutTypeDA(EntityStore store) throws DatabaseException {
        pIdx = store.getPrimaryIndex(Long.class, WorkoutType.class);
        sIdxByName = store.getSecondaryIndex(pIdx, String.class, "name");
    }
}
