package megamek.common.weapons;

/**
 * @author Andrew Hunter
 */
public class CLUAC10Prototype extends CLPrototypeUACWeapon {

    /**
     *
     */
    private static final long serialVersionUID = 6937673199956551674L;

    /**
     *
     */
    public CLUAC10Prototype() {
        super();
        name = "Ultra AC/10 (CP)";
        setInternalName("CLUltraAC10Prototype");
        heat = 4;
        damage = 10;
        rackSize = 10;
        shortRange = 6;
        mediumRange = 12;
        longRange = 18;
        extremeRange = 24;
        tonnage = 13.0f;
        criticals = 8;
        bv = 210;
        cost = 320000;
        shortAV = 15;
        medAV = 15;
        maxRange = RANGE_MED;
        explosionDamage = damage;
    }
}
