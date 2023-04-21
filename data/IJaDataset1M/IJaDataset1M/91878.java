package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantrySupportVLAWWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantrySupportVLAWWeapon() {
        super();
        techLevel = TechConstants.T_TW_ALL;
        name = "Infantry V-LAW";
        setInternalName(name);
        addLookupName("InfantryVeryLightAntitankWeapon");
        addLookupName("InfantryVLAW");
        ammoType = AmmoType.T_NA;
        cost = 75;
        bv = 0.44;
        flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_MISSILE).or(F_INF_SUPPORT);
        infantryDamage = 0.18;
        infantryRange = 1;
        crew = 1;
    }
}
