package megamek.common.equip;

import megamek.common.*;
import java.util.Vector;

public class LRMissileType extends MissileType {

    public LRMissileType(int tech, int size, Vector valid_ammo) {
        super(size, valid_ammo);
        this.techType = tech;
        if (tech == TechConstants.T_IS_LEVEL_1) {
            switch(size) {
                case 5:
                    this.tonnage = 2.0f;
                    this.criticals = 1;
                    this.bv = 45;
                    break;
                case 10:
                    this.tonnage = 5.0f;
                    this.criticals = 2;
                    this.bv = 90;
                    break;
                case 15:
                    this.tonnage = 7.0f;
                    this.criticals = 3;
                    this.bv = 126;
                    break;
                case 20:
                    this.tonnage = 10.0f;
                    this.criticals = 5;
                    this.bv = 181;
                    break;
            }
        } else {
            switch(size) {
                case 5:
                    this.tonnage = 1.0f;
                    this.criticals = 1;
                    this.bv = 55;
                    break;
                case 10:
                    this.tonnage = 2.5f;
                    this.criticals = 1;
                    this.bv = 109;
                    break;
                case 15:
                    this.tonnage = 3.5f;
                    this.criticals = 2;
                    this.bv = 164;
                    break;
                case 20:
                    this.tonnage = 5.0f;
                    this.criticals = 4;
                    this.bv = 220;
                    break;
            }
        }
    }

    public WeaponResult setupAttack(Mounted loc, Entity en, Targetable targ) {
        return null;
    }

    public void resolveAttack(WeaponResult wr) {
    }

    public TargetRoll getModifiersFor(Mounted loc, Entity en, Targetable targ) {
        return null;
    }
}
