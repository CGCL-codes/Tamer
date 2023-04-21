package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantryGrenadeMiniWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryGrenadeMiniWeapon() {
        super();
        techLevel = TechConstants.T_TW_ALL;
        name = "Mini Grenades";
        setInternalName(name);
        addLookupName("InfantryMiniGrenade");
        ammoType = AmmoType.T_NA;
        cost = 8;
        bv = 0.15;
        flags = flags.or(F_NO_FIRES).or(F_BALLISTIC).or(F_INF_SUPPORT);
        infantryDamage = 0.19;
        infantryRange = 0;
    }
}
