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
public class Prayer_Etherealness extends Prayer {

    public String ID() {
        return "Prayer_Etherealness";
    }

    public String name() {
        return "Etherealness";
    }

    public String displayText() {
        return "(Etherealness)";
    }

    public int classificationCode() {
        return Ability.ACODE_PRAYER | Ability.DOMAIN_NEUTRALIZATION;
    }

    protected int canAffectCode() {
        return CAN_MOBS;
    }

    protected int canTargetCode() {
        return 0;
    }

    public int abstractQuality() {
        return Ability.QUALITY_BENEFICIAL_SELF;
    }

    public long flags() {
        return Ability.FLAG_HOLY | Ability.FLAG_UNHOLY;
    }

    public void unInvoke() {
        if ((affected == null) || (!(affected instanceof MOB))) return;
        MOB mob = (MOB) affected;
        super.unInvoke();
        if (canBeUninvoked()) if ((mob.location() != null) && (!mob.amDead())) mob.location().show(mob, null, CMMsg.MSG_OK_VISUAL, "<S-NAME> return(s) to material form.");
    }

    public void affectEnvStats(Environmental affected, EnvStats affectableStats) {
        super.affectEnvStats(affected, affectableStats);
        affectableStats.setWeight(0);
        affectableStats.setHeight(-1);
    }

    public boolean okMessage(Environmental myHost, CMMsg msg) {
        if ((affected != null) && (affected instanceof MOB) && (msg.amISource((MOB) affected))) {
            switch(msg.sourceMinor()) {
                case CMMsg.TYP_ENTER:
                case CMMsg.TYP_LEAVE:
                    if ((msg.tool() instanceof Exit) && (((Exit) msg.tool()).hasADoor()) && (!((Exit) msg.tool()).isOpen()) && (msg.source().inventorySize() > 0)) {
                        msg.source().tell("Your corporeal equipment, suspended in your form, will not pass through the door.");
                        return false;
                    }
                    break;
                case CMMsg.TYP_GET:
                case CMMsg.TYP_PUT:
                case CMMsg.TYP_DROP:
                case CMMsg.TYP_HOLD:
                case CMMsg.TYP_WIELD:
                case CMMsg.TYP_WEAR:
                case CMMsg.TYP_REMOVE:
                case CMMsg.TYP_DELICATE_HANDS_ACT:
                case CMMsg.TYP_WITHDRAW:
                case CMMsg.TYP_BORROW:
                case CMMsg.TYP_LOCK:
                case CMMsg.TYP_UNLOCK:
                case CMMsg.TYP_HANDS:
                    msg.source().tell("You fail to manipulate matter in this form.");
                    return false;
                case CMMsg.TYP_KNOCK:
                case CMMsg.TYP_PULL:
                case CMMsg.TYP_PUSH:
                case CMMsg.TYP_OPEN:
                case CMMsg.TYP_CLOSE:
                    msg.source().tell("You fail your attempt to affect matter in this form.");
                    return false;
                case CMMsg.TYP_THROW:
                case CMMsg.TYP_WEAPONATTACK:
                    msg.source().tell("You fail your attempt to affect matter in this form.");
                    msg.source().makePeace();
                    return false;
            }
        }
        return super.okMessage(myHost, msg);
    }

    public int castingQuality(MOB mob, Environmental target) {
        if (mob != null) {
            if (mob.isInCombat()) return Ability.QUALITY_INDIFFERENT;
            if (mob.isMonster()) return Ability.QUALITY_INDIFFERENT;
        }
        return super.castingQuality(mob, target);
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        MOB target = mob;
        if ((auto) && (givenTarget != null) && (givenTarget instanceof MOB)) target = (MOB) givenTarget;
        if (target.fetchEffect(this.ID()) != null) {
            mob.tell(target, null, null, "<S-NAME> <S-IS-ARE> already ethereal.");
            return false;
        }
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, 0, auto);
        if (success) {
            CMMsg msg = CMClass.getMsg(mob, target, this, verbalCastCode(mob, target, auto), auto ? "" : "^S<S-NAME> " + prayWord(mob) + " that <T-NAME> be given an ethereal form.^?");
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                mob.location().show(target, null, CMMsg.MSG_OK_VISUAL, "<S-NAME> shimmer(s) and become(s) ethereal!");
                beneficialAffect(mob, target, asLevel, 3);
            }
        } else return beneficialWordsFizzle(mob, target, "<S-NAME> " + prayWord(mob) + " for a new form, but <S-HIS-HER> plea is not answered.");
        return success;
    }
}
