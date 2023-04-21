package starcraft.gamemodel.races.terrans.units;

import starcraft.gamemodel.races.Race;
import starcraft.gamemodel.races.defaults.DefaultGroundUnit;
import starcraft.gamemodel.races.terrans.TerranRace;

public class SiegeTankUnitType extends DefaultGroundUnit {

    private static final long serialVersionUID = -2777319943341615078L;

    public static SiegeTankUnitType INSTANCE = new SiegeTankUnitType();

    private SiegeTankUnitType() {
        super();
    }

    private Object readResolve() {
        return INSTANCE;
    }

    public Race getRace() {
        return TerranRace.INSTANCE;
    }

    @Override
    public String getName() {
        return "Siege Tank";
    }

    @Override
    public String getFileSuffix() {
        return "siege-tank";
    }
}
