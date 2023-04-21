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
public class ISBATaser extends AmmoWeapon {

    /**
     *
     */
    private static final long serialVersionUID = 4393086562754363816L;

    /**
     *
     */
    public ISBATaser() {
        super();
        techLevel = TechConstants.T_IS_ADVANCED;
        name = "Battle Armor Taser";
        setInternalName(name);
        addLookupName("ISBATaser");
        heat = 0;
        rackSize = 1;
        damage = 1;
        ammoType = AmmoType.T_TASER;
        shortRange = 1;
        mediumRange = 2;
        longRange = 3;
        extremeRange = 4;
        bv = 15;
        toHitModifier = 1;
        flags = flags.or(F_BA_WEAPON).or(F_ONESHOT).or(F_TASER);
    }

    @Override
    protected AttackHandler getCorrectHandler(ToHitData toHit, WeaponAttackAction waa, IGame game, Server server) {
        return new BATaserHandler(toHit, waa, game, server);
    }
}
