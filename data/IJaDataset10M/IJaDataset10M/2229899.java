package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Andrew Hunter
 */
public class ISAC20 extends ACWeapon {

    /**
     *
     */
    private static final long serialVersionUID = 4780847244648362671L;

    /**
     *
     */
    public ISAC20() {
        super();
        techLevel = TechConstants.T_INTRO_BOXSET;
        name = "AC/20";
        setInternalName("Autocannon/20");
        addLookupName("IS Auto Cannon/20");
        addLookupName("Auto Cannon/20");
        addLookupName("AutoCannon/20");
        addLookupName("ISAC20");
        addLookupName("IS Autocannon/20");
        heat = 7;
        damage = 20;
        rackSize = 20;
        shortRange = 3;
        mediumRange = 6;
        longRange = 9;
        extremeRange = 12;
        tonnage = 14.0f;
        criticals = 10;
        bv = 178;
        flags = flags.or(F_SPLITABLE);
        cost = 300000;
        shortAV = 20;
        maxRange = RANGE_SHORT;
        explosionDamage = damage;
    }
}
