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
public class Chant_SpellWard extends Chant {

    public String ID() {
        return "Chant_SpellWard";
    }

    public String name() {
        return "Spell Ward";
    }

    public String displayText() {
        return "(Spell Ward)";
    }

    public int abstractQuality() {
        return Ability.QUALITY_BENEFICIAL_SELF;
    }

    protected int canAffectCode() {
        return CAN_MOBS;
    }

    public int classificationCode() {
        return Ability.ACODE_CHANT | Ability.DOMAIN_PRESERVING;
    }

    public void unInvoke() {
        if ((affected == null) || (!(affected instanceof MOB))) return;
        MOB mob = (MOB) affected;
        if (canBeUninvoked()) mob.tell("Your ward against spells fades.");
        super.unInvoke();
    }

    public int castingQuality(MOB mob, Environmental target) {
        if (mob != null) {
            if (target instanceof MOB) {
                MOB victim = ((MOB) target).getVictim();
                if ((victim != null) && (CMLib.flags().domainAbilities(victim, Ability.ACODE_SPELL).size() == 0)) return Ability.QUALITY_INDIFFERENT;
            }
        }
        return super.castingQuality(mob, target);
    }

    public boolean okMessage(Environmental myHost, CMMsg msg) {
        if ((affected == null) || (!(affected instanceof MOB))) return super.okMessage(myHost, msg);
        MOB mob = (MOB) affected;
        if ((msg.amITarget(mob)) && (CMath.bset(msg.targetCode(), CMMsg.MASK_MALICIOUS)) && (msg.targetMinor() == CMMsg.TYP_CAST_SPELL) && (msg.tool() != null) && (msg.tool() instanceof Ability) && ((((Ability) msg.tool()).classificationCode() & Ability.ALL_ACODES) == Ability.ACODE_SPELL) && (invoker != null) && (!mob.amDead()) && (CMLib.dice().rollPercentage() < 35)) {
            mob.location().show(mob, null, CMMsg.MSG_OK_VISUAL, "The ward around <S-NAME> inhibits " + msg.tool().name() + "!");
            return false;
        }
        return super.okMessage(myHost, msg);
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        MOB target = mob;
        if ((auto) && (givenTarget != null) && (givenTarget instanceof MOB)) target = (MOB) givenTarget;
        if (target.fetchEffect(ID()) != null) {
            mob.tell(target, null, null, "<S-NAME> <S-IS-ARE> already affected by " + name() + ".");
            return false;
        }
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, 0, auto);
        if (success) {
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), auto ? "<T-NAME> <T-IS-ARE> protected from spells." : "^S<S-NAME> chant(s) for a ward against spells around <T-NAMESELF>.^?");
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                beneficialAffect(mob, target, asLevel, 0);
            }
        } else beneficialWordsFizzle(mob, target, "<S-NAME> chant(s) for a ward, but nothing happens.");
        return success;
    }
}
