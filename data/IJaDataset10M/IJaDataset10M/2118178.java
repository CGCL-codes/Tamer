package starcraft.gamemodel.races;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import starcraft.gamemodel.references.UnitType;

public class RacesUtils {

    public static UnitType findUnitTypeByName(String name) {
        for (UnitType unitType : getAllUnitTypes()) {
            if (unitType.getName().equalsIgnoreCase(name)) {
                return unitType;
            }
        }
        return null;
    }

    private static Set<UnitType> getAllUnitTypes() {
        Set<UnitType> result = new HashSet<UnitType>();
        for (Race race : Race.ALL) {
            result.addAll(Arrays.asList(race.getUnitTypes()));
        }
        return result;
    }
}
