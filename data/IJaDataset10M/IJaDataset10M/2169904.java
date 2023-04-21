package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class ISLRM15OS extends LRMWeapon {

    /**
     *
     */
    private static final long serialVersionUID = 1089075678687256997L;

    /**
     *
     */
    public ISLRM15OS() {
        super();
        techLevel = TechConstants.T_INTRO_BOXSET;
        name = "LRM 15 (OS)";
        setInternalName(name);
        addLookupName("IS OS LRM-15");
        addLookupName("ISLRM15 (OS)");
        addLookupName("IS LRM 15 (OS)");
        heat = 5;
        rackSize = 15;
        minimumRange = 6;
        tonnage = 7.5f;
        criticals = 3;
        bv = 27;
        flags = flags.or(F_ONESHOT);
        cost = 87500;
        shortAV = 9;
        medAV = 9;
        longAV = 9;
        maxRange = RANGE_LONG;
    }
}
