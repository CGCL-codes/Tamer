package starcraft.gamemodel.races.zerg.units;

import starcraft.gamemodel.races.Race;
import starcraft.gamemodel.races.defaults.DefaultGroundUnit;
import starcraft.gamemodel.races.zerg.ZergRace;

public class UltraliskUnitType extends DefaultGroundUnit {

    private static final long serialVersionUID = -1669112133287994122L;

    public static UltraliskUnitType INSTANCE = new UltraliskUnitType();

    private UltraliskUnitType() {
        super();
    }

    private Object readResolve() {
        return INSTANCE;
    }

    public Race getRace() {
        return ZergRace.INSTANCE;
    }

    @Override
    public String getName() {
        return "Ultralisk";
    }

    @Override
    public int getSkirmishAttackSupportValue() {
        return 2;
    }

    @Override
    public String getFileSuffix() {
        return "ultralisk";
    }
}
