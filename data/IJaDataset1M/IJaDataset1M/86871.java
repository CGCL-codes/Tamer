package megamek.common.weapons.infantry;

import megamek.common.AmmoType;
import megamek.common.TechConstants;

/**
 * @author Ben Grills
 */
public class InfantrySupportHeavyAutoGrenadeLauncherInfernoWeapon extends InfantryWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantrySupportHeavyAutoGrenadeLauncherInfernoWeapon() {
        super();
        techLevel = TechConstants.T_CLAN_TW;
        name = "Infantry Inferno Heavy Auto Grenade Launcher";
        setInternalName(name);
        addLookupName("InfantryHeavyAutoGrenadeLauncherInferno");
        ammoType = AmmoType.T_NA;
        cost = 4500;
        bv = 2.85;
        flags = flags.or(F_INFERNO).or(F_BALLISTIC).or(F_INF_ENCUMBER).or(F_INF_SUPPORT);
        infantryDamage = 0.65;
        infantryRange = 1;
        crew = 1;
    }
}
