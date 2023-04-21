package megamek.common.weapons;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class ISDavidLightGaussRifle extends Weapon {

    /**
     *
     */
    private static final long serialVersionUID = -4247046315958528324L;

    /**
     *
     */
    public ISDavidLightGaussRifle() {
        super();
        techLevel = TechConstants.T_IS_TW_NON_BOX;
        name = "David Light Gauss Rifle";
        setInternalName(name);
        addLookupName("ISDavidLightGaussRifle");
        damage = 1;
        ammoType = AmmoType.T_NA;
        shortRange = 3;
        mediumRange = 5;
        longRange = 8;
        extremeRange = 10;
        bv = 7;
        flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_BALLISTIC);
        cost = 22500;
    }
}
