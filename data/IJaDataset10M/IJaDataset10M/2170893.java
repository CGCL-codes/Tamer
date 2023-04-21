package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantryArchaicNunchakuWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryArchaicNunchakuWeapon() {
        super();
        techLevel = TechConstants.T_ALLOWED_ALL;
        name = "Nunchaku";
        setInternalName(name);
        addLookupName("InfantryNunchaku");
        ammoType = AmmoType.T_NA;
        cost = 10;
        bv = 0.02;
        flags = flags.or(F_NO_FIRES).or(F_INF_NONPENETRATING).or(F_INF_POINT_BLANK).or(F_INF_ARCHAIC);
        infantryDamage = 0.02;
        infantryRange = 0;
    }
}
