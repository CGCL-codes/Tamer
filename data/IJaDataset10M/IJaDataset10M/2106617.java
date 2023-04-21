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

public class Chant_PoisonousVine extends Chant_SummonVine {

    public String ID() {
        return "Chant_PoisonousVine";
    }

    public String name() {
        return "Poisonous Vine";
    }

    public String displayText() {
        return "(Poisonous Vine)";
    }

    public int classificationCode() {
        return Ability.ACODE_CHANT | Ability.DOMAIN_PLANTCONTROL;
    }

    public int abstractQuality() {
        return Ability.QUALITY_BENEFICIAL_SELF;
    }

    public int enchantQuality() {
        return Ability.QUALITY_INDIFFERENT;
    }

    protected int canAffectCode() {
        return CAN_MOBS;
    }

    protected int canTargetCode() {
        return 0;
    }

    public MOB determineMonster(MOB caster, int material) {
        MOB victim = caster.getVictim();
        MOB newMOB = CMClass.getMOB("GenMOB");
        int level = adjustedLevel(caster, 0);
        if (level < 1) level = 1;
        newMOB.baseEnvStats().setLevel(level);
        newMOB.baseEnvStats().setAbility(13);
        newMOB.baseCharStats().setMyRace(CMClass.getRace("Vine"));
        String name = "a poisonous vine";
        newMOB.setName(name);
        newMOB.setDisplayText(name + " looks enraged!");
        newMOB.setDescription("");
        CMLib.factions().setAlignment(newMOB, Faction.ALIGN_NEUTRAL);
        Ability A = CMClass.getAbility("Fighter_Rescue");
        A.setProficiency(100);
        newMOB.addAbility(A);
        A = null;
        int classlevel = CMLib.ableMapper().qualifyingClassLevel(caster, this) - CMLib.ableMapper().qualifyingLevel(caster, this);
        switch(classlevel / 5) {
            case 0:
                A = CMClass.getAbility("Poison_BeeSting");
                break;
            case 1:
                A = CMClass.getAbility("Poison_Bloodboil");
                break;
            case 2:
                A = CMClass.getAbility("Poison_Venom");
                break;
            default:
                A = CMClass.getAbility("Poison_Decreptifier");
                break;
        }
        if (A != null) {
            A.setProficiency(100);
            newMOB.addAbility(A);
        }
        newMOB.addBehavior(CMClass.getBehavior("CombatAbilities"));
        newMOB.setVictim(victim);
        newMOB.baseEnvStats().setSensesMask(newMOB.baseEnvStats().sensesMask() | EnvStats.CAN_SEE_DARK);
        newMOB.setLocation(caster.location());
        newMOB.baseEnvStats().setRejuv(Integer.MAX_VALUE);
        newMOB.baseEnvStats().setDamage(6 + (5 * (level / 5)));
        newMOB.baseEnvStats().setAttackAdjustment(10);
        newMOB.baseEnvStats().setArmor(100 - (30 + (level / 2)));
        newMOB.baseCharStats().setStat(CharStats.STAT_GENDER, 'N');
        newMOB.addNonUninvokableEffect(CMClass.getAbility("Prop_ModExperience"));
        newMOB.setMiscText(newMOB.text());
        newMOB.recoverCharStats();
        newMOB.recoverEnvStats();
        newMOB.recoverMaxState();
        newMOB.resetToMaxState();
        newMOB.bringToLife(caster.location(), true);
        CMLib.beanCounter().clearZeroMoney(newMOB, null);
        newMOB.setStartRoom(null);
        CMLib.commands().postFollow(newMOB, caster, true);
        if (newMOB.amFollowing() != caster) caster.tell(newMOB.name() + " seems unwilling to follow you."); else {
            if (newMOB.getVictim() != victim) newMOB.setVictim(victim);
            newMOB.location().showOthers(newMOB, victim, CMMsg.MSG_OK_ACTION, "<S-NAME> start(s) attacking <T-NAMESELF>!");
        }
        return (newMOB);
    }
}
