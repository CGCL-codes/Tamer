package megamek.common.weapons;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class ISTsunamiGaussRifle extends Weapon {

    /**
     *
     */
    private static final long serialVersionUID = -4179313979730970060L;

    /**
     *
     */
    public ISTsunamiGaussRifle() {
        super();
        techLevel = TechConstants.T_IS_TW_NON_BOX;
        name = "Tsunami Heavy Gauss Rifle";
        setInternalName(name);
        addLookupName("BA-ISTsunamiHeavyGaussRifle");
        heat = 0;
        damage = 1;
        ammoType = AmmoType.T_NA;
        shortRange = 2;
        mediumRange = 4;
        longRange = 5;
        extremeRange = 8;
        tonnage = 0.0f;
        criticals = 0;
        bv = 6;
        flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_BALLISTIC);
    }
}
