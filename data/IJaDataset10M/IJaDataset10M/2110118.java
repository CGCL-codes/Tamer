package megamek.common.equip;

import megamek.common.*;

public class UACAmmoType extends AmmoType {

    public UACAmmoType(int size, int tech) {
        this.techType = tech;
        this.rackSize = size;
        this.damagePerShot = this.rackSize;
        if (tech == TechConstants.T_IS_LEVEL_2) {
            switch(size) {
                case 2:
                    this.heat = 1;
                    this.range = new RangeType(3, 8, 17, 25);
                    this.shots = 45;
                    this.bv = 7;
                    break;
                case 5:
                    this.heat = 1;
                    this.range = new RangeType(2, 6, 13, 20);
                    this.shots = 20;
                    this.bv = 14;
                    break;
                case 10:
                    this.heat = 4;
                    this.range = new RangeType(6, 12, 18);
                    this.shots = 10;
                    this.bv = 29;
                    break;
                case 20:
                    this.heat = 8;
                    this.range = new RangeType(3, 7, 10);
                    this.shots = 5;
                    this.bv = 32;
                    break;
            }
        } else {
            switch(size) {
                case 2:
                    this.heat = 1;
                    this.range = new RangeType(2, 9, 18, 27);
                    this.shots = 45;
                    this.bv = 6;
                    break;
                case 5:
                    this.heat = 1;
                    this.range = new RangeType(7, 14, 21);
                    this.shots = 20;
                    this.bv = 15;
                    break;
                case 10:
                    this.heat = 3;
                    this.range = new RangeType(6, 12, 18);
                    this.shots = 10;
                    this.bv = 26;
                    break;
                case 20:
                    this.heat = 7;
                    this.range = new RangeType(4, 8, 12);
                    this.shots = 5;
                    this.bv = 35;
                    break;
            }
        }
    }

    protected HitData resolveACAttack(Game game, WeaponResult wr, UsesAmmoType weap, UsesAmmoState weap_state) {
        return null;
    }

    public void resolveAttack(Game game, WeaponResult wr, UsesAmmoType weap, UsesAmmoState weap_state) {
        resolveACAttack(game, wr, weap, weap_state);
    }
}
