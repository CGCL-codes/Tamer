package logic.common.player.playerListeners;

public interface PlayerListener {

    public static final String RESPAWNED = "respawned";

    public static final String HUNTERCHANGED = "hunterChanged";

    public static final String TARGETCHANGED = "targetChanged";

    public static final String KILLED = "killed";

    public static final String KILLASSIST = "killAssist";

    public static final String MISSIONASSIST = "missionAssist";

    public void respawned(RespawnEvent event);

    public void hunterChanged(HunterChangedEvent event);

    public void targetChanged(TargetChangedEvent event);

    public void killed(KillEvent event);

    public void killAssist(KillAssistEvent event);

    public void missionAssist(MissionAssistEvent event);
}
