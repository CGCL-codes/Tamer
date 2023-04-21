package org.matsim.signalsystems.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.signalsystems.data.signalsystems.v20.SignalData;
import org.matsim.signalsystems.mobsim.SignalizeableItem;

/**
 * @author dgrether
 *
 */
public class DatabasedSignal implements Signal {

    private static final Logger log = Logger.getLogger(DatabasedSignal.class);

    private List<SignalizeableItem> signalizedItems = new ArrayList<SignalizeableItem>();

    private SignalData data;

    public DatabasedSignal(SignalData signalData) {
        this.data = signalData;
    }

    @Override
    public Id getLinkId() {
        return this.data.getLinkId();
    }

    @Override
    public void setState(SignalGroupState state) {
        if (this.data.getTurningMoveRestrictions() == null || this.data.getTurningMoveRestrictions().isEmpty()) {
            for (SignalizeableItem item : this.signalizedItems) {
                item.setSignalStateAllTurningMoves(state);
            }
        } else {
            for (SignalizeableItem item : this.signalizedItems) {
                for (Id toLinkId : this.data.getTurningMoveRestrictions()) {
                    item.setSignalStateForTurningMove(state, toLinkId);
                }
            }
        }
    }

    @Override
    public void addSignalizeableItem(SignalizeableItem signalizedItem) {
        this.signalizedItems.add(signalizedItem);
    }

    @Override
    public Set<Id> getLaneIds() {
        return this.data.getLaneIds();
    }

    @Override
    public Id getId() {
        return this.data.getId();
    }

    @Override
    public Collection<SignalizeableItem> getSignalizeableItems() {
        return this.signalizedItems;
    }
}
