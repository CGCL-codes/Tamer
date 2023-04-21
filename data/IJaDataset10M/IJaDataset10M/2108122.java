package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantryArchaicBlackjackWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryArchaicBlackjackWeapon() {
        super();
        techLevel = TechConstants.T_ALLOWED_ALL;
        name = "Blackjack";
        setInternalName(name);
        addLookupName("InfantryBlackjack");
        addLookupName("InfantrySap");
        ammoType = AmmoType.T_NA;
        cost = 5;
        bv = 0;
        flags = flags.or(F_NO_FIRES).or(F_INF_POINT_BLANK).or(F_INF_NONPENETRATING).or(F_INF_ARCHAIC);
        infantryDamage = 0.05;
        infantryRange = 0;
    }
}
