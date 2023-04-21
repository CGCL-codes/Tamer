package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantryShotgunBuccaneerGelGunWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryShotgunBuccaneerGelGunWeapon() {
        super();
        techLevel = TechConstants.T_IS_TW_ALL;
        name = "Buccaneer Gel Gun";
        setInternalName(name);
        addLookupName("InfantryBuccaneerGelGun");
        ammoType = AmmoType.T_NA;
        cost = 200;
        bv = 0.06;
        flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_BALLISTIC);
        infantryDamage = 0.07;
        infantryRange = 0;
    }
}
