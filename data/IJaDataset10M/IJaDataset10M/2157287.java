package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class ISLRT15OS extends LRTWeapon {

    /**
     *
     */
    private static final long serialVersionUID = 106526906717711956L;

    /**
     *
     */
    public ISLRT15OS() {
        super();
        techLevel = TechConstants.T_IS_TW_NON_BOX;
        name = "LRT 15 (OS)";
        setInternalName(name);
        addLookupName("IS OS LRT-15");
        addLookupName("ISLRTorpedo15 (OS)");
        addLookupName("IS LRT 15 (OS)");
        addLookupName("ISLRT15OS");
        heat = 5;
        rackSize = 15;
        minimumRange = 6;
        waterShortRange = 7;
        waterMediumRange = 14;
        waterLongRange = 21;
        waterExtremeRange = 28;
        tonnage = 7.5f;
        criticals = 3;
        bv = 27;
        flags = flags.or(F_ONESHOT);
        cost = 87500;
    }
}
