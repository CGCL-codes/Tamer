package megamek.common.weapons;

import megamek.common.IGame;
import megamek.common.TechConstants;
import megamek.common.ToHitData;
import megamek.common.actions.WeaponAttackAction;
import megamek.server.Server;

/**
 * @author Jason Tighe
 */
public class ISBombastLaser extends LaserWeapon {

    /**
     *
     */
    private static final long serialVersionUID = 3379805005243042138L;

    public ISBombastLaser() {
        super();
        techLevel = TechConstants.T_IS_EXPERIMENTAL;
        name = "Bombast Laser";
        setInternalName(name);
        addLookupName("IS Bombast Laser");
        addLookupName("ISBombastLaser");
        heat = 12;
        damage = 12;
        shortRange = 5;
        mediumRange = 10;
        longRange = 15;
        extremeRange = 20;
        waterShortRange = 3;
        waterMediumRange = 6;
        waterLongRange = 9;
        waterExtremeRange = 12;
        tonnage = 7.0f;
        criticals = 3;
        bv = 137;
        cost = 200000;
        shortAV = 12;
        medAV = 12;
        maxRange = RANGE_MED;
        flags = flags.or(F_BOMBAST_LASER);
    }

    @Override
    protected AttackHandler getCorrectHandler(ToHitData toHit, WeaponAttackAction waa, IGame game, Server server) {
        return new BombastLaserWeaponHandler(toHit, waa, game, server);
    }
}
