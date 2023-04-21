package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantryArchaicPolearmWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryArchaicPolearmWeapon() {
        super();
        techLevel = TechConstants.T_ALLOWED_ALL;
        name = "Polearm";
        setInternalName(name);
        addLookupName("InfantryPolearm");
        ammoType = AmmoType.T_NA;
        cost = 50;
        bv = 0.03;
        flags = flags.or(F_NO_FIRES).or(F_INF_POINT_BLANK).or(F_INF_ARCHAIC);
        infantryDamage = 0.04;
        infantryRange = 0;
    }
}
