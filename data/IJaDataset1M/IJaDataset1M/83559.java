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
public class Prayer_AnimateSkeleton extends Prayer {

    public String ID() {
        return "Prayer_AnimateSkeleton";
    }

    public String name() {
        return "Animate Skeleton";
    }

    public int classificationCode() {
        return Ability.ACODE_PRAYER | Ability.DOMAIN_DEATHLORE;
    }

    public int abstractQuality() {
        return Ability.QUALITY_INDIFFERENT;
    }

    public int enchantQuality() {
        return Ability.QUALITY_INDIFFERENT;
    }

    public long flags() {
        return Ability.FLAG_UNHOLY;
    }

    protected int canTargetCode() {
        return CAN_ITEMS;
    }

    public void makeSkeletonFrom(Room R, DeadBody body, MOB mob, int level) {
        String description = body.mobDescription();
        if (description.trim().length() == 0) description = "It looks dead."; else description += "\n\rIt also looks dead.";
        MOB newMOB = CMClass.getMOB("GenUndead");
        newMOB.setName("a skeleton");
        newMOB.setDescription(description);
        newMOB.setDisplayText("a skeleton is here");
        newMOB.baseEnvStats().setLevel(level + (super.getX1Level(mob) * 2) + super.getXLEVELLevel(mob));
        newMOB.baseCharStats().setStat(CharStats.STAT_GENDER, body.charStats().getStat(CharStats.STAT_GENDER));
        newMOB.baseCharStats().setMyRace(CMClass.getRace("Skeleton"));
        newMOB.baseCharStats().setBodyPartsFromStringAfterRace(body.charStats().getBodyPartsAsString());
        Ability P = CMClass.getAbility("Prop_StatTrainer");
        if (P != null) {
            P.setMiscText("NOTEACH STR=16 INT=10 WIS=10 CON=10 DEX=15 CHA=2");
            newMOB.addNonUninvokableEffect(P);
        }
        newMOB.recoverCharStats();
        newMOB.baseEnvStats().setAttackAdjustment(CMLib.leveler().getLevelAttack(newMOB));
        newMOB.baseEnvStats().setDamage(CMLib.leveler().getLevelMOBDamage(newMOB));
        CMLib.factions().setAlignment(newMOB, Faction.ALIGN_EVIL);
        newMOB.baseState().setHitPoints(15 * newMOB.baseEnvStats().level());
        newMOB.baseState().setMovement(CMLib.leveler().getLevelMove(newMOB));
        newMOB.baseEnvStats().setArmor(CMLib.leveler().getLevelMOBArmor(newMOB));
        newMOB.addNonUninvokableEffect(CMClass.getAbility("Prop_ModExperience"));
        newMOB.baseState().setMana(0);
        Behavior B = CMClass.getBehavior("Aggressive");
        if ((B != null) && (mob != null)) {
            B.setParms("+NAMES \"-" + mob.Name() + "\"");
        }
        if (B != null) newMOB.addBehavior(B);
        newMOB.recoverCharStats();
        newMOB.recoverEnvStats();
        newMOB.recoverMaxState();
        newMOB.resetToMaxState();
        newMOB.text();
        newMOB.bringToLife(R, true);
        CMLib.beanCounter().clearZeroMoney(newMOB, null);
        R.showOthers(newMOB, null, CMMsg.MSG_OK_ACTION, "<S-NAME> appears!");
        int it = 0;
        while (it < R.numItems()) {
            Item item = R.fetchItem(it);
            if ((item != null) && (item.container() == body)) {
                CMMsg msg2 = CMClass.getMsg(newMOB, body, item, CMMsg.MSG_GET, null);
                newMOB.location().send(newMOB, msg2);
                CMMsg msg4 = CMClass.getMsg(newMOB, item, null, CMMsg.MSG_GET, null);
                newMOB.location().send(newMOB, msg4);
                CMMsg msg3 = CMClass.getMsg(newMOB, item, null, CMMsg.MSG_WEAR, null);
                newMOB.location().send(newMOB, msg3);
                if (!newMOB.isMine(item)) it++; else it = 0;
            } else it++;
        }
        body.destroy();
        newMOB.setStartRoom(null);
        R.show(newMOB, null, CMMsg.MSG_OK_VISUAL, "<S-NAME> begin(s) to rise!");
        R.recoverRoomStats();
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        Environmental target = getAnyTarget(mob, commands, givenTarget, Wearable.FILTER_UNWORNONLY);
        if (target == null) return false;
        if (target == mob) {
            mob.tell(target.name() + " doesn't look dead yet.");
            return false;
        }
        if (!(target instanceof DeadBody)) {
            mob.tell("You can't animate that.");
            return false;
        }
        DeadBody body = (DeadBody) target;
        if (body.playerCorpse() || (body.mobName().length() == 0)) {
            mob.tell("You can't animate that.");
            return false;
        }
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, 0, auto);
        if (success) {
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), auto ? "" : "^S<S-NAME> " + prayForWord(mob) + " to animate <T-NAMESELF> as a skeleton.^?");
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                makeSkeletonFrom(mob.location(), body, mob, 1);
            }
        } else return beneficialWordsFizzle(mob, target, "<S-NAME> " + prayForWord(mob) + " to animate <T-NAMESELF>, but fail(s) miserably.");
        return success;
    }
}
