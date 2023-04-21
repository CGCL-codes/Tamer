package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Sebastian Brocks
 */
public class ISLRM1 extends LRMWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -5976936994611000430L;

    /**
     *
     */
    public ISLRM1() {
        super();
        techLevel = TechConstants.T_IS_TW_NON_BOX;
        name = "LRM 1";
        setInternalName(name);
        addLookupName("IS LRM-1");
        addLookupName("ISLRM1");
        addLookupName("IS LRM 1");
        rackSize = 1;
        minimumRange = 6;
        bv = 14;
    }
}
