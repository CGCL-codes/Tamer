package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantryShotgunDoubleBarrelWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryShotgunDoubleBarrelWeapon() {
        super();
        techLevel = TechConstants.T_TW_ALL;
        name = "Double Barrel Shotgun";
        setInternalName(name);
        addLookupName("InfantryDoubleShotgun");
        ammoType = AmmoType.T_NA;
        cost = 30;
        bv = 0.02;
        flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_BALLISTIC);
        infantryDamage = 0.03;
        infantryRange = 0;
    }
}
