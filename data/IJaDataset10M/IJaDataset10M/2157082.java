package playground.dgrether.signalsNew.model;

import org.matsim.api.core.v01.Id;
import org.matsim.signalsystems.control.SignalGroupState;

/**
 * @author dgrether
 *
 */
public interface SignalizedItem {

    public void setSignalized(boolean isSignalized);

    public void setSignalStateAllTurningMoves(SignalGroupState state);

    public void setSignalStateForTurningMove(SignalGroupState state, Id toLinkId);
}
