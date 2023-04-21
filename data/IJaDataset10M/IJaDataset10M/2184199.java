package com.planet_ink.coffee_mud.Abilities.Druid;

import com.planet_ink.coffee_mud.core.interfaces.*;
import com.planet_ink.coffee_mud.core.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.Areas.interfaces.*;
import com.planet_ink.coffee_mud.Behaviors.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.interfaces.*;
import com.planet_ink.coffee_mud.Commands.interfaces.*;
import com.planet_ink.coffee_mud.Common.interfaces.*;
import com.planet_ink.coffee_mud.Exits.interfaces.*;
import com.planet_ink.coffee_mud.Items.interfaces.*;
import com.planet_ink.coffee_mud.Locales.interfaces.*;
import com.planet_ink.coffee_mud.MOBS.interfaces.*;
import com.planet_ink.coffee_mud.Races.interfaces.*;
import java.util.*;

@SuppressWarnings("unchecked")
public class Chant_Shillelagh extends Chant {

    public String ID() {
        return "Chant_Shillelagh";
    }

    public String name() {
        return "Shillelagh";
    }

    public String displayText() {
        return "(Shillelagh)";
    }

    public int classificationCode() {
        return Ability.ACODE_CHANT | Ability.DOMAIN_PLANTCONTROL;
    }

    public int abstractQuality() {
        return Ability.QUALITY_INDIFFERENT;
    }

    protected int canAffectCode() {
        return CAN_ITEMS;
    }

    protected int canTargetCode() {
        return CAN_ITEMS;
    }

    public void affectEnvStats(Environmental affected, EnvStats affectableStats) {
        super.affectEnvStats(affected, affectableStats);
        if (affected == null) return;
        affectableStats.setDisposition(affectableStats.disposition() | EnvStats.IS_BONUS);
        if (affected instanceof Item) affectableStats.setAbility(affectableStats.ability() + 4);
    }

    public void unInvoke() {
        if (canBeUninvoked()) {
            if (((affected != null) && (affected instanceof Item)) && ((((Item) affected).owner() != null) && (((Item) affected).owner() instanceof MOB))) ((MOB) ((Item) affected).owner()).tell("The enchantment on " + ((Item) affected).name() + " fades.");
        }
        super.unInvoke();
    }

    public int castingQuality(MOB mob, Environmental target) {
        if (mob != null) {
            if ((mob.fetchWieldedItem() instanceof Weapon) && ((((Weapon) mob.fetchWieldedItem()).material() & RawMaterial.MATERIAL_MASK) != RawMaterial.MATERIAL_WOODEN) && ((((Weapon) mob.fetchWieldedItem()).material() & RawMaterial.MATERIAL_MASK) != RawMaterial.MATERIAL_VEGETATION) && (mob.fetchWieldedItem().fetchEffect(ID()) == null)) return super.castingQuality(mob, target, Ability.QUALITY_BENEFICIAL_SELF);
        }
        return super.castingQuality(mob, target);
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        Item target = getTarget(mob, mob.location(), givenTarget, commands, Wearable.FILTER_ANY);
        if (target == null) {
            if ((mob.isMonster()) && (mob.fetchWieldedItem() instanceof Weapon) && ((((Weapon) mob.fetchWieldedItem()).material() & RawMaterial.MATERIAL_MASK) != RawMaterial.MATERIAL_WOODEN) && ((((Weapon) mob.fetchWieldedItem()).material() & RawMaterial.MATERIAL_MASK) != RawMaterial.MATERIAL_VEGETATION)) target = mob.fetchWieldedItem(); else return false;
        }
        if (!(target instanceof Weapon)) {
            mob.tell("You can only enchant weapons.");
            return false;
        }
        if (((((Weapon) target).material() & RawMaterial.MATERIAL_MASK) != RawMaterial.MATERIAL_WOODEN) && ((((Weapon) target).material() & RawMaterial.MATERIAL_MASK) != RawMaterial.MATERIAL_VEGETATION)) {
            mob.tell("You cannot enchant this foreign material.");
            return false;
        }
        if (((Weapon) target).fetchEffect(this.ID()) != null) {
            mob.tell(target.name() + " is already enchanted.");
            return false;
        }
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, 0, auto);
        if (success) {
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), auto ? "<T-NAME> appear(s) enchanted!" : "^S<S-NAME> chant(s) to <T-NAMESELF>.^?");
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                beneficialAffect(mob, target, asLevel, 0);
                mob.location().show(mob, target, CMMsg.MSG_OK_VISUAL, "<T-NAME> glow(s)!");
                target.recoverEnvStats();
                mob.recoverEnvStats();
            }
        } else return beneficialWordsFizzle(mob, target, "<S-NAME> chant(s) to <T-NAMESELF>, but nothing happens.");
        return success;
    }
}
