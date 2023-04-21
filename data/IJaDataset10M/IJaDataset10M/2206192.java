package objects.battle;

import objects.Race;
import objects.ShipGroup;
import objects.ShipType;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author: serhiy
 * Created on Nov 11, 2007, 1:04:03 AM
 */
public class Capture extends BattleFilter {

    private final Set<BattleGroup> invulnerables = Collections.newSetFromMap(new IdentityHashMap<BattleGroup, Boolean>());

    private final Map<Race, Map<ShipGroup, Integer>> capturedGroups = new IdentityHashMap<Race, Map<ShipGroup, Integer>>();

    @Override
    public void start(IBattleGenerator battle) {
        invulnerables.clear();
        capturedGroups.clear();
    }

    @Override
    public Shot.Result hit(BattleRace attackingRace, BattleGroup attackingGroup, BattleGroup targetGroup) {
        if (!isInvulnerable(attackingRace, attackingGroup)) return Shot.Result.DESTROYED;
        ShipGroup group = targetGroup.getGroup();
        Map<ShipGroup, Integer> groupCounts = capturedGroups.get(attackingRace.getRace());
        if (groupCounts == null) {
            groupCounts = new IdentityHashMap<ShipGroup, Integer>();
            capturedGroups.put(attackingRace.getRace(), groupCounts);
        }
        Integer count = groupCounts.get(group);
        if (count == null) groupCounts.put(group, 1); else groupCounts.put(group, count + 1);
        return Shot.Result.CAPTURED;
    }

    @Override
    public void finish(IBattleGenerator battle) {
        for (Map.Entry<Race, Map<ShipGroup, Integer>> er : capturedGroups.entrySet()) {
            Race race = er.getKey();
            Map<ShipGroup, Integer> groupCounts = er.getValue();
            Map<ShipType, ShipType> capturedTypes = new IdentityHashMap<ShipType, ShipType>();
            for (Map.Entry<ShipGroup, Integer> eg : groupCounts.entrySet()) {
                ShipGroup group = eg.getKey();
                ShipType type = capturedTypes.get(group.getType());
                if (type == null) {
                    type = group.getType().cloneShipType(race);
                    capturedTypes.put(group.getType(), type);
                }
                group.cloneGroup(eg.getValue(), type);
            }
        }
    }

    private boolean isInvulnerable(BattleRace attackingRace, BattleGroup attackingGroup) {
        if (invulnerables.contains(attackingGroup)) return true;
        double minOffence = attackingGroup.getDefence() - 1.0;
        for (BattleRace br : attackingRace.getEnemies()) if (br.getMaxOffence() > minOffence) return false;
        invulnerables.add(attackingGroup);
        return true;
    }
}
