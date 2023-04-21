package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author BATTLEMASTER
 */
public class ISEnhancedLRM5 extends EnhancedLRMWeapon {

    /**
     *
     */
    private static final long serialVersionUID = 3287950524687857609L;

    /**
     *
     */
    public ISEnhancedLRM5() {
        super();
        techLevel = TechConstants.T_IS_EXPERIMENTAL;
        name = "Enhanced LRM 5";
        setInternalName(name);
        addLookupName("ISEnhancedLRM5");
        heat = 2;
        rackSize = 5;
        minimumRange = 3;
        shortRange = 7;
        mediumRange = 14;
        longRange = 21;
        extremeRange = 28;
        tonnage = 3.0f;
        criticals = 2;
        bv = 52;
        cost = 37500;
        shortAV = 3;
        medAV = 3;
        longAV = 3;
        maxRange = RANGE_LONG;
    }
}
