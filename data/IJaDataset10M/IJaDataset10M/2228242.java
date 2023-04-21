package org.hypergraphdb.peer.bootstrap;

import java.util.Map;
import org.hypergraphdb.peer.BootstrapPeer;
import org.hypergraphdb.peer.HGDBOntology;
import org.hypergraphdb.peer.HyperGraphPeer;
import org.hypergraphdb.peer.Performative;
import org.hypergraphdb.peer.replication.CatchUpTaskServer;
import org.hypergraphdb.peer.replication.GetInterestsTask;
import org.hypergraphdb.peer.replication.PublishInterestsTask;
import org.hypergraphdb.peer.replication.RememberTaskServer;
import org.hypergraphdb.peer.replication.Replication;
import org.hypergraphdb.peer.workflow.QueryTaskServer;

public class ReplicationBootstrap implements BootstrapPeer {

    public void bootstrap(HyperGraphPeer peer, Map<String, Object> config) {
        peer.getActivityManager().registerActivityType(PublishInterestsTask.class);
        Replication replication = new Replication(peer);
        peer.getObjectContext().put(Replication.class.getName(), replication);
        replication.catchUp();
    }
}
