package logic.common.player.playerListeners;

public interface PlayerStationListener {

    public static final String NEARSPAWNSTATIONCHANGED = "nearSpawnStationChanged";

    public static final String SELECTEDSPAWNPOSITIONCHANGED = "selectedSpawnPositionChanged";

    public void nearSpawnStationChanged(NearStationChangedEvent event);

    public void selectedSpawnPositionChanged(SelectedSpawnChangedEvent event);
}
