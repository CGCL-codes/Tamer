package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class ISSmallXPulseLaser extends PulseLaserWeapon {

    /**
     *
     */
    private static final long serialVersionUID = 5322977585378755226L;

    /**
     *
     */
    public ISSmallXPulseLaser() {
        super();
        techLevel = TechConstants.T_IS_EXPERIMENTAL;
        name = "Small X-Pulse Laser";
        setInternalName("ISSmallXPulseLaser");
        addLookupName("IS X-Pulse Small Laser");
        addLookupName("IS Small X-Pulse Laser");
        heat = 3;
        damage = 3;
        toHitModifier = -2;
        shortRange = 2;
        mediumRange = 4;
        longRange = 5;
        extremeRange = 8;
        waterShortRange = 1;
        waterMediumRange = 2;
        waterLongRange = 2;
        waterExtremeRange = 4;
        tonnage = 1.0f;
        criticals = 1;
        bv = 21;
        maxRange = RANGE_SHORT;
        shortAV = 3;
        cost = 31000;
    }
}
