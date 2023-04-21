package com.planet_ink.coffee_mud.Abilities.Prayers;

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
public class Prayer_Doomspout extends Prayer implements DiseaseAffect {

    public String ID() {
        return "Prayer_Doomspout";
    }

    public String name() {
        return "Doomspout";
    }

    public int classificationCode() {
        return Ability.ACODE_PRAYER | Ability.DOMAIN_EVANGELISM;
    }

    public int abstractQuality() {
        return Ability.QUALITY_MALICIOUS;
    }

    public long flags() {
        return Ability.FLAG_UNHOLY;
    }

    public String displayText() {
        return "(Doomspout)";
    }

    protected int canAffectCode() {
        return Ability.CAN_MOBS;
    }

    protected int canTargetCode() {
        return Ability.CAN_MOBS;
    }

    public int difficultyLevel() {
        return 7;
    }

    int plagueDown = 4;

    String godName = "The Demon";

    protected boolean ispoke = false;

    public int abilityCode() {
        return DiseaseAffect.SPREAD_PROXIMITY;
    }

    public boolean tick(Tickable ticking, int tickID) {
        if ((affected == null) || (!(affected instanceof MOB))) return super.tick(ticking, tickID);
        if (!super.tick(ticking, tickID)) return false;
        if ((--plagueDown) <= 0) {
            MOB mob = (MOB) affected;
            plagueDown = 4;
            if (invoker == null) invoker = mob;
            if (mob.location() == null) return false;
            ispoke = false;
            switch(CMLib.dice().roll(1, 12, 0)) {
                case 1:
                    CMLib.commands().postSay(mob, null, "Repent, or " + godName + " will consume your soul!", false, false);
                    break;
                case 2:
                    CMLib.commands().postSay(mob, null, "We are all damned! Hope is forgotten!", false, false);
                    break;
                case 3:
                    CMLib.commands().postSay(mob, null, godName + " has damned us all!", false, false);
                    break;
                case 4:
                    CMLib.commands().postSay(mob, null, "Death is the only way out for us now!", false, false);
                    break;
                case 5:
                    CMLib.commands().postSay(mob, null, "The finger of " + godName + " will destroy all!", false, false);
                    break;
                case 6:
                    CMLib.commands().postSay(mob, null, "The waters will dry! The air will turn cold! Our bodies will fail! We are Lost!", false, false);
                    break;
                case 7:
                    CMLib.commands().postSay(mob, null, "Nothing can save you! Throw yourself on the mercy of " + godName + "!", false, false);
                    break;
                case 8:
                    CMLib.commands().postSay(mob, null, godName + " will show us no mercy!", false, false);
                    break;
                case 9:
                    CMLib.commands().postSay(mob, null, godName + " has spoken! We will all be destroyed!", false, false);
                    break;
                case 10:
                case 11:
                case 12:
                    CMLib.commands().postSay(mob, null, "Our doom is upon us! The end is near!", false, false);
                    break;
            }
            if ((CMLib.flags().canSpeak(mob)) && (ispoke)) {
                MOB target = mob.location().fetchInhabitant(CMLib.dice().roll(1, mob.location().numInhabitants(), -1));
                if ((target != null) && (CMLib.flags().canBeHeardBy(mob, target)) && (target != invoker) && (target != mob) && (target.fetchEffect(ID()) == null)) if (CMLib.dice().rollPercentage() > target.charStats().getSave(CharStats.STAT_SAVE_DISEASE)) {
                    mob.location().show(target, null, CMMsg.MSG_OK_VISUAL, "<S-NAME> look(s) seriously ill!");
                    maliciousAffect(invoker, target, 0, 0, -1);
                } else spreadImmunity(target);
            }
        }
        return true;
    }

    public void affectCharStats(MOB affected, CharStats affectableStats) {
        super.affectCharStats(affected, affectableStats);
        if (affected == null) return;
        if (affectableStats.getStat(CharStats.STAT_INTELLIGENCE) > 3) affectableStats.setStat(CharStats.STAT_INTELLIGENCE, 3);
    }

    public void executeMsg(Environmental myHost, CMMsg msg) {
        super.executeMsg(myHost, msg);
        if ((affected != null) && (msg.source() == affected) && (msg.sourceMinor() == CMMsg.TYP_SPEAK)) ispoke = true;
    }

    public void unInvoke() {
        if ((affected == null) || (!(affected instanceof MOB))) return;
        MOB mob = (MOB) affected;
        super.unInvoke();
        if (canBeUninvoked()) if ((mob.location() != null) && (!mob.amDead())) {
            spreadImmunity(mob);
            mob.location().show(mob, null, CMMsg.MSG_OK_VISUAL, "<S-YOUPOSS> doomspout disease clear up.");
        }
    }

    public int castingQuality(MOB mob, Environmental target) {
        if (mob != null) {
            if (mob.isInCombat()) return Ability.QUALITY_INDIFFERENT;
        }
        return super.castingQuality(mob, target);
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        MOB target = getTarget(mob, commands, givenTarget);
        if (target == null) return false;
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, 0, auto);
        if (success) {
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto) | CMMsg.MASK_MALICIOUS, auto ? "" : "^S<S-NAME> inflict(s) an unholy disease upon <T-NAMESELF>.^?");
            CMMsg msg2 = CMClass.getMsg(mob, target, this, CMMsg.MSK_CAST_MALICIOUS_VERBAL | CMMsg.TYP_DISEASE | (auto ? CMMsg.MASK_ALWAYS : 0), null);
            CMMsg msg3 = CMClass.getMsg(mob, target, this, CMMsg.MSK_CAST_MALICIOUS_VERBAL | CMMsg.TYP_MIND | (auto ? CMMsg.MASK_ALWAYS : 0), null);
            if ((mob.location().okMessage(mob, msg)) && (mob.location().okMessage(mob, msg2)) && (mob.location().okMessage(mob, msg3))) {
                mob.location().send(mob, msg);
                mob.location().send(mob, msg2);
                mob.location().send(mob, msg3);
                if ((msg.value() <= 0) && (msg2.value() <= 0) && (msg3.value() <= 0)) {
                    invoker = mob;
                    if (mob.getWorshipCharID().length() > 0) godName = mob.getWorshipCharID();
                    maliciousAffect(mob, target, asLevel, 0, -1);
                    mob.location().show(target, null, CMMsg.MSG_OK_VISUAL, "<S-NAME> look(s) seriously ill!");
                } else spreadImmunity(target);
            }
        } else return maliciousFizzle(mob, target, "<S-NAME> attempt(s) to inflict a disease upon <T-NAMESELF>, but flub(s) it.");
        return success;
    }
}
