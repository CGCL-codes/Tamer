package org.apache.zookeeper.server.quorum;

import org.apache.zookeeper.server.quorum.Vote;

public interface Election {

    public Vote lookForLeader() throws InterruptedException;

    public void shutdown();
}
