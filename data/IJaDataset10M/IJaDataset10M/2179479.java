package megamek.common.weapons;

import megamek.common.BattleArmor;
import megamek.common.Compute;
import megamek.common.IGame;
import megamek.common.Infantry;
import megamek.common.RangeType;
import megamek.common.ToHitData;
import megamek.common.actions.WeaponAttackAction;
import megamek.server.Server;

public class VariableSpeedPulseLaserWeaponHandler extends EnergyWeaponHandler {

    /**
     *
     */
    private static final long serialVersionUID = -5701939682138221449L;

    /**
     * @param toHit
     * @param waa
     * @param g
     */
    public VariableSpeedPulseLaserWeaponHandler(ToHitData toHit, WeaponAttackAction waa, IGame g, Server s) {
        super(toHit, waa, g, s);
    }

    @Override
    protected int calcDamagePerHit() {
        int[] nRanges = wtype.getRanges(weapon);
        double toReturn = wtype.getDamage(nRange);
        if (game.getOptions().booleanOption("tacops_energy_weapons") && wtype.hasModes()) {
            toReturn = Compute.dialDownDamage(weapon, wtype, nRange);
        }
        if (game.getOptions().booleanOption("tacops_altdmg")) {
            if (nRange <= 1) {
                toReturn++;
            } else if (nRange <= wtype.getMediumRange()) {
            } else if (nRange <= wtype.getLongRange()) {
                toReturn--;
            }
        }
        if ((target instanceof Infantry) && !(target instanceof BattleArmor)) {
            toReturn = Compute.directBlowInfantryDamage(toReturn, bDirect ? toHit.getMoS() / 3 : 0, wtype.getInfantryDamageClass(), ((Infantry) target).isMechanized());
            if (nRange <= nRanges[RangeType.RANGE_SHORT]) {
                toReturn += 3;
            } else if (nRange <= nRanges[RangeType.RANGE_MEDIUM]) {
                toReturn += 2;
            } else {
                toReturn++;
            }
        } else if (bDirect) {
            toReturn = Math.min(toReturn + (toHit.getMoS() / 3), toReturn * 2);
        }
        if (game.getOptions().booleanOption("tacops_range") && (nRange > nRanges[RangeType.RANGE_LONG])) {
            toReturn = (int) Math.floor(toReturn / 2.0);
            toReturn -= 1;
        }
        if (bGlancing) {
            toReturn = (int) Math.floor(toReturn / 2.0);
        }
        return (int) Math.ceil(toReturn);
    }
}
