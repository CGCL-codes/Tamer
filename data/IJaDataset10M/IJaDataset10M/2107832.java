package org.matsim.signalsystems.otfvis.io;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.matsim.lanes.otfvis.io.OTFLinkWLanes;
import org.matsim.signalsystems.model.SignalGroupState;

/**
 * @author dgrether
 *
 */
public class OTFSignal implements Serializable {

    private String id;

    private SignalGroupState state;

    private List<OTFLinkWLanes> turningMoveRestrictions = null;

    private String systemId;

    public OTFSignal(String systemId, String signalId) {
        this.systemId = systemId;
        this.id = signalId;
    }

    public String getId() {
        return this.id;
    }

    public String getSignalSystemId() {
        return this.systemId;
    }

    public void setState(SignalGroupState state) {
        this.state = state;
    }

    public SignalGroupState getSignalGroupState() {
        return this.state;
    }

    public List<OTFLinkWLanes> getTurningMoveRestrictions() {
        return this.turningMoveRestrictions;
    }

    public void addTurningMoveRestriction(OTFLinkWLanes toLink) {
        if (this.turningMoveRestrictions == null) {
            this.turningMoveRestrictions = new ArrayList<OTFLinkWLanes>();
        }
        this.turningMoveRestrictions.add(toLink);
    }
}
