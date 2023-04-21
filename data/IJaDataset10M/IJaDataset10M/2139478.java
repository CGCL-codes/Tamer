package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class ISMRM40OS extends MRMWeapon {

    /**
     *
     */
    private static final long serialVersionUID = 5383621160269655212L;

    /**
     *
     */
    public ISMRM40OS() {
        super();
        techLevel = TechConstants.T_IS_TW_NON_BOX;
        name = "MRM 40 (OS)";
        setInternalName(name);
        addLookupName("OS MRM-40");
        addLookupName("ISMRM40 (OS)");
        addLookupName("IS MRM 40 (OS)");
        heat = 12;
        rackSize = 40;
        shortRange = 3;
        mediumRange = 8;
        longRange = 15;
        extremeRange = 16;
        tonnage = 12.5f;
        criticals = 7;
        bv = 49;
        flags = flags.or(F_ONESHOT);
        cost = 175000;
        shortAV = 24;
        medAV = 24;
        maxRange = RANGE_MED;
    }
}
