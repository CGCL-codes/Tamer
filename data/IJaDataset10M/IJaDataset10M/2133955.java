package starcraft.gamemodel.races.protoss.units;

import starcraft.gamemodel.races.Race;
import starcraft.gamemodel.races.defaults.DefaultGroundUnit;
import starcraft.gamemodel.races.protoss.ProtossRace;

public class DarkTemplarUnitType extends DefaultGroundUnit {

    private static final long serialVersionUID = 1132423483276454621L;

    public static DarkTemplarUnitType INSTANCE = new DarkTemplarUnitType();

    private DarkTemplarUnitType() {
        super();
    }

    private Object readResolve() {
        return INSTANCE;
    }

    public Race getRace() {
        return ProtossRace.INSTANCE;
    }

    @Override
    public String getName() {
        return "Dark Templar";
    }

    @Override
    public String getFileSuffix() {
        return "dark-templar";
    }
}
