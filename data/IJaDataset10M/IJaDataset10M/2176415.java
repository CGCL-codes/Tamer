package megamek.common.equip;

import megamek.common.*;

public class ArmorPiercingACAmmoType extends ACAmmoType {

    public ArmorPiercingACAmmoType(int size) {
        super(size);
        this.techType = TechConstants.T_IS_LEVEL_2;
        this.shots = this.shots / 2;
    }

    public TargetRoll getModifiersFor(Game game, Entity en, Targetable targ) {
        TargetRoll tr = super.getModifiersFor(game, en, targ);
        tr.addModifier(1, "AP rounds");
        return tr;
    }

    public void resolveAttack(Game game, WeaponResult wr, UsesAmmoType weap, UsesAmmoState weap_state) {
        HitData hd = resolveACAttack(game, wr, weap, weap_state);
    }
}
