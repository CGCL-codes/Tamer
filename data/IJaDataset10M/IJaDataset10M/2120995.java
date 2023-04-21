package com.ohua.checkpoint.framework.serialization;

import com.ohua.checkpoint.framework.operatorcheckpoints.OhuaCheckpoint;

public interface TransactionalEndpointMinisSerializer {

    public void initialize();

    public void restart(long miniCheckpointPointer);

    public void serialize(OhuaCheckpoint cp);

    public OhuaCheckpoint deserialize();

    public void cleanup() throws Exception;
}
