package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class ISLargeXPulseLaser extends PulseLaserWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -8159582350685114767L;

    /**
     *
     */
    public ISLargeXPulseLaser() {
        super();
        techLevel = TechConstants.T_IS_EXPERIMENTAL;
        name = "Large X-Pulse Laser";
        setInternalName("ISLargeXPulseLaser");
        addLookupName("IS X-Pulse Large Laser");
        addLookupName("IS Large X-Pulse Laser");
        heat = 14;
        damage = 9;
        toHitModifier = -2;
        shortRange = 5;
        mediumRange = 10;
        longRange = 15;
        extremeRange = 20;
        waterShortRange = 2;
        waterMediumRange = 5;
        waterLongRange = 7;
        waterExtremeRange = 10;
        tonnage = 7.0f;
        criticals = 2;
        bv = 178;
        cost = 275000;
        maxRange = RANGE_MED;
        shortAV = 9;
        medAV = 9;
    }
}
