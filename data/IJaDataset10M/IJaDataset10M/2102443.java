package megamek.common.weapons;

import megamek.common.IGame;
import megamek.common.ToHitData;
import megamek.common.actions.WeaponAttackAction;
import megamek.server.Server;

/**
 * @author Jason Tighe
 */
public class VariableSpeedPulseLaserWeapon extends LaserWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -731162221147163665L;

    public VariableSpeedPulseLaserWeapon() {
        super();
        flags = flags.or(F_PULSE);
        atClass = CLASS_PULSE_LASER;
        infDamageClass = WEAPON_PULSE;
    }

    @Override
    protected AttackHandler getCorrectHandler(ToHitData toHit, WeaponAttackAction waa, IGame game, Server server) {
        return new VariableSpeedPulseLaserWeaponHandler(toHit, waa, game, server);
    }

    @Override
    public int getDamage(int range) {
        if (range <= shortRange) {
            return damageShort;
        }
        if (range <= mediumRange) {
            return damageMedium;
        }
        return damageLong;
    }
}
