package edu.asu.csid.irrigation.events;

import edu.asu.csid.event.AbstractEvent;
import edu.asu.csid.irrigation.server.ServerDataModel;
import edu.asu.csid.net.Identifier;

/**
 * @author Sanket
 *
 */
public class FacilitatorEndRoundEvent extends AbstractEvent {

    private static final long serialVersionUID = -7917878646417155623L;

    private final ServerDataModel serverDataModel;

    public FacilitatorEndRoundEvent(Identifier id, ServerDataModel serverDataModel) {
        super(id);
        this.serverDataModel = serverDataModel;
    }

    public ServerDataModel getServerDataModel() {
        return serverDataModel;
    }

    public boolean isLastRound() {
        return serverDataModel.getCurrentConfiguration().getParentConfiguration().isLastRound();
    }
}
