package org.caisi.bso.PMmodule;

import java.util.List;
import org.caisi.entity.PMmodule.ProgramQueue;

public interface ProgramQueueManager {

    public ProgramQueue getProgramQueue(String queueId);

    public List getAllProgramQueues();

    public void saveProgramQueue(ProgramQueue programQueue);

    public void removeProgramQueue(String queueId);
}
