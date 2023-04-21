package megamek.common.weapons;

import megamek.common.AmmoType;
import megamek.common.IGame;
import megamek.common.TechConstants;
import megamek.common.ToHitData;
import megamek.common.actions.WeaponAttackAction;
import megamek.server.Server;

/**
 * @author Sebastian Brocks
 */
public abstract class CLPrototypeStreakSRMWeapon extends SRMWeapon {

    /**
     *
     */
    private static final long serialVersionUID = 9157660680598071296L;

    public CLPrototypeStreakSRMWeapon() {
        super();
        ammoType = AmmoType.T_SRM_STREAK;
        toHitModifier = -1;
        techLevel = TechConstants.T_IS_EXPERIMENTAL;
    }

    @Override
    protected AttackHandler getCorrectHandler(ToHitData toHit, WeaponAttackAction waa, IGame game, Server server) {
        return new PrototypeStreakHandler(toHit, waa, game, server);
    }
}
