package starcraft.gamemodel.races.protoss;

import starcraft.gamemodel.compatibility.Color;
import starcraft.gamemodel.races.Faction;
import starcraft.gamemodel.races.Faction.FactionBase;
import starcraft.gamemodel.races.Race;
import starcraft.gamemodel.races.protoss.units.ZealotUnitType;
import starcraft.gamemodel.references.UnitType;

public class TassadarFaction extends FactionBase implements Faction {

    private static final long serialVersionUID = 1870726020108694875L;

    public static final TassadarFaction INSTANCE = new TassadarFaction();

    private static final UnitType[] startingForces = new UnitType[] { ZealotUnitType.INSTANCE, ZealotUnitType.INSTANCE, ZealotUnitType.INSTANCE };

    private Color color;

    private Color pastelColor;

    private TassadarFaction() {
    }

    private Object readResolve() {
        return INSTANCE;
    }

    @Override
    public String getName() {
        return "Tassadar";
    }

    @Override
    public Race getRace() {
        return ProtossRace.INSTANCE;
    }

    @Override
    public int getNumberOfStartingWorkers() {
        return 8;
    }

    @Override
    public UnitType[] getStartingForces() {
        return startingForces;
    }

    @Override
    public boolean isSpecialVictoryAchieved() {
        return false;
    }

    @Override
    public Color getColor() {
        if (this.color == null) {
            this.color = new Color(255, 255, 0);
        }
        return this.color;
    }

    @Override
    public Color getColorPastel() {
        if (this.pastelColor == null) {
            this.pastelColor = new Color(255, 255, 128);
        }
        return this.pastelColor;
    }

    @Override
    public String getFileSuffix() {
        return "tassadar";
    }
}
