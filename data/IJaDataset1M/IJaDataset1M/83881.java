package megamek.common.weapons;

/**
 * @author Jason Tighe
 */
public class CLMediumChemicalLaser extends CLChemicalLaserWeapon {

    /**
     *
     */
    private static final long serialVersionUID = 322396740172378519L;

    public CLMediumChemicalLaser() {
        name = "Medium Chem Laser";
        setInternalName("CLMediumChemicalLaser");
        setInternalName("CLMediumChemLaser");
        heat = 2;
        damage = 5;
        rackSize = 5;
        shortRange = 3;
        mediumRange = 6;
        longRange = 9;
        extremeRange = 12;
        waterShortRange = 2;
        waterMediumRange = 4;
        waterLongRange = 6;
        waterExtremeRange = 8;
        tonnage = 1.0f;
        criticals = 1;
        bv = 37;
        cost = 30000;
        shortAV = 5;
        maxRange = RANGE_SHORT;
    }
}
