package org.matsim.ptproject.qsim.interfaces;

import java.util.Map;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Network;
import org.matsim.vis.snapshots.writers.VisNetwork;

/**
 * @author nagel
 *
 */
public interface NetsimNetwork extends VisNetwork {

    Network getNetwork();

    Map<Id, ? extends NetsimLink> getNetsimLinks();

    Map<Id, ? extends NetsimNode> getNetsimNodes();

    /**
	 * Convenience method for getLinks().get( id ).  May be renamed
	 */
    public NetsimLink getNetsimLink(final Id id);

    /**
	 * Convenience method for getNodes().get( id ).  May be renamed
	 */
    public NetsimNode getNetsimNode(final Id id);

    void initialize(NetsimEngine netEngine);
}
