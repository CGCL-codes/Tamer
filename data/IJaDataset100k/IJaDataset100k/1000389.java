package com.planet_ink.coffee_mud.Abilities.SuperPowers;

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
public class Power_OctoGrapple extends SuperPower {

    public String ID() {
        return "Power_OctoGrapple";
    }

    public String name() {
        return "Octo-Grapple";
    }

    public String displayText() {
        if (affected == invoker) return "(Grappling)";
        return "(Grappled)";
    }

    private static final String[] triggerStrings = { "GRAPPLE" };

    public int abstractQuality() {
        return Ability.QUALITY_MALICIOUS;
    }

    public String[] triggerStrings() {
        return triggerStrings;
    }

    protected int canAffectCode() {
        return 0;
    }

    protected int canTargetCode() {
        return Ability.CAN_MOBS;
    }

    public int classificationCode() {
        return Ability.ACODE_SKILL;
    }

    public long flags() {
        return Ability.FLAG_BINDING;
    }

    public int usageType() {
        return USAGE_MOVEMENT;
    }

    public boolean okMessage(Environmental myHost, CMMsg msg) {
        if ((affected == null) || (!(affected instanceof MOB))) return true;
        MOB mob = (MOB) affected;
        if ((msg.amISource(mob)) && (!CMath.bset(msg.sourceMajor(), CMMsg.MASK_ALWAYS)) && (mob != invoker)) {
            if ((CMath.bset(msg.sourceMajor(), CMMsg.MASK_EYES)) || (CMath.bset(msg.sourceMajor(), CMMsg.MASK_HANDS)) || (CMath.bset(msg.sourceMajor(), CMMsg.MASK_MOUTH)) || (CMath.bset(msg.sourceMajor(), CMMsg.MASK_MOVE))) {
                if (msg.sourceMessage() != null) {
                    if (mob.location().show(mob, null, CMMsg.MSG_OK_ACTION, "<S-NAME> struggle(s) against the grappling arms.")) {
                        if (CMLib.dice().rollPercentage() < mob.charStats().getStat(CharStats.STAT_STRENGTH)) {
                            unInvoke();
                            if ((mob.fetchEffect(ID()) == null) && (invoker != null) && (invoker != mob)) {
                                Ability A = mob.fetchEffect(ID());
                                if (A != null) A.unInvoke();
                            }
                        }
                    }
                }
                return false;
            }
        }
        return super.okMessage(myHost, msg);
    }

    public void affectEnvStats(Environmental affected, EnvStats affectableStats) {
        super.affectEnvStats(affected, affectableStats);
        affectableStats.setDisposition(affectableStats.sensesMask() | EnvStats.IS_BOUND);
    }

    public void affectCharStats(MOB affected, CharStats affectableStats) {
        super.affectCharStats(affected, affectableStats);
        if (affected == invoker) affectableStats.alterBodypart(Race.BODY_ARM, -2);
    }

    public void unInvoke() {
        if ((affected == null) || (!(affected instanceof MOB))) return;
        MOB mob = (MOB) affected;
        super.unInvoke();
        if (canBeUninvoked()) {
            if ((!mob.amDead()) && (CMLib.flags().isInTheGame(mob, false))) {
                if (mob == invoker) {
                    if (mob.location() != null) mob.location().show(mob, null, CMMsg.MSG_OK_ACTION, "<S-NAME> release(s) <S-HIS-HER> grapple."); else mob.tell("You release your grapple.");
                } else {
                    if (mob.location() != null) mob.location().show(mob, null, CMMsg.MSG_OK_ACTION, "<S-NAME> <S-IS-ARE> released from the grapple"); else mob.tell("You are released from the grapple.");
                }
                CMLib.commands().postStand(mob, true);
            }
        }
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        MOB target = this.getTarget(mob, commands, givenTarget);
        if (target == null) return false;
        if ((!auto) && (mob.baseWeight() < (target.baseWeight() - 200))) {
            mob.tell(target.name() + " is too big to grapple!");
            return false;
        }
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        int levelDiff = target.envStats().level() - (mob.envStats().level() + (2 * super.getXLEVELLevel(mob)));
        if (levelDiff > 0) levelDiff = levelDiff * 10; else levelDiff = 0;
        boolean hit = (auto) || CMLib.combat().rollToHit(mob, target);
        boolean success = proficiencyCheck(mob, (-levelDiff) + (-(((target.charStats().getStat(CharStats.STAT_STRENGTH) - mob.charStats().getStat(CharStats.STAT_STRENGTH)) * 5))), auto) && (hit);
        success = success && (target.charStats().getBodyPart(Race.BODY_ARM) > 2);
        if (success) {
            invoker = mob;
            CMMsg msg = CMClass.getMsg(mob, target, this, CMMsg.MSK_MALICIOUS_MOVE | CMMsg.TYP_JUSTICE | (auto ? CMMsg.MASK_ALWAYS : 0), auto ? "<T-NAME> get(s) grappled!" : "^F^<FIGHT^><S-NAME> grab(s) <T-NAMESELF> with <S-HIS-HER> huge metallic arms!^</FIGHT^>^?");
            CMLib.color().fixSourceFightColor(msg);
            if ((mob.location().okMessage(mob, msg)) && (msg.value() <= 0)) {
                mob.location().send(mob, msg);
                success = maliciousAffect(mob, target, asLevel, 10, -1);
                success = maliciousAffect(mob, mob, asLevel, 10, -1);
            }
        } else return maliciousFizzle(mob, target, "<S-NAME> attempt(s) to grab <T-NAMESELF>, but fail(s).");
        return success;
    }
}
