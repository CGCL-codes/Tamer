package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantryPistolMydronAutoPistolWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryPistolMydronAutoPistolWeapon() {
        super();
        techLevel = TechConstants.T_ALLOWED_ALL;
        name = "Mydron Auto Pistol";
        setInternalName(name);
        addLookupName("InfantryMydronAutopistol");
        ammoType = AmmoType.T_AC;
        cost = 100;
        bv = 0.11;
        flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_BALLISTIC);
        infantryDamage = 0.14;
        infantryRange = 0;
    }
}
