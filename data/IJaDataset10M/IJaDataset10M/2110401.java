package megamek.common.weapons;

import megamek.common.AmmoType;
import megamek.common.IGame;
import megamek.common.ToHitData;
import megamek.common.actions.WeaponAttackAction;
import megamek.server.Server;

/**
 * @author Sebastian Brocks
 */
public abstract class HAGWeapon extends GaussWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -2890339452762009216L;

    public HAGWeapon() {
        super();
        damage = DAMAGE_VARIABLE;
        ammoType = AmmoType.T_HAG;
        flags = flags.or(F_NO_AIM);
        atClass = CLASS_AC;
        infDamageClass = WEAPON_CLUSTER_BALLISTIC;
    }

    @Override
    protected AttackHandler getCorrectHandler(ToHitData toHit, WeaponAttackAction waa, IGame game, Server server) {
        return new HAGWeaponHandler(toHit, waa, game, server);
    }
}
