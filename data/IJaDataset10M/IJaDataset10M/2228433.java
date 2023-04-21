package logic.common.team.teamListeners;

public interface TeamShipListener {

    public static final String SHIPADDED = "shipAdded";

    public static final String SHIPREMOVED = "shipRemoved";

    public void shipAdded(ShipAddedEvent event);

    public void shipRemoved(ShipRemovedEvent event);
}
