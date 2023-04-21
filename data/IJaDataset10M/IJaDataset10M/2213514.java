package org.yarl.db;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

public class WorkoutSubTypeDA {

    public PrimaryIndex<Long, WorkoutSubType> pIdx;

    public SecondaryIndex<Long, Long, WorkoutSubType> sIdxById;

    public SecondaryIndex<String, Long, WorkoutSubType> sIdxByName;

    public WorkoutSubTypeDA(EntityStore store) throws DatabaseException {
        pIdx = store.getPrimaryIndex(Long.class, WorkoutSubType.class);
        sIdxById = store.getSecondaryIndex(pIdx, Long.class, "workoutTypeId");
        sIdxByName = store.getSecondaryIndex(pIdx, String.class, "name");
    }
}
