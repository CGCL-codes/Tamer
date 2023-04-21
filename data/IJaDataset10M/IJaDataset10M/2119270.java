package megamek.common.weapons;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class ISThumperCannon extends ArtilleryCannonWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -1951764278554798130L;

    public ISThumperCannon() {
        super();
        techLevel = TechConstants.T_IS_EXPERIMENTAL;
        name = "Thumper Cannon";
        setInternalName("ISThumperCannon");
        addLookupName("ISThumperArtilleryCannon");
        addLookupName("IS Thumper Cannon");
        heat = 5;
        rackSize = 5;
        ammoType = AmmoType.T_THUMPER_CANNON;
        minimumRange = 3;
        shortRange = 4;
        mediumRange = 9;
        longRange = 14;
        extremeRange = 18;
        tonnage = 10f;
        criticals = 7;
        bv = 41;
        cost = 200000;
        shortAV = 5;
        medAV = 5;
        longAV = 5;
        maxRange = RANGE_MED;
    }
}
