package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantryPistolTKEnforcerAutoPistolWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryPistolTKEnforcerAutoPistolWeapon() {
        super();
        techLevel = TechConstants.T_IS_TW_ALL;
        name = "TK Enforcer Auto Pistol";
        setInternalName(name);
        addLookupName("InfantryTKEnforcerAutopistol");
        ammoType = AmmoType.T_AC;
        cost = 110;
        bv = 0.16;
        flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_BALLISTIC);
        infantryDamage = 0.20;
        infantryRange = 0;
    }
}
