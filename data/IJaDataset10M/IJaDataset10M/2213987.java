package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class CLAdvancedSRM4OS extends AdvancedSRMWeapon {

    /**
     *
     */
    private static final long serialVersionUID = 1382352551382640865L;

    /**
     *
     */
    public CLAdvancedSRM4OS() {
        super();
        techLevel = TechConstants.T_CLAN_TW;
        name = "Advanced SRM 4 (OS)";
        setInternalName("CLAdvancedSRM4OS");
        rackSize = 4;
        shortRange = 4;
        mediumRange = 8;
        longRange = 12;
        extremeRange = 16;
        bv = 12;
        flags = flags.or(F_NO_FIRES).or(F_ONESHOT);
        cost = 30000;
    }
}
