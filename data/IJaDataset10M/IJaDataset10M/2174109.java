package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class ISSRM3OS extends SRMWeapon {

    /**
     *
     */
    private static final long serialVersionUID = 8732407650030864483L;

    /**
     *
     */
    public ISSRM3OS() {
        super();
        techLevel = TechConstants.T_IS_TW_NON_BOX;
        name = "SRM 3 (OS)";
        setInternalName(name);
        addLookupName("ISSRM3OS");
        rackSize = 3;
        shortRange = 3;
        mediumRange = 6;
        longRange = 9;
        extremeRange = 12;
        bv = 6;
        flags = flags.or(F_NO_FIRES).or(F_ONESHOT);
    }
}
