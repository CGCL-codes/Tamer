package settings;

import logic.nodes.nodeSettings.Settings;

public class CheatConfig extends ConfigFile {

    private static final String COLLISION = "collision";

    private static final String COLLISION_DAMAGE = "collision_damage";

    private static final String TEAM_COLLISION = "team_collision";

    private static final String COLLISION_BIG_SHIPS = "collision_big_ships";

    private static final String SPAWN_STATION_SIGHT = "spawn_station_sight";

    private static final String MOTHERSHIP_SIGHT = "mothership_sight";

    private static final String SQUAD_SIGHT = "squad_sight";

    private static CheatConfig cheatConfig = new CheatConfig();

    private CheatConfig() {
        super();
    }

    public static CheatConfig get() {
        return cheatConfig;
    }

    @Override
    protected Settings getSettings() {
        return cheatConfig;
    }

    @Override
    public void reset() {
        cheatConfig = new CheatConfig();
    }

    @Override
    public String getFileName() {
        return "cheatConfig.xml";
    }

    public boolean getEnableCollision() {
        return Boolean.valueOf(getEnableCollisionString()).booleanValue();
    }

    public String getEnableCollisionString() {
        return getValueOf(COLLISION);
    }

    public boolean getEnableCollDamage() {
        return Boolean.valueOf(getEnableCollDamageString()).booleanValue();
    }

    public String getEnableCollDamageString() {
        return getValueOf(COLLISION_DAMAGE);
    }

    public boolean getDisableTeamCollision() {
        return Boolean.valueOf(getDisableTeamCollisionString()).booleanValue();
    }

    public String getDisableTeamCollisionString() {
        return getValueOf(TEAM_COLLISION);
    }

    public boolean getCollisionBigShips() {
        return Boolean.valueOf(getCollisionBigShipsString()).booleanValue();
    }

    public String getCollisionBigShipsString() {
        return getValueOf(COLLISION_BIG_SHIPS);
    }

    public boolean getMotherShipSight() {
        return Boolean.valueOf(getMotherShipString()).booleanValue();
    }

    public String getMotherShipString() {
        return getValueOf(MOTHERSHIP_SIGHT);
    }

    public boolean getSpawnStationSight() {
        return Boolean.valueOf(getSpawnStationSightString()).booleanValue();
    }

    public String getSpawnStationSightString() {
        return getValueOf(SPAWN_STATION_SIGHT);
    }

    public boolean getSquadSight() {
        return Boolean.valueOf(getSquadSightString()).booleanValue();
    }

    public String getSquadSightString() {
        return getValueOf(SQUAD_SIGHT);
    }
}
