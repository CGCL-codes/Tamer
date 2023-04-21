package org.matsim.core.basic.network;

import java.util.ArrayList;
import java.util.List;
import org.matsim.api.basic.v01.Id;

/**
 * @author dgrether
 */
public class BasicLanesToLinkAssignmentImpl implements BasicLanesToLinkAssignment {

    private Id linkId;

    private List<BasicLane> lanes;

    /**
	 * @param linkId
	 */
    public BasicLanesToLinkAssignmentImpl(Id linkId) {
        this.linkId = linkId;
    }

    /**
	 * @see org.matsim.core.basic.network.BasicLanesToLinkAssignment#getLanes()
	 */
    public List<BasicLane> getLanes() {
        return this.lanes;
    }

    /**
	 * @see org.matsim.core.basic.network.BasicLanesToLinkAssignment#addLane(org.matsim.core.basic.network.BasicLane)
	 */
    public void addLane(BasicLane lane) {
        if (this.lanes == null) {
            this.lanes = new ArrayList<BasicLane>();
        }
        this.lanes.add(lane);
    }

    /**
	 * @see org.matsim.core.basic.network.BasicLanesToLinkAssignment#getLinkId()
	 */
    public Id getLinkId() {
        return linkId;
    }
}
