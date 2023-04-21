package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantryPistolSternsnachtPistolWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryPistolSternsnachtPistolWeapon() {
        super();
        techLevel = TechConstants.T_IS_TW_ALL;
        name = "Sternsnacht Heavy Pistol";
        setInternalName(name);
        addLookupName("InfantrySternsnachtpistol");
        addLookupName("InfantryClaymorePistol");
        ammoType = AmmoType.T_AC;
        cost = 200;
        bv = 0.07;
        flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_BALLISTIC);
        infantryDamage = 0.09;
        infantryRange = 0;
    }
}
